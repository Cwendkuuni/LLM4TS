package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<TestPoint> points = Arrays.asList(new TestPoint(0.0, 0.0), new TestPoint(1.0, 1.0), new TestPoint(2.0, 2.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 3, -1);
        assertEquals(3, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertTrue(cluster.getPoints().isEmpty());
        }
        try {
            clustering.cluster(Collections.emptyList(), 3, 1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            //expected exception
        }
    }

    @Test
    public void testKMeansPlusPlusClusterer() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<TestPoint> points = Arrays.asList(new TestPoint(0.0, 0.0), new TestPoint(1.0, 1.0), new TestPoint(2.0, 2.0),
                new TestPoint(3.0, 3.0), new TestPoint(4.0, 4.0), new TestPoint(5.0, 5.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 3, 100);
        assertEquals(3, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }

        double eps = 0.01;
        for (Cluster<TestPoint> cluster : clusters) {
            TestPoint centroid = cluster.getCenter();
            List<TestPoint> pointsInCluster = cluster.getPoints();
            for (TestPoint point : pointsInCluster) {
                double distance = point.distanceFrom(centroid);
                assertTrue(distance < eps);
            }
        }
    }

    @Test
    public void testLargestVarianceStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> points = Arrays.asList(new TestPoint(0.0, 0.0), new TestPoint(1.0, 1.0), new TestPoint(2.0, 2.0),
                new TestPoint(3.0, 3.0), new TestPoint(4.0, 4.0), new TestPoint(5.0, 5.0), new TestPoint(5.5, 5.5),
                new TestPoint(6.0, 6.0), new TestPoint(7.0, 7.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 3, 100);
        assertEquals(3, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            TestPoint centroid = cluster.getCenter();
            List<TestPoint> pointsInCluster = cluster.getPoints();
            double variance = new Variance().calculate(pointsInCluster);
            double expectedVariance = Math.pow(Math.sqrt(7.0 / 10.0), 2.0);
            assertEquals(expectedVariance, variance, 0.001);
        }
    }

    @Test
    public void testLargestPointsNumberStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<TestPoint> points = Arrays.asList(new TestPoint(0.0, 0.0), new TestPoint(1.0, 1.0), new TestPoint(2.0, 2.0),
                new TestPoint(3.0, 3.0), new TestPoint(4.0, 4.0), new TestPoint(5.0, 5.0), new TestPoint(5.5, 5.5),
                new TestPoint(6.0, 6.0), new TestPoint(7.0, 7.0), new TestPoint(8.0, 8.0), new TestPoint(9.0, 9.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 3, 100);
        assertEquals(3, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
            int size = cluster.getPoints().size();
            int expectedSize = 4;
            assertEquals(expectedSize, size);
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<TestPoint> clustering = new KMeansPlusPlusClusterer<>(random);
        List<TestPoint> points = Arrays.asList(new TestPoint(0.0, 0.0), new TestPoint(1.0, 1.0), new TestPoint(2.0, 2.0),
                new TestPoint(3.0, 3.0), new TestPoint(4.0, 4.0), new TestPoint(5.0, 5.0), new TestPoint(5.5, 5.5),
                new TestPoint(6.0, 6.0), new TestPoint(7.0, 7.0), new TestPoint(8.0, 8.0), new TestPoint(9.0, 9.0));
        List<Cluster<TestPoint>> clusters = clustering.cluster(points, 3, Integer.MAX_VALUE);
    }

    private static class TestPoint implements Clusterable<TestPoint> {
        private final double x;
        private final double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            TestPoint point = (TestPoint) other;
            return Math.sqrt(Math.pow(this.x - point.x, 2.0) + Math.pow(this.y - point.y, 2.0));
        }

        @Override
        public Cluster<TestPoint> getCenter() {
            return null;
        }

        public double x() {
            return x;
        }

        public double y() {
            return y;
        }
    }
}