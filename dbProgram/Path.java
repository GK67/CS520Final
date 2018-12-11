import org.json.JSONObject;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;
import java.io.*;

public class Path{
    Query q;
    ArrayList hasWalked;

    public Path(){
	try{
	    q = new Query();
	    q.openConnection();
	    q.prepareStatements();
	    hasWalked = new ArrayList();
	}
	catch(Exception ex){
	    System.out.println("error");
	}
    }

    public void close(){
	try{
	    q.closeConnection();

	}
	catch(Exception ex){
	    System.out.println("error");
	}
    }
    
    public double distance(double xlat, double xlon, double ylat, double ylon){
	return Math.sqrt(Math.pow(xlat-ylat, 2) + Math.pow(xlon-ylon, 2));
    }

    public ArrayList getClosestPathNode(double lat, double lon){
	try{
	    ArrayList nodesInWays = q.transaction_search_all_node();
	    String closest = (String)(nodesInWays.get(0));
	    closest = closest.replaceAll("\\s+","");
	    q.transaction_search_node(closest);
	    Double newLat = q.getLatitude();
	    Double newLon = q.getLongitude();
	    Double least = distance(lat, lon, newLat, newLon);
	    for (int i = 1; i < nodesInWays.size(); i++){
		q.transaction_search_node((String)(nodesInWays.get(i)));
		newLat = q.getLatitude();
		newLon = q.getLongitude();
		if (newLat != null && newLon != null){
		    Double dist = distance(lat, lon,newLat,newLon);
		    if (least > dist){
			least = dist;
			closest = ((String)(nodesInWays.get(i))).replaceAll("\\s+","");;
		    }
		}
	    }
	    ArrayList ways = q.transaction_search_way(closest);
	    String way = ((String)(ways.get(0))).replaceAll("\\s+","");
	    ArrayList tuple = new ArrayList();
	    tuple.add(closest);
	    tuple.add(way);
	    return tuple;
	}
	catch(Exception ex){
	    System.out.println("error");
	}
	return null;
    }
    public ArrayList allNeighbors(ArrayList currT){
	try{
	    ArrayList output = new ArrayList();
	    String curr = (String)(currT.get(0));
	    String walkedWay = (String)(currT.get(1));
	    ArrayList ways = q.transaction_search_way(curr);
	    System.out.print(curr + "node :");
	    System.out.println(ways);
	    for (int i = 0; i < ways.size(); i ++){
		String way = (String)(ways.get(i));
		System.out.println("Curr way: " +way);
		way = way.replaceAll("\\s+","");
		if (!hasWalked.contains(way)){
		    ArrayList nodes = q.transaction_search_nodes_of_way(way);
		    for (int j = 0; j < nodes.size(); j++){
			String node = (String) (nodes.get(j));
			node = node.replaceAll("\\s+","");
			ArrayList tuple = new ArrayList();
			tuple.add(node);
			tuple.add(way);
			output.add(tuple);
		    }
		    if (walkedWay.equals(way)){
			hasWalked.add(way);
		    }
		}
	    }
	    return output;
	}
	catch(Exception ex){
	}
	return null;
    }
	
