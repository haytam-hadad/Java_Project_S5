# Système de Gestion de Vente de Véhicules d'Occasion

## Description
Application Java complète pour la gestion de la vente de véhicules d'occasion avec une interface en ligne de commande.

## Fonctionnalités

### 1. Gestion des Véhicules
- ✅ Ajouter un véhicule (BERLINE, SUV, CAMION, ELECTRIQUE)
- ✅ Lister tous les véhicules
- ✅ Lister les véhicules disponibles
- ✅ Rechercher des véhicules par critères (marque, type, prix, année)
- ✅ Modifier le statut d'un véhicule (DISPONIBLE, EN_NEGOCIATION, VENDU)
- ✅ Supprimer un véhicule

### 2. Gestion des Clients
- ✅ Ajouter un client
- ✅ Lister tous les clients
- ✅ Rechercher un client (par nom ou téléphone)
- ✅ Modifier les informations d'un client
- ✅ Consulter l'historique des opérations d'un client
- ✅ Supprimer un client

### 3. Gestion des Ventes
- ✅ Enregistrer une vente (associer un véhicule à un client)
- ✅ Calcul automatique du prix final avec taxes selon le type de véhicule
- ✅ Statistiques de ventes (nombre vendus, chiffre d'affaires, etc.)

### 4. Historique et Traçabilité
- ✅ Traçage automatique de toutes les opérations sur les clients (AJOUT, MODIFICATION, CONSULTATION)
- ✅ Consultation de l'historique complet d'un client

## Structure du Projet

```
Java_Project_S5/
├── src/
│   ├── Main.java                    # Point d'entrée avec menu interactif
│   ├── DatabaseConnection.java      # Gestion de la connexion MySQL (Singleton)
│   ├── Vehicule.java                # Classe abstraite pour les véhicules
│   ├── VehiculeImpl.java            # Implémentation concrète de Vehicule
│   ├── Berline.java                 # Classe pour les berlines
│   ├── SUV.java                     # Classe pour les SUV
│   ├── Camion.java                  # Classe pour les camions
│   ├── VoitureElectrique.java       # Classe pour les voitures électriques
│   ├── Client.java                  # Classe pour les clients
│   ├── VehiculeDAO.java             # DAO pour les opérations CRUD sur les véhicules
│   ├── ClientDAO.java               # DAO pour les opérations CRUD sur les clients
│   ├── VenteDAO.java                # DAO pour la gestion des ventes
│   └── HistoriqueClientDAO.java     # DAO pour l'historique des clients
├── database.sql                     # Script SQL pour créer la base de données
└── README.md                        # Documentation du projet
```

## Prérequis

1. **Java JDK 8 ou supérieur**
2. **MySQL Server** (version 5.7 ou supérieure)
3. **MySQL Connector/J** (driver JDBC pour MySQL)
   - Télécharger depuis: https://dev.mysql.com/downloads/connector/j/
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

### 2. Configuration de la Connexion

Modifiez les paramètres dans `DatabaseConnection.java` si nécessaire:
```java
private static final String URL = "jdbc:mysql://localhost:3306/gestion_voitures";
private static final String USERNAME = "root";
private static final String PASSWORD = "";  // Votre mot de passe MySQL
```

### 3. Compilation

```bash
# Compiler tous les fichiers Java
javac -cp ".:mysql-connector-java-8.0.XX.jar" src/*.java

# Ou si vous utilisez un IDE comme Eclipse/IntelliJ, configurez le classpath
```

### 4. Exécution

```bash
# Exécuter l'application
java -cp ".:src:mysql-connector-java-8.0.XX.jar" Main
```

## Utilisation

### Menu Principal
L'application démarre avec un menu principal offrant 4 options:
1. **Gestion des véhicules** - CRUD complet sur les véhicules
2. **Gestion des clients** - CRUD complet sur les clients
3. **Gestion des ventes** - Enregistrer des ventes et voir les statistiques
4. **Statistiques** - Afficher les statistiques globales

### Exemple d'utilisation

1. **Ajouter un véhicule:**
   - Choisir option 1 → 1
   - Remplir les informations demandées
   - Le véhicule est automatiquement ajouté à la base de données

2. **Ajouter un client:**
   - Choisir option 2 → 1
   - Remplir les informations du client
   - L'opération est automatiquement enregistrée dans l'historique

3. **Enregistrer une vente:**
   - Choisir option 3 → 1
   - Sélectionner un véhicule disponible
   - Sélectionner un client
   - La vente est enregistrée et le véhicule est marqué comme vendu

## Calcul des Prix

Le système calcule automatiquement le prix final avec taxes selon le type:
- **BERLINE**: +15% de taxe
- **SUV**: +20% de taxe
- **CAMION**: +18% de taxe
- **ELECTRIQUE**: +10% de taxe

## Architecture

Le projet suit une architecture en couches:
- **Modèle**: Classes métier (Vehicule, Client)
- **DAO (Data Access Object)**: Accès aux données (VehiculeDAO, ClientDAO, etc.)
- **Vue/Contrôleur**: Interface utilisateur dans Main.java

## Base de Données

### Tables

1. **vehicules**: Stocke tous les véhicules avec leurs caractéristiques
2. **clients**: Stocke les informations des clients
3. **historique_client**: Trace toutes les opérations sur les clients

### Relations
- `historique_client.client_id` → `clients.id` (Foreign Key avec CASCADE)

## Notes Importantes

- Les IDs doivent être uniques pour les véhicules et les clients
- Le statut d'un véhicule peut être: DISPONIBLE, EN_NEGOCIATION, ou VENDU
- Toutes les opérations sur les clients sont automatiquement tracées
- Les ventes utilisent des transactions pour garantir la cohérence des données

## Auteur

Projet développé pour ENSIASD - Semestre 5

## Licence

Ce projet est à des fins éducatives.
