import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.point.BasicPoint;
import org.apache.commons.math3.stat.clustering.point.DistanceMeasure;
import org.apache.commons.math3.stat.clustering.point.DistanceMeasureFactory;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        // Test empty cluster strategy
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        Clusterer<BasicPoint> result = clusterer.cluster(points, 1, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSinglePoint() {
        // Test single point
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        int k = 1;
        int maxIterations = 1;
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, k, maxIterations);
        assertEquals(1, result.size());
        Cluster<BasicPoint> cluster = result.get(0);
        assertEquals(1.0, cluster.getCenter().getX(), 0.01);
        assertEquals(1.0, cluster.getCenter().getY(), 0.01);
        assertEquals(1, cluster.getPoints().size());
        assertEquals(points.get(0), cluster.getPoints().get(0));
    }

    @Test
    public void testBasicCluster() {
        // Test basic cluster
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(3.0, 3.0));
        int k = 2;
        int maxIterations = 100;
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, k, maxIterations);
        assertEquals(2, result.size());
        Cluster<BasicPoint> cluster1 = result.get(0);
        assertEquals(1.5, cluster1.getCenter().getX(), 0.01);
        assertEquals(1.5, cluster1.getCenter().getY(), 0.01);
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(points.get(0), cluster1.getPoints().get(0));
        assertEquals(points.get(1), cluster1.getPoints().get(1));
        Cluster<BasicPoint> cluster2 = result.get(1);
        assertEquals(3.5, cluster2.getCenter().getX(), 0.01);
        assertEquals(3.5, cluster2.getCenter().getY(), 0.01);
        assertEquals(1, cluster2.getPoints().size());
        assertEquals(points.get(2), cluster2.getPoints().get(0));

        // Test Euclidean distance
        DistanceMeasure distance = DistanceMeasureFactory.getEuclideanDistance();
        for (Cluster<BasicPoint> cluster : result) {
            for (BasicPoint point : cluster.getPoints()) {
                for (BasicPoint otherPoint : points) {
                    if (cluster.equals(Collections.singletonList(otherPoint))) {
                        assertEquals(0.0, distance.distance(point, otherPoint), 0.01);
                    } else {
                        assertTrue(distance.distance(point, otherPoint) > 0.0);
                    }
                }
            }
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterDuringIteration() {
        // Test empty cluster during iteration
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(3.0, 3.0));
        int k = 2;
        int maxIterations = 1;
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, k, maxIterations);
        clusterer.cluster(Collections.emptyList(), k, maxIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterDuringIterationWithErrorStrategy() {
        // Test empty cluster during iteration with error strategy
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(3.0, 3.0));
        int k = 2;
        int maxIterations = 1;
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, k, maxIterations);
        clusterer.cluster(Collections.emptyList(), k, maxIterations);
    }

}