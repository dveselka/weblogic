See blog post https://danielveselka.blogspot.com/2020/10/create-weblogic-docker-domain-using.html

This creates Weblogic domain using Docker compose

Oracle images oracle/weblogic:12.2.1.4-generic have to be created first - see HOWTO here  https://github.com/dveselka/weblogic/tree/master/docker-domain/

```
[dave@dave dockerfiles]$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
oracle/weblogic     12.2.1.4-generic    eaf6a4a3dd74        7 seconds ago       1.27GB
<none>              <none>              9c232329604a        22 seconds ago      3.01GB
oracle/serverjre    8                   454b13098b07        5 minutes ago       373MB
oraclelinux         7-slim              977320706064        4 days ago          132MB
```


Docker container 
```
[dave@dave docker-compose]$ docker ps
CONTAINER ID        IMAGE                              COMMAND                  CREATED             STATUS              PORTS                                            NAMES
7734bda43ce6        oracle/weblogic:12.2.1.4-generic   "/u01/oracle/createA…"   47 seconds ago      Up 46 seconds       0.0.0.0:7001->7001/tcp, 0.0.0.0:9002->9002/tcp   adminserver
```
