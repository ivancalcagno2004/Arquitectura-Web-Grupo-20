# Documentación Docker — Resumen práctico

## 1. Conceptos rápidos
Docker funciona basandose en los siguientes recursos:
- **Imagen (image):** archivo inmutable que contiene filesystem + metadatos. Se crea con `Dockerfile` y se versiona con *tags* (ej. `miapp:1.2.0`).
    - Es basicamente un "programita" que se va a descargar y queda descargado en tu docker
- **Contenedor (container):** instancia en ejecución de una imagen (proceso aislado).
    - Es basicamente un proceso en ejecucion de un "programita" o de un conjunto de "porgramitas"
- Se puede entender un **contenedor** como un **proceso** en ejecucion en segundo plano, tambien puede que lo nombre directamente como **proceso**

---

## 2. Comandos básicos (imágenes y procesos)
> Cuando listas **contenedores** o **imagenes**, estos tendran 2 tipos de identificador (un **ID** o un **NAME**), docker los trata de forma equivalente, usa el que mas te guste.

* Listar imágenes locales:

```bash
docker images    # ó: docker image ls
```

* Listar contenedores en ejecución:

```bash
docker ps        # mostrar solo en ejecución
docker ps -a     # mostrar todos (running, exited, etc.)
```

* Ver logs de un contenedor:

```bash
docker logs -f <container_id|name>
```

---

## 3. Detener contenedores y borrar imágenes
> Es importante que sepas que un **CONTENEDOR** es un **proceso** que se esta ejecutando en tu PC en segundo plano, por lo que si no lo estas usando, apagalo, asi tu PC no estara consumiendo recursos de forma innecesaria.
> Las **IMAGENES** ocupan espacio en tu disco, por lo que si encontras imagenes que ya no necesitas, **borralas**.

* Detener contenedor en ejecución:

```bash
# detener el proceso
docker stop <container>
# matar el proceso, forzar inmediatamente
docker kill <container>
```

* Borrar contenedor (debe estar parado o usar -f):

```bash
docker rm <container>
# forzar borrado
docker rm -f <container>
```

* Borrar imagen:

```bash
docker rmi <image:tag_or_id>
# forzar
docker rmi -f <image>
```

* Limpiar recursos no usados:

```bash
docker container prune    # elimina contenedores parados
docker image prune -a     # elimina imágenes "dangling" y no referenciadas (con confirmación)
docker system prune -a    # ELIMINA TODO lo no usado (cuidado, no recomendado)
```

---

## 4. `docker-compose.yml` — sintaxis esencial

Compose define servicios, redes y volúmenes en un solo archivo.

### Encendido / Apagado (`up` / `down`)
> Es **IMPORTANTE** que estos comandos los ejecutes en la misma ruta donde esta tu archivo **docker-compose.yml**

* Levantar servicios (foreground):

```bash
docker-compose up
# o, usando la nueva CLI:
docker compose up
```

* Levantar en background (detached) y forzar rebuild:

```bash
docker-compose up -d --build
```

* Apagar y eliminar contenedores, redes y (opcional) volúmenes:

```bash
docker-compose down
# borrar volúmenes declarados con --volumes
docker-compose down --volumes
# eliminar imágenes creadas por compose
docker-compose down --rmi local
```

> Si usas `docker compose` (sin guión) las opciones son las mismas.

### Estructura mínima

```yaml
services:
  web:
    image: miusuario/miweb:1.0.0
    ports:
      - "8080:80"
    environment:
      - NODE_ENV=production
    volumes:
      - ./app:/app
```

> Nota: en Compose v2+ **no es obligatorio** el campo `version:`; muchas guías antiguas lo incluyen (`'3.8'`) pero con la CLI moderna `docker compose` se omite y se deja `services:` como raíz.

### Versionado de las imágenes (criterios)

* **No uses `latest` en producción.** Usar `latest` complica reproducibilidad.
* Emplea tags semánticos (`1.2.3`) o commit hashes (`miapp:abc1234`) para despliegues reproducibles.
* Si quieres build automático desde el repo, usa `build:` con `context:` y opcional `dockerfile:`.

Ejemplo: build vs image

```yaml
services:
  api:
    build:
      context: ./api
      dockerfile: Dockerfile
    image: miusuario/api:1.0.0   # opcional: etiqueta tras build
```

### Exposición de puertos

