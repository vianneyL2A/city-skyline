SUJET POUR LE PROJET DE INF2328
Date de Remise des projets : 16 Janvier 2026
Modalité : Le projet se fera par groupe de 2 (étant donné votre effectif de 20 étudiants), mais
il sera nécessaire que les deux personnes travaillent, à cet effet il vous sera demandé d’inclure
dans le Readme de votre projet une description de la répartition des tâches.
Votre projet de code pour l’UE INF2328 consiste à développer un jeu de gestion, aussi appelé
jeu de type tycoon. Dans ce type de jeu, le joueur ne contrôle pas un personnage en temps réel,
mais prend des décisions stratégiques afin de gérer un système complexe.
Scénario de base
Dans ce projet, le joueur incarne un gestionnaire de production et de distribution d’énergie dans
une petite ville où vivent des habitants répartis dans différentes résidences. Les résidences
possèdent plusieurs niveaux (au moins 3), et leurs besoins en énergie ainsi que leur pouvoir
d’achat électrique varient en fonction de leur niveau.
Le joueur est responsable de la production d’énergie électrique de la ville. Pour cela, il peut
construire et améliorer des centrales électriques de différents types et de différents niveaux. La
construction ou l’amélioration d’une centrale nécessite des ressources (que vous pouvez
représenter sous forme d’une monnaie locale), qui ne sont pas infinies.
Au début du jeu, le joueur dispose d’un stock limité de ressources. Il peut en gagner en vendant
de l’électricité aux foyers.
L’objectif du joueur est d’assurer un approvisionnement énergétique stable, tout en tenant
compte de contraintes techniques, économiques et temporelles. Le niveau de satisfaction des
habitants de la ville (ou de bonheur) est une des métriques aussi du jeu. Il doit aussi maintenir
le niveau de bonheur au-dessus d’un certain seuil.
Sentez-vous libre d’ajouter des éléments intéressants au lore du jeu ou à ce scénario de base.
Cela ne devrait pas passer inaperçu. Une chose intéressante serait de ne pas donner de valeurs
fixes à certains concepts (pouvoir d’achats, besoins d’énergie pour un bâtiment d’un certain
niveau) mais plutôt un intervalle dans lequel on pourra choisir aléatoirement des valeurs et ainsi
éviter un jeu qui se passe de la même manière à chaque fois.
Le jeu se déroule par cycles de temps simulés (jours, mois ou années). À chaque cycle, le
système évolue automatiquement en fonction des décisions du joueur et des règles définies dans
le modèle de simulation.
Il n’y a pas de notion de victoire immédiate : le but principal est de maintenir un système
fonctionnel et cohérent dans la durée. Une mauvaise gestion peut entraîner des pénuries
d’énergie, des pertes financières, voire l’arrêt du système (le maire de la ville vous retire la
gestion de l’électricité vu que tout le monde est mécontent).
Mécaniques de jeu attendues
Le joueur devra notamment :
• Produire de l’énergie à partir de différentes sources,
• Investir dans de nouvelles installations ou les améliorer,
• Surveiller l’équilibre entre production, demande et coûts,
• Faire face à des évolutions du système (par exemple, une augmentation de la demande).
Contraintes techniques
L’accent du projet est mis sur la logique de gestion et de simulation, et non sur l’aspect
graphique.
L’interface doit être fonctionnelle et permettre au joueur de comprendre l’état du système, de
prendre des décisions et d’en observer les conséquences.
Le projet devra être conçu selon une architecture MVC (Modèle – Vue – Contrôleur).
Le choix de la technologie d’interface graphique (Swing ou JavaFX) est laissé aux étudiants
NB : À titre indicatif, vous pouvez jouer au jeu mobile SimCity BuildIt afin de mieux
comprendre le fonctionnement général des jeux de gestion de type tycoon ou encore à
PaperClips