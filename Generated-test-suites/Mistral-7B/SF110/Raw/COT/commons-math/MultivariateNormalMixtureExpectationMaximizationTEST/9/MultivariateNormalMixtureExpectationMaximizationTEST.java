package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization mmeme;
    private double[][] data;

    public MultivariateNormalMixtureExpectationMaximizationTest(double[][] data) {
        try {
            this.mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown in constructor: " + e.getMessage());
        }
    }

    @Parameterized.Parameters(name = "{index}: data = {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}}}
        });
    }

    @Test
    public void testConstructor() {
        assertEquals(this.data.length, this.mmeme.getData().length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        new MultivariateNormalMixtureExpectationMaximization(new double[0][]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfWrongSize() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 3.5}});
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataOfWrongNumberOfColumns() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {1.5, 1.5}, {2.0, 3.0}, {2.5, 3.5}});
    }

    @Test
    public void testFit() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalDistribution.getDefaultDistribution(1.0, 1.0, 0.5), 0.5);
        this.mmeme.fit(initialMixture);
        assertEquals("Unexpected log likelihood value.", this.mmeme.getLogLikelihood(), 0.0, 1e-6);
        MixtureMultivariateNormalDistribution fittedModel = this.mmeme.getFittedModel();
        assertEquals("Unexpected number of components.", fittedModel.getComponents().size(), 1);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularMatrixException() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalDistribution.getDefaultDistribution(1.0, 1.0, 0.5), 0.5);
        this.mmeme.fit(initialMixture, 1, 1.0E-15);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalDistribution.getDefaultDistribution(1.0, 1.0, 0.5), 0.5);
        this.mmeme.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalDistribution.getDefaultDistribution(1.0, 1.0, 0.5), 0.5);
        this.mmeme.fit(initialMixture, 1000, -1.0);
    }

    @Test
    public void testFitWithDefaults() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalDistribution.getDefaultDistribution(1.0, 1.0, 0.5), 0.5);
        this.mmeme.fit(initialMixture);
        assertEquals("Unexpected number of components.", this.mmeme.getFittedModel().getComponents().size(), 1);
    }

    @Test
    public void testEstimate() throws Exception {
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(this.data, 2);
        assertEquals("Unexpected number of components.", result.getComponents().size(), 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfComponents() throws Exception {
        MultivariateNormalMixtureExpectationMaximization.estimate(this.data, 1);
    }

    @Test
    public void testEstimateWithTwoComponents() throws Exception {
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(this.data, 2);
        assertEquals("Unexpected number of components.", result.getComponents().size(), 2);

        MixtureMultivariateNormalDistribution firstComponent = result.getComponents().get(0);
        assertEquals("Unexpected number of means for the first component.", firstComponent.getSecond().getMeans().length, this.data[0].length);

        MixtureMultivariateNormalDistribution secondComponent = result.getComponents().get(1);
        assertEquals("Unexpected number of means for the second component.", secondComponent.getSecond().getMeans().length, this.data[0].length);

        MultivariateNormalDistribution firstDistribution = firstComponent.getSecond();
        MultivariateNormalDistribution secondDistribution = secondComponent.getSecond();

        RealMatrix firstCovarianceMatrix = firstDistribution.getCovarianceMatrix();
        RealMatrix secondCovarianceMatrix = secondDistribution.getCovarianceMatrix();

        assertTrue("Unexpected covariance matrices for the first and second components.", firstCovarianceMatrix.equals(secondCovarianceMatrix));
    }
}