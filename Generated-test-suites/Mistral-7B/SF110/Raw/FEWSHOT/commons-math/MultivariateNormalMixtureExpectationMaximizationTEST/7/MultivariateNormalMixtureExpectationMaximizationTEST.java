package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithEmptyDataThrowsException() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] {});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructWithDataOfWrongLengthThrowsException() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] { { 1.0 } });
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructWithDataOfMismatchedColumnsThrowsException() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] { { 1.0, 2.0 }, { 3.0, 4.0, 5.0 } });
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructWithDataOfWrongNumberOfColumnsThrowsException() {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] { { 1.0, 2.0, 3.0 } });
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitThrowsSingularMatrixException() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.0, 1.0))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture, 1, 0.0);
    }

    @Test
    public void testFitWithInitialMixtureAndMaxIterationsAndThreshold() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new NormalDistribution(1.5, 1.0))));
        MultivariateNormalMixtureExpectationMaximization fitter = new MultivariateNormalMixtureExpectationMaximization(data);
        fitter.fit(initialMixture, 100, 1.0E-5);
        assertEquals(100, fitter.getIterations());
        assertTrue(Math.abs(fitter.getLogLikelihood() - 2.025212523) < 1.0E-5);
    }

    @Test
    public void testFitWithInitialMixture() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new NormalDistribution(1.5, 1.0))));
        MultivariateNormalMixtureExpectationMaximization fitter = new MultivariateNormalMixtureExpectationMaximization(data);
        fitter.fit(initialMixture);
        assertEquals(1000, fitter.getIterations());
        assertTrue(Math.abs(fitter.getLogLikelihood() - 2.025212523) < 1.0E-5);
    }

    @Test
    public void testEstimateWith2Components() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.5, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.5, 1.0), new NormalDistribution(1.5, 1.0))));
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(expected.getComponents().size(), result.getComponents().size());
        assertEquals(expected.getComponents().get(0).getFirst(), result.getComponents().get(0).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getMeans(), result.getComponents().get(0).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), result.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), result.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
        assertEquals(expected.getComponents().get(1).getFirst(), result.getComponents().get(1).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getMeans(), result.getComponents().get(1).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), result.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), result.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
    }

    @Test
    public void testEstimateWith3Components() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new NormalDistribution(1.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(2.0, 1.0), new NormalDistribution(2.5, 1.0))));
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        assertEquals(expected.getComponents().size(), result.getComponents().size());
        assertEquals(expected.getComponents().get(0).getFirst(), result.getComponents().get(0).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getMeans(), result.getComponents().get(0).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), result.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), result.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
        assertEquals(expected.getComponents().get(1).getFirst(), result.getComponents().get(1).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getMeans(), result.getComponents().get(1).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), result.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), result.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
        assertEquals(expected.getComponents().get(2).getFirst(), result.getComponents().get(2).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(2).getSecond().getMeans(), result.getComponents().get(2).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(2).getSecond().getCovarianceMatrix().getEntry(0, 0), result.getComponents().get(2).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(2).getSecond().getCovarianceMatrix().getEntry(1, 1), result.getComponents().get(2).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MultivariateNormalMixtureExpectationMaximization fitter = new MultivariateNormalMixtureExpectationMaximization(data);
        fitter.fit(new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new NormalDistribution(1.5, 1.0)))));
        assertEquals(-2.025212523, fitter.getLogLikelihood(), 1.0E-5);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = { { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 }, { 1.0, 2.0 } };
        MultivariateNormalMixtureExpectationMaximization fitter = new MultivariateNormalMixtureExpectationMaximization(data);
        fitter.fit(new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new NormalDistribution(1.5, 1.0)))));
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(0.5, 1.0), new NormalDistribution(0.5, 1.0)),
                new MultivariateNormalDistribution(new NormalDistribution(1.5, 1.0), new NormalDistribution(1.5, 1.0))));
        assertEquals(expected.getComponents().size(), fitter.getFittedModel().getComponents().size());
        assertEquals(expected.getComponents().get(0).getFirst(), fitter.getFittedModel().getComponents().get(0).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getMeans(), fitter.getFittedModel().getComponents().get(0).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), fitter.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), fitter.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
        assertEquals(expected.getComponents().get(1).getFirst(), fitter.getFittedModel().getComponents().get(1).getFirst(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getMeans(), fitter.getFittedModel().getComponents().get(1).getSecond().getMeans(), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), fitter.getFittedModel().getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(0, 0), 0.001);
        assertEquals(expected.getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), fitter.getFittedModel().getComponents().get(1).getSecond().getCovarianceMatrix().getEntry(1, 1), 0.001);
    }
}