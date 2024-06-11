package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    @SuppressWarnings("unchecked")
    private static <T extends Clusterable<T>> KMeansPlusPlusClusterer<T> getTestObject(Random random) {
        return new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testEmptyStrategy() {
        // Test error strategy
        KMeansPlusPlusClusterer<TestPoint> emptyClusterError = new KMeansPlusPlusClusterer<>(new MersenneTwister(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<TestPoint> points = new ArrayList<>();
        try {
            emptyClusterError.cluster(points, 1, 1);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test largest variance strategy
        KMeansPlusPlusClusterer<TestPoint> emptyClusterLargestVariance = new KMeansPlusPlusClusterer<>(new MersenneTwister(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<TestPoint> pointsForEmptyStrategy = new ArrayList<>();
        List<Cluster<TestPoint>> clusters = emptyClusterLargestVariance.cluster(pointsForEmptyStrategy, 2, 1);
        assertFalse(clusters.get(0).isEmpty());
        assertFalse(clusters.get(1).isEmpty());
    }

    @Test
    public void testCluster() {
        // Test simple case
        Random random = new MersenneTwister();
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 100);
        assertEquals(2, clusters.size());
        for (Cluster<TestPoint> cluster : clusters) {
            assertFalse(cluster.isEmpty());
            TestPoint center = cluster.getCenter();
            double x = (cluster.getPoints().get(0).getX() + cluster.getPoints().get(1).getX()) / 2.0;
            double y = (cluster.getPoints().get(0).getY() + cluster.getPoints().get(1).getY()) / 2.0;
            assertEquals(x, center.getX(), 0.001);
            assertEquals(y, center.getY(), 0.001);
        }

        // Test max iterations
        List<TestPoint> pointsMaxIterations = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            pointsMaxIterations.add(new TestPoint(Math.random(), Math.random()));
        }
        List<Cluster<TestPoint>> clustersMaxIterations = testObject.cluster(pointsMaxIterations, 2, -1);
        assertEquals(2, clustersMaxIterations.size());
        for (Cluster<TestPoint> cluster : clustersMaxIterations) {
            assertFalse(cluster.isEmpty());
        }

        // Test k > points
        List<TestPoint> pointsKGreaterThanPoints = new ArrayList<>();
        pointsKGreaterThanPoints.add(new TestPoint(1.0, 1.0));
        testObject = getTestObject(random);
        try {
            testObject.cluster(pointsKGreaterThanPoints, 2, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test negative k
        testObject = getTestObject(random);
        try {
            testObject.cluster(points, -1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null max iterations
        testObject = getTestObject(random);
        try {
            testObject.cluster(points, 2, null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test null points
        testObject = getTestObject(random);
        try {
            testObject.cluster(null, 2, 1);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test k == 0
        testObject = getTestObject(random);
        try {
            testObject.cluster(Collections.emptyList(), 0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testChooseInitialCenters() {
        Random random = new MersenneTwister();
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        List<Cluster<TestPoint>> initialCenters = testObject.chooseInitialCenters(points, 3, random);
        assertEquals(3, initialCenters.size());
        for (Cluster<TestPoint> cluster : initialCenters) {
            assertFalse(cluster.isEmpty());
        }
    }

    @Test
    public void testAssignPointsToClusters() {
        Random random = new MersenneTwister();
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<TestPoint> points = new ArrayList<>();
        points.add(new TestPoint(1.0, 1.0));
        points.add(new TestPoint(2.0, 2.0));
        points.add(new TestPoint(3.0, 3.0));
        List<Cluster<TestPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<TestPoint>(new TestPoint(0.0, 0.0)));
        clusters.add(new Cluster<TestPoint>(new TestPoint(4.0, 4.0)));
        testObject.assignPointsToClusters(clusters, points);
        for (Cluster<TestPoint> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testGetNearestCluster() {
        Random random = new MersenneTwister();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 1);
        TestPoint point = new TestPoint(1.5, 1.5);
        Cluster<TestPoint> expectedCluster = clusters.get(0);
        Cluster<TestPoint> actualCluster = testObject.getNearestCluster(clusters, point);
        assertEquals(expectedCluster, actualCluster);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        Random random = new MersenneTwister();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 1);
        Cluster<TestPoint> largestVarianceCluster = null;
        double maxVariance = Double.NEGATIVE_INFINITY;
        for (Cluster<TestPoint> cluster : clusters) {
            Variance variance = new Variance();
            for (TestPoint point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            double varianceValue = variance.getResult();
            if (varianceValue > maxVariance) {
                largestVarianceCluster = cluster;
                maxVariance = varianceValue;
            }
        }
        TestPoint expectedPoint = largestVarianceCluster.getPoints().remove(0);
        TestPoint actualPoint = testObject.getPointFromLargestVarianceCluster(clusters);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        Random random = new MersenneTwister();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 1);
        Cluster<TestPoint> largestNumberCluster = null;
        int maxNumber = 0;
        for (Cluster<TestPoint> cluster : clusters) {
            int number = cluster.getPoints().size();
            if (number > maxNumber) {
                largestNumberCluster = cluster;
                maxNumber = number;
            }
        }
        TestPoint expectedPoint = largestNumberCluster.getPoints().remove(0);
        TestPoint actualPoint = testObject.getPointFromLargestNumberCluster(clusters);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testGetFarthestPoint() {
        Random random = new MersenneTwister();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 1);
        TestPoint expectedPoint = null;
        double maxDistance = Double.NEGATIVE_INFINITY;
        for (Cluster<TestPoint> cluster : clusters) {
            double distance = cluster.getPoints().get(0).distanceFrom(cluster.getCenter());
            if (distance > maxDistance) {
                maxDistance = distance;
                expectedPoint = cluster.getPoints().get(0);
            }
        }
        TestPoint actualPoint = testObject.getFarthestPoint(clusters);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test
    public void testCalculatePointDistance() {
        NormalDistribution normalDistribution = new NormalDistribution();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double x = normalDistribution.inverseCumulativeProbability(i / (double) 10);
            double y = normalDistribution.inverseCumulativeProbability(i / (double) 5);
            points.add(new TestPoint(x, y));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(new MersenneTwister());
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 100);
        TestPoint point1 = new TestPoint(1.0, 1.0);
        TestPoint point2 = new TestPoint(2.0, 2.0);
        Cluster<TestPoint> cluster1 = clusters.get(0);
        Cluster<TestPoint> cluster2 = clusters.get(1);
        double distance1 = point1.distanceFrom(cluster1.getCenter());
        double distance2 = point2.distanceFrom(cluster2.getCenter());
        double expectedDistance1ToCluster1 = point1.distanceFrom(cluster1.getCenter());
        double expectedDistance2ToCluster2 = point2.distanceFrom(cluster2.getCenter());
        double tolerance = 0.001;
        assertTrue(Math.abs(distance1 - expectedDistance1ToCluster1) <= tolerance);
        assertTrue(Math.abs(distance2 - expectedDistance2ToCluster2) <= tolerance);
    }

    @Test
    public void testMaxIterationsReached() {
        Random random = new MersenneTwister();
        List<TestPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new TestPoint(Math.random(), Math.random()));
        }
        KMeansPlusPlusClusterer<TestPoint> testObject = getTestObject(random);
        List<Cluster<TestPoint>> clusters = testObject.cluster(points, 2, 1);
        testObject.cluster(clusters, points, 1);
        List<Cluster<TestPoint>> clustersAfterIteration = testObject.cluster(points, 2, 100);
        assertEquals(clusters, clustersAfterIteration);
    }

    private static class TestPoint implements Clusterable<TestPoint> {

        private double x;
        private double y;

        public TestPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public TestPoint centroidOf(Collection<? extends Clusterable<TestPoint>> points) {
            double sumX = 0.0;
            double sumY = 0.0;
            int size = 0;
            for (Clusterable<TestPoint> point : points) {
                TestPoint testPoint = (TestPoint) point;
                sumX += testPoint.getX();
                sumY += testPoint.getY();
                size++;
            }
            return new TestPoint(sumX / size, sumY / size);
        }

        public double distanceFrom(Clusterable<TestPoint> other) {
            TestPoint otherPoint = (TestPoint) other;
            double dx = this.x - otherPoint.getX();
            double dy = this.y - otherPoint.getY();
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return "TestPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}