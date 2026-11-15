# users-menagement-desktop-hezagonal-ddd
Ejemplo de una aplicación Desktop que hace CRUDL y control de acceso de Usuarios, escrita en Java,  arquitectura Hexagonal y enfoque DDD, usando buenas practicas y Clean code
Este es un ejemplo de Proyecto Java con hexagonal para los alumnos de UdeC

---

## Extensión: entidad `Student` (CEA Ejercicio 16 - Exámenes académicos)

Se extendió el proyecto con la entidad **Student** (estudiante de Computación II), con las
operaciones CRUDL completas: crear, buscar por id, actualizar, eliminar y listar.
Los casos de uso originales de `User` no se modificaron.

### Dónde está cada cosa (Arquitectura Hexagonal)

| Pieza | Ubicación |
| --- | --- |
| Entidad y objetos de valor | `domain/model/StudentModel`, `domain/valueobject/Student*` |
| Puertos de entrada (casos de uso) | `application/port/in/*Student*UseCase` |
| Puertos de salida | `application/port/out/*Student*Port` |
| Servicios (lógica de cada caso de uso) | `application/service/*StudentService` |
| Adaptador de entrada (CLI) | `infrastructure/entrypoint/desktop/cli/handler/*Student*Handler` |
| Adaptador de salida en memoria | `infrastructure/adapter/persistence/repository/StudentRepositoryMemory` |
| Adaptador de salida MySQL (JDBC, sin ORM) | `infrastructure/adapter/persistence/repository/StudentRepositoryMySQL` |
| Conexión de todas las piezas | `infrastructure/config/DependencyContainer` |

Para cambiar de adaptador basta con editar **una línea** en `DependencyContainer`
(`StudentRepositoryMySQL` ↔ `StudentRepositoryMemory`); los casos de uso no se enteran.

### Base de datos

1. Tener MySQL 8 instalado en el equipo.
2. Restaurar el backup incluido: `mysql -u root -p < database/backup_crud_usuarios.sql`
3. Completar `src/main/resources/application.properties` con el host, puerto, nombre
   (`crud_usuarios`), usuario y contraseña de MySQL.

### Pruebas

```
mvn test
```
