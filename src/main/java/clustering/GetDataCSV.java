package clustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import dataStructures.Data;
import dataStructures.Point;

public class GetDataCSV {

    private String filePath;
    private Integer skipLines = 0;
    private String seperator = ",";
    private Integer header = 0;
    private boolean hasHeader = false;
    private boolean ignoreLabel = true;

    private static final Set<String> NULLS = new HashSet<String>(Arrays.asList(new String[] {
                    "null", "?"}));

    public GetDataCSV(String path) {
        this.filePath = path;
    }

    public GetDataCSV(String path, Integer skipLines) {
        this.filePath = path;
        this.skipLines = skipLines;
    }

    public GetDataCSV(String path, Integer skipLines, Integer header) {
        this.filePath = path;
        this.skipLines = skipLines;
        this.header = header - 1;
        this.hasHeader = true;
    }

    public Data getData() {

        BufferedReader br = null;
        String line = "";

        int count = this.skipLines;
        int hcount = 0;

        Data d = new Data();
        String cols[];

        try {
            br = new BufferedReader(new FileReader(this.filePath));
            while ((line = br.readLine()) != null) {
                if (count-- > 0) {
                    hcount++;
                    continue;
                } else {
                    if (hasHeader && hcount == header) {
                        // -1 To store the null value after the last comma
                        cols = line.split(seperator, -1);
                        d.setColNames(cols);
                        d.setIsColNames(true);
                    } else {
                        // -1 To store the null value after the last comma
                        String[] n = line.split(seperator, -1);

                        Point p = new Point(n.length);
                        for (int i = 0; i < n.length; i++) {
                            if (n[i] == null || NULLS.contains(n[i].toLowerCase())
                                            || n[i].length() == 0) {
                                p.setFeature(i, 0);
                                p.addNullAtIndex(i);
                            } else {
                                p.setFeature(i, NumberFormat.getInstance().parse(n[i]));
                            }
                        }
                        if (this.ignoreLabel)
                            p.setNumberOfFeatures(p.getNumberOfFeatures() - 1);
                        d.addPoint(p);
                    }
                }
                hcount++;
                if (count % 5000 == 0) {
                    System.out.println("Finished processing " + count + " lines...\n");
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        int noP = this.hasHeader ? (hcount - this.skipLines - 1) : (hcount - this.skipLines);
        d.setNumberOfPoints(noP);
        d.setNumberOfFeatures(d.getPoint(0).getNumberOfFeatures());
        d.findCompleteCases();
        return d;
    }

    public static void main(String[] args) {
        GetDataCSV getDataCSV = new GetDataCSV("data/seeds_dataset.txt", 0, 1);
        Data data = getDataCSV.getData();
        KMeansClustering clustering = new KMeansClustering(data, 3);
        // KMeans file has ways to change distance measures and stopping criteria
        // We can add more criteria by creating new constructors if necessary
        clustering.kMeans();
        clustering.displayPartitions();
    }
}
