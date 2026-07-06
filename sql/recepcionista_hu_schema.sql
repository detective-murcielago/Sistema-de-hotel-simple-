-- ============================================================
--  Cambios de BD para las Historias de Usuario del Recepcionista
--  F-004 / F-011  -> preferencia_huesped
--  F-010          -> pago_servicio  (relacionado con la ficha y la caja)
--  Corrección 5   -> columna 'motivo' en turno_caja
--  Base de datos: TRU_GARDEN
--
--  Ejecutar UNA vez en MySQL Workbench o consola:
--      SOURCE sql/recepcionista_hu_schema.sql;
-- ============================================================

USE `TRU_GARDEN`;

-- ------------------------------------------------------------
-- F-004 / F-011 : Preferencias del huésped
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS preferencia_huesped (
    id_preferencia   INT AUTO_INCREMENT PRIMARY KEY,
    num_documento    VARCHAR(20)  NOT NULL,
    tipo_preferencia VARCHAR(60)  NOT NULL,
    detalle          VARCHAR(255) NOT NULL,
    fecha_registro   DATETIME     NOT NULL,
    INDEX idx_pref_doc (num_documento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------
-- F-010 : Pago de servicios contratados
--   El servicio será siempre "Pago total": el importe total de la
--   ficha de hospedaje activa del huésped, cobrado antes de dar
--   las llaves. id_ficha da trazabilidad pago <-> ficha.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pago_servicio (
    id_pago        INT AUTO_INCREMENT PRIMARY KEY,
    num_documento  VARCHAR(20)   NOT NULL,
    servicio       VARCHAR(80)   NOT NULL,
    metodo_pago    VARCHAR(30)   NOT NULL,
    monto          DECIMAL(10,2) NOT NULL,
    comprobante    VARCHAR(40)   NOT NULL,
    fecha_pago     DATETIME      NOT NULL,
    id_ficha       VARCHAR(20)   NULL,
    INDEX idx_pago_doc (num_documento),
    INDEX idx_pago_ficha (id_ficha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------
-- Corrección 5 : motivo de diferencia en el cierre de caja
--   (por qué falta o sobra dinero). Se agrega a turno_caja.
-- ------------------------------------------------------------
--  Si tu MySQL es 8.0+, puedes usar IF NOT EXISTS. Si da error,
--  ejecuta solo la línea ALTER sin IF NOT EXISTS una sola vez.
ALTER TABLE turno_caja
    ADD COLUMN IF NOT EXISTS motivo VARCHAR(255) NOT NULL DEFAULT '';

-- ============================================================
--  INTEGRIDAD REFERENCIAL (tu tabla huesped ya tiene UNIQUE en
--  num_documento: uq_hue_documento). Ejecutar UNA sola vez.
-- ============================================================
ALTER TABLE preferencia_huesped
    ADD CONSTRAINT fk_pref_huesped FOREIGN KEY (num_documento)
    REFERENCES huesped(num_documento) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE pago_servicio
    ADD CONSTRAINT fk_pago_huesped FOREIGN KEY (num_documento)
    REFERENCES huesped(num_documento) ON UPDATE CASCADE ON DELETE CASCADE;

-- La FK a ficha_hospedaje es opcional (id_ficha puede ser NULL):
ALTER TABLE pago_servicio
    ADD CONSTRAINT fk_pago_ficha FOREIGN KEY (id_ficha)
    REFERENCES ficha_hospedaje(id_ficha) ON UPDATE CASCADE ON DELETE SET NULL;
