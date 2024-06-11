package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator} class.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)}.
     * @throws Exception if any error occurs
     */
    @Test
    public void testSetMaxCostEval() throws Exception {
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     * @throws Exception if any error occurs
     */
    @Test
    public void testEstimate() throws Exception {
        EstimationProblem problem = new EstimationProblem() {

            /**
             * {@inheritDoc}
             */
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getParametersNumber() {
                return 2;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0, 0.5, 1.5),
                    new WeightedMeasurement(2.0, -1.0, 0.7, 0.5),
                    new WeightedMeasurement(3.0, 1.0, 1.3, -0.1)
                };
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public EstimatedParameter[] getParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1", 0.0), new EstimatedParameter("p2", 0.0) };
            }
        };

        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        RealVector parameters = problem.getAllParameters();
        RealVector actual = problem.getAllParameters();
        RealVector expected = new ArrayRealVector(new double[] { 1.5, 0.5 });
        SimpleRegression regression = new SimpleRegression();
        RealMatrix X = new Array2DRowRealMatrix(3, 2, new double[][] {
            { 1.0, 1.0 },
            { 2.0, 1.0 },
            { 3.0, 1.0 }
        });
        RealMatrix Y = new Array2DRowRealMatrix(3, 1, new double[][] {
            { 1.5 },
            { 0.5 },
            { -0.1 }
        });
        regression.regress(X, Y.transpose());
        RealVector expectedParams = regression.getParameters();

        for (int i = 0; i < parameters.getDimension(); i++) {
            assertEquals(expectedParams.getEntry(i), actual.getEntry(i), 0.01);
        }
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)} with
     * maximal number of cost evaluations exceeded.
     * @throws Exception if any error occurs
     */
    @Test
    public void testEstimateMaxCostEvalExceeded() throws Exception {
        EstimationProblem problem = new EstimationProblem() {

            /**
             * {@inheritDoc}
             */
            @Override
            public int getMeasurementsNumber() {
                return 1000000;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getParametersNumber() {
                return 1;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public EstimatedParameter[] getParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1", 0.0) };
            }
        };
        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.setMaxCostEval(10);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetCovariances() throws Exception {
        EstimationProblem problem = new EstimationProblem() {

            /**
             * {@inheritDoc}
             */
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getParametersNumber() {
                return 2;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0, 0.5, 1.5),
                    new WeightedMeasurement(2.0, -1.0, 0.7, 0.5),
                    new WeightedMeasurement(3.0, 1.0, 1.3, -0.1)
                };
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public EstimatedParameter[] getParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1", 0.0), new EstimatedParameter("p2", 0.0) };
            }
        };

        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[][] expected = new double[][] { { 0.1, 0.05 }, { 0.05, 0.1 } };
        RealMatrix covariances = estimator.getCovariances(problem);

        RealArray2DSample sample = new RealArray2DSample(2, 2, expected);
        assertEquals(sample.getData(), covariances.getData());
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGuessParametersErrors() throws Exception {
        EstimationProblem problem = new EstimationProblem() {

            /**
             * {@inheritDoc}
             */
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getParametersNumber() {
                return 2;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0, 0.5, 1.5),
                    new WeightedMeasurement(2.0, -1.0, 0.7, 0.5),
                    new WeightedMeasurement(3.0, 1.0, 1.3, -0.1)
                };
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public EstimatedParameter[] getParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1", 0.0), new EstimatedParameter("p2", 0.0) };
            }
        };

        AbstractEstimator estimator = new MyAbstractEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expected = new double[] { 0.1, 0.05 };

        for (int i = 0; i < errors.length; i++) {
            assertEquals(expected[i], errors[i], 0.01);
        }
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)} with
     * singular problem.
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetCovariancesSingularProblem() throws Exception {
        EstimationProblem problem = new EstimationProblem() {

            /**
             * {@inheritDoc}
             */
            @Override
            public int getMeasurementsNumber() {
                return 1;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public int getParametersNumber() {
                return 1;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public EstimatedParameter[] getParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1", 0.0) };
            }
        };

        AbstractEstimator estimator = new MyAbstractEstimator();
        assertThrows(EstimationException.class, () -> estimator.getCovariances(problem));
    }
}

class MyAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // Empty implementation
    }
}