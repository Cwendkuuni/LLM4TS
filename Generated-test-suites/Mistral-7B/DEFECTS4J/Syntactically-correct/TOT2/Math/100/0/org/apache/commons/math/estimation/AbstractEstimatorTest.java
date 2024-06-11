import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedStatistics;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.EstimatedParameter;
import org.apache.commons.math.estimation.WeightedMeasurement;

/**
 * Tests for {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {
    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] {1, 2, 3});
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(new double[] {0, 1});
            }

            @Override
            public WeightedObservedStatistics getObservedStatistics() {
                return new WeightedObservedStatistics(null, null);
            }
        };
        estimator.initializeEstimate(problem);
    }

    /**
     * Tests {@link AbstractEstimator#setMaxCostEval(int)} method.
     */
    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Tests {@link AbstractEstimator#getCostEvaluations()} method.
     */
    @Test
    public void testGetCostEvaluations() {
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    /**
     * Tests {@link AbstractEstimator#getJacobianEvaluations()} method.
     */
    @Test
    public void testGetJacobianEvaluations() {
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    /**
     * Tests {@link AbstractEstimator#updateJacobian()} method.
     */
    @Test(expected = EstimationException.class)
    public void testUpdateJacobianException() {
        estimator.setMaxCostEval(0);
        estimator.updateJacobian();
    }

    /**
     * Tests {@link AbstractEstimator#updateResidualsAndCost()} method.
     */
    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostException() {
        estimator.setMaxCostEval(2);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    /**
     * Tests {@link AbstractEstimator#getRMS(EstimationProblem)} method.
     */
    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        assertTrue(rms > 0);
    }

    /**
     * Tests {@link AbstractEstimator#getChiSquare(EstimationProblem)} method.
     */
    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertTrue(chiSquare > 0);
    }

    /**
     * Tests {@link AbstractEstimator#getCovariances(EstimationProblem)} method.
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        assertEquals(problem.getAllParameters().length, covarianceMatrix.getRowDimension());
        assertEquals(problem.getAllParameters().length, covarianceMatrix.getColumnDimension());
    }

    /**
     * Tests {@link AbstractEstimator#guessParametersErrors(EstimationProblem)} method.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] {Double.NaN, Double.NaN}, errors, 0.001);
    }
}