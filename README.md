# Système de Gestion de Vente de Véhicules d'Occasion

## Description

Application Java complète pour la gestion de la vente de véhicules d'occasion avec une interface en ligne de commande colorée et intuitive.

## Fonctionnalités

### 1. Gestion des Véhicules

- ✅ Ajouter un véhicule (BERLINE, SUV, CAMION, ELECTRIQUE)
- ✅ Modifier le statut d'un véhicule (DISPONIBLE, VENDU)
- ✅ Supprimer un véhicule
- ✅ Afficher tous les véhicules
- ✅ Vendre un véhicule en l'associant à un client existant (création d'une vente)
- ✅ Afficher les véhicules disponibles
- ✅ Afficher les véhicules vendus
- ✅ Validation des données (prix, année, kilométrage)

### 2. Gestion des Clients

- ✅ Ajouter un client avec vérification des doublons
- ✅ Modifier les informations d'un client
- ✅ Rechercher un client (par nom ou téléphone)
- ✅ Afficher tous les clients
- ✅ Validation des données (nom, téléphone)

### 3. Recherche et Filtrage

- ✅ Rechercher par prix maximum
- ✅ Filtrer par catégorie de véhicule
- ✅ Rechercher par marque
- ✅ Filtrer par année minimum

### 4. Rapports et Statistiques

