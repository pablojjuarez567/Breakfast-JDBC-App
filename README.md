# Breakfast-JDBC-App

Crear una aplicación Java con los métodos CRUD necesarios para gestionar las comandas de los desayunos usando JDBC.

El modelo de datos será muy sencillo: dos tablas, una para almacenar la carta de productos (nombre, tipo, precio, disponibilidad) y otra para almacenar los pedidos que se van realizando (fecha, cliente, estado, producto).

El nombre del alumno cliente se almacenará en una columna de tipo TEXT de la tabla de pedidos.

Estarán vinculadas entre sí a través de la clave primaria de la tabla carta y la clave foránea de la tabla pedidos.  



Las especificaciones son:

Deberá permitir crear un nuevo pedido
Deberá permitir eliminar un pedido existente
Deberá poder marcar un pedido como recogido
Deberá poder listar solo las comandas pendientes de hoy
Deberá poder listar la carta disponible antes de crear la nueva comanda
Deberá poder listar el total de pedidos asociados a un alumno.
Deberá poder mostrar un resumen con algunos indicadores estadísticos que debéis definir (por ejemplo ganancias del último mes, total de clientes, mejor cliente, total de pedidos en la última semana, producto más vendido,...).
La interacción con el usuario se realizará a través de un menú desde linea de comando.
