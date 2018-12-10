import java.util.*;
public class NodeComparable implements Comparator<ArrayList>{
    String target;
    double tLat;
    double tLon;
    Query q;

    public void setTarget(String t){
	target = t;
	q = new Query();
	q.transaction_search_node(target);
	tLat = q.getLatitude();
	tLon = q.getLongitude();
    }
    public static double distance(double xlat, double xlon, double ylat, double ylon){
	return Math.sqrt(Math.pow(xlat-ylat, 2) + Math.pow(xlon-ylon, 2));
    }

    public int compare(ArrayList arg0, ArrayList arg1){
	String node0 = (String)(arg0.get(arg0.size()-1));
	String node1 = (String)(arg1.get(arg1.size()-1));
	q.transaction_search_node(node0);
	double lat0 = q.getLatitude();
	double lon0 = q.getLongitude();
	q.transaction_search_node(node1);
	double lat1 = q.getLatitude();
	double lon1 = q.getLongitude();
	double score1 = ((double)arg0.get(0)) + distance(lat0, lon0,tLat , tLon);
	double score2 = ((double)arg1.get(0)) + distance(lat1, lon1, tLat, tLon);
	
	return (int)Math.round(score1 - score2);
    }
}
    
