package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedObservedStats;
import org.apache.commons.math3.stat.regression.WeightedRegression;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.AbstractEstimator.EstimationProblem;
import org.apache.commons.math.estimation.AbstractEstimator.WeightedMeasurement;
import org.apache.commons.math.estimation.Regression;

/**
 * Test suite for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    private double[][] data;
    private double[] parameters;
    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        data = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        parameters = new double[] { 1.0, 1.0 };
        estimator = new Regression();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator = new Regression();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        assertEquals(3 * 3 + 3 * 2, ae.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        WeightedRegression wr = new WeightedRegression();
        wr.setMaxIterations(1);
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        assertEquals(2, ae.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        estimator.updateJacobian();
        RealMatrix jacobian = estimator.getJacobian();
        RealMatrix expectedJacobian = new DenseMatrix(2, 2, new double[][] { { -1.0, -1.0 },
            { -2.0, -2.0 } });
        assertEquals(expectedJacobian, jacobian);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateJacobianWithMaxCostEvalReached() {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        estimator.updateJacobian();
        estimator.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        estimator.updateResidualsAndCost();
        double expectedCost = Math.sqrt(1.0 / 3.0 * (1.0 * 1.0 + 4.0 * 4.0 + 9.0 * 9.0));
        assertEquals(expectedCost, estimator.getCost(), 1e-6);
    }

    @Test
    public void testGetRMS() {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        assertEquals(Math.sqrt(1.0 / 3.0 * (1.0 * 1.0 + 4.0 * 4.0 + 9.0 * 9.0)), ae.getRMS(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        }), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        assertEquals(1.0 / 3.0 * (1.0 * 1.0 + 4.0 * 4.0 + 9.0 * 9.0), ae.getChiSquare(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        }), 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        double[][] expectedCovariances = new double[][] { { 0.5, -0.5 }, { -0.5, 0.5 } };
        RealMatrix covarianceMatrix = ae.getCovariances(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        assertTrue(Arrays.deepEquals(expectedCovariances, covarianceMatrix.getData()));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        double[] expectedErrors = new double[] { 1.7320508075688772, 1.7320508075688772 };
        double[] errors = ae.guessParametersErrors(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        assertArrayEquals(expectedErrors, errors, 0.01);
    }

    @Test
    public void testEstimate() throws EstimationException {
        WeightedRegression wr = new WeightedRegression();
        wr.estimate(new EstimationProblem() {
            @Override
            public RealVector getMeasurements() {
                WeightedObservedStats wos = new WeightedObservedStats(data);
                return wos.getX();
            }

            @Override
            public RealVector getParameters() {
                return new ArrayRealVector(parameters);
            }

            @Override
            public int getMeasurementsDimension() {
                return 2;
            }

            @Override
            public int getParameterDimension() {
                return parameters.length;
            }

            @Override
            public WeightedMeasurement getMeasurement(int i) {
                return wos.getMeasurement(i);
            }
        });
        AbstractEstimator ae = (AbstractEstimator) wr;
        RealVector parametersEstimated = ae.getParameters();
        double[] expectedParameters = new double[] { 1.0, 1.0 };
        assertEquals(expectedParameters[0], parametersEstimated.getEntry(0), 0.01);
        assertEquals(expectedParameters[1], parametersEstimated.getEntry(1), 0.01);
    }
}