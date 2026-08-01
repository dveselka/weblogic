# Cluster and JNDI Test Guide

This guide shows how to start the WebLogic cluster, deploy the app, and run the JNDI client test.

## 1) Start the required services

### 1.1 Start Derby network server (required by this sample)

The EJB uses a Derby XA datasource on localhost:1527.

Example start command:

```sh
/app/weblogic-14.1.2/wlserver/common/derby/bin/startNetworkServer.sh
```

Verify Derby is listening:

```sh
ss -ltnp | grep ':1527'
```

### 1.2 Start AdminServer

```sh
cd /app/weblogic-14.1.2/user_projects/domains/base_domain
./startWebLogic.sh
```

Wait for RUNNING in log:

```sh
grep -n 'Server state changed to RUNNING' servers/AdminServer/logs/AdminServer.log | tail -n 1
```

### 1.3 Start managed servers in the cluster

```sh
cd /app/weblogic-14.1.2/user_projects/domains/base_domain
nohup ./bin/startManagedWebLogic.sh managed-server1 http://127.0.0.1:7002 > managed-server1.out 2>&1 &
nohup ./bin/startManagedWebLogic.sh managed-server2 http://127.0.0.1:7002 > managed-server2.out 2>&1 &
```

Check RUNNING:

```sh
grep -n 'Server state changed to RUNNING' servers/managed-server1/logs/managed-server1.log | tail -n 1
grep -n 'Server state changed to RUNNING' servers/managed-server2/logs/managed-server2.log | tail -n 1
```

## 2) Deploy to cluster

From project folder:

```sh
cd /home/dave/git/weblogic/sample-javaee/14.1.2/dave-basic-project-14.1.2/dave-basic-webapp-ejb-project
mvn clean verify
```

The POM is configured to deploy to target app-cluster via AdminServer t3://127.0.0.1:7002.

## 3) Run JNDI client test

Use class name, not source file path:

```sh
mvn compile exec:java -Dexec.mainClass=dave.client.AccountManagerClient
```

The client is cluster-aware and defaults to:

- t3://127.0.0.1:7003,127.0.0.1:7004

Optional overrides:

```sh
mvn compile exec:java \
  -Dexec.mainClass=dave.client.AccountManagerClient \
  -Dwls.providerUrl=t3://127.0.0.1:7003,127.0.0.1:7004 \
  -Dwls.username=weblogic \
  -Dwls.password=weblogic123
```

## 4) Troubleshooting

- If deploy fails with java.net.ConnectException during activation, make sure Derby is running on localhost:1527.
- If managed server startup asks for credentials when using nohup, ensure boot.properties exists under each server security folder.
- If JNDI lookup fails, verify app deployment is active on app-cluster and both managed servers are RUNNING.
