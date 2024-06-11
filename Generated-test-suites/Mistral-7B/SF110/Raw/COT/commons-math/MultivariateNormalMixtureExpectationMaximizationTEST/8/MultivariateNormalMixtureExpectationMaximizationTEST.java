package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.data.Array2DRowRealMatrix;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = { {1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2} };
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroDataLength() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDataRowMismatch() {
        double[][] data = { {1.0, 2.0}, {1.1, 2.1, 3.1} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorDataRowColumnTooSmall() {
        double[][] data = { {1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2, 1} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { {1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture, 10, 0.001);

        double tolerance = 1e-5;
        double expectedLogLikelihood = -1.981128332521443;
        assertEquals("Incorrect log likelihood after fitting.", expectedLogLikelihood, mnme.getLogLikelihood(), tolerance);

        int numComponents = initialMixture.getComponents().size();
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertEquals("Incorrect number of components in the fitted model.", numComponents, fittedModel.getComponents().size());

        for (int i = 0; i < numComponents; ++i) {
            MultivariateNormalDistribution component = fittedModel.getComponentDistribution(i);
            double[] means = component.getMeans();
            double[] covariance = component.getCovarianceMatrix().getData()[0];
            double toleranceMean = 1e-5;
            double toleranceCovariance = 1e-3;

            double[] expectedMeans = { 1.523461034458617, 2.502236155788468 };
            for (int j = 0; j < means.length; ++j) {
                assertEquals("Incorrect mean for component " + i + " in the x-axis.", expectedMeans[j], means[j], toleranceMean);
            }

            RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][] { {3.217381083992447, 0.890472467948591}, {0.890472467948591, 2.345033074619261} });
            RealMatrix actualCovarianceMatrix = component.getCovarianceMatrix();
            assertTrue("Incorrect covariance matrix for component " + i, actualCovarianceMatrix.isSimilarTo(expectedCovarianceMatrix, toleranceCovariance));
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrixException() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { {1.0, 2.0}, {1.1, 2.1}, {1.2, 2.2, 3.3}, {1.3, 2.3} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture, 10, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { {1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture, 10, -0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { {1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture, -1, 0.001);
    }

    @Test
    public void testFitWithoutInitialMixture() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { {1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}, {1.3, 2.3, 3.3}, {1.4, 2.4, 3.4} };
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(10, 0.001);

        double tolerance = 1e-5;
        double expectedLogLikelihood = -1.981128332521443;
        assertEquals("Incorrect log likelihood after fitting.", expectedLogLikelihood, mnme.getLogLikelihood(), tolerance);
    }
}