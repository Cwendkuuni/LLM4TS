import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.*;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private KMeansPlusPlusClusterer<T> clusterer;
    private Random random;

    public KMeansPlusPlusClustererTest(Random random) {
        this.random = random;
    }

    @Before
    public void setUp() {
        this.clusterer = new KMeansPlusPlusClusterer<>(random);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Random()},
                {new Random(12345)},
                {new Random(123456789)},
        });
    }

    @Test
    public void testClusterWhenProvidedValidPointsAndK() {
        final Collection<T> points = createPoints();
        final int k = 3;
        final List<Cluster<T>> result = clusterer.cluster(points, k, 10);
        final int expectedNumberOfClusters = k;
        final int actualNumberOfClusters = result.size();
        Assert.assertEquals("Unexpected number of clusters.", expectedNumberOfClusters, actualNumberOfClusters);

        for (final Cluster<T> cluster : result) {
            Assert.assertFalse("Empty cluster.", cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWhenProvidedEmptyCollectionAndK() {
        final Collection<T> emptyCollection = new ArrayList<>();
        final int k = 3;
        clusterer.cluster(emptyCollection, k, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWhenProvidedNullCollectionAndK() {
        final Collection<T> nullCollection = null;
        final int k = 3;
        clusterer.cluster(nullCollection, k, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWhenProvidedNegativeNumberOfClusters() {
        final Collection<T> points = createPoints();
        final int negativeNumberOfClusters = -1;
        clusterer.cluster(points, negativeNumberOfClusters, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWhenProvidedInvalidMaxIterations() {
        final Collection<T> points = createPoints();
        final int k = 3;
        final int invalidMaxIterations = -1;
        clusterer.cluster(points, k, invalidMaxIterations);
    }

    @Test
    public void testGetNearestCluster() {
        final List<Cluster<T>> clusters = createClusters();
        final T point = createPoint();
        final Cluster<T> nearestCluster = getNearestCluster(clusters, point);
        Assert.assertNotNull("Nearest cluster should not be null.", nearestCluster);
    }

    @Test
    public void testGetNearestClusterWhenAllClustersAreEmpty() {
        final List<Cluster<T>> emptyClusters = new ArrayList<>();
        final T point = createPoint();
        final Cluster<T> nearestCluster = getNearestCluster(emptyClusters, point);
        Assert.assertNull("Nearest cluster should be null when all clusters are empty.", nearestCluster);
    }

    private Collection<T> createPoints() {
        final List<T> points = new ArrayList<>();
        points.add(createPoint());
        points.add(createPoint());
        points.add(createPoint());
        return points;
    }

    private List<Cluster<T>> createClusters() {
        final List<Cluster<T>> clusters = new ArrayList<>();
        clusters.add(createCluster());
        clusters.add(createCluster());
        clusters.add(createCluster());
        return clusters;
    }

    private T createPoint() {
        return new DummyPoint();
    }

    private Cluster<T> createCluster() {
        return new DummyCluster<>();
    }

    private static class DummyPoint implements Clusterable<DummyPoint> {
        @Override
        public double distanceFrom(T o) {
            return Math.abs(this.getX() - o.getX());
        }

        private double x;

        public DummyPoint(double x) {
            this.x = x;
        }

        public double getX() {
            return this.x;
        }
    }

    private static class DummyCluster<DummyPoint extends Clusterable<DummyPoint>> implements Cluster<DummyPoint> {
        private DummyPoint center;
        private List<DummyPoint> points;

        public DummyCluster() {
            this.points = new ArrayList<>();
        }

        @Override
        public DummyPoint getCenter() {
            return this.center;
        }

        public void setCenter(DummyPoint center) {
            this.center = center;
        }

        @Override
        public List<DummyPoint> getPoints() {
            return this.points;
        }

        @Override
        public void addPoint(DummyPoint point) {
            this.points.add(point);
        }
    }
}