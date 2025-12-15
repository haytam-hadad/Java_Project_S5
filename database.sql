-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_voitures;
USE gestion_voitures;

-- Table vehicules
CREATE TABLE vehicules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    prix_achat DECIMAL(10,2) NOT NULL,
    prix_vente DECIMAL(10,2) NOT NULL,
    annee INT NOT NULL,
    kilometrage INT NOT NULL,
    type_vehicule ENUM('BERLINE', 'SUV', 'CAMION', 'ELECTRIQUE') NOT NULL,
    statut ENUM('DISPONIBLE', 'VENDU') DEFAULT 'DISPONIBLE',
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_vende TIMESTAMP NULL DEFAULT NULL
);

-- Table clients
CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    adresse TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table ventes (historique des ventes de véhicules)
CREATE TABLE IF NOT EXISTS ventes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicule_id INT NOT NULL,
    client_id INT NOT NULL,
    prix_vente_final DECIMAL(10,2) NOT NULL,
    date_vente TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mode_paiement ENUM('COMPTANT', 'CREDIT', 'CHEQUE') DEFAULT 'COMPTANT',
    notes TEXT,
    FOREIGN KEY (vehicule_id) REFERENCES vehicules(id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);