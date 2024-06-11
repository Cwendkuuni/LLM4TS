import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test with default strategy (Largest Variance)
        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = getRandomPoints(11, 2);
        List<Cluster<Point2D>> clusters = clusteringAlgorithm.cluster(points, 3, 10);

        // Empty cluster should be handled correctly
        assertEquals(3, clusters.size());
        assertFalse(clusters.get(0).getPoints().isEmpty());
        assertFalse(clusters.get(1).getPoints().isEmpty());
        assertTrue(clusters.get(2).getPoints().isEmpty());

        // Test with different strategies
        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithmFarthest = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clustersFarthest = clusteringAlgorithmFarthest.cluster(points, 3, 10);

        // Empty cluster should be handled differently
        assertEquals(3, clustersFarthest.size());
        assertFalse(clustersFarthest.get(0).getPoints().isEmpty());
        assertFalse(clustersFarthest.get(1).getPoints().isEmpty());
        assertTrue(clustersFarthest.get(2).getPoints().isEmpty());

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithmLargestNumber = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Point2D>> clustersLargestNumber = clusteringAlgorithmLargestNumber.cluster(points, 3, 10);

        // Empty cluster should be handled differently
        assertEquals(3, clustersLargestNumber.size());
        assertFalse(clustersLargestNumber.get(0).getPoints().isEmpty());
        assertFalse(clustersLargestNumber.get(1).getPoints().isEmpty());
        assertTrue(clustersLargestNumber.get(2).getPoints().isEmpty());

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithmError = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        try {
            clusters = clusteringAlgorithmError.cluster(points, 3, 10);
            fail();
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testTriangleInequality() {
        // Test triangle inequality
        List<Point2D> points = getRandomPoints(3, 2);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point2D(0, 0)));
        clusters.add(new Cluster<>(new Point2D(1, 1)));

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusteringAlgorithm.cluster(points, 2, 10);

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                final Point2D point1 = clusters.get(i).getPoints().get(0);
                final Point2D point2 = clusters.get(j).getPoints().get(0);
                double distance = point1.distanceFrom(point2);
                double distanceFromCentroids = point1.distanceFrom(clusters.get(i).getCenter()) + point2.distanceFrom(clusters.get(j).getCenter());
                assertTrue(distance <= distanceFromCentroids);
            }
        }
    }

    @Test
    public void testCentroidsConvergence() {
        // Test centroids convergence
        List<Point2D> points = getRandomPoints(10, 2);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point2D(0, 0)));
        clusters.add(new Cluster<>(new Point2D(1, 1)));

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusteringAlgorithm.cluster(points, 2, 100);

        for (Cluster<Point2D> cluster : clusters) {
            Point2D center = cluster.getCenter();
            List<Point2D> pointsInCluster = cluster.getPoints();
            for (Point2D point : pointsInCluster) {
                double distance = point.distanceFrom(center);
                assertTrue(distance < 0.01);
            }
        }
    }

    @Test
    public void testClusterSize() {
        // Test cluster size
        List<Point2D> points = getRandomPoints(10, 2);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point2D(0, 0)));
        clusters.add(new Cluster<>(new Point2D(1, 1)));

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusteringAlgorithm.cluster(points, 2, 100);

        for (Cluster<Point2D> cluster : clusters) {
            int size = cluster.getPoints().size();
            assertEquals(5, size);
        }
    }

    @Test
    public void testClusterDistanceVariance() {
        // Test cluster distance variance
        List<Point2D> points = getRandomPoints(10, 2);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point2D(0, 0)));
        clusters.add(new Cluster<>(new Point2D(1, 1)));

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusteringAlgorithm.cluster(points, 2, 100);

        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            Variance variance = new Variance();
            for (Point2D point : cluster.getPoints()) {
                variance.increment(point.distanceFrom(cluster.getCenter()));
            }
            double result = variance.getResult();
            double expected = 0.0;
            for (Point2D point : cluster.getPoints()) {
                expected += Math.pow(point.distanceFrom(cluster.getCenter()), 2);
            }
            assertEquals(expected / cluster.getPoints().size(), result, 0.001);
        }
    }

    @Test
    public void testAssignPointsToCluster() {
        // Test assign points to cluster
        List<Point2D> points = getRandomPoints(10, 2);
        List<Cluster<Point2D>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new Point2D(0, 0)));
        clusters.add(new Cluster<>(new Point2D(1, 1)));

        KMeansPlusPlusClusterer<Point2D> clusteringAlgorithm = new KMeansPlusPlusClusterer<>(new Random());
        clusters = clusteringAlgorithm.cluster(points, 2, 100);

        List<Point2D> newPoints = getRandomPoints(5, 2);
        assignPointsToClusters(clusters, newPoints);

        for (Cluster<Point2D> cluster : clusters) {
            List<Point2D> pointsInCluster = cluster.getPoints();
            int size = pointsInCluster.size();
            assertEquals(5, size);
        }
    }

    @Test
    public void testChooseInitialCenters() {
        // Test choose initial centers
        List<Point2D> points = getRandomPoints(11, 2);
        List<Cluster<Point2D>> initialCenters = KMeansPlusPlusClusterer.chooseInitialCenters(points, 3, new Random());

        assertEquals(3, initialCenters.size());
        for (Cluster<Point2D> initialCenter : initialCenters) {
            List<Point2D> pointsInCluster = initialCenter.getPoints();
            int size = pointsInCluster.size();
            assertTrue(size == 1 || size == 0);
        }
    }

    private List<Point2D> getRandomPoints(int size, int dimensions) {
        List<Point2D> points = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            double[] point = new double[dimensions];
            for (int j = 0; j < dimensions; j++) {
                point[j] = random.nextGaussian() + random.nextGaussian();
            }
            points.add(new Point2D(point));
        }
        return points;
    }

    private void assignPointsToClusters(List<Cluster<Point2D>> clusters, List<Point2D> points) {
        for (Point2D point : points) {
            Cluster<Point2D> cluster = getNearestCluster(clusters, point);
            cluster.addPoint(point);
        }
    }

    private Cluster<Point2D> getNearestCluster(List<Cluster<Point2D>> clusters, Point2D point) {
        Cluster<Point2D> nearestCluster = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<Point2D> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

}