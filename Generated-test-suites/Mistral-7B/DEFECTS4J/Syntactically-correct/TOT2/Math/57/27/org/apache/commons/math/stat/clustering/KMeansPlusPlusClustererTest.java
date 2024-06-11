import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KMeansPlusPlusClusterer.class)
public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterBasic() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        Collection<ClusterablePoint> points = createPoints();
        int k = 3;
        int maxIterations = 100;

        // When
        List<Cluster<ClusterablePoint>> result = clusterer.cluster(points, k, maxIterations);

        // Then
        Assert.assertEquals(k, result.size());
        for (Cluster<ClusterablePoint> cluster : result) {
            Assert.assertFalse(cluster.getPoints().isEmpty());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEmptyClusterError() {
        // Given
        Random random = new Random();
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(random, EmptyClusterStrategy.ERROR);
        Collection<ClusterablePoint> points = createEmptyPoints();
        int k = 3;
        int maxIterations = 100;

        // When
        clusterer.cluster(points, k, maxIterations);
    }

    @Test
    public void testGetNearestCluster() {
        // Given
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = mock(KMeansPlusPlusClusterer.class);
        Collection<Cluster<ClusterablePoint>> clusters = createClusters();
        ClusterablePoint point = mock(ClusterablePoint.class);
        when(clusterer.getNearestCluster(clusters, point)).thenCallRealMethod();

        // When
        Cluster<ClusterablePoint> result = clusterer.getNearestCluster(clusters, point);

        // Then
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPointFromLargestNumberCluster() {
        // Given
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);
        Collection<Cluster<ClusterablePoint>> clusters = createClusters();

        // When
        T result = clusterer.getPointFromLargestNumberCluster(clusters);

        // Then
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPointFromLargestVarianceCluster() {
        // Given
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.LARGEST_VARIANCE);
        Collection<Cluster<ClusterablePoint>> clusters = createClusters();

        // When
        T result = clusterer.getPointFromLargestVarianceCluster(clusters);

        // Then
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFarthestPoint() {
        // Given
        KMeansPlusPlusClusterer<ClusterablePoint> clusterer = new KMeansPlusPlusClusterer<>(new Random(), EmptyClusterStrategy.FARTHEST_POINT);
        Collection<Cluster<ClusterablePoint>> clusters = createClusters();

        // When
        T result = clusterer.getFarthestPoint(clusters);

        // Then
        Assert.assertNotNull(result);
    }

    private Collection<ClusterablePoint> createPoints() {
        List<ClusterablePoint> points = new ArrayList<>();
        points.add(new ClusterablePoint(1.0, 2.0));
        points.add(new ClusterablePoint(3.0, 4.0));
        points.add(new ClusterablePoint(5.0, 6.0));
        points.add(new ClusterablePoint(7.0, 8.0));
        return points;
    }

    private Collection<Cluster<ClusterablePoint>> createClusters() {
        List<Cluster<ClusterablePoint>> clusters = new ArrayList<>();
        clusters.add(new Cluster<>(new ClusterablePoint(1.0, 2.0)));
        clusters.add(new Cluster<>(new ClusterablePoint(3.0, 4.0)));
        clusters.add(new Cluster<>(new ClusterablePoint(5.0, 6.0)));
        clusters.add(new Cluster<>());
        return clusters;
    }

    private Collection<ClusterablePoint> createEmptyPoints() {
        List<ClusterablePoint> points = new ArrayList<>();
        return points;
    }

}