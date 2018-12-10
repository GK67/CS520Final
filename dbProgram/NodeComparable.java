import java.util.*;
public class NodeComparable implements Comparator<ArrayList>{
    String target;

    public void setTarget(String t){
	target = t;
    }
    public static double distance(double xlat, double xlon, double ylat, double ylon){
	return Math.sqrt(Math.pow(xlat-ylat, 2) + Math.pow(xlon-ylon, 2));
    }

    public int compare(ArrayList arg0, ArrayList arg1){/*
	double score1 = ((double)arg0.get(0)) + distance(getLatitude(arg0), getLongitude(arg0), getLatitude(target), getLongitude(target));
	double score2 = ((double)arg1.get(0)) + distance(getLatitude(arg1), getLongitude(arg1), getLatitude(target), getLongitude(target));*/
	double score1 = ((double)arg0.get(0));
	double score2 = ((double)arg1.get(0));
	
	return (int)Math.round(score1 - score2);
    }
}
    
