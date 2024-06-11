package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructors() {
        try {
            double[][] data = new double[0][];
            MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException or DimensionMismatchException");
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            // Expected
        }

        double[][] data2 = new double[1][2];
        MultivariateNormalMixtureExpectationMaximization mnmem2 = new MultivariateNormalMixtureExpectationMaximization(data2);
        assertEquals(1, mnmem2.getData().length);
        assertEquals(2, mnmem2.getData()[0].length);

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem3 = new MultivariateNormalMixtureExpectationMaximization(new double[1][1]);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        double[][] data3 = new double[2][2];
        MultivariateNormalMixtureExpectationMaximization mnmem4 = new MultivariateNormalMixtureExpectationMaximization(data3);
        assertEquals(2, mnmem4.getData().length);
        assertEquals(2, mnmem4.getData()[0].length);
        assertEquals(2, mnmem4.getData()[1].length);

        try {
            MultivariateNormalMixtureExpectationMaximization mnmem5 = new MultivariateNormalMixtureExpectationMaximization(new double[1][3]);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInitialMixtureAndGivenMaxIterationsAndThreshold() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][] {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 2.0},
                {2.5, 1.5}
        };
        MultivariateNormalMixtureDistribution initialMixture = MultivariateNormalMixtureDistribution.fit(data, 2);

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 100, 1.0E-5);

        assertEquals(2, mnmem.getFittedModel().getComponents().size());
        assertTrue(mnmem.getLogLikelihood() > Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testFitWithInitialMixture() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][] {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 2.0},
                {2.5, 1.5}
        };
        MultivariateNormalMixtureDistribution initialMixture = MultivariateNormalMixtureDistribution.fit(data, 2);

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture);

        assertEquals(2, mnmem.getFittedModel().getComponents().size());
        assertTrue(mnmem.getLogLikelihood() > Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][] {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 2.0},
                {2.5, 1.5}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        assertEquals(2, fittedModel.getComponents().size());
        assertTrue(fittedModel.getLogLikelihood() > Double.NEGATIVE_INFINITY);
    }
}