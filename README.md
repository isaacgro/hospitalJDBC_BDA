# Sistema de Gestión de Citas Médicas

## Descripción del Proyecto
El Sistema de Gestión de Citas Médicas es una aplicación Java diseñada para administrar citas médicas entre pacientes y médicos en un entorno clínico u hospitalario. Este sistema permite registrar pacientes, médicos y agendar citas médicas, facilitando la organización y gestión del servicio de atención médica.

## Características Principales
- Registro y gestión de pacientes
- Registro y gestión de médicos con sus especialidades
- Agendamiento de citas médicas
- Control de horarios de atención médica
- Validación de disponibilidad de citas
- Gestión de estados de citas (pendiente, confirmada, cancelada, etc.)

## Arquitectura del Sistema
El proyecto está estructurado siguiendo el patrón de arquitectura DAO (Data Access Object), lo que permite separar la lógica de acceso a datos de la lógica de negocio. Los componentes principales son:

### Conexión a Base de Datos
- `Conexion.Conexion`: Implementación de la conexión a la base de datos
- `Conexion.IConexion`: Interfaz que define los métodos de conexión

### Entidades
- `Entidades.Usuario`: Clase base que contiene información común de usuarios
- `Entidades.Paciente`: Información relacionada con los pacientes
- `Entidades.Medico`: Información relacionada con los médicos
- `Entidades.Horario`: Gestión de horarios de atención médica
- `Entidades.Cita`: Datos de las citas médicas

### Acceso a Datos
- `DAO.CitaDAO`: Objeto de acceso a datos para las operaciones con citas

### Excepciones
- `Excepciones.PersistenciaExcption`: Manejo de errores relacionados con la persistencia de datos

## Ejemplo de Uso
El archivo Main.java muestra un ejemplo básico de cómo utilizar el sistema:
1. Crear una conexión a la base de datos
2. Instanciar un DAO para manejar las citas
3. Crear objetos de paciente y médico con sus respectivos datos
4. Generar una cita asociando paciente y médico
5. Registrar la cita en el sistema

## Requisitos del Sistema
- Java 8 o superior
- Sistema de gestión de base de datos compatible (MySQL, PostgreSQL, etc.)
- Dependencias especificadas en el archivo de configuración

## Instalación y Configuración
1. Clonar el repositorio
2. Configurar los parámetros de conexión a la base de datos en el archivo de configuración
3. Compilar el proyecto
4. Ejecutar la aplicación

## Próximas Mejoras
- Implementación de interfaz gráfica
- Gestión de permisos y roles
- Sistema de notificaciones para citas
- Historial médico de pacientes
- Reportes estadísticos

## Licencia
[Especificar licencia del proyecto]

## Contacto
[Información de contacto para soporte o contribuciones]