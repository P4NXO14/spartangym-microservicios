# Usa Java 21 ligero como base
FROM eclipse-temurin:21-jdk-alpine

# Crea un punto de montaje
VOLUME /tmp

# Copia el archivo .jar compilado al contenedor
COPY target/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]