* `ports: - "HOST:CONTAINER"` mapea puerto del host al contenedor.
* `expose:` solo marca puertos expuestos entre servicios de la misma red de Compose (no los publica al host).

Ejemplos:

```yaml
ports:
  - "3000:3000"    # público en el host
expose:
  - "3000"         # accesible solo para otros servicios en red compose
```

### Ejemplo mínimo completo

```yaml
services:
  web:
    image: nginx:1.25
    ports:
      - "8080:80"
  db:
    image: postgres:15
    environment:
      POSTGRES_PASSWORD: ejemplo
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data:
```

---

## 5. Resolución de errores comunes

### Puerto en uso

**Síntomas:** `Bind for 0.0.0.0:8080 failed: port is already allocated` o servicio no inicia.

**Cómo detectar proceso que ocupa puerto:**

* Linux/macOS:

```bash
sudo lsof -i :8080
# o
ss -ltnp | grep :8080
```

* Windows (PowerShell / CMD):

```powershell
netstat -ano | findstr :8080
# luego obtener PID y cerrar
taskkill /PID <pid> /F
```

**Soluciones:**

* Cambiar el puerto host en `ports` (ej. `8081:80`).
* Parar el servicio que usa el puerto y reiniciar Docker.
* Si es un contenedor previo: `docker ps` → `docker stop <id>` → `docker rm <id>`.

### Errores de instalación (generales)

**Problema:** `Cannot connect to the Docker daemon. Is the docker daemon running?`

**Causas y soluciones:**

* Demonio no iniciado: `sudo systemctl start docker` / `sudo service docker start`.
* Permisos: "permission denied while trying to connect to the Docker daemon socket" → añadir usuario al grupo `docker` en Linux:`sudo usermod -aG docker $USER` y luego reloguearse.
* Conflictos con versiones previas: eliminar versiones antiguas antes de instalar (`apt remove docker docker-engine docker.io containerd runc`).
* Paquetes faltantes (Linux): seguir guía oficial para apt/yum y agregar repositorios oficiales.

### Errores frecuentes en Linux

* **Cgroup v2**: algunas distribuciones/configuraciones requieren habilitar soporte de cgroups v1 o ajustar el daemon para cgroup v2. Revisa logs con `journalctl -u docker.service`.
* **Kernel incompatible**: Docker necesita un kernel relativamente moderno; en distros muy antiguas puede fallar.
* **Problemas de permisos con volúmenes montados**: revisa UID/GID del host y contenedor; usa `:rw` o `:z` / `:Z` en SELinux.

### Errores frecuentes en Windows / Docker Desktop / WSL2

* **Docker Desktop no arranca**: comprobar que la virtualización esté activada en BIOS/UEFI y que WSL2 esté instalado/actualizado.
* **WSL2 kernel outdated**: actualizar el kernel de WSL2 según instrucciones de Microsoft.
* **Hyper-V vs WSL2**: en algunas versiones necesitas habilitar Hyper-V o usar WSL2 backend; revisar requisitos de versión de Windows.
* **Permisos de compartición de discos**: si se monta un volumen desde Windows, puede pedir credenciales o fallar por permisos.

---

## 6. Comandos de diagnóstico rápidos

* Estado del daemon:

```bash
sudo systemctl status docker
# logs
journalctl -u docker --no-pager --since "1 hour ago"
```

* Contenedores y logs:

```bash
docker ps -a
docker logs -f <container>
```

* Espacio y recursos:

```bash
docker system df
# limpiar
docker system prune -a
```

* Inspeccionar container / imagen:

```bash
docker inspect <container|image>
```

---

## 7. Notas útiles y buenas prácticas

* Versiona imágenes con tags concretos (semver o hash).
* Evita `latest` en producción.
* Usa `docker compose` (sin guión) si tu Docker CLI lo soporta (Compose v2 integrado).
* Para producir reproducibilidad, define `build` + `image` en compose y controla `Dockerfile` y `context`.
* Antes de borrar todo, revisa `docker system df` y borra selectivamente con `prune`.
* Documenta puertos y variables de entorno en un `.env.example` para tu `docker-compose.yml`.

---

### Fin — resumen de comandos esenciales

```bash
# listar
docker images
docker ps -a

# parar / borrar
docker stop <c>
docker rm -f <c>
docker rmi -f <img>

# compose
docker-compose up -d --build
docker-compose down --volumes --rmi local

# diagnóstico
docker logs -f <c>
sudo systemctl status docker
sudo lsof -i :8080
```
