-- ============================================================
--  Tabla: consumo_insumo  (KARDEX de salidas por consumo real)
--  Registra cada baja de inventario hecha por Limpieza o Cocina.
--  Ejecutar UNA vez sobre la base de datos hotel_trugarden.
-- ============================================================
USE hotel_trugarden;

CREATE TABLE IF NOT EXISTS `consumo_insumo` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `producto`    VARCHAR(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `cantidad`    INT NOT NULL,
  `area`        VARCHAR(20)  COLLATE utf8mb4_spanish_ci NOT NULL, -- 'LIMPIEZA' | 'COCINA'
  `referencia`  VARCHAR(50)  COLLATE utf8mb4_spanish_ci,          -- nro habitación o id pedido
  `responsable` VARCHAR(100) COLLATE utf8mb4_spanish_ci,          -- empleado que registra
  `fecha`       DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_consumo_producto` (`producto`),
  KEY `idx_consumo_area` (`area`),
  KEY `idx_consumo_fecha` (`fecha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
