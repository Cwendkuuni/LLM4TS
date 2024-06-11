package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationConstructor() {
        try {
            double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(3, mnme.getData().length);
            assertEquals(2, mnme.getData()[0].length);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithEmptyData() {
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithMismatchedDataDimensions() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithDataContainingLessThanTwoRows() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException, ConvergenceException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());
        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            assertEquals(true, Arrays.equals(new double[]{0.3, 0.7}, new double[]{component.getFirst(), 1.0 - component.getFirst()}));
        }
        assertEquals(1.5, mnme.getLogLikelihood(), 0.01);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrices() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException, ConvergenceException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}})))));
        mnme.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException, ConvergenceException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException, ConvergenceException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture, 1000, -1.0E-5);
    }

    @Test
    public void testFitWithDefaults() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException, ConvergenceException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());
        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            assertEquals(true, Arrays.equals(new double[]{0.3, 0.7}, new double[]{component.getFirst(), 1.0 - component.getFirst()}));
        }
        assertEquals(1.5, mnme.getLogLikelihood(), 0.01);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(2, result.getComponents().size());
        for (Pair<Double, MultivariateNormalDistribution> component : result.getComponents()) {
            assertEquals(0.5, component.getFirst(), 0.01);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithFewerComponentsThanDataPoints() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithMoreComponentsThanDataPoints() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 5);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture);
        assertEquals(1.5, mnme.getLogLikelihood(), 0.01);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<Double, MultivariateNormalDistribution>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        mnme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());
        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            assertEquals(true, Arrays.equals(new double[]{0.3, 0.7}, new double[]{component.getFirst(), 1.0 - component.getFirst()}));
        }
    }
}