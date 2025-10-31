# -------- Step 1: Build backend (Java 22)
FROM maven:3.9.6-eclipse-temurin-22 AS backend-build
WORKDIR /app/backend
COPY backend/. .
RUN mvn clean package -DskipTests

# -------- Step 2: Build frontend (React Vite)
FROM node:18 AS frontend-build
WORKDIR /app/frontend
COPY frontend/. .
RUN npm install
RUN npm run build

# -------- Step 3: Combine backend + frontend into final image
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copy backend jar
COPY --from=backend-build /app/backend/target/*.war app.war

# Copy frontend build to serve as static files (optional if using separate hosting)
COPY --from=frontend-build /app/frontend/dist ./frontend

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
