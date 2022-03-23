CREATE DATABASE  IF NOT EXISTS `my_balance` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `my_balance`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: my_balance
-- ------------------------------------------------------
-- Server version	5.6.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `spese`
--

DROP TABLE IF EXISTS `spese`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spese` (
  `NomeUtente` varchar(100) NOT NULL,
  `IdSpesa` int(10) unsigned NOT NULL,
  `Categoria` varchar(100) NOT NULL,
  `MetodoDiPagamento` varchar(100) NOT NULL,
  `Data` date NOT NULL,
  `Importo` float unsigned NOT NULL,
  `Descrizione` varchar(1500) NOT NULL,
  PRIMARY KEY (`NomeUtente`,`IdSpesa`),
  CONSTRAINT `NomeUtente` FOREIGN KEY (`NomeUtente`) REFERENCES `utente` (`NomeUtente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spese`
--

LOCK TABLES `spese` WRITE;
/*!40000 ALTER TABLE `spese` DISABLE KEYS */;
INSERT INTO `spese` VALUES ('Fabrizio Lanzillo',0,'Abbigliamento','Banca #1','2021-08-16',150,'Acquisto Cappotto'),('Fabrizio Lanzillo',1,'Mezzi Di Transporto','Banca #2','2021-09-01',3.5,'Biglietto del Treno'),('Fabrizio Lanzillo',2,'Generi alimentari','Banca #1','2021-08-14',37.5,'Spesa al supermercato'),('Fabrizio Lanzillo',3,'Hobbies e tempo libero','Banca #2','2021-08-03',15.99,'Rata Netflix'),('Fabrizio Lanzillo',4,'Sport','Contanti','2021-08-21',24,'Lezione di Tennis'),('Fabrizio Lanzillo',5,'Tasse','Banca #2','2021-08-29',420,'Tassa Universitaria'),('Fabrizio Lanzillo',6,'Veicoli','Banca #2','2021-08-31',20,'Rifornimento di benzina'),('Fabrizio Lanzillo',7,'Hobbies e tempo libero','Contanti','2021-08-25',14,'Film al cinema'),('Fabrizio Lanzillo',8,'Istruzione','Banca #1','2021-08-16',45.99,'Acquisto Libri Università');
/*!40000 ALTER TABLE `spese` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `spese_BINS` BEFORE INSERT ON `spese` FOR EACH ROW
BEGIN
	DECLARE _spesa_indice INT UNSIGNED DEFAULT NULL;
	
	IF NEW.Data > CURRENT_DATE() THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Impossibile aggiungere una spesa in una data futura';
	END IF;

	SELECT IdSpesaSuccessivo INTO _spesa_indice
    FROM utente U 
    WHERE U.NomeUtente = new.NomeUtente
	FOR UPDATE;

	UPDATE utente U
	SET U.IdSpesaSuccessivo = U.IdSpesaSuccessivo + 1
	WHERE U.NomeUtente = new.NomeUtente;

	IF _spesa_indice IS NULL THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'L\'utente non esiste nel database';
	END IF;

	SET new.IdSpesa = _spesa_indice;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `spese_BUPD` BEFORE UPDATE ON `spese` FOR EACH ROW
BEGIN
	IF NEW.Data > CURRENT_DATE() THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'Impossibile aggiungere una spesa in una data futura';
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `NomeUtente` varchar(100) NOT NULL,
  `IdSpesaSuccessivo` int(10) unsigned NOT NULL,
  PRIMARY KEY (`NomeUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES ('Fabrizio Lanzillo',9);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `utente_BINS` BEFORE INSERT ON `utente` FOR EACH ROW
BEGIN
	SET NEW.IdSpesaSuccessivo = 0;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Dumping routines for database 'my_balance'
--
/*!50003 DROP PROCEDURE IF EXISTS `controllo_esistenza_utente` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `controllo_esistenza_utente`(IN utente_da_controllare VARCHAR(100))
BEGIN
	DECLARE utente VARCHAR(100);
	
	SELECT NomeUtente INTO utente
	FROM utente
	WHERE NomeUtente = utente_da_controllare;
	
	IF utente IS NULL THEN
		INSERT INTO utente VALUES (utente_da_controllare, 0);
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-17  0:22:25
