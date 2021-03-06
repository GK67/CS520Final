import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.*;

public class MockProgram extends UI{

    public static void main(String[] args) throws Exception {
        // if (args.length < 2) {
        //     System.out.println("Usage: java VideoStore CUSTOMER_ID CUSTOMER_PASSWORD");
        //     System.exit(1);
        // }
        UI ui = new UI();
        ui.setVisible(true);
        Path path = new Path();
        ArrayList building= path.giveAllBuildings();
        String[] buildings = new String[building.size()];
        buildings= ((ArrayList<String>)building).toArray(buildings);
        

        ui.setStartDropDown(buildings);
        ui.setEndDropDown(buildings);
        //create mock data
       /* ArrayList output=ConvertData.nodesToArray("./northeast");/*
        int i=10;
        String id="42949710";
        Double la=42.3787077;
        Double lo=42.3787077;
        Double el=42.3787077;
        for(;i<40;i++){

            ArrayList node=new ArrayList();
            node.add(id+Integer.toString(i));
            node.add(la);
            la+=i;
            node.add(lo);
            lo-=i;
            node.add(el);
            el-=i;
            output.add(node);
	    }*/
        
        ArrayList outputWayList= ConvertData.waysToArray("./northeast");
        /*
        int j=0;
        String idW="429497109";
        String sName="abc";
        String bName="bbb";

        for(;j<10;j++){
            ArrayList outputWay=new ArrayList();
            ArrayList node=new ArrayList();
            for(int n=0;n<=j;n++){
                node.add(((ArrayList)output.get(n)).get(0));
            }

            outputWay.add(idW+Integer.toString(j));
            if(j==3 ||j==5)
                outputWay.add(null);
            else
                outputWay.add(sName+Integer.toString(j));

            outputWay.add(node);

            if(j==5 || j==7)
                outputWay.add(null);
            else
                outputWay.add(bName+Integer.toString(j));
            outputWayList.add(outputWay);
	    }*/

        //create mock data end

        /* prepare the database connection stuff */
        Query q = new Query();
        q.openConnection();
        q.prepareStatements();

        //only insert the data in the begining, each time running, it will delete the original tables
        //and create new one
       // q.transaction_insert_data(output, outputWayList);  
        System.out.println("Finish insert data");

        //search a node, before get value need do this search first
        q.transaction_search_node("66591571");

        //get the element of the node
        System.out.println("latitude\t"+q.getLatitude());
        System.out.println("latitude\t"+q.getLongitude());
        System.out.println("latitude\t"+q.getElevation());

        //search a node, before get value need do this search first
        q.transaction_search_node("66645458");

        //get the element of the node
        System.out.println("latitude\t"+q.getLatitude());
        System.out.println("latitude\t"+q.getLongitude());
        System.out.println("latitude\t"+q.getElevation());



        //search ways by a node id
        ArrayList wayList = q.transaction_search_way("1603680132");

        for(int l=0;l<wayList.size();l++){
            System.out.println("waylist:\t"+wayList.get(l));
        }

        //get all the nodes
        ArrayList nodeList = q.transaction_search_all_node();

        for(int l=0;l<nodeList.size();l++){
            System.out.println("all nodes list:\t"+nodeList.get(l));
        }

        // search nodes by a way id
        ArrayList nodeListOfAWay = q.transaction_search_nodes_of_way("4294971098");
        //get all the nodes of a way
        for(int l=0;l<nodeListOfAWay.size();l++){
            System.out.println("all nodes list of a way:\t"+nodeListOfAWay.get(l));
        }

        ArrayList buildingList = q.transaction_search_all_building();
        //get all the nodes of a way
        for(int l=0;l<buildingList.size();l++){
            System.out.println("all building name list:\t"+buildingList.get(l));
        }

        System.out.println("Start here......................................................");
        Path p = new Path();
        //System.out.println(p.giveDirections("Knowlton Hall", "Johnson Hall", 2));
        /*
        ArrayList neighbors = p.nearestNeighbors("66591571");
        for (int i = 0; i < neighbors.size(); i++){
            System.out.println(neighbors.get(i));
        }*/
        //String closestNode = p.getClosestPathNode();
        System.out.println("finish creating a path Object......................");

        // ArrayList path = p.getPath(42.3902621,-72.5302069,  42.3908489, -72.5287564);
        // ArrayList path = p.getPath(42.3904369,-72.5285867,  42.3897598, -72.5308958);
        //ArrayList path = p.getPath(42.3905743,-72.5303372);
        // System.out.println(path);

        System.out.println("streetName"+q.transaction_search_streetName("9058447"));

        System.out.println("stname"+q.transaction_search_streetName("9058384"));
 
        System.out.println("buildName"+q.transaction_search_buildingName_by_way("9058447"));

        System.out.println("buildName"+q.transaction_search_buildingName_by_way("9058384"));
   
        System.out.println("streeId"+q.transaction_search_way_by_buildingName("Governors Drive"));
        /*for (int i = 0; i < path.size(); i ++){
            System.out.println(path.get(i));
        }*/
        //System.out.println(closestNode);
        q.closeConnection();

    }

}