- ✅ Liste des véhicules disponibles
- ✅ Liste des véhicules vendus avec dates
- ✅ Statistiques de ventes (nombre vendus, disponibles, chiffre d'affaires)
- ✅ Calcul automatique du prix final avec taxes selon le type de véhicule
- ✅ Historique complet des ventes (client, véhicule, mode de paiement, notes)
- ✅ Historique des achats d'un client
- ✅ Historique des ventes d'un véhicule spécifique

### 5. Sécurité

- ✅ Protection par mot de passe à l'accès
- ✅ Confirmation avant suppression
- ✅ Validation des entrées utilisateur

## Structure du Projet

```
Java_Project_S5/
├── src/
│   ├── Main.java                    # Point d'entrée avec menu interactif
│   ├── dao/
│   │   ├── DatabaseConnection.java  # Gestion de la connexion MySQL (Singleton)
│   │   ├── VehiculeDAO.java         # DAO pour les opérations CRUD sur les véhicules
│   │   ├── ClientDAO.java           # DAO pour les opérations CRUD sur les clients
│   │   └── VenteDAO.java            # DAO pour les ventes et statistiques
│   ├── model/
│   │   ├── Vehicule.java            # Classe de base pour les véhicules
│   │   ├── Berline.java             # Classe pour les berlines (taxe 15%)
│   │   ├── SUV.java                 # Classe pour les SUV (taxe 20%)
│   │   ├── Camion.java              # Classe pour les camions (taxe 18%)
│   │   └── Electrique.java          # Classe pour les véhicules électriques (taxe 10%)
│   ├── menu/
│   │   ├── MenuVehicules.java       # Menu de gestion des véhicules (inclut la vente avec client)
│   │   ├── MenuClients.java         # Menu de gestion des clients
│   │   ├── MenuRecherche.java       # Menu de recherche et filtrage des véhicules
│   │   └── MenuRapports.java        # Menu des rapports et statistiques
│   └── util/
│       ├── ColorUtil.java           # Utilitaire pour les couleurs ANSI
│       └── ConsoleUtil.java         # Utilitaires de lecture clavier et menus communs
├── database.sql                     # Script SQL complet pour créer et pré-remplir la base de données
├── db.env                           # Fichier d'environnement (actuellement non utilisé par le code)
└── README.md                        # Documentation du projet
```

## Prérequis

1. **Java JDK 8 ou supérieur**
2. **MySQL Server** (version 5.7 ou supérieure)
3. **MySQL Connector/J** (driver JDBC pour MySQL)
   - Télécharger depuis: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - Ajouter le fichier JAR au classpath du projet

## Installation

### 1. Configuration de la Base de Données

```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script SQL
source database.sql
```

Ou exécutez directement le fichier `database.sql` dans votre client MySQL.

**Structure de la base de données:**

```sql
CREATE DATABASE IF NOT EXISTS gestion_voitures;
USE gestion_voitures;

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
    date_vende TIMESTAMP NULL
);

CREATE TABLE clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    adresse TEXT,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
```

### 2. Configuration de la Connexion

Modifiez les paramètres dans `DatabaseConnection.java` si nécessaire:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_voitures";
private static final String USERNAME = "root";
private static final String PASSWORD = "";  // Votre mot de passe MySQL
```

### 3. Compilation

```bash
# Avec javac en ligne de commande

# Linux / macOS
javac -encoding UTF-8 -cp ".:mysql-connector-java-8.0.XX.jar" -d out \
  src/Main.java src/dao/*.java src/model/*.java src/menu/*.java src/util/*.java

# Windows (PowerShell / CMD)
javac -encoding UTF-8 -cp ".;mysql-connector-java-8.0.XX.jar" -d out ^
  src\Main.java src\dao\*.java src\model\*.java src\menu\*.java src\util\*.java

# Ou, plus simplement, utilisez un IDE (IntelliJ, Eclipse, NetBeans)
# et ajoutez le JAR du connecteur MySQL dans les librairies du projet.
```

### 4. Exécution

```bash
# Exécuter l'application

# Linux / macOS
java -cp "out:mysql-connector-java-8.0.XX.jar" Main

# Windows
java -cp "out;mysql-connector-java-8.0.XX.jar" Main
```

**Mot de passe par défaut:** `111111`

## Utilisation

### Menu Principal

L'application démarre avec un menu principal offrant 4 options:

1. **Gestion des Véhicules** - CRUD complet sur les véhicules
2. **Gestion des Clients** - CRUD complet sur les clients
3. **Recherche et Filtrage** - Recherche avancée de véhicules
4. **Rapports** - Statistiques et rapports de ventes

### Exemple d'utilisation

#### 1. Ajouter un véhicule

```
Menu Principal → 1. Gestion des Véhicules → 1. Ajouter un véhicule
```

- Saisir la marque (ex: Toyota)
- Saisir le modèle (ex: Corolla)
- Saisir le prix d'achat (ex: 150000)
- Saisir le prix de vente (ex: 180000) - doit être > prix d'achat
- Saisir l'année (ex: 2020) - entre 1900 et année actuelle
- Saisir le kilométrage (ex: 50000) - doit être ≥ 0
- Choisir le type (1-4)
- Le système calcule automatiquement le prix final avec taxes

#### 2. Ajouter un client

```
Menu Principal → 2. Gestion des Clients → 1. Ajouter un client
```

- Saisir le nom (obligatoire)
- Saisir le téléphone (min. 8 chiffres, obligatoire)
- Saisir l'email (optionnel)
- Saisir l'adresse (optionnel)
- Le système vérifie automatiquement les doublons

#### 3. Rechercher des véhicules

```
Menu Principal → 3. Recherche et Filtrage
```

- **Par prix:** Affiche tous les véhicules ≤ prix max
- **Par catégorie:** Filtre par type (BERLINE, SUV, CAMION, ELECTRIQUE)

#### 4. Consulter les rapports

```
Menu Principal → 4. Rapports
```

- **Véhicules disponibles:** Liste tous les véhicules en stock
- **Véhicules vendus:** Historique des ventes avec dates
- **Statistiques:** Nombre de ventes, CA total, stock disponible

## Calcul des Prix

Le système calcule automatiquement le prix final avec taxes selon le type:

- **BERLINE**: Prix de vente × 1.15 (+15% de taxe)
- **SUV**: Prix de vente × 1.20 (+20% de taxe)
- **CAMION**: Prix de vente × 1.18 (+18% de taxe)
- **ELECTRIQUE**: Prix de vente × 1.10 (+10% de taxe)

**Exemple:**

```
Véhicule: SUV
Prix de vente: 200,000 DH
Prix final avec taxes: 240,000 DH (200,000 × 1.20)
```

## Validations Automatiques

### Véhicules

- ✅ Marque et modèle non vides
- ✅ Prix d'achat > 0
- ✅ Prix de vente > 0 et > prix d'achat
- ✅ Année entre 1900 et année actuelle
- ✅ Kilométrage ≥ 0
- ✅ Type valide (1-4)

### Clients

- ✅ Nom non vide
- ✅ Téléphone non vide et ≥ 8 caractères
- ✅ Détection des doublons (nom + téléphone ou téléphone seul)
- ✅ Confirmation avant ajout si doublon détecté

## Interface Utilisateur

L'application utilise des codes ANSI pour une interface colorée:

- 🟢 **Vert**: Messages de succès
- 🔴 **Rouge**: Messages d'erreur
- 🟡 **Jaune**: Avertissements et options
- 🔵 **Cyan**: Informations et en-têtes
- ⚪ **Blanc**: Données importantes (IDs, valeurs)

## Architecture

Le projet suit une architecture en couches:

### Couche Modèle

- `Vehicule.java` - Classe de base abstraite
- `Berline.java`, `SUV.java`, `Camion.java`, `Electrique.java` - Classes spécialisées
- `Client.java` - Modèle client

### Couche DAO (Data Access Object)

- `VehiculeDAO.java` - Opérations CRUD véhicules
- `ClientDAO.java` - Opérations CRUD clients
- `VenteDAO.java` - Statistiques et rapports

### Couche Présentation

- `Main.java` - Interface utilisateur console
- `ColorUtil.java` - Utilitaire de formatage

### Couche Données

- `DatabaseConnection.java` - Singleton pour la connexion MySQL

## Base de Données

### Tables

#### 1. vehicules

Stocke tous les véhicules avec leurs caractéristiques

- `id`: Identifiant unique (AUTO_INCREMENT)
- `marque`, `modele`: Informations du véhicule
- `prix_achat`, `prix_vente`: Prix en DH
- `annee`, `kilometrage`: Caractéristiques techniques
- `type_vehicule`: Type (BERLINE, SUV, CAMION, ELECTRIQUE)
- `statut`: DISPONIBLE ou VENDU
- `date_ajout`: Date d'ajout automatique
- `date_vende`: Date de vente (NULL si non vendu — nom de colonne tel que défini dans le script SQL)

#### 2. clients

Stocke les informations des clients

- `id`: Identifiant unique (AUTO_INCREMENT)
- `nom`: Nom du client
- `telephone`: Numéro de téléphone
- `email`: Email (optionnel)
- `adresse`: Adresse (optionnel)
- `date_creation`: Date de création automatique

#### 3. ventes

Stocke l'historique détaillé des ventes

- `id`: Identifiant unique (AUTO_INCREMENT)
- `vehicule_id`: Référence au véhicule vendu
- `client_id`: Référence au client acheteur
- `prix_vente_final`: Prix réellement payé (avec taxes, remise éventuelle, etc.)
- `date_vente`: Date de la vente
- `mode_paiement`: COMPTANT, CREDIT ou CHEQUE
- `notes`: Commentaires libres sur la vente

## Améliorations Futures

- [ ] Génération de factures PDF
- [ ] Export des données (CSV, Excel)
- [ ] Interface graphique (GUI)
- [ ] Authentification multi-utilisateurs
- [ ] Statistiques avancées (graphiques, tendances)
- [ ] Gestion des stocks et alertes
- [ ] Module de facturation
- [ ] Intégration paiement

## Dépannage

### Erreur de connexion MySQL

```
Erreur de connexion: Access denied for user 'root'@'localhost'
```

**Solution:** Vérifiez le mot de passe MySQL dans `DatabaseConnection.java`

### Driver MySQL introuvable

```
Driver MySQL introuvable: java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

**Solution:** Ajoutez le fichier `mysql-connector-java-X.X.XX.jar` au classpath

### Erreur d'encodage

```
Caractères français mal affichés
```

**Solution:**

```bash
javac -encoding UTF-8 src/*.java
java -Dfile.encoding=UTF-8 Main
```

## Auteur

Projet développé pour ENSIAS - Semestre 5

## Licence

Ce projet est à des fins éducatives.

---

**Version:** 1.0  
**Date:** Décembre 2024  
**Langage:** Java 8+  
**Base de données:** MySQL 5.7+
