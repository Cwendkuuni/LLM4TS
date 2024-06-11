package org.apache.commons.math.estimation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new DummyEstimationProblem();
    }

    @Test
    public void testGetRMS() {
        double expectedRMS = 1.0;
        Assert.assertEquals(expectedRMS, estimator.getRMS(problem), 1e-8);
    }

    @Test
    public void testGetChiSquare() {
        double expectedChiSquare = 2.0;
        Assert.assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 1e-8);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = { {1.0, 0.5}, {0.5, 1.0} };
        try {
            RealMatrix covariances = estimator.getCovariances(problem);
            RealMatrix expectedCovariancesMatrix = new Array2DRowRealMatrix(expectedCovariances);
            Assert.assertArrayEquals(expectedCovariances, covariances.getData(), 1e-8);
        } catch (EstimationException e) {
            // Should not reach here
            Assert.fail();
        }
    }

    @Test
    public void testGuessParametersErrors() {
        double expectedError = 2.0;
        try {
            double[] errors = estimator.guessParametersErrors(problem);
            Assert.assertEquals(expectedError, new StandardDeviation().evaluate(errors), 1e-8);
        } catch (EstimationException e) {
            // Should not reach here
            Assert.fail();
        }
    }

    @Test(expected = EstimationException.class)
    public void testMaxCostEvalReached() {
        AbstractEstimator estimatorWithMaxCostEval = new DummyEstimatorWithMaxCostEval(1);
        problem.setMeasurements(new WeightedMeasurement[1000]);
        estimatorWithMaxCostEval.estimate(problem);
    }

    @Test(expected = EstimationException.class)
    public void testSingularProblem() {
        problem.setMeasurements(new WeightedMeasurement[1]);
        try {
            estimator.getCovariances(problem);
            Assert.fail();
        } catch (EstimationException e) {
            // Expected
        }
    }

}

class DummyEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // Empty implementation
    }

}

class DummyEstimationProblem implements EstimationProblem {

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[5];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[2];
    }

    @Override
    public RealVector getParameters() {
        return null;
    }

    @Override
    public RealVector getResiduals() {
        return null;
    }

}