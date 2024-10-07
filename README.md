# Resumen de la Práctica - Aplicación para Registro de Vehículos

Esta práctica se centra en desarrollar una aplicación **Spring Boot** para gestionar vehículos en un concesionario, permitiendo crear, consultar, actualizar y eliminar registros de vehículos. A lo largo de los laboratorios, se implementaron diversas funcionalidades para completar una solución integral. Aquí se detalla un resumen del proyecto sin incluir el primer laboratorio:

### Estructura del proyecto con Spring Boot
Se creó la base del proyecto usando **Spring Boot**, organizando la paquetería en capas (controladores, servicios, repositorios). Se integró **Lombok** para simplificar la creación de objetos y se configuró un endpoint básico que mostraba información de vehículos a través de logs.

### Endpoints CRUD
Se añadieron endpoints **REST** para la gestión completa de vehículos. Los métodos `POST`, `GET`, `PUT` y `DELETE` permiten añadir, consultar, actualizar y eliminar vehículos, manejando respuestas HTTP adecuadas (`200`, `404`, `500`) y gestionando errores. **Swagger** se integró para documentar y probar los endpoints.

### Integración con Base de Datos
Se configuró una conexión a una base de datos relacional, creando tablas para los vehículos y estableciendo las relaciones correspondientes. Se implementaron todas las operaciones **CRUD** directamente sobre la base de datos usando **JPA/Hibernate**, asegurando un manejo eficiente de los datos.

#### Uso de Docker
Se trabajó con **Docker** para la creación de contenedores que ejecutaran la base de datos. Se configuró una imagen de **MySQL** con las credenciales y parámetros necesarios, facilitando la integración y la ejecución de la base de datos en el entorno local. El contenedor se creó y se ejecutó con los siguientes comandos:

```bash
docker run --name mysql-springboot -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=test -e MYSQL_USER=myroslava -e MYSQL_PASSWORD=password -p 3307:3306 -d mysql
mysql -h 127.0.0.1 -P 3307 -u myroslava -p
docker exec -it mysql-springboot mysql -u myroslava -p
```

Luego, introduce tu contraseña cuando sea solicitado.

#### Ejecutar el Script de Inicialización

Una vez dentro de la consola de MySQL, puedes ejecutar las siguientes consultas para crear la base de datos, tablas y cargar datos de ejemplo:

```sql
CREATE DATABASE IF NOT EXISTS vehicles;
USE vehicles;

-- Crear tabla de marcas
CREATE TABLE IF NOT EXISTS brand (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    warranty INT,
    country VARCHAR(100),
    PRIMARY KEY (id)
);

-- Crear tabla de vehículos
CREATE TABLE IF NOT EXISTS car (
    id BIGINT NOT NULL AUTO_INCREMENT,
    brand_id BIGINT,
    model VARCHAR(128),
    mileage INT,
    price DECIMAL(10, 2),
    year INT(4),
    description VARCHAR(255),
    colour VARCHAR(50),
    fuel_type VARCHAR(50),
    num_doors INT,
    PRIMARY KEY (id),
    FOREIGN KEY (brand_id) REFERENCES brand(id)
);

-- Insertar una marca de ejemplo
INSERT INTO brand (name, warranty, country) VALUES ('Toyota', 5, 'Japan');

-- Insertar vehículos de ejemplo
INSERT INTO car (brand_id, model, mileage, price, year, description, colour, fuel_type, num_doors) 
VALUES (1, 'Corolla', 50000, 20000.00, 2019, 'Good condition', 'Red', 'Petrol', 4),
       (1, 'Camry', 30000, 25000.00, 2020, 'New', 'Blue', 'Petrol', 4);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    surname VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    image LONGBLOB,
    role VARCHAR(50),
    PRIMARY KEY (id)
);

-- Insertar un usuario de ejemplo
INSERT INTO users (name, surname, email, password, role ) 
VALUES ('Michel', 'Jackson', 'lala@gmail.com', 'useruser', 'ROLE_VENDOR');
````

### Implementación de Asincronía
Se configuró asincronía en los métodos que requieren paralelización, optimizando el rendimiento del proyecto mediante el uso de un **pool de hilos** (mínimo 5, máximo 10).

### Autenticación y Autorización con JWT
Se añadió un sistema de autenticación basado en **tokens JWT**. Los usuarios pueden registrarse y autenticarse, y se definieron roles (clientes y vendedores). Dependiendo del rol, se controla el acceso a los distintos endpoints, limitando los métodos `GET` para los clientes y los métodos de modificación (`POST`, `PUT`, `DELETE`) para los vendedores.

### Pruebas y Optimización del Código
Se desarrollaron pruebas unitarias para asegurar la funcionalidad correcta de los componentes del proyecto, cubriendo al menos el **60 % del código**. Además, se utilizó **Sonar Lint** para optimizar el código y eliminar imports innecesarios, y se aplicó un **formatter** para mejorar la consistencia del proyecto.

### Gestión de Archivos e Imágenes
Se añadieron endpoints para la **carga y descarga de imágenes** asociadas a usuarios, así como para **exportar e importar datos** de vehículos en formato **CSV**. Estas funcionalidades permiten a los usuarios (clientes y vendedores) gestionar imágenes y cargar o descargar datos en bloque.
