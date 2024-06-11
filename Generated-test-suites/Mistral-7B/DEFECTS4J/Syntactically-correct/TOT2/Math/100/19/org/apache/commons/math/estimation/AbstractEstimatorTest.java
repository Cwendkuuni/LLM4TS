package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /** Test data for {@link AbstractEstimator#getRMS(EstimationProblem)} method. */
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(10.0 / 3.0), rms, 1e-12);
    }

    /** Test data for {@link AbstractEstimator#getChiSquare(EstimationProblem)} method. */
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(10.0 / 3.0, chiSquare, 1e-12);
    }

    /** Test data for {@link AbstractEstimator#getCovariances(EstimationProblem)} method. */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{
            {1.0 / 3.0, 0.0},
            {0.0, 1.0 / 3.0}
        });
        LUDecomposition lu = new LUDecomposition(expectedCovariances);
        RealMatrix inverse = lu.getSolver().getSolution(new ArrayRealVector(new double[]{1.0, 1.0}));
        assertArrayEquals(Arrays.stream(inverse.getData()).mapToDouble(d -> d[0]).toArray(), covariances[0]);
        assertArrayEquals(Arrays.stream(inverse.getData()).mapToDouble(d -> d[1]).toArray(), covariances[1]);
    }

    /** Test data for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)} method. */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new DummyEstimator();
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[]{1.0 / Math.sqrt(10.0 / 3.0), 1.0 / Math.sqrt(10.0 / 3.0)}, errors);
    }

    /** Test data for {@link AbstractEstimator#estimate(EstimationProblem)} method with maximal cost evaluations limit. */
    @Test(expected = EstimationException.class)
    public void testEstimateMaxCostEvaluationsLimit() throws EstimationException {
        AbstractEstimator estimator = new DummyEstimator();
        estimator.setMaxCostEval(2);
        EstimationProblem problem = new DummyProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    /** Test data for {@link AbstractEstimator#setMaxCostEval(int)} method with negative value. */
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCostEvalNegativeValue() {
        AbstractEstimator estimator = new DummyEstimator();
        estimator.setMaxCostEval(-1);
    }

}

/**
 * Dummy estimator for testing purposes.
 */
class DummyEstimator extends AbstractEstimator {

    /** Constructor. */
    DummyEstimator() {
        super();
    }

    @Override
    protected void initializeEstimate(EstimationProblem problem) {
        super.initializeEstimate(problem);
    }

    @Override
    protected void updateJacobian() {
        // empty implementation
    }

    @Override
    protected void updateResidualsAndCost() throws EstimationException {
        // empty implementation
    }

    @Override
    public void estimate(EstimationProblem problem)
      throws EstimationException {
        // empty implementation
    }

}

/**
 * Dummy problem for testing purposes.
 */
class DummyProblem implements EstimationProblem {

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[]{
            new WeightedMeasurement(new double[]{1.0, 1.0}, 2.0),
            new WeightedMeasurement(new double[]{2.0, -1.0}, 3.0),
            new WeightedMeasurement(new double[]{-1.0, 2.0}, 4.0)
        };
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[]{new EstimatedParameter("p1"), new EstimatedParameter("p2")};
    }

    @Override
    public EstimationProblem clone() {
        return this;
    }

}