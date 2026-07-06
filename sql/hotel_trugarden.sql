-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: hotel_trugarden
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `nombre_empleado` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo_marca` varchar(10) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `estado` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  `minutos_retraso` int NOT NULL DEFAULT '0',
  `observacion` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_asistencia_empleado` (`id_empleado`),
  CONSTRAINT `fk_asistencia_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (1,1234,'Victor vejarano','ENTRADA','2026-07-01 17:32:42','Asistencia Puntual',0,NULL);
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bitacora_auditoria`
--

DROP TABLE IF EXISTS `bitacora_auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bitacora_auditoria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int DEFAULT NULL,
  `nombre_empleado` varchar(150) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `mensaje` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_hora` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bitacora_auditoria`
--

LOCK TABLES `bitacora_auditoria` WRITE;
/*!40000 ALTER TABLE `bitacora_auditoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `bitacora_auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprobante_pago`
--

DROP TABLE IF EXISTS `comprobante_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprobante_pago` (
  `codigo` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_ficha` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `importe_total` decimal(10,2) NOT NULL,
  `fecha_emision` datetime NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `uq_comp_ficha` (`id_ficha`),
  CONSTRAINT `fk_comp_ficha` FOREIGN KEY (`id_ficha`) REFERENCES `ficha_hospedaje` (`id_ficha`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprobante_pago`
--

LOCK TABLES `comprobante_pago` WRITE;
/*!40000 ALTER TABLE `comprobante_pago` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprobante_pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_asistencia`
--

DROP TABLE IF EXISTS `configuracion_asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_asistencia` (
  `id` int NOT NULL,
  `minutos_tolerancia` int NOT NULL DEFAULT '10',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_asistencia`
--

LOCK TABLES `configuracion_asistencia` WRITE;
/*!40000 ALTER TABLE `configuracion_asistencia` DISABLE KEYS */;
INSERT INTO `configuracion_asistencia` VALUES (1,10);
/*!40000 ALTER TABLE `configuracion_asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `id` int NOT NULL,
  `id_hotel` int NOT NULL DEFAULT '1',
  `rol` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `sueldo` decimal(10,2) NOT NULL DEFAULT '0.00',
  `correo` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `inicio_contrato` date NOT NULL,
  `fin_contrato` date DEFAULT NULL,
  `nombre` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `apellido` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `num_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `telefono` int NOT NULL,
  `direccion` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_emp_documento` (`num_documento`),
  UNIQUE KEY `uq_emp_telefono` (`telefono`),
  UNIQUE KEY `uq_emp_correo` (`correo`),
  KEY `fk_emp_hotel` (`id_hotel`),
  CONSTRAINT `fk_emp_hotel` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id`),
  CONSTRAINT `ck_emp_rol` CHECK ((`rol` in (_utf8mb4'Recepcionista',_utf8mb4'Chef',_utf8mb4'Jefe de Almacen',_utf8mb4'Jefe de Compras',_utf8mb4'Gerente General',_utf8mb4'Limpieza')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (1,1,'Recepcionista',2000.00,'ana.gomez@trugarden.pe','2024-01-01',NULL,'Ana','Gomez','DNI','74521300',987654321,'Av. Los Pinos 123'),(2,1,'Chef',2500.00,'mario.rios@trugarden.pe','2024-01-01',NULL,'Mario','Rios','DNI','74521301',987654322,'Jr. Las Flores 45'),(3,1,'Jefe de Almacen',2800.00,'jefa.almacen@trugarden.pe','2024-01-01',NULL,'Rosa','Perez','DNI','74521303',987654324,'Ca. Las Palmas 10'),(4,1,'Gerente General',5000.00,'luis.vera@trugarden.pe','2023-06-01',NULL,'Luis','Vera','DNI','74521302',987654323,'Calle El Golf 69'),(1234,1,'Recepcionista',1200.00,'Viicc@gmail.com','2026-07-01','2026-12-31','Victor','vejarano','DNI','65412307',963258741,'Av. golf 1');
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacion_desempeno`
--

DROP TABLE IF EXISTS `evaluacion_desempeno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluacion_desempeno` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `nombre_empleado` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo_calificacion` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `calificacion` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `observaciones` text COLLATE utf8mb4_spanish_ci,
  `evaluador` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_evaluacion` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_evaluacion_empleado` (`id_empleado`),
  CONSTRAINT `fk_evaluacion_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacion_desempeno`
--

LOCK TABLES `evaluacion_desempeno` WRITE;
/*!40000 ALTER TABLE `evaluacion_desempeno` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacion_desempeno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ficha_hospedaje`
--

DROP TABLE IF EXISTS `ficha_hospedaje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ficha_hospedaje` (
  `id_ficha` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `numero_habitacion` varchar(10) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_huesped_titular` int NOT NULL,
  `noches_esperadas` int NOT NULL,
  `fecha_ingreso` datetime NOT NULL,
  `fecha_salida` datetime DEFAULT NULL,
  `estado` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'A',
  `cantidad_personas` int NOT NULL DEFAULT '1',
  `incluye_desayuno` tinyint(1) NOT NULL DEFAULT '0',
  `incluye_almuerzo` tinyint(1) NOT NULL DEFAULT '0',
  `incluye_cena` tinyint(1) NOT NULL DEFAULT '0',
  `estado_comida` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'Por entregar',
  `arqueada` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_ficha`),
  KEY `fk_ficha_hab` (`numero_habitacion`),
  KEY `fk_ficha_titular` (`id_huesped_titular`),
  CONSTRAINT `fk_ficha_hab` FOREIGN KEY (`numero_habitacion`) REFERENCES `habitacion` (`numero`),
  CONSTRAINT `fk_ficha_titular` FOREIGN KEY (`id_huesped_titular`) REFERENCES `huesped` (`id`),
  CONSTRAINT `ck_ficha_estado` CHECK ((`estado` in (_utf8mb4'A',_utf8mb4'F')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ficha_hospedaje`
--

LOCK TABLES `ficha_hospedaje` WRITE;
/*!40000 ALTER TABLE `ficha_hospedaje` DISABLE KEYS */;
INSERT INTO `ficha_hospedaje` VALUES ('F-101-2221','101',25,2,'2026-07-02 18:46:35','2026-07-02 18:47:11','F',1,0,0,0,'Por entregar',1),('F-101-564','101',2,1,'2026-06-29 02:43:24','2026-07-01 16:36:20','F',1,0,1,0,'Por entregar',1),('F-102-170','102',25,2,'2026-07-05 19:11:31','2026-07-02 19:13:12','F',1,0,0,0,'Por entregar',0),('F-102-8439','102',1,2,'2026-06-29 02:48:24','2026-06-29 02:49:13','F',1,0,1,0,'Por entregar',1),('F-201-4589','201',25,4,'2026-07-13 19:25:11','2026-07-02 19:26:22','F',1,0,0,0,'Por entregar',0);
/*!40000 ALTER TABLE `ficha_hospedaje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ficha_huesped`
--

DROP TABLE IF EXISTS `ficha_huesped`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ficha_huesped` (
  `id_ficha` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_huesped` int NOT NULL,
  PRIMARY KEY (`id_ficha`,`id_huesped`),
  KEY `fk_fh_huesped` (`id_huesped`),
  CONSTRAINT `fk_fh_ficha` FOREIGN KEY (`id_ficha`) REFERENCES `ficha_hospedaje` (`id_ficha`),
  CONSTRAINT `fk_fh_huesped` FOREIGN KEY (`id_huesped`) REFERENCES `huesped` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ficha_huesped`
--

LOCK TABLES `ficha_huesped` WRITE;
/*!40000 ALTER TABLE `ficha_huesped` DISABLE KEYS */;
INSERT INTO `ficha_huesped` VALUES ('F-102-8439',1),('F-101-564',2),('F-101-2221',25),('F-102-170',25),('F-201-4589',25);
/*!40000 ALTER TABLE `ficha_huesped` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habitacion`
--

DROP TABLE IF EXISTS `habitacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitacion` (
  `numero` varchar(10) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_hotel` int NOT NULL DEFAULT '1',
  `tipo` char(1) COLLATE utf8mb4_spanish_ci NOT NULL,
  `estado` char(1) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'D',
  `precio` decimal(10,2) NOT NULL,
  `capacidad` int NOT NULL DEFAULT '4',
  `descripcion_problema` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT '',
  `encargado_limpieza` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`numero`),
  KEY `fk_hab_hotel` (`id_hotel`),
  CONSTRAINT `fk_hab_hotel` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id`),
  CONSTRAINT `ck_hab_estado` CHECK ((`estado` in (_utf8mb4'D',_utf8mb4'O',_utf8mb4'M'))),
  CONSTRAINT `ck_hab_tipo` CHECK ((`tipo` in (_utf8mb4'S',_utf8mb4'D',_utf8mb4'M')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habitacion`
--

LOCK TABLES `habitacion` WRITE;
/*!40000 ALTER TABLE `habitacion` DISABLE KEYS */;
INSERT INTO `habitacion` VALUES ('101',1,'S','M',50.00,2,'',''),('102',1,'D','M',100.00,4,'',''),('201',1,'M','M',280.00,2,'',''),('202',1,'S','M',150.00,2,'Grifo roto','Mario Rios');
/*!40000 ALTER TABLE `habitacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_cargo`
--

DROP TABLE IF EXISTS `historial_cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_cargo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `cargo` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `departamento` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `sueldo_asignado` decimal(10,2) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_historial_empleado` (`id_empleado`),
  CONSTRAINT `fk_historial_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_cargo`
--

LOCK TABLES `historial_cargo` WRITE;
/*!40000 ALTER TABLE `historial_cargo` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'TruGarden Hotel',
  `direccion` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'av. El Golf 69',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,'TruGarden Hotel','av. El Golf 69');
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `huesped`
--

DROP TABLE IF EXISTS `huesped`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `huesped` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `apellido` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `num_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `telefono` int NOT NULL,
  `direccion` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_hue_documento` (`num_documento`),
  UNIQUE KEY `uq_hue_telefono` (`telefono`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `huesped`
--

LOCK TABLES `huesped` WRITE;
/*!40000 ALTER TABLE `huesped` DISABLE KEYS */;
INSERT INTO `huesped` VALUES (1,'Carlos','Mendoza','DNI','12345678',999111222,'Jr. Lima 100'),(2,'Sofia','Torres','DNI','87654321',999333444,'Av. Arequipa 55'),(25,'Alejandro','Cespedes','DNI','87456123',900365418,'Piura');
/*!40000 ALTER TABLE `huesped` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden_compra`
--

DROP TABLE IF EXISTS `orden_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden_compra` (
  `id_orden` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id_producto` int NOT NULL,
  `id_empleado` int NOT NULL,
  `cantidad` int NOT NULL,
  `fecha_emision` date NOT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `proveedor` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `precio_total` decimal(10,2) NOT NULL,
  `estado` varchar(15) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'Pendiente',
  PRIMARY KEY (`id_orden`),
  KEY `fk_orden_prod` (`id_producto`),
  KEY `fk_orden_emp` (`id_empleado`),
  CONSTRAINT `fk_orden_emp` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`),
  CONSTRAINT `fk_orden_prod` FOREIGN KEY (`id_producto`) REFERENCES `producto` (`id`),
  CONSTRAINT `ck_orden_estado` CHECK ((`estado` in (_utf8mb4'Pendiente',_utf8mb4'Aprobado',_utf8mb4'Rechazado')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden_compra`
--

LOCK TABLES `orden_compra` WRITE;
/*!40000 ALTER TABLE `orden_compra` DISABLE KEYS */;
INSERT INTO `orden_compra` VALUES ('ORD-77070',1,1,10,'2026-07-02','2026-07-02','SIDERAL S.A.',100.00,'Aprobado');
/*!40000 ALTER TABLE `orden_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago_servicio`
--

DROP TABLE IF EXISTS `pago_servicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago_servicio` (
  `id_pago` int NOT NULL AUTO_INCREMENT,
  `num_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `servicio` varchar(80) COLLATE utf8mb4_spanish_ci NOT NULL,
  `metodo_pago` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `comprobante` varchar(40) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_pago` datetime NOT NULL,
  `id_ficha` varchar(20) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `idx_pago_doc` (`num_documento`),
  KEY `idx_pago_ficha` (`id_ficha`),
  CONSTRAINT `fk_pago_ficha` FOREIGN KEY (`id_ficha`) REFERENCES `ficha_hospedaje` (`id_ficha`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_pago_huesped` FOREIGN KEY (`num_documento`) REFERENCES `huesped` (`num_documento`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago_servicio`
--

LOCK TABLES `pago_servicio` WRITE;
/*!40000 ALTER TABLE `pago_servicio` DISABLE KEYS */;
/*!40000 ALTER TABLE `pago_servicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permiso`
--

DROP TABLE IF EXISTS `permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permiso` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `nombre_empleado` varchar(150) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo` varchar(30) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `motivo` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `estado` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT 'Pendiente',
  `fecha_solicitud` datetime NOT NULL,
  `comentario_rrhh` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_permiso_empleado` (`id_empleado`),
  CONSTRAINT `fk_permiso_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permiso`
--

LOCK TABLES `permiso` WRITE;
/*!40000 ALTER TABLE `permiso` DISABLE KEYS */;
INSERT INTO `permiso` VALUES (1,3,'Rosa Perez','Vacaciones','2026-07-02','2026-07-03','Matrimonio','Aprobada','2026-07-01 17:33:57','');
/*!40000 ALTER TABLE `permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferencia_huesped`
--

DROP TABLE IF EXISTS `preferencia_huesped`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferencia_huesped` (
  `id_preferencia` int NOT NULL AUTO_INCREMENT,
  `num_documento` varchar(20) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo_preferencia` varchar(60) COLLATE utf8mb4_spanish_ci NOT NULL,
  `detalle` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_registro` datetime NOT NULL,
  PRIMARY KEY (`id_preferencia`),
  KEY `idx_pref_doc` (`num_documento`),
  CONSTRAINT `fk_pref_huesped` FOREIGN KEY (`num_documento`) REFERENCES `huesped` (`num_documento`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferencia_huesped`
--

LOCK TABLES `preferencia_huesped` WRITE;
/*!40000 ALTER TABLE `preferencia_huesped` DISABLE KEYS */;
/*!40000 ALTER TABLE `preferencia_huesped` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_hotel` int NOT NULL DEFAULT '1',
  `nombre` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `tipo` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `stock_minimo` int NOT NULL DEFAULT '9',
  `fecha_agregado` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_prod_nombre` (`nombre`),
  KEY `fk_prod_hotel` (`id_hotel`),
  CONSTRAINT `fk_prod_hotel` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,1,'Jabón de manos','Limpieza',50,10,'2025-01-01'),(2,1,'Papel higiénico','Limpieza',8,10,'2025-01-01'),(3,1,'Café molido','Alimentos',30,15,'2025-01-01'),(4,1,'JABON','Limpieza',8,9,'2026-07-02');
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turno_caja`
--

DROP TABLE IF EXISTS `turno_caja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turno_caja` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_empleado` int NOT NULL,
  `total_sistema` decimal(10,2) NOT NULL,
  `total_fisico` decimal(10,2) NOT NULL,
  `estado` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `fecha_cierre` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `motivo` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `fk_turno_emp` (`id_empleado`),
  CONSTRAINT `fk_turno_emp` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turno_caja`
--

LOCK TABLES `turno_caja` WRITE;
/*!40000 ALTER TABLE `turno_caja` DISABLE KEYS */;
INSERT INTO `turno_caja` VALUES (1,1,170.00,170.00,'Cuadrado (Perfecto)','2026-06-29 02:44:13',''),(2,1,480.00,480.00,'Cuadrado (Perfecto)','2026-06-29 02:49:51',''),(3,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(4,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(5,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(6,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(7,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(8,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(9,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(10,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22',''),(11,1,100.00,100.00,'Cuadrado (Perfecto)','2026-07-02 19:06:22','');
/*!40000 ALTER TABLE `turno_caja` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turno_empleado`
--

DROP TABLE IF EXISTS `turno_empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turno_empleado` (
  `id_empleado` int NOT NULL,
  `hora_entrada` time NOT NULL,
  `hora_salida` time NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_empleado`),
  CONSTRAINT `fk_turno_empleado` FOREIGN KEY (`id_empleado`) REFERENCES `empleado` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turno_empleado`
--

LOCK TABLES `turno_empleado` WRITE;
/*!40000 ALTER TABLE `turno_empleado` DISABLE KEYS */;
INSERT INTO `turno_empleado` VALUES (1234,'17:32:00','22:00:00',1);
/*!40000 ALTER TABLE `turno_empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_habitaciones`
--

DROP TABLE IF EXISTS `v_habitaciones`;
/*!50001 DROP VIEW IF EXISTS `v_habitaciones`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_habitaciones` AS SELECT 
 1 AS `numero`,
 1 AS `tipo`,
 1 AS `estado`,
 1 AS `precio`,
 1 AS `capacidad`,
 1 AS `ficha_activa`,
 1 AS `ingreso_actual`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_ordenes_pendientes`
--

DROP TABLE IF EXISTS `v_ordenes_pendientes`;
/*!50001 DROP VIEW IF EXISTS `v_ordenes_pendientes`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_ordenes_pendientes` AS SELECT 
 1 AS `id_orden`,
 1 AS `producto`,
 1 AS `cantidad`,
 1 AS `proveedor`,
 1 AS `precio_total`,
 1 AS `fecha_entrega`,
 1 AS `solicitado_por`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_stock_critico`
--

DROP TABLE IF EXISTS `v_stock_critico`;
/*!50001 DROP VIEW IF EXISTS `v_stock_critico`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_stock_critico` AS SELECT 
 1 AS `nombre`,
 1 AS `tipo`,
 1 AS `stock`,
 1 AS `stock_minimo`,
 1 AS `unidades_faltantes`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_ventas_por_dia`
--

DROP TABLE IF EXISTS `v_ventas_por_dia`;
/*!50001 DROP VIEW IF EXISTS `v_ventas_por_dia`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_ventas_por_dia` AS SELECT 
 1 AS `fecha`,
 1 AS `cantidad_checkouts`,
 1 AS `total_soles`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_habitaciones`
--

/*!50001 DROP VIEW IF EXISTS `v_habitaciones`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_habitaciones` AS select `h`.`numero` AS `numero`,(case `h`.`tipo` when 'S' then 'Simple' when 'D' then 'Doble' else 'Matrimonial' end) AS `tipo`,(case `h`.`estado` when 'D' then 'Disponible' when 'O' then 'Ocupado' else 'Mantenimiento' end) AS `estado`,`h`.`precio` AS `precio`,`h`.`capacidad` AS `capacidad`,`fh`.`id_ficha` AS `ficha_activa`,`fh`.`fecha_ingreso` AS `ingreso_actual` from (`habitacion` `h` left join `ficha_hospedaje` `fh` on(((`fh`.`numero_habitacion` = `h`.`numero`) and (`fh`.`estado` = 'A')))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ordenes_pendientes`
--

/*!50001 DROP VIEW IF EXISTS `v_ordenes_pendientes`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ordenes_pendientes` AS select `o`.`id_orden` AS `id_orden`,`p`.`nombre` AS `producto`,`o`.`cantidad` AS `cantidad`,`o`.`proveedor` AS `proveedor`,`o`.`precio_total` AS `precio_total`,`o`.`fecha_entrega` AS `fecha_entrega`,concat(`e`.`nombre`,' ',`e`.`apellido`) AS `solicitado_por` from ((`orden_compra` `o` join `producto` `p` on((`p`.`id` = `o`.`id_producto`))) join `empleado` `e` on((`e`.`id` = `o`.`id_empleado`))) where (`o`.`estado` = 'Pendiente') order by `o`.`fecha_emision` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_stock_critico`
--

/*!50001 DROP VIEW IF EXISTS `v_stock_critico`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_stock_critico` AS select `producto`.`nombre` AS `nombre`,`producto`.`tipo` AS `tipo`,`producto`.`stock` AS `stock`,`producto`.`stock_minimo` AS `stock_minimo`,(`producto`.`stock_minimo` - `producto`.`stock`) AS `unidades_faltantes` from `producto` where (`producto`.`stock` <= `producto`.`stock_minimo`) order by `producto`.`stock` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_ventas_por_dia`
--

/*!50001 DROP VIEW IF EXISTS `v_ventas_por_dia`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_ventas_por_dia` AS select cast(`f`.`fecha_salida` as date) AS `fecha`,count(0) AS `cantidad_checkouts`,sum(`c`.`importe_total`) AS `total_soles` from (`ficha_hospedaje` `f` join `comprobante_pago` `c` on((`c`.`id_ficha` = `f`.`id_ficha`))) where (`f`.`estado` = 'F') group by cast(`f`.`fecha_salida` as date) order by `fecha` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-06  1:46:54
