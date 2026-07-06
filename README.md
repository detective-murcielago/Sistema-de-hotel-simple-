# 🏨 Sistema de Gestión Hotelera en Java con Interfaz Gráfica (Swing)

Este proyecto es una **aplicación de escritorio en Java con interfaz gráfica usando Swing**, diseñada para gestionar de manera eficiente las operaciones básicas de un hotel. Permite registrar huéspedes, empleados, habitaciones, hospedajes y generar reportes de ventas.

##  Funcionalidades principales

- Registro, búsqueda y listado de **huéspedes**
-  Registro, búsqueda y listado de **empleados**
-  Gestión de **habitaciones** (registro, mantenimiento, eliminación)
-  Manejo de **fichas de hospedaje** (check-in y check-out)
-  **Reporte de ventas** por día
-  Validación de duplicados por DNI, ID o teléfono
-  Interfaz gráfica intuitiva construida con **Java Swing**

##  Interfaz Gráfica (Java Swing)

La aplicación cuenta con múltiples ventanas (JFrames y JPanels) que permiten al usuario:

- Ingresar datos de huéspedes, empleados y habitaciones
- Buscar registros por DNI o ID
- Consultar el estado de una habitación
- Generar y visualizar reportes

> La interfaz gráfica está implementada completamente con **Swing**, sin frameworks externos.

## 📂 Estructura del proyecto

ProyectoHotel/
│
├── controlador/
│ └── Hotel.java # Clase principal con la lógica del sistema
│
├── Entidades/
│ ├── Huesped.java
│ ├── Empleado.java
│ ├── Habitacion.java
│ └── FichaHospedaje.java
│
├── interfaz/ # Paquete con las clases de la interfaz gráfica
│ ├── login.java
│ └── ...
│
└── README.md


##  Tecnologías utilizadas

- **Java SE 8 o superior**
- **Java Swing** para la interfaz gráfica
- `ArrayList` para colecciones dinámicas
- `java.time.LocalDate` para gestión de fechas
- IDE sugerido: **NetBeans**, **IntelliJ IDEA** o **Eclipse**

##  Cómo ejecutar el proyecto

1. Abre el proyecto en tu IDE (NetBeans, IntelliJ, etc.)
2. Compila el proyecto
3. Ejecuta la clase principal de la interfaz:

```java
public class Main {
    public static void main(String[] args) {
        new login().setVisible(true); // Lanza la ventana principal
    }
}
👤 Autor
Desarrollado por: detective-murcielago
📅 Fecha: Julio 2025
