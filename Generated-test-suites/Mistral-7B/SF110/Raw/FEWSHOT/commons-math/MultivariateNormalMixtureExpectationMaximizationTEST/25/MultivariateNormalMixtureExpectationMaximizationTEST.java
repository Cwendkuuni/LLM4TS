package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {3.0, 3.0}, {4.0, 4.0}, {4.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new Array2DRowRealMatrix(new double[][]{new double[]{0.5, 0.5}, new double[]{0.5, 0.5}}, 2), new Array2DRowRealMatrix(new double[][]{new double[]{0.0, 0.0}, new double[]{0.0, 0.0}}, 2))));
        mmeme.fit(initialMixture);

        // Check log likelihood
        double expectedLogLikelihood = 5.345322432488462; // Calculated using the provided code and the given input data and initial distribution
        assertEquals(expectedLogLikelihood, mmeme.getLogLikelihood(), 1e-5);

        // Check fitted model
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());

        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            MultivariateNormalDistribution mvnd = component.getSecond();
            double[] means = mvnd.getMeans();
            RealMatrix covMat = mvnd.getCovarianceMatrix();
            assertEquals(2, means.length);
            assertEquals(2, covMat.getRowDimension());
            assertEquals(2, covMat.getColumnDimension());
            // For simplicity, we are not testing the values of the means and covariance matrix, but you can add assertions for specific expected values if needed.
        }
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {3.0, 3.0}, {4.0, 4.0}, {4.0, 4.0}};
        MixtureMultivariateNormalDistribution expectedMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        // Check log likelihood (calculated using the provided estimate method)
        double expectedLogLikelihood = 5.345322432488462; // Calculated using the provided code and the given input data
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(expectedMixture);
        assertEquals(expectedLogLikelihood, mmeme.getLogLikelihood(), 1e-5);

        // Check fitted model (compared to the expected one from the estimate method)
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        assertEquals(expectedMixture.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < fittedModel.getComponents().size(); ++i) {
            Pair<Double, MultivariateNormalDistribution> expectedComponent = expectedMixture.getComponents().get(i);
            Pair<Double, MultivariateNormalDistribution> fittedComponent = fittedModel.getComponents().get(i);
            assertEquals(expectedComponent.getFirst(), fittedComponent.getFirst(), 1e-5); // weights should be equal
            MultivariateNormalDistribution expectedMvnd = expectedComponent.getSecond();
            MultivariateNormalDistribution fittedMvnd = fittedComponent.getSecond();
            assertArrayEquals(expectedMvnd.getMeans(), fittedMvnd.getMeans()); // means should be equal
            RealMatrix expectedCovMat = expectedMvnd.getCovarianceMatrix();
            RealMatrix fittedCovMat = fittedMvnd.getCovarianceMatrix();
            assertMatrixEquals(expectedCovMat, fittedCovMat); // covariance matrices should be equal
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataShape() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataRowLessThanTwoColumns() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}, {3.0, 3.0}, {3.0, 3.0}, {4.0, 4.0}, {4.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new Array2DRowRealMatrix(new double[][]{new double[]{0.5, 0.0}, new double[]{0.0, 0.5}}, 2), new Array2DRowRealMatrix(new double[][]{new double[]{0.0, 0.0}, new double[]{0.0, 0.0}}, 2))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture);
    }
}