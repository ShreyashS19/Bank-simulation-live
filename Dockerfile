# -------- Step 1: Build backend (Java 22)
FROM maven:3.9.6-eclipse-temurin-22 AS backend-build
WORKDIR /app/backend
COPY backend/. .
RUN mvn clean package -DskipTests

# -------- Step 2: Build frontend (React + Vite)
FROM node:20 AS frontend-build
WORKDIR /app/frontend
COPY frontend/. .
# Set the API URL for production build
ARG VITE_API_URL=https://bank-backend-edsh.onrender.com/api
ENV VITE_API_URL=${VITE_API_URL}
RUN npm install
RUN npm run build

# -------- Step 3: Runtime image
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copy backend JAR
COPY --from=backend-build /app/backend/target/*-jar-with-dependencies.jar app.jar

# Copy frontend build (optional - if serving static files from backend)
COPY --from=frontend-build /app/frontend/dist ./frontend

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
