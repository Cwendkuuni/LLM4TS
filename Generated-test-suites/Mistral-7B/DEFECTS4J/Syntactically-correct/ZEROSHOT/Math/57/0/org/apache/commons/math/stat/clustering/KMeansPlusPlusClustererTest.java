import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.point.BasicPoint;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, 2, 1);
        assertNull(result.get(0).getCenter());
        assertNull(result.get(1).getCenter());
        assertEquals(1, result.get(0).getPoints().size());
        assertEquals(1, result.get(1).getPoints().size());
        assertEquals(1.0, result.get(0).getPoints().get(0).getX(), 0.001);
        assertEquals(1.0, result.get(0).getPoints().get(0).getY(), 0.001);
        assertEquals(2.0, result.get(1).getPoints().get(0).getX(), 0.001);
        assertEquals(2.0, result.get(1).getPoints().get(0).getY(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterStrategyError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, 2, 1);
        assertNull(result.get(0).getCenter());
        assertNull(result.get(1).getCenter());
        assertEquals(1, result.get(0).getPoints().size());
        assertEquals(1, result.get(1).getPoints().size());
        assertEquals(1.0, result.get(0).getPoints().get(0).getX(), 0.001);
        assertEquals(1.0, result.get(0).getPoints().get(0).getY(), 0.001);
        assertEquals(2.0, result.get(1).getPoints().get(0).getX(), 0.001);
        assertEquals(2.0, result.get(1).getPoints().get(0).getY(), 0.001);

        // Call the method again to check that the error is thrown
        result = clusterer.cluster(points, 2, 1);
    }

    @Test
    public void testLargestVarianceStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(3.0, 3.0));
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, 2, 10);
        assertNotNull(result.get(0).getCenter());
        assertNotNull(result.get(1).getCenter());
        double[] expectedCentroids = {1.5, 1.5};
        for (int i = 0; i < result.size(); i++) {
            assertEquals(Precision.equals(expectedCentroids[i], result.get(i).getCenter().getX(), 0.001), true);
            assertEquals(Precision.equals(expectedCentroids[i], result.get(i).getCenter().getY(), 0.001), true);
        }
    }

    @Test
    public void testLargestPointsNumberStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(3.0, 3.0));
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, 2, 10);
        assertNotNull(result.get(0).getCenter());
        assertNotNull(result.get(1).getCenter());
        double[] expectedCentroids = {2.0, 2.0};
        for (int i = 0; i < result.size(); i++) {
            assertEquals(Precision.equals(expectedCentroids[i], result.get(i).getCenter().getX(), 0.001), true);
            assertEquals(Precision.equals(expectedCentroids[i], result.get(i).getCenter().getY(), 0.001), true);
        }
    }

    @Test
    public void testFarthestPointStrategy() {
        Random random = new Random();
        List<BasicPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new BasicPoint(i, i));
        }
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<BasicPoint>> result = clusterer.cluster(points, 2, 10);
        assertNotNull(result.get(0).getCenter());
        assertNotNull(result.get(1).getCenter());
        assertEquals(5, result.get(0).getPoints().size());
        assertEquals(5, result.get(1).getPoints().size());
        Collections.shuffle(points);
        for (int i = 0; i < result.size(); i++) {
            BasicPoint farthestPoint = null;
            double maxDistance = Double.NEGATIVE_INFINITY;
            for (BasicPoint point : result.get(i).getPoints()) {
                double distance = point.distanceFrom(result.get(i).getCenter());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestPoint = point;
                }
            }
            assertEquals(farthestPoint, result.get(i).getPoints().get(result.get(i).getPoints().size() - 1));
        }
    }

}