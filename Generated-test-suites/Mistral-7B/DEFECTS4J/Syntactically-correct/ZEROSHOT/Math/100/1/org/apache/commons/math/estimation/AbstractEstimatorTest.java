import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
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

        AbstractEstimator estimator = new AbstractEstimator() {
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

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.initializeEstimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalExceeded() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[1];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[1];
            }

            @Override
            public int getMeasurementsLength() {
                return 1;
            }

            @Override
            public int getParametersLength() {
                return 1;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
                setMaxCostEval(1);
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                super.updateResidualsAndCost();
                Assert.assertEquals(1, costEvaluations);
                Assert.assertEquals(0, jacobianEvaluations);
                throw new EstimationException("maximal number of evaluations exceeded (1)");
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                initializeEstimate(problem);
                updateResidualsAndCost();
            }
        };

        estimator.estimate(problem);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(new double[] {1.0, 2.0}, 1.0),
                        new WeightedMeasurement(new double[] {3.0, 4.0}, 1.0),
                        new WeightedMeasurement(new double[] {5.0, 6.0}, 1.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 2;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return super.getRMS(problem) * 2.0;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        Assert.assertEquals(3.1274242424242423E-16, rms, 1.0e-10);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(new double[] {1.0, 2.0}, 4.0),
                        new WeightedMeasurement(new double[] {3.0, 4.0}, 1.0),
                        new WeightedMeasurement(new double[] {5.0, 6.0}, 4.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 2;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return super.getChiSquare(problem) * 2.0;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        Assert.assertEquals(12.25, chiSquare, 1.0e-10);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(new double[] {1.0, 2.0}, 4.0),
                        new WeightedMeasurement(new double[] {3.0, 4.0}, 1.0),
                        new WeightedMeasurement(new double[] {5.0, 6.0}, 4.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[] {
                        new EstimatedParameter("p1", new ArrayRealVector(new double[] {1.0})),
                        new EstimatedParameter("p2", new ArrayRealVector(new double[] {2.0}))
                };
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 2;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                double[][] covariances = super.getCovariances(problem);
                double[][] covariancesCopy = new double[2][2].clone();
                System.arraycopy(covariances, 0, covariancesCopy, 0, 4);
                return covariancesCopy;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[][] covariances = estimator.getCovariances(problem);

        Array2DRowRealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][] {
                {1.5, 0.5},
                {0.5, 1.5}
        });

        Assert.assertEquals(expectedCovariances, new RealMatrixImpl(covariances));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(new double[] {1.0, 2.0}, 4.0),
                        new WeightedMeasurement(new double[] {3.0, 4.0}, 1.0),
                        new WeightedMeasurement(new double[] {5.0, 6.0}, 4.0)
                };
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public int getMeasurementsLength() {
                return 3;
            }

            @Override
            public int getParametersLength() {
                return 2;
            }
        };

        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                double[] errors = super.guessParametersErrors(problem);
                double[] errorsCopy = new double[2].clone();
                System.arraycopy(errors, 0, errorsCopy, 0, 2);
                return errorsCopy;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };

        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);

        Assert.assertEquals(3.0, errors[0], 1.0e-10);
        Assert.assertEquals(3.0, errors[1], 1.0e-10);
    }
}