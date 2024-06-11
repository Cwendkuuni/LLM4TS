package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluations++;
            }
        };
        estimator.initializeEstimate(createProblem(3, 2));
        estimator.estimate(createProblem(3, 2));
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                jacobianEvaluations++;
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }
        };
        estimator.initializeEstimate(createProblem(3, 2));
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 5.0;
            }
        };
        EstimationProblem problem = createProblem(3, 2);
        assertEquals(5.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 3.0;
            }
        };
        EstimationProblem problem = createProblem(3, 2);
        assertEquals(3.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void updateJacobian() {
                // do nothing
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[][]{{1.0, 0.0}, {0.0, 1.0}};
            }
        };
        EstimationProblem problem = createProblem(3, 2);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actual = new Array2DRowRealMatrix(estimator.getCovariances(problem));
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[]{1.0, 1.0};
            }
        };
        EstimationProblem problem = createProblem(3, 2);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] expected = new double[]{1.0, 1.0};
        double[] actual = estimator.guessParametersErrors(problem);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInitializeEstimateWithDegreesOfFreedomLessThanMeasurements()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            // empty implementation
        };
        EstimationProblem problem = createProblem(2, 3);
        assertThrows(EstimationException.class, () -> {
            estimator.initializeEstimate(problem);
            estimator.estimate(problem);
        });
    }

    private EstimationProblem createProblem(int rows, int cols) {
        RealVector measurements = VectorUtils.createVector(rows);
        RealVector parameters = VectorUtils.createVector(cols);
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return rows;
            }

            @Override
            public int getAllParametersNumber() {
                return cols;
            }

            @Override
            public RealVector getMeasurements() {
                return measurements;
            }

            @Override
            public RealVector getAllParameters() {
                return parameters;
            }

            @Override
            public RealVector getUnboundParameters() {
                return parameters;
            }
        };
        for (int i = 0; i < rows; i++) {
            measurements.setEntry(i, i, StatUtils.GAUSSIAN_RANDOM.nextGaussian() + 1.0);
        }
        return problem;
    }

}