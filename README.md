Data-Mining-with-Java
=====================
This repo contains Data Mining algorithms and utilities written using Java.

Utlities
---------------------
1. <b>Point</b> - Generic container has been created to represent an n-dimensional numeric data point. The same data point may contain multiple data types on different features so long as all the data are numeric. Attribute positions with null values can be recorded in the container. This information can be used to replace the missing features or identify if the point is a complete case.
2. <b>Data</b> - Data are represented as collection of <i>'p'</i> n-dimensional points. Missing cases are recorded in the collection. Utilities to subset the data based on the number of the points and features have been written. Column-subsetting and row-subsetting implemented to make it flexible to deal with portions of the data points.
3. <b>Distances</b> - A generic class containing utilities to compute distances between two n-dimensional points has been implemented. The following distances and similarities were implemented: Euclidean, Manhattan distance, Cosine similarity, Jaccard Coefficient. Adding a new distance metric is just as easy since they can be added independent of other functionalities.
4. <b>CSV File reader</b> - A CSV file reader has been written which reads any text or csv file. Works for arbitrary field seperator, number of lines to be skipped and an optional header line containing column names. 

Algorithms
---------------
Clustering Algorithms
_______________________
1. <b>Kmeans</b> - 
<ul>
<li>The code works for any distance metric. All the distance metrics accept two n-dimensional data points and return a <i>double</i> distance. Any distance present in <i>Distances</i> utility is callable.</li>
<li>Two KMeans algorithms have been implemented.</li>
<li>The first of them is based on a stability criterion - The algorithm's threshold is the fraction of data change in clusters. In other words, a threshold is put on the amount of movement of data points between clusters. The cycle of assignment-updation of centroids would commence until less than 1\% of the points change the clusters. </li>
<li>Another implementation is based on cost-optimization. Clusters are picked randomly based on the data. Later, a user-defined number of iterations of the algorithm are run. For each run, we keep track of the sum of distances of all the points to their \emph{final-centroids}. Let us label this as \emph{cost}. Then, the run that resulted in least such cost is chosen to determine the centroids and their clusters.</li>
</ul>
