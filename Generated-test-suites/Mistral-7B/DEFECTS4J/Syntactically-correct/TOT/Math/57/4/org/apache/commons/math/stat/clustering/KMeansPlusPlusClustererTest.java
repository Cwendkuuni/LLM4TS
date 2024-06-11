import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class KMeansPlusPlusClustererTest {

    private Random random;
    private KMeansPlusPlusClusterer<Point2D> clusterer;
    private List<Point2D> points;

    @org.junit.Before
    public void setup() {
        random = new Random();
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        points = Arrays.asList(
                new Point2D(1.0, 1.0),
                new Point2D(2.0, 2.0),
                new Point2D(3.0, 3.0),
                new Point2D(4.0, 4.0),
                new Point2D(5.0, 5.0)
        );
    }

    @Test
    public void testEmptyClusterStrategy() {
        assertThrows(ConvergenceException.class, () -> clusterer.cluster(points, 2, -1));
    }

    @Test
    public void testEmptyClusterStrategy_largestPointsNumber() {
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        Cluster<Point2D> cluster1 = result.get(0);
        Cluster<Point2D> cluster2 = result.get(1);
        assertNotNull(cluster1);
        assertNotNull(cluster2);
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(3, cluster2.getPoints().size());
    }

    @Test
    public void testEmptyClusterStrategy_largestVariance() {
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        Cluster<Point2D> cluster1 = result.get(0);
        Cluster<Point2D> cluster2 = result.get(1);
        assertNotNull(cluster1);
        assertNotNull(cluster2);
        assertEquals(3, cluster1.getPoints().size());
        assertEquals(2, cluster2.getPoints().size());
        NormalDistribution normalDistribution = new NormalDistribution(cluster1.getCenter().getX(), cluster1.getCenter().getY());
        double[] expectedDistances = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            expectedDistances[i] = normalDistribution.inverseCumulativeProbability(1.0 - Math.random());
        }
        assertArrayEquals(expectedDistances, PointsDistances.distance(points.toArray(new Point2D[points.size()]), cluster1.getPoints().toArray(new Point2D[cluster1.getPoints().size()])), 0.001);
        assertArrayEquals(expectedDistances, PointsDistances.distance(points.toArray(new Point2D[points.size()]), cluster2.getPoints().toArray(new Point2D[cluster2.getPoints().size()])), 0.001);
    }

    @Test
    public void testEmptyClusterStrategy_farthestPoint() {
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        Cluster<Point2D> cluster1 = result.get(0);
        Cluster<Point2D> cluster2 = result.get(1);
        assertNotNull(cluster1);
        assertNotNull(cluster2);
        assertEquals(2, cluster1.getPoints().size());
        assertEquals(3, cluster2.getPoints().size());
        NormalDistribution normalDistribution = new NormalDistribution(cluster1.getCenter().getX(), cluster1.getCenter().getY());
        double[] expectedDistances = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            expectedDistances[i] = 1.0 / (Math.sqrt(2.0 * Math.PI) * Math.pow(0.1, 2)) * Math.exp(-Math.pow(PointsDistances.distance(points.get(i), cluster1.getCenter()), 2) / (2.0 * Math.pow(0.1, 2)));
        }
        assertArrayEquals(expectedDistances, PointsDistances.distance(points.toArray(new Point2D[points.size()]), cluster1.getPoints().toArray(new Point2D[cluster1.getPoints().size()])), 0.001);
        assertArrayEquals(expectedDistances, PointsDistances.distance(points.toArray(new Point2D[points.size()]), cluster2.getPoints().toArray(new Point2D[cluster2.getPoints().size()])), 0.001);
    }

    @Test
    public void testErrorEmptyClusterStrategy() {
        clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        assertThrows(IllegalArgumentException.class, () -> clusterer.cluster(points, 2, -1));
    }

    @Test
    public void testClusterCenter() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        Cluster<Point2D> cluster1 = result.get(0);
        Cluster<Point2D> cluster2 = result.get(1);
        Point2D expectedCenter1 = new Point2D(2.801515923, 2.801515923);
        Point2D expectedCenter2 = new Point2D(4.201515923, 4.201515923);
        Point2D actualCenter1 = cluster1.getCenter();
        Point2D actualCenter2 = cluster2.getCenter();
        assertNotNull(actualCenter1);
        assertNotNull(actualCenter2);
        assertEquals(expectedCenter1.getX(), Precision.round(actualCenter1.getX(), 5), 0.0);
        assertEquals(expectedCenter1.getY(), Precision.round(actualCenter1.getY(), 5), 0.0);
        assertEquals(expectedCenter2.getX(), Precision.round(actualCenter2.getX(), 5), 0.0);
        assertEquals(expectedCenter2.getY(), Precision.round(actualCenter2.getY(), 5), 0.0);
    }

    @Test
    public void testClusteringChanged() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        List<Cluster<Point2D>> result2 = clusterer.cluster(points, 2, -1);
        boolean clusteringChanged = false;
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result2.size(); j++) {
                if (!result.get(i).getPoints().equals(result2.get(j).getPoints())) {
                    clusteringChanged = true;
                    break;
                }
                if (j < result2.size()) {
                    break;
                }
            }
            if (!clusteringChanged) {
                break;
            }
        }
        assertTrue(clusteringChanged);
    }

    @Test
    public void testClusterSize() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        for (Cluster<Point2D> cluster : result) {
            assertEquals(cluster.getPoints().size(), 3);
        }
    }

    @Test
    public void testClusterPoints() {
        List<Cluster<Point2D>> result = clusterer.cluster(points, 2, -1);
        Cluster<Point2D> cluster = result.get(0);
        List<Point2D> pointsExpected = Arrays.asList(
                new Point2D(1.0, 1.0),
                new Point2D(2.0, 2.0),
                new Point2D(3.0, 3.0)
        );
        List<Point2D> pointsActual = new ArrayList<>();
        for (Point2D point : cluster.getPoints()) {
            pointsActual.add(point);
        }
        assertArrayEquals(pointsExpected.toArray(), pointsActual.toArray());
    }

}

class Point2D implements Clusterable<Point2D> {

    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceFrom(Clusterable<?> other) {
        Point2D p = (Point2D) other;
        double dx = x - p.getX();
        double dy = y - p.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

}

class PointsDistances {

    public static double[] distance(Clusterable<?>[] points, Clusterable<?> point) {
        double[] result = new double[points.length];
        for (int i = 0; i < points.length; ++i) {
            result[i] = points[i].distanceFrom(point);
        }
        return result;
    }

}