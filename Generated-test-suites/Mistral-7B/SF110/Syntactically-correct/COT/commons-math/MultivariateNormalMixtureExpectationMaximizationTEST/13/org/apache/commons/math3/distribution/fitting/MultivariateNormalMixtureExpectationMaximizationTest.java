package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        try {
            double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(mmeme.getFittedModel());
        } catch (Exception e) {
            fail("Exception should not be thrown in testConstructor");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDataLength() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {-1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDataRowMismatch() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0, 4.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorDataRowTooSmall() {
        double[][] data = new double[][]{{1.0}, {1.5, 2.5}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}}))));
        mmeme.fit(initialMixture, 10, 0.01);

        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        double logLikelihood = mmeme.getLogLikelihood();

        // Check the size and means of components
        List<MultivariateNormalDistribution> components = fittedModel.getComponents();
        assertEquals(2, components.size());
        for (MultivariateNormalDistribution component : components) {
            assertNotNull(component.getMeans());
            assertEquals(2, component.getMeans().length);
            for (double mean : component.getMeans()) {
                assertTrue(mean > 0.0);
            }
        }

        // Check covariance matrices
        for (MultivariateNormalDistribution component : components) {
            RealMatrix covarianceMatrix = component.getCovarianceMatrix();
            assertTrue(covarianceMatrix.getRowDimension() == 2);
            assertTrue(covarianceMatrix.getColumnDimension() == 2);
            assertTrue(covarianceMatrix.getEntry(0, 0) > 0.0);
            assertTrue(covarianceMatrix.getEntry(1, 1) > 0.0);
            assertTrue(covarianceMatrix.getEntry(0, 1) == covarianceMatrix.getEntry(1, 0));
        }

        // Check log likelihood
        double expectedLogLikelihood = 1.5 * Math.log(0.5) + 1.0 * Math.log(0.5) + Math.log(0.3989423); // Approximate value based on normal distribution log likelihood formula
        assertEquals(expectedLogLikelihood, logLikelihood, 0.01);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrix() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 0.0}, {1.5, 2.5, 0.0}, {2.0, 3.0, 0.0}}; // Singular matrix
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}}))));
        mmeme.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}}))));
        mmeme.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}}))));
        mmeme.fit(initialMixture, 1000, -1.0);
    }

    @Test
    public void testFitDefault() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{0.5, 0.5}, {0.5, 0.5}}))));
        mmeme.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        double logLikelihood = mmeme.getLogLikelihood();

        // Check the size and means of components
        List<MultivariateNormalDistribution> components = fittedModel.getComponents();
        assertEquals(2, components.size());
        for (MultivariateNormalDistribution component : components) {
            assertNotNull(component.getMeans());
            assertEquals(2, component.getMeans().length);
            for (double mean : component.getMeans()) {
                assertTrue(mean > 0.0);
            }
        }

        // Check covariance matrices
        for (MultivariateNormalDistribution component : components) {
            RealMatrix covarianceMatrix = component.getCovarianceMatrix();
            assertTrue(covarianceMatrix.getRowDimension() == 2);
            assertTrue(covarianceMatrix.getColumnDimension() == 2);
            assertTrue(covarianceMatrix.getEntry(0, 0) > 0.0);
            assertTrue(covarianceMatrix.getEntry(1, 1) > 0.0);
            assertTrue(covarianceMatrix.getEntry(0, 1) == covarianceMatrix.getEntry(1, 0));
        }

        // Check log likelihood
        double expectedLogLikelihood = 1.5 * Math.log(0.5) + 1.0 * Math.log(0.5) + Math.log(0.3989423); // Approximate value based on normal distribution log likelihood formula
        assertEquals(expectedLogLikelihood, logLikelihood, 0.01);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {3.5, 3.5}};
        MixtureMultivariateNormalDistribution actual = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})), new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{{2.5, 2.5}, {2.5, 2.5}}))));
        assertEquals(expected, actual);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateNegativeNumComponents() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {3.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateDataRowMismatch() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0, 0.0}, {3.5, 3.5, 0.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateTooFewRows() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateTooManyRows() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {3.5, 3.5}, {3.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
}