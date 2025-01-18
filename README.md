Voici une version mise à jour de votre fichier `README.md` incluant les informations sur le script que vous avez fourni. Ce script simplifie l'utilisation pour les personnes qui ne souhaitent pas utiliser Apache Ant.

---

```markdown
# **README - Jeu de Stratégie au Tour par Tour**

---

## **1. Informations sur le projet**

### **Encadrement**
Ce projet a été réalisé dans le cadre de la formation en **Licence 3 Informatique** sous la supervision de **[CHARRIER Christophe]**, dans un environnement pédagogique visant à appliquer des concepts avancés de programmation.

---

## **2. Description du projet**

Le **Jeu de Stratégie au Tour par Tour** est un projet de simulation stratégique où des joueurs (humains ou IA) s’affrontent sur une grille 2D. L’objectif est de combiner tactiques et gestion des ressources pour éliminer tous les adversaires.

### **Principales fonctionnalités :**
- **Déplacements :** Les joueurs se déplacent librement sur la grille.
- **Attaques :** Tirs directionnels (horizontaux et verticaux).
- **Explosifs :**
  - *Mines* : Explosion immédiate au contact.
  - *Bombes* : Explosion différée avec délais configurables.
- **Boucliers :** Protection temporaire contre les attaques.
- **Ressources :**
  - Consommation d’énergie pour chaque action.
  - Reconstitution grâce à des éléments sur la grille.
- **Obstacles :** Murs qui bloquent les mouvements et les tirs.
- **IA Avancée :** Plusieurs niveaux d'intelligence artificielle utilisant des patterns de stratégie.
- **Mode de démarrage configurable :** Console ou interface graphique (Swing).

---

## **3. Installation et exécution**

### **3.1 Prérequis**
Assurez-vous que les outils suivants sont installés :
- **Java JDK** (version 8 ou plus récente).
- **Apache Ant** pour la gestion du projet (facultatif si vous utilisez le script fourni).

---

### **3.2 Utilisation avec Apache Ant**

#### **Compilation**
Pour compiler le projet, exécutez :
```bash
ant compile
```

#### **Nettoyage**
Pour réinitialiser l’arborescence et supprimer les fichiers générés :
```bash
ant clean
```

#### **Lancer le jeu**
Pour démarrer le jeu (console ou graphique, selon la configuration) :
```bash
ant run
```

#### **Exécuter les tests unitaires**
Pour exécuter les tests unitaires définis :
```bash
ant test
```

#### **Créer un fichier exécutable**
Pour générer un fichier `.jar` exécutable :
```bash
ant dist
```

#### **Générer la documentation**
Pour produire la documentation technique (Javadoc) :
```bash
ant javadoc
```

---

### **3.3 Utilisation avec le script Bash**

Si vous ne souhaitez pas utiliser Apache Ant, un script simplifié (`script.sh`) est fourni. Voici les commandes disponibles avec ce script :

#### Instructions d'exécution

1. **Configurer les droits d'exécution :**
   ```bash
   chmod +x script.sh
   ```

#### **Nettoyage**
Pour nettoyer les fichiers générés :
```bash
./script.sh clean
```

#### **Compilation**
Pour compiler le projet :
```bash
./script.sh compile
```

#### **Exécution du jeu**
Pour lancer le jeu directement :
```bash
./script.sh run
```

#### **Tests unitaires**
Pour exécuter les tests unitaires :
```bash
./script.sh test
```

#### **Créer un fichier exécutable**
Pour générer un fichier JAR exécutable :
```bash
./script.sh jar
```

#### **Documentation**
Pour générer la documentation Javadoc :
```bash
./script.sh javadoc
```

---

## **4. Configurations**

Les paramètres du jeu peuvent être ajustés dans le fichier `config.xml`, notamment :
- **Grille :** Taille de la grille de jeu.
- **Mode :** Console ou graphique.
- **IA :** Stratégies et niveaux de difficulté.
- **Armes et explosifs :** Types et caractéristiques.

---

## **5. Structure du projet**

Le projet adopte une architecture **MVC** (Modèle-Vue-Contrôleur) pour favoriser la modularité et la maintenance :
- **src/** : Code source principal.
  - `controleur/` : Gestion des interactions et de la logique.
  - `modele/` : Règles métier et gestion des données.
  - `vue/` : Affichage console ou graphique.
- **config/** : Fichiers de configuration modifiables.
- **doc/** : Documentation générée par Javadoc.
- **dist/** : Contient les fichiers `.jar` générés.
- **lib/** : Bibliothèques externes nécessaires.

---

Ce fichier README offre une présentation complète pour comprendre, installer, et exécuter ce projet de jeu stratégique.

---

