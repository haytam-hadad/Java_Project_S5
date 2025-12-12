-- Création de la base de données
CREATE DATABASE IF NOT EXISTS gestion_voitures;
USE gestion_voitures;

-- Table vehicules
CREATE TABLE vehicules (
    id VARCHAR(20) PRIMARY KEY,
    marque VARCHAR(50) NOT NULL,
    modele VARCHAR(50) NOT NULL,
    prix_achat DECIMAL(10,2) NOT NULL,
    prix_vente DECIMAL(10,2) NOT NULL,
    annee INT NOT NULL,
    kilometrage INT NOT NULL,
    type_vehicule ENUM('BERLINE', 'SUV', 'CAMION', 'ELECTRIQUE') NOT NULL,
    statut ENUM('DISPONIBLE', 'EN_NEGOCIATION', 'VENDU') DEFAULT 'DISPONIBLE',
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_vende TIMESTAMP NULL DEFAULT NULL
);

-- Table clients
CREATE TABLE clients (
    id VARCHAR(20) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    adresse TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table historique_client pour tracer les opérations
CREATE TABLE historique_client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(20) NOT NULL,
    type_operation ENUM('AJOUT', 'MODIFICATION', 'CONSULTATION') NOT NULL,
    description TEXT,
    date_operation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);
