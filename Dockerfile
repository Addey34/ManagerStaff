# Étape 1 : Utiliser une image de base avec OpenJDK 21
FROM openjdk:21-jdk-slim

# Étape 2 : Installer Maven
RUN apt-get update && apt-get install -y maven

# Étape 3 : Définir un répertoire de travail
WORKDIR /app

# Étape 4 : Copier le fichier pom.xml pour télécharger les dépendances Maven (optionnel)
COPY pom.xml .

# Étape 5 : Télécharger les dépendances Maven
RUN mvn dependency:go-offline

# Étape 6 : Copier tous les fichiers du projet dans le conteneur
COPY . .

# Étape 7 : Compiler l'application (utilise Maven)
RUN mvn clean install

# Étape 8 : Exposer le port sur lequel l'application va écouter
EXPOSE 8080

# Étape 9 : Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "target/gestion-employes-0.0.1-SNAPSHOT.jar"]
