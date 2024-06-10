# Definindo o provedor AWS
provider "aws" {
  region = "us-west-1"
}

provider "kubernetes" {
  host                   = aws_eks_cluster.pix_cluster.endpoint
  cluster_ca_certificate = base64decode(aws_eks_cluster.pix_cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.pix.token
}

# Obtém as zonas de disponibilidade disponíveis na região especificada
data "aws_availability_zones" "available" {}

# ----------- -----------

# Cria um VPC para hospedar nosso cluster EKS
resource "aws_vpc" "pix_vpc" {
  cidr_block = "10.0.0.0/16"
}

# Cria duas sub-redes públicas
resource "aws_subnet" "public" {
  count                   = 2
  vpc_id                  = aws_vpc.pix_vpc.id
  cidr_block              = cidrsubnet(aws_vpc.pix_vpc.cidr_block, 8, count.index)
  availability_zone       = element(data.aws_availability_zones.available.names, count.index)
  map_public_ip_on_launch = true
}

# Cria o papel IAM para o cluster EKS
resource "aws_iam_role" "eks_role" {
  name = "eks_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action    = "sts:AssumeRole",
      Effect    = "Allow",
      Principal = {
        Service = "eks.amazonaws.com"
      }
    }]
  })
}

# Anexa a política necessária ao papel IAM do cluster EKS
resource "aws_iam_role_policy_attachment" "eks_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.eks_role.name
}

# Cria o cluster EKS
resource "aws_eks_cluster" "pix_cluster" {
  name     = "pix-cluster"
  role_arn = aws_iam_role.eks_role.arn

  vpc_config {
    subnet_ids = aws_subnet.public.*.id
  }
}

# Cria o papel IAM para os nós do EKS
resource "aws_iam_role" "eks_node_group_role" {
  name = "eks_node_group_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action    = "sts:AssumeRole",
      Effect    = "Allow",
      Principal = {
        Service = "ec2.amazonaws.com"
      }
    }]
  })
}

# Anexa as políticas necessárias ao papel IAM dos nós do EKS
resource "aws_iam_role_policy_attachment" "eks_node_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.eks_node_group_role.name
}

resource "aws_iam_role_policy_attachment" "eks_cni_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSCNIPolicy"
  role       = aws_iam_role.eks_node_group_role.name
}

resource "aws_iam_role_policy_attachment" "eks_ecr_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.eks_node_group_role.name
}

# Cria um grupo de nós no EKS
resource "aws_eks_node_group" "pix_node_group" {
  cluster_name    = aws_eks_cluster.pix_cluster.name
  node_group_name = "pix-node-group"
  node_role_arn   = aws_iam_role.eks_node_group_role.arn
  subnet_ids      = aws_subnet.public.*.id

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  instance_types = ["t3.medium"]

  remote_access {
    ec2_ssh_key = "my-key"
  }

  depends_on = [
    aws_eks_cluster.pix_cluster
  ]
}

# ---------- MYSQL ----------
# Cria um grupo de segurança para o MySQL
resource "aws_security_group" "default" {
  vpc_id = aws_vpc.pix_vpc.id

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Cria uma instância do MySQL
resource "aws_db_instance" "pix_mysql" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "8.0"
  instance_class       = "db.t3.micro"
  name                 = "pixdb"
  username             = "user"
  password             = "password"
  parameter_group_name = "default.mysql8.0"
  skip_final_snapshot  = true
  publicly_accessible  = true
  vpc_security_group_ids = [aws_security_group.default.id]
}

# ---------- MYSQL ----------
# Cria um cluster MSK para o Kafka
resource "aws_msk_cluster" "pix_kafka" {
  cluster_name           = "pix-cluster"
  kafka_version          = "2.8.0"
  number_of_broker_nodes = 2

  broker_node_group_info {
    instance_type   = "kafka.t3.small"
    client_subnets  = aws_subnet.public.*.id
    ebs_volume_size = 10
    security_groups = [aws_security_group.default.id]
  }

  encryption_info {
    encryption_in_transit {
      client_broker = "TLS"
      in_cluster    = true
    }
  }
}

# ----------- Aplicar Manifestos com Terraform -----------
resource "kubernetes_manifest" "pix_produtor" {
  manifest = yamlencode({
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "pix-produtor"
    }
    spec = {
      replicas = 1
      selector = {
        matchLabels = {
          app = "pix-produtor"
        }
      }
      template = {
        metadata = {
          labels = {
            app = "pix-produtor"
          }
        }
        spec = {
          containers = [{
            name  = "pix-produtor"
            image = "your-docker-repo/pix-produtor:latest"
            ports = [{
              containerPort = 8082
            }]
          }]
        }
      }
    }
  })
}

resource "kubernetes_manifest" "pix_consumidor" {
  manifest = yamlencode({
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "pix-consumidor"
    }
    spec = {
      replicas = 1
      selector = {
        matchLabels = {
          app = "pix-consumidor"
        }
      }
      template = {
        metadata = {
          labels = {
            app = "pix-consumidor"
          }
        }
        spec = {
          containers = [{
            name  = "pix-consumidor"
            image = "your-docker-repo/pix-consumidor:latest"
            ports = [{
              containerPort = 8081
            }]
          }]
        }
      }
    }
  })
}

resource "kubernetes_manifest" "pix_key" {
  manifest = yamlencode({
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "pix-key"
    }
    spec = {
      replicas = 1
      selector = {
        matchLabels = {
          app = "pix-key"
        }
      }
      template = {
        metadata = {
          labels = {
            app = "pix-key"
          }
        }
        spec = {
          containers = [{
            name  = "pix-key"
            image = "your-docker-repo/pix-key:latest"
            ports = [{
              containerPort = 8083
            }]
          }]
        }
      }
    }
  })
}
