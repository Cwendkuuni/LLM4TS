package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.fitting.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(model);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDataLength() {
        double[][] data = new double[][]{{1.0, 2.0}, {-1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDataRowsDifferentLengths() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorDataRowsWithLessThanTwoColumns() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = mock(MixtureMultivariateNormalDistribution.class);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 10, 1.0E-5);

        assertNotNull(model.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = mock(MixtureMultivariateNormalDistribution.class);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, -10, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = mock(MixtureMultivariateNormalDistribution.class);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 10, -1.0E-5);
    }

    @Test
    public void testFitWithInitialMixture() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = mock(MixtureMultivariateNormalDistribution.class);
        when(initialMixture.getComponents()).thenReturn(List.of(new Gaussian(new NormalDistribution(new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        })), 0.5), new Gaussian(new NormalDistribution(new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        })), 0.5)));
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(2, fittedModel.getComponents().size());
    }

    @Test
    public void testFitWithoutInitialMixture() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit();

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(2, fittedModel.getComponents().size());
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertNotNull(fittedModel);
        assertEquals(2, fittedModel.getComponents().size());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateNegativeNumberOfComponents() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {1.5, 2.5}, {2.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateDataRowsDifferentLengths() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}, {1.5, 2.5}, {2.5, 3.5, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    public static class Gaussian implements MultivariateNormalDistribution {
        private final NormalDistribution distribution;
        private final double weight;

        public Gaussian(NormalDistribution distribution, double weight) {
            this.distribution = distribution;
            this.weight = weight;
        }

        @Override
        public double density(double[] point) {
            return this.weight * this.distribution.density(point);
        }

        @Override
        public double[] getMeans() {
            return this.distribution.getMeans();
        }

        @Override
        public RealMatrix getCovarianceMatrix() {
            return this.distribution.getCovarianceMatrix();
        }
    }
}