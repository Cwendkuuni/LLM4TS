package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.Percentile;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private double[][] data = new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 3.0, 4.0},
            {3.0, 4.0, 5.0},
            {4.0, 5.0, 6.0}
    };
    private double[] expectedParameters = new double[] {1.0, 1.0};
    private double[] expectedResiduals = new double[] {-1.23456789, 0.89765432, -0.32456789, 1.45678901};
    private double[] expectedCovariances = new double[][] {
            {0.1, 0.05},
            {0.05, 0.1}
    };

    @Before
    public void setUp() {
        estimator = new TestEstimator();
    }

    @Test
    public void testEstimate() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(expectedParameters);
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(data);
            }

            @Override
            public double getWeight(int index) {
                return 1.0;
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealVector parameters = problem.getAllParameters();
        Assert.assertArrayEquals(expectedParameters, parameters.toArray(), 0.001);
        RealArray2D residuals = problem.getResiduals();
        Assert.assertArrayEquals(expectedResiduals, residuals.getData(), 0.001);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] {1.0});
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(new double[][] {{1.0}, {2.0}, {3.0}, {4.0}, {5.0}});
            }

            @Override
            public double getWeight(int index) {
                return 0.1;
            }
        };
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        double expectedRms = Math.sqrt(new Percentile(50.0, new RealVector(new double[] {0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9})).getPercentileValue() / 2.0);
        Assert.assertEquals(expectedRms, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 1;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] {1.0});
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(new double[][] {{1.0}, {2.0}, {3.0}, {4.0}, {5.0}});
            }

            @Override
            public double getWeight(int index) {
                return 1.0;
            }
        };
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 5.0;
        Assert.assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(expectedParameters);
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(data);
            }

            @Override
            public double getWeight(int index) {
                return 1.0;
            }
        };
        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        Assert.assertArrayEquals(expectedCovariances, covariances, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesSingularProblem() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] {1.0, 1.0});
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(new double[][] {{1.0}, {2.0}, {3.0}, {4.0}});
            }

            @Override
            public double getWeight(int index) {
                return 1.0;
            }
        };
        estimator.initializeEstimate(problem);
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public int getNumberOfParameters() {
                return 2;
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(expectedParameters);
            }

            @Override
            public RealVector getMeasurements() {
                return new Array2DRowRealVector(data);
            }

            @Override
            public double getWeight(int index) {
                return 1.0;
            }
        };
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        RealVector covariances = estimator.getCovariances(problem);
        for (int i = 0; i < errors.length; i++) {
            Assert.assertEquals(Math.sqrt(covariances.getEntry(i, i) * 5.0), errors[i], 0.001);
        }
    }

    private static class TestEstimator extends AbstractEstimator {
        @Override
        protected void estimate(EstimationProblem problem) throws EstimationException {
            // No implementation needed
        }
    }
}