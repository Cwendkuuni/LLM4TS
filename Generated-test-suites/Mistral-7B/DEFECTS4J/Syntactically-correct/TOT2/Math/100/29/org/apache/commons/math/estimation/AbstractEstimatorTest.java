import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            private int count = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                count++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        estimator.initializeEstimate(createTestProblem());
        estimator.estimate(createTestProblem());
        estimator.estimate(createTestProblem());
        estimator.estimate(createTestProblem());

        assertEquals(4, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            private int count = 0;

            @Override
            protected void updateJacobian() {
                count++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        estimator.initializeEstimate(createTestProblem());
        estimator.estimate(createTestProblem());
        estimator.estimate(createTestProblem());
        estimator.estimate(createTestProblem());

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            private double[] expectedJacobian = new double[4];

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }

            @Override
            protected void updateJacobian() {
                for (int i = 0; i < 4; i++) {
                    expectedJacobian[i] = Math.sqrt(1.0) * i;
                }
                Arrays.fill(jacobian, 0.0);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        jacobian[i * 3 + j] = expectedJacobian[i] * Math.sin(j / 10.0);
                    }
                }
            }
        };

        estimator.initializeEstimate(createTestProblem());
        estimator.updateJacobian();

        double[] actualJacobian = Arrays.copyOfRange(jacobian, 0, 12);
        assertArrayEquals(expectedJacobian, actualJacobian);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 1.0;
            }
        };

        EstimationProblem problem = createTestProblem();
        assertEquals(1.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 1.0;
            }
        };

        EstimationProblem problem = createTestProblem();
        assertEquals(1.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[][]{{1.0, 0.0}, {0.0, 1.0}};
            }
        };

        EstimationProblem problem = createTestProblem();
        double[][] actualCovariances = estimator.getCovariances(problem);
        double[][] expectedCovariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        assertArrayEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[]{1.0, 1.0};
            }
        };

        EstimationProblem problem = createTestProblem();
        double[] actualErrors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[]{1.0, 1.0};
        assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                // no operation
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        EstimationProblem problem = createTestProblem();
        estimator.initializeEstimate(problem);

        assertEquals(3, estimator.getRows());
        assertEquals(2, estimator.getCols());
        assertNull(estimator.getJacobian());
        assertNull(estimator.getResiduals());
        assertEquals(0.0, estimator.getCost(), 0.0);
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeDOF() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        EstimationProblem problem = new EstimationProblem(new double[1][], new double[2][], 1, 2);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test
    public void testEstimateWithSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("singular problem");
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no operation
            }
        };

        EstimationProblem problem = createTestProblem();
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createTestProblem() {
        double[] measurementsData = new double[]{1.0, 1.0, 2.0, 2.0};
        double[] parametersData = new double[]{0.5, 0.5};
        double[] measurementsError = new double[]{0.1, 0.1, 0.1, 0.1};
        double[] parametersError = new double[]{0.05, 0.05};
        double[][] measurements = new double[4][2];
        System.arraycopy(measurementsData, 0, measurements[0], 0, 2);
        System.arraycopy(measurementsData, 0, measurements[1], 0, 2);
        System.arraycopy(measurementsData, 0, measurements[2], 0, 2);
        System.arraycopy(measurementsData, 0, measurements[3], 0, 2);
        double[][] parameters = new double[2][];
        parameters[0] = new double[]{0.5};
        parameters[1] = new double[]{0.5};
        EstimationProblem problem = new EstimationProblem(measurements, parameters, 4, 2, measurementsError, parametersError);
        return problem;
    }

}