package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.Clustering;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.distance.EuclideanDistance;
import org.apache.commons.math3.stat.distance.DistanceMeasure;
import org.apache.commons.math3.stat.distance.PointDistance;
import org.apache.commons.math3.stat.distance.VectorDistance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link KMeansPlusPlusClusterer}.
 */
public class KMeansPlusPlusClustererTest {

    /** Strategies to use for replacing an empty cluster. */
    private static enum EmptyClusterStrategy {
        LARGEST_VARIANCE,
        LARGEST_POINTS_NUMBER,
        FARTHEST_POINT,
        ERROR
    }

    /** The number of points to generate for testing. */
    private static final int NUM_POINTS = 10;

    /** The number of dimensions for points. */
    private static final int NUM_DIMENSIONS = 2;

    /** The expected number of clusters. */
    private static final int K = 3;

    /** The random number generator. */
    private static final Random RANDOM = new Random(123);

    /** The clustering algorithm. */
    private static final KMeansPlusPlusClusterer<TestPoint> CLUSTERER = new KMeansPlusPlusClusterer<TestPoint>(
            RANDOM, EmptyClusterStrategy.LARGEST_VARIANCE);

    /**
     * Test the clustering algorithm with random points.
     */
    @Test
    public void testRandomClustering() {

        // Generate random points
        List<TestPoint> points = new ArrayList<TestPoint>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new TestPoint(RANDOM.nextDouble(), RANDOM.nextDouble()));
        }

        // Run the clustering algorithm
        List<Cluster<TestPoint>> result = CLUSTERER.cluster(points, K, -1);

        // Check the number of clusters and points
        assertEquals(K, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check the distance between each point and its cluster centroid
        for (Cluster<TestPoint> cluster : result) {
            TestPoint centroid = cluster.getCenter();
            for (TestPoint point : cluster.getPoints()) {
                final DistanceMeasure distance = new EuclideanDistance();
                double expectedDistance = distance.distance(point, centroid);
                double actualDistance = Precision.round(point.distanceFrom(centroid), 15);
                assertEquals(expectedDistance, actualDistance, 1e-12);
            }
        }

        // Check the variance of clusters
        for (Cluster<TestPoint> cluster : result) {
            List<TestPoint> pointsInCluster = cluster.getPoints();
            TestPoint centroid = cluster.getCenter();
            Variance variance = new Variance();
            for (TestPoint point : pointsInCluster) {
                variance.increment(point.distanceFrom(centroid) * point.distanceFrom(centroid));
            }
            double expectedVariance = variance.getResult();
            double actualVariance = Precision.round(cluster.getVariance(), 15);
            assertEquals(expectedVariance, actualVariance, 1e-12);
        }

    }

    /**
     * Test the clustering algorithm with points in one cluster.
     */
    @Test
    public void testOneCluster() {

        // Generate points in one cluster
        List<TestPoint> points = new ArrayList<TestPoint>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            points.add(new TestPoint(i % 2.0, i % 3.0));
        }

        // Run the clustering algorithm
        List<Cluster<TestPoint>> result = CLUSTERER.cluster(points, K, -1);

        // Check the number of clusters and points
        assertEquals(1, result.size());
        for (Cluster<TestPoint> cluster : result) {
            assertTrue(cluster.getPoints().size() >= NUM_POINTS);
        }

        // Check the distance between each point and its cluster centroid
        Cluster<TestPoint> cluster = result.get(0);
        TestPoint centroid = cluster.getCenter();
        for (TestPoint point : cluster.getPoints()) {
            final DistanceMeasure distance = new EuclideanDistance();
            double expectedDistance = distance.distance(point, centroid);
            double actualDistance = Precision.round(point.distanceFrom(centroid), 15);
            assertEquals(expectedDistance, actualDistance, 1e-12);
        }

        // Check the variance of clusters
        Variance variance = new Variance();
        double expectedVariance = variance.getResult();
        double actualVariance = Precision.round(cluster.getVariance(), 15);
        assertEquals(expectedVariance, actualVariance, 1e-12);

    }

    /**
     * Test the clustering algorithm with points in multiple clusters.
     */
    @Test
    public void testMultipleClusters() {

        // Generate points in multiple clusters
        List<TestPoint> points = new ArrayList<TestPoint>(NUM_POINTS);
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = i % 2.0;
            double y = i % 3.0;
            if (x < 0.5 || y < 0.5) {
                points.add(new TestPoint(x, y));
            } else {
                points.add(new TestPoint(x + 0.1, y + 0.1));
            }
        }

        // Run the clustering algorithm
        List<Cluster<TestPoint>> result = CLUSTERER.cluster(points, K, -1);

        // Check the number of clusters and points
        assertEquals(2, result.size());
        for (Cluster<TestPoint> cluster : result) {
            if (cluster.getPoints().size() > 0) {
                assertTrue(cluster.getPoints().size() > NUM_POINTS / 2);
            } else {
                assertTrue(cluster.getPoints().isEmpty());
            }
        }

        // Check the distance between each point and its cluster centroid
        Cluster<TestPoint> firstCluster = result.get(0);
        for (TestPoint point : firstCluster.getPoints()) {
            final DistanceMeasure distance = new EuclideanDistance();
            double expectedDistance = distance.distance(point, firstCluster.getCenter());
            double actualDistance = Precision.round(point.distanceFrom(firstCluster.getCenter()), 15);
            assertEquals(expectedDistance, actualDistance, 1e-12);
        }

        Cluster<TestPoint> secondCluster = result.get(1);
        for (TestPoint point : secondCluster.getPoints()) {
            final DistanceMeasure distance = new EuclideanDistance();
            double expectedDistance = distance.distance(point, secondCluster.getCenter());
            double actualDistance = Precision.round(point.distanceFrom(secondCluster.getCenter()), 15);
            assertEquals(expectedDistance, actualDistance, 1e-12);
        }

        // Check the variance of clusters
        for (Cluster<TestPoint> cluster : result) {
            List<TestPoint> pointsInCluster = cluster.getPoints();
            TestPoint centroid = cluster.getCenter();
            Variance variance = new Variance();
            for (TestPoint point : pointsInCluster) {
                variance.increment(point.distanceFrom(centroid) * point.distanceFrom(centroid));
            }
            double expectedVariance = variance.getResult();
            double actualVariance = Precision.round(cluster.getVariance(), 15);
            assertEquals(expectedVariance, actualVariance, 1e-12);
        }

    }

    /**
     * Test the clustering algorithm with an empty list.
     */
    @Test(expected = ConvergenceException.class)
    public void testEmptyList() {
        List<TestPoint> emptyList = Collections.emptyList();
        CLUSTERER.cluster(emptyList, K, -1);
    }

    /**
     * Test the clustering algorithm with a list of one point.
     */
    @Test(expected = ConvergenceException.class)
    public void testOnePoint() {
        List<TestPoint> onePointList = new ArrayList<TestPoint>(1);
        onePointList.add(new TestPoint(0.0, 0.0));
        CLUSTERER.cluster(onePointList, K, -1);
    }

}

/**
 * Test point with two dimensions.
 */
class TestPoint implements Clusterable<TestPoint> {

    private double x;
    private double y;

    /**
     * Constructs a new test point.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public TestPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    public double distanceFrom(Clusterable<?> other) {
        if (other instanceof TestPoint) {
            TestPoint point = (TestPoint) other;
            return new EuclideanDistance().distance(this, point);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + other.getClass());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Cluster<TestPoint> getCluster() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public TestPoint getCenter() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public List<TestPoint> getPoints() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void addPoint(TestPoint point) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setCluster(Cluster<TestPoint> cluster) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setCenter(TestPoint center) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public double[] toArray() {
        return new double[] { x, y };
    }

    /**
     * {@inheritDoc}
     */
    public int getNumPoints() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void addPoint(TestPoint point, int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public TestPoint getPoint(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void removePoint(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public TestPoint getPoint(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setPoint(int index, TestPoint point) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void setPoints(int index, List<TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void addAll(Collection<? extends TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void removeAll(Collection<? extends TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void retainAll(Collection<? extends TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void remove(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(TestPoint point) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<? extends TestPoint> points) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "(x, y) = (" + x + ", " + y + ")";
    }

}