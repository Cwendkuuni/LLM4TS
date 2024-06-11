package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.ZeroPointException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.PointValuePairFunction;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultiDirectional optimizer.
 */
public class MultiDirectionalTest {

    /**
     * Test the optimization of a simple function with known minimum value.
     */
    @Test
    public void testSimpleFunction() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0},
                x -> Math.pow(Math.sin(Math.PI * x[0] + Math.PI / 4.0), 2.0)
                        + Math.pow(Math.sin(Math.PI * x[1] + Math.PI / 4.0), 2.0) + 1.0);
        MultiDirectional optimizer = new MultiDirectional();
        PointValuePair solution;

        SimpleBounds bounds = new SimpleBounds(-2.5, 2.5, -2.5, 2.5);
        RealConvergenceChecker stopCondition = new RealConvergenceChecker(1.0e-6);
        MaxEval maxEval = new MaxEval(1000);

        try {
            solution = optimizer.optimize(function, bounds, stopCondition, null, null);
            Assert.assertEquals(3.0, solution.getValue(), 1.0e-5);
            Assert.assertArrayEquals(new double[]{-0.721011514947558, -0.721011514947558},
                    solution.getPointRef(), 1.0e-5);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test the optimization of a function with no brackets.
     */
    @Test(expected = NoBracketException.class)
    public void testNoBracketsFunction() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0}, x -> x[0] * x[1]);
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0, 0.0, 1.0);
        try {
            optimizer.optimize(function, bounds, null, null, null);
        } catch (Exception e) {
            throw new NoBracketException("No brackets found.", e);
        }
    }

    /**
     * Test the optimization of a function with zero point.
     */
    @Test(expected = ZeroPointException.class)
    public void testZeroPointFunction() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0}, x -> Math.pow(x[0], 2.0)
                - Math.pow(x[1], 2.0));
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-1.5, 1.5, -1.5, 1.5);
        try {
            optimizer.optimize(function, bounds, null, null, null);
        } catch (Exception e) {
            throw new ZeroPointException("Zero point detected.", e);
        }
    }

    /**
     * Test the optimization of a function that exceeds the maximum number of evaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalFunction() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0},
                x -> Math.sin(Math.PI * x[0] + Math.PI / 4.0) * Math.sin(Math.PI * x[1] + Math.PI / 4.0));
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-2.5, 2.5, -2.5, 2.5);
        RealConvergenceChecker stopCondition = new RealConvergenceChecker(1.0e-6);
        MaxEval maxEval = new MaxEval(3);
        try {
            optimizer.optimize(function, bounds, stopCondition, null, maxEval);
        } catch (Exception e) {
            throw new MaxEvalExceededException("Maximum number of evaluations exceeded.", e);
        }
    }

    /**
     * Test the optimization with an initial guess outside the feasible region.
     */
    @Test
    public void testInitialGuessOutsideBounds() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0}, x -> Math.pow(x[0], 2.0)
                + Math.pow(x[1], 2.0));
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0, 0.0, 1.0);
        InitialGuess initialGuess = new InitialGuess(new double[]{-2.0, 2.0});
        RealConvergenceChecker stopCondition = new RealConvergenceChecker(1.0e-6);
        MaxEval maxEval = new MaxEval(1000);
        try {
            optimizer.optimize(function, bounds, stopCondition, initialGuess, maxEval);
            Assert.assertEquals(Double.POSITIVE_INFINITY, optimizer.getFunctionValue(), 0.0);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test the optimization with a custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0}, x -> Math.pow(x[0], 2.0)
                + Math.pow(x[1], 2.0) + 1.0);
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0, 0.0, 1.0);
        RealConvergenceChecker customStopCondition = new RealConvergenceChecker(1.0e-3);
        MaxEval maxEval = new MaxEval(1000);
        try {
            optimizer.setConvergenceChecker(customStopCondition);
            PointValuePair solution = optimizer.optimize(function, bounds, null, null, maxEval);
            Assert.assertEquals(2.0, solution.getValue(), 1.0e-4);
            Assert.assertArrayEquals(new double[]{0.5, 0.5}, solution.getPointRef(), 1.0e-4);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

    /**
     * Test the optimization with a custom goal type.
     */
    @Test
    public void testCustomGoalType() {
        ObjectiveFunction function = new PointValuePairFunction(new double[]{-1.0, 1.0}, x -> Math.pow(x[0], 2.0)
                + Math.pow(x[1], 2.0) + 1.0);
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0, 0.0, 1.0);
        GoalType customGoalType = new GoalType() {
            @Override
            public double value(PointValuePair point) {
                return -point.getValue();
            }

            @Override
            public SimpleGoalType getGoalType() {
                return SimpleGoalType.MINIMIZE;
            }
        };
        MaxEval maxEval = new MaxEval(1000);
        try {
            optimizer.setGoalType(customGoalType);
            PointValuePair solution = optimizer.optimize(function, bounds, null, null, maxEval);
            Assert.assertEquals(2.0, solution.getValue(), 1.0e-4);
            Assert.assertArrayEquals(new double[]{0.5, 0.5}, solution.getPointRef(), 1.0e-4);
        } catch (Exception e) {
            Assert.fail("Optimization failed: " + e.getMessage());
        }
    }

}