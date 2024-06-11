import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setup() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public int getMeasurementsLength() {
                return 0;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };
    }

    @Test
    public void testGettersAndSetters() {
        estimator.setMaxCostEval(10);
        Assert.assertEquals(10, estimator.getMaxCostEval());
        estimator.setMaxCostEval(20);
        Assert.assertEquals(20, estimator.getMaxCostEval());
        Assert.assertEquals(0, estimator.getCostEvaluations());
        Assert.assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        Assert.assertArrayEquals(new double[0], estimator.jacobian, 0.0);
        estimator.initializeEstimate(createProblemWithData());
        estimator.updateJacobian();
        Assert.assertArrayNotEquals(new double[0][], estimator.jacobian, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateJacobianWithMaxCostEvalReached() throws EstimationException {
        estimator.setMaxCostEval(0);
        estimator.initializeEstimate(createProblemWithData());
        estimator.updateJacobian();
        estimator.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        Assert.assertEquals(0.0, estimator.cost, 0.0);
        estimator.initializeEstimate(createProblemWithData());
        estimator.updateResidualsAndCost();
        Assert.assertNotEquals(0.0, estimator.cost, 0.0);
    }

    @Test
    public void testGetRMS() throws EstimationException {
        estimator.initializeEstimate(createProblemWithRMSData());
        Assert.assertEquals(1.0, estimator.getRMS(problem), 0.01);
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        estimator.initializeEstimate(createProblemWithChiSquareData());
        Assert.assertEquals(10.0, estimator.getChiSquare(problem), 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(createProblemWithCovariancesData());
        double[][] covariances = estimator.getCovariances(problem);
        Assert.assertNotNull(covariances);
        Assert.assertEquals(3, covariances.length);
        Assert.assertEquals(3, covariances[0].length);
        Assert.assertEquals(3, covariances[1].length);
        Assert.assertEquals(3, covariances[2].length);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(createProblemWithGuessParametersErrorsData());
        double[] errors = estimator.guessParametersErrors(problem);
        Assert.assertArrayNotEquals(new double[0], errors, 0.0);
        Assert.assertEquals(3, errors.length);
    }

    private EstimationProblem createProblemWithData() {
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(1.0, 2.0, 1.0),
                        new WeightedMeasurement(2.0, 3.0, 2.0),
                        new WeightedMeasurement(3.0, 4.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0),
                        new EstimatedParameter("p3", 0.0)
                };
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 3;
            }
        };
    }

    private EstimationProblem createProblemWithRMSData() {
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(1.0, 1.0, 1.0),
                        new WeightedMeasurement(2.0, 4.0, 2.0),
                        new WeightedMeasurement(3.0, 9.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };
    }

    private EstimationProblem createProblemWithChiSquareData() {
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(1.0, 1.0, 1.0, 0.1),
                        new WeightedMeasurement(2.0, 4.0, 2.0, 2.0),
                        new WeightedMeasurement(3.0, 9.0, 3.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 0;
            }
        };
    }

    private EstimationProblem createProblemWithCovariancesData() {
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(1.0, 1.0, 1.0),
                        new WeightedMeasurement(2.0, 4.0, 2.0),
                        new WeightedMeasurement(3.0, 9.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0),
                        new EstimatedParameter("p3", 0.0)
                };
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 3;
            }
        };
    }

    private EstimationProblem createProblemWithGuessParametersErrorsData() {
        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{
                        new WeightedMeasurement(1.0, 1.0, 1.0),
                        new WeightedMeasurement(2.0, 4.0, 2.0),
                        new WeightedMeasurement(3.0, 9.0, 3.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[]{
                        new EstimatedParameter("p1", 0.0),
                        new EstimatedParameter("p2", 0.0),
                        new EstimatedParameter("p3", 0.0)
                };
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 3;
            }
        };
    }
}