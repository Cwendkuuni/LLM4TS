package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.precision.DoubleArray;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{3.5, 3.5}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.5, 2.5}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{1.5, 1.5}, new Array2DRowRealMatrix(3, 3)))));

        MixtureMultivariateNormalDistribution actual;
        try {
            actual = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        } catch (DimensionMismatchException | NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
            return;
        }

        assertEquals(expected.getComponents().size(), actual.getComponents().size());
        for (int i = 0; i < expected.getComponents().size(); ++i) {
            assertEquals(expected.getComponents().get(i).getFirst(), actual.getComponents().get(i).getFirst(), 0.001);
            assertEquals(expected.getComponents().get(i).getSecond().getMeans(), actual.getComponents().get(i).getSecond().getMeans(), 0.001);
            RealMatrix expectedCovMat = expected.getComponents().get(i).getSecond().getCovarianceMatrix();
            RealMatrix actualCovMat = actual.getComponents().get(i).getSecond().getCovarianceMatrix();
            for (int j = 0; j < expectedCovMat.getRowDimension(); ++j) {
                for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                    assertEquals(expectedCovMat.getEntry(j, k), actualCovMat.getEntry(j, k), 0.001);
                }
            }
        }
    }

    @Test
    public void testFitWithInitialMixtureAndMaxIterationsAndThreshold() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{3.0, 3.0}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(3, 3)))),
                new Pair<Double, MultivariateNormalDistribution>(0.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(3, 3))));

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, 100, 0.001);

        MixtureMultivariateNormalDistribution actual = em.getFittedModel();

        assertEquals(initialMixture.getComponents().size(), actual.getComponents().size());
        for (int i = 0; i < initialMixture.getComponents().size(); ++i) {
            assertEquals(initialMixture.getComponents().get(i).getFirst(), actual.getComponents().get(i).getFirst(), 0.001);
            assertEquals(initialMixture.getComponents().get(i).getSecond().getMeans().length, actual.getComponents().get(i).getSecond().getMeans().length);
            for (int j = 0; j < initialMixture.getComponents().get(i).getSecond().getMeans().length; ++j) {
                assertEquals(initialMixture.getComponents().get(i).getSecond().getMeans()[j], actual.getComponents().get(i).getSecond().getMeans()[j], 0.001);
            }
            RealMatrix expectedCovMat = initialMixture.getComponents().get(i).getSecond().getCovarianceMatrix();
            RealMatrix actualCovMat = actual.getComponents().get(i).getSecond().getCovarianceMatrix();
            for (int j = 0; j < expectedCovMat.getRowDimension(); ++j) {
                for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                    assertEquals(expectedCovMat.getEntry(j, k), actualCovMat.getEntry(j, k), 0.001);
                }
            }
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithInitialMixtureAndMaxIterationsAndThresholdThrowsException() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{3.0, 3.0}, new SingularValueDecomposition(new Array2DRowRealMatrix(3, 3)).getSingularValues())),
                new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(3, 3)))),
                new Pair<Double, MultivariateNormalDistribution>(0.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(3, 3))));

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, 100, 0.001);
    }

    @Test
    public void testFitWithInitialMixture() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{3.0, 3.0}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(3, 3))),
                new Pair<Double, MultivariateNormalDistribution>(0.25, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(3, 3)))),
                new Pair<Double, MultivariateNormalDistribution>(0.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(3, 3))));

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture);

        MixtureMultivariateNormalDistribution actual = em.getFittedModel();

        assertEquals(initialMixture.getComponents().size(), actual.getComponents().size());
        for (int i = 0; i < initialMixture.getComponents().size(); ++i) {
            assertEquals(initialMixture.getComponents().get(i).getFirst(), actual.getComponents().get(i).getFirst(), 0.001);
            assertEquals(initialMixture.getComponents().get(i).getSecond().getMeans().length, actual.getComponents().get(i).getSecond().getMeans().length);
            for (int j = 0; j < initialMixture.getComponents().get(i).getSecond().getMeans().length; ++j) {
                assertEquals(initialMixture.getComponents().get(i).getSecond().getMeans()[j], actual.getComponents().get(i).getSecond().getMeans()[j], 0.001);
            }
            RealMatrix expectedCovMat = initialMixture.getComponents().get(i).getSecond().getCovarianceMatrix();
            RealMatrix actualCovMat = actual.getComponents().get(i).getSecond().getCovarianceMatrix();
            for (int j = 0; j < expectedCovMat.getRowDimension(); ++j) {
                for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                    assertEquals(expectedCovMat.getEntry(j, k), actualCovMat.getEntry(j, k), 0.001);
                }
            }
        }
    }
}