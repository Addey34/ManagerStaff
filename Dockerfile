# Étape 1 : Utiliser une image de base avec OpenJDK 21
FROM openjdk:21-jdk-slim

# Étape 2 : Définir un répertoire de travail
WORKDIR /app

# Étape 3 : Copier le fichier pom.xml pour télécharger les dépendances Maven (optionnel)
COPY pom.xml .

# Étape 4 : Télécharger les dépendances Maven
RUN mvn dependency:go-offline

# Étape 5 : Copier tous les fichiers du projet dans le conteneur
COPY . .

# Étape 6 : Compiler l'application (utilise Maven)
RUN mvn clean install

# Étape 7 : Exposer le port sur lequel l'application va écouter
EXPOSE 8080

# Étape 8 : Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "target/gestion-employes-0.0.1-SNAPSHOT.jar"]
