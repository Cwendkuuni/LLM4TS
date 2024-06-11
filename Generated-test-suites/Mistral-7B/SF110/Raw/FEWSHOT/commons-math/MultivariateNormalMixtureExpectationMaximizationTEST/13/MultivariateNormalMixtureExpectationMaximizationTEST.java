package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.DataFormats;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testEstimate() {
        double[][] data = {
            {1.0, 2.0},
            {1.1, 2.1},
            {1.2, 2.2},
            {1.3, 2.3},
            {1.4, 2.4},
            {1.5, 2.5},
            {1.6, 2.6},
            {1.7, 2.7},
            {1.8, 2.8},
            {1.9, 2.9}
        };
        
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        expectedModel.getComponents().get(0).setMeans(DataFormats.toDoubleArray(new double[][]{
            {1.5, 2.5},
            {1.55, 2.55},
        }));
        expectedModel.getComponents().get(0).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
            {0.1, 0.05},
            {0.05, 0.1},
        }));
        expectedModel.getComponents().get(1).setMeans(DataFormats.toDoubleArray(new double[][]{
            {1.65, 2.65},
            {1.7, 2.7},
        }));
        expectedModel.getComponents().get(1).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
            {0.2, 0.01},
            {0.01, 0.2},
        }));

        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        
        assertEquals(expectedModel.getComponents().size(), model.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            Pair<Double, MultivariateNormalDistribution> expectedComponent = expectedModel.getComponents().get(i);
            Pair<Double, MultivariateNormalDistribution> actualComponent = model.getComponents().get(i);

            assertEquals(expectedComponent.getFirst(), actualComponent.getFirst(), 0.001);
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[0], actualComponent.getSecond().getMeans()[0], 0.001));
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[1], actualComponent.getSecond().getMeans()[1], 0.001));
            assertTrue(Arrays.deepEquals(expectedComponent.getSecond().getCovarianceMatrix().getData(), actualComponent.getSecond().getCovarianceMatrix().getData()));
        }
    }

    @Test
    public void testFit() {
        double[][] data = {
            {1.0, 2.0},
            {1.1, 2.1},
            {1.2, 2.2},
            {1.3, 2.3},
            {1.4, 2.4},
            {1.5, 2.5},
            {1.6, 2.6},
            {1.7, 2.7},
            {1.8, 2.8},
            {1.9, 2.9}
        };

        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        initialModel.getComponents().get(0).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.5, 2.5},
                {1.55, 2.55},
        }));
        initialModel.getComponents().get(0).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.1, 0.05},
                {0.05, 0.1},
        }));
        initialModel.getComponents().get(1).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.65, 2.65},
                {1.7, 2.7},
        }));
        initialModel.getComponents().get(1).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.2, 0.01},
                {0.01, 0.2},
        }));

        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(data);
        estimator.fit(initialModel);

        MixtureMultivariateNormalDistribution fittedModel = estimator.getFittedModel();

        assertEquals(initialModel.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < initialModel.getComponents().size(); ++i) {
            Pair<Double, MultivariateNormalDistribution> expectedComponent = initialModel.getComponents().get(i);
            Pair<Double, MultivariateNormalDistribution> actualComponent = fittedModel.getComponents().get(i);

            assertEquals(expectedComponent.getFirst(), actualComponent.getFirst(), 0.001);
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[0], actualComponent.getSecond().getMeans()[0], 0.001));
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[1], actualComponent.getSecond().getMeans()[1], 0.001));
            assertTrue(Arrays.deepEquals(expectedComponent.getSecond().getCovarianceMatrix().getData(), actualComponent.getSecond().getCovarianceMatrix().getData()));
        }

        double logLikelihood = estimator.getLogLikelihood();
        assertTrue(Precision.isClose(logLikelihood, -12.149331312017448, 0.001));
    }

    @Test(expected = ConvergenceException.class)
    public void testFitConvergenceException() {
        double[][] data = {
                {1.0, 2.0},
                {1.1, 2.1},
                {1.2, 2.2},
                {1.3, 2.3},
                {1.4, 2.4},
                {1.5, 2.5},
                {1.6, 2.6},
                {1.7, 2.7},
                {1.8, 2.8},
                {1.9, 2.9},
                {1.0, 2.0},
                {1.1, 2.1},
                {1.2, 2.2},
                {1.3, 2.3},
                {1.4, 2.4},
                {1.5, 2.5},
                {1.6, 2.6},
                {1.7, 2.7},
                {1.8, 2.8},
                {1.9, 2.9},
                {1.0, 2.0},
                {1.1, 2.1},
                {1.2, 2.2},
                {1.3, 2.3},
                {1.4, 2.4},
                {1.5, 2.5},
                {1.6, 2.6},
                {1.7, 2.7},
                {1.8, 2.8},
                {1.9, 2.9}
        };

        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        initialModel.getComponents().get(0).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.5, 2.5},
                {1.55, 2.55},
        }));
        initialModel.getComponents().get(0).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.1, 0.05},
                {0.05, 0.1},
        }));
        initialModel.getComponents().get(1).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.65, 2.65},
                {1.7, 2.7},
        }));
        initialModel.getComponents().get(1).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.2, 0.01},
                {0.01, 0.2},
        }));

        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(data);
        estimator.fit(initialModel, 10, 1.0E-11);
    }

    @Test
    public void testFitWithInitialModel() {
        double[][] data = {
                {1.0, 2.0},
                {1.1, 2.1},
                {1.2, 2.2},
                {1.3, 2.3},
                {1.4, 2.4},
                {1.5, 2.5},
                {1.6, 2.6},
                {1.7, 2.7},
                {1.8, 2.8},
                {1.9, 2.9}
        };

        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        initialModel.getComponents().get(0).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.5, 2.5},
                {1.55, 2.55},
        }));
        initialModel.getComponents().get(0).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.1, 0.05},
                {0.05, 0.1},
        }));
        initialModel.getComponents().get(1).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.65, 2.65},
                {1.7, 2.7},
        }));
        initialModel.getComponents().get(1).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.2, 0.01},
                {0.01, 0.2},
        }));

        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        expectedModel.getComponents().get(0).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.50289232, 2.50021035},
                {1.54691253, 2.53583462},
        }));
        expectedModel.getComponents().get(0).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.10642059, 0.05779883},
                {0.05779883, 0.10642059},
        }));
        expectedModel.getComponents().get(1).setMeans(DataFormats.toDoubleArray(new double[][]{
                {1.64803532, 2.63929374},
                {1.6897513, 2.68160515},
        }));
        expectedModel.getComponents().get(1).getSecond().setCovarianceMatrix(new Array2DRowRealMatrix(new double[][]{
                {0.19531508, 0.01461384},
                {0.01461384, 0.19531508},
        }));

        MultivariateNormalMixtureExpectationMaximization estimator = new MultivariateNormalMixtureExpectationMaximization(data);
        estimator.fit(initialModel);

        MixtureMultivariateNormalDistribution fittedModel = estimator.getFittedModel();

        assertEquals(expectedModel.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            Pair<Double, MultivariateNormalDistribution> expectedComponent = expectedModel.getComponents().get(i);
            Pair<Double, MultivariateNormalDistribution> actualComponent = fittedModel.getComponents().get(i);

            assertEquals(expectedComponent.getFirst(), actualComponent.getFirst(), 0.001);
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[0], actualComponent.getSecond().getMeans()[0], 0.001));
            assertTrue(Precision.isClose(expectedComponent.getSecond().getMeans()[1], actualComponent.getSecond().getMeans()[1], 0.001));
            assertTrue(Arrays.deepEquals(expectedComponent.getSecond().getCovarianceMatrix().getData(), actualComponent.getSecond().getCovarianceMatrix().getData()));
        }

        double logLikelihood = estimator.getLogLikelihood();
        assertTrue(Precision.isClose(logLikelihood, -11.549215444524928, 0.001));
    }
}