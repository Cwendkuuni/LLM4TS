import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.PointsCollection;
import org.apache.commons.math3.stat.clustering.memory.ArrayMemoryClusterer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Test suite for {@link KMeansPlusPlusClusterer}.
 */
public class KMeansPlusPlusClustererTest {

    private Random random;
    private PointsCollection points;
    private Clusterer<MyPoint> clusteringAlgorithm;

    @Before
    public void before() {
        random = new Random(0);
        points = new PointsCollection();
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    @After
    public void after() {
        clusteringAlgorithm = null;
        points = null;
        random = null;
    }

    @Test
    public void testEmptyConstructor() {
        Clusterer<MyPoint> clusteringAlgorithm = new KMeansPlusPlusClusterer<>();
        assertNotNull(clusteringAlgorithm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRandom() {
        new KMeansPlusPlusClusterer<>(null);
    }

    @Test
    public void testSimpleKMeans() {
        // prepare data
        MyPoint point1 = new MyPoint(1.0, 1.0);
        MyPoint point2 = new MyPoint(2.0, 2.0);
        MyPoint point3 = new MyPoint(3.0, 3.0);
        MyPoint point4 = new MyPoint(4.0, 4.0);
        MyPoint point5 = new MyPoint(5.0, 5.0);

        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);

        // execute clustering
        List<Cluster<MyPoint>> result = clusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check results
        assertEquals(3, result.size());
        Cluster<MyPoint> firstCluster = result.get(0);
        Cluster<MyPoint> secondCluster = result.get(1);
        Cluster<MyPoint> thirdCluster = result.get(2);

        assertTrue(firstCluster.getPoints().contains(point1));
        assertTrue(firstCluster.getPoints().contains(point2));
        assertTrue(firstCluster.getPoints().contains(point3));

        assertTrue(secondCluster.getPoints().contains(point4));
        assertTrue(secondCluster.getPoints().contains(point5));

        assertTrue(thirdCluster.getPoints().isEmpty());

        assertEquals(point1, firstCluster.getCenter());
        assertEquals(point4, secondCluster.getCenter());
        assertNull(thirdCluster.getCenter());
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterNotFound() {
        // prepare data
        MyPoint point1 = new MyPoint(1.0, 1.0);
        MyPoint point2 = new MyPoint(2.0, 2.0);
        MyPoint point3 = new MyPoint(3.0, 3.0);

        points.add(point1);
        points.add(point2);
        points.add(point3);

        // execute clustering
        List<Cluster<MyPoint>> result = clusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check results
        assertEquals(3, result.size());

        // remove one point and try to run again
        points.remove(point1);

        result = clusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check for error
        fail();
    }

    @Test
    public void testSwitchingClusterStrategy() {
        // prepare data
        MyPoint point1 = new MyPoint(1.0, 1.0);
        MyPoint point2 = new MyPoint(2.0, 2.0);
        MyPoint point3 = new MyPoint(3.0, 3.0);
        MyPoint point4 = new MyPoint(4.0, 4.0);
        MyPoint point5 = new MyPoint(5.0, 5.0);

        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);

        // execute clustering with LargestVariance strategy
        List<Cluster<MyPoint>> result1 = clusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check results
        assertEquals(3, result1.size());
        Cluster<MyPoint> firstCluster1 = result1.get(0);
        Cluster<MyPoint> secondCluster1 = result1.get(1);
        Cluster<MyPoint> thirdCluster1 = result1.get(2);

        assertTrue(firstCluster1.getPoints().containsAll(Arrays.asList(point1, point2, point3)));
        assertTrue(secondCluster1.getPoints().containsAll(Arrays.asList(point4, point5)));

        assertEquals(point1, firstCluster1.getCenter());
        assertNull(thirdCluster1.getCenter());

        // execute clustering with Farthest strategy
        Clusterer<MyPoint> newClusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<MyPoint>> result2 = newClusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check results
        assertEquals(3, result2.size());
        Cluster<MyPoint> firstCluster2 = result2.get(0);
        Cluster<MyPoint> secondCluster2 = result2.get(1);
        Cluster<MyPoint> thirdCluster2 = result2.get(2);

        List<MyPoint> firstPoints = firstCluster1.getPoints();
        List<MyPoint> secondPoints = secondCluster1.getPoints();

        assertFalse(firstCluster2.getPoints().containsAll(firstPoints));
        assertFalse(secondCluster2.getPoints().containsAll(secondPoints));

        // check that the centers have changed
        MyPoint center1 = firstCluster1.getCenter();
        MyPoint center2 = secondCluster1.getCenter();
        MyPoint newCenter1 = firstCluster2.getCenter();
        MyPoint newCenter2 = secondCluster2.getCenter();

        assertNotEquals(center1, newCenter1);
        assertNotEquals(center2, newCenter2);

        // check that the empty cluster strategy has been respected
        assertTrue(thirdCluster2.getPoints().isEmpty());

    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        // prepare data
        MyPoint point1 = new MyPoint(1.0, 1.0);
        MyPoint point2 = new MyPoint(2.0, 2.0);

        points.add(point1);
        points.add(point2);

        // execute clustering with Error strategy
        Clusterer<MyPoint> newClusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Cluster<MyPoint>> result = newClusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        fail();
    }

    @Test
    public void testMemoryClusterer() {
        // prepare data
        MyPoint point1 = new MyPoint(1.0, 1.0);
        MyPoint point2 = new MyPoint(2.0, 2.0);
        MyPoint point3 = new MyPoint(3.0, 3.0);
        MyPoint point4 = new MyPoint(4.0, 4.0);
        MyPoint point5 = new MyPoint(5.0, 5.0);

        points.add(point1);
        points.add(point2);
        points.add(point3);
        points.add(point4);
        points.add(point5);

        // execute clustering with default strategy
        List<Cluster<MyPoint>> result = clusteringAlgorithm.cluster(points.toList(MyPoint.class), 3, 10);

        // check results
        assertEquals(3, result.size());

        // check that the algorithm does not use more memory than necessary
        // by comparing to ArrayMemoryClusterer
        Clusterer<MyPoint> memoryClusterer = new ArrayMemoryClusterer<>();
        List<Cluster<MyPoint>> memoryResult = memoryClusterer.cluster(points.toList(MyPoint.class), 3, 10);

        assertEquals(3, memoryResult.size());

        for (Cluster<MyPoint> cluster : result) {
            List<MyPoint> clusterPoints = cluster.getPoints();
            Cluster<MyPoint> memoryCluster = memoryResult.get(cluster.getIndex());
            List<MyPoint> memoryClusterPoints = memoryCluster.getPoints();

            assertEquals(clusterPoints.size(), memoryClusterPoints.size());
            for (MyPoint point : clusterPoints) {
                assertTrue(memoryClusterPoints.contains(point));
            }
        }
    }

}

class MyPoint implements Clusterable<MyPoint> {

    private double x;
    private double y;

    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double distanceFrom(Cluster<MyPoint> cluster) {
        MyPoint center = cluster.getCenter();
        return Math.sqrt(Math.pow(x - center.getX(), 2) + Math.pow(y - center.getY(), 2));
    }

    @Override
    public double distanceFrom(MyPoint myPoint) {
        return Math.sqrt(Math.pow(x - myPoint.x, 2) + Math.pow(y - myPoint.y, 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}