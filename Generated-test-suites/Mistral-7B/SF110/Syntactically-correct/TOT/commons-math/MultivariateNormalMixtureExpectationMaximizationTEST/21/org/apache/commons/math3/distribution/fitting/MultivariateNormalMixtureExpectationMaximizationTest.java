package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithValidData() {
        double[][] inputData = {
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0, 5.0},
                {5.0, 6.0}
        };

        MixtureMultivariateNormalDistribution expected = estimate(inputData, 2);

        try {
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(inputData);
            mmeme.fit(expected);
            MixtureMultivariateNormalDistribution actual = mmeme.getFittedModel();
            Assert.assertEquals(expected.getComponents().size(), actual.getComponents().size());
            for (int i = 0; i < expected.getComponents().size(); i++) {
                MultivariateNormalDistribution actualComponent = actual.getComponents().get(i).getSecond();
                MultivariateNormalDistribution expectedComponent = expected.getComponents().get(i).getSecond();
                RealMatrix expectedCovariance = expectedComponent.getCovarianceMatrix().getData();
                RealMatrix actualCovariance = actualComponent.getCovarianceMatrix().getData();
                for (int j = 0; j < expectedCovariance.getRowDimension(); j++) {
                    for (int k = 0; k < expectedCovariance.getColumnDimension(); k++) {
                        Assert.assertEquals(expectedCovariance.getEntry(j, k), actualCovariance.getEntry(j, k), 0.001);
                    }
                }
                for (int j = 0; j < expected.getComponents().get(i).getFirst(); j++) {
                    double[] expectedDataPoint = inputData[j];
                    double[] actualDataPoint = mmeme.data[j];
                    Assert.assertArrayEquals(expectedDataPoint, actualDataPoint, 0.001);
                }
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFitWithEmptyData() {
        double[][] inputData = {};

        try {
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(inputData);
            Assert.assertThrows(NotStrictlyPositiveException.class, () -> mmeme.fit(null));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFitWithInvalidData() {
        double[][] inputData = {
                {1.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0, 5.0},
                {5.0, 6.0},
                {6.0, 7.0, 8.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(inputData);
            Assert.assertThrows(DimensionMismatchException.class, () -> mmeme.fit(null));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFitWithSingularCovarianceMatrices() {
        double[][] inputData = {
                {1.0, 0.0},
                {0.0, 1.0},
        };

        try {
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new Pair<Double, MultivariateNormalDistribution>(1.0, new MultivariateNormalDistribution(new RealMatrix[]{}))));
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(inputData);
            Assert.assertThrows(SingularMatrixException.class, () -> mmeme.fit(initialMixture));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testEstimateWithInvalidNumberOfComponents() {
        double[][] inputData = {
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0, 5.0},
                {5.0, 6.0}
        };

        try {
            Assert.assertThrows(NumberIsTooSmallException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(inputData, 1));
            Assert.assertThrows(NumberIsTooLargeException.class, () -> MultivariateNormalMixtureExpectationMaximization.estimate(inputData, inputData.length + 1));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private static MixtureMultivariateNormalDistribution estimate(double[][] data, int numComponents) {
        try {
            return MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        } catch (Exception e) {
            throw new RuntimeException("Failed to estimate mixture model", e);
        }
    }

    private static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getFirst() {
            return this.key;
        }

        public V getSecond() {
            return this.value;
        }
    }
}