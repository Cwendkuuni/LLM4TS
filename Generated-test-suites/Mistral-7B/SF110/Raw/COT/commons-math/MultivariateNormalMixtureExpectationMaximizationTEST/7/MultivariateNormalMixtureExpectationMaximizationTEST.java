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

    private MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization;
    private double[][] data;

    public MultivariateNormalMixtureExpectationMaximizationTest(double[][] data) {
        try {
            this.multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Initialization failed: " + e.getMessage());
        }
    }

    @Parameterized.Parameters(name = "Data: {\n{0}\n}")
    public static Collection<Object[]> dataProvider() {
        return Arrays.asList(new Object[][]{
                {new double[][]{
                        {1.0, 2.0},
                        {1.0, 3.0},
                        {2.0, 2.0},
                        {2.0, 3.0}
                }},
                {new double[][]{
                        {1.0, 2.0, 3.0},
                        {1.0, 2.0, 4.0},
                        {2.0, 2.0, 3.0},
                        {2.0, 2.0, 4.0}
                }},
                {new double[][]{
                        {1.0, 2.0, 3.0, 4.0},
                        {1.0, 2.0, 3.0, 5.0},
                        {2.0, 2.0, 3.0, 4.0},
                        {2.0, 2.0, 3.0, 5.0}
                }}
        });
    }

    @Test
    public void testConstructor() {
        assertEquals(multivariateNormalMixtureExpectationMaximization.getData().length, data.length);
    }

    @Test
    public void testFit() throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(3, 3))));
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 1000, 1.0E-5);
        assertNotNull(multivariateNormalMixtureExpectationMaximization.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(3, 3))));
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(3, 3))));
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 1000, -1.0E-5);
    }

    @Test
    public void testFitWithDefaults() throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(3, 3))));
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture);
        assertNotNull(multivariateNormalMixtureExpectationMaximization.getFittedModel());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() throws NotStrictlyPositiveException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(3, 3).scalarMultiply(0.0))));
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithMismatchedDataAndInitialMixture() throws NotStrictlyPositiveException, SingularMatrixException {
        double[][] data = {
                {1.0, 2.0},
                {1.0, 3.0},
                {2.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0).getParameters(), new Array2DRowRealMatrix(2, 2))));
        multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(data);
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {
                {1.0, 2.0},
                {1.0, 3.0},
                {2.0, 2.0},
                {2.0, 3.0}
        };
        MixtureMultivariateNormalDistribution expectedResult = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution result = multivariateNormalMixtureExpectationMaximization.getFittedModel();
        assertEquals(expectedResult.getComponents().size(), result.getComponents().size());
        for (int i = 0; i < result.getComponents().size(); ++i) {
            MultivariateNormalDistribution resultComponent = result.getComponents().get(i).getSecond();
            MultivariateNormalDistribution expectedComponent = expectedResult.getComponents().get(i).getSecond();
            assertEquals(resultComponent.getMeans().length, expectedComponent.getMeans().length);
            for (int j = 0; j < resultComponent.getMeans().length; ++j) {
                assertEquals(expectedComponent.getMeans()[j], resultComponent.getMeans()[j], 1e-5);
            }
            assertEquals(resultComponent.getCovarianceMatrix().getRowDimension(), resultComponent.getCovarianceMatrix().getColumnDimension(), 1);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(0, 0).getValue(), expectedComponent.getCovarianceMatrix().getEntry(0, 0).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(0, 1).getValue(), expectedComponent.getCovarianceMatrix().getEntry(0, 1).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(1, 0).getValue(), expectedComponent.getCovarianceMatrix().getEntry(1, 0).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(1, 1).getValue(), expectedComponent.getCovarianceMatrix().getEntry(1, 1).getValue(), 1e-5);
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double expectedLogLikelihood = Math.log(1.0 / (2.0 * Math.PI * 2.0 * 2.0)) - 1.0;
        assertEquals(multivariateNormalMixtureExpectationMaximization.getLogLikelihood(), expectedLogLikelihood, 1e-5);
    }

    @Test
    public void testGetFittedModel() {
        MixtureMultivariateNormalDistribution expectedResult = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution result = multivariateNormalMixtureExpectationMaximization.getFittedModel();
        assertEquals(expectedResult.getComponents().size(), result.getComponents().size());
        for (int i = 0; i < result.getComponents().size(); ++i) {
            MultivariateNormalDistribution resultComponent = result.getComponents().get(i).getSecond();
            MultivariateNormalDistribution expectedComponent = expectedResult.getComponents().get(i).getSecond();
            assertEquals(resultComponent.getMeans().length, expectedComponent.getMeans().length);
            for (int j = 0; j < resultComponent.getMeans().length; ++j) {
                assertEquals(expectedComponent.getMeans()[j], resultComponent.getMeans()[j], 1e-5);
            }
            assertEquals(resultComponent.getCovarianceMatrix().getRowDimension(), resultComponent.getCovarianceMatrix().getColumnDimension(), 1);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(0, 0).getValue(), expectedComponent.getCovarianceMatrix().getEntry(0, 0).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(0, 1).getValue(), expectedComponent.getCovarianceMatrix().getEntry(0, 1).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(1, 0).getValue(), expectedComponent.getCovarianceMatrix().getEntry(1, 0).getValue(), 1e-5);
            assertEquals(resultComponent.getCovarianceMatrix().getEntry(1, 1).getValue(), expectedComponent.getCovarianceMatrix().getEntry(1, 1).getValue(), 1e-5);
        }
    }
}

class DataRow implements Comparable<DataRow> {
    // The same as in the original class
}