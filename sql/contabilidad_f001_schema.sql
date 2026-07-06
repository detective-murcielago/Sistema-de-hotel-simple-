-- =====================================================================
--  MODULO CONTABLE - HU F-001 (Gerente General)
--  Generacion automatica de asientos en el LIBRO MAYOR
--  Base: hotel_trugarden  |  Plan de cuentas: PCGE (Peru) simplificado
-- =====================================================================
USE hotel_trugarden;

-- ---------------------------------------------------------------------
-- 1) PLAN DE CUENTAS  (catalogo de cuentas contables)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `mayor_movimiento`;
DROP TABLE IF EXISTS `asiento_contable`;
DROP TABLE IF EXISTS `mapeo_cuenta_servicio`;
DROP TABLE IF EXISTS `log_contable`;
DROP TABLE IF EXISTS `cuenta_contable`;

CREATE TABLE `cuenta_contable` (
  `codigo`      VARCHAR(10)  NOT NULL,                 -- ej. 1011, 7031
  `nombre`      VARCHAR(120) NOT NULL,
  `tipo`        ENUM('ACTIVO','PASIVO','PATRIMONIO','INGRESO','GASTO') NOT NULL,
  `naturaleza`  ENUM('DEUDORA','ACREEDORA') NOT NULL,  -- saldo normal
  `activa`      TINYINT(1)   NOT NULL DEFAULT 1,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

INSERT INTO `cuenta_contable` VALUES
 ('1011','Caja - Efectivo','ACTIVO','DEUDORA',1),
 ('1041','Cuentas corrientes - Tarjeta/POS','ACTIVO','DEUDORA',1),
 ('1042','Cuentas corrientes - Pagos QR/Digital','ACTIVO','DEUDORA',1),
 ('7031','Ventas - Servicio de Hospedaje','INGRESO','ACREEDORA',1),
 ('7041','Ventas - Servicio de Restaurante','INGRESO','ACREEDORA',1),
 ('7049','Ventas - Otros servicios','INGRESO','ACREEDORA',1);

-- ---------------------------------------------------------------------
-- 2) MAPEO SERVICIO -> CUENTA DE INGRESO (configurable desde el JFrame)
--    Si un servicio NO esta aqui => el asiento queda PENDIENTE_ASIENTO
-- ---------------------------------------------------------------------
CREATE TABLE `mapeo_cuenta_servicio` (
  `id_mapeo`       INT NOT NULL AUTO_INCREMENT,
  `servicio`       VARCHAR(80) NOT NULL,              -- coincide con pago_servicio.servicio
  `cuenta_ingreso` VARCHAR(10) NOT NULL,              -- FK a cuenta_contable (HABER)
  PRIMARY KEY (`id_mapeo`),
  UNIQUE KEY `uq_map_serv` (`servicio`),
  CONSTRAINT `fk_map_cuenta` FOREIGN KEY (`cuenta_ingreso`)
     REFERENCES `cuenta_contable` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- Mapeos base (ejemplo). "Lavanderia" queda SIN mapear a proposito
-- para poder demostrar el escenario 3 de la HU.
INSERT INTO `mapeo_cuenta_servicio` (`servicio`,`cuenta_ingreso`) VALUES
 ('Hospedaje','7031'),
 ('Habitacion','7031'),
 ('Desayuno','7041'),
 ('Restaurante','7041'),
 ('Almuerzo','7041'),
 ('Cena','7041');

-- ---------------------------------------------------------------------
-- 3) ASIENTO CONTABLE  (cabecera del asiento en el Libro Diario/Mayor)
-- ---------------------------------------------------------------------
CREATE TABLE `asiento_contable` (
  `id_asiento`     INT NOT NULL AUTO_INCREMENT,
  `fecha`          DATETIME    NOT NULL,
  `glosa`          VARCHAR(200) NOT NULL,
  `tipo`           ENUM('NORMAL','REVERSION') NOT NULL DEFAULT 'NORMAL',
  `estado`         ENUM('REGISTRADO','PENDIENTE_ASIENTO','ANULADO') NOT NULL DEFAULT 'REGISTRADO',
  `id_pago`        INT NULL,                           -- pago origen (pago_servicio)
  `id_asiento_ref` INT NULL,                           -- si es reversion: apunta al original
  `total_debe`     DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `total_haber`    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_asiento`),
  KEY `idx_as_pago` (`id_pago`),
  KEY `idx_as_ref`  (`id_asiento_ref`),
  CONSTRAINT `fk_as_ref` FOREIGN KEY (`id_asiento_ref`)
     REFERENCES `asiento_contable` (`id_asiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ---------------------------------------------------------------------
-- 4) MOVIMIENTO DEL LIBRO MAYOR (lineas Debe/Haber de cada asiento)
-- ---------------------------------------------------------------------
CREATE TABLE `mayor_movimiento` (
  `id_mov`      INT NOT NULL AUTO_INCREMENT,
  `id_asiento`  INT NOT NULL,
  `cuenta`      VARCHAR(10) NOT NULL,
  `debe`        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `haber`       DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`id_mov`),
  KEY `idx_mov_as` (`id_asiento`),
  KEY `idx_mov_cta` (`cuenta`),
  CONSTRAINT `fk_mov_asiento` FOREIGN KEY (`id_asiento`)
     REFERENCES `asiento_contable` (`id_asiento`) ON DELETE CASCADE,
  CONSTRAINT `fk_mov_cuenta`  FOREIGN KEY (`cuenta`)
     REFERENCES `cuenta_contable` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ---------------------------------------------------------------------
-- 5) LOG CONTABLE (trazabilidad de errores/excepciones -> escenario 3)
-- ---------------------------------------------------------------------
CREATE TABLE `log_contable` (
  `id_log`     INT NOT NULL AUTO_INCREMENT,
  `fecha`      DATETIME NOT NULL,
  `nivel`      ENUM('INFO','ADVERTENCIA','ERROR') NOT NULL DEFAULT 'INFO',
  `id_pago`    INT NULL,
  `servicio`   VARCHAR(80) NULL,
  `mensaje`    VARCHAR(300) NOT NULL,
  `resuelto`   TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_log`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ---------------------------------------------------------------------
-- 6) VISTA: LIBRO MAYOR consolidado (para reportes / saldos por cuenta)
-- ---------------------------------------------------------------------
DROP VIEW IF EXISTS `v_libro_mayor`;
CREATE VIEW `v_libro_mayor` AS
SELECT m.id_mov,
       a.id_asiento,
       a.fecha,
       a.tipo,
       a.estado,
       m.cuenta,
       c.nombre  AS nombre_cuenta,
       m.debe,
       m.haber,
       a.glosa
FROM mayor_movimiento m
JOIN asiento_contable a ON a.id_asiento = m.id_asiento
JOIN cuenta_contable  c ON c.codigo     = m.cuenta
ORDER BY a.fecha, a.id_asiento, m.id_mov;
