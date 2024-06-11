package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test for {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test for {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#getCovariances(EstimationProblem)}
     * with positive number of degrees of freedom.
     */
    @Test
    public void testGetCovariancesPositiveDegreesOfFreedom() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1.0, 1.0, 1.0),
                                                 new WeightedMeasurement(2.0, 2.0, 2.0),
                                                 new WeightedMeasurement(3.0, 3.0, 3.0) };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2"),
                                                 new EstimatedParameter("p3") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealMatrix covariances = estimator.getCovariances(problem);
        RealVector parameterVector = new Array2DRowRealMatrix(problem.getAllParameters()).getColumnVector(0);
        RealVector errors = estimator.guessParametersErrors(problem);
        RealVector expectedErrors = new Array2DRowRealMatrix(problem.getAllParameters()).getColumnVector(1);
        for (int i = 0; i < errors.getDimension(); ++i) {
            assertEquals(Precision.round(errors.getEntry(i), 5), Precision.round(expectedErrors.getEntry(i), 5), 1e-5);
        }
        assertEquals(covariances.getEntry(0, 0), Precision.round(covariances.getEntry(0, 0), 5), 1e-5);
        assertEquals(covariances.getEntry(1, 1), Precision.round(covariances.getEntry(1, 1), 5), 1e-5);
        assertEquals(covariances.getEntry(2, 2), Precision.round(covariances.getEntry(2, 2), 5), 1e-5);
        assertEquals(covariances.getEntry(0, 1), Precision.round(covariances.getEntry(0, 1), 5), 1e-5);
        assertEquals(covariances.getEntry(1, 0), Precision.round(covariances.getEntry(1, 0), 5), 1e-5);
        assertEquals(covariances.getEntry(1, 2), Precision.round(covariances.getEntry(1, 2), 5), 1e-5);
        assertEquals(covariances.getEntry(2, 0), Precision.round(covariances.getEntry(2, 0), 5), 1e-5);
        assertEquals(covariances.getEntry(2, 1), Precision.round(covariances.getEntry(2, 1), 5), 1e-5);
    }

    /**
     * Test for {@link AbstractEstimator#getCovariances(EstimationProblem)}
     * with zero degrees of freedom.
     */
    @Test
    public void testGetCovariancesZeroDegreesOfFreedom() {
        EstimationProblem problem = new EstimationProblem() {

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        assertThrows(EstimationException.class, () -> estimator.getCovariances(problem));
    }

    /**
     * Test for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1.0, 1.0, 1.0),
                                                 new WeightedMeasurement(2.0, 2.0, 2.0),
                                                 new WeightedMeasurement(3.0, 3.0, 3.0) };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2"),
                                                 new EstimatedParameter("p3") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(3, errors.length);
        for (int i = 0; i < errors.length; ++i) {
            assertEquals(Double.POSITIVE_INFINITY, errors[i], 1e-5);
        }
    }

}

class TestEstimator extends AbstractEstimator {

    @Override
    protected void estimate(EstimationProblem problem) throws EstimationException {
        // empty implementation
    }

}