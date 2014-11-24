package dataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Data {
    private int numberOfPoints;
    private int numberOfFeatures;
    private ArrayList<Point> points;
    private HashSet<Integer> incompleteCases;
    private ArrayList<String> featureNames;
    private Boolean hasFeatureNames = false;

    public Data() {
        this.points = new ArrayList<Point>(0);
        this.incompleteCases = new HashSet<Integer>(0);
        this.featureNames = new ArrayList<String>(0);
    }

    Data(Data d) {
        this.numberOfPoints = d.numberOfPoints;
        this.numberOfFeatures = d.numberOfFeatures;
        this.points = new ArrayList<Point>(0);
        for (Point p : d.points) {
            this.points.add(p);
        }
        this.incompleteCases = new HashSet<Integer>(0);
        for (Integer i : d.incompleteCases) {
            this.incompleteCases.add(i);
        }
        this.featureNames = d.featureNames;
        this.hasFeatureNames = d.hasFeatureNames;
    }

    Data(int noP) {
        this.numberOfPoints = noP;
        this.points = new ArrayList<Point>(noP);
        this.incompleteCases = new HashSet<Integer>(0);
        this.featureNames = new ArrayList<String>(0);
    }

    Data(int noP, int noD) {
        this.numberOfPoints = noP;
        this.numberOfFeatures = noD;
        this.points = new ArrayList<Point>(noP);
        this.incompleteCases = new HashSet<Integer>(0);
        this.featureNames = new ArrayList<String>(0);
    }

    Data(ArrayList<Point> p, HashSet<Integer> incompleteCases) {
        this.points = p;
        this.incompleteCases = incompleteCases;
        this.numberOfPoints = this.points.size();
        this.numberOfFeatures = this.points.get(0).getNumberOfFeatures();
        this.featureNames = new ArrayList<String>(0);
    }

    Data(ArrayList<Point> p, HashSet<Integer> incompleteCases, ArrayList<String> featureNames) {
        this.points = p;
        this.incompleteCases = incompleteCases;
        this.numberOfPoints = this.points.size();
        this.numberOfFeatures = this.points.get(0).getNumberOfFeatures();
        this.featureNames = featureNames;
        this.hasFeatureNames = true;
    }

    protected String getColName(int index) {
        return featureNames.get(index);
    }

    protected void setColNames(ArrayList<String> colNames) {
        this.featureNames = colNames;
    }

    public void setNumberOfFeatures(int numberOfFeatures) {
        this.numberOfFeatures = numberOfFeatures;
    }

    public void setColNames(String[] colNames) {
        this.featureNames = new ArrayList<String>(Arrays.asList(colNames));
    }

    protected void setColName(String colname, int index) {
        this.featureNames.set(index, colname);
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    protected void setPointAtIndex(Point p, int index) {
        this.points.set(index, p);
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    protected void removePoint(Point p) {
        this.points.remove(p);
    }

    protected void removePointAtIndex(int index) {
        this.points.remove(index);
        this.setNumberOfPoints(numberOfPoints - 1);
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    protected Boolean getIsColNames() {
        return hasFeatureNames;
    }

    public void setIsColNames(Boolean isColNames) {
        this.hasFeatureNames = isColNames;
    }

    public void addIncompleteCase(int incompleteCase) {
        this.incompleteCases.add(incompleteCase);
    }

    public void findCompleteCases() {
        for (int i = 0; i < getNumberOfPoints(); i++) {
            if (!getPoint(i).getNullFeatureIndices().isEmpty())
                this.addIncompleteCase(i);
        }
    }

    public boolean isPointAtIndexIncomplete(int index) {
        return this.incompleteCases.contains(index);
    }

    // For updation of centroid of a cluster
    Double verticalSubsetMeanForIndex(int index, Integer[] list) {
        Double mean = 0.0;
        for (int i : list) {
            if (!this.isPointAtIndexIncomplete(i))
                mean += this.getPoint(i).getFeature(index).doubleValue();
        }
        // return mean / (this.getNoP() - this.getIncompCases().size());
        return mean / (list.length);
    }

    Double verticalSubsetMeanForIndex(int index, Collection<Integer> list) {
        Double mean = 0.0;
        for (int i : list) {
            if (!this.isPointAtIndexIncomplete(i))
                mean += this.getPoint(i).getFeature(index).doubleValue();
        }
        // return mean / (this.getNoP() - this.getIncompCases().size());
        return mean / (list.size());
    }

    Double verticalMeanForIndex(int index) {
        Double mean = 0.0;
        for (int i = 0; i < numberOfPoints; i++) {
            if (!this.isPointAtIndexIncomplete(i))
                mean += this.getPoint(i).getFeature(index).doubleValue();
        }
        // return mean / (this.getNoP() - this.getIncompCases().size());
        return mean / (this.getNumberOfPoints());
    }

    // Update centroid of cluster
    Point mean(Integer[] list) {
        int noD = this.getPoint(0).getNumberOfFeatures();
        Point p = new Point(noD);
        for (int i = 0; i < noD; i++) {
            p.setFeature(i, verticalSubsetMeanForIndex(i, list));
        }
        return p;
    }

    public Point meanOfPointsList(Collection<Integer> list) {
        int numberOfFeatures = this.numberOfFeatures;
        Point p = new Point(numberOfFeatures);
        for (int i = 0; i < numberOfFeatures; i++) {
            p.setFeature(i, this.verticalSubsetMeanForIndex(i, list));
        }
        return p;
    }

    public Point meanOfAllPoints() {
        int noD = this.getPoint(0).getNumberOfFeatures();
        Point p = new Point(noD);
        for (int i = 0; i < noD; i++) {
            p.setFeature(i, verticalMeanForIndex(i));
        }
        return p;
    }

    public Point randomPoint() {
        int index = (int) (Math.random() * (this.numberOfPoints - 1));
        Point p = this.getPoint(index);
        return p;
    }

    public String toString() {
        String display = "";
        if (this.hasFeatureNames) {
            display += featureNames + "\n";
        }
        for (int i = 0; i < numberOfPoints; i++) {
            display += "Point " + (i + 1) + " : " + this.getPoint(i).toString() + "\n";
        }
        return display;
    }

    public void display() {
        if (this.hasFeatureNames) {
            System.out.println(featureNames + "\n");
        }
        for (int i = 0; i < numberOfPoints; i++) {
            System.out.println("Point " + (i + 1) + " : " + this.getPoint(i).toString() + "\n");
        }
    }

    public Data cleanByRemove(Data d) {
        Data newData = new Data(d);
        ArrayList<Integer> incompleteCaseList = new ArrayList<Integer>(this.incompleteCases);
        Collections.sort(incompleteCaseList, Collections.reverseOrder());
        for (int i : incompleteCaseList)
            newData.removePointAtIndex(i);
        newData.setIncompCases(new HashSet<Integer>(0));
        newData.findCompleteCases();
        return newData;
    }

    private void setIncompCases(HashSet<Integer> incompleteCases) {
        this.incompleteCases = incompleteCases;
    }

    public Data pointSubsetBySkipping(ArrayList<Integer> skipRows) {
        Data newData = new Data(this);
        HashSet<Integer> incompleteCases = this.incompleteCases;
        Collections.sort(skipRows, Collections.reverseOrder());
        for (int i : skipRows) {
            newData.removePointAtIndex(i);
            if (this.incompleteCases.contains(i))
                incompleteCases.remove(i);
        }
        newData.incompleteCases = incompleteCases;
        return newData;
    }

    public Data pointSubsetOfIndices(ArrayList<Integer> rowIndices) {
        Data newData = new Data();
        HashSet<Integer> incompleteCases = this.incompleteCases;
        for (int i : rowIndices) {
            newData.addPoint(this.getPoint(i));
            if (this.incompleteCases.contains(i))
                incompleteCases.remove(i);
        }
        newData.setNumberOfFeatures(this.numberOfFeatures);
        newData.setNumberOfPoints(rowIndices.size());
        newData.setColNames(this.featureNames);
        newData.setIsColNames(this.hasFeatureNames);
        return newData;
    }

    public Data pointSubsetBetweenIndices(int i1, int i2) {
        Data newD = new Data();
        for (int i = i1; i <= i2; i++) {
            newD.addPoint(this.getPoint(i));
        }
        newD.setNumberOfFeatures(this.numberOfFeatures);
        newD.setNumberOfPoints(i2 - i1 + 1);
        newD.findCompleteCases();
        newD.setColNames(this.featureNames);
        newD.setIsColNames(this.hasFeatureNames);
        return newD;
    }

    public HashSet<Integer> getIncompleteCases() {
        return this.incompleteCases;
    }
}
