package com.hive;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.tooling.GlobalGraphOperations;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

@Path("/service")
public class Service {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String ACTION = "action";
    public static final String DATA = "data";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-M-d-H-m");

    public Service(@Context GraphDatabaseService graphdb) {
    }

    @GET
    @Path("/helloworld")
    public String helloWorld() {
        return "Hello World!";
    }

    @GET
    @Path("/warmup")
    public String warmUp(@Context GraphDatabaseService db) {
        try ( Transaction tx = db.beginTx() )
        {
            ResourceIterator<Node> iter = db.findNodes(Labels.Equipment);
            while(iter.hasNext()) {
                Node userNode = iter.next();
                userNode.getPropertyKeys();
            }

 
            for ( Relationship relationship : GlobalGraphOperations.at(db).getAllRelationships()){
                relationship.getPropertyKeys();
                relationship.getNodes();
            }
        }
        return "Warmed up and ready to go!";
    }
    


     @POST
    @Path("/equipment")
    public Response createEquipment(String body, @Context GraphDatabaseService db) throws IOException, InterruptedException{
 //       HashMap input = Validators.getValidPersonInput(body);
        HashMap<String, Object> data = new HashMap<>();
    	Connection con = null;
    	Node equipmentNode = null;
    	Label equipmentLabel = DynamicLabel.label("Equipment");
        
    	final String SQL_STATEMENT = "SELECT sample_07.code,sample_07.description from sample_07";
    	
    	// set the impalad host
    	final String IMPALAD_HOST = "192.168.1.183";
    	final String HIVE_HOST = "192.168.1.183";
    	
    	// port 21050 is the default impalad JDBC port 
    	final String IMPALAD_JDBC_PORT = "21050";
    	final String HIVE_JDBC_PORT ="10000";

    	final String CONNECTION_URL = "jdbc:hive2://" + IMPALAD_HOST + ':' + IMPALAD_JDBC_PORT + "/;auth=noSasl";
    	final String HIVE_CONNECTION_URL = "jdbc:hive2://" + HIVE_HOST + ':' + HIVE_JDBC_PORT + "/;AuthMech=3;UID=cloudera;PWD=cloudera";

    	final String JDBC_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
    	final String HIVE_DRIVER_NAME = "com.cloudera.hive.jdbc41.HS1Driver";
        
        URL url = new URL("http://localhost:7474/v1/service/equipment");
    
        
        boolean exists = false;
        try (Transaction tx = db.beginTx()) {

    		System.out.println("trying Hive");
    		try {

    			try {
					Class.forName(JDBC_DRIVER_NAME);
				
    			con = DriverManager.getConnection(HIVE_CONNECTION_URL);

    			Statement stmt = con.createStatement();

    			ResultSet rs = stmt.executeQuery(SQL_STATEMENT);

    			System.out.println("\n== Begin Query Results ======================");
    			// print the results to the console
    			while (rs.next()) {
        			data.put("Code", rs.getString(1));
        			data.put("Description", rs.getString(2));
        			
        			equipmentNode = db.createNode(equipmentLabel);
        			equipmentNode.setProperty("Code", rs.getString(1));                                                        			
        			equipmentNode.setProperty("Description", rs.getString(2));     
    			}

    		    System.out.println("finished");
    			} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    			} catch (SQLException e) {
    				e.printStackTrace();
    			} catch (Exception e) {
    			e.printStackTrace();
    			} finally {
    				try {
    					con.close();
    				} catch (Exception e) {
    				// swallow
				}
    		}
        	
        	
        	

//        		}
//             } else {
//                exists = true;
//            }

            tx.success();
        }

        if (exists) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.CREATED).build();
        }
    }

    
}