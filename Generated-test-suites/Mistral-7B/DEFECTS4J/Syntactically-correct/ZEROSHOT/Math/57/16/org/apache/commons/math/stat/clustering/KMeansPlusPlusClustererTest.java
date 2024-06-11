package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for KMeansPlusPlusClusterer.
 */
public class KMeansPlusPlusClustererTest {

    private static final int N = 10;
    private static final int K = 3;
    private static final double EPSILON = 1e-5;

    private Random random;
    private KMeansPlusPlusClusterer<Point2D> clustering;

    @Before
    public void setUp() {
        random = new Random();
        clustering = new KMeansPlusPlusClusterer<Point2D>(random);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)}.
     */
    @Test
    public void testCluster() {
        List<Point2D> points = new ArrayList<Point2D>(N);
        for (int i = 0; i < N; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }

        List<Cluster<Point2D>> result = clustering.cluster(points, K, 10);

        assertEquals(K, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        for (Point2D point : points) {
            Cluster<Point2D> cluster = getNearestCluster(result, point);
            assertTrue(cluster.getPoints().contains(point));
        }

        double[] variances = new double[K];
        for (int i = 0; i < K; i++) {
            Cluster<Point2D> cluster = result.get(i);
            Variance variance = new Variance();
            for (Point2D point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            variances[i] = variance.getResult();
        }

        double totalVariance = 0;
        for (double variance : variances) {
            totalVariance += variance;
        }

        double sumOfSquaredDistances = 0;
        for (Point2D point : points) {
            Cluster<Point2D> cluster = getNearestCluster(result, point);
            double distance = point.distanceFrom(cluster.getCenter());
            sumOfSquaredDistances += Math.pow(distance, 2);
        }

        double expectedTotalVariance = sumOfSquaredDistances / N;
        double delta = Math.abs(expectedTotalVariance - totalVariance) / expectedTotalVariance;
        assertTrue(delta < EPSILON);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with empty input.
     */
    @Test(expected = ConvergenceException.class)
    public void testClusterEmptyCollection() {
        List<Clusterable<Point2D>> points = Collections.emptyList();
        clustering.cluster(points, K, 10);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with zero number of clusters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterZeroClusters() {
        List<Point2D> points = new ArrayList<Point2D>(N);
        for (int i = 0; i < N; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        clustering.cluster(points, 0, 10);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#cluster(Collection, int, int)} with negative number of iterations.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusterNegativeMaxIterations() {
        List<Point2D> points = new ArrayList<Point2D>(N);
        for (int i = 0; i < N; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        clustering.cluster(points, K, -1);
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#chooseInitialCenters(Collection, int, Random)}.
     */
    @Test
    public void testChooseInitialCenters() {
        List<Point2D> points = new ArrayList<Point2D>(N);
        for (int i = 0; i < N; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        List<Cluster<Point2D>> result = clustering.chooseInitialCenters(points, K, random);

        assertEquals(K, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().isEmpty());
        }

        for (Point2D point : points) {
            boolean found = false;
            for (Cluster<Point2D> cluster : result) {
                if (cluster.getCenter().distanceFrom(point) == 0) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)} with empty collection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNearestClusterEmptyCollection() {
        List<Cluster<Point2D>> clusters = Collections.emptyList();
        getNearestCluster(clusters, new Point2D(0, 0));
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)} with null collection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNearestClusterNullCollection() {
        getNearestCluster(null, new Point2D(0, 0));
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)} with empty cluster.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNearestClusterEmptyCluster() {
        List<Cluster<Point2D>> clusters = new ArrayList<Cluster<Point2D>>();
        clusters.add(new Cluster<Point2D>(new Point2D(0, 0)));
        getNearestCluster(clusters, new Point2D(0, 0));
    }

    /**
     * Test method for {@link KMeansPlusPlusClusterer#getNearestCluster(Collection, Clusterable)}.
     */
    @Test
    public void testGetNearestCluster() {
        List<Cluster<Point2D>> clusters = new ArrayList<Cluster<Point2D>>();
        List<Point2D> points = new ArrayList<Point2D>(N);
        for (int i = 0; i < N; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
            clusters.add(new Cluster<Point2D>(new Point2D(Math.random(), Math.random())));
        }

        Cluster<Point2D> nearest = getNearestCluster(clusters, new Point2D(0, 0));
        Point2D nearestCenter = nearest.getCenter();
        double minDistance = Double.MAX_VALUE;

        for (Point2D point : points) {
            Cluster<Point2D> cluster = getNearestCluster(clusters, point);
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = cluster;
                nearestCenter = cluster.getCenter();
            }
        }

        assertEquals(nearestCenter, nearest.getCenter());
    }

    private List<Cluster<Point2D>> getRandomPoints(int n, int k) {
        List<Point2D> points = new ArrayList<Point2D>(n);
        for (int i = 0; i < n; i++) {
            points.add(new Point2D(Math.random(), Math.random()));
        }
        return clustering.cluster(points, k, 10);
    }

    private List<Cluster<Point2D>> getPointsWithThreeClusters() {
        List<Point2D> points = new ArrayList<Point2D>();
        points.add(new Point2D(0, 0));
        points.add(new Point2D(1, 1));
        points.add(new Point2D(2, 2));
        points.add(new Point2D(3, 3));
        points.add(new Point2D(4, 4));
        points.add(new Point2D(5, 5));
        points.add(new Point2D(6, 6));
        points.add(new Point2D(7, 7));
        points.add(new Point2D(8, 8));
        points.add(new Point2D(9, 9));
        return clustering.cluster(points, K, 10);
    }

    private Cluster<Point2D> getNearestCluster(List<Cluster<Point2D>> clusters, Clusterable<Point2D> point) {
        Cluster<Point2D> nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<Point2D> cluster : clusters) {
            double d = point.distanceFrom(cluster.getCenter());
            if (d < minDistance) {
                nearest = cluster;
                minDistance = d;
            }
        }
        return nearest;
    }

    private static class Point2D implements Clusterable<Point2D> {

        private final double x;
        private final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<Point2D> other) {
            Point2D otherPoint = (Point2D) other;
            return Math.sqrt(Math.pow(otherPoint.getX() - x, 2) + Math.pow(otherPoint.getY() - y, 2));
        }

        @Override
        public double distanceFrom(Point2D other) {
            return distanceFrom((Clusterable<Point2D>) other);
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }

}