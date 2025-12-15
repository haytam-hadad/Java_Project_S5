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


-- Insertion de véhicules

INSERT INTO vehicules 
(marque, modele, prix_achat, prix_vente, annee, kilometrage, type_vehicule, statut)
VALUES
('Toyota', 'Corolla', 120000.00, 145000.00, 2019, 45000, 'BERLINE', 'DISPONIBLE'),
('Hyundai', 'Tucson', 180000.00, 215000.00, 2020, 38000, 'SUV', 'DISPONIBLE'),
('Dacia', 'Logan', 90000.00, 110000.00, 2018, 62000, 'BERLINE', 'VENDU'),
('Tesla', 'Model 3', 350000.00, 390000.00, 2022, 12000, 'ELECTRIQUE', 'DISPONIBLE'),
('Isuzu', 'NPR', 250000.00, 290000.00, 2017, 80000, 'CAMION', 'VENDU');

-- Insertion de clients

INSERT INTO clients 
(nom, telephone, email, adresse)
VALUES
('Ahmed El Amrani', '0612345678', 'ahmed.amrani@gmail.com', 'Casablanca'),
('Sara Benali', '0623456789', 'sara.benali@gmail.com', 'Rabat'),
('Youssef Ait Lahcen', '0634567890', 'youssef.ait@gmail.com', 'Marrakech'),
('Khadija Ouazzani', '0645678901', 'khadija.ouazzani@gmail.com', 'Agadir');

-- Insertion de ventes

INSERT INTO ventes 
(vehicule_id, client_id, prix_vente_final, mode_paiement, notes)
VALUES
(3, 1, 108000.00, 'COMPTANT', 'Vente rapide sans négociation'),
(5, 3, 285000.00, 'CHEQUE', 'Paiement en deux chèques');
