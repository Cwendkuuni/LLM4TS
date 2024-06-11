package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Tests for KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test clustering with 2 points
     */
    @Test
    public void testClusteringTwoPoints() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(1, cluster.getPoints().size());
            TestPoint point = cluster.getPoints().get(0);
            assertEquals(cluster.getCenter().getX(), point.getX(), 1e-9);
        }
    }

    /**
     * Test clustering with 3 points
     */
    @Test
    public void testClusteringThreePoints() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(1, cluster.getPoints().size());
            TestPoint point = cluster.getPoints().get(0);
            assertEquals(cluster.getCenter().getX(), point.getX(), 1e-9);
            if (cluster.getCenter().getX() < 2.0) {
                assertEquals(1.0, cluster.getCenter().getX(), 1e-9);
            } else {
                assertEquals(3.0, cluster.getCenter().getX(), 1e-9);
            }
        }
    }

    /**
     * Test clustering with 4 points and largest variance strategy
     */
    @Test
    public void testClusteringFourPointsLargestVariance() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0), new TestPoint(4.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(2, cluster.getPoints().size());
            double variance = 0;
            for (TestPoint point : cluster.getPoints()) {
                variance += Precision.pow(point.getX() - cluster.getCenter().getX(), 2);
            }
            Variance varianceCalc = new Variance();
            varianceCalc.increment(Precision.pow(clusters.get(0).getCenter().getX() - clusters.get(1).getCenter().getX(), 2));
            assertEquals(varianceCalc.getResult(), variance, 1e-9);
        }
    }

    /**
     * Test clustering with 4 points and largest number strategy
     */
    @Test
    public void testClusteringFourPointsLargestNumber() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0), new TestPoint(4.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(2, cluster.getPoints().size());
            if (cluster.getPoints().get(0).getX() > cluster.getPoints().get(1).getX()) {
                assertEquals(3.0, cluster.getPoints().get(0).getX(), 1e-9);
                assertEquals(4.0, cluster.getPoints().get(1).getX(), 1e-9);
            } else {
                assertEquals(1.0, cluster.getPoints().get(0).getX(), 1e-9);
                assertEquals(2.0, cluster.getPoints().get(1).getX(), 1e-9);
            }
        }
    }

    /**
     * Test clustering with 5 points and error strategy
     */
    @Test(expected = ConvergenceException.class)
    public void testClusteringFivePointsError() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0), new TestPoint(4.0), new TestPoint(5.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
    }

    /**
     * Test clustering with 5 points and farthest strategy
     */
    @Test
    public void testClusteringFivePointsFarthest() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0), new TestPoint(4.0), new TestPoint(5.0));
        List<Cluster<TestPoint>> clusters = clusterer.cluster(points, 2, 1);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            if (cluster.getCenter().getX() < 5.0) {
                assertEquals(4.0, cluster.getCenter().getX(), 1e-9);
                assertEquals(1, cluster.getPoints().size());
            } else {
                assertEquals(1.0, cluster.getCenter().getX(), 1e-9);
                assertEquals(1, cluster.getPoints().size());
            }
        }
    }

    /**
     * Test clustering with empty list
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusteringEmptyList() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<TestPoint> points = Collections.emptyList();
        clusterer.cluster(points, 1, 1);
    }

    /**
     * Test clustering with negative maxIterations
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusteringNegativeMaxIterations() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0));
        clusterer.cluster(points, 1, -1);
    }

    /**
     * Test clustering with number of points greater than maxIterations
     */
    @Test(expected = IllegalArgumentException.class)
    public void testClusteringMaxIterationsTooSmall() {
        KMeansPlusPlusClusterer<TestPoint> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<TestPoint> points = Arrays.asList(new TestPoint(1.0), new TestPoint(2.0), new TestPoint(3.0));
        clusterer.cluster(points, 3, 2);
    }

    /**
     * Test getNearestCluster with null clusters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNearestClusterNullClusters() {
        List<Cluster<TestPoint>> clusters = null;
        TestPoint point = new TestPoint(1.0);
        getNearestCluster(clusters, point);
    }

    /**
     * Test getNearestCluster with empty clusters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetNearestClusterEmptyClusters() {
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        TestPoint point = new TestPoint(1.0);
        getNearestCluster(clusters, point);
    }

    /**
     * Test getNearestCluster with single cluster
     */
    @Test
    public void testGetNearestClusterSingleCluster() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(new Cluster<TestPoint>(new TestPoint(1.0)));
        TestPoint point = new TestPoint(1.5);
        Cluster<TestPoint> nearestCluster = getNearestCluster(clusters, point);
        assertEquals(clusters.get(0), nearestCluster);
    }

    /**
     * Test getNearestCluster with multiple clusters
     */
    @Test
    public void testGetNearestClusterMultipleClusters() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(new Cluster<TestPoint>(new TestPoint(1.0)), new Cluster<TestPoint>(new TestPoint(2.5)));
        TestPoint point = new TestPoint(2.0);
        Cluster<TestPoint> nearestCluster = getNearestCluster(clusters, point);
        assertEquals(clusters.get(1), nearestCluster);
    }

    /**
     * Test getNearestCluster with points on boundary
     */
    @Test
    public void testGetNearestClusterBoundary() {
        List<Cluster<TestPoint>> clusters = Arrays.asList(new Cluster<TestPoint>(new TestPoint(1.0)), new Cluster<TestPoint>(new TestPoint(2.5)));
        TestPoint point = new TestPoint(1.5);
        Cluster<TestPoint> nearestCluster = getNearestCluster(clusters, point);
        assertEquals(clusters.get(0), nearestCluster);
    }

    /**
     * Utility method to get the nearest cluster
     * @param clusters the clusters to search
     * @param point the point to find the nearest cluster for
     * @return the nearest cluster
     */
    private static Cluster<TestPoint> getNearestCluster(List<Cluster<TestPoint>> clusters, TestPoint point) {
        Cluster<TestPoint> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<TestPoint> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    /**
     * Test point class
     */
    public static class TestPoint implements Clusterable<TestPoint> {

        private double x;

        public TestPoint(double x) {
            this.x = x;
        }

        public double getX() {
            return x;
        }

        public double distanceFrom(Clusterable<TestPoint> other) {
            TestPoint otherPoint = (TestPoint) other;
            return Math.pow(this.x - otherPoint.getX(), 2);
        }

    }

}