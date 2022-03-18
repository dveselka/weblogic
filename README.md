# Weblogic Java EE apps created via Maven archetype

see https://docs.oracle.com/middleware/12211/lcm/MAVEN/weblogic_maven.htm#MAVEN8767

JDBC driver https://blogs.oracle.com/developers/your-own-way-oracle-jdbc-drivers-19700-on-maven-central

Sample mvn command

```
mvn archetype:generate -DarchetypeGroupId=com.oracle.weblogic.archetype -DarchetypeArtifactId=basic-mdb -DarchetypeVersion=12.2.1-0-0 -DgroupId=org.dave -DartifactId=dave-basic-mdb -Dversion=1.0-SNAPSHOT
```

See the blog for details

### Java EE 8
* https://github.com/dveselka/java-ee-8

* Maven https://danielveselka.blogspot.com/2019/10/deploy-java-ee7-app-into-weblogic.html

### Docker and k8s

* Weblogic Docker domain https://danielveselka.blogspot.com/2020/10/create-weblogic-12214-docker-image-with.html

* Docker Compose https://danielveselka.blogspot.com/2020/10/create-weblogic-docker-domain-using.html

### Logging

*  ElasticSearch https://danielveselka.blogspot.com/2020/09/access-weblogic-log-information-through.html

