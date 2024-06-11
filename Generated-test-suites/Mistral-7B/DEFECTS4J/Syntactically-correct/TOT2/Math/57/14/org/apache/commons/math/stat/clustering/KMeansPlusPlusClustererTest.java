import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterer;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.data.DefaultPointCollection;
import org.apache.commons.math3.stat.clustering.data.Point2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testEmptyClusterStrategy() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        Clusterer<Point2D> clustering = clusterer.cluster(new DefaultPointCollection<Point2D>(), 2, 10);
        assertTrue(clustering.size() > 0);
        try {
            clusterer.cluster(new DefaultPointCollection<Point2D>(), 2, 10);
            fail("Should have thrown ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testBasicFunctionality() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);
        Collection<Point2D> points = new DefaultPointCollection<Point2D>();
        points.add(new Point2D(1.0, 2.0));
        points.add(new Point2D(3.0, 4.0));
        points.add(new Point2D(5.0, 6.0));
        List<Cluster<Point2D>> clusters = clusterer.cluster(points, 2, 10);
        assertEquals(2, clusters.size());
        for (Cluster<Point2D> cluster : clusters) {
            assertFalse(cluster.isEmpty());
        }
    }

    @Test
    public void testVariousInputSizes() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random);
        // empty collection
        Collection<Point2D> emptyPoints = new DefaultPointCollection<Point2D>();
        List<Cluster<Point2D>> emptyClusters = clusterer.cluster(emptyPoints, 3, 10);
        assertTrue(emptyClusters.isEmpty());
        // single point
        Collection<Point2D> singlePoint = new DefaultPointCollection<Point2D>();
        singlePoint.add(new Point2D(1.0, 2.0));
        List<Cluster<Point2D>> singleCluster = clusterer.cluster(singlePoint, 3, 10);
        assertEquals(1, singleCluster.size());
        // two points
        Collection<Point2D> twoPoints = new DefaultPointCollection<Point2D>();
        twoPoints.add(new Point2D(1.0, 2.0));
        twoPoints.add(new Point2D(3.0, 4.0));
        List<Cluster<Point2D>> twoClusters = clusterer.cluster(twoPoints, 2, 10);
        assertEquals(2, twoClusters.size());
        // large number of points
        int numPoints = 100;
        Collection<Point2D> largePoints = new DefaultPointCollection<Point2D>();
        for (int i = 0; i < numPoints; i++) {
            double x = Math.random();
            double y = Math.random();
            largePoints.add(new Point2D(x, y));
        }
        List<Cluster<Point2D>> largeClusters = clusterer.cluster(largePoints, 10, 10);
        assertEquals(10, largeClusters.size());
    }

    @Test
    public void testEdgeCases() {
        Random random = new Random();
        KMeansPlusPlusClusterer<Point2D> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_VARIANCE);
        // all points in same cluster
        Collection<Point2D> allSame = new DefaultPointCollection<Point2D>();
        allSame.add(new Point2D(1.0, 1.0));
        allSame.add(new Point2D(1.0, 1.0));
        allSame.add(new Point2D(1.0, 1.0));
        List<Cluster<Point2D>> sameClusters = clusterer.cluster(allSame, 2, 10);
        assertEquals(1, sameClusters.size());
        // all points in different clusters
        Collection<Point2D> allDifferent = new DefaultPointCollection<Point2D>();
        allDifferent.add(new Point2D(1.0, 1.0));
        allDifferent.add(new Point2D(2.0, 2.0));
        allDifferent.add(new Point2D(3.0, 3.0));
        List<Cluster<Point2D>> differentClusters = clusterer.cluster(allDifferent, 3, 10);
        assertEquals(3, differentClusters.size());
        // points on line
        Collection<Point2D> onLine = new DefaultPointCollection<Point2D>();
        onLine.add(new Point2D(0.0, 0.0));
        onLine.add(new Point2D(1.0, 0.0));
        onLine.add(new Point2D(2.0, 0.0));
        List<Cluster<Point2D>> onLineClusters = clusterer.cluster(onLine, 2, 10);
        assertEquals(2, onLineClusters.size());
        // points in circle
        Collection<Point2D> inCircle = new DefaultPointCollection<Point2D>();
        Point2D center = new Point2D(1.0, 1.0);
        double radius = 0.5;
        for (double angle = 0.0; angle < 2 * Math.PI; angle += Math.PI / 10.0) {
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            inCircle.add(new Point2D(x, y));
        }
        List<Cluster<Point2D>> inCircleClusters = clusterer.cluster(inCircle, 5, 10);
        assertEquals(5, inCircleClusters.size());
    }

}