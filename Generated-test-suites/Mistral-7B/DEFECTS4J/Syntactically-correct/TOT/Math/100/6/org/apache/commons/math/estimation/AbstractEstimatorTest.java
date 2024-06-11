package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedVersusPredicted;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** A simple estimation problem with two parameters. */
    private static final double[][] DATA = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
    };

    /** The estimation problem. */
    private EstimationProblem problem;

    /** The AbstractEstimator instance under test. */
    private AbstractEstimator estimator;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        problem = new LinearRegressionProblem(DATA);
        estimator = new LinearRegressionEstimator();
        estimator.setMaxCostEval(1000);
        estimator.initializeEstimate(problem);
    }

    /**
     * Tests the estimation method.
     */
    @Test
    public void testEstimation() {
        try {
            estimator.estimate(problem);
            RealVector parameters = problem.getAllParameters();
            double[] expected = {1.0, 1.0};
            assertEquals("Unexpected estimation result", expected, problem.getAllParameters().clone(), 1e-10);
        } catch (EstimationException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        RealVector residuals = problem.getResiduals();
        double[] expectedResiduals = {-0.5, -0.3, 0.2};
        assertEquals("Unexpected estimation result", expectedResiduals, residuals.toArray(), 1e-10);
    }

    /**
     * Tests the estimation method with maximal number of cost evaluations.
     */
    @Test
    public void testMaxCostEval() {
        estimator.setMaxCostEval(1);
        assertThrows("Expected an estimation exception", EstimationException.class, () -> estimator.estimate(problem));
    }

    /**
     * Tests the getCovariances method.
     */
    @Test
    public void testCovariances() {
        try {
            estimator.estimate(problem);
            RealMatrix covarianceMatrix = estimator.getCovariances(problem);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.3, 0.1}, {0.1, 0.3}});
            assertEquals("Unexpected covariance matrix", expected, covarianceMatrix);
        } catch (EstimationException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    /**
     * Tests the guessParametersErrors method.
     */
    @Test
    public void testGuessParametersErrors() {
        try {
            estimator.estimate(problem);
            double[] expectedErrors = {0.235, 0.118};
            double[] errors = estimator.guessParametersErrors(problem);
            assertArrayEquals("Unexpected error estimates", expectedErrors, errors);
        } catch (EstimationException ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

}