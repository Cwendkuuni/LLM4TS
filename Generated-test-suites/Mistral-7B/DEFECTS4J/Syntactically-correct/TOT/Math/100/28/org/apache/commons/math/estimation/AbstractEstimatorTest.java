package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new LevenbergMarquardtEstimator();
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            private final WeightedMeasurement[] measurements = new WeightedMeasurement[] {
                new WeightedMeasurement(1.0, 2.0, 3.0),
                new WeightedMeasurement(4.0, 5.0, 6.0),
                new WeightedMeasurement(7.0, 8.0, 9.0)
            };

            private final EstimatedParameter[] parameters = new EstimatedParameter[] {
                new EstimatedParameter("p1"),
                new EstimatedParameter("p2")
            };
        };
    }

    /**
     * Test that {@link AbstractEstimator#estimate} returns the expected
     * parameters for a simple problem.
     */
    @Test
    public void testEstimateSimple() throws EstimationException {
        estimator.setMaxCostEval(1000);
        estimator.estimate(problem);
        RealVector params = problem.getAllParameters();
        RealVector expected = RealVectorFactory.getInstance().createVector(2);
        expected.setEntry(0, 2.0);
        expected.setEntry(1, 3.0);
        SimpleRegression regression = new SimpleRegression();
        RealMatrix X = new Array2DRowRealMatrix(3, 2);
        RealVector y = RealVectorFactory.getInstance().createVector(3);
        for (int i = 0; i < 3; i++) {
            X.setEntry(i, 0, problem.getMeasurements()[i].getX());
            X.setEntry(i, 1, problem.getMeasurements()[i].getY());
            y.setEntry(i, problem.getMeasurements()[i].getY());
        }
        regression.fit(X, y);
        assertEquals(expected, regression.getParameters());
        assertEquals(estimator.getCostEvaluations(), 1, 0.0);
        assertEquals(estimator.getJacobianEvaluations(), 1, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#estimate} throws an exception
     * for an unsolvable problem.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateUnsolvable() throws EstimationException {
        problem.getMeasurements()[0].setWeight(0);
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
    }

    /**
     * Test that {@link AbstractEstimator#getRMS} returns the expected value
     * for a simple problem.
     */
    @Test
    public void testGetRMS() {
        estimator.setMaxCostEval(1000);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(rms, 1.0, 0.001);
    }

    /**
     * Test that {@link AbstractEstimator#getChiSquare} returns the expected value
     * for a simple problem.
     */
    @Test
    public void testGetChiSquare() {
        estimator.setMaxCostEval(1000);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(chiSquare, 7.0, 0.001);
    }

    /**
     * Test that {@link AbstractEstimator#getCovariances} returns the expected values
     * for a simple problem.
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.setMaxCostEval(1000);
        estimator.estimate(problem);
        RealMatrix cov = estimator.getCovariances(problem);
        RealArray2DSample covSample = new RealArray2DSample(cov, 2, 2);
        double[][] expected = { { 0.1, 0.05 }, { 0.05, 0.1 } };
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                assertEquals(expected[i][j], covSample.getEntry(i, j), 0.001);
            }
        }
        assertEquals(estimator.getCostEvaluations(), 3, 0.0);
        assertEquals(estimator.getJacobianEvaluations(), 3, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#guessParametersErrors} returns the expected values
     * for a simple problem.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.setMaxCostEval(1000);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(errors[0], 0.1, 0.001);
        assertEquals(errors[1], 0.1, 0.001);
        assertEquals(estimator.getCostEvaluations(), 1, 0.0);
        assertEquals(estimator.getJacobianEvaluations(), 1, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#initializeEstimate} sets the expected values
     */
    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        assertEquals(estimator.getCostEvaluations(), 0, 0.0);
        assertEquals(estimator.getJacobianEvaluations(), 0, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#setMaxCostEval} sets the expected value.
     */
    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(5);
        assertEquals(estimator.getMaxCostEval(), 5, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#getCostEvaluations} returns the expected value.
     */
    @Test
    public void testGetCostEvaluations() {
        estimator.setMaxCostEval(5);
        estimator.estimate(problem);
        assertEquals(estimator.getCostEvaluations(), 1, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#getJacobianEvaluations} returns the expected value.
     */
    @Test
    public void testGetJacobianEvaluations() {
        estimator.setMaxCostEval(5);
        estimator.estimate(problem);
        assertEquals(estimator.getJacobianEvaluations(), 1, 0.0);
    }

    /**
     * Test that {@link AbstractEstimator#updateJacobian} sets the expected values of the jacobian.
     */
    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        assertEquals(estimator.jacobian[0], -5.0, 0.001);
        assertEquals(estimator.jacobian[1], -3.0, 0.001);
        assertEquals(estimator.jacobian[2], -3.0, 0.001);
        assertEquals(estimator.jacobian[3], -1.0, 0.001);
        assertEquals(estimator.jacobian[4], -3.0, 0.001);
        assertEquals(estimator.jacobian[5], -1.0, 0.001);
        assertEquals(estimator.jacobian[6], -3.0, 0.001);
        assertEquals(estimator.jacobian[7], -1.0, 0.001);
        assertEquals(estimator.jacobian[8], 0.0, 0.001);
        assertEquals(estimator.jacobian[9], 0.0, 0.001);
        assertEquals(estimator.jacobian[10], 0.0, 0.001);
        assertEquals(estimator.jacobian[11], 0.0, 0.001);
    }

    /**
     * Test that {@link AbstractEstimator#updateResidualsAndCost} sets the expected values of the residuals and cost.
     */
    @Test
    public void testUpdateResidualsAndCost() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        assertEquals(estimator.cost, 10.0, 0.001);
        assertEquals(estimator.residuals[0], -2.0, 0.001);
        assertEquals(estimator.residuals[1], 1.0, 0.001);
        assertEquals(estimator.residuals[2], 2.0, 0.001);
    }

}