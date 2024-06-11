package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction f;
    private MultiStartUnivariateRealOptimizer optimizer;
    private double[] expectedOptima;
    private double[] expectedOptimaValues;

    @Before
    public void setUp() {
        f = new PolynomialFunction(2, new double[]{0.1, 1.2, -0.3, 0.5});
        optimizer = new MultiStartUnivariateRealOptimizer(new SimpleUnivariateRealOptimizer(), 5, new RandomGenerator());
    }

    /**
     * Test minimizing a polynomial function with 5 starts
     */
    @Test
    public void testMinimizePolynomialFunction5Starts() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.setMaximalIterationCount(100);
        optimizer.setMaxEvaluations(500);

        double[] actualOptima = optimizer.optimize(f, goalType, min, max);

        expectedOptima = new double[]{1.2546646229950234, 1.2546646229950234, 1.2546646229950234,
                1.2546646229950234, 1.2546646229950234, 1.2546646229950234};
        expectedOptimaValues = new double[]{-0.5826444734271642, -0.5826444734271642, -0.5826444734271642,
                -0.5826444734271642, -0.5826444734271642, -0.5826444734271642};

        assertArrayEquals(expectedOptima, actualOptima);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.000001);
        assertEquals(-0.5826444734271642, optimizer.getFunctionValue(), 0.000001);
    }

    /**
     * Test maximizing a polynomial function with 5 starts
     */
    @Test
    public void testMaximizePolynomialFunction5Starts() {
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.setMaximalIterationCount(100);
        optimizer.setMaxEvaluations(500);

        double[] actualOptima = optimizer.optimize(f, goalType, min, max);

        expectedOptima = new double[]{-1.2546646229950234, -1.2546646229950234, -1.2546646229950234,
                -1.2546646229950234, -1.2546646229950234, -1.2546646229950234};
        expectedOptimaValues = new double[]{3.1173555265728366, 3.1173555265728366, 3.1173555265728366,
                3.1173555265728366, 3.1173555265728366, 3.1173555265728366};

        assertArrayEquals(expectedOptima, actualOptima);
        assertArrayEquals(expectedOptimaValues, optimizer.getOptimaValues(), 0.000001);
        assertEquals(3.1173555265728366, optimizer.getFunctionValue(), 0.000001);
    }

    /**
     * Test minimizing a polynomial function with 1 start
     */
    @Test(expected = IllegalStateException.class)
    public void testMinimizePolynomialFunction1Starts() {
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.setMaximalIterationCount(100);
        optimizer.setMaxEvaluations(500);

        optimizer.setStarts(1);
        optimizer.optimize(f, goalType, min, max);
    }

    /**
     * Test maximizing a polynomial function with 1 start
     */
    @Test(expected = IllegalStateException.class)
    public void testMaximizePolynomialFunction1Starts() {
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;

        optimizer.setMaximalIterationCount(100);
        optimizer.setMaxEvaluations(500);

        optimizer.setStarts(1);
        optimizer.optimize(f, goalType, min, max);
    }
}

class SimpleUnivariateRealOptimizer implements UnivariateRealOptimizer {
    @Override
    public double optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
            throws ConvergenceException, FunctionEvaluationException {
        throw new FunctionEvaluationException("Not implemented");
    }

    @Override
    public double getFunctionValue() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public double getResult() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public double getAbsoluteAccuracy() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getIterationCount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getMaximalIterationCount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getMaxEvaluations() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int getEvaluations() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public double getRelativeAccuracy() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void resetAbsoluteAccuracy() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void resetMaximalIterationCount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void resetRelativeAccuracy() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setAbsoluteAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setMaximalIterationCount(int count) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void setRelativeAccuracy(double accuracy) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair minimize(UnivariateRealFunction function, double lowerBound, double upperBound, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair minimize(UnivariateRealFunction function, double lowerBound, double upperBound, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair minimize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair minimize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair[] minimize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, int maxEvals, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair[] minimize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, int maxEvals, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair maximize(UnivariateRealFunction function, double lowerBound, double upperBound, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair maximize(UnivariateRealFunction function, double lowerBound, double upperBound, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair maximize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair maximize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair[] maximize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, int maxEvals, MaxEval maxEval)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PointValuePair[] maximize(UnivariateRealFunction function, GoalType goal, double lowerBound, double upperBound, int maxEvals, MaxEval maxEval, double stepSize)
            throws FunctionEvaluationException, ConvergenceException {
        throw new UnsupportedOperationException("Not implemented");
    }
}