import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.io.FileInputStream;

/**
 * Runs queries against a back-end database
 */
public class Query {
    private static Properties configProps = new Properties();

    private static String osmdbUrl;

    private static String postgreSQLDriver;
    private static String postgreSQLUser;
    private static String postgreSQLPassword;

    private static ArrayList oneNode = null;

    // DB Connection
    private Connection _osmdb;
    // Canned queries

    private String _create_node_table_sql = "CREATE TABLE node ("
  						+"id CHAR(70) NOT NULL,"
  						+"latitude FLOAT NOT NULL,"
  						+"longitude FLOAT NOT NULL,"
  						+"elevation FLOAT NOT NULL,"
  						+"PRIMARY KEY (id))";
    private PreparedStatement _create_node_table_statement;

    private String _create_way_table_sql = "CREATE TABLE way ("
  						+"id CHAR(70) NOT NULL,"
  						+"streetName CHAR(300),"
  						+"nid CHAR(70) NOT NULL,"
  						+"buildingName CHAR(100),"
  						+"FOREIGN KEY(nid) REFERENCES node)";
    private PreparedStatement _create_way_table_statement;

    private String _insert_node_sql = "INSERT INTO node VALUES (?, ?, ?, ?)";
    private PreparedStatement _insert_node_statement;

    private String _insert_way_sql = "INSERT INTO way VALUES (?, ?, ?, ?)";
    private PreparedStatement _insert_way_statement;
    

    private String _delete_table_node_sql = "DROP TABLE IF EXISTS node";
    private PreparedStatement _delete_table_node_statement;

    private String _delete_table_way_sql = "DROP TABLE IF EXISTS way";
    private PreparedStatement _delete_table_way_statement;
    

    private String _search_node_by_nid_sql = "SELECT * FROM node WHERE id = ?";
    private PreparedStatement _search_node_by_nid_statement;

    private String _search_way_by_nid_sql = "SELECT distinct id FROM way WHERE nid = ?";
    private PreparedStatement _search_way_by_nid_statement;

    private String _search_all_nodeId_sql = "SELECT distinct nid FROM way";
    private PreparedStatement _search_all_nodeId_statement;

    private String _search_node_by_wid_sql = "SELECT nid FROM way WHERE id = ?";
    private PreparedStatement _search_node_by_wid_statement;

    private String _search_building_sql = "SELECT distinct buildingName FROM way WHERE buildingName <>''";
    private PreparedStatement _search_building_statement;

    private String _search_streetName_sql = "SELECT distinct streetName FROM way WHERE id = ?";
    private PreparedStatement _search_streetName_statement;

    private String _search_buildingName_of_way_sql = "SELECT distinct buildingName FROM way WHERE id = ?";
    private PreparedStatement _search_buildingName_of_way_statement;

    private String _search_way_by_buildingName_sql = "SELECT distinct id FROM way WHERE buildingName = ?";
    private PreparedStatement _search_way_by_buildingName_statement;




    private String _begin_transaction_read_write_sql = "BEGIN TRANSACTION READ WRITE";
    private PreparedStatement _begin_transaction_read_write_statement;

    private String _begin_transaction_read_only_sql = "BEGIN TRANSACTION READ ONLY";
    private PreparedStatement _begin_transaction_read_only_statement;

    private String _commit_transaction_sql = "COMMIT TRANSACTION";
    private PreparedStatement _commit_transaction_statement;
     

    public Query() {
    }

    /**********************************************************/
    /* Connections to postgres databases */

    public void openConnection() throws Exception {
        configProps.load(new FileInputStream("dbconn.config"));
        
        
        osmdbUrl        = configProps.getProperty("osmdbUrl");
        postgreSQLDriver   = configProps.getProperty("postgreSQLDriver");
        postgreSQLUser     = configProps.getProperty("postgreSQLUser");
        postgreSQLPassword = configProps.getProperty("postgreSQLPassword");


        /* load jdbc drivers */
        Class.forName(postgreSQLDriver).newInstance();

        /* open connections to database: osmdb database */
        _osmdb = DriverManager.getConnection(osmdbUrl, // database
                postgreSQLUser, // user
                postgreSQLPassword); // password
    }

