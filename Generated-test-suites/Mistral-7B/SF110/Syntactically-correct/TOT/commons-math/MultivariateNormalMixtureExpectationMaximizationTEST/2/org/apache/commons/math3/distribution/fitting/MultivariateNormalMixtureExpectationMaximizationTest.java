package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfDifferentSizes() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1, 3.0 }, { 1.2, 2.2 } };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataOfSizeLessThanTwo() {
        double[][] data = { { 1.0 } };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInitialModelAndMaxIterationsAndThreshold() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3 }, { 1.4, 2.4 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture, 10, 1e-5);
        assertEquals(5, initialMixture.getComponents().size());
        // Add more assertions to check the properties of the fitted model
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        double[][] data = { { 1.0, 2.0, 0.0 }, { 1.1, 2.1, 0.0 }, { 1.2, 2.2, 0.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture, 10, -1.0E-5);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithUnconvergedModel() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture, 1, 1.0E-5);
    }

    @Test
    public void testFitWithDefaults() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture);
        // Add more assertions to check the properties of the fitted model
    }

    @Test
    public void testEstimate() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(2, estimatedModel.getComponents().size());
        // Add more assertions to check the properties of the estimated model
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithZeroNumComponents() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithNumComponentsLargerThanDataSize() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MultivariateNormalMixtureExpectationMaximization.estimate(data, data.length + 1);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(new MixtureMultivariateNormalDistribution(new ArrayList<>()));
        double logLikelihood = mmeme.getLogLikelihood();
        // Add assertion to check the value of the logLikelihood
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        mmeme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        // Add assertions to check the properties of the fitted model
    }
}