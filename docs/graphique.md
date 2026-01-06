# Guide Graphique - CitySkyline

## Vue d'ensemble

L'interface graphique du jeu est construite avec **Java Swing** et suit une approche minimaliste mais fonctionnelle, inspirée de jeux comme City Skylines et SimCity.

---

## 🗺️ La Carte de la Ville

### Concept
La carte est une **grille 2D** représentant le territoire de la ville. Chaque cellule peut contenir un bâtiment.

### Dimensions
- Grille de **20x20 cases** (400 emplacements)
- Chaque case fait **40x40 pixels**
- Dimensions totales : 800x800 pixels (scrollable)

### Types de cellules

| Type | Couleur | Icône | Description |
|------|---------|-------|-------------|
| Vide | Vert clair 🟩 | - | Terrain constructible |
| Résidence | Bleu 🟦 | 🏠 | Habitation des citoyens |
| Charbon | Gris foncé ⬛ | 🏭 | Centrale à charbon |
| Solaire | Jaune 🟨 | ☀️ | Panneaux solaires |
| Éolien | Cyan 🟦 | 🌬️ | Éolienne |
| Nucléaire | Violet 🟪 | ⚛️ | Centrale nucléaire |
| Hydraulique | Bleu foncé 🟦 | 💧 | Barrage |
| Non alimenté | Rouge 🟥 | ⚠️ | Résidence sans électricité |

---

## 🎮 Interactions

### Placement de bâtiments
1. Sélectionner un type dans la barre d'outils
2. Cliquer sur une case vide
3. Confirmation si assez d'argent

### Sélection
- Clic sur un bâtiment → affiche ses détails
- Possibilité d'améliorer ou de détruire

### Zones d'influence
- Les centrales ont une **zone de couverture** (rayon)
- Les résidences dans la zone sont alimentées
- Visualisation par cercle semi-transparent

---

## 🎨 Palette de couleurs

```
Primaire      : #2E7D32 (Vert énergie)
Secondaire    : #FF9800 (Orange)
Fond          : #F5F5F5 (Gris clair)
Surface       : #FFFFFF (Blanc)
Succès        : #4CAF50 (Vert)
Attention     : #FFC107 (Jaune)
Erreur        : #F44336 (Rouge)
```

---

## 📐 Structure de l'interface

```
┌─────────────────────────────────────────────────────┐
│  Barre de ressources (argent, date, énergie)        │
├─────────────────────────────────────────────────────┤
│  Onglets : [Carte] [Centrales] [Résidences] [Stats] │
├───────────────────────┬─────────────────────────────┤
│                       │                             │
│                       │   Panneau de détails        │
│   CARTE DE LA VILLE   │   (info sur sélection)      │
│   (grille 20x20)      │                             │
│                       │   [Améliorer] [Détruire]    │
│                       │                             │
├───────────────────────┴─────────────────────────────┤
│  Barre d'outils : [🏠] [🏭] [☀️] [🌬️] [⚛️] [💧]      │
├─────────────────────────────────────────────────────┤
│  Contrôles : [▶ Jouer] [⏸ Pause] [Vitesse: x1]     │
└─────────────────────────────────────────────────────┘
```

---

## 🔧 Composants Swing utilisés

| Composant | Usage |
|-----------|-------|
| `JFrame` | Fenêtre principale |
| `JPanel` (custom) | Carte avec `paintComponent()` |
| `JTabbedPane` | Navigation par onglets |
| `JButton` | Actions et outils |
| `JLabel` | Affichage d'informations |
| `JProgressBar` | Jauges (bonheur, énergie) |
| `JScrollPane` | Défilement de la carte |

---

## 📱 Responsive

- Fenêtre redimensionnable (minimum 1024x600)
- La carte utilise un `JScrollPane` si nécessaire
- Les panneaux s'adaptent à la taille
