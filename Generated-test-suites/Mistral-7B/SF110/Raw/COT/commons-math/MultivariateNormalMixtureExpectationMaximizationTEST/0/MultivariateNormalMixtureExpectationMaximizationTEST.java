package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Exception should not be thrown in constructor.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidData() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorMismatchedData() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1, 3.0 } };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInitialMixture() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
            new MultivariateNormalDistribution(new NormalDistribution(NormalDistribution.StandardNormal.DISTRIBUTION), new Array2DRowRealMatrix(new double[][]{ { 0.1, 0.1 }, { 0.2, 0.2 } }))));

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 10, 1.0E-5);

        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();
        assertEquals(1, fittedModel.getComponents().size());
        MultivariateNormalDistribution component = fittedModel.getComponents().get(0).getSecond();
        RealMatrix covMat = component.getCovarianceMatrix();
        RealMatrix expectedCovMat = new Array2DRowRealMatrix(new double[][]{ { 0.14844737, 0.07422369 }, { 0.07422369, 0.14844737 } });
        assertTrue(Arrays.deepEquals(covMat.getData(), expectedCovMat.getData()));
    }

    @Test
    public void testFitDefaultParameters() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
            new MultivariateNormalDistribution(new NormalDistribution(NormalDistribution.StandardNormal.DISTRIBUTION), new Array2DRowRealMatrix(new double[][]{ { 0.1, 0.1 }, { 0.2, 0.2 } }))));

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();
        assertEquals(1, fittedModel.getComponents().size());
        MultivariateNormalDistribution component = fittedModel.getComponents().get(0).getSecond();
        RealMatrix covMat = component.getCovarianceMatrix();
        RealMatrix expectedCovMat = new Array2DRowRealMatrix(new double[][]{ { 0.14844737, 0.07422369 }, { 0.07422369, 0.14844737 } });
        assertTrue(Arrays.deepEquals(covMat.getData(), expectedCovMat.getData()));
    }

    @Test
    public void testEstimate() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution actualModel = new MultivariateNormalMixtureExpectationMaximization(data).getFittedModel();
        assertEquals(expectedModel, actualModel);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitConvergenceException() {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
            new MultivariateNormalDistribution(new NormalDistribution(NormalDistribution.StandardNormal.DISTRIBUTION), new Array2DRowRealMatrix(new double[][]{ { 0.1, 0.1 }, { 0.2, 0.2 } }))));

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1, 1.0E-5);
    }
}