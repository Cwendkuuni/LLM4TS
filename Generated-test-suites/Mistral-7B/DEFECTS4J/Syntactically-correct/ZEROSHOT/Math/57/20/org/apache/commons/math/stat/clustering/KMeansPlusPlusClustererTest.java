package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

/**
 * Test case for KMeansPlusPlusClusterer class.
 */
public class KMeansPlusPlusClustererTest {

    /** Number of points to test with. */
    private static final int NUM_POINTS = 10;

    /** Number of clusters to use. */
    private static final int NUM_CLUSTERS = 3;

    /** Seed for random number generation. */
    private static final long SEED = 123456789;

    /** Test data points. */
    private static final double[][] TEST_DATA = {
            {1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0},
            {6.0, 6.0}, {7.0, 7.0}, {8.0, 8.0}, {9.0, 9.0}, {10.0, 10.0}
    };

    /** Test strategy for handling empty clusters. */
    private static final EmptyClusterStrategy EMPTY_STRATEGY = EmptyClusterStrategy.LARGEST_VARIANCE;

    /** Test random number generator. */
    private static Random random;

    /** Test clusterer. */
    private static KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer;

    static {
        random = new Random(SEED);
        clusterer = new KMeansPlusPlusClusterer<>(random, EMPTY_STRATEGY);
    }

