# --- Etapa 1: Construcción (Build Stage) ---
# CAMBIO: Usamos una imagen de Maven (que ya incluye Java 17)
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app
# Copiamos todo el código del proyecto
COPY . .
# Ejecutamos Maven para construir el .jar
RUN mvn package -DskipTests

# --- Etapa 2: Imagen Final (Runtime Stage) ---
# Esta etapa se queda igual, usamos la imagen liviana
FROM eclipse-temurin:17-jre-focal

# Exponemos el puerto
EXPOSE 8080

# Copiamos el .jar que se creó en la Etapa 1
COPY --from=builder /app/target/*.jar app.jar

# El comando para arrancar la app
ENTRYPOINT ["java","-jar","/app.jar"]