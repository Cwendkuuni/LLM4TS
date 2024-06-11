package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationConstructor() {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorNegativeLength() {
        double[][] data = new double[][] {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorDimensionMismatch() {
        double[][] data = new double[][] {{1.0, 2.0}, {1.5, 3.0, 3.5}, {2.0, 3.0, 4.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorDataWithLessThanTwoColumns() {
        double[][] data = new double[][] {{1.0, 2.0}, {1.5, 3.5}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1000, 1.0E-5);
        assertEquals("Unexpected log likelihood after fitting.", 2.761109738489636, mnmem.getLogLikelihood(), 0.0000001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrixException() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1000, -1.0);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}};
        MixtureMultivariateNormalDistribution mvnd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        Pair<MultivariateNormalDistribution, MultivariateNormalDistribution> component0 = mvnd.getComponents().get(0);
        Pair<MultivariateNormalDistribution, MultivariateNormalDistribution> component1 = mvnd.getComponents().get(1);
        assertEquals("Unexpected number of components.", 2, mvnd.getComponents().size());
        assertEquals("Unexpected weight of the first component.", 0.5, component0.getFirst(), 0.0);
        assertEquals("Unexpected mean of the first component.", new double[] {1.5, 2.5}, component0.getSecond().getMeans(), 0.0);
        assertEquals("Unexpected standard deviation of the first component.", 0.5, component0.getSecond().getStandardDeviations()[0], 0.001);
        assertEquals("Unexpected correlation between the first and second components.", 0.5, component0.getSecond().getCorrelationMatrix().getEntry(0, 1), 0.001);
        assertEquals("Unexpected weight of the second component.", 0.5, component1.getFirst(), 0.0);
        assertEquals("Unexpected mean of the second component.", new double[] {2.5, 3.5}, component1.getSecond().getMeans(), 0.0);
        assertEquals("Unexpected standard deviation of the second component.", 0.5, component1.getSecond().getStandardDeviations()[0], 0.001);
        assertEquals("Unexpected correlation between the first and second components.", 0.5, component1.getSecond().getCorrelationMatrix().getEntry(0, 1), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithTooFewDataPoints() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataPointsOfDifferentLengths() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0}, {1.5, 2.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithTooManyComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }
}