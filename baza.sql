-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: baza
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `adres`
--

DROP TABLE IF EXISTS `adres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `adres` (
  `adres_ID` int NOT NULL AUTO_INCREMENT,
  `kod_pocztowy` int NOT NULL,
  `ulica` varchar(30) NOT NULL,
  `numer` int NOT NULL,
  PRIMARY KEY (`adres_ID`),
  UNIQUE KEY `adres_ID_UNIQUE` (`adres_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adres`
--

LOCK TABLES `adres` WRITE;
/*!40000 ALTER TABLE `adres` DISABLE KEYS */;
INSERT INTO `adres` VALUES (1,50120,'Szkolna',12),(2,50120,'Szkolna',12),(3,50120,'Szkolna',12),(4,50120,'Szkolna',12),(5,50120,'Szkolna',12),(6,50120,'Szkolna',12),(7,50120,'Szkolna',12),(8,50120,'Szkolna',12),(9,50120,'Szkolna',12);
/*!40000 ALTER TABLE `adres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dane osobowe`
--

DROP TABLE IF EXISTS `dane osobowe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dane osobowe` (
  `dane_osobowe_ID` int NOT NULL AUTO_INCREMENT,
  `Imie` varchar(30) NOT NULL,
  `Nazwisko` varchar(30) NOT NULL,
  `nr_telefonu` int NOT NULL,
  `mail` varchar(40) NOT NULL,
  `adres_id` int NOT NULL,
  PRIMARY KEY (`dane_osobowe_ID`),
  UNIQUE KEY `nr_telefonu` (`nr_telefonu`),
  UNIQUE KEY `mail` (`mail`),
  UNIQUE KEY `dane_osobowe_ID_UNIQUE` (`dane_osobowe_ID`),
  KEY `FKDane osobo145922` (`adres_id`),
  CONSTRAINT `FKDane osobo145922` FOREIGN KEY (`adres_id`) REFERENCES `adres` (`adres_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dane osobowe`
--

LOCK TABLES `dane osobowe` WRITE;
/*!40000 ALTER TABLE `dane osobowe` DISABLE KEYS */;
/*!40000 ALTER TABLE `dane osobowe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kategoria`
--

DROP TABLE IF EXISTS `kategoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategoria` (
  `kategoria_ID` int NOT NULL AUTO_INCREMENT,
  `Nazwa` varchar(20) NOT NULL,
  PRIMARY KEY (`kategoria_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategoria`
--

LOCK TABLES `kategoria` WRITE;
/*!40000 ALTER TABLE `kategoria` DISABLE KEYS */;
INSERT INTO `kategoria` VALUES (1,'Jedzenie');
/*!40000 ALTER TABLE `kategoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `klient`
--

DROP TABLE IF EXISTS `klient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `klient` (
  `klient_ID` int NOT NULL AUTO_INCREMENT,
  `koszyk_ID` int NOT NULL,
  `dane_osobowe_ID` int NOT NULL,
  `login` varchar(20) NOT NULL,
  `haslo` varchar(168) NOT NULL,
  PRIMARY KEY (`klient_ID`),
  UNIQUE KEY `Login` (`login`),
  KEY `FKKlient89231` (`koszyk_ID`),
  KEY `FKKlient120104` (`dane_osobowe_ID`),
  CONSTRAINT `FKKlient120104` FOREIGN KEY (`dane_osobowe_ID`) REFERENCES `dane osobowe` (`dane_osobowe_ID`),
  CONSTRAINT `FKKlient89231` FOREIGN KEY (`koszyk_ID`) REFERENCES `koszyk` (`koszyk_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `klient`
--

