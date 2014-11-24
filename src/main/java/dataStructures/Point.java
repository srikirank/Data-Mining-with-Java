package dataStructures;

import java.text.DecimalFormat;
import java.util.*;

import dataStructures.DistanceMeasure.DistanceType;

public class Point {
    private int numberOfFeatures;
    private Number[] featureArray;
    private ArrayList<Integer> nullFeatureIndices;

    public Point(Number[] featureArray, ArrayList<Integer> nullFeatureIndices) {
        this.featureArray = featureArray;
        numberOfFeatures = featureArray.length;
        this.nullFeatureIndices = nullFeatureIndices;
    }

    public Point(int num) {
        this.featureArray = new Number[num];
        this.numberOfFeatures = num;
        this.nullFeatureIndices = new ArrayList<Integer>(0);
    }

    public Point(Point p) {
        this.featureArray = new Number[p.featureArray.length];
        this.nullFeatureIndices = new ArrayList<Integer>(p.nullFeatureIndices.size());
        this.numberOfFeatures = p.numberOfFeatures;
        for (int i = 0; i < numberOfFeatures; i++)
            this.featureArray[i] = p.featureArray[i];
        for (Integer i : p.nullFeatureIndices)
            this.nullFeatureIndices.add(i.intValue());
    }

    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    public void setNumberOfFeatures(int numberOfFeatures) {
        this.numberOfFeatures = numberOfFeatures;
    }

    protected Number getFeature(int index) {
        return featureArray[index];
    }

    protected ArrayList<Integer> getNullFeatureIndices() {
        return nullFeatureIndices;
    }

    public void setFeature(int index, Number val) {
        this.featureArray[index] = val;
    }

    protected void setFeatureArray(Number[] coords) {
        this.featureArray = coords;
    }

    protected void setFeatureArray(ArrayList<Number> coords) {
        this.featureArray = coords.toArray(new Number[coords.size()]);
    }

    protected void setNulls(ArrayList<Integer> nullFeatureIndices) {
        this.nullFeatureIndices = nullFeatureIndices;
    }

    public Double magnitude() {
        DistanceMeasure dm = new DistanceMeasure(DistanceType.EUCLIDEAN);
        return dm.distance(this, this);
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        boolean isEqual = true;
        Point p = (Point) obj;
        for (int i = 0; i < numberOfFeatures; i++) {
            if (p.getFeature(i) != this.getFeature(i)) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        String display = "(";
        for (int i = 0; i < numberOfFeatures - 1; i++) {
            display += df.format(this.getFeature(i)) + ", ";
        }
        display += df.format(this.getFeature(numberOfFeatures - 1)) + ")";
        return display;
    }

    public void addNullAtIndex(int i) {
        this.nullFeatureIndices.add(i);
    }
}
