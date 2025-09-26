# Etapa de compilación
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo los archivos necesarios primero (para caching eficiente)
COPY pom.xml mvnw* ./
COPY .mvn .mvn
RUN mvn -B dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn -B -DskipTests clean package

# Etapa de ejecución (imagen más ligera con solo JRE)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Crear un volumen para persistencia de SQLite
VOLUME /data

# Copiar el JAR desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno opcionales
ENV JAVA_OPTS=""

# Comando de arranque
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]