# 🏥 HURST-Control — Java/JavaFX Desktop Application

Control de Acceso Hospitalario para estudiantes universitarios en práctica.  
**Hospital San Rafael de Tunja**

---

## 📋 Requisitos del sistema

| Herramienta  | Versión mínima |
|--------------|----------------|
| Java JDK     | 21+            |
| JavaFX SDK   | 21 (incluido vía Gradle) |
| MySQL        | 8.0+ (recomendado: XAMPP) |
| Gradle       | 8.7 (wrapper incluido) |

---

## ⚙️ Configuración inicial — paso a paso

### 1. Instalar Java 21

Descarga desde https://adoptium.net/ (Eclipse Temurin 21 LTS)

Verifica: `java -version`

### 2. Instalar MySQL con XAMPP

Descarga desde https://www.apachefriends.org/  
Inicia el módulo **MySQL** desde el panel de control de XAMPP.

> Por defecto, XAMPP crea el usuario `root` **sin contraseña**.  
> Si usas otra configuración, edita `src/main/resources/META-INF/Persistence.xml`.

### 3. Crear la base de datos

Abre phpMyAdmin (`http://localhost/phpmyadmin`) o la terminal MySQL y ejecuta:

```sql
CREATE DATABASE hurst_control CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. Verificar la conexión JPA

El archivo de configuración ya apunta a XAMPP con zona horaria de Bogotá:

```xml
<!-- src/main/resources/META-INF/Persistence.xml -->
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:mysql://localhost:3306/hurst_control?useSSL=false
                 &amp;serverTimezone=America/Bogota
                 &amp;allowPublicKeyRetrieval=true"/>
<property name="jakarta.persistence.jdbc.user"     value="root"/>
<property name="jakarta.persistence.jdbc.password" value=""/>
```

Ajusta `user` y `password` si tu instancia MySQL tiene credenciales diferentes.

### 5. Ejecutar la aplicación

```bash
# Windows
gradlew.bat run

# Linux / macOS
./gradlew run
```

Hibernate creará todas las tablas automáticamente (`hbm2ddl.auto=update`) en el primer arranque.

---

## 🚦 Cómo funciona el Control de Acceso

El módulo **Control de Acceso** valida las siguientes condiciones antes de permitir la entrada:

1. **Estudiante activo** — existe en el sistema y tiene el flag `activo = true`
2. **Inducción completada** — estado `REALIZADA` (no `PENDIENTE`)
3. **ARL vigente** — fecha de vigencia no vencida respecto a la fecha actual
4. **Cronograma APROBADO** — existe un cronograma con estado `APROBADO` para hoy y dentro del horario actual (`horaInicio` ≤ ahora ≤ `horaFin`)
5. **Cupo disponible** — el servicio asignado no ha alcanzado su `capacidadMaxima`

> **Nota:** La validación de presencia del docente está implementada pero actualmente deshabilitada en el código (`AccesoController.java`).

---

## 🌳 Árbol Organizacional

El sistema incluye un árbol n-ario (`ArbolOrganizacional`) que representa la jerarquía:

```
HURST CONTROL  (raíz virtual)
└── Universidad A
    ├── Docente 1
    │   ├── Estudiante X
    │   └── Estudiante Y
    └── Docente 2
        └── Estudiante Z
└── Universidad B
    └── ...
```

El árbol se construye en memoria desde los DAOs y se refresca automáticamente con cada operación de escritura a través de `HurstFacade`. El módulo **Árbol** lo muestra de forma interactiva con íconos SVG y estadísticas por nodo.

---

## 🏗️ Arquitectura del proyecto

```
src/main/java/
├── usta/hurstcontrol/
│   └── Main.java                    ← Punto de entrada JavaFX
├── controller/                      ← Controladores JavaFX
│   ├── MainController.java          ← Navegación lateral (sidebar)
│   ├── AccesoController.java        ← Semáforo + tabla de activos
│   ├── ArbolController.java         ← Árbol organizacional interactivo
│   ├── CronogramaController.java    ← Gestión y aprobación de cronogramas
│   ├── EstudianteController.java    ← CRUD estudiantes
│   ├── DocenteController.java       ← CRUD docentes
│   ├── ServicioController.java      ← CRUD servicios hospitalarios
│   └── UniversidadController.java   ← CRUD universidades con convenio
├── facade/
│   └── HurstFacade.java             ← Patrón Facade + Singleton: punto único de acceso
├── dao/                             ← Patrón Generic DAO
│   ├── GenericDao.java              ← Base genérica (findAll, findById, save, delete)
│   ├── EstudianteDAO.java
│   ├── DocenteDAO.java
│   ├── CronogramaDAO.java
│   ├── ServicioDAO.java
│   ├── UniversidadDAO.java
│   ├── UsuarioDAO.java
│   ├── RegistroAccesoDAO.java
│   ├── RegistroDocenteDAO.java
│   ├── AlertaLogDAO.java
│   └── AuditoriaDAO.java
├── model/                           ← 10 entidades JPA
│   ├── Universidad.java
│   ├── Usuario.java
│   ├── Docente.java
│   ├── Servicio.java
│   ├── Estudiante.java              ← enum EstadoInduccion {PENDIENTE, REALIZADA}
│   ├── Cronograma.java              ← enum Estado {PENDIENTE, APROBADO, RECHAZADO, MODIFICADO}
│   ├── RegistroAcceso.java
│   ├── RegistroDocente.java
│   ├── AlertaLog.java
│   └── Auditoria.java
├── tree/                            ← Estructura de datos propia
│   ├── ArbolOrganizacional.java     ← Árbol n-ario en memoria
│   ├── NodoOrganizacional.java      ← Nodo genérico con lista de hijos
│   └── NodoVisitor.java             ← Interfaz de recorrido (Visitor pattern)
└── util/
    └── JPAUtil.java                 ← EntityManagerFactory singleton

