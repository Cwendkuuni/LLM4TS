package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = null;
        try {
            mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Unexpected exception while constructing MultivariateNormalMixtureExpectationMaximization instance.");
        }
        assertNotNull(mnme);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = new double[0][];
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfDifferentSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataOfInvalidSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInvalidInitialMixture() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = null;
        try {
            mnme.fit(initialMixture, 1000, 1.0E-5);
        } catch (NullPointerException e) {
            // Expected result
        }
        assertNull(mnme.getFittedModel());
    }

    @Test
    public void testFitWithValidInitialMixtureAndParams() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        mnme.fit(initialMixture, 1000, 1.0E-5);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(3, fittedModel.getComponents().size());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        mnme.fit(initialMixture, 1000, 1.0E-5);
        mnme.fit(initialMixture); // Second call to trigger the convergence exception
    }

    @Test
    public void testFitWithMaxIterationsAndThreshold() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        mnme.fit(initialMixture, 1, 1.0);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(1, fittedModel.getComponents().size());
    }

    @Test
    public void testEstimate() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution expectedResult = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        MixtureMultivariateNormalDistribution result = new MultivariateNormalMixtureExpectationMaximization().estimate(data, 3);
        assertArrayEquals(expectedResult.getComponents().toArray(), result.getComponents().toArray());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumComponents() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization().estimate(data, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithInvalidData() {
        double[][] data = new double[][]{{1.0}, {1.5, 2.5}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization().estimate(data, 3);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        mnme.fit(initialMixture, 1000, 1.0E-5);
        double logLikelihood = mnme.getLogLikelihood();
        assertTrue(logLikelihood > Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        mnme.fit(initialMixture, 1000, 1.0E-5);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(3, fittedModel.getComponents().size());
    }
}