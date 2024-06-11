package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for the AbstractEstimator class.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)} and {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testCostEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(5);
        estimator.estimate(createTestEstimationProblem());
        assertEquals(5, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testJacobianEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.estimate(createTestEstimationProblem());
        assertEquals(2 * 3, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() {
        EstimationProblem problem = createTestEstimationProblem();
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(1.0, rms, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = createTestEstimationProblem();
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(7.0 / 2.0, chiSquare, 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testgetCovariances() {
        EstimationProblem problem = createTestEstimationProblem();
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0 / 3.0, 2.0 / 3.0},
                                                                               {2.0 / 3.0, 4.0 / 3.0}});
        assertArrayEquals(expectedCovariances.getData(), covariances.getData(), 1e-10);
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = createTestEstimationProblem();
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2.0, errors[0], 1e-10);
        assertEquals(2.0, errors[1], 1e-10);
    }

    /**
     * Test method for the initialization of the common parts of the estimation ({@link #initializeEstimate(EstimationProblem)}).
     */
    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = createTestEstimationProblem();
        TestAbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(problem);
        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertArrayEquals(new double[0], estimator.jacobian);
        assertArrayEquals(new double[0], estimator.residuals);
        assertNull(estimator.cost);
    }

    /**
     * Test method for the estimation method ({@link #estimate(EstimationProblem)}).
     */
    @Test
    public void testEstimate() {
        EstimationProblem problem = createTestEstimationProblem();
        TestAbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
        RealVector expectedParameters = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector actualParameters = problem.getAllParameters();
        assertEquals(expectedParameters, actualParameters);
    }

    /**
     * Test method for the handling of the maximal number of cost evaluations ({@link EstimationException}).
     */
    @Test
    public void testMaxCostEvalException() {
        EstimationProblem problem = new TestEstimationProblem(new double[][]{{1.0, 2.0},
                                                                            {3.0, 4.0},
                                                                            {5.0, 6.0}},
                                                                 new double[]{1.0, 1.0},
                                                                 3);
        TestAbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(1);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    /**
     * Test method for the handling of a singular problem ({@link EstimationException}).
     */
    @Test
    public void testSingularProblemException() {
        EstimationProblem problem = new TestEstimationProblem(new double[][]{{1.0, 2.0},
                                                                            {3.0, 4.0},
                                                                            {5.0, 6.0}},
                                                                 new double[]{1.0, 1.0, 1.0},
                                                                 3);
        TestAbstractEstimator estimator = new TestAbstractEstimator();
        assertThrows(EstimationException.class, () -> estimator.guessParametersErrors(problem));
    }

    /**
     * Create a test estimation problem.
     *
     * @return a test estimation problem
     */
    private EstimationProblem createTestEstimationProblem() {
        RealVector measurements = new ArrayRealVector(new double[]{1.0, 2.0, 3.0}, false);
        RealVector parameters = new ArrayRealVector(new double[]{1.0, 1.0}, false);
        return new TestEstimationProblem(measurements.toArray(), parameters.toArray(), 2);
    }

    /**
     * A test implementation of the {@link AbstractEstimator} interface.
     */
    private static class TestAbstractEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            initializeEstimate(problem);
            SimpleRegression regression = new SimpleRegression();
            regression.estimateRegressionParameters(problem.getMeasurements().toArray(new WeightedMeasurement[0]),
                                                   problem.getAllParameters().toArray(new EstimatedParameter[0]));
            parameters = problem.getAllParameters();
        }
    }

    /**
     * An estimation problem representation.
     */
    static class TestEstimationProblem implements EstimationProblem {

        /** The measurements. */
        private final double[][] measurements;

        /** The parameters. */
        private final double[] parameters;

        /** The number of measurements. */
        private final int n;

        /**
         * Constructor.
         *
         * @param measurements the measurements
         * @param parameters the parameters
         * @param rows the number of rows
         */
        public TestEstimationProblem(double[][] measurements, double[] parameters, int rows) {
            this.measurements = measurements;
            this.parameters = parameters;
            this.n = rows;
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[n];
            for (int i = 0; i < n; ++i) {
                measurements[i] = new WeightedMeasurement(measurements[i][0], measurements[i][1]);
            }
            return measurements;
        }

        @Override
        public EstimatedParameter[] getUnboundParameters() {
            EstimatedParameter[] parameters = new EstimatedParameter[parameters.length];
            for (int i = 0; i < parameters.length; ++i) {
                parameters[i] = new EstimatedParameter(parameters[i]);
            }
            return parameters;
        }

        @Override
        public int getMeasurementsSize() {
            return n;
        }

        @Override
        public int getAllParametersSize() {
            return parameters.length;
        }

        @Override
        public double[] getMeasurementsData() {
            return measurements;
        }

        @Override
        public double[] getAllParametersData() {
            return parameters;
        }

    }

}