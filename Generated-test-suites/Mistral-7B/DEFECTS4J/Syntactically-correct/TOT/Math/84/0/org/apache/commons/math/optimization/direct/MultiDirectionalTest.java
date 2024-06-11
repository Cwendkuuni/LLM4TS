package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnboundedException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction testFunction;
    private double[] lowerBounds;
    private double[] upperBounds;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
        optimizer = new MultiDirectional(2.0, 0.5);
        lowerBounds = new double[]{-5.0, -5.0};
        upperBounds = new double[]{5.0, 5.0};
    }

    @Test
    public void testSimpleCase() throws Exception {
        SimpleBounds bounds = new SimpleBounds(lowerBounds, upperBounds);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(testFunction, bounds, goalType, 1000, new RealConvergenceChecker(1.0e-6));
        assertEquals(-3.0, result.getPoint().length, 0.0);
        assertEquals(-3.0, result.getValue(), 0.0);
    }

    @Test(expected = UnboundedException.class)
    public void testUnboundedFunction() throws Exception {
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.optimize(new UnboundedTestFunction(), bounds, goalType, 1000, new RealConvergenceChecker(1.0e-6));
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketingFunction() throws Exception {
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.optimize(new NoBracketingTestFunction(), bounds, goalType, 1000, new RealConvergenceChecker(1.0e-6));
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceededFunction() throws Exception {
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.optimize(new MaxEvalExceededTestFunction(), bounds, goalType, 1, new RealConvergenceChecker(1.0e-6));
    }

    @Test
    public void testErrorHandling() throws Exception {
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});
        GoalType goalType = GoalType.MINIMIZE;
        try {
            optimizer.optimize(null, bounds, goalType, 1000, new RealConvergenceChecker(1.0e-6));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}

class TestFunction implements ObjectiveFunction {
    @Override
    public double value(double[] x) {
        return Math.pow(Math.sin(x[0]), 2) + Math.pow(Math.sin(x[1]), 2);
    }
}

class UnboundedTestFunction implements ObjectiveFunction {
    @Override
    public double value(double[] x) {
        return Math.sin(x[0]) / x[0];
    }
}

class NoBracketingTestFunction implements ObjectiveFunction {
    @Override
    public double value(double[] x) {
        return Math.sin(Math.PI - x[0]);
    }
}

class MaxEvalExceededTestFunction implements ObjectiveFunction {
    @Override
    public double value(double[] x) {
        throw new MaxEvalExceededException("Maximum number of evaluations exceeded.");
    }
}