import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Point2D;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() throws ConvergenceException {
        // Test empty cluster strategy with an empty list
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Point2D> points = new ArrayList<>();
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 3, -1);
        Assert.assertEquals(0, clusters.size());

        // Test empty cluster strategy with a list containing only empty clusters
        points = new ArrayList<>();
        points.add(new Point2D(0.0, 0.0));
        points.add(new Point2D(0.0, 0.0));
        clusters = clusterer.cluster(points, 3, -1);
        Assert.assertEquals(3, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            Assert.assertTrue(cluster.isEmpty());
        }

        // Test different empty cluster strategies
        KMeansPlusPlusClusterer<Point2D> clustererWithLargeVariance = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        KMeansPlusPlusClusterer<Point2D> clustererWithLargestPointsNumber = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        KMeansPlusPlusClusterer<Point2D> clustererWithFarthestPoint = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        KMeansPlusPlusClusterer<Point2D> clustererWithError = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);

        List<Point2D> pointsWithThreeEmptyClusters = new ArrayList<>();
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        clusters = clustererWithLargeVariance.cluster(pointsWithThreeEmptyClusters, 3, -1);
        Assert.assertEquals(3, clusters.size());
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            if (i == 0) {
                Assert.assertFalse(cluster.isEmpty());
            } else {
                Assert.assertTrue(cluster.isEmpty());
            }
        }

        pointsWithThreeEmptyClusters = new ArrayList<>();
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        clusters = clustererWithLargestPointsNumber.cluster(pointsWithThreeEmptyClusters, 3, -1);
        Assert.assertEquals(3, clusters.size());
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            if (i == 0) {
                Assert.assertFalse(cluster.isEmpty());
            } else {
                Assert.assertTrue(cluster.isEmpty());
            }
        }

        pointsWithThreeEmptyClusters = new ArrayList<>();
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        clusters = clustererWithFarthestPoint.cluster(pointsWithThreeEmptyClusters, 3, -1);
        Assert.assertEquals(3, clusters.size());
        for (int i = 0; i < 3; i++) {
            Cluster<Point2D> cluster = clusters.get(i);
            if (i == 0) {
                Assert.assertFalse(cluster.isEmpty());
            } else {
                Assert.assertTrue(cluster.isEmpty());
            }
        }

        pointsWithThreeEmptyClusters = new ArrayList<>();
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        pointsWithThreeEmptyClusters.add(new Point2D(0.0, 0.0));
        try {
            clusters = clustererWithError.cluster(pointsWithThreeEmptyClusters, 3, -1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testCluster() {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(1.0, 1.0));
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(2.0, 1.0));
        points.add(new Point2D(2.0, 2.0));
        List<Cluster<Point2D>> clusters = new KMeansPlusPlusClusterer<>(new Random()).cluster(points, 2, 1000);

        // Test clusters' centers
        Cluster<Point2D> cluster1 = clusters.get(0);
        Cluster<Point2D> cluster2 = clusters.get(1);
        Point2D center1 = cluster1.getCenter();
        Point2D center2 = cluster2.getCenter();
        double expectedCenter1X = (1.0 + 2.0) / 2.0;
        double expectedCenter1Y = (1.0 + 1.0 + 2.0 + 2.0) / 4.0;
        double expectedCenter2X = (2.0 + 2.0) / 2.0;
        double expectedCenter2Y = (2.0 + 2.0) / 2.0;
        assertEquals(expectedCenter1X, center1.getX(), 0.001);
        assertEquals(expectedCenter1Y, center1.getY(), 0.001);
        assertEquals(expectedCenter2X, center2.getX(), 0.001);
        assertEquals(expectedCenter2Y, center2.getY(), 0.001);

        // Test clusters' points
        List<Point2D> expectedCluster1Points = new ArrayList<>();
        expectedCluster1Points.add(new Point2D(1.0, 1.0));
        expectedCluster1Points.add(new Point2D(1.0, 2.0));
        List<Point2D> expectedCluster2Points = new ArrayList<>();
        expectedCluster2Points.add(new Point2D(2.0, 1.0));
        expectedCluster2Points.add(new Point2D(2.0, 2.0));
        List<Point2D> cluster1Points = cluster1.getPoints();
        List<Point2D> cluster2Points = cluster2.getPoints();
        assertEquals(expectedCluster1Points.size(), cluster1.size());
        assertEquals(expectedCluster2Points.size(), cluster2.size());
        for (int i = 0; i < cluster1.size(); i++) {
            Point2D point = cluster1Points.get(i);
            Point2D expectedPoint = expectedCluster1Points.get(i);
            assertEquals(expectedPoint.getX(), point.getX(), 0.001);
            assertEquals(expectedPoint.getY(), point.getY(), 0.001);
        }
        for (int i = 0; i < cluster2.size(); i++) {
            Point2D point = cluster2Points.get(i);
            Point2D expectedPoint = expectedCluster2Points.get(i);
            assertEquals(expectedPoint.getX(), point.getX(), 0.001);
            assertEquals(expectedPoint.getY(), point.getY(), 0.001);
        }
    }

}