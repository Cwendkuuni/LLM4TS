package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.distribution.fitting.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationConstructor() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(mnmem.getFittedModel());
        assertArrayEquals(new double[][]{ {1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0} }, mnmem.getData(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorEmptyData() {
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[0][]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorDataWithWrongNumberOfColumns() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0, 4.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    // Testing fit method with valid input
    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationFit() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}});
        mnmem.fit(initialMixture, 10, 0.001);
        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();
        double logLikelihood = mnmem.getLogLikelihood();
        assertEquals(2.0, fittedModel.getComponents().size());
        assertNotNull(fittedModel.getComponents().get(0).getFirst());
        assertNotNull(fittedModel.getComponents().get(1).getFirst());
        assertEquals(logLikelihood, mnmem.logLikelihood, 0.0);
    }

    // Testing fit method with invalid input
    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationFitSingularMatrixException() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}});
        mnmem.fit(initialMixture, 10, 0.001);
        mnmem.fit(initialMixture, 10, 0.001); // This call should throw an exception
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationFitNegativeMaxIterations() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}});
        mnmem.fit(initialMixture, -1, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationFitNegativeThreshold() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}});
        mnmem.fit(initialMixture, 10, -0.001);
    }

    // Testing fit method with invalid data
    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationFitWithInvalidData() throws Exception {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0}, {3.0, 3.0}, {3.5, 3.5}});
        mnmem.fit(initialMixture, 10, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationFitWithInitialMixtureMismatchedData() throws Exception {
        double[][] data = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 2.0), 0.5)));
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 10, 0.001);
    }

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationEstimate() throws Exception {
        double[][] data = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}};
        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(2, fittedModel.getComponents().size());
        assertNotNull(fittedModel.getComponents().get(0).getFirst());
        assertNotNull(fittedModel.getComponents().get(1).getFirst());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationEstimateWithInvalidNumComponents() throws Exception {
        double[][] data = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationEstimateWithDataWithWrongNumberOfColumns() throws Exception {
        double[][] data = {{1.0, 1.0}, {2.0}, {3.0, 3.0}, {3.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationEstimateWithInvalidNumComponents() throws Exception {
        double[][] data = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}, {3.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, -1);
    }
}