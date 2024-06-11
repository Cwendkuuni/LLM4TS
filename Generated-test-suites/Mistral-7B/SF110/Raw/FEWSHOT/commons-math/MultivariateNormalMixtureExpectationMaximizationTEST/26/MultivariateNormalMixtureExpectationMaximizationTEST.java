package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndSpecifiedMaxIterationsAndThreshold() throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new double[] { 1.0, 1.0 }, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture, 10, 0.01);

        assertEquals(4, emm.getFittedModel().getComponents().size());
        assertArrayEquals(new double[] { 1.045739167231234, 0.9542608327687878, 0.0, 0.0 }, emm.getFittedModel().getComponents().get(0).getSecond().getMeans(), 0.001);
        assertArrayEquals(new double[] { 0.25, 0.25 }, emm.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(0));
        assertArrayEquals(new double[] { 0.25, 0.25 }, emm.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(1));
        assertEquals(0.4, emm.getFittedModel().getComponents().get(0).getFirst(), 0.01);
        assertEquals(0.3, emm.getFittedModel().getComponents().get(1).getFirst(), 0.01);
        assertEquals(0.2, emm.getFittedModel().getComponents().get(2).getFirst(), 0.01);
        assertEquals(0.1, emm.getFittedModel().getComponents().get(3).getFirst(), 0.01);

        assertEquals(2.1517675452229964, emm.getLogLikelihood(), 0.001);
    }

    @Test
    public void testFitWithInitialMixtureAndDefaultMaxIterationsAndThreshold() throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new double[] { 1.0, 1.0 }, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);
        emm.fit(initialMixture);

        assertEquals(4, emm.getFittedModel().getComponents().size());
        assertArrayEquals(new double[] { 1.045739167231234, 0.9542608327687878, 0.0, 0.0 }, emm.getFittedModel().getComponents().get(0).getSecond().getMeans(), 0.001);
        assertArrayEquals(new double[] { 0.25, 0.25 }, emm.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(0));
        assertArrayEquals(new double[] { 0.25, 0.25 }, emm.getFittedModel().getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(1));
        assertEquals(0.4, emm.getFittedModel().getComponents().get(0).getFirst(), 0.01);
        assertEquals(0.3, emm.getFittedModel().getComponents().get(1).getFirst(), 0.01);
        assertEquals(0.2, emm.getFittedModel().getComponents().get(2).getFirst(), 0.01);
        assertEquals(0.1, emm.getFittedModel().getComponents().get(3).getFirst(), 0.01);

        assertEquals(2.1517675452229964, emm.getLogLikelihood(), 0.001);
    }

    @Test
    public void testFitWithEmptyData() throws NotStrictlyPositiveException {
        double[][] data = {};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new double[] { 1.0, 1.0 }, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        assertEquals(0, emm.getFittedModel().getComponents().size());
    }

    @Test
    public void testFitWithDataOfWrongSize() throws NotStrictlyPositiveException {
        double[][] data = { { 1.0 }, { 1.1, 2.1 }, { 1.2, 2.2, 3.3 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new double[] { 1.0, 1.0 }, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }

        assertEquals(0, emm.getFittedModel().getComponents().size());
    }

    @Test
    public void testFitWithDataOfWrongNumberOfComponents() throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3, 3.3 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new double[] { 1.0, 1.0 }, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization emm = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            emm.fit(initialMixture);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        assertEquals(0, emm.getFittedModel().getComponents().size());
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3 } };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        assertEquals(3, fittedModel.getComponents().size());
        assertArrayEquals(new double[] { 1.045739167231234, 0.9542608327687878, 0.0 }, fittedModel.getComponents().get(0).getSecond().getMeans(), 0.001);
        assertArrayEquals(new double[] { 0.25, 0.25 }, fittedModel.getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(0));
        assertArrayEquals(new double[] { 0.25, 0.25 }, fittedModel.getComponents().get(0).getSecond().getCovarianceMatrix().getColumnVector(1));
        assertEquals(0.3333333333333333, fittedModel.getComponents().get(0).getFirst(), 0.01);
        assertEquals(0.3333333333333333, fittedModel.getComponents().get(1).getFirst(), 0.01);
        assertEquals(0.3333333333333333, fittedModel.getComponents().get(2).getFirst(), 0.01);

        assertEquals(2.1517675452229964, fittedModel.getLogLikelihood(), 0.001);
    }

    @Test
    public void testEstimateWithEmptyData() throws NotStrictlyPositiveException {
        double[][] data = {};

        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        assertEquals(0, MultivariateNormalMixtureExpectationMaximization.estimate(data, 3).getComponents().size());
    }

    @Test
    public void testEstimateWithDataOfWrongSize() throws NotStrictlyPositiveException {
        double[][] data = { { 1.0 }, { 1.1, 2.1 }, { 1.2, 2.2, 3.3 } };

        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }

        assertEquals(0, MultivariateNormalMixtureExpectationMaximization.estimate(data, 3).getComponents().size());
    }

    @Test
    public void testEstimateWithDataOfWrongNumberOfComponents() throws NotStrictlyPositiveException {
        double[][] data = { { 1.0, 2.0 }, { 1.1, 2.1 }, { 1.2, 2.2 }, { 1.3, 2.3, 3.3, 4.4 } };

        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }

        assertEquals(0, MultivariateNormalMixtureExpectationMaximization.estimate(data, 3).getComponents().size());
    }
}