    /**
     * Tests initialization of clusterer with correct number of clusters and
     * points.
     */
    @Test
    public void testInit() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> clusters = clusterer.cluster(points, NUM_CLUSTERS, 1);
        assertEquals(NUM_CLUSTERS, clusters.size());
    }

    /**
     * Tests that initial clusters are chosen correctly.
     */
    @Test
    public void testChooseInitialCenters() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> initialCenters = clusterer.chooseInitialCenters(points, NUM_CLUSTERS, random);
        assertEquals(NUM_CLUSTERS, initialCenters.size());
        for (Cluster<ClusterablePoint2D> cluster : initialCenters) {
            assertNotNull(cluster.getCenter());
        }
    }

    /**
     * Tests that points are assigned to correct clusters.
     */
    @Test
    public void testAssignPointsToClusters() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> initialCenters = clusterer.chooseInitialCenters(points, NUM_CLUSTERS, random);
        List<Cluster<ClusterablePoint2D>> clusters = new ArrayList<>();
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            clusters.add(new Cluster<>(initialCenters.get(i).getCenter()));
        }
        assignPointsToClusters(clusters, points);
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(points.size(), cluster.getPoints().size());
            for (ClusterablePoint2D point : cluster.getPoints()) {
                assertEquals(cluster.getCenter(), point.getPosition());
            }
        }
    }

    /**
     * Tests that centroids are updated correctly.
     */
    @Test
    public void testUpdateCentroids() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> initialCenters = new ArrayList<>();
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            initialCenters.add(new Cluster<>(new double[]{i * 2.0, i * 2.0}));
        }
        List<Cluster<ClusterablePoint2D>> clusters = new ArrayList<>();
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            clusters.add(new Cluster<>(initialCenters.get(i).getCenter()));
        }
        assignPointsToClusters(clusters, points);
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            List<ClusterablePoint2D> clusterPoints = cluster.getPoints();
            double[] center = cluster.getCenter().getPosition();
            double sumX = 0.0;
            double sumY = 0.0;
            for (ClusterablePoint2D point : clusterPoints) {
                sumX += point.getX();
                sumY += point.getY();
            }
            double newX = sumX / clusterPoints.size();
            double newY = sumY / clusterPoints.size();
            cluster.setCenter(new ClusterablePoint2D(newX, newY));
        }
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(Precision.compareDoubles(cluster.getCenter().getX(), newX, 1e-10), 0);
            assertEquals(Precision.compareDoubles(cluster.getCenter().getY(), newY, 1e-10), 0);
        }
    }

    /**
     * Tests that empty cluster strategy works correctly.
     */
    @Test
    public void testEmptyClusterStrategy() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> initialCenters = new ArrayList<>();
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            initialCenters.add(new Cluster<>(new double[]{i * 2.0, i * 2.0}));
        }
        List<Cluster<ClusterablePoint2D>> clusters = new ArrayList<>();
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            clusters.add(new Cluster<>(initialCenters.get(i).getCenter()));
        }
        // Remove one point from a random cluster
        int index = random.nextInt(NUM_CLUSTERS);
        Cluster<ClusterablePoint2D> emptyCluster = clusters.get(index);
        ClusterablePoint2D removedPoint = emptyCluster.getPoints().remove(random.nextInt(emptyCluster.getPoints().size()));
        // Replace empty cluster
        switch (EMPTY_STRATEGY) {
            case LARGEST_VARIANCE:
                Cluster<ClusterablePoint2D> largestVarianceCluster = null;
                double largestVariance = Double.NEGATIVE_INFINITY;
                for (Cluster<ClusterablePoint2D> c : clusters) {
                    if (!c.getPoints().isEmpty()) {
                        Variance stat = new Variance();
                        for (ClusterablePoint2D point : c.getPoints()) {
                            stat.increment(point.distanceFrom(c.getCenter()));
                        }
                        double variance = stat.getResult();
                        if (variance > largestVariance) {
                            largestVarianceCluster = c;
                            largestVariance = variance;
                        }
                    }
                }
                ClusterablePoint2D newPoint = largestVarianceCluster.getPoints().get(random.nextInt(largestVarianceCluster.getPoints().size()));
                emptyCluster.setCenter(new ClusterablePoint2D(newPoint.getX(), newPoint.getY()));
                emptyCluster.addPoint(removedPoint);
                break;
            case LARGEST_POINTS_NUMBER:
                Cluster<ClusterablePoint2D> largestPointsCluster = null;
                int largestPointsNumber = Integer.MIN_VALUE;
                for (Cluster<ClusterablePoint2D> c : clusters) {
                    if (!c.getPoints().isEmpty()) {
                        if (c.getPoints().size() > largestPointsNumber) {
                            largestPointsCluster = c;
                            largestPointsNumber = c.getPoints().size();
                        }
                    }
                }
                ClusterablePoint2D newPoint2 = largestPointsCluster.getPoints().get(random.nextInt(largestPointsCluster.getPoints().size()));
                emptyCluster.setCenter(new ClusterablePoint2D(newPoint2.getX(), newPoint2.getY()));
                emptyCluster.addPoint(removedPoint);
                break;
            case FARTHEST_POINT:
                Cluster<ClusterablePoint2D> farthestCluster = null;
                double farthestDistance = Double.NEGATIVE_INFINITY;
                for (Cluster<ClusterablePoint2D> c : clusters) {
                    if (!c.getPoints().isEmpty()) {
                        ClusterablePoint2D farthestPoint = c.getPoints().get(0);
                        for (ClusterablePoint2D point : c.getPoints()) {
                            double distance = point.distanceFrom(c.getCenter());
                            if (distance > farthestDistance) {
                                farthestDistance = distance;
                                farthestCluster = c;
                                farthestPoint = point;
                            }
                        }
                        if (point.distanceFrom(c.getCenter()) != farthestDistance) {
                            throw new AssertionError("Farthest point and cluster center do not match.");
                        }
                    }
                }
                emptyCluster.setCenter(new ClusterablePoint2D(farthestPoint.getX(), farthestPoint.getY()));
                emptyCluster.addPoint(removedPoint);
                break;
            case ERROR:
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        // Points should still be distributed evenly among clusters
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(NUM_CLUSTERS != 1 ? points.size() / NUM_CLUSTERS : points.size(), cluster.getPoints().size());
        }
    }

    /**
     * Tests that getNearestCluster method works correctly.
     */
    @Test
    public void testGetNearestCluster() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (double[] data : TEST_DATA) {
            points.add(new ClusterablePoint2D(data[0], data[1]));
        }
        List<Cluster<ClusterablePoint2D>> initialCenters = clusterer.chooseInitialCenters(points, NUM_CLUSTERS, random);
        for (Cluster<ClusterablePoint2D> point : points) {
            Cluster<ClusterablePoint2D> nearestCluster = getNearestCluster(initialCenters, point);
            assertNotNull(nearestCluster);
            assertNotNull(nearestCluster.getCenter());
            assertEquals(point.getPosition()[0], nearestCluster.getCenter().getX(), 1e-10);
            assertEquals(point.getPosition()[1], nearestCluster.getCenter().getY(), 1e-10);
        }
    }

    /**
     * Helper method to get nearest cluster for a point.
     */
    private static Cluster<ClusterablePoint2D> getNearestCluster(List<Cluster<ClusterablePoint2D>> clusters, ClusterablePoint2D point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<ClusterablePoint2D> nearestCluster = null;
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

}

class ClusterablePoint2D implements Clusterable<ClusterablePoint2D> {

    /** X coordinate. */
    private double x;

    /** Y coordinate. */
    private double y;

    /**
     * Constructor.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public ClusterablePoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets X coordinate.
     *
     * @return X coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets Y coordinate.
     *
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets position as a double array.
     *
     * @return Position as a double array
     */
    public double[] getPosition() {
        return new double[]{x, y};
    }

    /**
     * Returns distance from given point.
     *
     * @param point point to compare distance to
     * @return distance from given point
     */
    public double distanceFrom(ClusterablePoint2D point) {
        return Math.sqrt(Math.pow(x - point.getX(), 2) + Math.pow(y - point.getY(), 2));
    }

}