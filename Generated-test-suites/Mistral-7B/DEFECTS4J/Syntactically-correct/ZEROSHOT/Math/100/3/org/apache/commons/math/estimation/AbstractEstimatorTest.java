package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimatedParameter;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimationProblem;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            // empty implementation
        };
        problem = new EstimationProblem() {
            // empty implementation
        };
    }

    @Test
    public void testGetRMS() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        problem.setMeasurements(new WeightedMeasurement[3]);
        for (int i = 0; i < 3; i++) {
            problem.getMeasurements()[i] = new WeightedMeasurement(new Complex(x[i], 0),
                                                                  new Complex(y[i], 0),
                                                                  1.0);
        }
        problem.setAllParameters(new EstimatedParameter[3]);
        estimator.initializeEstimate(problem);

        AbstractEstimator estimatorSpy = new SpyEstimator(estimator);
        estimator.estimate(problem);
        double rms = estimatorSpy.getRMS(problem);

        double[] expectedRms = new double[] {0.27546512262863475, 0.27546512262863475, 0.27546512262863475};
        DescriptiveStatistics stats = new DescriptiveStatistics(expectedRms);
        assertEquals(stats.getMean(), rms, 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        problem.setMeasurements(new WeightedMeasurement[3]);
        for (int i = 0; i < 3; i++) {
            problem.getMeasurements()[i] = new WeightedMeasurement(new Complex(x[i], 0),
                                                                  new Complex(y[i], 0),
                                                                  1.0);
        }
        problem.setAllParameters(new EstimatedParameter[3]);
        estimator.initializeEstimate(problem);

        AbstractEstimator estimatorSpy = new SpyEstimator(estimator);
        estimator.estimate(problem);
        double chiSquare = estimatorSpy.getChiSquare(problem);

        double[] expectedChiSquare = new double[] {1.35, 1.35, 1.35};
        SimpleRegression regression = new SimpleRegression();
        regression.computeRegressionParameters(x, y);
        double[] expectedCoefficients = regression.getParameterEstimates();
        double[] expectedResiduals = regression.residuals();
        double expectedChiSquareValue = regression.rSquared();
        RealMatrix xMatrix = new Array2DRowRealMatrix(x);
        RealMatrix xTranspose = xMatrix.transpose();
        RealVector yVector = new Array2DRowRealMatrix(expectedResiduals).columnVector(0);
        RealMatrix X = xMatrix.add(xTranspose.multiply(regression.getParameterEstimates()));
        RealMatrix XInv = X.inverse();
        double[] yMean = new double[] {regression.getMean()};
        double[] errors = XInv.operate(yVector).toArray();
        double[] expectedErrors = new double[] {Math.sqrt(regression.getBetaSE()[0]),
                                               Math.sqrt(regression.getBetaSE()[1]),
                                               Math.sqrt(regression.getBetaSE()[2])};
        assertArrayEquals(expectedErrors, errors);
        double expectedChiSquareSum = 0;
        for (int i = 0; i < 3; i++) {
            expectedChiSquareSum += Math.pow(errors[i] * yMean[0], 2);
        }
        assertEquals(expectedChiSquareSum, chiSquare, 1e-6);
    }

    @Test
    public void testGetCovariances() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        problem.setMeasurements(new WeightedMeasurement[3]);
        for (int i = 0; i < 3; i++) {
            problem.getMeasurements()[i] = new WeightedMeasurement(new Complex(x[i], 0),
                                                                  new Complex(y[i], 0),
                                                                  1.0);
        }
        problem.setAllParameters(new EstimatedParameter[3]);
        estimator.initializeEstimate(problem);

        AbstractEstimator estimatorSpy = new SpyEstimator(estimator);
        estimator.estimate(problem);
        double[][] covariances = estimatorSpy.getCovariances(problem);

        double[][] expectedCovariances = new double[][] {
                {0.00191471966543179, 0.0, 0.0},
                {0.0, 0.00224332172244923, 0.0},
                {0.0, 0.0, 0.00332462612717928}
        };
        assertArrayEquals(expectedCovariances, covariances, 1e-6);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 4.0, 6.0};
        problem.setMeasurements(new WeightedMeasurement[3]);
        for (int i = 0; i < 3; i++) {
            problem.getMeasurements()[i] = new WeightedMeasurement(new Complex(x[i], 0),
                                                                  new Complex(y[i], 0),
                                                                  1.0);
        }
        problem.setAllParameters(new EstimatedParameter[3]);
        estimator.initializeEstimate(problem);

        AbstractEstimator estimatorSpy = new SpyEstimator(estimator);
        estimator.estimate(problem);
        double[] errors = estimatorSpy.guessParametersErrors(problem);

        double[] expectedErrors = new double[] {0.012959067866180615, 0.015811388300841825, 0.02179646663425952};
        assertArrayEquals(expectedErrors, errors, 1e-6);
    }

    private static class SpyEstimator extends AbstractEstimator {

        private AbstractEstimator estimator;

        public SpyEstimator(AbstractEstimator estimator) {
            this.estimator = estimator;
        }

        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            estimator.estimate(problem);
        }

        public double getRMS(EstimationProblem problem) {
            return estimator.cost;
        }

        public double getChiSquare(EstimationProblem problem) {
            return estimator.cost;
        }

        public double[][] getCovariances(EstimationProblem problem)
          throws EstimationException {
            return estimator.getCovariances(problem);
        }

    }

}