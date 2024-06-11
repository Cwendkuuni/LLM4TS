package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
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
    public void init() {
        estimator = new DummyEstimator();
        problem = new EstimationProblem() {

            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public int getParametersNumber() {
                return 1;
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealMatrix(new double[][] { {1, 2}, {4, 6}, {9, 10} })
                        .getColumnVector(0);
            }

            @Override
            public RealVector getParameters() {
                return new Array2DRowRealMatrix(new double[][] { {0} })
                        .getColumnVector(0);
            }

            @Override
            public RealVector getAllParameters() {
                return new Array2DRowRealMatrix(new double[][] { {0} })
                        .getColumnVector(0);
            }

            @Override
            public SimpleRegression getRegression() {
                return null;
            }

            @Override
            public void setMeasurements(RealVector measurements) {
                // not used
            }

            @Override
            public void setParameters(RealVector parameters) {
                // not used
            }

            @Override
            public void setAllParameters(RealVector parameters) {
                // not used
            }
        };
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        int initialValue = estimator.getMaxCostEval();
        estimator.setMaxCostEval(42);
        assertEquals(42, estimator.getMaxCostEval());
        estimator.setMaxCostEval(initialValue);
    }

    @Test
    public void testSetAndGetCostEvaluations() {
        int initialCostEvaluations = estimator.getCostEvaluations();
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations() - initialCostEvaluations);
    }

    @Test
    public void testSetAndGetJacobianEvaluations() {
        int initialJacobianEvaluations = estimator.getJacobianEvaluations();
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations() - initialJacobianEvaluations);
    }

    @Test
    public void testGetRMS() {
        double expectedRMS = Math.sqrt(12.0 / 3.0);
        assertEquals(expectedRMS, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double expectedChiSquare = 2.0;
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] expectedCovariances = { {1.0 / 3.0} };
        RealMatrix actualCovariances = estimator.getCovariances(problem);
        assertEquals(expectedCovariances[0][0], actualCovariances.getEntry(0, 0), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double expectedError = Math.sqrt(1.0 / 3.0);
        double[] actualErrors = estimator.guessParametersErrors(problem);
        assertEquals(expectedError, actualErrors[0], 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithTooFewMeasurements() throws EstimationException {
        problem.setMeasurementsNumber(0);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithTooManyMeasurements() throws EstimationException {
        problem.setMeasurementsNumber(4);
        estimator.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithTooManyParameters() throws EstimationException {
        problem.setParametersNumber(4);
        estimator.estimate(problem);
    }

}