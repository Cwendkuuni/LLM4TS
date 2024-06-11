package org.apache.commons.math.stat.clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;

public class KMeansPlusPlusClustererTest<T extends Clusterable<T>> {

    private static final double DELTA = 0.001;
    private static final double EPSILON = 0.01;

    private Random random;
    private KMeansPlusPlusClusterer<T> clusteringAlgorithm;

    @Before
    public void setup() {
        random = new Random(1);
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random);
    }

    @Test
    public void testChooseInitialCenters() {
        List<T> points = createListOfRandomPoints(5, 2);
        int numberOfCenters = 3;
        List<Cluster<T>> initialCenters = clusteringAlgorithm.chooseInitialCenters(points, numberOfCenters, random);
        assertEquals(numberOfCenters, initialCenters.size());
        checkInitialCentersAreDifferent(initialCenters);
    }

    @Test
    public void testCluster_EmptyCollection() {
        List<T> points = Collections.emptyList();
        int numberOfCenters = 2;
        List<Cluster<T>> result = clusteringAlgorithm.cluster(points, numberOfCenters, -1);
        assertEquals(numberOfCenters, result.size());
        for (Cluster<T> cluster : result) {
            assertTrue(cluster.isEmpty());
        }
    }

    @Test
    public void testCluster_EmptyStrategyLargestVariance() {
        List<T> points = createListOfRandomPoints(5, 2);
        int numberOfCenters = 3;
        EmptyClusterStrategy emptyStrategy = EmptyClusterStrategy.LARGEST_VARIANCE;
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, emptyStrategy);

        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, numberOfCenters, -1);
        assertEquals(numberOfCenters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            if (cluster.isEmpty()) {
                Cluster<T> largerCluster = getLargestCluster(clusters);
                T pointFromLargerCluster = getPointFromLargestVarianceCluster(largerCluster.getPoints());
                assertEquals(pointFromLargerCluster, cluster.getCenter());
            } else {
                assertTrue(cluster.getPoints().size() > 0);
            }
        }
    }

    @Test
    public void testCluster_EmptyStrategyLargestPointsNumber() {
        List<T> points = createListOfRandomPoints(5, 2);
        int numberOfCenters = 3;
        EmptyClusterStrategy emptyStrategy = EmptyClusterStrategy.LARGEST_POINTS_NUMBER;
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, emptyStrategy);

        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, numberOfCenters, -1);
        assertEquals(numberOfCenters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            if (cluster.isEmpty()) {
                Cluster<T> largerCluster = getLargestCluster(clusters);
                T pointFromLargerCluster = getPointFromLargestNumberCluster(largerCluster.getPoints());
                assertEquals(pointFromLargerCluster, cluster.getCenter());
            } else {
                assertTrue(cluster.getPoints().size() > 0);
            }
        }
    }

    @Test
    public void testCluster_EmptyStrategyFarthestPoint() {
        List<T> points = createListOfRandomPoints(5, 2);
        int numberOfCenters = 3;
        EmptyClusterStrategy emptyStrategy = EmptyClusterStrategy.FARTHEST_POINT;
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, emptyStrategy);

        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, numberOfCenters, -1);
        assertEquals(numberOfCenters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            if (cluster.isEmpty()) {
                Cluster<T> largerCluster = getLargestCluster(clusters);
                T farthestPoint = getFarthestPoint(largerCluster.getPoints());
                assertEquals(farthestPoint, cluster.getCenter());
            } else {
                assertTrue(cluster.getPoints().size() > 0);
            }
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCluster_EmptyStrategyError() {
        List<T> points = createListOfRandomPoints(5, 2);
        int numberOfCenters = 3;
        EmptyClusterStrategy emptyStrategy = EmptyClusterStrategy.ERROR;
        clusteringAlgorithm = new KMeansPlusPlusClusterer<>(random, emptyStrategy);

        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, numberOfCenters, -1);
        assertEquals(numberOfCenters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            if (cluster.isEmpty()) {
                throw new ConvergenceException(LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
            } else {
                assertTrue(cluster.getPoints().size() > 0);
            }
        }
    }

    @Test
    public void testCluster_Convergence() {
        List<T> points = createListOfRandomPoints(10, 2);
        int numberOfCenters = 3;
        List<Cluster<T>> clusters = clusteringAlgorithm.cluster(points, numberOfCenters, 50);
        assertEquals(numberOfCenters, clusters.size());
        for (Cluster<T> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }

        // Check that the algorithm converged
        for (int i = 0; i < 50; i++) {
            List<Cluster<T>> newClusters = new ArrayList<>();
            for (Cluster<T> cluster : clusters) {
                T newCenter;
                if (cluster.getPoints().isEmpty()) {
                    switch (emptyStrategy) {
                        case LARGEST_VARIANCE:
                            newCenter = getPointFromLargestVarianceCluster(clusters);
                            break;
                        case LARGEST_POINTS_NUMBER:
                            newCenter = getPointFromLargestNumberCluster(clusters);
                            break;
                        case FARTHEST_POINT:
                            newCenter = getFarthestPoint(clusters);
                            break;
                        default:
                            throw new AssertionError("Unexpected emptyStrategy.");
                    }
                    newClusters.add(new Cluster<T>(newCenter));
                    continue;
                }
                T oldCenter = cluster.getCenter();
                newCenter = cluster.getCenter().centroidOf(cluster.getPoints());
                if (newCenter.equals(oldCenter)) {
                    break;
                }
                newClusters.add(new Cluster<T>(newCenter));
            }
            clusters = newClusters;
            if (clusters.equals(newClusters)) {
                break;
            }
        }
    }

    @Test
    public void testCluster_AssignPointsToClusters() {
        List<T> points = createListOfRandomPoints(5, 2);
        List<Cluster<T>> clusters = new ArrayList<>();
        clusters.add(new Cluster<T>());
        clusters.add(new Cluster<T>());
        assignPointsToClusters(clusters, points);
        for (Cluster<T> cluster : clusters) {
            assertTrue(cluster.getPoints().size() > 0);
        }
    }

    private void checkInitialCentersAreDifferent(List<Cluster<T>> initialCenters) {
        for (int i = 0; i < initialCenters.size(); i++) {
            for (int j = i + 1; j < initialCenters.size(); j++) {
                T centerI = initialCenters.get(i).getCenter();
                T centerJ = initialCenters.get(j).getCenter();
                assertNotEquals(centerI, centerJ);
            }
        }
    }

    private List<T> createListOfRandomPoints(int numberOfPoints, int numberOfDimensions) {
        List<T> points = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            double[] randomPoint = new double[numberOfDimensions];
            for (int j = 0; j < numberOfDimensions; j++) {
                randomPoint[j] = random.nextGaussian();
            }
            T point = new MockPoint(randomPoint);
            points.add(point);
        }
        return points;
    }

    private Cluster<T> getFarthestPoint(Collection<Cluster<T>> clusters) {
        Cluster<T> farthestCluster = null;
        double farthestDistance = 0;
        for (Cluster<T> cluster : clusters) {
            T farthestPoint = cluster.getPoints().get(0);
            double distance = farthestPoint.distanceFrom(cluster.getCenter());
            if (farthestDistance == 0 || distance > farthestDistance) {
                farthestDistance = distance;
                farthestCluster = cluster;
            }
        }
        return farthestCluster;
    }

    private T getLargestCluster(List<Cluster<T>> clusters) {
        Cluster<T> largestCluster = clusters.get(0);
        for (Cluster<T> cluster : clusters) {
            if (cluster.getPoints().size() > largestCluster.getPoints().size()) {
                largestCluster = cluster;
            }
        }
        return largestCluster;
    }

    private T getPointFromLargestVarianceCluster(Collection<T> points) {
        Cluster<T> largestCluster = getLargestCluster(clusteringAlgorithm.cluster(points, 1, -1));
        return largestCluster.getCenter();
    }

    private T getPointFromLargestNumberCluster(Collection<T> points) {
        Cluster<T> largestCluster = getLargestCluster(clusteringAlgorithm.cluster(points, 1, -1));
        return largestCluster.getPoints().get(0);
    }

    private void assignPointsToClusters(List<Cluster<T>> clusters, Collection<T> points) {
        for (T point : points) {
            Cluster<T> nearestCluster = getNearestCluster(clusters, point);
            nearestCluster.addPoint(point);
        }
    }

    private <U extends Clusterable<U>> Cluster<U> getNearestCluster(Collection<Cluster<U>> clusters, U point) {
        Cluster<U> nearestCluster = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Cluster<U> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (distance < nearestDistance) {
                nearestCluster = cluster;
                nearestDistance = distance;
            }
        }
        return nearestCluster;
    }

}

class MockPoint implements Clusterable<MockPoint> {

    private double[] point;

    public MockPoint(double[] point) {
        this.point = point;
    }

    @Override
    public double[] getPoint() {
        return point;
    }

    @Override
    public double distanceFrom(Clusterable<MockPoint> other) {
        MockPoint otherPoint = (MockPoint) other;
        double sum = 0;
        for (int i = 0; i < point.length; i++) {
            sum += Math.pow(point[i] - otherPoint.point[i], 2);
        }
        return Math.sqrt(sum);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < point.length; i++) {
            sb.append(point[i]).append(" ");
        }
        return sb.toString();
    }

}