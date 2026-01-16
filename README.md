# City Skyline - Gestionnaire d'Énergie

Un jeu de simulation en Java Swing où vous gérez le réseau électrique d'une ville en pleine croissance.

## Prérequis

- **Java 21** (JDK)
- **Maven 3.8+**

## Lancement du jeu

```bash
cd /Users/m2pro/NetBeansProjects/city-skyline
mvn compile exec:java
```

## Comment Jouer

### 1. Menu Principal
Au lancement, choisissez votre niveau de difficulté :

| Niveau | Argent Initial | Bonheur Initial | Seuil Game Over |
|--------|---------------|-----------------|-----------------|
| Facile | 15 000€ | 85% | 3% |
| Normal | 10 000€ | 75% | 5% |
| Difficile | 5 000€ | 60% | 10% |

Cliquez sur **"Démarrer la partie"** pour commencer.

### 2. Interface de Jeu

L'écran est divisé en plusieurs zones :

- **Barre supérieure** :  Argent, production/demande d'énergie, date, population, bonheur
- **Onglets** :
  - **Carte** : Vue de la ville, construction de bâtiments
  - **Tableau de bord** : Statistiques globales
  - **Centrales** :  Gestion des centrales électriques
  - **Économie** : Revenus, dépenses, transactions
  - **Résidences** : Liste et amélioration des logements

### 3. Construire sur la Carte

1. Sélectionnez un **outil** dans le panneau de droite : 
   - **Résidence** :  Consomme de l'énergie, paie des taxes
   - **Centrale Solaire** : Production 50 kWh, coût 1000€
   - **Éolienne** : Production 75 kWh, coût 1500€
   - **Centrale Thermique** : Production 200 kWh, coût 3000€
   - **Ligne électrique** : Connecte centrales et résidences éloignées

2. Cliquez sur une case de la carte pour construire

### 4. Système Électrique

- Les centrales alimentent les résidences dans un **rayon de couverture**
- Les résidences non alimentées ne payent pas de taxes et réduisent le bonheur
- Utilisez les **lignes électriques** pour connecter des résidences éloignées

### 5. Économie

- **Revenus** : Vente d'électricité + taxes des habitants
- **Dépenses** : Maintenance des centrales
- L'argent s'affiche en haut à gauche

### 6. Conditions de Fin de Partie

Le jeu se termine si :
- **Bonheur < seuil** : Les habitants mécontents vous renvoient
- **Argent < 0** : Faillite

## Contrôles

- **Play/Pause** : Démarre ou met en pause le temps
- **Vitesse** : Ajustez la vitesse du jeu avec le slider

## Structure du Projet

```
src/main/java/tg/univlome/epl/ajee/city/skyline/
├── Main.java                    # Point d'entrée
├── model/
│   ├── entities/                # Résidences, Habitants, Joueur
│   ├── energy/                  # Centrales électriques
│   ├── economy/                 # Marché, transactions
│   ├── map/                     # Carte, cellules, terrain
│   └── simulation/              # Moteur de jeu, temps, événements
├── view/
│   ├── panels/                  # Panneaux UI (Menu, Carte, Dashboard...)
│   ├── components/              # Composants réutilisables
│   └── styles/                  # Couleurs, thème
├── observer/                    # Pattern Observer
└── utils/                       # Constantes, utilitaires
```

## Équipe de Développement

**Projet universitaire - EPL AJEE, Université de Lomé**

### Contributions

#### **DOGBO Sarah A. Afi** - [@SARAH-GIT90](https://github.com/SARAH-GIT90)
- Architecture initiale du projet et configuration
- Système de centralisation des revenus et ajustement des valeurs des résidences
- Intégration de la carte (CityMap) avec la logique de jeu et l'interface utilisateur
- Règles de placement et affichage des centrales électriques
- Images dédiées pour les tuiles des centrales électriques

#### **BARCOLA Mazamesso** - [@vianneyL2A](https://github.com/vianneyL2A)
- Implémentation du système de lignes électriques (powerlines)
- Mise à jour du système de vente d'énergie sur le marché
- Couverture directe des centrales pour les résidences avec documentation du système de taxes
- Panel Économie : affichage des résumés financiers, transactions récentes et statistiques
- Système de collecte de taxes basé sur le niveau des résidents et leur approvisionnement énergétique
- Types de terrain et système de grille électrique avec propagation d'énergie
- Gestion du temps : système de cycles TICK et avancement de la simulation
- Paramètres d'équilibre du jeu et logique de progression des cycles
- Niveaux de difficulté et fonctionnalité de réinitialisation du jeu
- Refactorisation de l'initialisation du moteur de jeu
- Utilitaire de formatage de durée pour les événements
- Panel des Résidences pour la gestion des logements de la ville
- Refactorisation du `ButtonRenderer` pour améliorer la fiabilité
- Documentation complète :  README détaillé sur la configuration, le gameplay et la structure du projet
- Expansion du fichier . gitignore

---
