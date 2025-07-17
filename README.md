# ProyectoFinalNoam
# InvenTrack Pro

**InvenTrack Pro** es una aplicación de gestión de inventario diseñada para pequeñas y medianas empresas. Permite controlar productos, stock, clientes, proveedores y transacciones de compra y venta, con un sistema de usuarios.

## Funcionalidades principales

- Autenticación segura por usuario
- Gestión completa de productos (crear, editar, eliminar, buscar)
- Soporte para múltiples almacenes
- Gestión de clientes y proveedores
- Panel de control interactivo con gráficos (productos bajos en stock, distribución de stock, etc.)
- Registro de compras y ventas (actualización automática del stock)
- Cálculo automático de cantidades por producto y almacén
- Alerta visual para productos bajo el stock mínimo (en rojo)
- Historial de transacciones
- Interfaz clara y moderna con barra lateral

## Tecnologías utilizadas

- **Lenguaje**: Java (Swing)
- **Arquitectura**: MVVM (Model – View – ViewModel)
- **Base de datos**: MySQL con XAMPP (Puerto: 3306, Base de datos: `inventaire_db`)
- **IDE recomendado**: NetBeans

## Estructura del proyecto

- `modelo/`: clases de entidad (`Producto`, `Cliente`, `Proveedor`, `Usuario`, `Transaccion`, `Stock`, etc.)
- `dao/`: clases DAO para conexión y operaciones con MySQL
- `viewmodel/`: lógica central del negocio
- `vista/`: interfaces gráficas (JPanels y JFrame)
- `util/`: conexión con la base de datos
- `com.mycompany.inventario/`: clase principal

## Requisitos previos

- Java 8 o superior
- XAMPP (MySQL 5.7+)
- NetBeans u otro IDE Java
- Navegador para acceder a phpMyAdmin (localhost/phpmyadmin)

## Funcionalidades principales del sistema

Inicio de sesión y gestión de cuentas:
-Inicio de sesión seguro con ID y contraseña.
-Creación de nuevos usuarios desde la interfaz.

Panel de navegación intuitivo:
-Menú lateral que permite acceder rápidamente a las secciones: Productos, Clientes, Proveedores, Stock, Transacciones y Dashboard.

Gestión de proveedores:
-Visualización, creación, edición y eliminación de proveedores.
-Datos manejados: nombre, teléfono, correo y dirección.

Gestión de clientes:
-Visualización, creación, edición y eliminación de clientes.
-Datos manejados: nombre, teléfono, correo y dirección.

Gestión de productos:
-Visualización, creación, edición y eliminación de productos.
-Datos manejados: código, nombre, descripción, stock mínimo, proveedor y nota.

Gestión de almacenes y stock:
-Creación de nuevos almacenes.
-Visualización de stock por producto y por almacén.
-Búsqueda rápida de stock por producto.
-Edición de cantidades, eliminación de entradas y color rojo para productos bajo stock mínimo.

Gestión de transacciones:
-Registro de compras y ventas.
-Selección de almacén, proveedor o cliente, producto y cantidad.
-Visualización de todas las transacciones del usuario y posibilidad de eliminarlas.
-Actualización automática del stock tras cada transacción.

Dashboard:
-Estadísticas globales: cantidad total de productos, clientes, proveedores y productos bajo stock mínimo.
-Gráfico de distribución de productos entre los distintos almacenes.

Cierre de sesión:
-Opción para cerrar sesión de forma segura y volver al inicio.

## Autor

Desarrollado como proyecto académico por **Noam Grea**, 2025.
