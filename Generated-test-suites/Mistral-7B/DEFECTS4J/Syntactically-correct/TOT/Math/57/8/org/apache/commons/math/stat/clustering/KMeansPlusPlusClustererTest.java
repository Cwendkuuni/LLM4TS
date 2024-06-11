package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final int numPoints;
    private final int k;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlusClustererTest(int numPoints, int k, EmptyClusterStrategy emptyStrategy) {
        this.numPoints = numPoints;
        this.k = k;
        this.emptyStrategy = emptyStrategy;
    }

    @Parameterized.Parameters(name = "test with {0} points and {1} clusters")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {10, 3, EmptyClusterStrategy.LARGEST_VARIANCE},
                        {15, 5, EmptyClusterStrategy.LARGEST_POINTS_NUMBER},
                        {5, 2, EmptyClusterStrategy.FARTHEST_POINT},
                        {20, 10, EmptyClusterStrategy.ERROR}
                }
        );
    }

    @Test
    public void testClustererCreation() {
        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        assertNotNull(clusterer);
    }

    @Test
    public void testEmptyClusterStrategy() {
        assertThrows(ConvergenceException.class, () -> new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR));
    }

    @Test
    public void testEmptyClusters() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterablePoint2D(i, i));
        }

        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        List<Cluster<ClusterablePoint2D>> clusters = clusterer.cluster(points, k, -1);

        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
        }

        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            if (cluster.getPoints().isEmpty()) {
                switch (emptyStrategy) {
                    case LARGEST_VARIANCE:
                        assertNotNull(getPointFromLargestVarianceCluster(clusters));
                        break;
                    case LARGEST_POINTS_NUMBER:
                        assertNotNull(getPointFromLargestNumberCluster(clusters));
                        break;
                    case FARTHEST_POINT:
                        assertNotNull(getFarthestPoint(clusters));
                        break;
                    case ERROR:
                        assertThrows(ConvergenceException.class, () -> getPointFromEmptyCluster(cluster));
                        break;
                }
            }
        }
    }

    @Test
    public void testKMeansPlusPlusClustering() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterablePoint2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        List<Cluster<ClusterablePoint2D>> clusters = clusterer.cluster(points, k, 10);

        assertEquals(k, clusters.size());
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertFalse(cluster.getPoints().isEmpty());
            assertEquals(Math.round(cluster.getCenter().getX()), Math.round(cluster.getCentroid().getX()), 0.01);
            assertEquals(Math.round(cluster.getCenter().getY()), Math.round(cluster.getCentroid().getY()), 0.01);
        }

        // test that all points belong to some cluster
        for (T point : points) {
            boolean found = false;
            for (Cluster<ClusterablePoint2D> cluster : clusters) {
                if (cluster.getPoints().contains(point)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

        // test that each point belong to the nearest cluster
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            List<ClusterablePoint2D> pointsInCluster = new ArrayList<>();
            pointsInCluster.addAll(cluster.getPoints());
            for (ClusterablePoint2D point : points) {
                if (!pointsInCluster.contains(point)) {
                    Cluster<ClusterablePoint2D> nearestCluster = getNearestCluster(clusters, point);
                    assertEquals(nearestCluster.getCenter(), cluster.getCenter());
                }
            }
        }
    }

    @Test
    public void testTooManyIterations() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterablePoint2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        assertThrows(NumberIsTooLargeException.class, () -> clusterer.cluster(points, k, Integer.MAX_VALUE));
    }

    @Test
    public void testNegativeIterations() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterablePoint2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        assertThrows(IllegalArgumentException.class, () -> clusterer.cluster(points, k, -1));
    }

    @Test
    public void testInvalidNumberOfClusters() {
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new ClusterablePoint2D(Math.random(), Math.random()));
        }

        KMeansPlusPlusClusterer<ClusterablePoint2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), emptyStrategy);
        assertThrows(IllegalArgumentException.class, () -> clusterer.cluster(points, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> clusterer.cluster(points, numPoints + 1, 10));
    }

    private T getPointFromEmptyCluster(Cluster<ClusterablePoint2D> emptyCluster) {
        throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
    }

    private ClusterablePoint2D getFarthestPoint(Collection<Cluster<ClusterablePoint2D>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        Cluster<ClusterablePoint2D> maxDistanceCluster = null;
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            List<ClusterablePoint2D> points = cluster.getPoints();
            for (ClusterablePoint2D point : points) {
                double distance = point.distanceFrom(cluster.getCenter());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    maxDistanceCluster = cluster;
                }
            }
        }
        return getFarthestPoint(maxDistanceCluster);
    }

    private ClusterablePoint2D getFarthestPoint(Cluster<ClusterablePoint2D> cluster) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        ClusterablePoint2D maxDistancePoint = null;
        for (ClusterablePoint2D point : cluster.getPoints()) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance > maxDistance) {
                maxDistance = distance;
                maxDistancePoint = point;
            }
        }
        return maxDistancePoint;
    }

    private Cluster<ClusterablePoint2D> getNearestCluster(Collection<Cluster<ClusterablePoint2D>> clusters, ClusterablePoint2D point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<ClusterablePoint2D> minDistanceCluster = null;
        for (Cluster<ClusterablePoint2D> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minDistanceCluster = cluster;
            }
        }
        return minDistanceCluster;
    }

}