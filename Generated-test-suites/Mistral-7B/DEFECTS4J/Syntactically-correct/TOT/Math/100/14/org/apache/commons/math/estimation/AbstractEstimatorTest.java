package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSolution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new DummyEstimationProblem(1, 1);
    }

    @Test
    public void testEstimateWithValidData() throws EstimationException {
        estimator.estimate(problem);
        RealVector parameters = problem.getAllParameters();
        RealVector expectedParameters = new Array2DRowRealMatrix(new double[][]{{1.0}}).getColumnVector(0);
        assertEquals(expectedParameters, parameters);
        assertEquals(0.0, estimator.getRMS(problem), 1e-6);
        assertEquals(0.0, estimator.getChiSquare(problem), 1e-6);
    }

    @Test
    public void testEstimateWithInvalidData() {
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(2.0, 0.0), new WeightedMeasurement(1.0, 1.0)});
        try {
            estimator.estimate(problem);
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }
    }

    @Test
    public void testGetCovariancesWithValidData() throws EstimationException {
        estimator.estimate(problem);
        RealMatrix covariances = estimator.getCovariances(problem);
        RealVector expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0}}).getColumnVector(0);
        assertEquals(expectedCovariances, covariances.getColumnVector(0));
    }

    @Test
    public void testGuessParametersErrorsWithValidData() throws EstimationException {
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(1.0, errors[0], 1e-6);
    }

    @Test
    public void testRMSAndChiSquareWithValidData() {
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0), new WeightedMeasurement(2.0, 4.0)});
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(2.5, rms, 1e-6);
        assertEquals(4.5, chiSquare, 1e-6);
    }

    @Test
    public void testRMSAndChiSquareWithInvalidData() {
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(1.0, 1.0), new WeightedMeasurement(2.0, 2.0)});
        estimator.estimate(problem);
        try {
            estimator.getRMS(problem);
            estimator.getChiSquare(problem);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(5);
        assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.estimate(problem);
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobianWithValidData() {
        estimator.estimate(problem);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(1.0, 1.0);
        regression.addData(2.0, 2.0);
        RealMatrix expectedJacobian = regression.getRegressionMatrix().transpose();
        RealMatrix jacobian = new Array2DRowRealMatrix(estimator.jacobian);
        assertEquals(expectedJacobian, jacobian);
    }

    @Test
    public void testUpdateJacobianWithInvalidData() {
        estimator.estimate(problem);
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(2.0, 0.0), new WeightedMeasurement(1.0, 1.0)});
        estimator.updateJacobian();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testUpdateResidualsAndCostWithValidData() throws EstimationException {
        estimator.estimate(problem);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(1.0, 1.0);
        regression.addData(2.0, 2.0);
        estimator.updateResidualsAndCost();
        assertEquals(regression.getResiduals().getEntry(0), estimator.residuals[0], 1e-6);
        assertEquals(regression.getResiduals().getEntry(1), estimator.residuals[1], 1e-6);
        assertEquals(regression.getSSE(), estimator.cost, 1e-6);
    }

    @Test
    public void testUpdateResidualsAndCostWithInvalidData() {
        estimator.estimate(problem);
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(2.0, 0.0), new WeightedMeasurement(1.0, 1.0)});
        try {
            estimator.updateResidualsAndCost();
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }
    }

    @Test
    public void testInitializeEstimateWithValidData() {
        estimator.estimate(problem);
        estimator.initializeEstimate(problem);
        assertArrayEquals(estimator.measurements, problem.getMeasurements());
        assertArrayEquals(estimator.parameters, problem.getUnboundParameters());
        assertEquals(estimator.rows, problem.getMeasurements().length);
        assertEquals(estimator.cols, problem.getUnboundParameters().length);
        assertArrayEquals(estimator.jacobian, new double[estimator.rows * estimator.cols]);
        assertArrayEquals(estimator.residuals, new double[estimator.rows]);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test
    public void testInitializeEstimateWithInvalidData() {
        problem.setMeasurements(new WeightedMeasurement[]{new WeightedMeasurement(2.0, 0.0), new WeightedMeasurement(1.0, 1.0)});
        try {
            estimator.initializeEstimate(problem);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}