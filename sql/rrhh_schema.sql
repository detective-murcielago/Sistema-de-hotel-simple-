-- =====================================================================
--  Módulo RRHH - Tablas nuevas (F-017, F-018, F-019, F-020)
--  Base de datos: hotel_trugarden
--  Ejecutar este script UNA VEZ, después de tener creada la tabla `empleado`.
-- =====================================================================

USE hotel_trugarden;

-- ---------------------------------------------------------------------
-- F-017: turno programado por empleado (necesario para validar marcaje)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS turno_empleado (
    id_empleado   INT PRIMARY KEY,
    hora_entrada  TIME NOT NULL,
    hora_salida   TIME NOT NULL,
    activo        TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT fk_turno_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id)
);

-- ---------------------------------------------------------------------
-- F-017: configuración de minutos de tolerancia para la hora de entrada
-- (fila única, id = 1)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS configuracion_asistencia (
    id                  INT PRIMARY KEY,
    minutos_tolerancia  INT NOT NULL DEFAULT 10
);
INSERT IGNORE INTO configuracion_asistencia (id, minutos_tolerancia) VALUES (1, 10);

-- ---------------------------------------------------------------------
-- F-017: registro de marcajes de asistencia
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS asistencia (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado      INT NOT NULL,
    nombre_empleado  VARCHAR(150) NOT NULL,
    tipo_marca       VARCHAR(10) NOT NULL,          -- ENTRADA / SALIDA
    fecha_hora       DATETIME NOT NULL,
    estado           VARCHAR(30) NOT NULL,          -- Asistencia Puntual / Tardanza / Rechazado
    minutos_retraso  INT NOT NULL DEFAULT 0,
    observacion      VARCHAR(255),
    CONSTRAINT fk_asistencia_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id)
);

-- ---------------------------------------------------------------------
-- F-017: bitácora de auditoría de RR.HH. (intentos fallidos de marcaje)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bitacora_auditoria (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado      INT,
    nombre_empleado  VARCHAR(150),
    mensaje          VARCHAR(255) NOT NULL,
    fecha_hora       DATETIME NOT NULL
);

-- ---------------------------------------------------------------------
-- F-018: historial de cargos / puestos ocupados por cada empleado
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS historial_cargo (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado      INT NOT NULL,
    cargo            VARCHAR(100) NOT NULL,
    departamento     VARCHAR(100) NOT NULL,
    sueldo_asignado  DECIMAL(10,2) NOT NULL,
    fecha_inicio     DATE NOT NULL,
    fecha_fin        DATE,
    CONSTRAINT fk_historial_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id)
);

-- ---------------------------------------------------------------------
-- F-019: solicitudes de vacaciones / permisos
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS permiso (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado       INT NOT NULL,
    nombre_empleado   VARCHAR(150) NOT NULL,
    tipo              VARCHAR(30) NOT NULL,         -- Vacaciones / Permiso
    fecha_inicio      DATE NOT NULL,
    fecha_fin         DATE NOT NULL,
    motivo            VARCHAR(255),
    estado            VARCHAR(20) NOT NULL DEFAULT 'Pendiente', -- Pendiente/Aprobada/Rechazada
    fecha_solicitud   DATETIME NOT NULL,
    comentario_rrhh   VARCHAR(255),
    CONSTRAINT fk_permiso_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id)
);

-- ---------------------------------------------------------------------
-- F-020: evaluaciones de desempeño
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS evaluacion_desempeno (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado        INT NOT NULL,
    nombre_empleado    VARCHAR(150) NOT NULL,
    tipo_calificacion  VARCHAR(20) NOT NULL,        -- Numerica / Cualitativa
    calificacion       VARCHAR(50) NOT NULL,
    observaciones      TEXT,
    evaluador          VARCHAR(150) NOT NULL,
    fecha_evaluacion   DATE NOT NULL,
    CONSTRAINT fk_evaluacion_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id)
);
