# MediLabo Solutions

Application de dépistage du risque de diabète de type 2, développée en architecture microservices pour le compte du client MédiLabo Solutions.

## Architecture

| Microservice | Rôle | Port | Base de données |
|---|---|---|---|
| `patient` | Gestion des dossiers patients | 8081 | PostgreSQL |
| `notes` | Notes des médecins | 8083 | MongoDB |
| `risk` | Évaluation du risque de diabète | 8084 | Aucune (interroge `patient` et `notes`) |
| `gateway` | Point d'entrée unique (Spring Cloud Gateway) | 8080 | — |
| `front` | Interface utilisateur (Thymeleaf) | 8082 | — |

Chaque microservice back (`patient`, `notes`, `risk`) est sécurisé par Spring Security (authentification HTTP Basic). La gateway centralise l'accès aux API pour le front ; `risk` appelle `patient` et `notes` directement (service-à-service, sans repasser par la gateway).

## Lancer le projet

### Avec Docker (recommandé)

```bash
docker-compose up --build
```

Front accessible sur [http://localhost:8082/patients](http://localhost:8082/patients).

### En local (sans Docker)

Prérequis : PostgreSQL et MongoDB installés et démarrés en local, Java 21, Maven.

Lancer chaque module dans son propre terminal, dans l'ordre :
```bash
cd patient && mvn spring-boot:run
cd notes && mvn spring-boot:run
cd risk && mvn spring-boot:run
cd gateway && mvn spring-boot:run
cd front && mvn spring-boot:run
```

## Tests

Chaque module dispose de sa propre suite de tests (`mvn test` depuis sa racine, ou `mvn test` à la racine du projet pour tout exécuter).

## Green Code

### Objectif du Green Code

Le Green Code (ou éco-conception logicielle) vise à réduire l'empreinte énergétique d'un logiciel *en phase d'usage*, c'est-à-dire pendant son exécution : moins de calculs inutiles, moins de mémoire mobilisée, moins d'échanges réseau superflus. Selon l'Institut du Numérique Responsable, jusqu'à 80 % de l'empreinte environnementale d'un service numérique se décide dès la phase de conception — d'où l'intérêt d'y penser dès l'architecture, pas seulement en optimisation a posteriori.

Concrètement, pour ce projet en microservices, cela touche : le nombre d'appels réseau entre services, la taille des données échangées, la complexité des algorithmes (notamment le calcul de risque), et le dimensionnement des ressources (bases de données, conteneurs Docker).

### Identifier le code énergivore

Le code qui consomme de la mémoire inutilement se repère principalement via des outils de profiling JVM :
- **Java Flight Recorder / Java Mission Control** (intégrés au JDK) : suivi en direct du tas mémoire, des threads et du CPU sans surcharge notable.
- **VisualVM** ou **JProfiler** : identifient les objets qui s'accumulent en mémoire (fuites) et les méthodes les plus coûteuses en temps CPU.
- **Heap dumps** (`jmap`, ou `-XX:+HeapDumpOnOutOfMemoryError`) analysés avec l'Eclipse Memory Analyzer (MAT) : utile pour repérer les collections qui grossissent sans jamais être vidées.

En pratique, les signaux à surveiller dans du code Spring Boot : des requêtes JPA qui chargent des entités entières alors que seuls 2-3 champs sont utilisés, des listes chargées en mémoire puis filtrées côté Java plutôt que côté base de données, ou des appels réseau redondants au sein d'une même requête.

### Pistes d'amélioration identifiées pour ce projet

1. **Appels HTTP séquentiels dans `front-service`** : `patientDetail()` effectue 3 appels bloquants l'un après l'autre (patient, notes, risque) via `RestTemplate`. Passer à un client réactif (`WebClient`) permettrait de paralléliser ces 3 appels indépendants et de réduire le temps de réponse ainsi que le temps CPU/mémoire mobilisé par requête.

2. **Absence de pagination sur `GET /patients` et `GET /notes/patient/{id}`** : ces endpoints renvoient l'intégralité des résultats. Sur un vrai volume de données (des milliers de patients), cela gonfle inutilement la mémoire et la bande passante à chaque appel. Une pagination (`Pageable` côté Spring Data) limiterait la charge par requête.

3. **Comptage des déclencheurs dans `RiskAssessmentService`** : l'algorithme actuel parcourt l'intégralité du texte concaténé pour chacun des 11 groupes de déclencheurs (`String.contains()` en boucle). Pour le volume de notes de ce projet c'est négligeable, mais à grande échelle, un seul passage sur le texte avec une structure de recherche multi-motifs (type Aho-Corasick) réduirait la complexité de O(n × m) à O(n).

4. **`ddl-auto=update` en développement** : pratique pour itérer vite, mais Hibernate y consacre des requêtes de vérification de schéma à chaque démarrage. En production, des migrations versionnées (Flyway/Liquibase) évitent ce travail redondant à chaque redémarrage de conteneur.

5. **Images Docker déjà optimisées** : le choix d'une image `eclipse-temurin:21-jre` (et non `-jdk`) pour l'étape finale des Dockerfiles limite déjà la taille des images et donc les ressources nécessaires à leur déploiement — bonne pratique déjà en place, à documenter comme telle.

6. **DTOs déjà utilisés** pour ne transmettre que les champs nécessaires entre microservices (plutôt que de sérialiser des entités JPA/MongoDB complètes) — limite la taille des payloads réseau, autre bonne pratique déjà appliquée sans qu'elle ait été pensée sous cet angle au départ.

### Ressources consultées
- Le Goaër, O., *Green code : écrivez du code vert !*, Institut du Numérique Responsable, 2020.
- Documentation officielle Java Mission Control et Eclipse Memory Analyzer (MAT).