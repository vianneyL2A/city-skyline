# 🏙️ City Skyline - Gestionnaire d'Énergie

Un jeu de simulation en Java Swing où vous gérez le réseau électrique d'une ville en pleine croissance.

## 📋 Prérequis

- **Java 21** (JDK)
- **Maven 3.8+**

## 🚀 Lancement du jeu

```bash
cd /Users/m2pro/NetBeansProjects/city-skyline
mvn compile exec:java
```

## 🎮 Comment Jouer

### 1. Menu Principal
Au lancement, choisissez votre niveau de difficulté :

| Niveau | Argent Initial | Bonheur Initial | Seuil Game Over |
|--------|---------------|-----------------|-----------------|
| 🌱 Facile | 15 000€ | 85% | 3% |
| ⚡ Normal | 10 000€ | 75% | 5% |
| 🔥 Difficile | 5 000€ | 60% | 10% |

Cliquez sur **"Démarrer la partie"** pour commencer.

### 2. Interface de Jeu

L'écran est divisé en plusieurs zones :

- **Barre supérieure** : Argent, production/demande d'énergie, date, population, bonheur
- **Onglets** :
  - 🗺️ **Carte** : Vue de la ville, construction de bâtiments
  - 📊 **Tableau de bord** : Statistiques globales
  - ⚡ **Centrales** : Gestion des centrales électriques
  - 💰 **Économie** : Revenus, dépenses, transactions
  - 🏠 **Résidences** : Liste et amélioration des logements

### 3. Construire sur la Carte

1. Sélectionnez un **outil** dans le panneau de droite :
   - 🏠 **Résidence** : Consomme de l'énergie, paie des taxes
   - ☀️ **Centrale Solaire** : Production 50 kWh, coût 1000€
   - 💨 **Éolienne** : Production 75 kWh, coût 1500€
   - 🔥 **Centrale Thermique** : Production 200 kWh, coût 3000€
   - ⚡ **Ligne électrique** : Connecte centrales et résidences éloignées

2. Cliquez sur une case de la carte pour construire

### 4. Système Électrique

- Les centrales alimentent les résidences dans un **rayon de couverture**
- Les résidences non alimentées (❌) ne payent pas de taxes et réduisent le bonheur
- Utilisez les **lignes électriques** pour connecter des résidences éloignées

### 5. Économie

- **Revenus** : Vente d'électricité + taxes des habitants
- **Dépenses** : Maintenance des centrales
- L'argent s'affiche en haut à gauche

### 6. Conditions de Fin de Partie

Le jeu se termine si :
- 💀 **Bonheur < seuil** : Les habitants mécontents vous renvoient
- 💸 **Argent < 0** : Faillite

## 🎛️ Contrôles

- **▶️ Play/Pause** : Démarre ou met en pause le temps
- **Vitesse** : Ajustez la vitesse du jeu avec le slider

## 📁 Structure du Projet

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

## 👨‍💻 Développé par

Projet universitaire - EPL AJEE, Université de Lomé

---

*Bonne gestion de votre ville !* 🌆
