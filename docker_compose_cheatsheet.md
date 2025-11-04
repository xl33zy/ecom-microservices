## 🐳 Docker Compose Cheatsheet

### 🧩 1. Basic Structure

```yaml
version: '3.9'
services:
  app:
    build: ../../Downloads
    container_name: my-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    depends_on:
      - db
  db:
    image: postgres:14
    container_name: postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```

---

### 🐘 2. PostgreSQL + Adminer
```yaml
version: '3.9'
services:
  postgres:
    image: postgres:14
    container_name: postgres
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  adminer:
    image: adminer
    container_name: adminer
    ports:
      - "8081:8080"
volumes:
  pgdata:
```

---

### 🪶 3. MySQL + phpMyAdmin
```yaml
version: '3.9'
services:
  mysql:
    image: mysql:8
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: mydb
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - mysqldata:/var/lib/mysql

  phpmyadmin:
    image: phpmyadmin/phpmyadmin
    container_name: phpmyadmin
    ports:
      - "8081:80"
    environment:
      PMA_HOST: mysql
volumes:
  mysqldata:
```

---

### ⚡ 4. MongoDB + Mongo Express
```yaml
version: '3.9'
services:
  mongo:
    image: mongo:latest
    container_name: mongodb
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: password
    ports:
      - "27017:27017"
    volumes:
      - mongodata:/data/db

  mongo-express:
    image: mongo-express
    container_name: mongo-express
    ports:
      - "8081:8081"
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: root
      ME_CONFIG_MONGODB_ADMINPASSWORD: password
      ME_CONFIG_MONGODB_SERVER: mongo
volumes:
  mongodata:
```

---

### ☁️ 5. Redis + RedisInsight
```yaml
version: '3.9'
services:
  redis:
    image: redis:latest
    container_name: redis
    ports:
      - "6379:6379"
    volumes:
      - redisdata:/data

  redisinsight:
    image: redis/redisinsight:latest
    container_name: redisinsight
    ports:
      - "5540:5540"
volumes:
  redisdata:
```

---

### 🧠 6. Spring Boot App + PostgreSQL
```yaml
version: '3.9'
services:
  order-service:
    image: openjdk:17-jdk-slim
    container_name: order-service
    working_dir: /app
    volumes:
      - ./:/app
    command: ["java", "-jar", "target/order-service.jar"]
    ports:
      - "8083:8083"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/orderdb
      SPRING_DATASOURCE_USERNAME: xl33zy
      SPRING_DATASOURCE_PASSWORD: xl33zy
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    depends_on:
      - postgres

  postgres:
    image: postgres:14
    container_name: postgres
    environment:
      POSTGRES_DB: orderdb
      POSTGRES_USER: xl33zy
      POSTGRES_PASSWORD: xl33zy
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```

---

### 📬 7. RabbitMQ (with management UI)
```yaml
version: '3.9'
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"   # Main connection port
      - "15672:15672" # Management UI
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
```

---

### 📡 8. Kafka + Zookeeper
```yaml
version: '3.9'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: localhost
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    depends_on:
      - zookeeper
```

---

### 📊 9. Prometheus + Grafana
```yaml
version: '3.9'
services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml

  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=admin
    depends_on:
      - prometheus
```

---

### 🧩 10. Networks (for multi-container projects)
```yaml
version: '3.9'
services:
  backend:
    build: ./backend
    networks:
      - app-net
  db:
    image: postgres:14
    networks:
      - app-net
networks:
  app-net:
    driver: bridge
```

---

### 🚀 11. Useful Commands
```bash
docker compose up -d        # Run in background
docker compose down         # Stop and remove containers
docker compose logs -f      # Stream logs
docker compose exec db bash # Access container shell
docker volume ls            # List volumes
```

---

### 💡 12. Environment Variables (.env)
`.env`:
```
POSTGRES_USER=xl33zy
POSTGRES_PASSWORD=xl33zy
POSTGRES_DB=orderdb
```

`docker-compose.yml`:
```yaml
environment:
  POSTGRES_USER: ${POSTGRES_USER}
  POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
  POSTGRES_DB: ${POSTGRES_DB}
```

