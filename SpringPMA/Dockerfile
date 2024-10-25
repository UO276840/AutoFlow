# Usar una imagen base de Maven para construir el proyecto
FROM maven:3.8.4-openjdk-17 AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y el resto de los archivos del proyecto al contenedor
COPY pom.xml .
COPY src ./src
COPY src/main/resources/templates /app/templates
COPY src/main/resources/static /app/static

# Construir el proyecto usando Maven
RUN mvn clean package -DskipTests

# Usar una imagen base de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado en la fase de construcción al contenedor
COPY --from=build /app/target/TFG_Pipeline-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
