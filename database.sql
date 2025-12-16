-- =====================================================
-- Database: gestion_voitures
-- =====================================================

CREATE DATABASE IF NOT EXISTS gestion_voitures;
USE gestion_voitures;

-- =====================================================
-- TABLE: clients
-- =====================================================
CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100) DEFAULT NULL,
    adresse TEXT DEFAULT NULL,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO clients (id, nom, telephone, email, adresse, date_creation) VALUES
(1, 'Ahmed El Amrani', '0612345678', 'ahmed.amrani@gmail.com', 'Casablanca', '2025-12-15 20:43:50'),
(2, 'Sara Benali', '0623456789', 'sara.benali@gmail.com', 'Rabat', '2025-12-15 20:43:50'),
(3, 'Youssef Ait Lahcen', '0634567890', 'youssef.ait@gmail.com', 'Marrakech', '2025-12-15 20:43:50'),
(4, 'Khadija Ouazzani', '0645678901', 'khadija.ouazzani@gmail.com', 'Agadir', '2025-12-15 20:43:50');

-- =====================================================
-- TABLE: vehicules
-- =====================================================
CREATE TABLE vehicules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    prix_achat DECIMAL(10,2) NOT NULL,
    prix_vente DECIMAL(10,2) NOT NULL,
    annee INT NOT NULL,
    kilometrage INT NOT NULL,
    type_vehicule ENUM('BERLINE','SUV','CAMION','ELECTRIQUE') NOT NULL,
    statut ENUM('DISPONIBLE','VENDU') DEFAULT 'DISPONIBLE',
    date_ajout TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_vende TIMESTAMP NULL DEFAULT NULL,

    capacite_coffre INT DEFAULT NULL,
    traction VARCHAR(10) DEFAULT NULL,
    capacite_chargement DECIMAL(10,2) DEFAULT NULL,
    autonomie_km INT DEFAULT NULL,
    temps_charge_heures DECIMAL(4,1) DEFAULT NULL
);

INSERT INTO vehicules (
    id, marque, modele, prix_achat, prix_vente, annee, kilometrage,
    type_vehicule, statut, date_ajout, date_vende,
    capacite_coffre, traction, capacite_chargement,
    autonomie_km, temps_charge_heures
) VALUES
(2, 'Hyundai', 'Tucson', 180000.00, 215000.00, 2020, 38000, 'SUV', 'VENDU', '2025-12-15 20:43:50', NULL, NULL, '4x2', NULL, NULL, NULL),
(3, 'Dacia', 'Logan', 90000.00, 110000.00, 2018, 62000, 'BERLINE', 'VENDU', '2025-12-15 20:43:50', NULL, 510, NULL, NULL, NULL, NULL),
(4, 'Tesla', 'Model 3', 350000.00, 390000.00, 2022, 12000, 'ELECTRIQUE', 'DISPONIBLE', '2025-12-15 20:43:50', NULL, NULL, NULL, NULL, 580, 8.5),
(7, 'Dacia', 'Duster', 10000.00, 10000.00, 2006, 1000, 'SUV', 'DISPONIBLE', '2025-12-15 20:56:42', NULL, NULL, '4x4', NULL, NULL, NULL);

-- =====================================================
-- TABLE: ventes
-- =====================================================
CREATE TABLE ventes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicule_id INT NOT NULL,
    client_id INT NOT NULL,
    prix_vente_final DECIMAL(10,2) NOT NULL,
    date_vente TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mode_paiement ENUM('COMPTANT','CREDIT','CHEQUE') DEFAULT 'COMPTANT',
    notes TEXT DEFAULT NULL,

    CONSTRAINT fk_ventes_vehicule
        FOREIGN KEY (vehicule_id)
        REFERENCES vehicules(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_ventes_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE CASCADE
);
