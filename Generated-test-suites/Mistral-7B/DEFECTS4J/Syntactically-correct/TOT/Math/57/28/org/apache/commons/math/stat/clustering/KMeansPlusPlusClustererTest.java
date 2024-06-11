package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.point.EuclideanDistance;
import org.apache.commons.math3.stat.clustering.point.Point2D;
import org.junit.Test;

/**
 * Test class for {@link KMeansPlusPlusClusterer}.
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test with valid input and expected positive results.
     */
    @Test
    public void testValidInput() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    /**
     * Test with valid input and expected negative results (empty list).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        clustering.cluster(Collections.emptyList(), 2, 10);
    }

    /**
     * Test with valid input and expected negative results (negative number of clusters).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfClusters() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        clustering.cluster(points, -1, 10);
    }

    /**
     * Test with valid input and expected negative results (negative maximum number of iterations).
     */
    @Test
    public void testNegativeMaxIterations() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, -1);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        // The algorithm should still have converged
    }

    /**
     * Test with valid input and expected positive results (variance strategy).
     */
    @Test
    public void testLargestVarianceStrategy() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
            final Variance var = new Variance();
            for (final Point2D p : cluster.getPoints()) {
                var.increment(p.distanceFrom(cluster.getCenter()));
            }
            final double variance = var.getResult();
            assertTrue(variance > 0.0);
        }
    }

    /**
     * Test with valid input and expected positive results (points number strategy).
     */
    @Test
    public void testLargestPointsNumberStrategy() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
        final List<Cluster<Point2D>> result2 = clustering.cluster(points, 3, 10);
        assertNotNull(result2);
        assertEquals(3, result2.size());
        for (final Cluster<Point2D> cluster : result2) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    /**
     * Test with valid input and expected positive results (farthest point strategy).
     */
    @Test
    public void testFarthestPointStrategy() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    /**
     * Test with valid input and expected negative results (error strategy).
     */
    @Test(expected = ConvergenceException.class)
    public void testErrorStrategy() {
        final Random random = new Random(1234L);
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        clustering.cluster(points, 2, 10);
    }

    /**
     * Test with valid input and expected positive results (distance function).
     */
    @Test
    public void testCustomDistanceFunction() {
        final List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        final KMeansPlusPlusClusterer<Point2D> clustering = new KMeansPlusPlusClusterer<>(new Random(1234L), EmptyClusterStrategy.LARGEST_VARIANCE, new EuclideanDistance());
        final List<Cluster<Point2D>> result = clustering.cluster(points, 2, 10);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (final Cluster<Point2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

}