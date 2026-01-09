USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = 'BD_USERS')
BEGIN
    ALTER DATABASE BD_USERS SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE BD_USERS;
END
GO

CREATE DATABASE BD_USERS;
GO

USE BD_USERS;
GO

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    estado BIT NOT NULL DEFAULT 1, -- 1 para True, 0 para False
    fecha_creacion DATETIME DEFAULT GETDATE()
);
GO

-- Usuario de prueba (Password: admin123)
-- Importante: El hash debe ser generado por BCrypt en tu Backend
INSERT INTO usuarios (username, email, password, estado, fecha_creacion) 
VALUES ('admin', 'admin@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uqqCyS', 1, GETDATE());
GO