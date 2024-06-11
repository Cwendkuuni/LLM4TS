package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double EPSILON = 1e-6;

    private KMeansPlusPlusClusterer<T> clusteringAlgorithm;
    private List<T> points;
    private int k;

    @Parameterized.Parameters(name = "k = {0}")
    public static Collection<Object[]> data() {
        Random random = new Random();
        return Arrays.asList(new Object[][]{
                {new RandomPointGenerator().nextPoints(100, 2), 3},
                {new RandomPointGenerator().nextPoints(1000, 3), 5},
                {new RandomPointGenerator().nextPoints(10000, 10), 15}
        });
    }

    public KMeansPlusPlusClustererTest(List<T> points, int k) {
        this.points = points;
        this.k = k;
    }

    @Before
    public void setUp() {
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(RandomUtils.getPrng(), EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testCluster() {
        List<Cluster<T>> result = clusteringAlgorithm.cluster(points, k, 100);
        assertEquals(k, result.size());
        for (Cluster<T> cluster : result) {
            assertEquals(0, cluster.getPoints().size(), "Cluster should not be empty.");
            assertEquals(points.size() / k, cluster.getSize(), "Size of each cluster should be equal.");
        }

        for (T point : points) {
            Cluster<T> cluster = getNearestCluster(result, point);
            assertEquals(point, cluster.getCenter(), EPSILON, "Each point should be assigned to the nearest cluster.");
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyCluster() {
        List<T> emptyPoints = new ArrayList<>();
        Cluster<T> emptyCluster = new Cluster<>(new RandomPointGenerator().nextPoint(2));
        emptyPoints.add(emptyCluster);
        clusteringAlgorithm.cluster(emptyPoints, 1, 10);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidNumberOfClusters() {
        clusteringAlgorithm.cluster(points, 0, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfPoints() {
        clusteringAlgorithm.cluster(null, k, 10);
    }

    @Test
    public void testRandomInitialCenters() {
        List<Cluster<T>> result = clusteringAlgorithm.cluster(points, k, 10);
        List<Cluster<T>> result2 = clusteringAlgorithm.cluster(points, k, 100);
        List<Cluster<T>> result3 = clusteringAlgorithm.cluster(points, k, -1);
        assertEquals(result, result2);
        assertEquals(result, result3);
    }

    @Test
    public void testEmptyClusterStrategy() {
        KMeansPlusPlusClusterer<T> clusteringAlgorithmFarthest = new KMeansPlusPlusClusterer<>(RandomUtils.getPrng(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<T>> result = clusteringAlgorithmFarthest.cluster(points, k, 10);
        for (Cluster<T> cluster : result) {
            if (cluster.getPoints().isEmpty()) {
                Cluster<T> emptyCluster = cluster;
                assertEquals(EmptyClusterStrategy.FARTHEST_POINT, emptyCluster.getEmptyClusterStrategy());
            }
        }
    }

    private Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                nearestCluster = cluster;
                minDistance = distance;
            }
        }
        return nearestCluster;
    }

}

class RandomPointGenerator {
    private Random random = new Random();

    public List<T> nextPoints(int size, int dimensions) {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            points.add(new RandomPoint(dimensions, random));
        }
        return points;
    }

    public T nextPoint(int dimensions) {
        double[] coordinates = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            coordinates[i] = random.nextDouble();
        }
        return new RandomPoint(coordinates);
    }
}

class RandomPoint<T extends Clusterable<T>> implements Clusterable<T> {
    private double[] coordinates;

    public RandomPoint(int dimensions, Random random) {
        this.coordinates = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            this.coordinates[i] = random.nextDouble();
        }
    }

    public RandomPoint(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public double distanceFrom(Clusterable<T> other) {
        double distance = 0;
        for (int i = 0; i < coordinates.length; i++) {
            distance += Precision.pow(Precision.pow(coordinates[i] - other.getCoordinates()[i], 2), 0.5);
        }
        return Math.sqrt(distance);
    }

    @Override
    public double[] getCoordinates() {
        return coordinates;
    }
}