    public void closeConnection() throws Exception {
        _osmdb.close();
    }


    public void prepareStatements() throws Exception {


    	  _create_node_table_statement= _osmdb.prepareStatement(_create_node_table_sql);
        _create_way_table_statement = _osmdb.prepareStatement(_create_way_table_sql);
        _insert_node_statement = _osmdb.prepareStatement(_insert_node_sql);
        _insert_way_statement = _osmdb.prepareStatement(_insert_way_sql);



        _begin_transaction_read_write_statement = _osmdb.prepareStatement(_begin_transaction_read_write_sql);

        _begin_transaction_read_only_statement = _osmdb.prepareStatement(_begin_transaction_read_only_sql);

        _commit_transaction_statement = _osmdb.prepareStatement(_commit_transaction_sql);



        _delete_table_node_statement = _osmdb.prepareStatement(_delete_table_node_sql);
        _delete_table_way_statement = _osmdb.prepareStatement(_delete_table_way_sql);
   
        _search_node_by_nid_statement = _osmdb.prepareStatement(_search_node_by_nid_sql);

		    _search_way_by_nid_statement = _osmdb.prepareStatement(_search_way_by_nid_sql);

		    _search_all_nodeId_statement = _osmdb.prepareStatement(_search_all_nodeId_sql);

		    _search_node_by_wid_statement= _osmdb.prepareStatement(_search_node_by_wid_sql);

		    _search_building_statement= _osmdb.prepareStatement(_search_building_sql);

        _search_streetName_statement= _osmdb.prepareStatement(_search_streetName_sql);

        _search_buildingName_of_way_statement= _osmdb.prepareStatement(_search_buildingName_of_way_sql);

        _search_way_by_buildingName_statement= _osmdb.prepareStatement(_search_way_by_buildingName_sql);
    }


    public void transaction_insert_data(ArrayList outputNode, ArrayList outputWay) throws Exception {
        
       _begin_transaction_read_write_statement.executeUpdate();
       
       _delete_table_way_statement.executeUpdate();
       _delete_table_node_statement.executeUpdate();

       _create_node_table_statement.executeUpdate();
       _create_way_table_statement.executeUpdate();

       for(int i=0;i<outputNode.size();i++){
       		_insert_node_statement.clearParameters();
       		_insert_node_statement.setString(1,((ArrayList)outputNode.get(i)).get(0).toString());
       		_insert_node_statement.setDouble(2,(Double)((ArrayList)outputNode.get(i)).get(1));
       		_insert_node_statement.setDouble(3,(Double)((ArrayList)outputNode.get(i)).get(2));
       		_insert_node_statement.setDouble(4,(Double)((ArrayList)outputNode.get(i)).get(3));
       		_insert_node_statement.executeUpdate();		
       }

       for(int i=0;i<outputWay.size();i++){

       		ArrayList nodeId = (ArrayList)(((ArrayList)outputWay.get(i)).get(2));
       		String idW=((ArrayList)outputWay.get(i)).get(0).toString();

       		Object sName=((ArrayList)outputWay.get(i)).get(1);
       		Object bName=((ArrayList)outputWay.get(i)).get(3);

       		for(int j=0;j<nodeId.size();j++){
       			_insert_way_statement.clearParameters();
       			_insert_way_statement.setString(1,idW);
       			if(sName==null)
       				_insert_way_statement.setString(2,"");
       			else
       				_insert_way_statement.setString(2,sName.toString());

       			_insert_way_statement.setString(3,(nodeId.get(j)).toString());

       			if(bName==null)
       				_insert_way_statement.setString(4,"");
       			else
       				_insert_way_statement.setString(4,bName.toString());
       			_insert_way_statement.executeUpdate();
       		}
       				
       }
       _commit_transaction_statement.executeUpdate();
        
    }


    public Double getLatitude() throws Exception {
        
        if(oneNode==null)
        	return null;
        else{
        	return (Double)(oneNode.get(0));
        }
    }

