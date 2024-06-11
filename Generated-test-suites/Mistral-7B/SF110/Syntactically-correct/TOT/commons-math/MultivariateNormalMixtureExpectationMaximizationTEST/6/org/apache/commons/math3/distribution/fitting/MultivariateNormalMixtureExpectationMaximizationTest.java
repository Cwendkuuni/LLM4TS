package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
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
        double[][] data = {{1.0, 2.0}, {2.0}, {1.0, 1.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataOfInsufficientLength() {
        double[][] data = {{1.0, 2.0}, {1.0, 1.0}, {1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mmem.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mmem.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularMatrix() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 0.0}, {1.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mmem.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mmem.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mmem.fit(initialMixture, 1000, -1.0E-5);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(2, fittedModel.getComponents().size());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithNegativeNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataOfIncorrectLength() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0}, {2.0, 1.0}, {1.0, 1.0}};
        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mmem.fit(new MixtureMultivariateNormalDistribution(2));
            double logLikelihood = mmem.getLogLikelihood();
            assertNotNull(logLikelihood);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mmem = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
            mmem.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = mmem.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}