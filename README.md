# accessing hive from Neo4j
Querying HIVE and Impala Data from Cloudera

# Instructions

1. Build it:

        mvn clean package

2. Copy target/hive-1.0.jar to the plugins/ directory of your Neo4j server.

3. Configure Neo4j by adding a line to conf/neo4j-server.properties:

        org.neo4j.server.thirdparty_jaxrs_classes=com.hive=/v1
       
4. Start Neo4j server.

5. Check that it is installed correctly over HTTP:

        :GET /v1/service/helloworld
        
6. Query the database:
        
        :POST /v1/service/equipment
