package clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import dataStructures.Data;
import dataStructures.DistanceMeasure;
import dataStructures.DistanceMeasure.DistanceType;
import dataStructures.Point;

public class KMeansClustering {
    public static enum StoppingCriterion {
        COST_OPTIMIZE, STABLE_CLUSTER, FIXED_ITERATIONS
    }

    Data data;
    int k;
    int numberOfIterations = 100;
    HashMap<Integer, TreeSet<Integer>> clusters = new HashMap<Integer, TreeSet<Integer>>();
    int[] partitionLabels;
    ArrayList<Double> costs;
    Point[] centroids;
    DistanceMeasure dm;
    StoppingCriterion criterion;

    public KMeansClustering(Data d, int k) {
        this.data = d;
        this.k = k;

        this.partitionLabels = new int[this.data.getNumberOfPoints()];
        this.costs = new ArrayList<Double>(numberOfIterations);
        Arrays.fill(partitionLabels, 0);
        this.centroids = new Point[k];
        this.dm = new DistanceMeasure(DistanceType.EUCLIDEAN);
        this.criterion = StoppingCriterion.FIXED_ITERATIONS;
    }

    public KMeansClustering(Data data, int k, StoppingCriterion criterion) {
        this.data = data;
        this.k = k;

        costs = new ArrayList<Double>(numberOfIterations);
        partitionLabels = new int[this.data.getNumberOfPoints()];
        Arrays.fill(partitionLabels, 0);
        centroids = new Point[k];
        this.dm = new DistanceMeasure(DistanceType.EUCLIDEAN);
        this.criterion = criterion;
    }

    public KMeansClustering(Data data, int k, int numberOfIterations) {
        this.data = data;
        this.k = k;
        this.numberOfIterations = numberOfIterations;

        costs = new ArrayList<Double>(numberOfIterations);
        partitionLabels = new int[this.data.getNumberOfPoints()];
        this.dm = new DistanceMeasure(DistanceType.EUCLIDEAN);
        centroids = new Point[k];
    }

    public KMeansClustering(Data data, int k, int numberOfIterations, StoppingCriterion criterion) {
        this.data = data;
        this.k = k;
        this.numberOfIterations = numberOfIterations;

        costs = new ArrayList<Double>(numberOfIterations);
        partitionLabels = new int[this.data.getNumberOfPoints()];
        centroids = new Point[k];
        this.dm = new DistanceMeasure(DistanceType.EUCLIDEAN);
        this.criterion = criterion;
    }

    public void kMeans() {
        if (this.criterion == StoppingCriterion.COST_OPTIMIZE)
            this.kMeansCostOptimize();
        else if (this.criterion == StoppingCriterion.STABLE_CLUSTER)
            this.kMeansUntilMarginalChange();
        else if (this.criterion == StoppingCriterion.FIXED_ITERATIONS)
            this.kMeansFixedIterations();
    }

    void kMeansUntilMarginalChange() {
        // Random centroids based on min and max of point co-ordinates
        // iterated until no considerable change (1%) in partition_labels;
        Point[] centroids = this.randomCentroids();
        HashMap<Integer, TreeSet<Integer>> currentClusters = null;
        HashMap<Integer, TreeSet<Integer>> prevClusters = new HashMap<Integer, TreeSet<Integer>>();
        Double distance = 0.0;

        // to hold the fraction of changed labels
        double fractionChange;
        int count = 0;

        do {
            count++;
            currentClusters = new HashMap<Integer, TreeSet<Integer>>();
            for (int i = 0; i < k; i++)
                currentClusters.put(i, new TreeSet<Integer>());

            for (int i = 0; i < data.getNumberOfPoints(); i++) {
                ArrayList<Double> distances = new ArrayList<Double>();
                for (int j = 0; j < this.k; j++) {
                    distance = this.dm.distance(data.getPoint(i), centroids[j]);
                    distances.add(distance);
                }
                distance = Collections.min(distances);

                int clusterKey = distances.indexOf(distance);
                TreeSet<Integer> clusterSet = currentClusters.get(clusterKey);
                clusterSet.add(i);
                currentClusters.put(clusterKey, clusterSet);
            }
            fractionChange = kMeansFracChange(prevClusters, currentClusters);
            for (int j = 0; j < k; j++) {
                if (returnPartition(j).size() > 0)
                    centroids[j] = data.meanOfPointsList(returnPartition(j));
            }
            prevClusters = currentClusters;
            System.out.println(fractionChange);
        } while (fractionChange > 0.01);
        this.clusters = currentClusters;
        this.centroids = centroids;
        System.out.println("Converged in " + count + " iterations");
    }

    private double getCost(int i) {
        return this.costs.get(i);
    }

    private void addCostFunction(Double cost) {
        this.costs.add(cost);
    }

