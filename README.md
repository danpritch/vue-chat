# Vue Chat

## Description

**Vue Chat** is a multi-user, real-time messaging application, similar to Slack and Discord.

## Components

The demo consists of the following components:

- **PostgreSQL**: Acts as the primary data store and "single source of truth."
- **Debezium**: Captures data changes from PostgreSQL and streams them into Apache Kafka. It serves as a reliable alternative to implementing the "transactional outbox" pattern manually.
- **KSQLDB**: Processes and combines data streams from Kafka topics.
- **Spring Boot**: Provides a Reactive API layer integrating with PostgreSQL and Kafka.
- **VueJS**: A Single Page Application (SPA) for user management, conversation handling, and messaging.
- **Docker / Docker Compose**: Facilitates building, running, and managing the entire system locally.

## Demo

![Vue Chat Demo](docs/vue-chat-demo.gif)

## Design

This system is specifically designed to demonstrate extensive knowledge across several domains:

- Database schema design and SQL with PostgreSQL.
- Data engineering using Debezium, Kafka, and KSQLDB.
- Event-driven architectures.
- API development with Spring Boot.
- Front-end development using JavaScript frameworks like VueJS.
- Infrastructure and network management using Docker and Docker Compose.

Additionally, the design emphasizes data integrity by specifically addressing the "dual write problem," a common challenge in event-driven distributed systems.

The **dual write problem** occurs when a single component attempts to write data to multiple data stores (e.g., PostgreSQL and Kafka) separately. If the system fails or encounters an interruption between these writes, it can result in inconsistent states and compromised data integrity.

To address this issue, solutions such as the "transactional outbox" pattern or tools like Debezium can be employed. Debezium captures database transactions reliably from the primary datastore (PostgreSQL) and propagates these changes to other systems via Kafka, ensuring consistency across the distributed architecture.

![Vue Chat](docs/vue-chat.png)

In the current demo, PostgreSQL is the sole primary datastore, making the use of Debezium appear potentially unnecessary or "over-engineered." However, this architecture facilitates seamless scalability and the addition of other data stores in the future. For example, messaging applications often require efficient full-text search capabilities. Using PostgreSQL for intensive full-text searches in high-throughput scenarios can introduce significant latency, instability, and degrade user experience. Therefore, it would be advantageous to integrate specialized technologies like ElasticSearch. The existing architecture, leveraging Debezium and Kafka, can easily support such extensions without compromising data integrity.

![Vue Chat Extended](docs/vue-chat-extended.png)

## Getting Started

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed on your machine.
- [Make](https://www.gnu.org/software/make/) installed for running build commands.

### Installation

1. **Build Docker Images**

   Build all required images by running:
   ```bash
   make build
   ```

2. **Start the Environment**

   Launch the Docker Compose environment with:
   ```bash
   make up
   ```

3. **Interact with the Demo**

   Open your browser and navigate to [http://localhost:5173/](http://localhost:5173/) to start using Vue Chat.


