package fall13.dm.kmeans;
import java.text.DecimalFormat;
import java.util.*;

public class Point{
	private int noD;
	private Number[] coords;
	private ArrayList<Integer> nulls;
	
	Point(Number[] a, ArrayList<Integer> anulls){
		coords = a;
		noD = a.length;
		nulls = anulls;
	}
	
	Point(int num){
		this.coords = new Number[num];
		this.noD = num;
		this.nulls = new ArrayList<Integer>(0);
	}
	
	Point(){
		
	}

	Point(Point p){
		this.coords = new Number[p.coords.length];
		this.nulls = new ArrayList<Integer>(p.nulls.size());
		this.noD = p.noD;
		for(int i = 0; i < noD; i++) this.coords[i] = p.coords[i];
		for(Integer i : p.nulls) this.nulls.add(i.intValue());
	}

	protected int getNoD() {
		return noD;
	}

	protected Number[] getCoords() {
		return coords;
	}

	protected Number getCoord(int index) {
		return coords[index];
	}

	protected ArrayList<Integer> getNulls() {
		return nulls;
	}

	protected void setNoD(int noD) {
		this.noD = noD;
	}

	protected void setCoord(int index, Number val) {
		this.coords[index] = val;
	}

	protected void setCoords(Number[] coords) {
		this.coords = coords;
	}

	protected void setCoords(ArrayList<Number> coords) {
		this.coords = coords.toArray(new Number[coords.size()]);
	}

	protected void addNull(int index) {
		this.nulls.add(index);
	}

	protected void setNulls(ArrayList<Integer> anulls) {
		this.nulls = anulls;
	}

	Double magnitude(){
		return Distances.eucDist(this,this);
	}
	

	Boolean equalTo(Point p){
		Boolean equals = Boolean.TRUE;
		for(int i = 0; i < noD; i++){
			if(p.getCoord(i) != this.getCoord(i)){
				equals = Boolean.FALSE;
				break;
			}
		}
		return equals;
	}
	
	public String toString(){
		DecimalFormat df = new DecimalFormat("#.##");
		String display = "(";
		for(int i = 0; i < noD-1; i++){
			display += df.format(this.getCoord(i)) + ", ";
		}
		display += df.format(this.getCoord(noD - 1)) + ")";
		return display;
	}

	public String tokenWrite(){
		String display = "";
		for(int i = 0; i < noD-1; i++){
			display += this.getCoord(i) + ",";
		}
		display += this.getCoord(noD - 1) + "\n";
		return display;
	}

}
