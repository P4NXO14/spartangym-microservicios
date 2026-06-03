# SpartanGYM 

SpartanGYM es un sistema desarrollado con arquitectura de microservicios para la gestión de distintas áreas de un gimnasio. El proyecto fue construido en Java con Spring Boot, aplicando separación de responsabilidades, persistencia con JPA, endpoints REST, validaciones, manejo de errores y comunicación entre microservicios.

## Integrantes

* Nombre integrante 1
* Nombre integrante 2
* Nombre integrante 3

## Microservicios del sistema

El sistema está compuesto por los siguientes microservicios:

* **clientes**: gestiona la información de los clientes registrados en el sistema.
* **reservas**: administra las reservas de clases realizadas por los clientes.
* **asistencia**: registra y controla la asistencia de los clientes.
* **logros**: gestiona los logros obtenidos por los clientes.
* **notificaciones**: permite registrar y administrar notificaciones del sistema.
* **productos**: gestiona productos, ventas y control de stock.
* **pagos**: administra los pagos asociados a planes y productos.
* **planes**: gestiona los planes disponibles y contratados por los clientes.
* **rutinas**: administra las rutinas de entrenamiento.

## Puertos utilizados

| Microservicio  | Puerto |
| -------------- | -----: |
| clientes       |   8080 |
| reservas       |   8081 |
| asistencia     |   8082 |
| logros         |   8083 |
| notificaciones |   8084 |
| productos      |   8085 |
| pagos          |   8086 |
| planes         |   8087 |
| rutinas        |   8088 |

## Tecnologías utilizadas

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Bean Validation
* SLF4J para logs
* RestTemplate para comunicación entre microservicios
* Postman para pruebas REST
* GitHub para control de versiones

## Estructura general

Cada microservicio mantiene una estructura organizada por capas, siguiendo el patrón CSR:

* **Controller**: expone los endpoints REST.
* **Service**: contiene la lógica de negocio.
* **Repository**: permite el acceso a datos mediante JpaRepository.
* **Model**: define las entidades principales del microservicio.
* **DTO**: permite transportar datos entre capas o entre microservicios cuando corresponde.
* **Exception / Handler**: centraliza el manejo de errores y respuestas controladas.

## Funcionalidades principales

El proyecto permite realizar operaciones CRUD en los distintos microservicios, aplicar validaciones sobre los datos recibidos, controlar errores mediante respuestas HTTP adecuadas y registrar eventos importantes mediante logs. Además, algunos microservicios se comunican entre sí para validar información o completar procesos del sistema.

Entre los flujos principales se encuentran:

* Registro y administración de clientes.
* Gestión de reservas de clases.
* Control de asistencia.
* Administración de planes.
* Gestión de productos y ventas.
* Registro de pagos de planes y productos.
* Administración de rutinas.
* Registro de logros.
* Gestión de notificaciones.

## Comunicación entre microservicios

El sistema utiliza RestTemplate para consumir endpoints entre microservicios, siguiendo lo trabajado en clases. Algunas comunicaciones importantes son:

* **reservas** consulta información de **clientes**.
* **asistencia** consulta información de **clientes**.
* **productos** se comunica con **pagos** al generar ventas.
* **planes** se comunica con **pagos** al registrar pagos asociados a planes.

## Requisitos para ejecutar

Antes de ejecutar los microservicios, se debe contar con:

* Java 21 instalado.
* MySQL instalado y en ejecución.
* Maven configurado.
* Bases de datos creadas para cada microservicio según su archivo `application.properties`.

## Ejecución del proyecto

Para ejecutar cada microservicio, se debe ingresar a la carpeta correspondiente y utilizar Maven:

```bash
mvn spring-boot:run
```

También se puede ejecutar cada microservicio desde el IDE utilizando la clase principal de Spring Boot.

Se recomienda iniciar primero los microservicios que son consultados por otros servicios, como **clientes** y **pagos**, y luego ejecutar los demás microservicios según el flujo que se desee probar.

## Pruebas

Las pruebas de los endpoints REST fueron realizadas utilizando Postman, verificando operaciones de creación, consulta, actualización, eliminación, validaciones, manejo de errores y comunicación entre microservicios.

## Control de versiones

El proyecto fue subido a GitHub con commits separados y descriptivos para evidenciar el avance del desarrollo y la participación de los integrantes del equipo.