src/main/resources/
├── META-INF/Persistence.xml         ← Configuración JPA/Hibernate/MySQL
├── images/
│   ├── logito.png
│   └── logo_hospital.png
└── view/                            ← 9 archivos FXML
    ├── Header.fxml                  ← Cabecera reutilizable
    ├── main.fxml                    ← Ventana principal con sidebar
    ├── acceso.fxml                  ← Semáforo RGB + tabla de activos
    ├── arbol.fxml                   ← Árbol organizacional
    ├── cronograma.fxml
    ├── estudiante.fxml
    ├── docente.fxml
    ├── servicio.fxml
    └── universidad.fxml
```

---

## 🧩 Módulos disponibles

| Módulo               | Función |
|----------------------|---------|
| 🚦 Control de Acceso  | Semáforo RGB (verde/rojo) + registro de entrada/salida por cédula |
| 🌳 Árbol Organizacional | Vista jerárquica Universidad → Docente → Estudiante con estadísticas de nodo |
| 📅 Cronogramas        | Creación y gestión de horarios con flujo de aprobación |
| 🎓 Estudiantes        | CRUD con ARL, seguro estudiantil e inducción |
| 👨‍🏫 Docentes          | CRUD de docentes vinculados a universidad |
| 🏥 Servicios          | CRUD de servicios con capacidad máxima |
| 🏛 Universidades      | CRUD de universidades con convenio activo |

---

## 🎨 Patrones de diseño implementados

| Patrón    | Clase principal       | Descripción |
|-----------|-----------------------|-------------|
| Facade    | `HurstFacade`         | Punto único de acceso a todos los DAOs; simplifica los controladores |
| Singleton | `HurstFacade`, `JPAUtil` | Una sola instancia compartida por toda la aplicación |
| Generic DAO | `GenericDao<T, ID>` | Base reutilizable para todas las operaciones de persistencia |
| Visitor   | `NodoVisitor`         | Recorrido desacoplado del árbol n-ario |
| MVC       | FXML + Controllers    | Separación de vista (`.fxml`) y lógica de presentación |

---

## 🛠️ Abrir en IntelliJ IDEA

1. **File → Open** → selecciona la carpeta `hurst-control`
2. IntelliJ detecta el proyecto Gradle automáticamente
3. Espera a que descarguen las dependencias (primera vez puede tardar)
4. Verifica que el JDK configurado sea **Java 21**
5. Ejecuta `Main.java` con el botón ▶ o usa la tarea Gradle `run`

---

## 📦 Dependencias principales

| Librería                  | Versión      | Función |
|---------------------------|--------------|---------|
| JavaFX (controls + fxml)  | 21           | Interfaz gráfica de escritorio |
| Hibernate ORM             | 6.4.4.Final  | Persistencia JPA |
| Jakarta Persistence API   | 3.1.0        | Especificación JPA |
| MySQL Connector/J         | 8.0.33       | Driver JDBC MySQL |
| jBCrypt (mindrot)         | 0.4          | Hash de contraseñas |
| JUnit Jupiter             | 5.10.0       | Tests unitarios |

---

## 🐛 Solución de problemas comunes

**`No suitable driver found`**  
→ Verifica que MySQL Connector/J está en `build.gradle` y que Gradle descargó las dependencias correctamente.

**`Access denied for user 'root'`**  
→ Edita `Persistence.xml`. En XAMPP el password de `root` es vacío por defecto; si cambiaste la contraseña, reflétala ahí.

**`Communications link failure`**  
→ Asegúrate de que el servicio MySQL de XAMPP está corriendo (panel de control → MySQL → Start).

**Pantalla en blanco al cargar un módulo**  
→ Revisa la consola por errores de FXML. La causa más común es un `fx:id` en el FXML que no coincide con el campo `@FXML` en el controlador.

**`Cannot find module javafx...`**  
→ Asegúrate de estar usando exactamente **Java 21**. El plugin `org.openjfx.javafxplugin` descarga JavaFX automáticamente; no hace falta instalarlo por separado.

**El árbol aparece vacío**  
→ Es normal si la base de datos aún no tiene datos. Ingresa al menos una Universidad, un Docente y un Estudiante desde sus respectivos módulos; el árbol se refresca automáticamente.

---

*HURST-Control v1.0 — Desarrollado con Java 21 + JavaFX + Hibernate + MySQL*  
*Universidad Santo Tomás — Proyecto académico*
*Desarrollado por: Pablo Andres Araque Suarez — Harold Leonardo Bautista Vargas — Brayan Felipe Carrillo Guerra — Diego Alejandro Espejo Briceño — Karen Alexandra Torres Burgos*
