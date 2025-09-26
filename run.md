# ▶️ Instrucciones de Ejecución - Inventory Prototype Service

Este documento describe cómo levantar el microservicio de inventario en **local** y en **Docker**.

---

## 🔧 Requisitos Previos
- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.9+](https://maven.apache.org/install.html)
- [Docker](https://docs.docker.com/get-docker/) (opcional, solo para levantar en contenedor)

---

## 🧪 1. Ejecución Local con SQLite

### Compilar el proyecto
```bash
mvn clean install
```
Levantar el servicio
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
Esto iniciará el servicio en:

```bash
http://localhost:8081/api/inventory
```
📂 Se creará un archivo inventory.db en la raíz del proyecto como base de datos SQLite.
## 🧪 2. Ejecución en Modo Test con H2
Ideal para correr pruebas unitarias e integración.

```bash
mvn test -Dspring.profiles.active=test
```

Esto usa una base en memoria (H2) que se crea y destruye en cada ejecución.

## 🐳 3. Ejecución en Docker
Construir imagen
```bash
docker build -t inventory-service .
```

Ejecutar contenedor
```bash
docker run -p 8081:8081 inventory-service
```

El servicio quedará disponible en:
```bash
http://localhost:8081/api/inventory
```
## 📡 Endpoints Disponibles
GET /api/inventory → Listar todo el inventario

GET /api/inventory/{sku} → Consultar un ítem por SKU

POST /api/inventory?sku=...&name=...&quantity=... → Crear o actualizar stock

POST /api/inventory/{sku}/reserve?quantity=... → Reservar stock

POST /api/inventory/{sku}/release?quantity=... → Liberar stock

## 📊 Observabilidad
Actuator habilitado:

Health → http://localhost:8080/actuator/health

Info → http://localhost:8080/actuator/info

## 🧪 Probar con cURL
### Crear stock
```bash
curl -X POST "http://localhost:8080/api/inventory?sku=SKU123&name=Laptop&quantity=10"
```
### Listar inventario
```bash
curl -X GET "http://localhost:8080/api/inventory"
```
### Reservar stock
```bash
curl -X POST "http://localhost:8080/api/inventory/SKU123/reserve?quantity=3"
```
### Liberar stock
```bash
curl -X POST "http://localhost:8080/api/inventory/SKU123/release?quantity=2"
```
## ✅ Notas
Usa application-local.yml para SQLite.
Usa application-test.yml para H2.
Usa application-docker.yml dentro de contenedor.