package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testRMS() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        assertEquals(3.0, estimator.getRMS(problem), 1e-10);
    }

    @Test
    public void testChiSquare() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        assertEquals(9.0, estimator.getChiSquare(problem), 1e-10);
    }

    @Test
    public void testCovariances() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix actual = estimator.getCovariances(problem);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        double[] expected = {2.5, 1.25};
        double[] actual = estimator.guessParametersErrors(problem);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = EstimationException.class)
    public void testEstimationException() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.setMaxCostEval(0);
        estimator.estimate(problem);
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] expected = new double[] {-2.0, -1.5};
        assertArrayEquals(expected, estimator.jacobian, 0);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double[] expected = new double[] {1.0, 1.5};
        assertArrayEquals(expected, estimator.residuals, 0);
        assertEquals(Math.sqrt(3.0), estimator.cost, 1e-10);
    }

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.setMaxCostEval(1);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.estimate(problem);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.estimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    private static class DummyEstimator extends AbstractEstimator {
        @Override
        void estimate(EstimationProblem problem) throws EstimationException {
            // empty
        }
    }

    private static class DummyProblem implements EstimationProblem {
        @Override
        WeightedMeasurement[] getMeasurements() {
            return new WeightedMeasurement[] {new WeightedMeasurement(1.0, 2.0, 1.0),
                                             new WeightedMeasurement(3.0, 4.0, 1.0)};
        }

        @Override
        EstimatedParameter[] getUnboundParameters() {
            return new EstimatedParameter[] {new EstimatedParameter(1.0), new EstimatedParameter(1.0)};
        }

        @Override
        EstimatedParameter[] getAllParameters() {
            return new EstimatedParameter[] {new EstimatedParameter(1.0), new EstimatedParameter(1.0)};
        }
    }

    private static void assertArrayEquals(double[] a, double[] b) {
        assertEquals(a.length, b.length);
        for (int i = 0; i < a.length; ++i) {
            assertEquals(a[i], b[i], 1e-10);
        }
    }

}