## 🧠 Spring Boot Database Configuration Cheatsheet

### 🧩 1. Basic Structure (`application.yml`)
```yaml
spring:
  application:
    name: my-service
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: user
    password: pass
    driver-class-name: org.postgresql.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
server:
  port: 8080
```

---

### 🐘 2. PostgreSQL
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dbname
    username: postgres
    password: secret
    driver-class-name: org.postgresql.Driver
  jpa:
    database: POSTGRESQL
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

---

### 🪶 3. MySQL
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/dbname?useSSL=false&serverTimezone=UTC
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    database: MYSQL
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

---

### ⚡ 4. H2 (in-memory, for tests)
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    database-platform: org.hibernate.dialect.H2Dialect
```

---

### 🍃 5. MongoDB (NoSQL)
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/mydb
      username: mongoUser
      password: mongoPass
```

---

### ☁️ 6. External Cloud Database (Neon / Atlas / Cloud SQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://ep-coolhost.us-east-1.aws.neon.tech/mydb
    username: youruser
    password: yourpass
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
```

---

### 🔐 7. Using `.env` Variables
`.env`:
```
DB_URL=jdbc:postgresql://localhost:5432/orderdb
DB_USER=xl33zy
DB_PASS=xl33zy
```

`application.yml`:
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASS}
```
> 💡 IDE might not show `.env`, but Spring will inject it if it’s set in environment or run configs.

---

### 🧩 8. Multi-Profile Setup
`application.yml`
```yaml
spring:
  profiles:
    active: dev
```

`application-dev.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devdb
```

`application-prod.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://prod-host:5432/proddb
```

---

### 🚀 9. Quick DB Switch
```yaml
# spring.profiles.active=postgres
# spring.profiles.active=h2
```

---

### ⚙️ 10. Default Value Fallback
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/defaultdb}
```

