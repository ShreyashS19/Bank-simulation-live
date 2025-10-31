# ============================
# 🏗️ Step 1: Build Backend (Java 22)
# ============================
FROM maven:3.9.6-eclipse-temurin-22 AS backend-build
WORKDIR /app/backend

# Copy only backend code
COPY backend/. .

# Package with all dependencies into a single executable JAR
RUN mvn clean package -DskipTests

# ============================
# ⚛️ Step 2: Build Frontend (React + Vite)
# ============================
FROM node:18 AS frontend-build
WORKDIR /app/frontend

# Copy frontend code and build it
COPY frontend/. .
RUN npm install
RUN npm run build

# ============================
# 🚀 Step 3: Create Final Runtime Image
# ============================
FROM eclipse-temurin:22-jre
WORKDIR /app

# Copy the executable JAR built by Maven
# NOTE: Adjust the filename pattern if your jar name differs
COPY --from=backend-build /app/backend/target/*-jar-with-dependencies.jar app.jar

# Copy frontend build (for static file hosting or separate service)
COPY --from=frontend-build /app/frontend/dist ./frontend

# Expose port 8080 for Render / browser access
EXPOSE 8080

# Default command to run the backend
ENTRYPOINT ["java", "-jar", "app.jar"]
