-- =====================================================================
--  TruGarden Hotel · Módulo RRHH
--  Migración: Plantilla/Dotación (topes por rol) + Horario semanal
--  Compatible con la BD existente hotel_trugarden (MySQL 8 / InnoDB)
--
--  Ejecutar sobre la base ya creada:
--    USE hotel_trugarden;  luego correr este script completo.
--
--  Diseño:
--   * No se toca `empleado` ni `turno_empleado` (usados por Asistencia).
--   * `rol_plantilla` guarda el tope máximo de cada rol -> límites por DATO,
--     configurables sin recompilar.
--   * `horario_semanal` guarda 1 fila por (empleado, día de semana) con
--     entrada/salida u opción de descanso.
--   * Triggers refuerzan los topes a nivel BD (defensa en profundidad);
--     la capa Java también valida para dar mensajes amigables.
-- =====================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------------------
-- 1) Catálogo de roles con su tope máximo de dotación
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `rol_plantilla`;
CREATE TABLE `rol_plantilla` (
  `rol`        VARCHAR(50)  NOT NULL,
  `max_empleados` INT       NOT NULL,
  PRIMARY KEY (`rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- Topes solicitados. (Chef queda con un tope amplio por no haberse pedido límite.)
INSERT INTO `rol_plantilla` (`rol`, `max_empleados`) VALUES
  ('Recepcionista',    3),
  ('Limpieza',         2),
  ('Jefe de Almacen',  2),
  ('Jefe de Compras',  2),
  ('Gerente General',  1),
  ('Chef',             5);

-- ---------------------------------------------------------------------
-- 2) Horario semanal (7 días) por empleado
--    dia_semana: 1=Lunes ... 7=Domingo (ISO)
-- ---------------------------------------------------------------------
DROP TABLE IF EXISTS `horario_semanal`;
CREATE TABLE `horario_semanal` (
  `id`           INT NOT NULL AUTO_INCREMENT,
  `id_empleado`  INT NOT NULL,
  `dia_semana`   TINYINT NOT NULL,          -- 1..7 (Lun..Dom)
  `hora_entrada` TIME NULL,                 -- NULL si es descanso
  `hora_salida`  TIME NULL,                 -- NULL si es descanso
  `descanso`     TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_emp_dia` (`id_empleado`, `dia_semana`),
  CONSTRAINT `fk_horario_empleado`
      FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
      ON DELETE CASCADE,
  CONSTRAINT `ck_horario_dia`
      CHECK (`dia_semana` BETWEEN 1 AND 7),
  CONSTRAINT `ck_horario_coherencia`
      CHECK (
        (`descanso` = 1 AND `hora_entrada` IS NULL AND `hora_salida` IS NULL)
        OR
        (`descanso` = 0 AND `hora_entrada` IS NOT NULL AND `hora_salida` IS NOT NULL
         AND `hora_salida` > `hora_entrada`)
      )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ---------------------------------------------------------------------
-- 3) Refuerzo de topes por rol a nivel BD.
--
--    IMPORTANTE: en MySQL un trigger NO puede consultar (SELECT COUNT)
--    la misma tabla que lo dispara. Por eso se usa una tabla contadora
--    `rol_conteo` que se mantiene con triggers AFTER, y el BEFORE INSERT
--    valida contra ese contador (no contra `empleado`).
--
--    La capa Java (EmpleadoDAO.hayCupo) valida antes para dar mensajes
--    amigables; esto es la última línea de defensa.
-- ---------------------------------------------------------------------

DROP TABLE IF EXISTS `rol_conteo`;
CREATE TABLE `rol_conteo` (
  `rol`       VARCHAR(50) NOT NULL,
  `ocupados`  INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- Inicializa el contador con los empleados ya existentes.
INSERT INTO `rol_conteo` (`rol`, `ocupados`)
SELECT rp.`rol`, COALESCE(c.n, 0)
FROM `rol_plantilla` rp
LEFT JOIN (SELECT `rol`, COUNT(*) n FROM `empleado` GROUP BY `rol`) c
       ON c.`rol` = rp.`rol`;

DROP TRIGGER IF EXISTS `trg_empleado_tope_ins`;
DROP TRIGGER IF EXISTS `trg_empleado_cnt_ins`;
DROP TRIGGER IF EXISTS `trg_empleado_cnt_del`;
DROP TRIGGER IF EXISTS `trg_empleado_cnt_upd`;

DELIMITER $$

-- BEFORE INSERT: valida el tope leyendo el contador (tabla distinta).
CREATE TRIGGER `trg_empleado_tope_ins`
BEFORE INSERT ON `empleado`
FOR EACH ROW
BEGIN
  DECLARE v_max INT;
  DECLARE v_cnt INT;
  SELECT `max_empleados` INTO v_max FROM `rol_plantilla` WHERE `rol` = NEW.`rol`;
  IF v_max IS NOT NULL THEN
    SELECT `ocupados` INTO v_cnt FROM `rol_conteo` WHERE `rol` = NEW.`rol`;
    IF v_cnt IS NULL THEN SET v_cnt = 0; END IF;
    IF v_cnt >= v_max THEN
      SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Se alcanzo el maximo de empleados para este rol.';
    END IF;
  END IF;
END$$

-- AFTER INSERT: incrementa el contador del rol.
CREATE TRIGGER `trg_empleado_cnt_ins`
AFTER INSERT ON `empleado`
FOR EACH ROW
BEGIN
  INSERT INTO `rol_conteo` (`rol`, `ocupados`) VALUES (NEW.`rol`, 1)
    ON DUPLICATE KEY UPDATE `ocupados` = `ocupados` + 1;
END$$

-- AFTER DELETE: decrementa.
CREATE TRIGGER `trg_empleado_cnt_del`
AFTER DELETE ON `empleado`
FOR EACH ROW
BEGIN
  UPDATE `rol_conteo` SET `ocupados` = GREATEST(0, `ocupados` - 1)
   WHERE `rol` = OLD.`rol`;
END$$

-- AFTER UPDATE de rol: ajusta ambos contadores.
CREATE TRIGGER `trg_empleado_cnt_upd`
AFTER UPDATE ON `empleado`
FOR EACH ROW
BEGIN
  IF NEW.`rol` <> OLD.`rol` THEN
    UPDATE `rol_conteo` SET `ocupados` = GREATEST(0, `ocupados` - 1)
     WHERE `rol` = OLD.`rol`;
    INSERT INTO `rol_conteo` (`rol`, `ocupados`) VALUES (NEW.`rol`, 1)
      ON DUPLICATE KEY UPDATE `ocupados` = `ocupados` + 1;
  END IF;
END$$

DELIMITER ;

-- NOTA sobre el tope en UPDATE de rol: si necesitas bloquear también los
-- cambios de rol que excedan el máximo, hazlo en la capa Java (EmpleadoDAO)
-- antes del UPDATE, ya que un BEFORE UPDATE tampoco puede leer `rol_conteo`
-- de forma consistente durante la misma transacción del propio conteo.

-- ---------------------------------------------------------------------
-- 4) Vista de dotación: ocupados vs máximo por rol
-- ---------------------------------------------------------------------
DROP VIEW IF EXISTS `v_dotacion`;
CREATE VIEW `v_dotacion` AS
SELECT
  rp.`rol`                          AS `rol`,
  rp.`max_empleados`                AS `maximo`,
  COALESCE(e.`ocupados`, 0)         AS `ocupados`,
  (rp.`max_empleados` - COALESCE(e.`ocupados`, 0)) AS `disponibles`
FROM `rol_plantilla` rp
LEFT JOIN (
  SELECT `rol`, COUNT(*) AS `ocupados`
  FROM `empleado`
  GROUP BY `rol`
) e ON e.`rol` = rp.`rol`;

SET FOREIGN_KEY_CHECKS = 1;
