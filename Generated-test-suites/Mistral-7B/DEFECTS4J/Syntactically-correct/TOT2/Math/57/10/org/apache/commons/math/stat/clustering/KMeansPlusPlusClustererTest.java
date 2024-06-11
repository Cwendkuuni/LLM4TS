package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.Clusterer;
import org.apache.commons.math.stat.clustering.Clusterable;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math.stat.clustering.Point2D;
import org.apache.commons.math.stat.descriptive.moment.Variance;

/**
 * Test class for {@link KMeansPlusPlusClusterer}.
 */
public class KMeansPlusPlusClustererTest {

    /** The random generator. */
    private final Random random = new Random(12345);

    /** The empty cluster strategy. */
    private static final KMeansPlusPlusClusterer.EmptyClusterStrategy EMPTY_STRATEGY = KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE;

    /** The test points. */
    private List<Point2D> points;

    /** The test clusters. */
    private List<Cluster<Point2D>> clusters;

    /** The test clusterer. */
    private Clusterer<Point2D> clusterer;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        points = new ArrayList<Point2D>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.5, 1.5));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(2.5, 2.5));
        points.add(new Point2D(3.0, 3.0));

        clusters = new ArrayList<Cluster<Point2D>>();
        clusters.add(new Cluster<Point2D>(new Point2D(0.0, 0.0)));
        clusters.add(new Cluster<Point2D>(new Point2D(4.0, 4.0)));

        clusterer = new KMeansPlusPlusClusterer<Point2D>(random, EMPTY_STRATEGY);
    }

    /**
     * Tests the basic functionality of the {@link KMeansPlusPlusClusterer}.
     */
    @Test
    public void testBasicFunctionality() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
        }
    }

    /**
     * Tests the handling of empty clusters.
     */
    @Test
    public void testHandlingEmptyClusters() {
        List<Cluster<Point2D>> result = clusterer.cluster(Collections.singletonList(new Point2D(4.0, 4.0)), 2, -1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPoints().size());
        assertThrows(ConvergenceException.class, () ->
                clusterer.cluster(Collections.emptyList(), 2, -1));
    }

    /**
     * Tests the handling of the maximum number of iterations.
     */
    @Test
    public void testMaxIterations() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, 1);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
        }

        try {
            clusterer.cluster(points, 2, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the handling of the number of clusters.
     */
    @Test
    public void testNumberOfClusters() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 3, -1);
        assertNotNull(result);
        assertEquals(3, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
        }

        try {
            clusterer.cluster(points, 0, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            clusterer.cluster(points, 5, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Tests the initial centers selection.
     */
    @Test
    public void testInitialCentersSelection() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
        }

        List<Cluster<Point2D>> result2 = clusterer.cluster(points, 2, 2);
        assertNotNull(result2);
        assertEquals(2, result2.size());
        for (Cluster<Point2D> cluster : result2) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
            assertNotSame(cluster.getCenter(), cluster.getPoints().get(0));
        }
    }

    /**
     * Tests the convergence of the algorithm.
     */
    @Test
    public void testConvergence() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, 20);
        assertNotNull(result);
        assertEquals(2, result.size());
        for (Cluster<Point2D> cluster : result) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
        }

        List<Cluster<Point2D>> result2 = clusterer.cluster(points, 2, 100);
        assertNotNull(result2);
        assertEquals(2, result2.size());
        for (Cluster<Point2D> cluster : result2) {
            assertNotNull(cluster);
            assertSame(cluster.getClass(), Cluster.class);
            assertNotNull(cluster.getCenter());
            assertSame(cluster.getClass(), Point2D.class);
            assertEquals(2, cluster.getPoints().size());
            assertEquals(result, result2);
        }
    }

    /**
     * Tests the handling of the {@link ConvergenceException}.
     */
    @Test
    public void testConvergenceException() {
        try {
            clusterer.cluster(points, 2, 1);
            fail();
        } catch (ConvergenceException e) {
            // Expected exception
            assertEquals(LocalizedFormats.MAX_ITERATIONS_REACHED, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 2, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(LocalizedFormats.NEGATIVE_NUMBER_OF_CLUSTERS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 0, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(LocalizedFormats.NEGATIVE_NUMBER_OF_CLUSTERS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 3, -1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(LocalizedFormats.NEGATIVE_MAX_ITERATIONS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 3, 1);
            fail();
        } catch (ConvergenceException e) {
            // Expected exception
            assertEquals(LocalizedFormats.MAX_ITERATIONS_REACHED, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(Collections.singletonList(new Point2D(4.0, 4.0)), 2, 1);
            fail();
        } catch (ConvergenceException e) {
            // Expected exception
            assertEquals(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(Collections.emptyList(), 2, 1);
            fail();
        } catch (ConvergenceException e) {
            // Expected exception
            assertEquals(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 5, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(LocalizedFormats.TOO_MANY_CLUSTERS, e.getLocalizedMessage());
        }

        try {
            clusterer.cluster(points, 2, -1000);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(LocalizedFormats.NEGATIVE_MAX_ITERATIONS, e.getLocalizedMessage());
        }
    }

    /**
     * Tests the handling of the {@link NoSolutionException}.
     */
    @Test
    public void testNoSolutionException() {
        List<Point2D> points2 = new ArrayList<Point2D>();
        points2.add(new Point2D(1.0, 1.0));
        points2.add(new Point2D(1.5, 1.5));
        points2.add(new Point2D(2.0, 2.0));
        points2.add(new Point2D(2.5, 2.5));
        points2.add(new Point2D(3.0, 3.0));
        points2.add(new Point2D(2.5, 2.5));
        points2.add(new Point2D(2.5, 2.5));
        points2.add(new Point2D(2.5, 2.5));
        points2.add(new Point2D(2.5, 2.5));

        try {
            clusterer.cluster(points2, 1, -1);
            fail();
        } catch (NoSolutionException e) {
            // Expected exception
            assertEquals(LocalizedFormats.NO_SOLUTION, e.getLocalizedMessage());
        }
    }

    /**
     * Tests the handling of the {@link MaxCountExceededException}.
     */
    @Test
    public void testMaxCountExceededException() {
        List<Point2D> points3 = new ArrayList<Point2D>();
        points3.add(new Point2D(1.0, 1.0));
        points3.add(new Point2D(1.5, 1.5));
        points3.add(new Point2D(2.0, 2.0));
        points3.add(new Point2D(2.5, 2.5));
        points3.add(new Point2D(3.0, 3.0));
        points3.add(new Point2D(3.5, 3.5));
        points3.add(new Point2D(4.0, 4.0));
        points3.add(new Point2D(4.5, 4.5));
        points3.add(new Point2D(5.0, 5.0));

        try {
            clusterer.cluster(points3, 3, -1);
            fail();
        } catch (MaxCountExceededException e) {
            // Expected exception
            assertEquals(LocalizedFormats.TOO_MANY_POINTS_FOR_CLUSTERING, e.getLocalizedMessage());
        }
    }

}