    public Double getLongitude() throws Exception {
        
        if(oneNode==null)
        	return null;
        else{
        	return (Double)(oneNode.get(1));
        }
    }

    public Double getElevation() throws Exception {
        
        if(oneNode==null)
        	return null;
        else{
        	return (Double)(oneNode.get(2));
        }
    }

    public void transaction_search_node(String nodeId) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_node_by_nid_statement.clearParameters();
        _search_node_by_nid_statement.setString(1,nodeId);


        ResultSet nodeSet = _search_node_by_nid_statement.executeQuery();
        oneNode = new ArrayList();
        if(nodeSet.next()){
        	Double la= nodeSet.getDouble(2);
        	Double lo= nodeSet.getDouble(3);
        	Double el= nodeSet.getDouble(4);

        	oneNode.add(la);
        	oneNode.add(lo);
        	oneNode.add(el);
        }
        else{
        	oneNode= null;
        }

        _commit_transaction_statement.executeUpdate();
    }

    public ArrayList transaction_search_way(String nodeId) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_way_by_nid_statement.clearParameters();
        _search_way_by_nid_statement.setString(1,nodeId);


        ResultSet waySet = _search_way_by_nid_statement.executeQuery();
        ArrayList wayList = new ArrayList();
        
        while(waySet.next()){
        	wayList.add(waySet.getString("id"));
        }

        _commit_transaction_statement.executeUpdate();

        return wayList;
    }

    public ArrayList transaction_search_all_node() throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();

        ResultSet nodeSet = _search_all_nodeId_statement.executeQuery();
        ArrayList nodeList = new ArrayList();
        
        while(nodeSet.next()){
        	nodeList.add(nodeSet.getString("nid"));
        }

        _commit_transaction_statement.executeUpdate();

        return nodeList;
    }

    public ArrayList transaction_search_nodes_of_way(String wayId) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_node_by_wid_statement.clearParameters();
        _search_node_by_wid_statement.setString(1,wayId);


        ResultSet nodeSet = _search_node_by_wid_statement.executeQuery();
        ArrayList nodeList = new ArrayList();
        
        while(nodeSet.next()){
        	nodeList.add(nodeSet.getString("nid"));
        }

        _commit_transaction_statement.executeUpdate();

        return nodeList;
    }

    public ArrayList transaction_search_all_building() throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        

        ResultSet buildingSet = _search_building_statement.executeQuery();
        ArrayList buildingList = new ArrayList();
        
        while(buildingSet.next()){
        	buildingList.add(buildingSet.getString("buildingName"));
        }

        _commit_transaction_statement.executeUpdate();

        return buildingList;
    }

    public String transaction_search_streetName(String wayId) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_streetName_statement.clearParameters();
        _search_streetName_statement.setString(1,wayId);

        ResultSet streetSet = _search_streetName_statement.executeQuery();
        String streetName=null;
        if(streetSet.next()){
          streetName =streetSet.getString("streetName");
        }

        _commit_transaction_statement.executeUpdate();

        if(streetName==""||streetName==null)
          return null;

        return streetName;
    }

    public String transaction_search_buildingName_by_way(String wayId) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_buildingName_of_way_statement.clearParameters();
        _search_buildingName_of_way_statement.setString(1,wayId);

        ResultSet buildingSet = _search_buildingName_of_way_statement.executeQuery();
        String buildingName=null;
        if(buildingSet.next()){
          buildingName =buildingSet.getString("buildingName");
        }

        _commit_transaction_statement.executeUpdate();

        if(buildingName==""||buildingName==null)
          return null;

        return buildingName;
    }

    public String transaction_search_way_by_buildingName(String buildingName) throws Exception {
        
        _begin_transaction_read_only_statement.executeUpdate();
        
        _search_way_by_buildingName_statement.clearParameters();
        _search_way_by_buildingName_statement.setString(1,buildingName);

        ResultSet waySet = _search_way_by_buildingName_statement.executeQuery();
        String wayId=null;
        if(waySet.next()){
          wayId =waySet.getString("id");
        }
        
        _commit_transaction_statement.executeUpdate();

        return wayId;
    }
  
}
