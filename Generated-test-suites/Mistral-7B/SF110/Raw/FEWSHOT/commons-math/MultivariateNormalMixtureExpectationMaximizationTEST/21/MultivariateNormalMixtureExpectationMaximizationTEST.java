package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndCustomParameters() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture, 10, 0.001);
            MixtureMultivariateNormalDistribution fittedModel = mixEM.getFittedModel();
            Assert.assertEquals(5, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                double[] weights = component.getSecond().getProbabilityVector();
                for (double weight : weights) {
                    assertTrue(weight > 0.0 && weight < 1.0);
                }
                MultivariateNormalDistribution mvn = component.getSecond();
                double[] means = mvn.getMeans();
                double[][] covMat = mvn.getCovarianceMatrix().getData();
                assertArrayEquals(new double[]{0.0, 0.0}, means, 0.01);
                assertArrayEquals(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}}, covMat, 0.01);
            }
        } catch (Exception e) {
            fail("Fit failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        try {
            MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            MixtureMultivariateNormalDistribution fittedModel = new MultivariateNormalMixtureExpectationMaximization(data).getFittedModel();
            assertEquals(estimatedModel.getComponents().size(), fittedModel.getComponents().size());
            for (int i = 0; i < estimatedModel.getComponents().size(); ++i) {
                Pair<Double, MultivariateNormalDistribution> estimatedComponent = estimatedModel.getComponents().get(i);
                Pair<Double, MultivariateNormalDistribution> fittedComponent = fittedModel.getComponents().get(i);
                // Check if the estimated and fitted components have similar parameters
                double[] estimatedMeans = estimatedComponent.getSecond().getMeans();
                double[] fittedMeans = fittedComponent.getSecond().getMeans();
                assertArrayEquals(estimatedMeans, fittedMeans, 0.01);

                RealMatrix estimatedCovMat = estimatedComponent.getSecond().getCovarianceMatrix();
                RealMatrix fittedCovMat = fittedComponent.getSecond().getCovarianceMatrix();
                assertEquals(estimatedCovMat.getRowDimension(), fittedCovMat.getRowDimension());
                assertEquals(estimatedCovMat.getColumnDimension(), fittedCovMat.getColumnDimension());
                for (int j = 0; j < estimatedCovMat.getRowDimension(); ++j) {
                    for (int k = 0; k < estimatedCovMat.getColumnDimension(); ++k) {
                        double estimatedCov = estimatedCovMat.getEntry(j, k);
                        double fittedCov = fittedCovMat.getEntry(j, k);
                        assertEquals(estimatedCov, fittedCov, 0.01);
                    }
                }
            }
        } catch (Exception e) {
            fail("Estimation failed with exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataShape() {
        double[][] data = {{1.0}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDataShape2() {
        double[][] data = {{1.0, 2.0}, {}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 2.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture, 10, 0.001);
            fail("Fit should have thrown SingularMatrixException");
        } catch (Exception e) {
            if (!(e instanceof SingularMatrixException)) {
                throw e;
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture, -10, 0.001);
            fail("Fit should have thrown NotStrictlyPositiveException");
        } catch (Exception e) {
            if (!(e instanceof NotStrictlyPositiveException)) {
                throw e;
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture, 10, -0.001);
            fail("Fit should have thrown NotStrictlyPositiveException");
        } catch (Exception e) {
            if (!(e instanceof NotStrictlyPositiveException)) {
                throw e;
            }
        }
    }

    @Test
    public void testFitWithDefaultParameters() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = mixEM.getFittedModel();
            assertEquals(5, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                double[] weights = component.getSecond().getProbabilityVector();
                for (double weight : weights) {
                    assertTrue(weight > 0.0 && weight < 1.0);
                }
                MultivariateNormalDistribution mvn = component.getSecond();
                double[] means = mvn.getMeans();
                double[][] covMat = mvn.getCovarianceMatrix().getData();
                assertArrayEquals(new double[]{0.0, 0.0}, means, 0.01);
                assertArrayEquals(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}}, covMat, 0.01);
            }
        } catch (Exception e) {
            fail("Fit failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture);
            double logLikelihood = mixEM.getLogLikelihood();
            assertTrue(logLikelihood > Double.NEGATIVE_INFINITY);
        } catch (Exception e) {
            fail("Fit failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})),
                0.5));
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mixEM.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = mixEM.getFittedModel();
            assertEquals(5, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                double[] weights = component.getSecond().getProbabilityVector();
                for (double weight : weights) {
                    assertTrue(weight > 0.0 && weight < 1.0);
                }
                MultivariateNormalDistribution mvn = component.getSecond();
                double[] means = mvn.getMeans();
                double[][] covMat = mvn.getCovarianceMatrix().getData();
                assertArrayEquals(new double[]{0.0, 0.0}, means, 0.01);
                assertArrayEquals(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}}, covMat, 0.01);
            }
        } catch (Exception e) {
            fail("Fit failed with exception: " + e.getMessage());
        }
    }
}