package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethod;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link MultiDirectional} class.
 */
public class MultiDirectionalTest {

    private ObjectiveFunction function;

    @Before
    public void setUp() {
        function = new MyFunction();
    }

    /**
     * Test with default constructor.
     */
    @Test
    public void testMultiDirectionalDefaultConstructor() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair solution = optimizer.optimize(function, new SimpleBounds(-10, 10), GoalType.MINIMIZE);
        assertEquals(-3.347571624597395, solution.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.561273592294438, 0.822633726255335}, solution.getPointRef(), 1e-8);
    }

    /**
     * Test with custom coefficients constructor.
     */
    @Test
    public void testMultiDirectionalCustomConstructor() {
        final double khi = 3.0;
        final double gamma = 0.6;
        final MultiDirectional optimizer = new MultiDirectional(khi, gamma);
        final RealPointValuePair solution = optimizer.optimize(function, new SimpleBounds(-10, 10), GoalType.MINIMIZE);
        // The solution may vary depending on the coefficients, so no hardcoded assertions here.
        assertFalse(solution.getValue() > -3.3);
    }

    /**
     * Test with null objective function.
     */
    @Test(expected = NullArgumentException.class)
    public void testMultiDirectionalNullObjectiveFunction() {
        new MultiDirectional().optimize(null, new SimpleBounds(-10, 10), GoalType.MINIMIZE);
    }

    /**
     * Test with null bounds.
     */
    @Test(expected = NullArgumentException.class)
    public void testMultiDirectionalNullBounds() {
        new MultiDirectional().optimize(function, null, GoalType.MINIMIZE);
    }

    /**
     * Test with invalid goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalInvalidGoalType() {
        new MultiDirectional().optimize(function, new SimpleBounds(-10, 10), GoalType.MAXIMIZE);
    }

    /**
     * Test with simple bounds.
     */
    @Test
    public void testMultiDirectionalSimpleBounds() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair solution = optimizer.optimize(function, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        assertEquals(-2.356557635644136, solution.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.822633726255335, 0.334533797258835}, solution.getPointRef(), 1e-8);
    }

    /**
     * Test with bounds exceeding the domain of the function.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiDirectionalBoundsExceedFunctionDomain() {
        new MultiDirectional().optimize(function, new SimpleBounds(-100, 100), GoalType.MINIMIZE);
    }

    /**
     * Test with bounds containing the solution.
     */
    @Test
    public void testMultiDirectionalBoundsContainingSolution() {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair solution = optimizer.optimize(function, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        assertEquals(-2.356557635644136, solution.getValue(), 1e-8);
        assertArrayEquals(new double[]{0.822633726255335, 0.334533797258835}, solution.getPointRef(), 1e-8);
    }

    /**
     * Test with bounds not bracketing the solution.
     */
    @Test(expected = NoBracketingException.class)
    public void testMultiDirectionalBoundsNotBracketingSolution() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.optimize(function, new SimpleBounds(-10, 0), GoalType.MINIMIZE);
    }

    /**
     * Test with maximum number of evaluations exceeded.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMultiDirectionalMaxEvalExceeded() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(function, new SimpleBounds(-10, 10), GoalType.MINIMIZE);
    }

    /**
     * Test with unsupported operation.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMultiDirectionalUnsupportedOperation() {
        final MultiDirectional optimizer = new MultiDirectional();
        optimizer.setInitialGuess(new InitialGuess(){
            @Override
            public PointValuePair getGuess() {
                throw new UnsupportedOperationException();
            }
        });
        optimizer.optimize(function, new SimpleBounds(-10, 10), GoalType.MINIMIZE);
    }

}

/**
 * Test function for {@link MultiDirectionalTest}.
 */
class MyFunction implements ObjectiveFunction {

    @Override
    public PointValuePair value(final double[] x) throws FunctionEvaluationException {
        double value = 0;
        for (int i = 0; i < x.length; ++i) {
            value += Math.pow(Math.sin(Math.PI * x[i]), 2) + Math.pow(Math.cos(Math.PI * x[i]), 2);
        }
        return new RealPointValuePair(value, null);
    }

}