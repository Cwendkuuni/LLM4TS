package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithValidParameters() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 6.0}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>() {{
            add(new Pair<>(0.25, new MultivariateNormalDistribution(new RealVectorValue(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}})))));
            add(new Pair<>(0.75, new MultivariateNormalDistribution(new RealVectorValue(new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}})))));
        }});

        MultivariateNormalMixtureExpectationMaximization mnmem = null;
        try {
            mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        mnmem.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();
        double logLikelihood = mnmem.getLogLikelihood();

        // Check the number of components and their properties
        assertEquals(2, fittedModel.getComponents().size());
        assertEquals(0.25, fittedModel.getComponents().get(0).getFirst(), 0.001);
        assertEquals(0.75, fittedModel.getComponents().get(1).getFirst(), 0.001);

        // Check the means and covariance matrices of the components
        MultivariateNormalDistribution component0 = fittedModel.getComponents().get(0).getSecond();
        RealMatrix component0Means = component0.getMeans();
        RealMatrix component0CovMats = component0.getCovarianceMatrices()[0];

        MultivariateNormalDistribution component1 = fittedModel.getComponents().get(1).getSecond();
        RealMatrix component1Means = component1.getMeans();
        RealMatrix component1CovMats = component1.getCovarianceMatrices()[0];

        RealVector component0Mean = component0Means.getColumnVector(0);
        RealVector component0StdDevs = component0CovMats.getColumnVector(0);
        RealVector component1Mean = component1Means.getColumnVector(0);
        RealVector component1StdDevs = component1CovMats.getColumnVector(0);

        // Check the means and standard deviations of the components
        assertEquals(1.0, component0Mean.getEntry(0), 0.001);
        assertEquals(1.0, component0Mean.getEntry(1), 0.001);
        assertEquals(1.0, component0StdDevs.getEntry(0), 0.001);
        assertEquals(1.0, component0StdDevs.getEntry(1), 0.001);

        assertEquals(2.0, component1Mean.getEntry(0), 0.001);
        assertEquals(2.0, component1Mean.getEntry(1), 0.001);
        assertEquals(1.0, component1StdDevs.getEntry(0), 0.001);
        assertEquals(1.0, component1StdDevs.getEntry(1), 0.001);

        // Check the log-likelihood
        assertEquals(11.0, logLikelihood, 0.001);
    }

    @Test
    public void testFitWithInvalidNumberOfRows() {
        double[][] data = {};

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()));
            fail("Exception should be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidNumberOfColumns() {
        double[][] data = {
                {1.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()));
            fail("Exception should be thrown");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidNumberOfComponents() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 6.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(), 1));
            fail("Exception should be thrown");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>(), 0));
            fail("Exception should be thrown");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithSingularCovarianceMatrices() {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 1.0},
                {1.0, 2.0},
                {2.0, 1.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()));
            fail("Exception should be thrown");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithNegativeThreshold() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 6.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()), -1.0);
            fail("Exception should be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidMaxIterations() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0},
                {4.0, 5.0, 6.0}
        };

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            mnmem.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()), -1);
            fail("Exception should be thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0, 4.0},
                {4.0, 5.0},
                {5.0, 6.0}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        // Check the number of components and their properties
        assertEquals(2, fittedModel.getComponents().size());

        // Check the means and covariance matrices of the components
        MultivariateNormalDistribution component0 = fittedModel.getComponents().get(0).getSecond();
        RealMatrix component0Means = component0.getMeans();
        RealMatrix component0CovMats = component0.getCovarianceMatrices()[0];

        MultivariateNormalDistribution component1 = fittedModel.getComponents().get(1).getSecond();
        RealMatrix component1Means = component1.getMeans();
        RealMatrix component1CovMats = component1.getCovarianceMatrices()[0];

        RealVector component0Mean = component0Means.getColumnVector(0);
        RealVector component0StdDevs = component0CovMats.getColumnVector(0);
        RealVector component1Mean = component1Means.getColumnVector(0);
        RealVector component1StdDevs = component1CovMats.getColumnVector(0);

        // Check the means and standard deviations of the components
        assertEquals(3.0 / 5.0, component0Mean.getEntry(0), 0.01);
        assertEquals(3.0 / 5.0, component0Mean.getEntry(1), 0.01);
        assertEquals(Math.sqrt(2.0 / 5.0), component0StdDevs.getEntry(0), 0.01);
        assertEquals(Math.sqrt(2.0 / 5.0), component0StdDevs.getEntry(1), 0.01);

        assertEquals(2.0 / 5.0, component1Mean.getEntry(0), 0.01);
        assertEquals(2.0 / 5.0, component1Mean.getEntry(1), 0.01);
        assertEquals(Math.sqrt(2.0 / 5.0), component1StdDevs.getEntry(0), 0.01);
        assertEquals(Math.sqrt(2.0 / 5.0), component1StdDevs.getEntry(1), 0.01);
    }
}