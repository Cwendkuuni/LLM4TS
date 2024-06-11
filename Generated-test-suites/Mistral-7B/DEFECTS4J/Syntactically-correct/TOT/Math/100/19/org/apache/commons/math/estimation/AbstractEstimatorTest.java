package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        problem = new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] { 1, 2 });
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(new ArrayRealVector(new double[] { 1, 3 }), 1),
                    new WeightedMeasurement(new ArrayRealVector(new double[] { 2, 4 }), 2),
                    new WeightedMeasurement(new ArrayRealVector(new double[] { 3, 5 }), 3)
                };
            }
        };
        estimator.initializeEstimate(problem);
    }

    /**
     * Test {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    /**
     * Test {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    /**
     * Test {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertEquals(1.0, Precision.doubleEquals(Math.sqrt(3.0 / 3.0), rms, 1e-8));
    }

    /**
     * Test {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(1.0, chiSquare);
    }

    /**
     * Test {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testgetCovariances() {
        try {
            double[][] covariances = estimator.getCovariances(problem);
            assertNotNull(covariances);
            assertEquals(2, covariances.length);
            assertEquals(2, covariances[0].length);
            assertEquals(2, covariances[1].length);
            assertArrayEquals(new double[] { 1.0 / 3.0, 0.0 }, covariances[0], 1e-8);
            assertArrayEquals(new double[] { 0.0, 1.0 / 3.0 }, covariances[1], 1e-8);
        } catch (EstimationException e) {
            // not expected
        }
    }

    /**
     * Test {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() {
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] { Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0) }, errors, 1e-8);
    }

}