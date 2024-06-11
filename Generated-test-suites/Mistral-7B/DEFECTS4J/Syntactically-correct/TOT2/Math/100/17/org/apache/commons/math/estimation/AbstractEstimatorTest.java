package org.apache.commons.math.estimation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator testee;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        testee = new MyAbstractEstimator();
        testee.setMaxCostEval(10);
        problem = new EstimationProblem() {

            @Override
            public RealVector getAllParameters() {
                throw new UnsupportedOperationException();
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] { 1.0, 2.0 });
            }

            @Override
            public RealVector getMeasurements() {
                return new ArrayRealVector(new double[] { 3.0, 4.0 });
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        testee.setMaxCostEval(10);
        assertEquals(10, testee.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        testee.estimate(problem);
        assertEquals(1, testee.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        testee.estimate(problem);
        assertEquals(1, testee.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        testee.initializeEstimate(problem);
        testee.updateJacobian();
        RealMatrix jacobian = testee.getJacobian();
        RealMatrix expectedJacobian = new DenseMatrix(new double[][] {
                {-Math.sqrt(problem.getMeasurements(0).getWeight()), -Math.sqrt(problem.getMeasurements(0).getWeight())},
                {-Math.sqrt(problem.getMeasurements(1).getWeight()), -Math.sqrt(problem.getMeasurements(1).getWeight())}
        });
        assertArrayEquals(expectedJacobian.getData(), jacobian.getData(), 1e-12);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        testee.initializeEstimate(problem);
        testee.updateResidualsAndCost();
        assertEquals(Math.sqrt(1.0), testee.getCost(), 1e-12);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() throws EstimationException {
        testee.setMaxCostEval(1);
        testee.initializeEstimate(problem);
        testee.updateResidualsAndCost();
        testee.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        testee.estimate(problem);
        assertEquals(Math.sqrt(0.005), testee.getRMS(problem), 1e-12);
    }

    @Test
    public void testGetChiSquare() {
        testee.estimate(problem);
        assertEquals(0.005, testee.getChiSquare(problem), 1e-12);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        testee.estimate(problem);
        double[][] expectedCovariances = new double[][] {
                {0.1, 0.0},
                {0.0, 0.1}
        };
        assertArrayEquals(expectedCovariances, testee.getCovariances(problem), 1e-12);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        testee.estimate(problem);
        double[] expectedErrors = new double[] { 0.05, 0.05 };
        assertArrayEquals(expectedErrors, testee.guessParametersErrors(problem), 1e-12);
    }

}

class MyAbstractEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // This method should be implemented by derived classes
    }

}