
# Auth Application

---

## Prerequisites
- Java 11+ installed
- Maven installed
- Docker and Docker Compose installed and running

---

## Build and Run Instructions

1. **Build the project:**

   ```bash
   mvn clean install
   ```

2. **Stop and remove existing Docker containers, volumes, and images (optional but recommended on first run):**

   ```bash
   docker-compose down --volumes --rmi all
   ```

3. **Start the application:**

   Put your email username, password, host and port to file compose.yaml.
   Please make sure Auth app has been already started!!!

   ```bash
   docker-compose up -d
   ```

   This will start all necessary services in detached mode.

---