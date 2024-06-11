import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.point.BasicPoint;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterMethodWithTypicalUseCase() {
        // Given
        List<Clusterable<BasicPoint>> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(1.5, 1.5));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(2.5, 2.5));
        points.add(new BasicPoint(3.0, 3.0));

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Cluster<BasicPoint>> resultClusters = kMeansClusterer.cluster(points, 2, 10);

        // When

        // Then
        Assert.assertEquals(2, resultClusters.size());

        Cluster<BasicPoint> expectedCluster1 = new Cluster<>(new BasicPoint(1.5, 1.5));
        Cluster<BasicPoint> expectedCluster2 = new Cluster<>(new BasicPoint(3.0, 3.0));

        Assert.assertEquals(expectedCluster1.getCenter().getX(), resultClusters.get(0).getCenter().getX(), 0.001);
        Assert.assertEquals(expectedCluster1.getCenter().getY(), resultClusters.get(0).getCenter().getY(), 0.001);
        Assert.assertEquals(2, resultClusters.get(0).getPoints().size());
        Assert.assertEquals(expectedCluster2.getCenter().getX(), resultClusters.get(1).getCenter().getX(), 0.001);
        Assert.assertEquals(expectedCluster2.getCenter().getY(), resultClusters.get(1).getCenter().getY(), 0.001);
        Assert.assertEquals(3, resultClusters.get(1).getPoints().size());
    }

    @Test
    public void testEmptyStrategies() {
        // Given
        List<Clusterable<BasicPoint>> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer1 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.ERROR);
        List<Cluster<BasicPoint>> resultClusters1 = kMeansClusterer1.cluster(points, 2, 10);

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer2 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        List<Cluster<BasicPoint>> resultClusters2 = kMeansClusterer2.cluster(points, 2, 10);

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer3 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        List<Cluster<BasicPoint>> resultClusters3 = kMeansClusterer3.cluster(points, 2, 10);

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer4 = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Cluster<BasicPoint>> resultClusters4 = kMeansClusterer4.cluster(points, 2, 10);

        // When & Then
        Assert.assertThrows(ConvergenceException.class, () -> resultClusters1.get(0).getPoints().size());
        Assert.assertEquals(1, resultClusters2.get(0).getPoints().size());
        Assert.assertEquals(1, resultClusters3.get(0).getPoints().size());
        Assert.assertThrows(ConvergenceException.class, () -> resultClusters4.get(0).getPoints().size());
    }

    @Test
    public void testNegativeMaxIterations() {
        // Given
        List<Clusterable<BasicPoint>> points = new ArrayList<>();
        points.add(new BasicPoint(1.0, 1.0));
        points.add(new BasicPoint(1.5, 1.5));
        points.add(new BasicPoint(2.0, 2.0));
        points.add(new BasicPoint(2.5, 2.5));
        points.add(new BasicPoint(3.0, 3.0));

        KMeansPlusPlusClusterer<BasicPoint> kMeansClusterer = new KMeansPlusPlusClusterer<>(new Random());

        // When
        List<Cluster<BasicPoint>> resultClusters = kMeansClusterer.cluster(points, 2, -1);

        // Then
        Assert.assertEquals(2, resultClusters.size());
    }

    @Test
    public void testGetNearestClusterWithTypicalUseCase() {
        // Given
        List<Cluster<BasicPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new BasicPoint(1.0, 1.0)));
        clusters.add(new Cluster<>(new BasicPoint(2.0, 2.0)));
        clusters.add(new Cluster<>(new BasicPoint(3.0, 3.0)));

        // When
        Cluster<BasicPoint> nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, new BasicPoint(1.5, 1.5));

        // Then
        Assert.assertEquals(clusters.get(0).getCenter().getX(), nearestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(clusters.get(0).getCenter().getY(), nearestCluster.getCenter().getY(), 0.001);
    }

    @Test
    public void testGetNearestClusterWithEmptyClusters() {
        // Given
        List<Cluster<BasicPoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new BasicPoint(1.0, 1.0)));
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>(new BasicPoint(3.0, 3.0)));

        // When
        Cluster<BasicPoint> nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, new BasicPoint(1.5, 1.5));

        // Then
        Assert.assertEquals(clusters.get(0).getCenter().getX(), nearestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(clusters.get(0).getCenter().getY(), nearestCluster.getCenter().getY(), 0.001);

        // Given
        clusters.remove(0);
        clusters.add(new Cluster<>(new BasicPoint(1.0, 1.0)));
        clusters.add(new Cluster<>());
        clusters.add(new Cluster<>(new BasicPoint(3.0, 3.0)));

        // When
        nearestCluster = KMeansPlusPlusClustererTest.getNearestCluster(clusters, new BasicPoint(1.5, 1.5));

        // Then
        Assert.assertEquals(clusters.get(1).getCenter().getX(), nearestCluster.getCenter().getX(), 0.001);
        Assert.assertEquals(clusters.get(1).getCenter().getY(), nearestCluster.getCenter().getY(), 0.001);
    }

    private static Cluster<BasicPoint> getNearestCluster(final Collection<Cluster<BasicPoint>> clusters, final BasicPoint point) {
        double minDistance = Double.MAX_VALUE;
        Cluster<BasicPoint> minCluster = null;
        for (final Cluster<BasicPoint> cluster : clusters) {
            final double distance = point.distanceFrom(cluster.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = cluster;
            }
        }
        return minCluster;
    }

}