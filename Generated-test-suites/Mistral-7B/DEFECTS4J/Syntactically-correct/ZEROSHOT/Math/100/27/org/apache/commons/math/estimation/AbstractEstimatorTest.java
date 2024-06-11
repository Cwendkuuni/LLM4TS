package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.stat.regression.WeightedObservedVersusFitted;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.LeastSquaresEstimationProblem;

public class AbstractEstimatorTest {

    @org.junit.Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                // empty implementation
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @org.junit.Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateResidualsAndCost()
                    throws EstimationException {
                ++counter;
            }

            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                for (int i = 0; i < 5; ++i) {
                    updateResidualsAndCost();
                }
            }
        };
        assertEquals(5, estimator.getCostEvaluations());
    }

    @org.junit.Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                ++counter;
            }

            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(new LeastSquaresEstimationProblem(1, 1));
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @org.junit.Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            double[] jacobian = new double[3];
            WeightedMeasurement[] measurements = new WeightedMeasurement[2];
            EstimatedParameter[] parameters = new EstimatedParameter[1];

            @Override
            protected void updateJacobian() {
                measurements[0] = new WeightedMeasurement(1, 1, 2, 1);
                measurements[1] = new WeightedMeasurement(1, 1, 3, 1);
                parameters[0] = new EstimatedParameter(0);
                for (int i = 0; i < 3; ++i) {
                    jacobian[i] = i;
                }
            }

            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(new LeastSquaresEstimationProblem(2, 1));
        assertArrayEquals(new double[][] {
                {-1.5, -1.5},
                {-1.5, -1.5}}, estimator.jacobian, 0);
    }

    @org.junit.Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0;
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(1, 1);
        assertEquals(0, estimator.getRMS(problem), 0);
    }

    @org.junit.Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(1, 1);
        assertEquals(0, estimator.getChiSquare(problem), 0);
    }

    @org.junit.Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[][] {
                        {1, 0},
                        {0, 1}
                };
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(2, 2);
        RealMatrix covariance = estimator.getCovariances(problem);
        assertNotNull(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {1, 0},
                {0, 1}});
        assertMatrixEquals(expected, covariance);
    }

    @org.junit.Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[] {1, 1};
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(2, 2);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] {1, 1}, errors, 0);
    }

    @org.junit.Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // empty implementation
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(2, 1);
        estimator.initializeEstimate(problem);
        assertArrayEquals(new WeightedMeasurement[2], estimator.measurements);
        assertArrayEquals(new EstimatedParameter[1], estimator.parameters);
        assertEquals(2, estimator.rows);
        assertEquals(1, estimator.cols);
        assertArrayEquals(new double[2 * 1], estimator.jacobian);
        assertArrayEquals(new double[2], estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @org.junit.Test
    public void testEstimateThrowsException() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                throw new EstimationException("test error");
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(1, 1);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    @org.junit.Test
    public void testEstimateReturnsValues() {
        AbstractEstimator estimator = new AbstractEstimator() {
            double[] parameters = new double[1];

            @Override
            public void estimate(EstimationProblem problem)
                    throws EstimationException {
                parameters[0] = 1;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {new EstimatedParameter(0, parameters[0])};
            }
        };
        EstimationProblem problem = new LeastSquaresEstimationProblem(1, 1);
        estimator.estimate(problem);
        RealVector expected = VectorUtils.createRealVector(new double[] {1});
        RealVector actual = problem.getAllParameters();
        assertMatrixEquals(expected, actual, 0);
    }

    private static void assertMatrixEquals(RealMatrix expected, RealMatrix actual) {
        assertMatrixEquals(expected, actual, 1e-12);
    }

    private static void assertMatrixEquals(RealMatrix expected, RealMatrix actual, double tolerance) {
        WeightedObservedVersusFitted ovf = new WeightedObservedVersusFitted(expected, actual);
        assertEquals(0, ovf.rSquared(), 0);
        assertEquals(0, ovf.adjustedRSquared(), 0);
        assertEquals(0, ovf.rootMeanSumSquare(), tolerance);
        assertEquals(0, ovf.rootMeanSquare(), tolerance);
        assertEquals(0, ovf.meanSquareError(), tolerance);
        assertEquals(0, ovf.standardError(), tolerance);
        assertEquals(0, ovf.tStatistic(), 0);
        assertEquals(0, ovf.probabilityProduct(), 0);
        assertEquals(0, ovf.probability(), 0);
        assertEquals(0, ovf.standardizedResiduals().getMaximum(), tolerance);
        assertEquals(0, ovf.standardizedResiduals().getMinimum(), tolerance);
        assertEquals(0, ovf.standardizedResiduals().getMean(), 0);
        assertEquals(0, ovf.standardizedResiduals().getVariance(), tolerance);
        assertEquals(0, ovf.standardizedResiduals().getStandardDeviation(), tolerance);
    }
}