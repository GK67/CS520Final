
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

public class convertData{
     public static String convertStreamToString(InputStream is) {

	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	
	String line = null;
	try {
	    while ((line = reader.readLine()) != null) {
		sb.append(line + "\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return sb.toString();
    }
    
    public static Document getMapData(String filename){	
	try {
	    File fXmlFile = new File(filename);
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);
	    
	    doc.getDocumentElement().normalize();
	    return doc;
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static JSONObject requestEle(JSONObject[] coords){
	try {
	    JSONObject fields = new JSONObject();
	    fields.put("locations", coords);
	    HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
	
	    HttpPost request = new HttpPost("https://api.open-elevation.com/api/v1/lookup");
	    StringEntity params =new StringEntity(fields.toString());
	    request.addHeader("Accept", "application/json");
	    request.addHeader("Content-Type", "application/json");
	    request.setEntity(params);
	    HttpResponse response = httpClient.execute(request);
	    
	    //handle response here...
	    InputStream in = response.getEntity().getContent();
	    return(new JSONObject(convertStreamToString(in)));
	    
	    
	    
	}catch (Exception ex) {
	    
	    //handle exception here
	    
	}
	return null;
    }

    public static JSONObject getElevation(NodeList nList){
	try {
	    JSONObject coords[] = new JSONObject[nList.getLength()];
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	    
		Node nNode = nList.item(temp);
	    
	    
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		
		    Element eElement = (Element) nNode;
		    
		    JSONObject place = new JSONObject();
		    place.put("latitude", Double.parseDouble(eElement.getAttribute("lat")));
		    place.put("longitude", Double.parseDouble(eElement.getAttribute("lon")));
		    coords[temp] = place;
		
		}
	    }
	    return requestEle(coords);
	}
	catch(Exception ex){
	}
	return null;
    }

    public static ArrayList nodesToArray(String filename){
	try{
	    Document doc = getMapData(filename);
	    NodeList nodes = doc.getElementsByTagName("node");
	    JSONObject elevation = getElevation(nodes);
	    ArrayList output = new ArrayList();
	    for (int i = 0; i < nodes.getLength(); i++){
		Element element = (Element) nodes.item(i);
		JSONObject holder = elevation.getJSONArray("results").getJSONObject(i);
		ArrayList node = new ArrayList();
		node.add(element.getAttribute("id"));
		node.add(Double.parseDouble(element.getAttribute("lat")));
		node.add(Double.parseDouble(element.getAttribute("lon")));
		node.add(holder.getDouble("elevation"));
		output.add(node);
	    }
	    return output;
	}
	catch(Exception ex){
	    System.out.println("something happened");
	}
	return null;
    }

    
    public static ArrayList waysToArray(String filename){
	try{
	    Document doc = getMapData(filename);
	    NodeList ways = doc.getElementsByTagName("way");
	    ArrayList output = new ArrayList();
	    for (int i = 0; i < ways.getLength(); i++){
		Element element = (Element) ways.item(i);
		ArrayList node = new ArrayList();
		node.add(element.getAttribute("id"));
		ArrayList nodes = new ArrayList();
		NodeList nds = element.getElementsByTagName("nd");
		for (int j = 0; j < nds.getLength(); j++){
		    Element ref = (Element) nds.item(j);
		    nodes.add(ref.getAttribute("ref"));
		}
		String name = null;
		NodeList tags = element.getElementsByTagName("tag");
		boolean isBuilding = false;
		String building = null;
		for (int j = 0; j < tags.getLength(); j++){
		    Element tag = (Element) tags.item(j);
		    if (tag.getAttribute("k").equals("name")){
			name = tag.getAttribute("v");
		    }
		    if (tag.getAttribute("k").equals("building")){
			isBuilding = true;
		    }
		}
		if (isBuilding){
		    name = null;
		    for (int j = 0; j < tags.getLength(); j++){
			Element tag = (Element) tags.item(j);
			if (tag.getAttribute("k").equals("name")){
			    building = tag.getAttribute("v");
			}
			if (tag.getAttribute("k").equals("addr:street")){
			    name = tag.getAttribute("v");
			}
		    }
		}
		node.add(name);
		node.add(nodes);
		node.add(building);
		output.add(node);
	    }
	    return output;
	}
	catch(Exception ex){
	    System.out.println("error");
	}
	return null;
    }
}
