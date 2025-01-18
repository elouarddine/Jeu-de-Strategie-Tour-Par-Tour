#!/bin/bash

# Définir les répertoires et propriétés
SRC_DIR="src"
TEST_DIR="test"
BUILD_DIR="build"
CLASSES_DIR="$BUILD_DIR/classes"
TEST_CLASSES_DIR="$BUILD_DIR/test-classes"
LIB_DIR="lib"
DIST_DIR="dist"
MAIN_CLASS="model.Main"
CONFIG_DIR="config"
RESOURCES_DIR="ressources"

# Nettoyer le répertoire de build
clean() {
    echo "Nettoyage du répertoire de build..."
    rm -rf "$BUILD_DIR"
    # rm -rf "$DIST_DIR" # Décommenter si vous souhaitez supprimer le dossier dist
}

# Compiler les sources
compile() {
    clean
    echo "Compilation des fichiers sources..."
    mkdir -p "$CLASSES_DIR"
    javac -d "$CLASSES_DIR" -cp "$(find "$LIB_DIR" -name '*.jar' | tr '\n' ':')" $(find "$SRC_DIR" -name '*.java')
    
    echo "Copie des fichiers de configuration..."
    mkdir -p "$CLASSES_DIR/config"
    cp -r "$CONFIG_DIR/"* "$CLASSES_DIR/config"

    echo "Copie des ressources..."
    mkdir -p "$CLASSES_DIR/ressources"
    cp -r "$RESOURCES_DIR/"* "$CLASSES_DIR/ressources"
}

# Compiler les tests
compile_tests() {
    compile
    echo "Compilation des fichiers de test..."
    mkdir -p "$TEST_CLASSES_DIR"
    javac -d "$TEST_CLASSES_DIR" -cp "$CLASSES_DIR:$(find "$LIB_DIR" -name '*.jar' | tr '\n' ':')" $(find "$TEST_DIR" -name '*.java')
}

# Exécuter les tests unitaires
run_tests() {
    compile_tests
    echo "Exécution des tests..."
    mkdir -p "$BUILD_DIR/test-results"
    java -cp "$CLASSES_DIR:$TEST_CLASSES_DIR:$(find "$LIB_DIR" -name '*.jar' | tr '\n' ':')" org.junit.runner.JUnitCore $(find "$TEST_CLASSES_DIR" -name '*Test.class' | sed 's|.*/||' | sed 's|.class$||')
}

# Créer le JAR exécutable
create_jar() {
    compile
    echo "Création du JAR exécutable..."
    mkdir -p "$DIST_DIR"
    jar cfve "$DIST_DIR/FightingProject.jar" "$MAIN_CLASS" -C "$CLASSES_DIR" .
}

# Générer la documentation Javadoc
generate_javadoc() {
    clean
    echo "Génération de la documentation Javadoc..."
    mkdir -p doc
    javadoc -d doc -sourcepath "$SRC_DIR" -cp "$(find "$LIB_DIR" -name '*.jar' | tr '\n' ':')" $(find "$SRC_DIR" -name '*.java' | sed 's|.*/||' | sed 's|.java$||')
}

# Exécuter l'application
run() {
    create_jar
    echo "Exécution de l'application..."
    java -jar "$DIST_DIR/FightingProject.jar"
}

# Afficher l'aide
help() {
    echo "Utilisation : $0 [commande]"
    echo "Commandes disponibles :"
    echo "  clean          Nettoyer les fichiers générés"
    echo "  compile        Compiler les fichiers sources"
    echo "  compile-tests  Compiler les fichiers de test"
    echo "  test           Exécuter les tests unitaires"
    echo "  jar            Créer le JAR exécutable"
    echo "  javadoc        Générer la documentation Javadoc"
    echo "  run            Exécuter l'application"
}

# Exécuter la commande spécifiée
if [ $# -eq 0 ]; then
    help
else
    "$@"
fi
