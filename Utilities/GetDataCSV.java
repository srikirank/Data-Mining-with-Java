package fall13.dm.kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class GetDataCSV {

	private String filePath;
	private Integer skipLines = 0;
	private String seperator = ",";
	private Integer header = 0;
	private Boolean isHeader = false;

	private static final Set<String> NULLS = new HashSet<String>(Arrays.asList(
			new String[] {"null", "?"}
			)); 

	public GetDataCSV(String path){
		this.filePath = path;
	}

	public GetDataCSV(String path, Integer num){
		this.filePath = path;
		this.skipLines = num;
	}

	public GetDataCSV(String path, Integer num, Integer header){
		this.filePath = path;
		this.skipLines = num;
		this.header = header - 1;
		this.isHeader = true;
	}
	
	protected Integer getHeader() {
		return header;
	}

	protected void setHeader(Integer header) {
		this.header = header;
	}

	protected String getFilePath() {
		return filePath;
	}

	protected Integer getSkipLines() {
		return skipLines;
	}

	protected String getSeperator() {
		return seperator;
	}

	protected void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	protected void setSkipLines(Integer skipLines) {
		this.skipLines = skipLines;
	}

	protected void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public Data getData(){

		BufferedReader br = null;
		String line = "";
		
		int count = this.skipLines;
		int hcount = 0;
		
		Data d = new Data();
		String cols[];
		
		try{
			br = new BufferedReader(new FileReader(this.filePath));
			while((line = br.readLine()) != null){
				if(count-- > 0){
					hcount++;
					continue;
				}
				else{
					if(isHeader && hcount == header){
						// -1 To store the null value after the last comma
						cols = line.split(seperator,-1);
						d.setColNames(cols);
						d.setIsColNames(true);
					}
					else{
						// -1 To store the null value after the last comma
						String[] n = line.split(seperator,-1);
						
						Point p = new Point(n.length);
						for(int i = 0; i < n.length; i++){
							if(n[i] == null || NULLS.contains(n[i].toLowerCase()) || n[i].length() == 0){
								p.setCoord(i, 0);
								p.addNull(i);
							}
							else{
								p.setCoord(i, NumberFormat.getInstance().parse(n[i]));
							}
						}
						d.addPoint(p);
					}
				}
				hcount++;
				if(count % 5000 == 0){
					System.out.println("Finished processing " + count + " lines...\n");
				}
			}
		}
		catch(FileNotFoundException e){
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		finally{
			if(br != null){
				try{
					br.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
				
			}
		}
		int noP = this.isHeader ? (hcount- this.skipLines - 1) : (hcount - this.skipLines);
		d.setNoP(noP);
		d.setNoD(d.getPoint(0).getNoD());
		d.compCases();
		return d;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		GetDataCSV g = new GetDataCSV("/Users/Kiran/Development/Workspace/K-Means/src/fall13/dm/kmeans/data.csv", 1, 2);
//		GetDataCSV g = new GetDataCSV("/Users/Kiran/Google Drive/Masters/Fall 2013/B565-DATA MINING/B565-HomeWork/Homework3/Resources/breast-cancer-wisconsin.data");
//		Data d = g.getData();
		
		GetDataCSV g1 = new GetDataCSV("/Users/Kiran/Google Drive/Masters/Fall 2013/B565-DATA MINING/B565-HomeWork/Homework4/kmeansCleaned.csv", 0, 1);
		Data d1 = g1.getData();
		System.out.println(d1.getPoint(0));
//		System.out.println(d.getIncompCases().size());
//		Data missingD = d.rowSubsetCollection(d.getIncompCases());
//		System.out.println(missingD);
//		Data missingDSCN = missingD.columnSubset(new Integer[] {1,2,3,4,5,6,7,8,9,10});
//		System.out.println(missingDSCN);
//		Data missingD = d.rowSubset(d.getIncompCases());
//
//		Data newD1 = missingD.columnSubset(new Integer[]{0,10});
//		Data newD2 = missingD.columnSubset(new Integer[]{0,8,9,10});
//		Data newD3 = missingD.columnSubset(new Integer[]{0,6,7,8,9,10});
//		Data newD4 = missingD.columnSubset(new Integer[]{0,4,5,6,7,8,9,10});
//		Data newD5 = missingD.columnSubset(new Integer[]{0,3,4,5,6,7,8,9,10});
//
//		Clustering c1 = new Clustering(newD1,2);
//		Clustering c2 = new Clustering(newD2,2);
//		Clustering c3 = new Clustering(newD3,2);
//		Clustering c4 = new Clustering(newD4,2);
//		Clustering c5 = new Clustering(newD5,2);
//
//		c1.kMeans();
//		c2.kMeans();
//		c3.kMeans();
//		c4.kMeans();
//		c5.kMeans();
//
//		System.out.println("A[1:9] : " + c1.PPF(c1));
//		System.out.println("A[1:7] : " + c1.PPF(c2));
//		System.out.println("A[1:5] : " + c1.PPF(c3));
//		System.out.println("A[1:3] : " + c1.PPF(c4));
//		System.out.println("A[1:2] : " + c1.PPF(c5));
//		
//		c1.displayPartitions();
//		for(int i = 0; i < c1.noC; i++){
////			System.out.println("Cluster " + i + " : " + c1.returnPartition(i));
//			ArrayList<Number> labels = new ArrayList<Number>(0);
//			for(int j : c1.returnPartition(i)){
//				labels.add(d.getPoint(j).getCoord(10));
//			}
//			System.out.println("Label for Cluster " + i +" : " + labels);
//		}
	}
}
