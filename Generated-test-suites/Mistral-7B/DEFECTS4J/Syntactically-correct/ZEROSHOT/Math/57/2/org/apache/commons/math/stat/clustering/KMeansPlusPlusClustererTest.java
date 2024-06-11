package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link KMeansPlusPlusClusterer} class.
 */
public class KMeansPlusPlusClustererTest {

    private static final double EPSILON = 1e-9;
    private static final double DELTA = 1e-6;
    private static final int NUM_CLUSTERS = 3;
    private static final int MAX_ITERATIONS = 5;
    private List<Clusterable<Double>> points;
    private KMeansPlusPlusClusterer<Clusterable<Double>> clusterer;

    @Before
    public void setUp() {
        points = Arrays.asList(new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 0.0;
            }

            @Override
            public Double getY() {
                return 0.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                double x1 = getX();
                double x2 = other.getX();
                double y1 = getY();
                double y2 = other.getY();
                return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }

            @Override
            public String toString() {
                return String.format("(%f, %f)", getX(), getY());
            }
        }, new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 1.0;
            }

            @Override
            public Double getY() {
                return 1.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                double x1 = getX();
                double x2 = other.getX();
                double y1 = getY();
                double y2 = other.getY();
                return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }

            @Override
            public String toString() {
                return String.format("(%f, %f)", getX(), getY());
            }
        }, new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 2.0;
            }

            @Override
            public Double getY() {
                return 2.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                double x1 = getX();
                double x2 = other.getX();
                double y1 = getY();
                double y2 = other.getY();
                return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }

            @Override
            public String toString() {
                return String.format("(%f, %f)", getX(), getY());
            }
        }, new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 3.0;
            }

            @Override
            public Double getY() {
                return 3.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                double x1 = getX();
                double x2 = other.getX();
                double y1 = getY();
                double y2 = other.getY();
                return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }

            @Override
            public String toString() {
                return String.format("(%f, %f)", getX(), getY());
            }
        }, new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 4.0;
            }

            @Override
            public Double getY() {
                return 4.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                double x1 = getX();
                double x2 = other.getX();
                double y1 = getY();
                double y2 = other.getY();
                return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }

            @Override
            public String toString() {
                return String.format("(%f, %f)", getX(), getY());
            }
        });
        clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    /**
     * Tests that {@link KMeansPlusPlusClusterer} correctly initializes clusters.
     */
    @Test
    public void testChooseInitialCenters() {
        List<Cluster<Clusterable<Double>>> result = clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        assertNotNull(result);
        assertEquals(NUM_CLUSTERS, result.size());
        for (Cluster<Clusterable<Double>> cluster : result) {
            assertNotNull(cluster.getCenter());
        }
    }

    /**
     * Tests that {@link KMeansPlusPlusClusterer} correctly assigns points to clusters.
     */
    @Test
    public void testAssignPointsToClusters() {
        List<Cluster<Clusterable<Double>>> result = clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        for (Cluster<Clusterable<Double>> cluster : result) {
            assertEquals(1, cluster.getPoints().size());
            Clusterable<Double> point = cluster.getPoints().get(0);
            assertEquals(cluster, point.getCluster());
        }
    }

    /**
     * Tests that {@link KMeansPlusPlusClusterer} correctly updates cluster centers.
     */
    @Test
    public void testUpdateCenters() throws ConvergenceException {
        List<Cluster<Clusterable<Double>>> result = clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        List<Cluster<Clusterable<Double>>> newResult = new ArrayList<>();
        List<Clusterable<Double>> newPoints = new ArrayList<>();
        newPoints.addAll(points);

        // Remove one point from each initial cluster
        for (Cluster<Clusterable<Double>> cluster : result) {
            newPoints.remove(cluster.getPoints().get(0));
            newResult.add(new Cluster<>(null));
        }

        // Run the algorithm again to update centers
        result = clusterer.cluster(newPoints, NUM_CLUSTERS, MAX_ITERATIONS);

        // Check that new centers are different from initial ones
        for (Cluster<Clusterable<Double>> oldCluster : result) {
            Cluster<Clusterable<Double>> newCluster = newResult.get(Collections.indexOfList(result, oldCluster));
            Clusterable<Double> centerOld = oldCluster.getCenter();
            Clusterable<Double> centerNew = newCluster.getCenter();
            assertNotNull(centerOld);
            assertNotNull(centerNew);
            assertFalse(centerOld.equals(centerNew));
            double oldDistance = points.get(0).distanceFrom(centerOld);
            double newDistance = points.get(0).distanceFrom(centerNew);
            assertTrue(newDistance < oldDistance + EPSILON);
        }
    }

    /**
     * Tests that {@link KMeansPlusPlusClusterer} handles empty clusters correctly.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyClusters() {
        List<Cluster<Clusterable<Double>>> result = clusterer.cluster(Collections.singletonList(new Clusterable<Double>() {
            @Override
            public Double getX() {
                return 0.0;
            }

            @Override
            public Double getY() {
                return 0.0;
            }

            @Override
            public void setX(Double x) {
                // empty
            }

            @Override
            public void setY(Double y) {
                // empty
            }

            @Override
            public double distanceFrom(Clusterable<Double> other) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String toString() {
                return "Empty";
            }
        }), NUM_CLUSTERS, MAX_ITERATIONS);
    }

    /**
     * Tests that {@link KMeansPlusPlusClusterer} handles different empty strategy.
     */
    @Test
    public void testEmptyClustersStrategy() {
        List<Cluster<Clusterable<Double>>> result = clusterer.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        List<Cluster<Clusterable<Double>>> newResult = new ArrayList<>();
        List<Clusterable<Double>> newPoints = new ArrayList<>();
        newPoints.addAll(points);

        // Remove one point from the cluster with the largest number of points
        Cluster<Clusterable<Double>> largestPointsCluster = null;
        int largestPointsSize = 0;
        for (Cluster<Clusterable<Double>> cluster : result) {
            if (cluster.getPoints().size() > largestPointsSize) {
                largestPointsCluster = cluster;
                largestPointsSize = cluster.getPoints().size();
            }
            newResult.add(new Cluster<>(null));
        }

        // Run the algorithm again with a different empty strategy
        clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        result = clusterer.cluster(newPoints, NUM_CLUSTERS, MAX_ITERATIONS);

        // Check that the new empty cluster is chosen differently
        Cluster<Clusterable<Double>> newEmptyCluster = null;
        int newEmptyClusterSize = 0;
        for (Cluster<Clusterable<Double>> cluster : result) {
            if (cluster.getPoints().size() == 0 && (newEmptyCluster == null || cluster.getPoints().size() > newEmptyCluster.getPoints().size())) {
                newEmptyCluster = cluster;
                newEmptyClusterSize = cluster.getPoints().size();
            }
        }
        assertNotNull(newEmptyCluster);
        assertFalse(newEmptyCluster.equals(largestPointsCluster));
        assertEquals(0, newEmptyCluster.getPoints().size());
    }

}