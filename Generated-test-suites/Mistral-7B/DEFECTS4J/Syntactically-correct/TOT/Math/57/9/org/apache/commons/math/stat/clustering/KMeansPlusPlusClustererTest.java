package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.PointMassCluster;

/**
 * Tests for the KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    /** A random number generator used to create test data and initial centers */
    private final Random random = new Random();

    /** A list of test points */
    private List<Clusterable<Double>> testPoints;

    /** The number of clusters to create */
    private int numClusters;

    /** The maximum number of iterations to run */
    private int maxIterations;

    /** The strategy for handling empty clusters */
    private EmptyClusterStrategy emptyStrategy;

    /** The clusterer to test */
    private KMeansPlusPlusClusterer<PointMassCluster> clusterer;

    /**
     * Set up test data
     */
    @Before
    public void setUp() {
        // Create some test points
        testPoints = Arrays.asList(
                new PointMassCluster(0.1, 0.1),
                new PointMassCluster(1.1, 1.1),
                new PointMassCluster(2.1, 2.1),
                new PointMassCluster(3.1, 3.1),
                new PointMassCluster(4.1, 4.1)
        );

        // Set the number of clusters to create, the maximum number of iterations,
        // and the strategy for handling empty clusters
        numClusters = 3;
        maxIterations = 10;
        emptyStrategy = EmptyClusterStrategy.LARGEST_POINTS_NUMBER;

        // Create the clusterer
        clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
    }

    /**
     * Test that the algorithm correctly determines the initial centers when
     * given a small set of points
     */
    @Test
    public void testInitialCenters() {
        List<Cluster<PointMassCluster>> clusters = clusterer.cluster(testPoints, numClusters, maxIterations);
        assertEquals("Number of clusters", numClusters, clusters.size());

        // The first center should be a random point from the data set
        Cluster<PointMassCluster> firstCluster = clusters.get(0);
        assertFalse("First cluster should not be empty", firstCluster.isEmpty());
        assertEquals("First center should be a test point", testPoints.get(random.nextInt(testPoints.size())),
                     firstCluster.getCenter());

        // The remaining centers should be chosen based on the distances from the
        // data points to the existing centers
        for (int i = 1; i < numClusters; i++) {
            Cluster<PointMassCluster> cluster = clusters.get(i);
            assertFalse("Cluster should not be empty", cluster.isEmpty());
            List<PointMassCluster> points = cluster.getPoints();
            Cluster<PointMassCluster> nearestCluster = clusters.get(i - 1);
            PointMassCluster center = nearestCluster.getCenter();
            PointMassCluster point = points.get(0);
            double distance = point.distanceFrom(center);
            PointMassCluster expectedCenter = testPoints.get(random.nextInt(testPoints.size()));
            while (expectedCenter.equals(center) || points.contains(expectedCenter)) {
                expectedCenter = testPoints.get(random.nextInt(testPoints.size()));
            }
            assertNotSame("Unexpected center", center, expectedCenter);
            double actualDistance = point.distanceFrom(expectedCenter);
            assertEquals("Unexpected distance to center", distance, actualDistance, 0.001);
            assertEquals("Unexpected center", expectedCenter, cluster.getCenter());
        }
    }

    /**
     * Test that the algorithm correctly assigns points to the nearest cluster
     */
    @Test
    public void testAssignPoints() {
        List<Cluster<PointMassCluster>> clusters = clusterer.cluster(testPoints, numClusters, maxIterations);
        for (Cluster<PointMassCluster> cluster : clusters) {
            List<PointMassCluster> points = cluster.getPoints();
            for (PointMassCluster point : testPoints) {
                if (!points.contains(point)) {
                    Cluster<PointMassCluster> nearestCluster = clusterer.getNearestCluster(clusters, point);
                    assertEquals("Unexpected nearest cluster", cluster, nearestCluster);
                }
            }
        }
    }

    /**
     * Test that the algorithm converges when given a large set of points
     */
    @Test
    public void testConvergence() {
        List<Cluster<PointMassCluster>> oldClusters = null;
        for (int iter = 0; iter < maxIterations; iter++) {
            List<Cluster<PointMassCluster>> clusters = clusterer.cluster(testPoints, numClusters, maxIterations);
            // Check for convergence
            if (oldClusters != null) {
                for (int i = 0; i < numClusters; i++) {
                    Cluster<PointMassCluster> oldCluster = oldClusters.get(i);
                    Cluster<PointMassCluster> newCluster = clusters.get(i);
                    assertSame("Unexpected cluster", oldCluster, newCluster);
                    List<PointMassCluster> oldPoints = oldCluster.getPoints();
                    List<PointMassCluster> newPoints = newCluster.getPoints();
                    assertEquals("Unexpected number of points", oldPoints.size(), newPoints.size());
                    for (int j = 0; j < oldPoints.size(); j++) {
                        PointMassCluster oldPoint = oldPoints.get(j);
                        PointMassCluster newPoint = newPoints.get(j);
                        assertEquals("Unexpected point", oldPoint, newPoint);
                    }
                }
            }
            oldClusters = clusters;
        }
    }

    /**
     * Test that the algorithm correctly handles empty clusters
     */
    @Test
    public void testEmptyClusters() {
        // Create a list of test points that is split into two clusters
        List<Clusterable<Double>> points = Arrays.asList(
                new PointMassCluster(0.1, 0.1),
                new PointMassCluster(0.2, 0.2),
                new PointMassCluster(0.3, 0.3),
                new PointMassCluster(0.4, 0.4),
                new PointMassCluster(1.0, 1.0),
                new PointMassCluster(2.0, 2.0),
                new PointMassCluster(3.0, 3.0),
                new PointMassCluster(4.0, 4.0)
        );

        // Create a clusterer with the strategy to split empty clusters based on
        // the number of points they contain
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        // Run the algorithm on the test points, allowing it to converge
        List<Cluster<PointMassCluster>> clusters = clusterer.cluster(points, numClusters, maxIterations);

        // Check that there are no empty clusters
        for (Cluster<PointMassCluster> cluster : clusters) {
            assertTrue("Cluster should not be empty", !cluster.isEmpty());
        }

        // Remove the points from the first cluster and add them to the second
        List<PointMassCluster> firstClusterPoints = clusters.get(0).getPoints();
        List<PointMassCluster> secondClusterPoints = clusters.get(1).getPoints();
        List<PointMassCluster> firstClusterRemovedPoints = new ArrayList<>(firstClusterPoints.size());
        for (PointMassCluster point : firstClusterPoints) {
            firstClusterRemovedPoints.add(point);
            secondClusterPoints.add(point);
            firstClusterPoints.remove(point);
        }

        // Run the algorithm again, allowing it to converge
        List<Cluster<PointMassCluster>> newClusters = clusterer.cluster(points, numClusters, maxIterations);

        // Check that there are no empty clusters
        for (Cluster<PointMassCluster> cluster : newClusters) {
            assertTrue("Cluster should not be empty", !cluster.isEmpty());
        }

        // Check that the first cluster has the same points as before, now
        // assigned to the second cluster
        Cluster<PointMassCluster> firstCluster = clusters.get(0);
        Cluster<PointMassCluster> secondCluster = newClusters.get(1);
        List<PointMassCluster> firstClusterNewPoints = firstCluster.getPoints();
        List<PointMassCluster> secondClusterNewPoints = secondCluster.getPoints();
        for (PointMassCluster point : firstClusterRemovedPoints) {
            assertTrue("Unexpected point in first cluster", firstClusterNewPoints.contains(point));
            assertFalse("Unexpected point in second cluster", secondClusterNewPoints.contains(point));
        }

        // Check that the second cluster has the points that were removed from
        // the first cluster
        for (PointMassCluster point : firstClusterRemovedPoints) {
            assertTrue("Unexpected point in second cluster", secondClusterNewPoints.contains(point));
            assertFalse("Unexpected point in first cluster", firstClusterNewPoints.contains(point));
        }

        // Check that the first and second clusters have the same centers
        PointMassCluster firstClusterCenter = firstCluster.getCenter();
        PointMassCluster secondClusterCenter = secondCluster.getCenter();
        assertEquals("Unexpected centers of first and second clusters", firstClusterCenter, secondClusterCenter);
    }

    /**
     * Test that the algorithm throws an exception when given an empty set of points
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPoints() {
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(Collections.emptyList(), numClusters, maxIterations);
    }

    /**
     * Test that the algorithm throws an exception when given a negative number
     * of clusters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumClusters() {
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(testPoints, -1, maxIterations);
    }

    /**
     * Test that the algorithm throws an exception when given a negative maximum
     * number of iterations
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(testPoints, numClusters, -1);
    }

    /**
     * Test that the algorithm throws an exception when given a strategy for
     * handling empty clusters that is not recognized
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnknownEmptyClusterStrategy() {
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
    }

    /**
     * Test that the algorithm throws an exception when the number of points is
     * larger than the maximum possible number of double values
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testMaxNumberOfPoints() {
        List<Clusterable<Double>> points = new ArrayList<>();
        for (int i = 0; i < Long.MAX_VALUE; i++) {
            points.add(new PointMassCluster(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        }
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(points, numClusters, maxIterations);
    }

    /**
     * Test that the algorithm throws an exception when the number of iterations
     * exceeds the maximum possible number of iterations
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        List<Clusterable<Double>> points = Arrays.asList(new PointMassCluster(0.0, 0.0));
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(points, numClusters, Integer.MAX_VALUE + 1);
    }

    /**
     * Test that the algorithm throws an exception when given a point outside the
     * valid range
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangePoint() {
        List<Clusterable<Double>> points = Arrays.asList(new PointMassCluster(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        KMeansPlusPlusClusterer<PointMassCluster> clusterer = new KMeansPlusPlusClusterer<>(random, emptyStrategy);
        clusterer.cluster(points, numClusters, maxIterations);
    }

}