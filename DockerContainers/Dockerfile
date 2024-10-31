FROM jenkins/jenkins:lts

USER root

# Instalar Docker Compose
RUN apt-get update && \
    apt-get install -y curl && \
    curl -L "https://github.com/docker/compose/releases/download/v2.19.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# Verificar la instalación
RUN docker-compose --version
