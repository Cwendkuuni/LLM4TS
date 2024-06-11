package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for KMeansPlusPlusClusterer class
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test clustering using 2 points
     * @throws Exception
     */
    @Test
    public void testCluster2Points() throws Exception {
        final double[] testData = {1.0, 1.5};

        final int k = 2;
        final int maxIterations = 10;

        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>((Random) new NormalDistribution(0, 1));
        final List<Cluster<Point2D>> clusters = clusterer.cluster(Arrays.asList(new Point2D(testData[0], testData[0]), new Point2D(testData[1], testData[1])), k, maxIterations);

        assertEquals(k, clusters.size());

        final Cluster<Point2D> cluster1 = clusters.get(0);
        final Cluster<Point2D> cluster2 = clusters.get(1);

        assertEquals(1, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());

        final Point2D expectedPoint1 = new Point2D(testData[0], testData[0]);
        final Point2D expectedPoint2 = new Point2D(testData[1], testData[1]);

        final Point2D point1 = (Point2D) cluster1.getPoints().get(0);
        final Point2D point2 = (Point2D) cluster2.getPoints().get(0);

        assertEquals(expectedPoint1, point1);
        assertEquals(expectedPoint2, point2);

        final double expectedCentroid1X = testData[0];
        final double expectedCentroid1Y = testData[0];
        final double expectedCentroid2X = testData[1];
        final double expectedCentroid2Y = testData[1];

        final Clusterable<Point2D> centroid1 = cluster1.getCenter();
        final Clusterable<Point2D> centroid2 = cluster2.getCenter();

        assertEquals(expectedCentroid1X, Precision.round(centroid1.getX(), 10), 0.0);
        assertEquals(expectedCentroid1Y, Precision.round(centroid1.getY(), 10), 0.0);
        assertEquals(expectedCentroid2X, Precision.round(centroid2.getX(), 10), 0.0);
        assertEquals(expectedCentroid2Y, Precision.round(centroid2.getY(), 10), 0.0);
    }

    /**
     * Test clustering using 3 points
     * @throws Exception
     */
    @Test
    public void testCluster3Points() throws Exception {
        final double[] testData = {1.0, 1.5, 2.0};

        final int k = 2;
        final int maxIterations = 10;

        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>((Random) new NormalDistribution(0, 1));
        final List<Cluster<Point2D>> clusters = clusterer.cluster(Arrays.asList(new Point2D(testData[0], testData[0]), new Point2D(testData[1], testData[1]), new Point2D(testData[2], testData[2])), k, maxIterations);

        assertEquals(k, clusters.size());

        final Cluster<Point2D> cluster1 = clusters.get(0);
        final Cluster<Point2D> cluster2 = clusters.get(1);

        assertEquals(1, cluster1.getPoints().size());
        assertEquals(2, cluster2.getPoints().size());

        final Point2D expectedPoint1 = new Point2D(testData[0], testData[0]);
        final List<Point2D> expectedPoints2 = Arrays.asList(new Point2D(testData[1], testData[1]), new Point2D(testData[2], testData[2]));

        final Point2D point1 = (Point2D) cluster1.getPoints().get(0);
        final List<Point2D> points2 = (List<Point2D>) cluster2.getPoints();

        assertEquals(expectedPoint1, point1);
        assertEquals(expectedPoints2, points2);

        final double expectedCentroid1X = testData[0];
        final double expectedCentroid1Y = testData[0];
        final double expectedCentroid2X = (testData[1] + testData[2]) / 2.0;
        final double expectedCentroid2Y = (testData[1] + testData[2]) / 2.0;

        final Clusterable<Point2D> centroid1 = cluster1.getCenter();
        final Clusterable<Point2D> centroid2 = cluster2.getCenter();

        assertEquals(expectedCentroid1X, Precision.round(centroid1.getX(), 10), 0.0);
        assertEquals(expectedCentroid1Y, Precision.round(centroid1.getY(), 10), 0.0);

        final Mean mean1 = new Mean();
        for (final Point2D point : cluster1.getPoints()) {
            mean1.increment(point.distanceFrom(centroid1));
        }
        final double variance1 = new Variance().inverse(mean1);
        assertEquals(variance1, Precision.round(cluster1.getVariance(), 10), 0.0);

        final Mean mean2 = new Mean();
        for (final Point2D point : cluster2.getPoints()) {
            mean2.increment(point.distanceFrom(centroid2));
        }
        final double variance2 = new Variance().inverse(mean2);
        assertEquals(variance2, Precision.round(cluster2.getVariance(), 10), 0.0);

        assertEquals(expectedCentroid2X, Precision.round(centroid2.getX(), 10), 0.0);
        assertEquals(expectedCentroid2Y, Precision.round(centroid2.getY(), 10), 0.0);
    }

    /**
     * Test clustering using 1000 points in 2D space
     * @throws Exception
     */
    @Test
    public void testCluster1000Points() throws Exception {
        final int nPoints = 1000;
        final int k = 10;
        final int maxIterations = 100;

        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>((Random) new NormalDistribution(0, 1));
        final List<Cluster<Point2D>> clusters = clusterer.cluster(Arrays.asList(new Point2D[nPoints]), k, maxIterations);

        assertEquals(k, clusters.size());

        for (final Cluster<Point2D> cluster : clusters) {
            assertNotNull(cluster);
            assertEquals(true, cluster.isDense());
            assertTrue(cluster.getPoints().size() > 0);
        }

        final double[][] testData = new double[nPoints][2];
        for (int i = 0; i < nPoints; i++) {
            testData[i][0] = Math.random();
            testData[i][1] = Math.random();
        }

        final List<Cluster<Point2D>> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(new Cluster<Point2D>(new Point2D(Math.random(), Math.random())));
        }

        final List<Cluster<Point2D>> expectedClusters = result;

        final List<Cluster<Point2D>> expectedAssignments = new ArrayList<>();
        for (final Point2D point : Arrays.asList(testData)) {
            Cluster<Point2D> nearestCluster = null;
            double nearestDistance = Double.MAX_VALUE;
            for (final Cluster<Point2D> cluster : expectedClusters) {
                final double distance = point.distanceFrom(cluster.getCenter());
                if (distance < nearestDistance) {
                    nearestCluster = cluster;
                    nearestDistance = distance;
                }
            }
            expectedAssignments.add(nearestCluster);
        }

        final List<Cluster<Point2D>> assignments = new ArrayList<>();
        for (final Cluster<Point2D> cluster : clusters) {
            assignments.add(cluster);
        }

        for (int i = 0; i < nPoints; i++) {
            final Cluster<Point2D> expectedCluster = expectedAssignments.get(i);
            final Cluster<Point2D> cluster = assignments.get(i);
            assertEquals(expectedCluster, cluster);
        }

        final double[][] expectedCentroids = new double[k][2];
        for (int i = 0; i < k; i++) {
            expectedCentroids[i][0] = Math.random();
            expectedCentroids[i][1] = Math.random();
        }

        for (int i = 0; i < k; i++) {
            final Cluster<Point2D> cluster = clusters.get(i);
            final Clusterable<Point2D> centroid = cluster.getCenter();
            assertEquals(expectedCentroids[i][0], centroid.getX(), 0.001);
            assertEquals(expectedCentroids[i][1], centroid.getY(), 0.001);
        }
    }

    /**
     * Test empty cluster handling strategy
     * @throws Exception
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterHandlingStrategy() throws Exception {
        final int nPoints = 10;
        final int k = 2;
        final int maxIterations = 100;

        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>((Random) new NormalDistribution(0, 1), EmptyClusterStrategy.ERROR);
        final List<Cluster<Point2D>> clusters = clusterer.cluster(Arrays.asList(new Point2D[nPoints]), k, maxIterations);

        assertEquals(k, clusters.size());

        for (final Cluster<Point2D> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            }
        }
    }

    /**
     * Test selection of initial centers using the largest number strategy
     * @throws Exception
     */
    @Test
    public void testInitializeWithLargestNumberStrategy() throws Exception {
        final int nPoints = 10;
        final int k = 3;
        final int maxIterations = 100;

        final KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>((Random) new NormalDistribution(0, 1), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        final List<Cluster<Point2D>> clusters = clusterer.cluster(Arrays.asList(new Point2D[nPoints]), k, maxIterations);

        assertEquals(k, clusters.size());

        int maxPoints = 0;
        Cluster<Point2D> maxCluster = null;
        for (final Cluster<Point2D> cluster : clusters) {
            if (cluster.getPoints().size() > maxPoints) {
                maxPoints = cluster.getPoints().size();
                maxCluster = cluster;
            }
        }

        assertNotNull(maxCluster);
        assertEquals(true, maxCluster.isDense());
        assertTrue(maxCluster.getPoints().size() > 0);
    }

}

class Point2D implements Clusterable<Point2D> {

    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double distanceFrom(Clusterable<Point2D> obj) {
        final Point2D point = (Point2D) obj;
        final double dx = this.x - point.x;
        final double dy = this.y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

}