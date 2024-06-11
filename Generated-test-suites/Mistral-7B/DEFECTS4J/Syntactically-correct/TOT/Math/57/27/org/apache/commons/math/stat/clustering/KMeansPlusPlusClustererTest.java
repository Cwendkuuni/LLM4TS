import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    private static final int NUM_POINTS = 10;
    private static final int NUM_CLUSTERS = 3;
    private static final double EPSILON = 1e-6;
    private static final double MAX_ITERATIONS = 100;

    private Random random;
    private KMeansPlusPlusClusterer<ClusterablePoint> kmeans;

    @Before
    public void setup() {
        random = new Random();
        kmeans = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testEmptyClusterStrategy() {
        List<ClusterablePoint> points = new ArrayList<>();
        ClusterablePoint p1 = new ClusterablePoint(0, 0);
        ClusterablePoint p2 = new ClusterablePoint(5, 5);
        ClusterablePoint p3 = new ClusterablePoint(10, 10);
        points.add(p1);
        points.add(p2);
        points.add(p3);

        List<Cluster<ClusterablePoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, -1);
        Assert.assertEquals(3, clusters.size());
        Assert.assertTrue(clusters.get(0).getPoints().isEmpty());
        Assert.assertTrue(clusters.get(1).getPoints().isEmpty());
        Assert.assertTrue(clusters.get(2).getPoints().isEmpty());

        // Test empty cluster strategy
        try {
            kmeans.cluster(Collections.emptyList(), NUM_CLUSTERS, -1);
            Assert.fail("Should throw an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test all empty cluster strategies
        for (KMeansPlusPlusClusterer.EmptyClusterStrategy strategy : KMeansPlusPlusClusterer.EmptyClusterStrategy.values()) {
            kmeans = new KMeansPlusPlusClusterer<>(random, strategy);
            clusters = kmeans.cluster(points, NUM_CLUSTERS, -1);
            Assert.assertEquals(3, clusters.size());
            switch (strategy) {
                case LARGEST_VARIANCE:
                    Assert.assertTrue(clusters.get(0).getPoints().isEmpty());
                    Assert.assertTrue(clusters.get(1).getPoints().isEmpty());
                    Assert.assertTrue(clusters.get(2).getPoints().isEmpty());
                    break;
                case LARGEST_POINTS_NUMBER:
                    Assert.assertTrue(clusters.get(0).getPoints().size() > 0);
                    Assert.assertTrue(clusters.get(1).getPoints().size() > 0);
                    Assert.assertTrue(clusters.get(2).getPoints().isEmpty());
                    break;
                case FARTHEST_POINT:
                    Assert.assertTrue(clusters.get(0).getPoints().isEmpty());
                    Assert.assertTrue(clusters.get(1).getPoints().isEmpty());
                    Assert.assertTrue(clusters.get(2).getPoints().isEmpty());
                    break;
                case ERROR:
                    try {
                        kmeans.cluster(points, NUM_CLUSTERS, -1);
                        Assert.fail("Should throw an exception");
                    } catch (ConvergenceException e) {
                        // Expected
                    }
                    break;
            }
        }
    }

    @Test
    public void testSimpleCase() {
        List<ClusterablePoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new ClusterablePoint(x, y));
        }

        List<Cluster<ClusterablePoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
        Assert.assertEquals(NUM_CLUSTERS, clusters.size());
        for (Cluster<ClusterablePoint> cluster : clusters) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }

        // Check centroids
        for (Cluster<ClusterablePoint> cluster : clusters) {
            List<ClusterablePoint> pointsInCluster = cluster.getPoints();
            ClusterablePoint centroid = cluster.getCenter();
            Variance variance = new Variance();

            for (ClusterablePoint point : pointsInCluster) {
                variance.increment(point.distanceFrom(centroid));
            }

            double varianceValue = variance.getResult();
            double expectedVariance = varianceValue / pointsInCluster.size();
            Assert.assertEquals(expectedVariance, varianceValue, EPSILON);
        }

        // Check no points left in initial points list
        for (ClusterablePoint point : points) {
            boolean found = false;
            for (Cluster<ClusterablePoint> cluster : clusters) {
                if (cluster.getPoints().contains(point)) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testMaxIterationsReached() {
        List<ClusterablePoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new ClusterablePoint(x, y));
        }

        List<Cluster<ClusterablePoint>> clusters = kmeans.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS - 1);
        Assert.assertEquals(NUM_CLUSTERS, clusters.size());

        // This test case should throw an exception
        kmeans.cluster(points, NUM_CLUSTERS, MAX_ITERATIONS);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeNumberOfClusters() {
        List<ClusterablePoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new ClusterablePoint(x, y));
        }

        kmeans.cluster(points, -1, MAX_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollectionOfPoints() {
        kmeans.cluster(null, NUM_CLUSTERS, MAX_ITERATIONS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollectionOfClusters() {
        List<ClusterablePoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new ClusterablePoint(x, y));
        }

        kmeans.cluster(points, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfClusters() {
        List<ClusterablePoint> points = new ArrayList<>();
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new ClusterablePoint(x, y));
        }

        kmeans.cluster(points, NUM_CLUSTERS + 1, MAX_ITERATIONS);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEmptyCollectionOfPoints() {
        kmeans.cluster(Collections.emptyList(), NUM_CLUSTERS, MAX_ITERATIONS);
    }

}

class ClusterablePoint implements Clusterable<ClusterablePoint> {
    private double x;
    private double y;

    public ClusterablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(Clusterable<ClusterablePoint> other) {
        ClusterablePoint point = (ClusterablePoint) other;
        return Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}