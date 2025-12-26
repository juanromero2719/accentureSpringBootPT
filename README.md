# Accenture API - Sistema de Gestión de Franquicias

API REST desarrollada con Spring Boot para la gestión de franquicias, sucursales y productos. Permite crear, actualizar y consultar información sobre franquicias, sus sucursales y los productos asociados a cada sucursal.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Configuración](#configuración)
- [Ejecución Local](#ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Despliegue](#despliegue)
- [API Endpoints](#api-endpoints)
- [Base de Datos](#base-de-datos)

## 📝 Descripción

Esta aplicación es un sistema de gestión que permite:

- **Franquicias**: Crear y actualizar franquicias
- **Sucursales**: Agregar sucursales a una franquicia y actualizar su información
- **Productos**: Gestionar productos por sucursal (crear, actualizar stock, actualizar nombre, eliminar)
- **Consultas**: Obtener el producto con mayor stock por sucursal para una franquicia

La aplicación sigue los principios de arquitectura MVC, SOLID y buenas prácticas de desarrollo.

## 🛠 Tecnologías

- **Java 21**: Lenguaje de programación
- **Spring Boot 4.0.1**: Framework de aplicación
- **Spring Web MVC**: Para APIs REST
- **Spring JDBC**: Para acceso a datos
- **PostgreSQL**: Base de datos relacional
- **HikariCP**: Pool de conexiones
- **Maven**: Gestión de dependencias
- **Docker**: Containerización

## 📦 Requisitos Previos

Para ejecutar este proyecto necesitas:

- **Java 21** o superior
- **Maven 3.6+** (incluido en el proyecto como wrapper)
- **PostgreSQL** (o acceso a una base de datos PostgreSQL como Supabase)
- **Docker** (opcional, solo si quieres ejecutar con Docker)

### Verificar Instalación

```bash
java -version    # Debe mostrar Java 21 o superior
mvn -version     # Debe mostrar Maven 3.6+
docker --version # Opcional
```

## 📁 Estructura del Proyecto

```
accenture/
├── src/
│   ├── main/
│   │   ├── java/com/pt/accenture/
│   │   │   ├── api/              # Controladores REST
│   │   │   │   ├── BranchController.java
│   │   │   │   ├── FranchiseController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   └── dto/          # Data Transfer Objects
│   │   │   ├── config/           # Configuraciones
│   │   │   │   └── DatabaseMigration.java
│   │   │   ├── domain/           # Entidades del dominio
│   │   │   │   ├── Branch.java
│   │   │   │   ├── Franchise.java
│   │   │   │   └── Product.java
│   │   │   ├── repository/       # Acceso a datos
│   │   │   │   ├── BranchRepository.java
│   │   │   │   ├── FranchiseRepository.java
│   │   │   │   └── ProductRepository.java
│   │   │   ├── service/          # Lógica de negocio
│   │   │   │   ├── BranchService.java
│   │   │   │   ├── FranchiseService.java
│   │   │   │   └── ProductService.java
│   │   │   └── AccentureApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application.yml
│   │       └── migrations/
│   │           └── 001_init_franchises.sql
│   └── test/                     # Tests
├── Dockerfile
├── .dockerignore
├── pom.xml
├── mvnw                         # Maven Wrapper (Linux/Mac)
├── mvnw.cmd                     # Maven Wrapper (Windows)
└── README.md                     # Este archivo
```

## ⚙️ Configuración

### Variables de Entorno

La aplicación utiliza variables de entorno para la configuración de la base de datos. Puedes configurarlas de dos formas:

#### Opción 1: Variables de Entorno del Sistema

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/postgres?sslmode=require
export SPRING_DATASOURCE_USERNAME=tu_usuario
export SPRING_DATASOURCE_PASSWORD=tu_password
```

#### Opción 2: Archivo application.properties

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://tu-host:5432/postgres?sslmode=require
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

**Nota**: Si no configuras las variables de entorno, la aplicación usará los valores por defecto del archivo `application.properties`.

### Base de Datos

La aplicación crea automáticamente las tablas necesarias al iniciar mediante migraciones SQL ubicadas en `src/main/resources/migrations/`.

## 🚀 Ejecución Local

### Paso 1: Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd accenture/accenture
```

### Paso 2: Configurar la Base de Datos

Asegúrate de tener acceso a una base de datos PostgreSQL y configura las variables de entorno o edita `application.properties`.

### Paso 3: Compilar el Proyecto

```bash
# Windows
mvnw.cmd clean package

# Linux/Mac
./mvnw clean package
```

### Paso 4: Ejecutar la Aplicación

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

O ejecutar el JAR directamente:

```bash
java -jar target/accenture-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

### Verificar que Funciona

```bash
curl http://localhost:8080/franchises
```

## 🐳 Ejecución con Docker

### Paso 1: Construir la Imagen

```bash
docker build -t accenture-api .
```

### Paso 2: Ejecutar el Contenedor

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://tu-host:5432/postgres?sslmode=require" \
  -e SPRING_DATASOURCE_USERNAME="tu_usuario" \
  -e SPRING_DATASOURCE_PASSWORD="tu_password" \
  accenture-api
```

O usando un archivo `.env`:

```bash
docker run -p 8080:8080 --env-file .env accenture-api
```

### Verificar que Funciona

```bash
curl http://localhost:8080/franchises
```

## 📡 API Endpoints

### Franquicias

#### Crear Franquicia
```http
POST /franchises
Content-Type: application/json

{
  "name": "Nombre de la franquicia"
}
```

**Response**: `201 Created`
```json
{
  "id": "uuid",
  "name": "Nombre de la franquicia"
}
```

#### Actualizar Nombre de Franquicia
```http
PUT /franchises/{franchiseId}
Content-Type: application/json

{
  "name": "Nuevo nombre"
}
```

**Response**: `200 OK`

#### Obtener Productos con Mayor Stock por Sucursal
```http
GET /franchises/{franchiseId}/products/max-stock
```

**Response**: `200 OK`
```json
[
  {
    "id": "uuid",
    "branchId": "uuid",
    "branchName": "Nombre de la sucursal",
    "name": "Nombre del producto",
    "stock": 150
  }
]
```

### Sucursales

#### Crear Sucursal
```http
POST /franchises/{franchiseId}/branches
Content-Type: application/json

{
  "name": "Nombre de la sucursal"
}
```

**Response**: `201 Created`
```json
{
  "id": "uuid",
  "franchiseId": "uuid",
  "name": "Nombre de la sucursal"
}
```

#### Actualizar Nombre de Sucursal
```http
PUT /franchises/{franchiseId}/branches/{branchId}
Content-Type: application/json

{
  "name": "Nuevo nombre"
}
```

**Response**: `200 OK`

### Productos

#### Crear Producto
```http
POST /branches/{branchId}/products
Content-Type: application/json

{
  "name": "Nombre del producto",
  "stock": 100
}
```

**Response**: `201 Created`
```json
{
  "id": "uuid",
  "branchId": "uuid",
  "name": "Nombre del producto",
  "stock": 100
}
```

#### Actualizar Stock de Producto
```http
PUT /branches/{branchId}/products/{productId}/stock
Content-Type: application/json

{
  "stock": 150
}
```

**Response**: `200 OK`

#### Actualizar Nombre de Producto
```http
PUT /branches/{branchId}/products/{productId}/name
Content-Type: application/json

{
  "name": "Nuevo nombre"
}
```

**Response**: `200 OK`

#### Eliminar Producto
```http
DELETE /branches/{branchId}/products/{productId}
```

**Response**: `204 No Content`

## 🗄️ Base de Datos

### Estructura de Tablas

#### Tabla: `franchise`
- `id` (UUID, PRIMARY KEY)
- `name` (VARCHAR(255), NOT NULL, UNIQUE)

#### Tabla: `branch`
- `id` (UUID, PRIMARY KEY)
- `franchise_id` (UUID, FOREIGN KEY → franchise.id)
- `name` (VARCHAR(255), NOT NULL)
- UNIQUE (`franchise_id`, `name`)

#### Tabla: `product`
- `id` (UUID, PRIMARY KEY)
- `branch_id` (UUID, FOREIGN KEY → branch.id)
- `name` (VARCHAR(255), NOT NULL)
- `stock` (INTEGER, NOT NULL)
- UNIQUE (`branch_id`, `name`)

### Migraciones

Las migraciones SQL se ejecutan automáticamente al iniciar la aplicación desde `src/main/resources/migrations/001_init_franchises.sql`.

## 🧪 Testing

```bash
# Ejecutar tests
mvnw test

# Windows
mvnw.cmd test
```

## 📝 Notas Importantes

- La aplicación usa **UUID** como identificadores
- Todas las validaciones se realizan en la capa de servicio
- Los nombres deben tener máximo 255 caracteres
- El stock no puede ser negativo
- Los nombres de franquicias, sucursales y productos deben ser únicos dentro de su contexto

## 🐛 Solución de Problemas

### Error 409 Conflict al crear una franquicia

Este error es **normal y esperado** cuando intentas crear una franquicia, sucursal o producto con un nombre que ya existe. El sistema valida la unicidad de los nombres.

**Solución**: Usa un nombre diferente o actualiza la entidad existente en lugar de crear una nueva.

**Ejemplo de respuesta**:
```json
{
  "timestamp": "2025-12-26T20:04:10.761Z",
  "status": 409,
  "error": "Conflict",
  "message": "La franquicia ya existe",
  "path": "/franchises"
}
```

### Error: "Failed to obtain JDBC Connection"

- Verifica que la base de datos esté accesible
- Revisa las credenciales en `application.properties` o variables de entorno
- Asegúrate de que el hostname de la base de datos sea correcto
- En Render, verifica que las variables de entorno estén configuradas correctamente

### Error: "UnknownHostException"

- Verifica que el hostname de la base de datos sea correcto
- Si usas Supabase, verifica que uses el Session Pooler para IPv4
- Asegúrate de que la URL de conexión sea correcta

### La aplicación no inicia

- Verifica que Java 21 esté instalado: `java -version`
- Verifica que Maven esté funcionando: `mvnw --version`
- Revisa los logs de la aplicación para más detalles
- En Render, verifica que el Dockerfile esté en la raíz del proyecto

### Problemas de Despliegue en Render

1. **Variables de Entorno**: Asegúrate de configurar estas variables en Render:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`

2. **Dockerfile Path**: Verifica que en Render esté configurado como `./Dockerfile`

3. **Root Directory**: Si tu proyecto está en un subdirectorio, configura el "Root Directory" en Render

4. **Puerto**: La aplicación usa el puerto 8080 por defecto, Render lo detecta automáticamente

## 📄 Licencia

Este proyecto es un proyecto de demostración.

## 👥 Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

