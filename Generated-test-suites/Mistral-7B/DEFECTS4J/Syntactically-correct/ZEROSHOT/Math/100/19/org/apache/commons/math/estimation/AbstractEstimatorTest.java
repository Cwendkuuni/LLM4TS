package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.WeightedMeasurement;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no implementation needed
            }
        };
        problem = new EstimationProblem() {
            @Override
            public int getMeasurementDimension() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, new RealVector(new double[] {1.0, 2.0})),
                    new WeightedMeasurement(0.5, new RealVector(new double[] {3.0, 4.0}))
                };
            }

            @Override
            public RealVector getAllParameters() {
                return new RealVector(new double[] {0.0, 0.0});
            }

            @Override
            public RealVector getUnboundParameters() {
                return new RealVector(new double[] {0.0, 0.0});
            }

            @Override
            public double getMaximumLikelihoodValue() {
                return 0.0;
            }

            @Override
            public double getMaximumLikelihoodValueGradientNorm() {
                return 0.0;
            }

            @Override
            public RealMatrix getJacobian(RealVector parameters) {
                return new RealMatrixImpl(new double[][] {
                    {1.0, 1.0},
                    {2.0, 2.0}
                });
            }

            @Override
            public double[] getResiduals(RealVector parameters) {
                double[] residuals = new double[getMeasurementDimension()];
                for (int i = 0; i < getMeasurementDimension(); ++i) {
                    residuals[i] = getMeasurement(i).getResidual(parameters);
                }
                return residuals;
            }
        };
    }

    @Test
    public void testGetRMS() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt((2.0 * 0.5 * 0.5 + 2.0 * 0.5 * 0.5) / 2.0), rms, 1e-9);
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(2.0 * 0.5 * 0.5 + 2.0 * 0.5 * 0.5, chiSquare, 1e-9);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesException() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(Math.sqrt(2.0 * 0.5), errors[0], 1e-9);
        assertEquals(Math.sqrt(2.0 * 0.5), errors[1], 1e-9);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsSingular() throws EstimationException {
        problem = new EstimationProblem() {
            // Singular problem (number of measurements equals number of parameters)
            @Override
            public int getMeasurementDimension() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, new RealVector(new double[] {1.0, 1.0})),
                    new WeightedMeasurement(1.0, new RealVector(new double[] {1.0, 1.0}))
                };
            }

            // Other methods are the same as in the previous test
        };
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no implementation needed
            }
        };
        estimator.initializeEstimate(problem);
        estimator.getCovariances(problem);
    }
}