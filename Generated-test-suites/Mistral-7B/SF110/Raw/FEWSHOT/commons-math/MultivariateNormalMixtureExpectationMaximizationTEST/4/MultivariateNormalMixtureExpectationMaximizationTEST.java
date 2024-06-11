package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructors() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] {});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {}

        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] { {1.0, 2.0}, {3.0, 4.0} }, 1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {}

        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] { {1.0, 2.0}, {3.0, 2.0} }, 2);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {}

        double[][] data = new double[][] { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, mmeme.getFittedModel().getComponents().size());
        assertEquals(3, mmeme.data.length);
    }

    @Test
    public void testEstimate() {
        double[][] data = new double[][] { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0} };
        MixtureMultivariateNormalDistribution result;
        try {
            result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, result.getComponents().size());
        assertEquals(5, result.getComponents().get(0).getFirst(), 0.0);
    }

    @Test
    public void testFit() {
        double[][] data = new double[][] { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new Array2DRowRealMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0} }), new Array2DRowRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0} }))));
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
            mmeme.fit(initialMixture);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, mmeme.getFittedModel().getComponents().size());
        assertEquals(3, mmeme.data.length);
        assertEquals(0.0, mmeme.getLogLikelihood(), 0.0);
    }

    @Test
    public void testFitWithInitialModelAndThreshold() {
        double[][] data = new double[][] { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new Array2DRowRealMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0} }), new Array2DRowRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0} }))));
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
            mmeme.fit(initialMixture, 100, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, mmeme.getFittedModel().getComponents().size());
        assertEquals(3, mmeme.data.length);
        assertTrue(mmeme.getLogLikelihood() > 0.0);
    }

    @Test
    public void testFitWithInitialModel() {
        double[][] data = new double[][] { {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new Array2DRowRealMatrix(new double[][] { {1.0, 1.0}, {1.0, 1.0} }), new Array2DRowRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0} }))));
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
            mmeme.fit(initialMixture);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(3, mmeme.getFittedModel().getComponents().size());
        assertEquals(3, mmeme.data.length);
        assertTrue(mmeme.getLogLikelihood() > 0.0);
    }
}