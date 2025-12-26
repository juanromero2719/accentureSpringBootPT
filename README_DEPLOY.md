# Guía de Despliegue

## Opciones de Despliegue para Spring Boot

### Railway (Recomendado - Más fácil)

1. Crea una cuenta en [Railway](https://railway.app)
2. Conecta tu repositorio de GitHub
3. Railway detectará automáticamente el `railway.json` y el `Dockerfile`
4. Configura las variables de entorno:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
5. Railway desplegará automáticamente

### Render

1. Crea una cuenta en [Render](https://render.com)
2. Selecciona "New Web Service"
3. Conecta tu repositorio
4. Render detectará el `render.yaml`
5. Configura las variables de entorno en el dashboard
6. Render desplegará automáticamente

### Fly.io

1. Instala Fly CLI: `curl -L https://fly.io/install.sh | sh`
2. Ejecuta: `fly launch`
3. Sigue las instrucciones
4. Configura las variables de entorno: `fly secrets set SPRING_DATASOURCE_URL=...`

### Variables de Entorno Necesarias

Asegúrate de configurar estas variables en la plataforma elegida:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://tu-host:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=tu-usuario
SPRING_DATASOURCE_PASSWORD=tu-password
```

### Prueba Local del Dockerfile

```bash
docker build -t accenture-api .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="..." \
  -e SPRING_DATASOURCE_USERNAME="..." \
  -e SPRING_DATASOURCE_PASSWORD="..." \
  accenture-api
```

