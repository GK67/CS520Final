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

    public Path(){
	try{
	    q = new Query();
	    q.openConnection();
	    q.prepareStatements();
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

    public String getClosestPathNode(double lat, double lon){
	try{
	    ArrayList nodesInWays = q.transaction_search_all_node();
	    String closest = (String)(nodesInWays.get(0));

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
			closest = (String)(nodesInWays.get(i));
		    }
		}
	    }
	    return closest;
	}
	catch(Exception ex){
	    System.out.println("error");
	}
	return null;
    }
    public ArrayList allNeighbors(String curr){
	try{
	    ArrayList output = new ArrayList();
	    ArrayList ways = q.transaction_search_way(curr);
	    System.out.println(ways.size());
	    for (int i = 0; i < ways.size(); i ++){
		ArrayList nodes = q.transaction_search_nodes_of_way((String)(ways.get(i)));
		for (int j = 0; j < nodes.size(); j++){
		    String node = (String) (nodes.get(j));
		    output.add(node);
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
	    ArrayList output = new ArrayList();
	    ArrayList ways = q.transaction_search_way(curr);
	    System.out.println(ways.size());
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
    
    
    public ArrayList getPath(double lat0, double lon0, double lat1, double lon1){
	try{
	    String startingNode = getClosestPathNode(lat0, lon0);
	    startingNode.replaceAll("\\s+","");
	    String endNode = getClosestPathNode(lat1,lon1);
	    endNode.replaceAll("\\s+","");
	    NodeComparable nc = new NodeComparable();
	    nc.setTarget(endNode);
	    PriorityQueue<ArrayList> pq = new PriorityQueue<ArrayList>(10, nc);
	    /* ArrayList neighbors = nearestNeighbors(startingNode);
	    System.out.println("Found Neighbors");
	    q.transaction_search_node(startingNode);
	    Double sLat = q.getLatitude();
	    Double sLon = q.getLongitude();
	    for(int i = 0; i < neighbors.size(); i ++){
		ArrayList temp = new ArrayList();
		String node = (String)(neighbors.get(i));
		q.transaction_search_node(node);
		Double nLat = q.getLatitude();
		Double nLon = q.getLongitude();
		if (nLat != null && nLon != null && sLat != null && sLon != null){
		    temp.add(distance(sLat,sLon, nLat,nLon));
		    temp.add(distance(sLat,sLon, nLat,nLon));
		    temp.add(startingNode);
		    temp.add(node);
		    pq.add(temp);
		}
	    }*/
	    ArrayList temp = new ArrayList();
	    temp.add(0);
	    temp.add(0);
	    temp.add(startingNode);
	    pq.add(temp);
	    System.out.println("Priority Queue initialized");
	    ArrayList path = (ArrayList)(pq.peek());
	    while (pq.size() > 0 && !(((String) (path.get(path.size()-1))) .equals(endNode))   ){
		System.out.print("Path: ");
		for (int i = 0; i < path.size(); i++){
		    System.out.print(path.get(i) + " ");
		}
		path = (ArrayList)(pq.peek());
		pq.remove(path);
		String node = (String)(path.get(path.size() - 1));
		q.transaction_search_node(node);
		Double nEle = q.getElevation();
		Double nLat = q.getLatitude();
		Double nLon = q.getLongitude();
		System.out.println("Attempt to start neighbor search");
		ArrayList neighbors = nearestNeighbors(node);
		System.out.println(neighbors);
		
		for (int i = 0; i < neighbors.size(); i++){
		    String neighbor = (String) (neighbors.get(i));
		    neighbor = neighbor.replaceAll("\\s+","");
		    if (! path.contains(neighbor)){
			double dist = (double)(path.get(0));
			double ele = 1.0;
			q.transaction_search_node(neighbor);
			Double neighborEle = q.getElevation();
			Double neighborLat = q.getLatitude();
			Double neighborLon = q.getLongitude();
			System.out.println("Data found on :" + neighbor);
			if (nEle != null && nLat != null && nLon != null && neighborEle != null && neighborLat != null && neighborLon != null){
			    if (neighborEle > nEle){
				ele = 1.3;
			    }
			    else if (neighborEle < nEle){
				ele = 0.9;
			    }
			    ele = Math.pow(ele, Math.abs(neighborEle - nEle));
			    dist += distance( neighborLat,neighborLon,nLat, nLon);
			    path.set(0, dist*ele);
			    path.set(1, dist);
			    path.add(neighbor);
			    pq.add(path);
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
}
