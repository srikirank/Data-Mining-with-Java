package fall13.dm.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Clustering {
	Data d;
	int noC; //Clusters
	int noI = 100; //Iterations
	Integer[] partitionLabels;
	ArrayList<Double> costFunction;
	Point[] centroids;
	static final String[] distances = {"eucDist"};
	static final String[] methods = {"costOpt", "stableCluster"};
	String methodType = "costOpt";
	String distanceType = "eucDist";
	
	
	public Clustering(Data d, int noC){
		this.d = d;
		this.noC = noC;
		
		partitionLabels = new Integer[this.d.getNoP()];
		costFunction = new ArrayList<Double>(noI);
		// Initially filling the partition labels with zeroes.
		Arrays.fill(partitionLabels, 0);
		centroids = new Point[noC];
	}

	Clustering(Data d, int noC, String method){
		this.d = d;
		this.noC = noC;
		this.noI = 100;
		
		costFunction = new ArrayList<Double>(noI);
		partitionLabels = new Integer[this.d.getNoP()];
		
		// Initially filling the partition labels with zeroes.
		Arrays.fill(partitionLabels, 0);
		
		centroids = new Point[noC];
	}

	Clustering(Data d, int noC, int noI){
		this.d = d;
		this.noC = noC;
		this.noI = noI;
		
		costFunction = new ArrayList<Double>(noI);
		partitionLabels = new Integer[this.d.getNoP()];
		centroids = new Point[noC];
	}
		
	Clustering(Data d, int noC, int noI, String method){
		this.d = d;
		this.noC = noC;
		this.noI = noI;
		
		costFunction = new ArrayList<Double>(noI);
		partitionLabels = new Integer[this.d.getNoP()];
		centroids = new Point[noC];
	}
		
	protected Data getD() {
		return d;
	}

	protected int getNoC() {
		return noC;
	}

	protected int getNoI() {
		return noI;
	}

	protected String getDistanceType() {
		return distanceType;
	}

	protected void setD(Data d) {
		this.d = d;
	}

	protected void setNoC(int noC) {
		this.noC = noC;
	}

	protected void setNoI(int noI) {
		this.noI = noI;
	}

	protected Double getDistance(Point p1, Point p2) {
		if(this.distanceType.equalsIgnoreCase(distances[0])) return Distances.eucDist(p1, p2);
		return 0.0;
	}
	
	protected void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}
	
	
	protected String getMethodType() {
		return methodType;
	}

	protected void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	protected ArrayList<Double> getCostFunction() {
		return costFunction;
	}

	protected Double getCostFunction(int index) {
		return costFunction.get(index);
	}

	protected void setCostFunction(ArrayList<Double> cost) {
		this.costFunction = cost;
	}

	protected void addCostFunction(Double cost) {
		this.costFunction.add(cost);
	}

	protected Integer[] getPartitionLabels() {
		return partitionLabels;
	}

	protected Integer getPartitionLabel(int index) {
		return partitionLabels[index];
	}

	protected void setPartitionLabels(Integer[] partitionLabels) {
		this.partitionLabels = partitionLabels;
	}

	protected void setPartitionLabel(int index, Integer partitionLabel) {
		this.partitionLabels[index] = partitionLabel;
	}

	protected Point[] getCentroids() {
		return centroids;
	}

	protected Point getCentroids(int index) {
		return centroids[index];
	}

	protected void setCentroids(Point[] centroids) {
		this.centroids = centroids;
	}

	public void kMeans(){
		if(getMethodType().toLowerCase().equalsIgnoreCase(methods[0])) this.kMeansCostOptimize2();
		else if(getMethodType().toLowerCase().equalsIgnoreCase(methods[1])) this.kMeansPLNoChange2();
	}
	
	void kMeansPLNoChange2(){
		// Random centroids based on min and max of point co-ordinates
		// iterated until no considerable change in partition_labels;
		Integer[] plNew = new Integer[this.partitionLabels.length];
		
		// to hold the fraction of changed labels
		double fractionChange;
		int count = 0;
		
		this.centroidsRandom();
		// for(int k = 0; k < 100; k++){
		do{
			count++;
			for(int i = 0; i < d.getNoP(); i++){
				ArrayList<Double> dist = new ArrayList<Double>(noC);
				for(int j = 0; j < noC; j++){
					dist.add(getDistance(d.getPoint(i),(centroids[j])));
				}
				plNew[i] = dist.indexOf(Collections.min(dist));
			}
			fractionChange = kMeansFracChange(plNew, partitionLabels);  
			for(int j = 0; j < noC; j++){
				if(returnPartition(j).size() > 0)
					centroids[j] = d.mean(returnPartition(j));
			}
			partitionLabels = plNew;
		// }
		}
		while(fractionChange > 0.01);
	}


	
	void kMeansCostOptimize(){
		int len = this.partitionLabels.length;
		Integer[] pLabels = new Integer[len];
		Point[] centrds = new Point[noC];
		
		Double distance = 0.0;
		Double cost = 0.0;
		
		for(int k = 0; k < noI; k++){
			cost = 0.0;
			centrds = this.centrdsRandom();
			
			for(int t = 0; t < 20; t++){
				
				for(int i = 0; i < d.getNoP(); i++){
					ArrayList<Double> distances = new ArrayList<Double>(noC);
					for(int j = 0; j < noC; j++){
						distance = getDistance(d.getPoint(i),(centrds[j]));
						distances.add(distance);
					}
					distance = Collections.min(distances);
					cost += distance;
					pLabels[i] = distances.indexOf(distance);
				}
				for(int j = 0; j < noC; j++){
					if(returnPartition(j).size() > 0)
						centrds[j] = d.mean(returnPartition(j)); 
				}
				
			}
			this.addCostFunction(cost);
			if(k == 0 || this.getCostFunction(k-1) > this.getCostFunction(k)){
				partitionLabels = pLabels;
				this.centroids = centrds;
			}
			
		}
		cost = Collections.min(this.costFunction);
		int index = this.costFunction.indexOf(cost);
//		System.out.println("\nMin cost + " + (index+1) +": " + cost);
		fixLabels();
	}
	
	void kMeansCostOptimize2(){
		int len = this.partitionLabels.length;
		Integer[] pLabels = new Integer[len];
		Point[] centrds = new Point[noC];
		Arrays.fill(partitionLabels, 0);
		
		Double distance = 0.0;
		Double cost = 0.0;
		
		for(int k = 0; k < noI; k++){
			cost = 0.0;
			centrds = this.centrdsRandom();
			double fractionChange = 0.0;
			do{
				for(int i = 0; i < d.getNoP(); i++){
					ArrayList<Double> distances = new ArrayList<Double>(noC);
					for(int j = 0; j < noC; j++){
						distance = getDistance(d.getPoint(i),(centrds[j]));
						distances.add(distance);
					}
					distance = Collections.min(distances);
					cost += distance;
					pLabels[i] = distances.indexOf(distance);
				}
				fractionChange = kMeansFracChange(pLabels, partitionLabels);  
				for(int j = 0; j < noC; j++){
					if(returnPartition(j).size() > 0)
						centrds[j] = d.mean(returnPartition(j)); 
				}
				partitionLabels = pLabels;
			}
			while(fractionChange > 0.01);
				
			this.addCostFunction(cost);
			if(k == 0 || this.getCostFunction(k-1) > this.getCostFunction(k)){
				partitionLabels = pLabels;
				this.centroids = centrds;
			}
		}
		cost = Collections.min(this.costFunction);
		int index = this.costFunction.indexOf(cost);
		fixLabels();
	}
	
	void fixLabels(){
		double distance = 0.0;
		for(int i = 0; i < d.getNoP(); i++){
			ArrayList<Double> distances = new ArrayList<Double>(noC);
			for(int j = 0; j < noC; j++){
				distance = getDistance(d.getPoint(i),(centroids[j]));
				distances.add(distance);
			}
			distance = Collections.min(distances);
			partitionLabels[i] = distances.indexOf(distance);
		}
	}

	double kMeansFracChange(Integer[] newArr, Integer[] oldArr){
		int len = newArr.length;
		int count = 0;
		for(int i = 0; i < len; i++){
			if(newArr[i] != oldArr[i]) count++;
		}
		return (double)count/(double)len;
	}
	
	void centroidsRandom(){
		for(int i = 0; i < this.noC; i++){
			centroids[i] = this.d.randomPoint();
		}
	}

	Point[] centrdsRandom(){
		Point[] centrds = new Point[noC];
		for(int i = 0; i < this.noC; i++){
			centrds[i] = this.d.randomPoint();
		}
		return centrds;
	}
	
	public void displayPartitions(){
		ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < noC; i++){
			clusters.add(new ArrayList<Integer>(0));
		}
		int len = partitionLabels.length;
		for(Integer i = 0; i < len; i++){
			int ind = partitionLabels[i];
			clusters.get(ind).add(i+1);
		}
		for(int i = 0; i < noC; i++){
			System.out.println("Cluster : " + (i + 1) + " : " + clusters.get(i));
		}
	}

	Double PPF(Clustering c){
		double ppf = 0.0;
		double distance = 0.0;
		int tp = 0;
		int fp = 0;
		Integer[] mappingTable = new Integer[noC];
		for(int i = 0; i < noC; i++){
			ArrayList<Double> distances = new ArrayList<Double>(noC);
			for(int j = 0; j < noC; j++){
				distance = getDistance(c.getCentroids(i),this.getCentroids(j));
				distances.add(distance);
			}
			distance = Collections.min(distances);
			mappingTable[i] = distances.indexOf(distance);
		}
		for(int i = 0; i < partitionLabels.length; i++){
			if(this.partitionLabels[i] == mappingTable[c.partitionLabels[i]]){
				tp++;
			}
			else{
				fp++;
			}
		}
		ppf = ((double)tp / (double)(tp + fp));
		return ppf;
	}
		
	ArrayList<Integer> returnPartition(int index){
		ArrayList<Integer> cluster = new ArrayList<Integer>(0);
		int len = partitionLabels.length;
		for(Integer i = 0; i < len; i++){
			if(partitionLabels[i] == index)
				cluster.add(i);
		}
		return cluster;
	}
}
