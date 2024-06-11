package org.apache.commons.math.stat.clustering;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer.*;

/**
 * Test class for KMeansPlusPlusClusterer.
 */
public class KMeansPlusPlusClustererTest {

    /**
     * Test that the clustering algorithm works with positive parameters.
     * @throws MathIllegalArgumentException when the number of points is negative
     * @throws NumberIsTooSmallException when the number of clusters is less than 1
     */
    @Test
    public void testPositiveCluster() {
        List<Clusterable<Double>> points = new ArrayList<>();
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(3.0, 4.0));
        points.add(new Point2D(5.0, 5.0));
        points.add(new Point2D(7.0, 8.0));

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 10);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }
    }

    /**
     * Test that an empty list of points fails with an exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyPoints() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        clusterer.cluster(Collections.emptyList(), 1, 1);
    }

    /**
     * Test that a negative number of points fails with an exception.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativePoints() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        clusterer.cluster(new ArrayList<>(), -1, 1);
    }

    /**
     * Test that a negative number of clusters fails with an exception.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeClusters() {
        KMeansPlusPlusClusterer<Clusterable<Double>> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        clusterer.cluster(new ArrayList<>(), 1, -1);
    }

    /**
     * Test that the algorithm converges with a small dataset.
     */
    @Test
    public void testConvergence() {
        List<Clusterable<Double>> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));
        points.add(new Point2D(4.0, 4.0));
        points.add(new Point2D(5.0, 5.0));

        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, 100);

        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertFalse(cluster.isEmpty());
        }
    }

    /**
     * Test that the empty strategy is used correctly.
     */
    @Test
    public void testEmptyStrategy() {
        List<Clusterable<Double>> points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        points.add(new Point2D(3.0, 3.0));

        KMeansPlusPlusClusterer<Point2D> clusterer1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point2D>> clusters1 = clusterer1.cluster(points, 3, 10);

        KMeansPlusPlusClusterer<Point2D> clusterer2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Point2D>> clusters2 = clusterer2.cluster(points, 3, 10);

        KMeansPlusPlusClusterer<Point2D> clusterer3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> clusters3 = clusterer3.cluster(points, 3, 10);

        Assert.assertEquals(3, clusters1.size());
        Assert.assertEquals(3, clusters2.size());
        Assert.assertEquals(3, clusters3.size());

        for (Cluster<Point2D> cluster : clusters1) {
            Assert.assertFalse(cluster.isEmpty());
        }

        for (Cluster<Point2D> cluster : clusters2) {
            Assert.assertFalse(cluster.isEmpty());
        }

        for (Cluster<Point2D> cluster : clusters3) {
            Assert.assertFalse(cluster.isEmpty());
        }

        List<Clusterable<Double>> points2 = new ArrayList<>();
        points2.add(new Point2D(0.0, 0.0));
        points2.add(new Point2D(1.0, 1.0));

        KMeansPlusPlusClusterer<Point2D> clusterer4 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<Cluster<Point2D>> clusters4 = clusterer4.cluster(points2, 3, 10);

        Assert.assertEquals(3, clusters4.size());
        for (Cluster<Point2D> cluster : clusters4) {
            Assert.assertTrue(cluster.isEmpty());
        }
    }
}