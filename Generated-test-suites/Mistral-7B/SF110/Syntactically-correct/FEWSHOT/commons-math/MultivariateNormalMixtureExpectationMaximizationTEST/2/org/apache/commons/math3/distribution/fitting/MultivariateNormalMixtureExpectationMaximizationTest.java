package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndCustomParams() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, 10, 1e-5);

        MixtureMultivariateNormalDistribution fittedModel = emm.getFittedModel();

        assertEquals(2, fittedModel.getComponents().size());
        MultivariateNormalDistribution firstComponent = fittedModel.getComponents().get(0).getSecond();
        MultivariateNormalDistribution secondComponent = fittedModel.getComponents().get(1).getSecond();

        assertNotNull(firstComponent);
        assertNotNull(secondComponent);

        double[] firstMean = firstComponent.getMeans();
        double[] secondMean = secondComponent.getMeans();

        assertArrayEquals(new double[]{0.5, 0.5}, firstMean, 0.001);
        assertArrayEquals(new double[]{1.5, 1.5}, secondMean, 0.001);

        RealMatrix firstCov = firstComponent.getCovarianceMatrix();
        RealMatrix secondCov = secondComponent.getCovarianceMatrix();

        assertArrayEquals(new double[][]{
                {0.25, 0.25},
                {0.25, 0.25}
        }, firstCov.getData(), 0.001);
        assertArrayEquals(new double[][]{
                {0.5, 0.5},
                {0.5, 0.5}
        }, secondCov.getData(), 0.001);

        assertEquals(0.3126, emm.getLogLikelihood(), 0.001);
    }

    @Test
    public void testEstimateWithCustomData() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        assertEquals(2, fittedModel.getComponents().size());

        MultivariateNormalDistribution firstComponent = fittedModel.getComponents().get(0).getSecond();
        MultivariateNormalDistribution secondComponent = fittedModel.getComponents().get(1).getSecond();

        double[] firstMean = firstComponent.getMeans();
        double[] secondMean = secondComponent.getMeans();

        assertArrayEquals(new double[]{0.5, 0.5}, firstMean, 0.001);
        assertArrayEquals(new double[]{1.5, 1.5}, secondMean, 0.001);

        RealMatrix firstCov = firstComponent.getCovarianceMatrix();
        RealMatrix secondCov = secondComponent.getCovarianceMatrix();

        assertArrayEquals(new double[][]{
                {0.25, 0.25},
                {0.25, 0.25}
        }, firstCov.getData(), 0.001);
        assertArrayEquals(new double[][]{
                {0.5, 0.5},
                {0.5, 0.5}
        }, secondCov.getData(), 0.001);

        assertEquals(0.3126, fittedModel.logProbability(new double[]{1.0, 2.0}), 0.001);
        assertEquals(0.3126, fittedModel.logProbability(new double[]{1.5, 2.5}), 0.001);
        assertEquals(0.3126, fittedModel.logProbability(new double[]{2.0, 3.0}), 0.001);
        assertEquals(0.3126, fittedModel.logProbability(new double[]{2.5, 3.5}), 0.001);
        assertEquals(0.3126, fittedModel.logProbability(new double[]{3.0, 4.0}), 0.001);
    }

    @Test
    public void testFitWithDefaults() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = emm.getFittedModel();

        assertEquals(2, fittedModel.getComponents().size());
        MultivariateNormalDistribution firstComponent = fittedModel.getComponents().get(0).getSecond();
        MultivariateNormalDistribution secondComponent = fittedModel.getComponents().get(1).getSecond();

        assertNotNull(firstComponent);
        assertNotNull(secondComponent);

        double[] firstMean = firstComponent.getMeans();
        double[] secondMean = secondComponent.getMeans();

        assertArrayEquals(new double[]{0.5, 0.5}, firstMean, 0.001);
        assertArrayEquals(new double[]{1.5, 1.5}, secondMean, 0.001);

        RealMatrix firstCov = firstComponent.getCovarianceMatrix();
        RealMatrix secondCov = secondComponent.getCovarianceMatrix();

        assertArrayEquals(new double[][]{
                {0.25, 0.25},
                {0.25, 0.25}
        }, firstCov.getData(), 0.001);
        assertArrayEquals(new double[][]{
                {0.5, 0.5},
                {0.5, 0.5}
        }, secondCov.getData(), 0.001);

        assertEquals(0.3126, emm.getLogLikelihood(), 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, 10, -1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDataOfIncorrectSize() throws Exception {
        double[][] data = {
                {1.0, 2.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataOfIncorrectSize() throws Exception {
        double[][] data = {
                {1.0, 2.0}
        };

        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithInitialMixtureWithFewerComponents() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, 10, 1.0E-5, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testFitWithInitialMixtureWithMoreComponentsThanData() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-1.0, -1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, 10, 1.0E-5);
    }
}