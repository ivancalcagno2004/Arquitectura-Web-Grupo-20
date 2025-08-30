# Docker + MySQL + phpMyAdmin — instalación rápida

Resumen: Añadi `docker-compose.yml` al proyecto (raíz del repo) para levantar MySQL + phpMyAdmin. Los datos sensibles (contraseñas, puertos editables) se separan en un archivo `.env` en la raíz y **NO se suben al repositorio**.

# ¿Qué se agregó?

- **docker-compose.yml** — define servicios `db` (MySQL) y `phpmyadmin`.
- **.env** — variables sensibles / configurables (ruta: **misma carpeta que** `docker-compose.yml`, es decir *root del proyecto*).

> **IMPORTANTE**: Se añadió .env a .gitignore para que no se suba el archivo al repo.

# Crear el archivo `.env`

1. En la raíz del proyecto (donde está `docker-compose.yml`) crea un archivo llamado `.env`.
2. Pegar el template (editar valores antes de arrancar).

```
# MySQL
MYSQL_ROOT_PASSWORD=MiRootPassSegura123!
MYSQL_DATABASE=mi_base
MYSQL_USER=usuario
MYSQL_PASSWORD=MiPassUsuario123!

# Puertos (opcional — cambiar si hay conflictos)
MYSQL_HOST_PORT=3306
PHPMYADMIN_HOST_PORT=8080
```

# Comandos básicos - para levantar docker

Desde la raíz del proyecto (donde están `docker-compose.yml` y `.env`):
- Si te gusta ver lo que hace `docker` en la terminal y visualizar los logs.
    - **Levantar docker**: `docker-compose up`
    - **Matar proceso**: `ctrl + c`
    - **Apagar docker**: `docker-compose down`
- Si no te interesa ver lo que hace docker y no queres que te quede la terminal ocupada.
    - **Levantar docker**: `docker-compose up -d`
    - **Apagar docker**: `docker-compose down`

## Si todo salio bien
Acceder a phpMyAdmin: `http://localhost:8080` (usuario y contraseña: las que definiste en tu `.env`, `MYSQL_USER` y `MYSQL_PASSWORD`).

## Si todo salio mal
Lee el siguietne apartado.

# Error común 1: instalaste `docker` pa la mierda.
Anda a powershell o al emulador de terminal que uses y ejecuta `docker -v` deberia aparecer la version de `Docker` que tenes instalada.

# Error común 2: instalaste `docker` pero no `docker-compose`.
Anda a powershell o al emulador de terminal que uses y ejecuta `docker-compose -v` deberia aparecer la version de `docker-compose` que tenes instalada.

# Error común: puerto en uso

**Síntoma**: `Bind for 0.0.0.0:3306 failed: port is already allocated` o `address already in use`.

**Soluciones rápidas** (elige una):
- **Cambiar el puerto** en `.env` (p. ej. `MYSQL_HOST_PORT=3307`) y volver a `docker-compose up`.
- **Detener el proceso que usa el puerto**:
    - Linux/macOS:
    ``` bash
    sudo lsof -i :3306
    sudo kill -9 <PID>
    ```
    - Windows (PowerShell)
    ``` powershell
    netstat -ano | findstr :3306
    taskkill /PID <PID> /F
    ```

**Soluciones lentas**: Averigua que programa te esta usando el puerto y apagalo, preguntale a `GPT`.