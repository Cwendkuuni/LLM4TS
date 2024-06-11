package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UnivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialization() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Initialization should not throw exception.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInitializationWithNegativeData() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{-1.0, 2.0}, {2.0, -3.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInitializationWithUnevenDataDimension() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
    }

    @Test
    public void testFitNullInitialMixture() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {2.0, 3.0}}).fit(null, 1000, 1.0E-5);
            fail("Fitting with null initial mixture should throw exception.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFitEmptyInitialMixture() {
        try {
            MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.emptyList());
            mmme.fit(initialMixture, 1000, 1.0E-5);
            fail("Fitting with empty initial mixture should throw exception.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFitInvalidInitialMixtureSize() {
        try {
            double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new int[]{2, 2})))));
            mmme.fit(initialMixture, 1000, 1.0E-5);
            fail("Fitting with initial mixture size less than number of data points should throw exception.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testFitMaxIterations() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new int[]{2, 2}))),
                new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new int[]{2, 2})))));

        try {
            mmme.fit(initialMixture, 0, 1.0E-5);
            fail("Fitting with non-positive max iterations should throw exception.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testFitThresholdZero() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new int[]{2, 2}))),
                new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new int[]{2, 2})))));

        try {
            mmme.fit(initialMixture, 1000, 0);
            fail("Fitting with threshold less or equal to zero should throw exception.");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testFitRandomData() {
        double[][] data = new double[][]{{1.5, 2.5}, {2.0, 3.0}, {2.5, 1.5}, {3.0, 2.0}, {1.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.2, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new int[]{2, 2}))),
                new Pair<>(0.8, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new int[]{2, 2})))));

        mmme.fit(initialMixture);

        double expectedLogLikelihood = 5.463941878301604;
        double actualLogLikelihood = mmme.getLogLikelihood();
        assertEquals(expectedLogLikelihood, actualLogLikelihood, 1e-5);

        MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();

        double[] weights = new double[components.size()];
        for (int i = 0; i < components.size(); i++) {
            weights[i] = components.get(i).getFirst();
        }

        double[][] means = new double[components.size()][2];
        for (int i = 0; i < components.size(); i++) {
            means[i] = components.get(i).getSecond().getMeans();
        }

        double[][] covMats = new double[components.size()][2][2];
        for (int i = 0; i < components.size(); i++) {
            covMats[i] = components.get(i).getSecond().getCovarianceMatrix().getData();
        }

        UnivariateNormalDistribution normDist1 = new MultivariateNormalDistribution(means[0], covMats[0]).getComponentDistributions()[0];
        UnivariateNormalDistribution normDist2 = new MultivariateNormalDistribution(means[1], covMats[1]).getComponentDistributions()[0];

        assertEquals(0.2, weights[0], 1e-5);
        assertEquals(0.8, weights[1], 1e-5);
        assertEquals(0.0, normDist1.inverseCumulativeProbability(0.1), 1e-5);
        assertEquals(0.25, normDist2.inverseCumulativeProbability(0.3), 1e-5);
    }

    @Test
    public void testEstimate() {
        double[][] data = new double[][]{{1.5, 2.5}, {2.0, 3.0}, {2.5, 1.5}, {3.0, 2.0}, {1.0, 1.0}};

        double[][] estimatedData = new double[][]{{1.1, 1.6}, {1.9, 2.2}, {2.3, 2.4}, {2.7, 2.8}, {3.1, 3.3}};

        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{1.3, 1.7}, new Array2DRowRealMatrix(new int[]{2, 2}))),
                new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{2.1, 2.3}, new Array2DRowRealMatrix(new int[]{2, 2})))));

        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        assertEquals(expectedModel, model);

        double[][] estimatedDataSubset = new double[][]{{1.1, 1.6}, {1.9, 2.2}};
        MixtureMultivariateNormalDistribution subsetModel = MultivariateNormalMixtureExpectationMaximization.estimate(estimatedDataSubset, 2);

        assertEquals(expectedModel.getComponents().get(0).getSecond(), subsetModel.getComponents().get(0).getSecond());
        assertEquals(expectedModel.getComponents().get(1).getSecond(), subsetModel.getComponents().get(1).getSecond());
    }
}