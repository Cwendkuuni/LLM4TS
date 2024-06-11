package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testSetMaxCostEval() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                fail("Not implemented");
            }
        };

        estimator.setMaxCostEval(10);
        Assert.assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test method for {@link AbstractEstimator#getCostEvaluations()}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testGetCostEvaluations() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                assert counter <= estimator.getMaxCostEval();
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateResidualsAndCost();
            }
        };

        estimator.setMaxCostEval(1);
        estimator.estimate(createProblem(new double[] { 1, 1 }, new double[] { 1, 2 }));
        Assert.assertEquals(1, estimator.getCostEvaluations());
        Assert.assertEquals(0, estimator.getJacobianEvaluations());

        estimator.setMaxCostEval(3);
        estimator.estimate(createProblem(new double[] { 1, 1, 1 }, new double[] { 1, 2, 2 }));
        Assert.assertEquals(3, estimator.getCostEvaluations());
        Assert.assertEquals(0, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testGetJacobianEvaluations() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateJacobian() {
                estimator.incrementJacobianEvaluationsCounter();
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
            }
        };

        estimator.estimate(createProblem(new double[] { 1 }, new double[] { 1 }));
        Assert.assertEquals(1, estimator.getJacobianEvaluations());
        Assert.assertEquals(1, estimator.getCostEvaluations());

        estimator.estimate(createProblem(new double[] { 1, 1 }, new double[] { 1, 2 }));
        Assert.assertEquals(2, estimator.getJacobianEvaluations());
        Assert.assertEquals(2, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateJacobian()}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testUpdateJacobian() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateJacobian() {
                estimator.incrementJacobianEvaluationsCounter();
                Arrays.fill(jacobian, 1);
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateJacobian();
            }
        };

        EstimationProblem problem = createProblem(
            new double[] { 1, 1, 1, 1 },
            new double[] { 1, 2, 3, 4 }
        );

        estimator.estimate(problem);

        RealMatrix jacobian = new Array2DRowRealMatrix(estimator.getJacobian());
        RealMatrix expectedJacobian = new SingularValueDecomposition(
            new Array2DRowRealMatrix(new double[][] {
                { -1,  1,  1, -1 },
                { -2,  2,  2,  2 },
                { -3,  3,  3,  3 },
                { -4,  4,  4,  4 }
            })).getV().transpose();

        Assert.assertEquals(expectedJacobian, jacobian);
        Assert.assertEquals(3, estimator.getJacobianEvaluations());
        Assert.assertEquals(3, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateResidualsAndCost()}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                estimator.incrementCostEvaluationsCounter();
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                estimator.initializeEstimate(problem);
                estimator.updateResidualsAndCost();
            }
        };

        estimator.estimate(createProblem(new double[] { 1, 1 }, new double[] { 1, 2 }));
        Assert.assertEquals(1, estimator.getCostEvaluations());
        Assert.assertEquals(1, estimator.getJacobianEvaluations());

        estimator.estimate(createProblem(new double[] { 1, 1, 1 }, new double[] { 1, 2, 2 }));
        Assert.assertEquals(2, estimator.getCostEvaluations());
        Assert.assertEquals(2, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testGetRMS() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return 1;
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }
        };

        EstimationProblem problem = createProblem(
            new double[] { 1, 1, 1, 1 },
            new double[] { 1, 2, 3, 4 }
        );

        estimator.estimate(problem);

        double rms = estimator.getRMS(problem);
        double chiSquare = estimator.getChiSquare(problem);
        double errors[] = estimator.guessParametersErrors(problem);

        Assert.assertEquals(Math.sqrt(2.0 / 4.0), rms, 0.001);
        Assert.assertEquals(4.0 / 2.0, chiSquare, 0.001);
        Assert.assertEquals(Math.sqrt(2.0 / 4.0), errors[0], 0.001);
        Assert.assertEquals(Math.sqrt(2.0 / 4.0), errors[1], 0.001);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                throw new UnsupportedOperationException();
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return 1;
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0;
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                double[][] covariances = new double[][] {
                    { 1, 0 },
                    { 0, 1 }
                };

                return covariances;
            }
        };

        EstimationProblem problem = createProblem(
            new double[] { 1, 1, 1, 1 },
            new double[] { 1, 2, 3, 4 }
        );

        estimator.estimate(problem);

        double[][] covariances = estimator.getCovariances(problem);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(Arrays.asList(2.0, 2.5), Arrays.asList(4.0, 5.0));
        double[] expectedCovariances = regression.estimateRegressionParameters();

        Assert.assertArrayEquals(expectedCovariances, covariances[0], 0.001);
        Assert.assertArrayEquals(expectedCovariances, covariances[1], 0.001);
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                double[] errors = new double[2];
                double[] covariances = getCovariances(problem);

                for (int i = 0; i < 2; ++i) {
                    errors[i] = Math.sqrt(covariances[i][i]);
                }

                return errors;
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                double[][] covariances = new double[][] {
                    { 1, 0 },
                    { 0, 1 }
                };

                return covariances;
            }
        };

        EstimationProblem problem = createProblem(
            new double[] { 1, 1, 1, 1 },
            new double[] { 1, 2, 3, 4 }
        );

        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[] { Math.sqrt(2.0 / 4.0), Math.sqrt(2.0 / 4.0) };

        Assert.assertArrayEquals(expectedErrors, errors, 0.001);
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithNegativeCostEvaluations() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public int getMaxCostEval() {
                return -1;
            }

            @Override
            public void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                fail("Not implemented");
            }
        };

        EstimationProblem problem = createProblem(new double[0], new double[0]);
        estimator.initializeEstimate(problem);
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithNullMeasurements() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator();

        estimator.initializeEstimate(null);
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     * @throws EstimationException if the test fails
     */
    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithNullParameters() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator();

        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public RealVector getAllParameters() {
                return null;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }
        });
    }

    /**
     * Creates a new {@link EstimationProblem}.
     * @param parameters the parameters
     * @param measurements the measurements
     * @return the estimation problem
     */
    private static EstimationProblem createProblem(double[] parameters, double[] measurements) {
        WeightedMeasurement[] wm = new WeightedMeasurement[measurements.length];
        for (int i = 0; i < measurements.length; ++i) {
            wm[i] = new WeightedMeasurement(measurements[i], new EstimatedParameter(i, parameters[i]));
        }

        return new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return wm;
            }

            @Override
            public RealVector getAllParameters() {
                return new Array2DSample(new double[][] { parameters }, 1, 0);
            }
        };
    }

}