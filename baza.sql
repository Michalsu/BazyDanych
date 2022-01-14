CREATE DATABASE  IF NOT EXISTS `baza` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `baza`;
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
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adres`
--

LOCK TABLES `adres` WRITE;
/*!40000 ALTER TABLE `adres` DISABLE KEYS */;
INSERT INTO `adres` VALUES (1,50120,'Szkolna',12),(2,50120,'Szkolna',12),(3,50120,'Szkolna',12),(4,50120,'Szkolna',12),(5,50120,'Szkolna',12),(6,50120,'Szkolna',12),(7,50120,'Szkolna',12),(8,50120,'Szkolna',12),(9,50120,'Szkolna',12),(10,50120,'Szkolna',12),(11,50120,'Szkolna',12),(12,50120,'Szkolna',12),(13,50120,'Szkolna',12),(14,50120,'Szkolna',12),(15,50120,'Szkolna',12),(16,50120,'Szkolna',12),(17,50120,'Szkolna',12),(18,50120,'Szkolna',12),(19,50120,'Szkolna',12),(20,50120,'Szkolna',12),(21,50120,'Szkolna',12),(22,50120,'Szkolna',12),(23,50120,'Szkolna',12),(24,50120,'Szkolna',12),(25,50120,'Szkolna',12),(26,50120,'Szkolna',12),(27,50120,'Szkolna',12),(28,50120,'Szkolna',12),(29,50120,'Szkolna',12),(30,50120,'Szkolna',12),(31,50120,'Szkolna',12),(32,50120,'Zielna',14),(33,50120,'Zielna',14),(34,50120,'Zielna',15),(35,50120,'Zielna',15),(36,50120,'Zielna',16),(37,50120,'Zielna',16),(38,50120,'Zielna',17),(39,50120,'Zielna',17),(40,50110,'Zielna',17),(41,50110,'Zielna',17),(42,10120,'Kasztanowa',11),(43,54321,'Granatowa',22),(44,12345,'Poziomkowa',12),(45,12345,'Poziomkowa',12),(46,12345,'Poziomkowa',12),(47,54321,'Granatowa',22),(48,54321,'Granatowa',22),(49,54321,'Granatowa',22),(50,12345,'Poziomkowa',12);
/*!40000 ALTER TABLE `adres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dane_osobowe`
--

DROP TABLE IF EXISTS `dane_osobowe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dane_osobowe` (
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dane_osobowe`
--

