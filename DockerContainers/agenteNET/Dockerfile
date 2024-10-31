# Usa la imagen base jenkins/ssh-agent:bullseye-jdk17
FROM jenkins/ssh-agent:bullseye-jdk17

# Cambia al usuario root para instalar Docker y .NET
USER root

# Instala Docker y sus componentes necesarios
RUN apt-get update && \
    apt-get install -y apt-transport-https ca-certificates curl gnupg2 software-properties-common && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get install -y docker-ce docker-ce-cli docker-buildx-plugin docker-compose-plugin && \
    apt-get install --yes libcurl4 && \
    curl -L "https://github.com/docker/compose/releases/download/v2.19.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose && \
    curl -sL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor -o /etc/apt/trusted.gpg.d/microsoft.gpg && \
    echo "deb [arch=amd64] https://packages.microsoft.com/repos/azure-cli/ bullseye main" > /etc/apt/sources.list.d/azure-cli.list && \
    apt-get update && \
    apt-get install -y azure-cli

# Añade el usuario jenkins al grupo docker
RUN usermod -aG docker jenkins

# Agregar el usuario Jenkins al grupo sudo sin contraseña
RUN echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Instala wget
RUN apt-get install -y wget

# Instala .NET SDK
RUN wget https://packages.microsoft.com/config/debian/11/packages-microsoft-prod.deb -O packages-microsoft-prod.deb && \
    dpkg -i packages-microsoft-prod.deb && \
    rm packages-microsoft-prod.deb && \
    apt-get update && \
    apt-get install -y dotnet-sdk-8.0

RUN dotnet tool install --global dotnet-sonarscanner

ENV PATH="$PATH:/root/.dotnet/tools"

# Volver al usuario Jenkins
USER jenkins

