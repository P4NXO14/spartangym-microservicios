#  🏋️‍♂️ SpartanGYM

SpartanGYM es un sistema desarrollado con arquitectura de microservicios para la gestion de distintas areas de un gimnasio. El proyecto fue construido en Java con Spring Boot, aplicando separacion de responsabilidades, persistencia con JPA, endpoints REST, validaciones, manejo de errores y comunicacion entre microservicios.

## 👥 Integrantes

Francisco Osorio
Danilo Poblete

## 🧩 Microservicios del sistema

El sistema esta compuesto por los siguientes microservicios:

* 👤 **clientes**: gestiona la informacion de los clientes registrados en el sistema.
* 📅 **reservas**: administra las reservas de clases realizadas por los clientes.
* ✅ **asistencia**: registra y controla la asistencia de los clientes.
* 🏆 **logros**: gestiona los logros obtenidos por los clientes.
* 🔔 **notificaciones**: permite registrar y administrar notificaciones del sistema.
* 🏷️**productos**: gestiona productos, ventas y control de stock.
* 💳 **pagos**: administra los pagos asociados a planes y productos.
* 📋 **planes**: gestiona los planes disponibles y contratados por los clientes.
* 💪**rutinas**: administra las rutinas de entrenamiento.

## ⚙️ Componentes de infraestructura

Ademas de los microservicios de negocio, el proyecto cuenta con los siguientes componentes:

* 📡 **eureka-server**: permite el registro y descubrimiento dinamico de los microservicios.
* 🚪 **api-gateway**: centraliza el acceso a los endpoints de los microservicios mediante un unico punto de entrada.

## Puertos utilizados

| Componente / Microservicio | Puerto |
| -------------------------- | ------ |
| 📡 Eureka Server           | 8761   |
| 🚪 API Gateway             | 8090   |
| 👤 clientes                | 8080   |
| 📅 reservas                | 8081   |
| ✅ asistencia              | 8082   |
| 🏆 logros                  | 8083   |
| 🔔 notificaciones          | 8084   |
| 🏷️ productos               | 8085   |
| 💳 pagos                   | 8086   |
| 📋 planes                   | 8087  |
| 💪 rutinas                 | 8088   |

## 💻 Tecnologias utilizadas

* ☕ Java 21
* 🌱 Spring Boot
* 🌱 Spring Data JPA
* 🛡️ Hibernate
* 🐬 MySQL
* 🏗️ Maven
* 🛡️ Bean Validation
* 📝 SLF4J para logs
* 🌐 RestTemplate para comunicacion entre microservicios cuando corresponde
* 🚀 Postman para pruebas REST
* 🐙 GitHub para control de versiones
* 📖 Spring Cloud Netflix Eureka
* 📖 Spring Cloud Gateway
* 📖 Springdoc OpenAPI (Swagger UI)
* 🧪 JUnit 5, MockMvc y Mockito

## 🏛️ Estructura general

Cada microservicio mantiene una estructura organizada por capas, siguiendo el patron CSR:

* **Controller**: expone los endpoints REST.
* **Service**: contiene la logica de negocio.
* **Repository**: permite el acceso a datos mediante JpaRepository.
* **Model**: define las entidades principales del microservicio.
* **DTO**: permite transportar datos entre capas o entre microservicios cuando corresponde.
* **Exception / Handler**: centraliza el manejo de errores y respuestas controladas.
* **Swagger / Config**: clases de configuracion para la documentacion interactiva autogenerada.

## ✨ Funcionalidades principales

El proyecto permite realizar operaciones CRUD en los distintos microservicios, aplicar validaciones sobre los datos recibidos, controlar errores mediante respuestas HTTP adecuadas y registrar eventos importantes mediante logs. Ademas, algunos microservicios se comunican entre si para validar informacion o completar procesos del sistema.

Entre los flujos principales se encuentran:

* Registro y administracion de clientes.
* Gestion de reservas de clases.
* Control de asistencia.
* Administracion de planes.
* Gestion de productos y ventas.
* Registro de pagos de planes y productos.
* Administracion de rutinas.
* Registro de logros.
* Gestion de notificaciones.

## 🔄 Comunicacion entre microservicios

El sistema utiliza RestTemplate cuando un microservicio requiere consultar o enviar informacion a otro servicio, siguiendo lo trabajado en clases. Algunas comunicaciones importantes son:

* ** reservas** consulta informacion de **clientes**.
* **asistencia** consulta informacion de **clientes**.
* **productos** se comunica con **pagos** al generar ventas.
* **planes** se comunica con **pagos** al registrar pagos asociados a planes.
* Todos los microservicios se registran en **Spring Cloud Netflix Eureka**, permitiendo su descubrimiento dentro de la arquitectura.
* **API Gateway** centraliza el acceso externo a los endpoints mediante el puerto `8090`.

## 📚 Documentacion de la API

Cada microservicio cuenta con su propia documentacion interactiva de la API implementada con OpenAPI 3.

Una vez que un microservicio esta en ejecucion, se puede acceder a Swagger UI mediante:

```text
http://localhost:<PUERTO_DEL_MICROSERVICIO>/swagger-ui/index.html
```

Ejemplo para clientes:

```text
http://localhost:8080/swagger-ui/index.html

```
## 🐳 Despliegue automatizado con Docker (Recomendado)

El proyecto completo ha sido dockerizado para levantar la arquitectura de los 11 contenedores con un solo comando utilizando **Docker-Compose up --build**, asegurando el orden de inicio (Eureka -> Gateway -> Microservicios) y conectándolos mediante la red virtual `spartan-net`.

**Pasos para desplegar con Docker:**

1. **Compilar los microservicios:** Generar los archivos `.jar` ejecutando el siguiente comando en la terminal. 

cd C:\Users\Danilo\Desktop\spartangym-microservicios
cd clientes/clientes; .\mvnw clean package -DskipTests; cd ../..
cd reservas/reservas; .\mvnw clean package -DskipTests; cd ../..
cd asistencia/asistencia; .\mvnw clean package -DskipTests; cd ../..
cd logros/logros; .\mvnw clean package -DskipTests; cd ../..
cd notificaciones/notificaciones; .\mvnw clean package -DskipTests; cd ../..
cd productos/productos; .\mvnw clean package -DskipTests; cd ../..
cd pagos/pagos; .\mvnw clean package -DskipTests; cd ../..
cd planes/planes; .\mvnw clean package -DskipTests; cd ../..
cd rutinas/rutinas; .\mvnw clean package -DskipTests; cd ../..

2. **Levantar la arquitectura:** Desde la carpeta raiz (donde se encuentra el archivo `docker-compose.yml`), ejecutar:
   ```bash
   docker-compose build --no-cache
   docker-compose up 


## ⚠️ Requisitos para ejecutar

Antes de ejecutar los microservicios, se debe contar con:

* Java 21 instalado.
* MySQL instalado y en ejecucion.
* Maven configurado.
* Bases de datos creadas para cada microservicio segun su archivo `application.properties`.

## ⌨️ Ejecucion del proyecto

Para ejecutar el proyecto se recomienda seguir el siguiente orden:

1. Iniciar 📡 **Eureka Server** en el puerto `8761`.
2. Iniciar 🚪 **API Gateway** en el puerto `8090`.
3. Iniciar los microservicios de negocio:

   * 👤 clientes
   * 📅 reservas
   * ✅ asistencia
   * 🏆 logros
   * 🔔 notificaciones
   * 🏷️ productos
   * 💳 pagos
   * 📋 planes
   * 💪 rutinas
4. Verificar el registro de los servicios en 📡 Eureka:

```text
http://localhost:8761
```

5. Probar los endpoints mediante 🚪 API Gateway:

```text
http://localhost:8090
```

Cada proyecto puede ejecutarse desde su carpeta utilizando:

```bash
mvn spring-boot:run
```

Tambien es posible ejecutar cada microservicio desde el IDE mediante su clase principal de Spring Boot.

## 🧪 Pruebas

Las pruebas de los endpoints REST fueron realizadas utilizando Postman, verificando operaciones de creacion, consulta, actualizacion, eliminacion, validaciones, manejo de errores y comunicacion entre microservicios.

Tambien se implementaron pruebas unitarias utilizando JUnit 5, MockMvc y Mockito.

Se verificaron respuestas HTTP controladas como:

* `200 OK`
* `201 Created`
* `204 No Content`
* `400 Bad Request`
* `404 Not Found`
* `503 Service Unavailable`

## Control de versiones

El proyecto fue subido a GitHub con commits separados y descriptivos para evidenciar el avance del desarrollo y la participacion de los integrantes del equipo.
