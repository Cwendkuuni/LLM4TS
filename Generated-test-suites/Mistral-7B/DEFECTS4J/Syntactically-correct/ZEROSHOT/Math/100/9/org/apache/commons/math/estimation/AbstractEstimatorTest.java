package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.Statistics;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) {
                // empty implementation
            }
        };
        problem = new EstimationProblem() {
            @Override
            public int getMeasurementCount() {
                return 3;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 2.0),
                        new WeightedMeasurement(3.0, 1.0),
                        new WeightedMeasurement(4.0, 4.0)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0)
                };
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.costEvaluations = 5;
        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 3;
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.rows = 3;
        estimator.cols = 2;
        estimator.jacobian = new double[6];
        estimator.updateJacobian();
        assertArrayEquals(new double[] {-1.0, -1.0, -1.0, 1.0, 1.0, 1.0}, estimator.jacobian, 0.001);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        double[] residuals = new double[3];
        estimator.residuals = residuals;
        estimator.cost = 0.0;
        estimator.updateResidualsAndCost();
        assertArrayEquals(new double[] {2.44948974278317, 1.41421369578969, 5.0}, estimator.residuals, 0.001);
        assertEquals(3.16227766016838, estimator.cost, 0.001);
    }

    @Test
    public void testGetRms() {
        assertEquals(2.58266938432495, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        assertEquals(12.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testgetCovariances() throws EstimationException {
        estimator.initializeEstimate(problem);
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        Array2DRowRealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(new double[][] {
                {1.13433384238186, 0.65458126385674},
                {0.65458126385674, 2.02812339824047}
        });
        assertArrayEquals(expectedCovarianceMatrix.getData(), covarianceMatrix.getData(), 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] {0.914472025142964, 1.11934187269162}, errors, 0.001);
    }

    @Test
    public void testEstimateThrowsWhenMaxCostEvalExceeded() {
        estimator.setMaxCostEval(2);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }
}