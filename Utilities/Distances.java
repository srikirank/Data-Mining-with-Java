package fall13.dm.kmeans;

public class Distances {
	static <Number> Double eucDist(Point p1, Point p2){
		Double dist = 0.0;
		//null values are given value 0
		for(int i = 0; i < p1.getNoD(); i++){
			Double diff = p1.getCoord(i).doubleValue() - p2.getCoord(i).doubleValue();
			dist += diff * diff;
		}
		return Math.sqrt(dist);
	}
	
	static <Number> Double manhattanDist(Point p1, Point p2){
		Double dist = 0.0;
		//null values are given value 0
		for(int i = 0; i < p1.getNoD(); i++){
			Double diff = Math.abs(p1.getCoord(i).doubleValue() - p2.getCoord(i).doubleValue());
			dist += diff;
		}
		return dist;
	}
	
	static <Number> Double cosineSim(Point p1, Point p2){
		Double sim = 0.0;
		//null values are given value 0
		for(int i = 0; i < p1.getNoD(); i++){
			Double dotProduct = p1.getCoord(i).doubleValue() * p2.getCoord(i).doubleValue();
			sim += dotProduct;
		}
		return sim / p1.magnitude() * p2.magnitude();
	}
	
	static <Number> Double jaccardCoef(Point p1, Point p2){
		Double sim = 0.0;
		//null values are given value 0
		for(int i = 0; i < p1.getNoD(); i++){
			Double dotProduct = p1.getCoord(i).doubleValue() * p2.getCoord(i).doubleValue();
			sim += dotProduct;
		}
		return sim / (p1.magnitude() * p1.magnitude() + p2.magnitude() * p2.magnitude() - p1.magnitude() * p2.magnitude());
	}
}