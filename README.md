# Vue Chat

## Description

**Vue Chat** est une application de messagerie multi-utilisateurs en temps réel, similaire à Slack et Discord.

## Composants

La démonstration se compose des composants suivants :

- **PostgreSQL** : Sert de stockage de données principal et de « source unique de vérité ».
- **Debezium** : Capture les changements de données de PostgreSQL et les diffuse dans Apache Kafka. Il constitue une alternative fiable à l'implémentation manuelle du modèle « transactional outbox ».
- **KSQLDB** : Traite et combine les flux de données provenant des sujets Kafka.
- **Spring Boot** : Fournit une couche d'API réactive intégrée avec PostgreSQL et Kafka.
- **VueJS** : Une application monopage (SPA) pour la gestion des utilisateurs, la gestion des conversations et la messagerie.
- **Docker / Docker Compose** : Facilite la construction, l'exécution et la gestion complète du système en local.

## Démonstration

![Démo Vue Chat](docs/vue-chat-demo.gif)

## Conception

Ce système est spécialement conçu pour démontrer une connaissance approfondie de plusieurs domaines :

- Conception de schémas de bases de données et SQL avec PostgreSQL.
- Ingénierie des données avec Debezium, Kafka et KSQLDB.
- Architectures orientées événements.
- Développement d'API avec Spring Boot.
- Développement front-end avec des frameworks JavaScript comme VueJS.
- Gestion de l'infrastructure et du réseau avec Docker et Docker Compose.

De plus, la conception met l'accent sur l'intégrité des données en abordant spécifiquement le « problème d'écriture double », un défi courant dans les systèmes distribués orientés événements.

Le **problème d'écriture double** survient lorsqu'un composant tente d'écrire des données séparément dans plusieurs bases de données (par exemple, PostgreSQL et Kafka). Si le système tombe en panne ou rencontre une interruption entre ces écritures, cela peut entraîner des états incohérents et compromettre l'intégrité des données.

Pour résoudre ce problème, des solutions telles que le modèle « transactional outbox » ou des outils comme Debezium peuvent être employés. Debezium capture de manière fiable les transactions de la base de données principale (PostgreSQL) et propage ces changements vers d'autres systèmes via Kafka, assurant ainsi une cohérence dans l'architecture distribuée.

![Vue Chat](docs/vue-chat.png)

Dans la démonstration actuelle, PostgreSQL est la seule base de données principale, ce qui peut rendre l'utilisation de Debezium potentiellement inutile ou « sur-conçue ». Cependant, cette architecture facilite une évolutivité transparente et l'ajout d'autres bases de données à l'avenir. Par exemple, les applications de messagerie nécessitent souvent des capacités efficaces de recherche en texte intégral. Utiliser PostgreSQL pour des recherches intensives en texte intégral dans des scénarios à fort débit peut introduire une latence importante, de l'instabilité et dégrader l'expérience utilisateur. Il serait donc avantageux d'intégrer des technologies spécialisées telles qu'ElasticSearch. L'architecture existante, utilisant Debezium et Kafka, peut facilement prendre en charge de telles extensions sans compromettre l'intégrité des données.

![Vue Chat étendu](docs/vue-chat-extended.png)

## Démarrage

### Prérequis

- [Docker](https://docs.docker.com/get-docker/) et [Docker Compose](https://docs.docker.com/compose/install/) installés sur votre machine.
- [Make](https://www.gnu.org/software/make/) installé pour exécuter les commandes de construction.

### Installation

1. **Construire les images Docker**

   Construisez toutes les images requises en exécutant :
   ```bash
   make build
   ```

2. **Lancer l'environnement**

   Lancez l'environnement Docker Compose avec :
   ```bash
   make up
   ```

3. **Interagir avec la démonstration**

   Ouvrez votre navigateur et rendez-vous sur [http://localhost:5173/](http://localhost:5173/) pour commencer à utiliser Vue Chat.


