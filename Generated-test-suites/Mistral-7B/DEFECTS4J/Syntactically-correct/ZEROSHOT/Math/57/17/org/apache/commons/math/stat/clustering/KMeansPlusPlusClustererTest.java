package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.EmptyClusterStrategy;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;

@RunWith(Parameterized.class)
public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private final Random random;
    private final EmptyClusterStrategy emptyStrategy;
    private final double[][] points;
    private final int k;

    public KMeansPlusPlusClustererTest(Random random, EmptyClusterStrategy emptyStrategy, int k, double[][] points) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
        this.k = k;
        this.points = points;
    }

    @Parameterized.Parameters(name = "{0} with strategy {1} and k={2}")
    public static Object[][] data() {
        return new Object[][] {
                {new Random(), EmptyClusterStrategy.LARGEST_VARIANCE, 3, new double[][] {{1.0, 2.0}, {2.0, 2.0}, {3.0, 3.0}}},
                {new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER, 3, new double[][] {{1.0, 2.0}, {2.0, 2.0}, {3.0, 3.0}}},
                {new Random(), EmptyClusterStrategy.FARTHEST_POINT, 3, new double[][] {{1.0, 2.0}, {2.0, 2.0}, {3.0, 3.0}}},
                {new Random(), EmptyClusterStrategy.ERROR, 3, new double[][] {{1.0, 2.0}, {2.0, 2.0}, {3.0, 3.0}}}
        };
    }

    @Test
    public void testCluster() throws MathException, NumberIsTooSmallException, MaxEvalExceededException {

        List<T> originalPoints = new ArrayList<>();
        for (double[] point : points) {
            originalPoints.add((Clusterable<T>) new Point(point[0], point[1]));
        }

        List<Cluster<T>> result = new KMeansPlusPlusClusterer<>(random, emptyStrategy).cluster(originalPoints.stream().collect(Collectors.toList()), k, 10);

        List<Cluster<T>> expected = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            expected.add(new Cluster<T>(new Point(getExpectedCenterX(i), getExpectedCenterY(i))));
        }
        for (T point : originalPoints) {
            Cluster<T> cluster = getNearestCluster(result, point);
            assertEquals(expected.get(cluster.getIndex()).getCenter().getX(), Precision.round(point.getX(), 4), 0.0);
            assertEquals(expected.get(cluster.getIndex()).getCenter().getY(), Precision.round(point.getY(), 4), 0.0);
        }

    }

    private double getExpectedCenterX(int index) {
        if (index == 0) {
            return 2.5;
        } else if (index == 1) {
            return 1.5;
        } else {
            return 3.5;
        }
    }

    private double getExpectedCenterY(int index) {
        if (index == 0) {
            return 2.5;
        } else if (index == 1) {
            return 1.5;
        } else {
            return 3.5;
        }
    }

    private Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = cluster;
            }
        }
        return nearest;
    }

    /**
     * Test for EmptyClusterStrategy.ERROR
     */
    @Test(expected = ConvergenceException.class)
    public void testErrorEmptyClusterStrategy() throws MathException, NumberIsTooSmallException, MaxEvalExceededException {

        List<T> originalPoints = new ArrayList<>();
        originalPoints.add((Clusterable<T>) new Point(1.0, 2.0));
        originalPoints.add((Clusterable<T>) new Point(2.0, 2.0));
        originalPoints.add((Clusterable<T>) new Point(3.0, 3.0));

        new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR).cluster(originalPoints.stream().collect(Collectors.toList()), k, 10);

    }

    private static class Point implements Clusterable<Point> {

        private double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<Point> other) {
            Point p = (Point) other;
            return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}