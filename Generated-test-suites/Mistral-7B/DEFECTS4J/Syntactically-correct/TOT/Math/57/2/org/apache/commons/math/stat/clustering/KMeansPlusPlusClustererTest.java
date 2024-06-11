package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.apache.commons.math3.stat.random.MersenneTwister;
import org.apache.commons.math3.stat.statistics.Mean;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

/**
 * Test {@link KMeansPlusPlusClusterer} class
 */
public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 10;
    private static final int NUM_DIMENSIONS = 2;
    private static final int NUM_CLUSTERS = 3;

    private List<Clusterable<Double>> points;
    private KMeansPlusPlusClusterer<Clusterable<Double>> clustering;
    private Random random;

    @Before
    public void setUp() {
        points = new ArrayList<Clusterable<Double>>(NUM_POINTS);
        
        // Initialize random generator
        random = new MersenneTwister(0xABCDEF1234L);
        clustering = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, EmptyClusterStrategy.FARTHEST_POINT);
    }

    /**
     * Test initial clustering
     */
    @Test
    public void testInitialClustering() {
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new DummyPoint(i * NUM_DIMENSIONS, i * NUM_DIMENSIONS + 1));
        }

        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, NUM_CLUSTERS, -1);

        assertEquals(NUM_CLUSTERS, result.size());
        for (Cluster<Clusterable<Double>> cluster : result) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }
    }

    /**
     * Test clustering with empty strategy FARTHEST_POINT
     */
    @Test
    public void testEmptyStrategyFarthestPoint() {
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new DummyPoint(i * NUM_DIMENSIONS, i * NUM_DIMENSIONS + 1));
        }
        points.remove(5); // remove one point to simulate an empty cluster

        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, NUM_CLUSTERS, -1);

        assertEquals(NUM_CLUSTERS, result.size());
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : result) {
            if (cluster.getPoints().isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);
        assertNull(emptyCluster.getCenter());
        assertEquals(EmptyClusterStrategy.FARTHEST_POINT, clustering.getEmptyStrategy());

        // get the farthest point from the empty cluster and check its distance to the centroid
        Clusterable<Double> farthestPoint = emptyCluster.getPoints().remove(new UniformRealDistribution(0.0, 1.0).sample());
        Cluster<Clusterable<Double>> newCluster = new KMeansPlusPlusClusterer<Clusterable<Double>>(random, EmptyClusterStrategy.FARTHEST_POINT).cluster(Collections.singletonList(farthestPoint), 1, -1).get(0);
        double distance = new EuclideanDistance().distance(farthestPoint, newCluster.getCenter());
        double expectedDistance = new Mean(new StandardDeviation(new Variance(new EuclideanDistance())).inverseTransform(0.95), new EuclideanDistance()).distance(emptyCluster.getCenter(), newCluster.getCenter());
        assertEquals(expectedDistance, distance, 0.01);
    }

    /**
     * Test clustering with empty strategy LARGEST_VARIANCE
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyStrategyLargestVariance() {
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new DummyPoint(i * NUM_DIMENSIONS, i * NUM_DIMENSIONS + 1));
        }
        points.remove(5); // remove one point to simulate an empty cluster

        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, NUM_CLUSTERS, -1);
        clustering.setEmptyStrategy(EmptyClusterStrategy.LARGEST_VARIANCE);

        assertEquals(NUM_CLUSTERS, result.size());
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : result) {
            if (cluster.getPoints().isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);
        assertNull(emptyCluster.getCenter());

        // try to split the empty cluster
        clustering.cluster(Collections.singletonList(emptyCluster), 1, -1);
    }

    /**
     * Test clustering with empty strategy LARGEST_POINTS_NUMBER
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyStrategyLargestPointsNumber() {
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new DummyPoint(i * NUM_DIMENSIONS, i * NUM_DIMENSIONS + 1));
        }
        points.remove(5); // remove one point to simulate an empty cluster

        List<Cluster<Clusterable<Double>>> result = clustering.cluster(points, NUM_CLUSTERS, -1);
        clustering.setEmptyStrategy(EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

        assertEquals(NUM_CLUSTERS, result.size());
        Cluster<Clusterable<Double>> emptyCluster = null;
        for (Cluster<Clusterable<Double>> cluster : result) {
            if (cluster.getPoints().isEmpty()) {
                emptyCluster = cluster;
                break;
            }
        }
        assertNotNull(emptyCluster);
        assertNull(emptyCluster.getCenter());

        // try to split the empty cluster
        clustering.cluster(Collections.singletonList(emptyCluster), 1, -1);
    }

}

class DummyPoint implements Clusterable<Double> {

    private final double[] coords;

    public DummyPoint(double x, double y) {
        this.coords = new double[] {x, y};
    }

    @Override
    public double[] getCoordinates() {
        return coords;
    }

    @Override
    public double distanceFrom(Clusterable<?> other) {
        DummyPoint otherPoint = (DummyPoint) other;
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        return euclideanDistance.distance(this.getCoordinates(), otherPoint.getCoordinates());
    }

}