LOCK TABLES `klient` WRITE;
/*!40000 ALTER TABLE `klient` DISABLE KEYS */;
/*!40000 ALTER TABLE `klient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `koszyk`
--

DROP TABLE IF EXISTS `koszyk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `koszyk` (
  `koszyk_ID` int NOT NULL AUTO_INCREMENT,
  `produkt_list` int DEFAULT NULL,
  `wartosc_koszyka` float DEFAULT NULL,
  PRIMARY KEY (`koszyk_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `koszyk`
--

LOCK TABLES `koszyk` WRITE;
/*!40000 ALTER TABLE `koszyk` DISABLE KEYS */;
INSERT INTO `koszyk` VALUES (1,NULL,NULL);
/*!40000 ALTER TABLE `koszyk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `koszyk_produkt`
--

DROP TABLE IF EXISTS `koszyk_produkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `koszyk_produkt` (
  `koszyk_ID` int NOT NULL,
  `produkt_ID` int NOT NULL,
  `liczba_sztuk` int NOT NULL,
  PRIMARY KEY (`koszyk_ID`,`produkt_ID`),
  KEY `FKKoszyk_Pro768534` (`produkt_ID`),
  CONSTRAINT `FKKoszyk_Pro338204` FOREIGN KEY (`koszyk_ID`) REFERENCES `koszyk` (`koszyk_ID`),
  CONSTRAINT `FKKoszyk_Pro768534` FOREIGN KEY (`produkt_ID`) REFERENCES `produkt` (`produkt_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `koszyk_produkt`
--

LOCK TABLES `koszyk_produkt` WRITE;
/*!40000 ALTER TABLE `koszyk_produkt` DISABLE KEYS */;
/*!40000 ALTER TABLE `koszyk_produkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazyn`
--

DROP TABLE IF EXISTS `magazyn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazyn` (
  `magazyn_ID` int NOT NULL AUTO_INCREMENT,
  `lista_produktow dostepnych` varchar(40) NOT NULL,
  `przestrzen_magazynowa` int NOT NULL,
  PRIMARY KEY (`magazyn_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazyn`
--

LOCK TABLES `magazyn` WRITE;
/*!40000 ALTER TABLE `magazyn` DISABLE KEYS */;
/*!40000 ALTER TABLE `magazyn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazyn_produkt`
--

DROP TABLE IF EXISTS `magazyn_produkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazyn_produkt` (
  `magazyn_produkt_ID` int NOT NULL,
  `magazynmagazyn_ID` int NOT NULL,
  `produktprodukt_ID` int NOT NULL,
  PRIMARY KEY (`magazyn_produkt_ID`,`magazynmagazyn_ID`,`produktprodukt_ID`),
  KEY `FKMagazyn_Pr209943` (`produktprodukt_ID`),
  KEY `FKMagazyn_Pr607489` (`magazynmagazyn_ID`),
  CONSTRAINT `FKMagazyn_Pr209943` FOREIGN KEY (`produktprodukt_ID`) REFERENCES `produkt` (`produkt_ID`),
  CONSTRAINT `FKMagazyn_Pr607489` FOREIGN KEY (`magazynmagazyn_ID`) REFERENCES `magazyn` (`magazyn_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazyn_produkt`
--

LOCK TABLES `magazyn_produkt` WRITE;
/*!40000 ALTER TABLE `magazyn_produkt` DISABLE KEYS */;
/*!40000 ALTER TABLE `magazyn_produkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `placowka`
--

DROP TABLE IF EXISTS `placowka`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `placowka` (
  `placowka_id` int NOT NULL AUTO_INCREMENT,
  `adres_id` int NOT NULL,
  `magazyn_ID` int NOT NULL,
  `nazwa` varchar(40) NOT NULL,
  `lista_pracownikow` int NOT NULL,
  PRIMARY KEY (`placowka_id`),
  KEY `FKPlacowka994938` (`adres_id`),
  KEY `FKPlacowka205223` (`magazyn_ID`),
  CONSTRAINT `FKPlacowka205223` FOREIGN KEY (`magazyn_ID`) REFERENCES `magazyn` (`magazyn_ID`),
  CONSTRAINT `FKPlacowka994938` FOREIGN KEY (`adres_id`) REFERENCES `adres` (`adres_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `placowka`
--

LOCK TABLES `placowka` WRITE;
/*!40000 ALTER TABLE `placowka` DISABLE KEYS */;
/*!40000 ALTER TABLE `placowka` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pracownik`
--

DROP TABLE IF EXISTS `pracownik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pracownik` (
  `pracownik_ID` int NOT NULL AUTO_INCREMENT,
  `dane_osobowe_ID` int NOT NULL,
  `placowka_id` int NOT NULL,
  `login` varchar(20) NOT NULL,
  `haslo` varchar(168) NOT NULL,
  `funkcja` varchar(10) NOT NULL,
  PRIMARY KEY (`pracownik_ID`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `Funkcja` (`funkcja`),
  KEY `FKPracownik866164` (`placowka_id`),
  KEY `FKPracownik784739` (`dane_osobowe_ID`),
  CONSTRAINT `FKPracownik784739` FOREIGN KEY (`dane_osobowe_ID`) REFERENCES `dane osobowe` (`dane_osobowe_ID`),
  CONSTRAINT `FKPracownik866164` FOREIGN KEY (`placowka_id`) REFERENCES `placowka` (`placowka_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pracownik`
--

LOCK TABLES `pracownik` WRITE;
/*!40000 ALTER TABLE `pracownik` DISABLE KEYS */;
/*!40000 ALTER TABLE `pracownik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produkt`
--

DROP TABLE IF EXISTS `produkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produkt` (
  `produkt_ID` int NOT NULL AUTO_INCREMENT,
  `cena` float NOT NULL,
  `Promocja` int DEFAULT NULL,
  `nazwa` varchar(40) NOT NULL,
  `opis` varchar(255) NOT NULL,
  `kategoria_ID` int NOT NULL,
  PRIMARY KEY (`produkt_ID`),
  KEY `FKProdukt286136` (`kategoria_ID`),
  CONSTRAINT `FKProdukt286136` FOREIGN KEY (`kategoria_ID`) REFERENCES `kategoria` (`kategoria_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1236 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produkt`
--

LOCK TABLES `produkt` WRITE;
/*!40000 ALTER TABLE `produkt` DISABLE KEYS */;
INSERT INTO `produkt` VALUES (1233,13,NULL,'Mleko','Produkt sporzywczy pyszne mleko',1),(1235,14,NULL,'Mleko czekoladowe','Produkt sporzywczy pyszne mleko',1);
/*!40000 ALTER TABLE `produkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zamowienie`
--

DROP TABLE IF EXISTS `zamowienie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zamowienie` (
  `zamoweienie_ID` int NOT NULL AUTO_INCREMENT,
  `klient_ID` int NOT NULL,
  `Data` date NOT NULL,
  `Stan` char(15) NOT NULL,
  `Platnosc` char(15) NOT NULL,
  `SposobDostawy` char(15) NOT NULL,
  PRIMARY KEY (`zamoweienie_ID`),
  KEY `FKZamowienie880970` (`klient_ID`),
  CONSTRAINT `FKZamowienie880970` FOREIGN KEY (`klient_ID`) REFERENCES `klient` (`klient_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zamowienie`
--

LOCK TABLES `zamowienie` WRITE;
/*!40000 ALTER TABLE `zamowienie` DISABLE KEYS */;
/*!40000 ALTER TABLE `zamowienie` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-01-07 17:13:42
