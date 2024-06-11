import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.point.BasicPointFactory;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterEmpty() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        List<Cluster<BasicPoint>> clusters = clusterer.cluster(points, 1, -1);
        assertNotNull(clusters);
        assertNull(clusters.get(0));
        assertEquals(0, clusters.size());
    }

    @Test
    public void testClusterSinglePoint() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<BasicPoint> points = Collections.singletonList(new BasicPoint(1.0, 1.0));
        List<Cluster<BasicPoint>> clusters = clusterer.cluster(points, 1, -1);
        assertNotNull(clusters);
        Cluster<BasicPoint> cluster = clusters.get(0);
        assertNotNull(cluster);
        assertEquals(1, clusters.size());
        assertEquals(1, cluster.getPoints().size());
        assertEquals(1.0, cluster.getCenter().getX(), 0.001);
        assertEquals(1.0, cluster.getCenter().getY(), 0.001);
    }

    @Test
    public void testClusterTwoPoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<BasicPoint> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(2.0, 2.0));
        List<Cluster<BasicPoint>> clusters = clusterer.cluster(points, 2, -1);
        assertNotNull(clusters);
        assertEquals(2, clusters.size());
        for (Cluster<BasicPoint> cluster : clusters) {
            assertNotNull(cluster);
            assertEquals(1, cluster.getPoints().size());
        }
        // Checking the clusters centers
        Cluster<BasicPoint> cluster1 = clusters.get(0);
        Cluster<BasicPoint> cluster2 = clusters.get(1);
        assertEquals(1.0, cluster1.getCenter().getX(), 0.001);
        assertEquals(1.0, cluster1.getCenter().getY(), 0.001);
        assertEquals(2.0, cluster2.getCenter().getX(), 0.001);
        assertEquals(2.0, cluster2.getCenter().getY(), 0.001);
    }

    @Test
    public void testClusterMultiplePoints() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<BasicPoint> points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new BasicPoint(i * 0.3, i * 0.4));
        }
        List<Cluster<BasicPoint>> clusters = clusterer.cluster(points, 3, -1);
        assertNotNull(clusters);
        assertEquals(3, clusters.size());
        int totalPoints = 0;
        for (Cluster<BasicPoint> cluster : clusters) {
            assertNotNull(cluster);
            assertEquals(3, cluster.getPoints().size());
            totalPoints += cluster.getPoints().size();
        }
        // Checking the clusters centers
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<BasicPoint> cluster = clusters.get(i);
            BasicPoint center = cluster.getCenter();
            double sumX = 0.0, sumY = 0.0;
            for (BasicPoint point : cluster.getPoints()) {
                sumX += point.getX();
                sumY += point.getY();
            }
            sumX /= cluster.getPoints().size();
            sumY /= cluster.getPoints().size();
            assertEquals(sumX, center.getX(), 0.001);
            assertEquals(sumY, center.getY(), 0.001);
        }
        assertEquals(10, totalPoints);
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        Random random = new Random();
        KMeansPlusPlusClusterer<BasicPoint> clusterer = new KMeansPlusPlusClusterer<>(random, KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<BasicPoint> points = new ArrayList<>();
        List<Cluster<BasicPoint>> clusters = clusterer.cluster(points, 1, -1);
        assertNull(clusters);
    }

    static class BasicPoint implements Clusterable<BasicPoint> {

        private final double x, y;

        public BasicPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public double distanceFrom(Clusterable<?> other) {
            BasicPoint point = (BasicPoint) other;
            return Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }
    }

}