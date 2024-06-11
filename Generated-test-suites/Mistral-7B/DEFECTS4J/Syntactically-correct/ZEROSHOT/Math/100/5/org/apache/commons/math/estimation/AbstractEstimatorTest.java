import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        estimator.setMaxCostEval(10);
        Assert.assertEquals(10, estimator.getCostEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testExceedMaxCostEval() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        estimator.setMaxCostEval(1);
        estimator.estimate(createDummyEstimationProblem());
        estimator.estimate(createDummyEstimationProblem());
    }

    @Test
    public void testGetCostEvaluations() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        estimator.estimate(createDummyEstimationProblem());
        estimator.estimate(createDummyEstimationProblem());
        Assert.assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.updateJacobian();
            }
        };

        estimator.estimate(createDummyEstimationProblem());
        estimator.estimate(createDummyEstimationProblem());
        estimator.estimate(createDummyEstimationProblem());
        estimator.estimate(createDummyEstimationProblem());
        Assert.assertEquals(4, estimator.getJacobianEvaluations());
    }

    @Test
    public void testGetRMS() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        Assert.assertNotEquals(Double.POSITIVE_INFINITY, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        Assert.assertNotEquals(Double.POSITIVE_INFINITY, chiSquare, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testSingularProblem() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.getCovariances(problem);
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[0]);
        estimator.estimate(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        Assert.assertArrayNotEquals(new double[0], errors);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
                Assert.assertArrayEquals(new WeightedMeasurement[0], measurements);
                Assert.assertArrayEquals(new EstimatedParameter[0], parameters);
                Assert.assertArrayEquals(new double[0], jacobian);
                Assert.assertArrayEquals(new double[0], residuals);
                Assert.assertEquals(Double.POSITIVE_INFINITY, cost, 0.0);
                Assert.assertEquals(0, costEvaluations);
                Assert.assertEquals(0, jacobianEvaluations);
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);

        Assert.assertNotEquals(new WeightedMeasurement[0], measurements);
        Assert.assertNotEquals(new EstimatedParameter[0], parameters);
        Assert.assertNotEquals(new double[0], jacobian);
        Assert.assertNotEquals(new double[0], residuals);
        Assert.assertNotEquals(Double.POSITIVE_INFINITY, cost, 0.0);
        Assert.assertEquals(1, costEvaluations);
        Assert.assertEquals(1, jacobianEvaluations);
    }

    @Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void updateJacobian() {
                super.updateJacobian();
                for (int i = 0; i < rows * cols; ++i) {
                    Assert.assertEquals(0, jacobian[i]);
                }
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);
        estimator.updateJacobian();

        for (int i = 0; i < rows * cols; ++i) {
            Assert.assertEquals(0, jacobian[i], 0.001);
        }
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                super.updateResidualsAndCost();
                for (int i = 0; i < rows; ++i) {
                    Assert.assertEquals(0, residuals[i], 0.001);
                }
                Assert.assertEquals(0, cost, 0.001);
            }

            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            protected void updateJacobian() {
                super.updateJacobian();
            }
        };

        EstimationProblem problem = createDummyEstimationProblem();
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();

        for (int i = 0; i < rows; ++i) {
            Assert.assertEquals(0, residuals[i], 0.001);
        }
        Assert.assertEquals(0, cost, 0.001);
    }

    private EstimationProblem createDummyEstimationProblem() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        };

        problem.setAllParameters(new EstimatedParameter[1]);
        problem.setMeasurements(new WeightedMeasurement[1]);
        problem.setUnboundParameters(new EstimatedParameter[1]);
        return problem;
    }

    private RealVector createDummyVector(double[] data) {
        return new ArrayRealVector(data);
    }

    private RealMatrixImpl createDummyMatrix(double[][] data) {
        return new RealMatrixImpl(new Array2DRowRealMatrix(data));
    }

    private WeightedMeasurement createDummyWeightedMeasurement() {
        WeightedMeasurement wm = new WeightedMeasurement() {
            @Override
            public double getWeight() {
                return 1;
            }

            @Override
            public double getResidual() {
                return 1;
            }

            @Override
            public double[] getUnboundParameters() {
                return new double[]{0};
            }

            @Override
            public double[] getParameters() {
                return new double[]{0};
            }

            @Override
            public double[] getPartialDerivatives() {
                return new double[]{0};
            }
        };

        wm.setMeasurement(createDummyVector(new double[]{1}));
        wm.setResidual(1);
        return wm;
    }

}