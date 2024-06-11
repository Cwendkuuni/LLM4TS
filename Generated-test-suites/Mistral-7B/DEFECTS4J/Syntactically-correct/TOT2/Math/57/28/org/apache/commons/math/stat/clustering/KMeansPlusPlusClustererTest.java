package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.DistanceMeasure;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double EPSILON = 1e-10;

    private static final double[][] data = {
            {1.0, 1.0},
            {1.5, 1.5},
            {2.0, 2.0},
            {1.8, 1.8},
            {2.5, 2.5},
            {3.0, 3.0},
            {3.5, 3.5},
            {4.0, 4.0}
    };

    private static final int[] labels = {0, 0, 0, 1, 1, 1, 1, 1};

    private static final DistanceMeasure distance = new EuclideanDistance();

    private KMeansPlusPlusClusterer<T> clustering;

    private int k;
    private int maxIterations;

    public KMeansPlusPlusClustererTest(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    @Parameters(name = "{index}: k={0}, maxIterations={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, 100},
                {5, 10},
                {2, -1},
                {7, 20}
        });
    }

    @Before
    public void setUp() {
        clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @Test
    public void testCluster() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < data.length; ++i) {
            T point = new Point(data[i][0], data[i][1]);
            points.add(point);
        }

        List<Cluster<T>> result = clustering.cluster(points, k, maxIterations);

        for (int i = 0; i < result.size(); ++i) {
            Cluster<T> cluster = result.get(i);
            List<T> clusterPoints = cluster.getPoints();
            assertEquals("Cluster label does not match", labels[i], cluster.getId());
            assertEquals("Number of points in cluster does not match", labels.length / k, clusterPoints.size());
        }

        for (int i = 0; i < labels.length; ++i) {
            int clusterIndex = i / k;
            T point = points.get(i);
            Cluster<T> cluster = result.get(clusterIndex);
            assertTrue("Point " + point + " is not in cluster " + cluster, cluster.contains(point));
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        List<T> points = new ArrayList<>();
        points.add(new Point(0.0, 0.0));
        clustering.cluster(points, 2, maxIterations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidK() {
        List<T> points = new ArrayList<>(Arrays.asList(new Point(0.0, 0.0)));
        clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(points, 0, maxIterations);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIterations() {
        List<T> points = new ArrayList<>(Arrays.asList(new Point(0.0, 0.0)));
        clustering.cluster(points, k, -1);
    }

    @Test
    public void testMaxIterationsReached() {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < data.length; ++i) {
            T point = new Point(data[i][0], data[i][1]);
            points.add(point);
        }

        List<Cluster<T>> result = clustering.cluster(points, k, maxIterations);

        for (int i = 0; i < result.size(); ++i) {
            Cluster<T> cluster = result.get(i);
            List<T> clusterPoints = cluster.getPoints();
            assertEquals("Cluster label does not match", labels[i], cluster.getId());
            assertEquals("Number of points in cluster does not match", labels.length / k, clusterPoints.size());
        }

        for (int i = 0; i < labels.length; ++i) {
            int clusterIndex = i / k;
            T point = points.get(i);
            Cluster<T> cluster = result.get(clusterIndex);
            assertTrue("Point " + point + " is not in cluster " + cluster, cluster.contains(point));
        }

        try {
            clustering.cluster(points, k, maxIterations);
            fail("Should have thrown an exception");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testEmptyClusterStrategyLargestVariance() {
        List<T> points = Arrays.asList(
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(1.0, 1.0),
                new Point(1.0, 1.0)
        );

        List<Cluster<T>> result = clustering.cluster(points, 2, maxIterations);

        assertEquals("Number of clusters does not match", 2, result.size());
        Cluster<T> cluster1 = result.get(0);
        assertEquals("Number of points in cluster does not match", 3, cluster1.getPoints().size());
        Cluster<T> cluster2 = result.get(1);
        assertEquals("Number of points in cluster does not match", 2, cluster2.getPoints().size());

        Cluster<T> largestVarianceCluster = getLargestVarianceCluster(result);

        assertEquals("Cluster label does not match", 0, largestVarianceCluster.getId());
        List<T> largestVarianceClusterPoints = largestVarianceCluster.getPoints();
        double variance = new Variance().calculate(largestVarianceClusterPoints.stream().mapToDouble(p -> p.distanceFrom(largestVarianceCluster.getCenter()).doubleValue()).toArray());
        assertTrue("Variance is not the largest", variance > 0.0);
    }

    @Test
    public void testEmptyClusterStrategyLargestPointsNumber() {
        List<T> points = Arrays.asList(
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(1.0, 1.0),
                new Point(1.0, 1.0)
        );

        List<Cluster<T>> result = clustering.cluster(points, 2, maxIterations);

        assertEquals("Number of clusters does not match", 2, result.size());
        Cluster<T> cluster1 = result.get(0);
        assertEquals("Number of points in cluster does not match", 3, cluster1.getPoints().size());
        Cluster<T> cluster2 = result.get(1);
        assertEquals("Number of points in cluster does not match", 2, cluster2.getPoints().size());

        Cluster<T> largestPointsNumberCluster = getLargestPointsNumberCluster(result);

        assertEquals("Cluster label does not match", 1, largestPointsNumberCluster.getId());
        List<T> largestPointsNumberClusterPoints = largestPointsNumberCluster.getPoints();
        assertTrue("Number of points in cluster is not the largest", largestPointsNumberClusterPoints.size() > cluster1.getPoints().size());
    }

    @Test
    public void testEmptyClusterStrategyFarthestPoint() {
        List<T> points = Arrays.asList(
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(0.0, 0.0),
                new Point(1.0, 1.0),
                new Point(1.0, 1.0)
        );

        List<Cluster<T>> result = clustering.cluster(points, 2, maxIterations);

        assertEquals("Number of clusters does not match", 2, result.size());
        Cluster<T> cluster1 = result.get(0);
        assertEquals("Number of points in cluster does not match", 3, cluster1.getPoints().size());
        Cluster<T> cluster2 = result.get(1);
        assertEquals("Number of points in cluster does not match", 2, cluster2.getPoints().size());

        Cluster<T> farthestPointCluster = getFarthestPointCluster(result);

        assertEquals("Cluster label does not match", 1, farthestPointCluster.getId());
        List<T> farthestPointClusterPoints = farthestPointCluster.getPoints();
        T farthestPoint = farthestPointClusterPoints.get(0);
        T center = farthestPointCluster.getCenter();
        double distance = point.distanceFrom(center);
        assertTrue("Distance to cluster center is not the largest", distance > 0.0);
    }

    @Test
    public void testEmptyClusterStrategyError() {
        List<T> points = new ArrayList<>();
        clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        try {
            clustering.cluster(points, k, maxIterations);
            fail("Should have thrown an exception");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    private Cluster<T> getLargestVarianceCluster(List<Cluster<T>> clusters) {
        Cluster<T> result = null;
        double largestVariance = Double.MIN_VALUE;

        for (Cluster<T> cluster : clusters) {
            double variance = new Variance().calculate(cluster.getPoints().stream().mapToDouble(p -> p.distanceFrom(cluster.getCenter()).doubleValue()).toArray());
            if (variance > largestVariance) {
                largestVariance = variance;
                result = cluster;
            }
        }

        return result;
    }

    private Cluster<T> getLargestPointsNumberCluster(List<Cluster<T>> clusters) {
        Cluster<T> result = null;
        int largestNumber = 0;

        for (Cluster<T> cluster : clusters) {
            if (cluster.getPoints().size() > largestNumber) {
                largestNumber = cluster.getPoints().size();
                result = cluster;
            }
        }

        return result;
    }

    private Cluster<T> getFarthestPointCluster(List<Cluster<T>> clusters) {
        Cluster<T> result = null;

        double farthestDistance = Double.MIN_VALUE;

        for (Cluster<T> cluster : clusters) {
            T farthestPoint = cluster.getPoints().get(0);
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance > farthestDistance) {
                farthestDistance = distance;
                result = cluster;
            }
        }

        return result;
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
            return distanceFrom(cluster.getCenter());
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public void setId(int id) {
            throw new NotPositiveException("Point id cannot be negative");
        }

        @Override
        public List<Cluster<Point>> getNearestClusters(int numNearest) {
            List<Cluster<Point>> result = new ArrayList<>();
            result.add(new Cluster<Point>(new Point(1.0, 1.0)));
            return result;
        }

        @Override
        public List<Cluster<Point>> getNearestClusters(List<Cluster<Point>> clusters, int numNearest) {
            return getNearestClusters(numNearest);
        }
    }

}