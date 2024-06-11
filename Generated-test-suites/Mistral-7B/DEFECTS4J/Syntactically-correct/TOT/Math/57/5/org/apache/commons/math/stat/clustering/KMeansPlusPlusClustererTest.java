package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.data.Point2D;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 * Tests for the {@link KMeansPlusPlusClusterer} class.
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test the cluster method with 2 dimensions points.
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testCluster2D() throws NumberIsTooSmallException {
        final int k = 3;
        final int maxIterations = 10;
        List<Clusterable<Point2D>> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.1, 1.1));
        points.add(new Point2D(1.2, 1.2));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(2.1, 2.1));
        points.add(new Point2D(2.2, 2.2));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(3.1, 3.1));
        points.add(new Point2D(3.2, 3.2));

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, k, maxIterations);

        assertNotNull(clusters);
        assertEquals(k, clusters.size());

        for (Cluster<Point2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertEquals(k > 0 ? 1 : 0, cluster.getPoints().size());
        }
    }

    /**
     * Test the cluster method with 1 dimension points.
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testCluster1D() throws NumberIsTooSmallException {
        final int k = 3;
        final int maxIterations = 10;
        List<Clusterable<Number>> points = Arrays.asList(1.0, 1.1, 1.2, 2.0, 2.1, 2.2, 3.0, 3.1, 3.2);

        KMeansPlusPlusClusterer<Number> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters = clusterer.cluster(points, k, maxIterations);

        assertNotNull(clusters);
        assertEquals(k, clusters.size());

        for (Cluster<Number> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertNotNull(cluster.getPoints());
            assertEquals(k > 0 ? 1 : 0, cluster.getPoints().size());
        }

        for (Cluster<Number> cluster : clusters) {
            Number center = cluster.getCenter();
            Variance variance = new Variance();
            for (Clusterable<Number> point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(center));
            }
            double expectedVariance = variance.getResult();
            double tolerance = 0.001;
            double actualVariance = cluster.getCenter().distanceFrom(new Point2D(cluster.getPoints().get(0).doubleValue(), cluster.getPoints().get(0).doubleValue())).pow(2.0);
            assertEquals("The variance of cluster " + cluster.getIndex() + " should be around " + expectedVariance + " but was " + actualVariance, expectedVariance, actualVariance, tolerance);
        }
    }

    /**
     * Test the emptyClusterStrategy parameter
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testEmptyClusterStrategy() throws NumberIsTooSmallException {
        final int k = 3;
        final int maxIterations = 10;
        List<Clusterable<Number>> points = Arrays.asList(1.0, 1.1, 1.2, null);

        KMeansPlusPlusClusterer<Number> clusterer1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters1 = clusterer1.cluster(points, k, maxIterations);
        assertEquals(3, clusters1.size());

        KMeansPlusPlusClusterer<Number> clusterer2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Number>> clusters2 = clusterer2.cluster(points, k, maxIterations);
        assertEquals(2, clusters2.size());

        KMeansPlusPlusClusterer<Number> clusterer3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<Cluster<Number>> clusters3 = clusterer3.cluster(points, k, maxIterations);
        assertEquals(1, clusters3.size());
        assertNotNull(clusters3.get(0).getPoints());
        assertEquals(3, clusters3.get(0).getPoints().size());
    }

    /**
     * Test the maxIterations parameter
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testMaxIterations() throws NumberIsTooSmallException {
        final int k = 3;
        List<Clusterable<Number>> points = Arrays.asList(1.0, 1.1, 1.2, 2.0, 2.1, 2.2, 3.0, 3.1, 3.2);

        KMeansPlusPlusClusterer<Number> clusterer1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters1 = clusterer1.cluster(points, k, 3);
        assertEquals(3, clusters1.size());

        KMeansPlusPlusClusterer<Number> clusterer2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters2 = clusterer2.cluster(points, k, -1);
        assertEquals(3, clusters2.size());

        KMeansPlusPlusClusterer<Number> clusterer3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters3 = clusterer3.cluster(points, k, Integer.MAX_VALUE);
        assertEquals(3, clusters3.size());
    }

    /**
     * Test the k parameter
     * @throws NumberIsTooSmallException
     */
    @org.junit.Test
    public void testK() throws NumberIsTooSmallException {
        final int maxIterations = 10;
        List<Clusterable<Number>> points = Arrays.asList(1.0, 1.1, 1.2, 2.0, 2.1, 2.2, 3.0, 3.1, 3.2);

        KMeansPlusPlusClusterer<Number> clusterer1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters1 = clusterer1.cluster(points, 2, maxIterations);
        assertEquals(2, clusters1.size());

        KMeansPlusPlusClusterer<Number> clusterer2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters2 = clusterer2.cluster(points, 1, maxIterations);
        assertEquals(1, clusters2.size());

        KMeansPlusPlusClusterer<Number> clusterer3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Number>> clusters3 = clusterer3.cluster(points, 4, maxIterations);
        assertEquals(4, clusters3.size());
    }

}