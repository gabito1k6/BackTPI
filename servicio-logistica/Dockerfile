# --- Etapa 1: Construcción (Build Stage) ---
FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# --- Etapa 2: Imagen Final (Runtime Stage) ---
FROM eclipse-temurin:17-jre-focal

# Exponemos el puerto
EXPOSE 8082

# Copiamos el .jar que se creó en la Etapa 1
COPY --from=builder /app/target/*.jar app.jar

# El comando para arrancar la app
ENTRYPOINT ["java","-jar","/app.jar"]