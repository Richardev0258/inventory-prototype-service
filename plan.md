# 📑 Plan del Proyecto - Inventory Prototype Service

Este documento describe las decisiones técnicas, la arquitectura propuesta y la estrategia seguida en el desarrollo del microservicio de gestión de inventario.

---

## 🎯 Objetivo
Diseñar y prototipar un sistema de inventario distribuido que reduzca la latencia de sincronización, mejore la consistencia de los datos y facilite la observabilidad y la escalabilidad.

---

## 🏗️ Arquitectura Propuesta

- **Microservicio independiente**: El inventario se gestiona como un servicio separado, con su propia base de datos.
- **Persistencia**:
    - **SQLite** en entorno local: ligera y sin dependencias externas.
    - **H2 en memoria** para pruebas: rápida, aislada y autodestructiva al finalizar los tests.
- **Consistencia**: Se implementa **optimistic locking** con el campo `@Version` en la entidad `InventoryItem`. Esto evita problemas de concurrencia en operaciones simultáneas.
- **Exposición de API REST**:
    - `GET /api/inventory` → listar todo el inventario.
    - `GET /api/inventory/{sku}` → consultar un producto específico.
    - `POST /api/inventory` → crear/actualizar stock.
    - `POST /api/inventory/{sku}/reserve` → reservar stock.
    - `POST /api/inventory/{sku}/release` → liberar stock.
- **Observabilidad**: Se integró **Spring Boot Actuator** para exponer health checks, métricas e información de la aplicación.
- **Seguridad básica**: Se puede proteger con un filtro sencillo de **API Key** configurable en `application.yml`.

---

## ⚙️ Pila Tecnológica

- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Web**
- **SQLite / H2 Database**
- **Spring Boot Actuator**
- **JUnit 5 + Mockito + MockMvc** para pruebas unitarias e integración

---

## ✅ Estrategia de Pruebas

1. **Unitarias**:
    - `InventoryControllerTest` con Mockito y MockMvc standalone.
    - `InventoryServiceImplTest` probando la lógica de negocio con mocks del repositorio.

2. **Integración**:
    - `InventoryIntegrationTest` cubre el flujo completo Controller → Service → Repository → BD (H2).

3. **Cobertura**:
    - Se añadieron pruebas de repositorio y entidad para garantizar >90% de cobertura en Sonar.

---

## 📊 Observabilidad

- **Actuator endpoints**: `/actuator/health`, `/actuator/info`, `/actuator/metrics`.
- **Logging**: Configurable con Logback (`logback-spring.xml`) o usando el logging por defecto de Spring Boot.

---

## 🔐 Seguridad

- API Key configurable en `application.yml`.
- Se recomienda usar un **filtro de request** que valide la cabecera `X-API-KEY`.
- Esto asegura un nivel básico de protección para el prototipo sin necesidad de un sistema complejo de autenticación.

---

## 🚀 Estrategia de Despliegue

- **Local**: usando perfil `local` y SQLite (`inventory.db`).
- **Pruebas**: usando perfil `test` y base en memoria H2.
- **Docker**: empaquetado con un `Dockerfile` y perfil `docker` que persiste la BD en `/data`.

---

## 🤖 Uso de IA y Herramientas Modernas

Durante el desarrollo se utilizaron herramientas de IA generativa de forma **complementaria**, principalmente para:
- Acelerar la creación de boilerplate (clases, tests y configuraciones).
- Obtener ejemplos de configuraciones (`application.yml`, `Dockerfile`).
- Mejorar documentación (`README.md`, `run.md`, `prompts.md`).

Todas las salidas fueron revisadas y adaptadas manualmente.

---

## 📌 Conclusión

El microservicio cumple los objetivos del reto técnico:
- Arquitectura limpia y separada por capas.
- Persistencia ligera y portable.
- Endpoints REST completos para operaciones de inventario.
- Pruebas unitarias e integración con alta cobertura.
- Observabilidad y seguridad básica implementadas.

Este prototipo puede escalar fácilmente hacia un entorno distribuido con múltiples instancias y sincronización de inventario en tiempo real.

---