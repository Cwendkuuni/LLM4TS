package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testExceedMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(1);
        for (int i = 0; i < 10; i++) {
            estimator.updateResidualsAndCost();
        }
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double expectedRMS = 1.4142135625;
        assertEquals(expectedRMS, estimator.getRMS(problem), 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double expectedChiSquare = 12.0;
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 1e-10);
    }

    @Test
    public void testGetCovariances() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] expectedCovariances = new double[][] { { 1.5, -0.5 },
                                                          { -0.5, 2.5 } };
        RealMatrix expectedCovarianceMatrix = new RealMatrixImpl(expectedCovariances);
        RealMatrix actualCovarianceMatrix = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovarianceMatrix.getData(), actualCovarianceMatrix.getData());
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] expectedErrors = new double[] { 0.707106781186548, 0.707106781186548 };
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 1e-10);
    }

    @Test(expected = EstimationException.class)
    public void testSingularProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(3, 1) };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        assertEquals(4, estimator.getRows());
        assertEquals(2, estimator.getCols());
        assertEquals(8, estimator.getJacobian().length);
        assertEquals(4, estimator.getResiduals().length);
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 1e-10);
    }

    @Test
    public void testEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] { new WeightedMeasurement(1, 2),
                                                 new WeightedMeasurement(3, 1),
                                                 new WeightedMeasurement(4, 0),
                                                 new WeightedMeasurement(5, 3)};
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        AbstractEstimator estimator = new TestEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        EstimatedParameter[] expectedParameters = problem.getAllParameters();
        assertEquals(expectedParameters[0].getName(), estimator.getParameters()[0].getName(), "p1");
        assertEquals(expectedParameters[1].getName(), estimator.getParameters()[1].getName(), "p2");
        assertEquals(1.5, estimator.getParameters()[0].getEstimate(), 1e-10);
        assertEquals(-0.5, estimator.getParameters()[1].getEstimate(), 1e-10);
    }
}

class TestEstimator extends AbstractEstimator {
    @Override
    protected void estimate(EstimationProblem problem)
            throws EstimationException {
        SimpleRegression regression = new SimpleRegression();
        RealVector parameters = problem.getAllParameters();
        RealVector data = new ArrayRealVector(new double[] { 1, 2, 3, 4, 5 });
        RealVector constants = new ArrayRealVector(new double[] { 2, 1, 0, 3 });
        regression.fit(data, constants);
        super.updateParameters(regression.getParameterEstimates());
    }
}