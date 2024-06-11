package org.apache.commons.math.stat.clustering.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

/**
 * Test the {@link KMeansPlusPlusClusterer} class.
 */
public class KMeansPlusPlusClustererTest {
    /** Test points. */
    private static final List<Clusterable<Double>> POINTS = new ArrayList<>();

    static {
        POINTS.add(new DoublePoint(1.0));
        POINTS.add(new DoublePoint(1.5));
        POINTS.add(new DoublePoint(2.0));
        POINTS.add(new DoublePoint(1.75));
        POINTS.add(new DoublePoint(2.25));
        POINTS.add(new DoublePoint(3.0));
        POINTS.add(new DoublePoint(3.5));
    }

    /** Test random. */
    private static final Random RANDOM = new Random();

    /** Strategy for empty clusters. */
    private static final EmptyClusterStrategy EMPTY_STRATEGY = EmptyClusterStrategy.FARTHEST_POINT;

    /** Test empty cluster strategy. */
    @org.junit.Test
    public void testEmptyClusterStrategy() {
        final List<Cluster<Clusterable<Double>>> clusters = new ArrayList<>();
        final KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM, EMPTY_STRATEGY);
        final List<Clusterable<Double>> emptyPoints = new ArrayList<>();
        emptyPoints.add(new DoublePoint(5.0));
        emptyPoints.add(new DoublePoint(6.0));
        clusters.addAll(clustering.cluster(emptyPoints, 2, 1));
        assertEquals(2, clusters.size());
        for (final Cluster<Clusterable<Double>> cluster : clusters) {
            assertTrue(cluster.getPoints().isEmpty());
            assertTrue(cluster.getCenter() instanceof EmptyClusterCenter);
        }
    }

    /** Test kmeans++. */
    @org.junit.Test
    public void testKMeansPP() {
        final List<Cluster<Clusterable<Double>>> clusters = new ArrayList<>();
        final KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM);
        clusters.addAll(clustering.cluster(POINTS, 3, 100));
        assertEquals(3, clusters.size());
        for (final Cluster<Clusterable<Double>> cluster : clusters) {
            assertFalse(cluster.getPoints().isEmpty());
        }
        final Cluster<Clusterable<Double>> cluster1 = clusters.get(0);
        final Cluster<Clusterable<Double>> cluster2 = clusters.get(1);
        final Cluster<Clusterable<Double>> cluster3 = clusters.get(2);
        for (final Clusterable<Double> point : POINTS) {
            final int index = point.getIndex();
            if (index < 3) {
                assertEquals(cluster1.getCenter(), POINTS.get(index));
                assertTrue(cluster1.getPoints().contains(POINTS.get(index)));
            } else {
                assertEquals(cluster2.getCenter(), POINTS.get(index));
                assertTrue(cluster2.getPoints().contains(POINTS.get(index)));
            }
        }
        final double[] distances1 = new double[3];
        final double[] distances2 = new double[3];
        final double[] distances3 = new double[3];
        for (int i = 0; i < 3; i++) {
            distances1[i] = POINTS.get(i).distanceFrom(cluster1.getCenter());
            distances2[i] = POINTS.get(i + 3).distanceFrom(cluster2.getCenter());
            distances3[i] = POINTS.get(i + 3).distanceFrom(cluster3.getCenter());
        }
        Arrays.sort(distances1);
        Arrays.sort(distances2);
        Arrays.sort(distances3);
        for (int i = 0; i < 3; i++) {
            assertTrue(distances1[i] > distances2[i]);
            assertTrue(distances1[i] > distances3[i]);
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(distances2[i] > distances3[i]);
        }
        final Variance variance1 = new Variance();
        for (final Clusterable<Double> point : cluster1.getPoints()) {
            variance1.increment(point.distanceFrom(cluster1.getCenter()));
        }
        final Variance variance2 = new Variance();
        for (final Clusterable<Double> point : cluster2.getPoints()) {
            variance2.increment(point.distanceFrom(cluster2.getCenter()));
        }
        final Variance variance3 = new Variance();
        for (final Clusterable<Double> point : cluster3.getPoints()) {
            variance3.increment(point.distanceFrom(cluster3.getCenter()));
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(variance1.getResult(), variance2.getResult(), 1e-14);
            assertEquals(variance1.getResult(), variance3.getResult(), 1e-14);
        }
    }

    /** Test kmeans++ with error strategy. */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testKMeansPPError() {
        final List<Cluster<Clusterable<Double>>> clusters = new ArrayList<>();
        final KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM, EmptyClusterStrategy.ERROR);
        clusters.addAll(clustering.cluster(POINTS, 3, 100));
        fail("Should have thrown ConvergenceException");
    }

    /** Test kmeans++ with negative max iterations. */
    @org.junit.Test
    public void testKMeansPPNegativeMaxIterations() {
        final List<Cluster<Clusterable<Double>>> clusters = new ArrayList<>();
        final KMeansPlusPlusClusterer<Clusterable<Double>> clustering = new KMeansPlusPlusClusterer<>(RANDOM);
        try {
            clusters.addAll(clustering.cluster(POINTS, 3, -10));
            fail("Should have thrown IllegalArgumentException");
        } catch (final IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Test point.
     */
    private static class DoublePoint implements Clusterable<Double> {
        /** Index. */
        private final int index;

        /** Constructor. */
        public DoublePoint(final Double d) {
            this.index = POINTS.indexOf(this);
            this.value = d;
        }

        /** Value. */
        private final Double value;

        /** Index. */
        public int getIndex() {
            return this.index;
        }

        /** Distance to center. */
        public double distanceFrom(final Double center) {
            return Math.pow(this.value - center, 2);
        }

        /** Distance to center. */
        public double distanceFrom(final Cluster<Clusterable<Double>> center) {
            return this.distanceFrom(center.getCenter().getDoubleValue());
        }
    }

}