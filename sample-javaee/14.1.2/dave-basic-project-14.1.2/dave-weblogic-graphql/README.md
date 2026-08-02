# dave-weblogic-graphql

A GraphQL API deployed as a WAR on Oracle WebLogic 14.1.2. It queries the same Derby database and `MAVEN_ARCHETYPE_SAMPLE_ACCOUNT` table used by `dave-basic-webapp-ejb-project`.

## Architecture

```
HTTP client
    │
    ▼
JAX-RS  GET  /api/graphql  ──► GraphiQL UI (browser)
        POST /api/graphql  ──► GraphQLResource (JAX-RS)
                                    │
                                    ▼
                            GraphQLSchemaConfig (CDI @ApplicationScoped)
                            graphql-java 21.5 runtime + schema.graphqls
                                    │
                                    ▼
                            AccountRepository (@Stateless EJB)
                                    │
                              JPA / EclipseLink
                                    │
                              Derby 1527 → examples DB
                              MAVEN_ARCHETYPE_SAMPLE_ACCOUNT
```

### Key classes

| Class | Package | Role |
|---|---|---|
| `RestApplication` | `dave.graphql.app` | Registers JAX-RS at `/api` |
| `GraphQLResource` | `dave.graphql.web` | POST handler + GraphiQL GET |
| `GraphQLSchemaConfig` | `dave.graphql.config` | Builds `GraphQL` instance at startup |
| `AccountRepository` | `dave.graphql.repository` | EJB / JPA queries against Derby |
| `Account` | `dave.graphql.entity` | JPA entity mapping `MAVEN_ARCHETYPE_SAMPLE_ACCOUNT` |

### GraphQL schema (`src/main/resources/graphql/schema.graphqls`)

```graphql
type Query {
    account(name: String!): Account
    accounts: [Account]
}

type Account {
    name: String
    amount: Float
}
```

## Prerequisites

- Oracle WebLogic 14.1.2 running (admin server or cluster member)
- Apache Derby network server listening on `localhost:1527`, database `examples` (user/password `examples`)
- The Derby server and `examples` database are the same ones used by `dave-basic-webapp-ejb-project`
- WebLogic Maven plugin and `wls-common` parent POM available in your local Maven repository

> **Note:** `dave-basic-webapp-ejb-project` uses `drop-and-create-tables` so it will recreate the table on each deploy. Deploy that app first, then deploy this one.

## Build

```bash
cd dave-weblogic-graphql
mvn clean package
```

Produces `target/weblogicGraphQL.war`.

## Deploy

```bash
# build + deploy in one step (requires WebLogic admin server reachable at t3://127.0.0.1:7002)
mvn clean verify
```

Deployment properties (override on the command line with `-D<prop>=<value>`):

| Property | Default |
|---|---|
| `oracleMiddlewareHome` | `/app/weblogic-14.1.2` |
| `oracleServerUrl` | `t3://127.0.0.1:7002` |
| `oracleUsername` | `weblogic` |
| `oraclePassword` | `weblogic123` |
| `oracleServerName` | `app-cluster` |

## Usage

### GraphiQL browser UI

Open in a browser:

```
http://<host>:<port>/weblogicGraphQL/api/graphql
```

The embedded GraphiQL interface lets you write and run queries interactively.

### Query via curl

**List all accounts:**

```bash
curl -s -X POST http://localhost:7003/weblogicGraphQL/api/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ accounts { name amount } }"}'
```

**Find a single account by name:**

```bash
curl -s -X POST http://localhost:7003/weblogicGraphQL/api/graphql \
  -H 'Content-Type: application/json' \
  -d '{"query":"{ account(name: \"alice\") { name amount } }"}'
```

**Example response:**

```json
{
  "data": {
    "accounts": [
      { "name": "alice", "amount": 250.0 },
      { "name": "bob",   "amount": 100.0 }
    ]
  }
}
```

### Using variables

```bash
curl -s -X POST http://localhost:7003/weblogicGraphQL/api/graphql \
  -H 'Content-Type: application/json' \
  -d '{
    "query": "query GetAccount($name: String!) { account(name: $name) { name amount } }",
    "variables": { "name": "alice" }
  }'
```

## Datasource

The datasource is declared in `AccountRepository` via `@DataSourceDefinition` (module-scoped JNDI `java:module/env/graphqlDataSource`). It connects to the same Derby instance as `dave-basic-webapp-ejb-project` but registers under a different JNDI name so both applications can run simultaneously.

Derby connection parameters:

| Parameter | Value |
|---|---|
| Host | `localhost` |
| Port | `1527` |
| Database | `examples` |
| User | `examples` |
| Password | `examples` |

## Classloader configuration

`weblogic.xml` uses `prefer-application-packages` to ensure the bundled `graphql-java`, Jackson, ANTLR 4, and Reactive Streams jars take precedence over any conflicting versions inside WebLogic's system classloader.
