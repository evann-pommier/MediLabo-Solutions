# --- Étape 1 : compilation ---
# Image temporaire contenant Maven + JDK, utilisée uniquement pour builder
# le .jar. Elle ne sera pas conservée dans l'image finale.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# On copie le pom.xml seul avant le code source : Docker met en cache
# chaque instruction, donc tant que les dépendances ne changent pas,
# ce téléchargement n'est pas refait à chaque modification du code.
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# --- Étape 2 : exécution ---
# Image finale : uniquement un JRE (pas le JDK complet, ni Maven), pour
# rester la plus légère possible. Seul le .jar compilé est copié depuis
# l'étape précédente.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]