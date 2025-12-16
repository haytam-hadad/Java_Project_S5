-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 16, 2025 at 06:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gestion_voitures`
--

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE `clients` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `adresse` text DEFAULT NULL,
  `date_creation` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`id`, `nom`, `telephone`, `email`, `adresse`, `date_creation`) VALUES
(1, 'Ahmed El Amrani', '0612345678', 'ahmed.amrani@gmail.com', 'Casablanca', '2025-12-15 20:43:50'),
(2, 'Sara Benali', '0623456789', 'sara.benali@gmail.com', 'Rabat', '2025-12-15 20:43:50'),
(3, 'Youssef Ait Lahcen', '0634567890', 'youssef.ait@gmail.com', 'Marrakech', '2025-12-15 20:43:50'),
(4, 'Khadija Ouazzani', '0645678901', 'khadija.ouazzani@gmail.com', 'Agadir', '2025-12-15 20:43:50');

-- --------------------------------------------------------

--
-- Table structure for table `vehicules`
--

CREATE TABLE `vehicules` (
  `id` int(11) NOT NULL,
  `marque` varchar(50) NOT NULL,
  `modele` varchar(50) NOT NULL,
  `prix_achat` decimal(10,2) NOT NULL,
  `prix_vente` decimal(10,2) NOT NULL,
  `annee` int(11) NOT NULL,
  `kilometrage` int(11) NOT NULL,
  `type_vehicule` enum('BERLINE','SUV','CAMION','ELECTRIQUE') NOT NULL,
  `statut` enum('DISPONIBLE','VENDU') DEFAULT 'DISPONIBLE',
  `date_ajout` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_vende` timestamp NULL DEFAULT NULL,
  `capacite_coffre` int(11) DEFAULT NULL,
  `traction` varchar(10) DEFAULT NULL,
  `capacite_chargement` decimal(10,2) DEFAULT NULL,
  `autonomie_km` int(11) DEFAULT NULL,
  `temps_charge_heures` decimal(4,1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicules`
--

INSERT INTO `vehicules` (`id`, `marque`, `modele`, `prix_achat`, `prix_vente`, `annee`, `kilometrage`, `type_vehicule`, `statut`, `date_ajout`, `date_vende`, `capacite_coffre`, `traction`, `capacite_chargement`, `autonomie_km`, `temps_charge_heures`) VALUES
(2, 'Hyundai', 'Tucson', 180000.00, 215000.00, 2020, 38000, 'SUV', 'VENDU', '2025-12-15 20:43:50', NULL, NULL, '4x2', NULL, NULL, NULL),
(3, 'Dacia', 'Logan', 90000.00, 110000.00, 2018, 62000, 'BERLINE', 'VENDU', '2025-12-15 20:43:50', NULL, 510, NULL, NULL, NULL, NULL),
(4, 'Tesla', 'Model 3', 350000.00, 390000.00, 2022, 12000, 'ELECTRIQUE', 'DISPONIBLE', '2025-12-15 20:43:50', NULL, NULL, NULL, NULL, 580, 8.5),
(7, 'Dacia', 'Duster', 10000.00, 10000.00, 2006, 1000, 'SUV', 'DISPONIBLE', '2025-12-15 20:56:42', NULL, NULL, '4x4', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `ventes`
--

CREATE TABLE `ventes` (
  `id` int(11) NOT NULL,
  `vehicule_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `prix_vente_final` decimal(10,2) NOT NULL,
  `date_vente` timestamp NOT NULL DEFAULT current_timestamp(),
  `mode_paiement` enum('COMPTANT','CREDIT','CHEQUE') DEFAULT 'COMPTANT',
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vehicules`
--
ALTER TABLE `vehicules`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ventes`
--
ALTER TABLE `ventes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `vehicule_id` (`vehicule_id`),
  ADD KEY `client_id` (`client_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `vehicules`
--
ALTER TABLE `vehicules`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `ventes`
--
ALTER TABLE `ventes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ventes`
--
ALTER TABLE `ventes`
  ADD CONSTRAINT `ventes_ibfk_1` FOREIGN KEY (`vehicule_id`) REFERENCES `vehicules` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ventes_ibfk_2` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
