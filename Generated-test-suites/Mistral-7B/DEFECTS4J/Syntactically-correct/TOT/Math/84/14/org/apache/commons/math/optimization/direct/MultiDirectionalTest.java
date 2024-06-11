package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealFunction function;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional(2.0, 0.5);
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2.0);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
    }

    @Test
    public void testSimpleMinimum() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        double solution = optimizer.optimize(function, bounds);
        assertEquals(0.0, solution, 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        FibonacciSearch fibonacciSearch = new FibonacciSearch(function, bounds);
        optimizer.optimize(function, bounds, fibonacciSearch);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        try {
            optimizer.optimize(null, new SimpleBounds(-1.0, 1.0));
        } catch (Exception e) {
            throw new NullArgumentException(e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(function, null);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, bounds);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNumberIsTooSmallException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.optimize(function, bounds, new NelderMeadSimplex(bounds, 2));
    }

    @Test(expected = TooManyIterationsException.class)
    public void testTooManyIterationsException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, bounds);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, bounds);
    }

    @Test
    public void testNoSolution() throws Exception {
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        try {
            optimizer.optimize(new ObjectiveFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - 1.0;
                }

                @Override
                public GoalType getGoalType() {
                    return GoalType.MINIMIZE;
                }
            }, bounds);
            fail("Expected NoSolutionException");
        } catch (NoSolutionException e) {
            // expected
        }
    }

}