    public ArrayList nearestNeighbors(String curr){
	try{
	    curr = curr.replaceAll("\\s+","");
	    ArrayList output = new ArrayList();
	    ArrayList ways = q.transaction_search_way(curr);
	    System.out.print(curr + " ");
	    System.out.println(ways);
	    for (int i = 0; i < ways.size(); i++){
		String way = (String)(ways.get(i));
		way = way.replaceAll("\\s+","");
		ArrayList nodes = q.transaction_search_nodes_of_way((String)(ways.get(i)));
		String first = null;
		String second = null;
		int n = 0;
		while( (first == null || second == null) && n < nodes.size()){
		    String node = (String)(nodes.get(n));
		    node = node.replaceAll("\\s+","");
		    System.out.println(first + " " + second + " " + curr);
		    if (first == null){
			if (! (node.equals(curr))){
			    first = node;
			}
		    }
		    else{
			if(!(node.equals(curr))){
			    second = node;
			}
		    }
		    System.out.println("Counter n: " +n);
		    n++;
		}
		if (first == null){
		    return output;
		}
		q.transaction_search_node(first);
		double fLat = q.getLatitude();
		double fLon = q.getLongitude();
		q.transaction_search_node(second);
		double sLat = q.getLatitude();
		double sLon = q.getLongitude();
		for (int j = 0; j < nodes.size(); j++){
		    String node = ((String)nodes.get(j));
		    node = node.replaceAll("\\s+","");
		    if (!(node.equals(curr))){
			System.out.println("Current neighbor " +node);
			q.transaction_search_node(node);
			double nLat = q.getLatitude();
			double nLon = q.getLongitude();
			q.transaction_search_node(curr);
			double cLat = q.getLatitude();
			double cLon = q.getLongitude();
			if ( !node.equals(second) &&distance(nLat,nLon,cLat,cLon) < distance(fLat,fLon,cLat,cLon)){
			    first = node;
			}
			else if ( !node.equals(first) && distance(nLat, nLon, cLat, cLon) < distance(sLat,sLon,cLat, cLon)){
			    second = node;
			}
		    }
		}
		output.add(first);
		output.add(second);
	    }
	    return output;
	}
	catch(Exception ex){
	    System.out.println("error");
	}
	return null;
    }    
    
    
    public ArrayList getPath(double lat0, double lon0, double lat1, double lon1, int choice){
	try{
	    hasWalked = new ArrayList();
	    ArrayList startingT = getClosestPathNode(lat0, lon0);
	    String startingNode = ((String)(startingT.get(0))).replaceAll("\\s+","");
	    ArrayList endT = getClosestPathNode(lat1,lon1);
	    String endNode = ((String)(endT.get(0))).replaceAll("\\s+","");
	    NodeComparable nc = new NodeComparable();
	    nc.setTarget(endNode);
	    PriorityQueue<ArrayList> pq = new PriorityQueue<ArrayList>(10, nc);
	    ArrayList temp = new ArrayList();
	    temp.add(0.0);
	    temp.add(0.0);
	    ArrayList startTuple = new ArrayList();
	    startTuple.add(startingNode);
	    startTuple.add((String) (startingT.get(1)));
	    temp.add(startTuple);
	    pq.add(temp);
	    System.out.println("Priority Queue initialized");
	    ArrayList path = (ArrayList)(pq.peek());
	    ArrayList nodeT = (ArrayList)(path.get(path.size() -1));
	    String node = (String) (nodeT.get(0));
	    while (pq.size() > 0 && !(node.equals(endNode))   ){
		path = (ArrayList)(pq.peek());
		nodeT = (ArrayList)(path.get(path.size() -1));
		System.out.println(pq.remove(path));
		node = (String)(nodeT.get(0));
		q.transaction_search_node(node);
		Double nEle = q.getElevation();
		Double nLat = q.getLatitude();
		Double nLon = q.getLongitude();
		System.out.println("Attempt to start neighbor search");
		ArrayList neighbors = allNeighbors(nodeT);
		System.out.println(neighbors);
		boolean walkedPath = false;
		for (int i = 0; i < neighbors.size(); i++){
		    ArrayList tuple = (ArrayList)(neighbors.get(i));
		    ArrayList newPath = new ArrayList(path);
		    String neighbor = (String) (tuple.get(0));
		    neighbor = neighbor.replaceAll("\\s+","");
		    if (! newPath.contains(neighbor)){
			System.out.println("test");
			Double dist = (Double)(path.get(0));
			System.out.println("line1");
			Double ele = 1.0;
			System.out.println("line2");
			q.transaction_search_node(neighbor);
			System.out.println("line3");
			Double neighborEle = q.getElevation();
			System.out.println("line4");
			Double neighborLat = q.getLatitude();
			System.out.println("line5");
			Double neighborLon = q.getLongitude();
			System.out.println("Data found on :" + neighbor);
			if (nEle != null && nLat != null && nLon != null && neighborEle != null && neighborLat != null && neighborLon != null){
			    if (choice == 0){
				if (neighborEle > nEle){
				    ele = 1.3;
				}
				else if (neighborEle < nEle){
				    ele = 0.9;
				}
			    }
			    else if (choice == 1){
				ele = 2;
			    }
			    else{
				ele = 1;
			    }
			    ele = Math.pow(ele, Math.abs(neighborEle - nEle));
			    dist += distance( neighborLat,neighborLon,nLat, nLon);
			    newPath.set(0, dist*ele);
			    newPath.set(1, dist);
			    newPath.add(tuple);
			    System.out.println(neighbor + " added to path");
			    pq.add(newPath);
			    if (neighbor.equals(endNode)){
				return newPath;
			    }
			}
		    }
		}
	    }
	    if(pq.size() > 0){
		return pq.peek();
	    }
	    return null;
	}
	catch(Exception ex){
	    System.out.println("error");
	}
	return null;
    }

    public ArrayList toEnglish(ArrayList path){
	if (path == null){
	    return null;
	}
	ArrayList output = new ArrayList();
	ArrayList prev = (ArrayList)(path.get(2));
	String node = (String)(tuple.get(0));
	String way = (String)(tuple.get(1));
	String street = q.transaction_search_streetName(way);
	String building = q.transaction_search_buildingName(way);
	ArrayList curr;
	if (building != null){
	    building = building.trim();
	    output.add("Starting from " + building);
	}
	else{
	    output.add("Starting from your location");
	}
	for (int i = 3; i < path.size(); i++){
	    curr = (ArrayList)(path.get(i));
	    node = (String)(curr.get(0));
	    way = (String)(curr.get(1));
	    street = q.transaction_search_streetName(way)
	    String pNode = (String)(prev.get(0));
	    String pWay = (String)(prev.get(1));
	    q.transaction_search_node(node);
	    Double cLat = q.getLatitude();
	    Double cLon = q.getLongitude();
	    q.transaction_search_node(pNode);
	    Double pLat = pNode.getLatitude();
	    Double pLon = pNode.getLongitude();
	    Double cRad = Math.toRadians(q.getLatitude());
	    Double pRad = Math.toRadians(pLat);
	    Double differenceLat = Math.toRadians(pLat - cLat);
	    Double differenceLon = Math.toRadians(pLon - cLon);
	    Double a = Math.sin(differenceLat/2) * Math.sin(differenceLat/2) + Math.cos(cRad)* Math.cos(pRad) *Math.sin(differenceLon/2) * Math.sin(differenceLon/2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    Double d = 6371000 * c;
	    String line;
	    String blank;
	    if (((String)(curr.get(1))).equals(((String)(prev.get(1))))){
		if (street == null){
		    blank = "the road.";
		}
		else{
		    street = street.trim();
		    blank = street;
		}
		String line = "Walk " + d + " meters on " + blank;
		
	    }
	    else{
		if (street == null){
		    blank == "the path";
		}
		else{
		    street = street.trim();
		    blank = street;
		}
		String line = "Turn onto " + blank + " and walk " + d + " meters";
	    }
	    output.add(line);
	}
	building = q.transaction_search_buildingName(way);
	if (building != null){
	    building = building.trim();
	    output.add("You have arrived at " + building);
	}
	else{
	    output.add("You have arrived at your destination");
	}
	return output;
    }
}
