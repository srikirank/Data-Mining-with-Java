package fall13.dm.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Data{
	private int noP;
	private int noD;
	private ArrayList<Point> points;
	private ArrayList<Integer> incompCases;
	private ArrayList<String> colNames;
	private Boolean isColNames = false;

	Data(){
		this.points = new ArrayList<Point>(0);
		this.incompCases = new ArrayList<Integer>(0);
		this.colNames = new ArrayList<String>(0);		
	}
	Data(Data d){
		this.noP = d.noP;
		this.noD = d.noD;
		this.points = new ArrayList<Point>(0);
		for(Point p : d.points){
			this.points.add(p);
		}
		this.incompCases = new ArrayList<Integer>(0);
		for(Integer i : d.incompCases){
			this.incompCases.add(i);
		}
		this.colNames = d.colNames;
		this.isColNames = d.isColNames;
	}
	
	Data(int noP){
		this.noP = noP;
		this.points = new ArrayList<Point>(noP);
		this.incompCases = new ArrayList<Integer>(0);
		this.colNames = new ArrayList<String>(0);
	}
	
	Data(int noP, int noD){
		this.noP = noP;
		this.noD = noD;
		this.points = new ArrayList<Point>(noP);
		this.incompCases = new ArrayList<Integer>(0);	
		this.colNames = new ArrayList<String>(0);
	}

	Data(ArrayList<Point> p, ArrayList<Integer> incomp){
		this.points = p;
		this.incompCases = incomp;
		this.noP = this.points.size();
		this.noD = this.points.get(0).getNoD();
		this.colNames = new ArrayList<String>(0);
	}

	Data(ArrayList<Point> p, ArrayList<Integer> incomp, ArrayList<String> cols){
		this.points = p;
		this.incompCases = incomp;
		this.noP = this.points.size();
		this.noD = this.points.get(0).getNoD();
		this.colNames = cols;
		this.isColNames = true;
	}
	
	protected int getNoD() {
		return noD;
	}

	protected ArrayList<String> getColNames() {
		return colNames;
	}

	protected String getColName(int index) {
		return colNames.get(index);
	}
	
	protected void setNoD(int noD) {
		this.noD = noD;
	}

	protected void setColNames(ArrayList<String> colNames) {
		this.colNames = colNames;
	}

	protected void setColNames(String[] colNames) {
		this.colNames = new ArrayList<String>(Arrays.asList(colNames));
	}

	protected void setColName(String colname, int index) {
		this.colNames.set(index, colname);
	}

	protected void addColName(String colname) {
		this.colNames.add(colname);
	}
	
	protected int getNoP() {
		return noP;
	}

	protected ArrayList<Point> getPoints() {
		return points;
	}

	public Point getPoint(int index) {
		return points.get(index);
	}

	protected ArrayList<Integer> getIncompCases() {
		return incompCases;
	}

	protected void setNoP(int noP) {
		this.noP = noP;
	}

	protected void setPoint(Point p, int index) {
		this.points.set(index, p);
	}

	protected void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	protected void addPoint(Point p) {
		this.points.add(p);
	}
	
	protected void removePoint(Point p){
		this.points.remove(p);
	}

	protected void removePoint(int index){
		this.points.remove(index);
		this.setNoP(noP - 1);
	}

	protected void setIncompCases(ArrayList<Integer> incompCases) {
		this.incompCases = incompCases;
	}

	protected void setIncompCases(Integer[] incompCases) {
		this.incompCases = new ArrayList<Integer>(Arrays.asList(incompCases));
	}

	protected void addIncompCase(Integer incompCase){
		this.incompCases.add(incompCase);
	}

	protected Boolean getIsColNames() {
		return isColNames;
	}

	protected void setIsColNames(Boolean isColNames) {
		this.isColNames = isColNames;
	}
	
	void compCases(){
		for(int i=0; i < getNoP(); i++){
			if(!getPoint(i).getNulls().isEmpty())
				this.getIncompCases().add(i);
		}
	}
	
	// For updation of centroid of a cluster
	Double vMean(Integer[] list, int index){
		Double mean = 0.0;
		for(int i : list){
			// if(this.getIncompCases().indexOf(i) == -1)
			mean += this.getPoint(i).getCoord(index).doubleValue();
		}
		// return mean / (this.getNoP() - this.getIncompCases().size());
		return mean / (list.length);
	}
	
	Double vMean(ArrayList<Integer> list, int index){
		Double mean = 0.0;
		for(int i : list){
			// if(this.getIncompCases().indexOf(i) == -1)
				mean += this.getPoint(i).getCoord(index).doubleValue();
		}
		// return mean / (this.getNoP() - this.getIncompCases().size());
		return mean / (list.size());
	}
	
	Double vMean(int index){
		Double mean = 0.0;
		for(int i = 0; i < noP; i++){
			// if(this.getIncompCases().indexOf(i) == -1)
				mean += this.getPoint(i).getCoord(index).doubleValue();
		}
		// return mean / (this.getNoP() - this.getIncompCases().size());
		return mean / (this.getNoP());
	}

	// Update centroid of cluster
	Point mean(Integer[] list){
		int noD = this.getPoint(0).getNoD();
		Point p = new Point(noD);
		for(int i = 0; i < noD; i++){
			p.setCoord(i, vMean(list,i));
		}
		return p;
	}

	Point mean(ArrayList<Integer> list){
		int noD = this.getPoint(0).getNoD();
		Point p = new Point(noD);
		for(int i = 0; i < noD; i++){
			p.setCoord(i, vMean(list,i));
		}
		return p;
	}
	
	Point mean(){
		int noD = this.getPoint(0).getNoD();
		Point p = new Point(noD);
		for(int i = 0; i < noD; i++){
			p.setCoord(i, vMean(i));
		}
		return p;
	}
	
	Number randomInd(int index){
		ArrayList<Double> a = new ArrayList<Double>(this.getNoP());
		for(int i = 0; i < noP; i++){
			a.add(this.getPoint(i).getCoord(index).doubleValue());
		}
		return Collections.min(a) + Math.random() * (Collections.max(a) - Collections.min(a));
	}
	
	Point randomPoint(){
		int noD = this.getPoint(0).getNoD();
		Point p = new Point(noD);
		for(int i = 0; i < noD; i++){
			p.setCoord(i, randomInd(i));
		}
		return p;
	}
	
	public String toString(){
		String display = "";
		if(this.isColNames){
			display += colNames + "\n";
		}
		for(int i = 0; i < noP; i++){
			display += "Point " + (i+1) + " : " + this.getPoint(i).toString() + "\n";
		}
		return display;
	}

	public void display(){
		if(this.isColNames){
			System.out.println(colNames + "\n");
		}
		for(int i = 0; i < noP; i++){
			System.out.println("Point " + (i+1) + " : " + this.getPoint(i).toString() + "\n");
		}
	}

	void reverseIncompCases(){
		Collections.reverse(this.incompCases);
	}
	
	static Data cleanByRemove(Data d){
		Data newD = new Data(d);
		newD.reverseIncompCases();
		for(int i : newD.getIncompCases())
			newD.removePoint(i);
		newD.setIncompCases(new ArrayList<Integer>(0));
		newD.compCases();
		return newD;
	}
	
	// void cleanByRemove(){
	// 	reverseIncompCases();
	// 	for(int i : incompCases)
	// 		removePoint(i);
	// 	setIncompCases(new ArrayList<Integer>(0));
	// 	compCases();
	// }
	
	Data rowSubset(Integer[] skipRows){
		Data newD = new Data(this);
		newD.reverseIncompCases();
		for(int i : skipRows)
			newD.removePoint(i);
		newD.setIncompCases(new ArrayList<Integer>(0));
		newD.compCases();
		return newD;
	}
	
	Data rowSubset(ArrayList<Integer> skipRows){
		Data newD = new Data(this);
		newD.reverseIncompCases();
		for(int i : skipRows)
			newD.removePoint(i);
		newD.setIncompCases(new ArrayList<Integer>(0));
		newD.compCases();
		return newD;
	}
	
	Data rowSubsetCollection(ArrayList<Integer> incRows){
		Data newD = new Data();
		for(int i : incRows){
			newD.addPoint(this.getPoint(i));
		}
		newD.setNoD(this.noD);
		newD.setNoP(incRows.size());
		newD.compCases();
		newD.setColNames(this.colNames);
		newD.setIsColNames(this.isColNames);
		return newD;
	}

	Data rowSubsetCollection(int i1, int i2){
		Data newD = new Data();
		for(int i = i1; i <= i2; i++){
			newD.addPoint(this.getPoint(i));
		}
		newD.setNoD(this.noD);
		newD.setNoP(i2 - i1 + 1);
		newD.compCases();
		newD.setColNames(this.colNames);
		newD.setIsColNames(this.isColNames);
		return newD;
	}

	Data rowSubset(int i1, int i2){
		Data newD = new Data();
		for(int i = 0; i < i1; i++){
			newD.addPoint(this.getPoint(i));
		}
		for(int i = i2 + 1; i < this.noP; i++){
			newD.addPoint(this.getPoint(i));
		}
		
		newD.setNoD(this.noD);
		newD.setNoP(this.noP - i2 + i1 - 1);
		newD.compCases();
		newD.setColNames(this.colNames);
		newD.setIsColNames(this.isColNames);
		return newD;
	}

	Data rowSubsetCollection(Integer[] incRows){
		Data newD = new Data(this);
		
		return newD;
	}

	Data columnSubset(Integer[] skipColumns){
		int num = skipColumns.length;
		Data newD = new Data(this.getNoP());
		
		newD.setNoD(this.noD - num);
		
		HashSet<Integer> skipList = new HashSet<Integer>(Arrays.asList(skipColumns));
				
		for(int i = 0; i < this.noP; i++){
			Point pnew = new Point(this.noD - num);
			HashSet<Integer> anulls = new HashSet<Integer>(this.getPoint(i).getNulls());
			
			int k = 0;
			
			for(int j = 0; j < this.noD; j++){
				if(skipList.contains(j)) continue;
					
				else{
					pnew.setCoord(k, this.getPoint(i).getCoord(j));
					if(anulls.contains(j)){
						pnew.addNull(k);
					}
					k++;
				}
			}
			// Adding the new point to new data element
			newD.addPoint(pnew);
		}
		newD.compCases();
		if(this.isColNames){
			for(int i = 0; i < this.noD; i++){
				if(skipList.contains(i)) continue;
				else newD.addColName(this.getColName(i));
			}
			newD.isColNames = true;
		}
		return newD;
	}

}
