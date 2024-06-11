package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialFunction;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.leastsq.LevenbergMarquardtOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}
 */
public class AbstractLeastSquaresOptimizerTest {

    /** The optimizer to test. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** The test function and the target. */
    private SimpleVectorialFunction testFunction;
    private double[] target;

    /** The initial point. */
    private double[] initialPoint;

    /** The weights. */
    private double[] weights;

    /** The optimizer's checker. */
    private VectorialConvergenceChecker checker;

    /**
     * Set up the test before each test method.
     * @throws FunctionEvaluationException if the function cannot be initialized
     */
    @Before
    public void setUp() throws FunctionEvaluationException {
        optimizer = new LevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        testFunction = new SimpleVectorialFunction(new double[] { 0d, 1d }, x -> x[0] * x[0] + x[1] * x[1]);
        target = new double[] { 1d, 1d };
        initialPoint = new double[] { 2d, 2d };
        weights = new double[] { 1d, 1d };
        checker = optimizer.getConvergenceChecker();
    }

    /**
     * Test the optimizer.
     * @throws FunctionEvaluationException if the function evaluation fails
     * @throws OptimizationException if the optimization fails
     */
    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
        // Given
        optimizer.optimize(testFunction, target, weights, initialPoint);

        // Then
        PointValuePair optimalPointValuePair = optimizer.getPointValuePair();
        ArrayRealVector optimalPoint = (ArrayRealVector) optimalPointValuePair.getPoint();
        double optimalValue = optimalPointValuePair.getValue();
        double[] expectedOptimalPoint = new double[] { 1.000000213282277, 0.9999999264344113 };
        double expectedOptimalValue = 2.00000025;

        // Check the results
        assertEquals("Optimal point is not expected", expectedOptimalPoint, optimalPoint.toArray());
        assertEquals("Optimal value is not expected", expectedOptimalValue, optimalValue, 1e-6);
        assertTrue("Convergence not reached", checker.isConverged());
    }

    /**
     * Test the optimization when the maximum number of iterations is exceeded.
     * @throws FunctionEvaluationException if the function evaluation fails
     * @throws OptimizationException if the optimization fails
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, target, weights, initialPoint);
    }

    /**
     * Test the optimization when the maximum number of evaluations is exceeded.
     * @throws FunctionEvaluationException if the function evaluation fails
     * @throws OptimizationException if the optimization fails
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(testFunction, target, weights, initialPoint);
    }

    /**
     * Test the optimization when the convergence checker is not satisfied.
     * @throws FunctionEvaluationException if the function evaluation fails
     * @throws OptimizationException if the optimization fails
     */
    @Test
    public void testConvergenceCheckerNotSatisfied() throws FunctionEvaluationException, OptimizationException {
        optimizer.setConvergenceChecker(new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(double[] point, double[] objective, double[] gradient, double tolerance) {
                return false;
            }
        });
        optimizer.optimize(testFunction, target, weights, initialPoint);
    }
}