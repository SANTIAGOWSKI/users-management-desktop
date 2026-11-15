-- =============================================================
-- Backup de la base de datos: crud_usuarios
-- Motor: MySQL 8.x
-- Proyecto: users-management-desktop (Arquitectura Hexagonal)
-- Entidad del CEA: Student (Ejercicio 16 - Examenes academicos)
--
-- Como restaurarlo (desde la terminal):
--   mysql -u root -p < database/backup_crud_usuarios.sql
-- =============================================================

/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;

CREATE DATABASE IF NOT EXISTS crud_usuarios
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE crud_usuarios;

-- -------------------------------------------------------------
-- Tabla: users
-- -------------------------------------------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        ENUM('ADMIN', 'MEMBER', 'REVIEWER') NOT NULL,
    status      ENUM('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES users WRITE;
INSERT INTO users (id, name, email, password, role, status) VALUES
    ('00000000-0000-0000-0000-000000000001',
     'Administrador',
     'admin@example.com',
     '$2a$12$placeholderHashReplaceWithRealBCryptHash',
     'ADMIN',
     'ACTIVE');
UNLOCK TABLES;

-- -------------------------------------------------------------
-- Tabla: students
-- -------------------------------------------------------------
DROP TABLE IF EXISTS students;
CREATE TABLE students (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    group_name  VARCHAR(50)  NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES students WRITE;
INSERT INTO students (id, name, group_name) VALUES
    ('2024001', 'Laura Martinez',  'G1'),
    ('2024002', 'Carlos Perez',    'G1'),
    ('2024003', 'Valentina Ruiz',  'G2');
UNLOCK TABLES;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