LOCK TABLES `dane_osobowe` WRITE;
/*!40000 ALTER TABLE `dane_osobowe` DISABLE KEYS */;
INSERT INTO `dane_osobowe` VALUES (1,'Adam','Nowak',123,'ada@bat.pl',1),(4,'Adam','Nowak',124,'adaa@bat.pl',1),(6,'Adam','Nowak',1243,'adaaa@bat.pl',1),(7,'Adam','Nowak',997,'adam@gmail.com',29),(9,'Adam','Nowak',998,'adama@gmail.com',31),(11,'Adam','Nowak',991,'adam1a@gmail.com',33),(12,'Adam','Nowak',9921,'adam1xa@gmail.com',34),(13,'Adam','Nowak',992221,'adam1xaa@gmail.com',35),(14,'Adam','Nowak',92221,'adam1xaa@gail.com',36),(15,'Adam','Nowak',922121,'adam1xasa@gail.com',37),(16,'Adam','Nowak',9228121,'adam1xas4a@gail.com',38),(17,'Adam','Nowak',92286121,'am1xas4a@gail.com',39),(19,'Adam','Nowak',922861821,'am1hxas4a@gail.com',41),(20,'Adam','Nowak',321,'abat@gail.com',42),(24,'Michał','Sujewicz',123456789,'ms@poczta.pl',50);
/*!40000 ALTER TABLE `dane_osobowe` ENABLE KEYS */;
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
                          `haslo` varchar(180) NOT NULL,
                          PRIMARY KEY (`klient_ID`),
                          UNIQUE KEY `Login` (`login`),
                          KEY `FKKlient89231` (`koszyk_ID`),
                          KEY `FKKlient120104` (`dane_osobowe_ID`),
                          CONSTRAINT `FKKlient120104` FOREIGN KEY (`dane_osobowe_ID`) REFERENCES `dane_osobowe` (`dane_osobowe_ID`),
                          CONSTRAINT `FKKlient89231` FOREIGN KEY (`koszyk_ID`) REFERENCES `koszyk` (`koszyk_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `klient`
--

LOCK TABLES `klient` WRITE;
/*!40000 ALTER TABLE `klient` DISABLE KEYS */;
INSERT INTO `klient` VALUES (1,4,19,'Adaadfa11mNdswa','0b0f750d7cd94fcbf14619c02ca917f1e392ec36231862afc27baacf6b9dd52f9eb8a3166b97854fb4c0dacae19e9d8cc7b3372940a15901bcd163ae1f9609ce:10dd3207217d23af70e144a2c5e40c175245c525a7a3fb70522'),(2,5,20,'Adamnowak','0a68c74d564ed883804c9391e0bb05e9023f28cd83379c5104231565f9b30156f59cc3f6dc783ac7028c250f8739749eee81768e7274e2d059ebace67218f332:535f758b7157701dc7468a30e40ef765019a7b9055700'),(3,6,1,'Adam','0a68c74d564ed883804c9391e0bb05e9023f28cd83379c5104231565f9b30156f59cc3f6dc783ac7028c250f8739749eee81768e7274e2d059ebace67218f332:535f758b7157701dc7468a30e40ef765019a7b9055700');
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
                          `produkt_list` json DEFAULT NULL,
                          `wartosc_koszyka` float DEFAULT NULL,
                          PRIMARY KEY (`koszyk_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `koszyk`
--

LOCK TABLES `koszyk` WRITE;
/*!40000 ALTER TABLE `koszyk` DISABLE KEYS */;
INSERT INTO `koszyk` VALUES (4,'[]',0),(5,'{\"produkty\": [2, 1]}',5500),(6,'{\"produkty\": [1, 1, 1]}',15000);
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
INSERT INTO `koszyk_produkt` VALUES (5,1,5),(5,2,5),(6,1,5);
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
                           `lista_produktow_dostepnych` json DEFAULT NULL,
                           `przestrzen_magazynowa` int NOT NULL,
                           PRIMARY KEY (`magazyn_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazyn`
--

LOCK TABLES `magazyn` WRITE;
/*!40000 ALTER TABLE `magazyn` DISABLE KEYS */;
INSERT INTO `magazyn` VALUES (1,'[]',10000),(2,'[]',10000),(3,'[]',10000),(4,'[]',10000);
/*!40000 ALTER TABLE `magazyn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magazyn_produkt`
--

DROP TABLE IF EXISTS `magazyn_produkt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magazyn_produkt` (
                                   `magazyn_ID` int NOT NULL,
                                   `produkt_ID` int NOT NULL,
                                   `liczba_produktow` int NOT NULL,
                                   PRIMARY KEY (`magazyn_ID`,`produkt_ID`),
                                   KEY `FKMagazyn_Pr209943` (`produkt_ID`),
                                   KEY `FKMagazyn_Pr607489` (`magazyn_ID`),
                                   CONSTRAINT `FKMagazyn_Pr209943` FOREIGN KEY (`produkt_ID`) REFERENCES `produkt` (`produkt_ID`),
                                   CONSTRAINT `FKMagazyn_Pr607489` FOREIGN KEY (`magazyn_ID`) REFERENCES `magazyn` (`magazyn_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magazyn_produkt`
--

LOCK TABLES `magazyn_produkt` WRITE;
/*!40000 ALTER TABLE `magazyn_produkt` DISABLE KEYS */;
INSERT INTO `magazyn_produkt` VALUES (1,1,111),(1,2,152),(2,2,152);
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
                            PRIMARY KEY (`placowka_id`),
                            KEY `FKPlacowka994938` (`adres_id`),
                            KEY `FKPlacowka205223` (`magazyn_ID`),
                            CONSTRAINT `FKPlacowka205223` FOREIGN KEY (`magazyn_ID`) REFERENCES `magazyn` (`magazyn_ID`),
                            CONSTRAINT `FKPlacowka994938` FOREIGN KEY (`adres_id`) REFERENCES `adres` (`adres_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `placowka`
--

LOCK TABLES `placowka` WRITE;
/*!40000 ALTER TABLE `placowka` DISABLE KEYS */;
INSERT INTO `placowka` VALUES (1,49,4,'Prowizoryczna');
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
                             `haslo` varchar(180) NOT NULL,
                             `funkcja` varchar(10) NOT NULL,
                             PRIMARY KEY (`pracownik_ID`),
                             UNIQUE KEY `login` (`login`),
                             UNIQUE KEY `Funkcja` (`funkcja`),
                             KEY `FKPracownik866164` (`placowka_id`),
                             KEY `FKPracownik784739` (`dane_osobowe_ID`),
                             CONSTRAINT `FKPracownik784739` FOREIGN KEY (`dane_osobowe_ID`) REFERENCES `dane_osobowe` (`dane_osobowe_ID`),
                             CONSTRAINT `FKPracownik866164` FOREIGN KEY (`placowka_id`) REFERENCES `placowka` (`placowka_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pracownik`
--

LOCK TABLES `pracownik` WRITE;
/*!40000 ALTER TABLE `pracownik` DISABLE KEYS */;
INSERT INTO `pracownik` VALUES (2,24,1,'p252818','3fc64ba08f7ca9af58477f4f3468452c84dfff5abbc9d09f68856c10d9580444b4f147c91679e505270e0d4b1c577bb7bbfd760ac34f18ed904ad7a5f81e1d91:d5fe66e46250527f1068f38783cac97d46796657e7e6e','admin');
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
INSERT INTO `produkt` VALUES (1,1000,1,' dasda',' dsadas',1),(2,100,1,' dasda',' dsadas',1);
/*!40000 ALTER TABLE `produkt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
                        `id` int NOT NULL,
                        `settings` json NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
INSERT INTO `test` VALUES (3,'{\"newFoo\": [10, 10, 10, 10]}');
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zamowienie`
--

DROP TABLE IF EXISTS `zamowienie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zamowienie` (
                              `zamowienie_ID` int NOT NULL AUTO_INCREMENT,
                              `klient_ID` int NOT NULL,
                              `Data` date NOT NULL,
                              `Stan` char(15) NOT NULL,
                              `Platnosc` char(15) NOT NULL,
                              `SposobDostawy` char(15) NOT NULL,
                              `produkt_list` json NOT NULL,
                              `Wartosc_zamowienia` int NOT NULL,
                              PRIMARY KEY (`zamowienie_ID`),
                              KEY `FKZamowienie880970` (`klient_ID`),
                              CONSTRAINT `FKZamowienie880970` FOREIGN KEY (`klient_ID`) REFERENCES `klient` (`klient_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zamowienie`
--

LOCK TABLES `zamowienie` WRITE;
/*!40000 ALTER TABLE `zamowienie` DISABLE KEYS */;
INSERT INTO `zamowienie` VALUES (1,1,'2000-12-12','g','g','g','[]',0),(2,2,'2022-01-13','gotowe','karta','dodomu','[2, 1]',0),(3,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(4,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(5,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(6,3,'2022-01-14','gotowe','karta','dodomu','[1]',0),(7,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(8,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(9,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',0),(10,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(11,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(12,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(13,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(14,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(15,2,'2022-01-14','gotowe','karta','dodomu','[1]',0),(16,2,'2022-01-14','gotowe','karta','dodomu','[1, 2]',0),(17,2,'2022-01-14','gotowe','karta','dodomu','[2, 1]',5500);
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

-- Dump completed on 2022-01-14 19:42:34