    void kMeansCostOptimize() {
        Point[] centroids = new Point[k];

        Double distance = 0.0;
        Double cost = 0.0;

        for (int t = 0; t < numberOfIterations; t++) {
            centroids = this.randomCentroids();
            double fractionChange = 0.0;
            HashMap<Integer, TreeSet<Integer>> prevClusters =
                            new HashMap<Integer, TreeSet<Integer>>();
            HashMap<Integer, TreeSet<Integer>> currentClusters;
            do {
                cost = 0.0;
                currentClusters = new HashMap<Integer, TreeSet<Integer>>();

                for (int i = 0; i < k; i++)
                    currentClusters.put(i, new TreeSet<Integer>());

                for (int i = 0; i < data.getNumberOfPoints(); i++) {
                    ArrayList<Double> distances = new ArrayList<Double>();
                    for (int j = 0; j < this.k; j++) {
                        distance = this.dm.distance(data.getPoint(i), (centroids[j]));
                        distances.add(distance);
                    }
                    distance = Collections.min(distances);
                    cost += distance;

                    int clusterKey = distances.indexOf(distance);
                    TreeSet<Integer> clusterSet = currentClusters.get(clusterKey);
                    clusterSet.add(i);
                    currentClusters.put(clusterKey, clusterSet);
                }
                System.out.println("COST : " + cost);
                fractionChange = kMeansFracChange(prevClusters, currentClusters);
                for (int j = 0; j < this.k; j++) {
                    if (currentClusters.get(j).size() > 0)
                        centroids[j] = data.meanOfPointsList(currentClusters.get(j));
                }
                prevClusters = currentClusters;
            } while (fractionChange > 0.01);
            System.out.println("Clusters for iteration " + (t + 1));
            this.displayPartitions();
            System.out.println();
            this.addCostFunction(cost);
            if (t == 0 || this.getCost(t - 1) > this.getCost(t)) {
                this.clusters = currentClusters;
                this.centroids = centroids;
                System.out.println("--------------" + (t + 1));
            }
        }
        cost = Collections.min(this.costs);
        // this.fixLabels();
    }

    void kMeansFixedIterations() {
        Point[] centroids = this.randomCentroids();
        HashMap<Integer, TreeSet<Integer>> clusters = null;
        Double distance = 0.0;

        for (int t = 0; t < numberOfIterations; t++) {
            clusters = new HashMap<Integer, TreeSet<Integer>>();
            for (int i = 0; i < k; i++)
                clusters.put(i, new TreeSet<Integer>());

            for (int i = 0; i < data.getNumberOfPoints(); i++) {
                ArrayList<Double> distances = new ArrayList<Double>();
                for (int j = 0; j < this.k; j++) {
                    distance = this.dm.distance(data.getPoint(i), (centroids[j]));
                    distances.add(distance);
                }
                distance = Collections.min(distances);

                int clusterKey = distances.indexOf(distance);
                TreeSet<Integer> clusterSet = clusters.get(clusterKey);
                clusterSet.add(i);
                clusters.put(clusterKey, clusterSet);
            }
            for (int j = 0; j < this.k; j++) {
                if (clusters.get(j).size() > 0)
                    centroids[j] = data.meanOfPointsList(clusters.get(j));
            }
            this.clusters = clusters;
        }
        this.clusters = clusters;
        this.centroids = centroids;
    }

    double kMeansFracChange(int[] newArr, int[] oldArr) {
        int len = newArr.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (newArr[i] != oldArr[i])
                count++;
        }
        return (double) count / (double) len;
    }

    double kMeansFracChange(HashMap<Integer, TreeSet<Integer>> clusters1,
                    HashMap<Integer, TreeSet<Integer>> clusters2) {
        double change = 0.0;
        for (int i : clusters2.keySet()) {
            TreeSet<Integer> set1 = clusters1.get(i);
            TreeSet<Integer> set2 = clusters2.get(i);
            int total = set2.size();
            int remaining = set2.size();
            Iterator<Integer> itr = set2.iterator();
            while (set1 != null && itr.hasNext()) {
                if (set1.contains(itr.next()))
                    remaining -= 1;
            }
            if (total != 0)
                change += remaining / total;
        }
        return change / clusters2.size();
    }

    void centroidsRandom() {
        for (int i = 0; i < this.k; i++) {
            centroids[i] = this.data.randomPoint();
        }
    }

    Point[] randomCentroids() {
        Point[] centroids = new Point[k];
        for (int i = 0; i < this.k; i++) {
            centroids[i] = this.data.randomPoint();
        }
        return centroids;
    }

    public void displayPartitions() {
        for (int i = 0; i < k; i++) {
            System.out.println("Cluster " + (i + 1) + ": " + this.clusters.get(i));
        }
    }

    Double PPF(KMeansClustering c) {
        double ppf = 0.0;
        double distance = 0.0;
        int tp = 0;
        int fp = 0;
        Integer[] mappingTable = new Integer[k];
        for (int i = 0; i < k; i++) {
            ArrayList<Double> distances = new ArrayList<Double>(k);
            for (int j = 0; j < k; j++) {
                distance = this.dm.distance(c.getCentroids(i), this.getCentroids(j));
                distances.add(distance);
            }
            distance = Collections.min(distances);
            mappingTable[i] = distances.indexOf(distance);
        }
        for (int i = 0; i < partitionLabels.length; i++) {
            if (this.partitionLabels[i] == mappingTable[c.partitionLabels[i]]) {
                tp++;
            } else {
                fp++;
            }
        }
        ppf = ((double) tp / (double) (tp + fp));
        return ppf;
    }

    private Point getCentroids(int i) {
        return this.centroids[i];
    }

    ArrayList<Integer> returnPartition(int index) {
        ArrayList<Integer> cluster = new ArrayList<Integer>(0);
        int len = partitionLabels.length;
        for (Integer i = 0; i < len; i++) {
            if (partitionLabels[i] == index)
                cluster.add(i);
        }
        return cluster;
    }
}
