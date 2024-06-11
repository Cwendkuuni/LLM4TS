import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final int numPoints;
    private final int numClusters;
    private final double tolerance;

    public KMeansPlusPlusClustererTest(int numPoints, int numClusters, double tolerance) {
        this.numPoints = numPoints;
        this.numClusters = numClusters;
        this.tolerance = tolerance;
    }

    @Parameterized.Parameters(name = "{index}: k={0} clusters, tolerance={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {10, 3, 0.1},
                {100, 10, 0.05},
                {1000, 5, 0.01}
        });
    }

    @Test
    public void testCluster() {
        List<T> points = createRandomPoints(numPoints);
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(points, numClusters, 50);
        assertEquals(numClusters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertNotNull(cluster.getCenter());
            assertEquals(Collections.min(points.size(), 1), cluster.getPoints().size());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumClusters() {
        List<T> points = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 1.0));
        new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(points, -2, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCollection() {
        new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(Collections.emptyList(), 2, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCollection() {
        new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(null, 2, 10);
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        List<T> points = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 1.0), new Point(2.0, 2.0));
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE).cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        Cluster<T> cluster1 = clusters.get(0);
        Cluster<T> cluster2 = clusters.get(1);
        assertEquals(1, cluster1.getPoints().size());
        assertEquals(1, cluster2.getPoints().size());
        T point1 = cluster1.getPoints().get(0);
        T point2 = cluster2.getPoints().get(0);
        double distance1 = point1.distanceFrom(cluster1.getCenter());
        double distance2 = point2.distanceFrom(cluster2.getCenter());
        double variance1 = new Variance().increment(distance1 * distance1).getResult();
        double variance2 = new Variance().increment(distance2 * distance2).getResult();
        assertEquals(Math.max(variance1, variance2), Precision.abs(new Variance().increment(sumOfSquaredDistances(points, clusters))
                .getResult()), tolerance);
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        List<T> points = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 1.0), new Point(2.0, 2.0), new Point(3.0, 3.0));
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER).cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        Cluster<T> cluster1 = clusters.get(0);
        Cluster<T> cluster2 = clusters.get(1);
        assertEquals(Math.max(cluster1.getPoints().size(), cluster2.getPoints().size()), points.size());
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        List<T> points = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 1.0), new Point(2.0, 2.0), new Point(3.0, 3.0), new Point(4.0, 4.0));
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT).cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        Cluster<T> cluster1 = clusters.get(0);
        Cluster<T> cluster2 = clusters.get(1);
        Point farthestPoint = getFarthestPoint(points, cluster1.getCenter());
        assertEquals(farthestPoint, cluster1.getPoints().get(0));
        farthestPoint = getFarthestPoint(points, cluster2.getCenter());
        assertEquals(farthestPoint, cluster2.getPoints().get(0));
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterInKMeans() {
        List<T> points = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 1.0), new Point(2.0, 2.0), new Point(3.0, 3.0), new Point(4.0, 4.0), new Point(5.0, 5.0));
        List<Cluster<T>> clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR).cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        clusters = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR).cluster(points, 2, 10);
    }

    private List<T> createRandomPoints(int numPoints) {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < numPoints; ++i) {
            double x = RandomUtils.nextDouble(0.0, 10.0);
            double y = RandomUtils.nextDouble(0.0, 10.0);
            points.add(new Point(x, y));
        }
        return points;
    }

    private double sumOfSquaredDistances(List<T> points, List<Cluster<T>> clusters) {
        double sum = 0;
        for (Cluster<T> cluster : clusters) {
            for (T point : cluster.getPoints()) {
                sum += point.distanceFrom(cluster.getCenter()) * point.distanceFrom(cluster.getCenter());
            }
        }
        return sum;
    }

    private T getFarthestPoint(List<T> points, T center) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        T maxPoint = null;
        for (T point : points) {
            double distance = point.distanceFrom(center);
            if (distance > maxDistance) {
                maxDistance = distance;
                maxPoint = point;
            }
        }
        return maxPoint;
    }

    private static class Point implements Clusterable<Point> {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<Point> other) {
            Point otherPoint = (Point) other;
            return Math.sqrt(Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2));
        }

        @Override
        public double distanceFrom(Cluster<Point> cluster) {
            Cluster<Point> otherCluster = (Cluster<Point>) cluster;
            return distanceFrom(otherCluster.getCenter());
        }

        @Override
        public double[] toArray() {
            return new double[]{x, y};
        }
    }
}