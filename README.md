# AutoFlow

Este repositorio agrupa la infraestructura y los proyectos necesarios para un **entorno CI/CD** completo basado en **Jenkins** orquestado con **Docker Compose**, e integra calidad de código, pruebas automatizadas y despliegue en la nube.

## ¿Qué incluye?

- **Contenedores**  
  - `jenkins`: servidor Jenkins LTS  
  - `agent-spring`, `agent-node`, `agent-dotnet`: agentes especializados para cada stack  
  - `sonarqube` + `postgres`: análisis de calidad de código  
  - `selenoid`: entorno de pruebas E2E con navegadores en contenedores  

- **Proyectos de ejemplo**  
  - **Spring Boot** (`SpringPMA/`)  
  - **Node.js** (`TodoListNodeJs/`)  
  - **.NET 8** (`TiendaNET/`)  

- **Plugin**  
  - `SlackNotificationPlugin/`: plugin Jenkins para notificaciones a Slack  
 

- **Orquestación**  
  - `DockerContainers/docker-compose.yml`: define y arranca todo el stack de servicios de la infraestructura
  - `DockerContainers/docker-compose-selenoid.yml`: define y arranca todo el stack de servicios de selenoid
