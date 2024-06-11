package org.apache.commons.math.estimation.tests;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.LevenbergMarquardtEstimator;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.Random;

/**
 * Test cases for AbstractEstimator class
 */
public class AbstractEstimatorTest extends TestCase {

    private static final Random RAND = new Random();

    private static final double EPSILON = 1e-6;

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new LevenbergMarquardtEstimator();
        problem = new EstimationProblem() {
            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1", 0), new EstimatedParameter("p2", 0)};
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{new WeightedMeasurement(1, 2, 3, 1), new WeightedMeasurement(4, 5, 6, 2)};
            }
        };

        estimator.initializeEstimate(problem);
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        int maxCostEval = 10;
        estimator.setMaxCostEval(maxCostEval);
        assertEquals(maxCostEval, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        int costEvaluations = estimator.getCostEvaluations();
        assertEquals(0, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals(0, jacobianEvaluations);
    }

    @Test
    public void testUpdateJacobianAndResiduals() {
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        double[][] jacobian = estimator.getJacobian();
        double[] residuals = estimator.getResiduals();

        double[][] expectedJacobian = new double[][]{
                {-3, -3},
                {-5, -5}
        };

        double[] expectedResiduals = new double[]{3, 3};

        assertArrayEquals(expectedJacobian, jacobian);
        assertArrayEquals(expectedResiduals, residuals);

        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());

        Arrays.fill(jacobian, 0);
        Arrays.fill(residuals, 0);
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(Math.sqrt(chiSquare / 2), rms, EPSILON);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(19.0, chiSquare, EPSILON);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2.0 * Math.sqrt(2.0 / 2.0), errors[0], EPSILON);
        assertEquals(2.0 * Math.sqrt(2.0 / 2.0), errors[1], EPSILON);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(problem);

        RealVector parameters = problem.getAllParametersVector();
        RealVector expectedParameters = new ArrayRealVector(new double[]{1.0, 1.0});

        RealVector actualParameters = estimator.getParameters();
        assertEquals(expectedParameters, actualParameters);

        RealMatrix jacobian = estimator.getJacobian();
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(
                new double[][]{
                        {-3, -3},
                        {-5, -5}
                });

        assertEquals(expectedJacobian, jacobian);

        double[] residuals = estimator.getResiduals();
        double[] expectedResiduals = new double[]{0, 0};

        assertArrayEquals(expectedResiduals, residuals);

        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(1.5 * Math.sqrt(2.0 / 2.0), errors[0], EPSILON);
        assertEquals(1.5 * Math.sqrt(2.0 / 2.0), errors[1], EPSILON);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateException() throws EstimationException {
        problem.setMaxCostEval(1);
        estimator.estimate(problem);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        estimator.initializeEstimate(problem);

        double[][] jacobian = estimator.getJacobian();
        double[] residuals = estimator.getResiduals();
        int costEvaluations = estimator.getCostEvaluations();
        int jacobianEvaluations = estimator.getJacobianEvaluations();

        assertEquals(0, costEvaluations);
        assertEquals(0, jacobianEvaluations);
        assertArrayEquals(new double[0][], jacobian);
        assertArrayEquals(new double[0], residuals);

        Arrays.fill(jacobian, 0);
        Arrays.fill(residuals, 0);
    }

    @Test
    public void testEstimateWithCustomErrorFunction() throws EstimationException {
        EstimationProblem customProblem = new EstimationProblem() {
            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1", 0), new EstimatedParameter("p2", 0)};
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{new WeightedMeasurement(1, 2, 3, 1), new WeightedMeasurement(4, 5, 6, 2)};
            }

            @Override
            public double getErrorFunctionValue(RealVector parameters) {
                return parameters.getEntry(0) * parameters.getEntry(0) + parameters.getEntry(1) * parameters.getEntry(1) - 1;
            }
        };

        estimator.initializeEstimate(customProblem);
        estimator.estimate(customProblem);

        RealVector parameters = customProblem.getAllParametersVector();
        RealVector expectedParameters = new ArrayRealVector(new double[]{1.0, -1.0});

        RealVector actualParameters = estimator.getParameters();
        assertEquals(expectedParameters, actualParameters);

        RealMatrix jacobian = estimator.getJacobian();
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(
                new double[][]{
                        {2, 0},
                        {0, 2}
                });

        assertEquals(expectedJacobian, jacobian);

        double[] residuals = estimator.getResiduals();
        double[] expectedResiduals = new double[]{0, 0};

        assertArrayEquals(expectedResiduals, residuals);
    }
}