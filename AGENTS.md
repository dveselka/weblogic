# AGENTS.md

This file provides guidance for AI coding agents working in this repository.

## Repository overview

This is a collection of Oracle WebLogic sample projects, Docker images, and deployment configurations. It is organized as independent modules rather than a single unified build.

## Top-level directory structure

| Directory | Purpose |
|---|---|
| `sample-javaee/` | Java EE sample WAR applications versioned by WebLogic release (14.1.1, 14.1.2) |
| `docker-images/` | Dockerfiles and scripts to build base Oracle JDK and WebLogic server images |
| `docker-domain/` | Dockerfile and shell scripts to create a WebLogic domain persisted to a host volume |
| `docker-compose/` | Docker Compose setup for a single admin server domain |
| `docker-compose-managed-server/` | Docker Compose setup with admin server + managed server |
| `k8s/` | Kubernetes manifests for the WebLogic Kubernetes Operator (Domain CRDs, Ingress) |
| `wdt/` | WebLogic Deploy Tooling (WDT) domain model YAML files |
| `wit/` | WebLogic Image Tool (WIT) auxiliary-image assets (domain model, archive, domain-resource YAML) |
| `weblogic-config/` | Domain configuration notes for specific WebLogic versions |

## Java EE application modules

Each sample module lives under `sample-javaee/<version>/dave-basic-project[-<version>]/` and is an independent Maven project. There is **no parent/aggregator POM** at the repository root.

### Modules in `sample-javaee/14.1.1/dave-basic-project/`

| Module | Packaging | Description |
|---|---|---|
| `dave-basic-webapp-project` | WAR | Minimal JSF web application |
| `dave-basic-webapp-ejb-project` | WAR | Web application with EJB session beans (Account/Bank domain), JPA, CDI interceptors |
| `dave-basic-mdb` | WAR | JSF front-end + JMS Message Driven Bean |
| `dave-basic-webservice-project` | WAR | JAX-WS web service |
| `rs-api` | WAR | JAX-RS REST API |
| `get-oracle-connection` | WAR | EJB + JPA demo using Oracle JDBC datasource |
| `jwsc-ant-webservice` | (Ant) | Web service built with the WebLogic `jwsc` Ant task |

### Modules in `sample-javaee/14.1.2/dave-basic-project-14.1.2/`

| Module | Packaging | Description |
|---|---|---|
| `dave-basic-webapp-ejb-project` | WAR | EJB session beans with remote interface, JMS client utilities, CDI interceptors (targets Java 21) |
| `dave-basic-mdb-project` | WAR | JSF + JMS MDB (similar to the 14.1.1 version, adapted for 14.1.2) |

## Build and deploy

Each Maven module is built and deployed independently:

```bash
# Build only
mvn clean package

# Build + deploy to a running WebLogic server (requires server running and correct pom.xml properties)
mvn clean verify
```

Deployment is done via the `weblogic-maven-plugin` configured in each `pom.xml`. The plugin connects over T3 to the Admin Server. Relevant properties (set per-module):

- `oracleMiddlewareHome` – local WebLogic install path
- `oracleServerUrl` – T3 URL, e.g. `t3://127.0.0.1:7001`
- `oracleUsername` / `oraclePassword` – WebLogic admin credentials

There are no automated tests in this repository; validation is manual deployment to a running server.

## Docker workflows

1. **Build base images** – `docker-images/docker-oracle-java-8` / `docker-oracle-java-11` produce `oracle/jdk:8` / `oracle/jdk:11`.
2. **Build WebLogic install image** – `docker-images/docker-weblogic-14.1.1.0/dockerfiles/buildDockerImage.sh` builds `oracle/weblogic:<version>-<dist>-<jdk>`.
3. **Create domain image** – `docker-domain/Dockerfile` extends the install image and creates a domain on a volume. Run with `run_admin_server.sh` / `run_managed_server.sh`.
4. **Docker Compose** – `docker-compose/` or `docker-compose-managed-server/` orchestrate admin + managed servers together.

Domain configuration is driven by environment variables (see each `Dockerfile` for the full list, e.g. `DOMAIN_NAME`, `CLUSTER_NAME`, `MANAGED_SERVER_PORT`).

## Kubernetes (WebLogic Kubernetes Operator)

Manifests live in `k8s/quickstart/`:

- `model.yaml` – WDT domain model (topology + app deployment)
- `model.properties` – variable substitution for the model
- `domain-resource.yaml` – `Domain` and `Cluster` CRDs consumed by the operator
- `ingress-route.yaml` – Traefik IngressRoute for the console and app

The domain uses **Model in Image** (`domainHomeSourceType: FromModel`) with a WDT auxiliary image.

## WDT / WIT assets

- `wdt/` – discovered WDT YAML models exported from running domains. These describe topology, clusters, and app deployments.
- `wit/dave-domain-aux-image/` – ready-made auxiliary image bundle (model YAML + application WAR archive) used with the WebLogic Image Tool to produce an aux image for Model-in-Image deployments.

## Conventions

- Java source targets **Java 11** for 14.1.1 modules and **Java 21** for 14.1.2 modules.
- All WAR artifacts are built with `failOnMissingWebXml=false`.
- Shell scripts in `container-scripts/` use WLST Python (`createWLSDomain.sh` delegates to a `.py` script) to create domains programmatically.
- Credentials in property files and `pom.xml` (`weblogic123`) are **samples only** and must be changed for any real environment.
- There is no CI pipeline that runs tests; the `Jenkinsfile` only compiles (`mvn clean package`).

## What agents should and should not do

- **Do** make changes inside a specific module (`sample-javaee/`, `docker-*`, `k8s/`, etc.) without touching unrelated modules.
- **Do** respect the per-module Maven POM structure; there is no root aggregator to add modules to.
- **Do not** add secrets or real credentials to any file.
- **Do not** introduce new top-level build tooling unless the task explicitly requires it.
- **Do not** modify Docker base image versions unless the task is specifically about upgrading images.
