package dataStructures;

public class DistanceMeasure {
    public enum DistanceType {
        EUCLIDEAN, MANHATTAN, COSINE, JACCARD
    }

    private DistanceType type;

    public DistanceMeasure(DistanceType type) {
        this.type = type;
    }

    public Double distance(Point p1, Point p2) {
        switch (this.type) {
            case EUCLIDEAN:
                return this.eucDist(p1, p2);
            case MANHATTAN:
                return this.manhattanDist(p1, p2);
            case COSINE:
                return this.cosineSim(p1, p2);
            case JACCARD:
                return this.jaccardCoef(p1, p2);
            default:
                throw new IllegalArgumentException("Invalid distance metric.");
        }
    }

    private Double eucDist(Point p1, Point p2) {
        Double dist = 0.0;
        // null values are given value 0
        for (int i = 0; i < p1.getNumberOfFeatures(); i++) {
            Double diff = p1.getFeature(i).doubleValue() - p2.getFeature(i).doubleValue();
            dist += diff * diff;
        }
        return Math.sqrt(dist);
    }

    private Double manhattanDist(Point p1, Point p2) {
        Double dist = 0.0;
        // null values are given value 0
        for (int i = 0; i < p1.getNumberOfFeatures(); i++) {
            Double diff = Math.abs(p1.getFeature(i).doubleValue() - p2.getFeature(i).doubleValue());
            dist += diff;
        }
        return dist;
    }

    private Double cosineSim(Point p1, Point p2) {
        Double sim = 0.0;
        // null values are given value 0
        for (int i = 0; i < p1.getNumberOfFeatures(); i++) {
            Double dotProduct = p1.getFeature(i).doubleValue() * p2.getFeature(i).doubleValue();
            sim += dotProduct;
        }
        return sim / p1.magnitude() * p2.magnitude();
    }

    private Double jaccardCoef(Point p1, Point p2) {
        Double sim = 0.0;
        // null values are given value 0
        for (int i = 0; i < p1.getNumberOfFeatures(); i++) {
            Double dotProduct = p1.getFeature(i).doubleValue() * p2.getFeature(i).doubleValue();
            sim += dotProduct;
        }
        return sim
                        / (p1.magnitude() * p1.magnitude() + p2.magnitude() * p2.magnitude() - p1
                                        .magnitude() * p2.magnitude());
    }
}
