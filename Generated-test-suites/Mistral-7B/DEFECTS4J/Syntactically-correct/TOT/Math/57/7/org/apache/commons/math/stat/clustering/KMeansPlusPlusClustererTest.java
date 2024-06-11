package org.apache.commons.math.stat.clustering.test;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math.stat.clustering.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyList() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<ClusterablePoint2D> points = new ArrayList<>();
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, -1);
        assertEquals(0, result.size());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterDuringIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), new ClusterablePoint2D(0.4, 0.4), new ClusterablePoint2D(5.5, 5.5));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        // The 5th point should cause an empty cluster during the iterations
        // and the algorithm should throw an exception
    }

    @Test
    public void testThreePointsInTwoDimensions() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 2, 10);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getPoints().containsAll(Arrays.asList(points.get(0), points.get(1))));
        assertTrue(result.get(1).getPoints().containsAll(Arrays.asList(points.get(1), points.get(2))));
    }

    @Test
    public void testThreePointsInTwoDimensionsRandom() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            points.add(new ClusterablePoint2D(random.nextGaussian(), random.nextGaussian()));
        }
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        assertEquals(3, result.size());
        for (Cluster<ClusterablePoint2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testThreePointsInTwoDimensionsMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 2, 1);
        assertEquals(2, result.size());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = new ArrayList<>();
        for (int i = 0; i < 100000; ++i) {
            points.add(new ClusterablePoint2D(random.nextGaussian(), random.nextGaussian()));
        }
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 10, 1000);
        assertEquals(10, result.size());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberOfClustersIsZero() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = new ArrayList<>();
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 0, 10);
        assertEquals(0, result.size());
    }

    @Test
    public void testFourPointsInTwoDimensions() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), new ClusterablePoint2D(0.4, 0.4));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        assertEquals(3, result.size());
    }

    @Test
    public void testFourPointsInTwoDimensionsErrorStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), new ClusterablePoint2D(0.4, 0.4));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        assertEquals(3, result.size());
        for (Cluster<ClusterablePoint2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
            assertEquals(cluster.getCenter().getX(), new Double(cluster.getCentroid().getX()).doubleValue(), 0.01);
            assertEquals(cluster.getCenter().getY(), new Double(cluster.getCentroid().getY()).doubleValue(), 0.01);
        }
    }

    @Test
    public void testFourPointsInTwoDimensionsMismatchedSize() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), null);
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        assertEquals(3, result.size());
        for (Cluster<ClusterablePoint2D> cluster : result) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    @Test
    public void testFourPointsInTwoDimensionsErrorOnEmptyCluster() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), new ClusterablePoint2D(0.4, 0.4), new ClusterablePoint2D(0.5, 0.5));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 3, 10);
        assertEquals(3, result.size());
        List<Cluster<ClusterablePoint2D>> errorResult = kmeans.cluster(Collections.singletonList(points.get(4)), 3, 10);
        assertEquals(0, errorResult.size());
    }

    @Test
    public void testFourPointsInTwoDimensionsMismatchedNumberOfClusters() {
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint2D> kmeans = new KMeansPlusPlusClusterer<>(random);
        List<ClusterablePoint2D> points = Arrays.asList(new ClusterablePoint2D(0.1, 0.1), new ClusterablePoint2D(0.2, 0.2), new ClusterablePoint2D(0.3, 0.3), new ClusterablePoint2D(0.4, 0.4));
        List<Cluster<ClusterablePoint2D>> result = kmeans.cluster(points, 5, 10);
        assertEquals(3, result.size());
    }

}