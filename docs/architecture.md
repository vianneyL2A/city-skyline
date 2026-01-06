EnergyTycoon/
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   ├── 📁 com/
│   │   │   │   ├── 📁 energytycoon/
│   │   │   │   │   │
│   │   │   │   │   ├── 📄 Main.java                     # Point d'entrée de l'application
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 model/                        # 🔷 MODÈLE - Logique métier & données
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 entities/                 # Entités du jeu
│   │   │   │   │   │   │   ├── 📄 City.java             # Ville (conteneur principal)
│   │   │   │   │   │   │   ├── 📄 Residence.java        # Résidence des habitants
│   │   │   │   │   │   │   ├── 📄 ResidenceLevel.java   # Enum des niveaux de résidence
│   │   │   │   │   │   │   ├── 📄 Inhabitant.java       # Habitant
│   │   │   │   │   │   │   └── 📄 Player.java           # Joueur/Gestionnaire
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 energy/                   # Système énergétique
│   │   │   │   │   │   │   ├── 📄 PowerPlant.java       # Classe abstraite centrale
│   │   │   │   │   │   │   ├── 📄 CoalPlant.java        # Centrale à charbon
│   │   │   │   │   │   │   ├── 📄 SolarPlant.java       # Centrale solaire
│   │   │   │   │   │   │   ├── 📄 WindPlant.java        # Éolienne
│   │   │   │   │   │   │   ├── 📄 NuclearPlant. java     # Centrale nucléaire
│   │   │   │   │   │   │   ├── 📄 HydroPlant.java       # Centrale hydraulique
│   │   │   │   │   │   │   ├── 📄 PlantLevel.java       # Enum des niveaux de centrale
│   │   │   │   │   │   │   └── 📄 EnergyType.java       # Enum des types d'énergie
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 economy/                  # Système économique
│   │   │   │   │   │   │   ├── 📄 Resource.java         # Ressources/Monnaie
│   │   │   │   │   │   │   ├── 📄 Market.java           # Marché de l'électricité
│   │   │   │   │   │   │   ├── 📄 Transaction.java      # Historique des transactions
│   │   │   │   │   │   │   └── 📄 PricingStrategy.java  # Stratégie de tarification
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 simulation/               # Moteur de simulation
│   │   │   │   │   │   │   ├── 📄 GameEngine.java       # Moteur principal du jeu
│   │   │   │   │   │   │   ├── 📄 TimeManager.java      # Gestion des cycles de temps
│   │   │   │   │   │   │   ├── 📄 TimeCycle.java        # Enum (JOUR, MOIS, ANNEE)
│   │   │   │   │   │   │   ├── 📄 EventManager.java     # Gestion des événements aléatoires
│   │   │   │   │   │   │   ├── 📄 GameEvent.java        # Événement du jeu
│   │   │   │   │   │   │   └── 📄 GameState.java        # État actuel du jeu
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 statistics/               # Statistiques et métriques
│   │   │   │   │   │   │   ├── 📄 HappinessCalculator.java  # Calcul satisfaction habitants
│   │   │   │   │   │   │   ├── 📄 EnergyStatistics.java     # Stats de production/conso
│   │   │   │   │   │   │   └── 📄 FinancialReport.java      # Rapport financier
│   │   │   │   │   │   │
│   │   │   │   │   │   └── 📁 config/                   # Configuration du jeu
│   │   │   │   │   │       ├── 📄 GameConfig. java       # Paramètres globaux
│   │   │   │   │   │       └── 📄 RandomRange.java      # Utilitaire pour valeurs aléatoires
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 view/                         # 🔶 VUE - Interface utilisateur
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📄 MainWindow.java           # Fenêtre principale
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 panels/                   # Panneaux de l'interface
│   │   │   │   │   │   │   ├── 📄 DashboardPanel.java   # Tableau de bord principal
│   │   │   │   │   │   │   ├── 📄 CityMapPanel.java     # Vue de la ville
│   │   │   │   │   │   │   ├── 📄 PowerPlantPanel.java  # Gestion des centrales
│   │   │   │   │   │   │   ├── 📄 ResidencePanel.java   # Vue des résidences
│   │   │   │   │   │   │   ├── 📄 EconomyPanel.java     # Vue économique
│   │   │   │   │   │   │   ├── 📄 StatisticsPanel.java  # Statistiques détaillées
│   │   │   │   │   │   │   └── 📄 EventLogPanel.java    # Journal des événements
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 components/               # Composants réutilisables
│   │   │   │   │   │   │   ├── 📄 ResourceBar.java      # Barre de ressources
│   │   │   │   │   │   │   ├── 📄 HappinessGauge.java   # Jauge de satisfaction
│   │   │   │   │   │   │   ├── 📄 EnergyMeter.java      # Indicateur énergie
│   │   │   │   │   │   │   ├── 📄 TimeControlBar.java   # Contrôle du temps
│   │   │   │   │   │   │   ├── 📄 PlantCard.java        # Carte d'une centrale
│   │   │   │   │   │   │   └── 📄 NotificationPopup.java # Notifications
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📁 dialogs/                  # Fenêtres de dialogue
│   │   │   │   │   │   │   ├── 📄 BuildPlantDialog.java # Construction centrale
│   │   │   │   │   │   │   ├── 📄 UpgradeDialog.java    # Amélioration
│   │   │   │   │   │   │   ├── 📄 GameOverDialog.java   # Fin de partie
│   │   │   │   │   │   │   └── 📄 SettingsDialog.java   # Paramètres
│   │   │   │   │   │   │
│   │   │   │   │   │   └── 📁 styles/                   # Styles et thèmes
│   │   │   │   │   │       ├── 📄 Theme.java            # Thème de l'application
│   │   │   │   │   │       └── 📄 Colors.java           # Palette de couleurs
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 controller/                   # 🔷 CONTRÔLEUR - Logique de contrôle
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── 📄 GameController.java       # Contrôleur principal
│   │   │   │   │   │   ├── 📄 CityController.java       # Gestion de la ville
│   │   │   │   │   │   ├── 📄 PowerPlantController.java # Gestion des centrales
│   │   │   │   │   │   ├── 📄 EconomyController.java    # Gestion économique
│   │   │   │   │   │   ├── 📄 TimeController.java       # Contrôle du temps
│   │   │   │   │   │   └── 📄 EventController.java      # Gestion des événements
│   │   │   │   │   │
│   │   │   │   │   ├── 📁 observer/                     # Pattern Observer
│   │   │   │   │   │   ├── 📄 GameObserver.java         # Interface observateur
│   │   │   │   │   │   ├── 📄 GameObservable.java       # Interface observable
│   │   │   │   │   │   └── 📄 GameEventType.java        # Types d'événements observables
│   │   │   │   │   │
│   │   │   │   │   └── 📁 utils/                        # Utilitaires
│   │   │   │   │       ├── 📄 Constants.java            # Constantes du jeu
│   │   │   │   │       ├── 📄 RandomGenerator.java      # Générateur aléatoire
│   │   │   │   │       ├── 📄 GameLogger.java           # Logger du jeu
│   │   │   │   │       └── 📄 SaveManager.java          # Sauvegarde/Chargement
│   │   │   │   │
│   │   │   │   └── 📁 resources/                        # Ressources
│   │   │   │       ├── 📁 images/                       # Images et icônes
│   │   │   │       │   ├── 📁 plants/                   # Icônes des centrales
│   │   │   │       │   ├── 📁 buildings/                # Icônes des bâtiments
│   │   │   │       │   └── 📁 ui/                       # Éléments d'interface
│   │   │   │       ├── 📁 sounds/                       # Sons (optionnel)
│   │   │   │       └── 📁 config/                       # Fichiers de configuration
│   │   │   │           └── 📄 game-config.properties    # Paramètres par défaut
│   │   │   │
│   │   └── 📁 test/                                     # Tests unitaires
│   │       └── 📁 java/
│   │           └── 📁 com/
│   │               └── 📁 energytycoon/
│   │                   ├── 📁 model/
│   │                   │   ├── 📄 CityTest.java
│   │                   │   ├── 📄 PowerPlantTest.java
│   │                   │   └── 📄 GameEngineTest.java
│   │                   └── 📁 controller/
│   │                       └── 📄 GameControllerTest.java
│   │
├── 📄 pom.xml                                           # Configuration Maven
├── 📄 README. md                                         # Documentation du projet
├── 📄 . gitignore                                        # Fichiers à ignorer par Git
└── 📄 LICENSE                                           # Licence du projet 