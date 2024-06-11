package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for MultiDirectional class.
 */
public class MultiDirectionalTest {

    private ObjectiveFunction testFunction;
    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        testFunction = new TestFunction();
        optimizer = new MultiDirectional(3.0, 1.5);
    }

    /**
     * Test the MultiDirectional optimizer with a simple function.
     *
     * @throws Exception if the optimization fails
     */
    @Test
    public void testSimpleFunction() throws Exception {
        final double min = 0.0;
        final double max = 1.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.5);

        optimizer.optimize(testFunction, new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
        final PointValuePair solution = optimizer.getSolution();
        final double expectedValue = Math.sqrt(2.0) / 2.0;
        assertEquals(expectedValue, solution.getValue(), 1e-6);
    }

    /**
     * Test the MultiDirectional optimizer with a function with multiple local minima.
     *
     * @throws Exception if the optimization fails
     */
    @Test
    public void testFunctionWithMultipleLocalMinima() throws Exception {
        final double min = -5.0;
        final double max = 5.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.0);

        optimizer.optimize(testFunction, new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
        final PointValuePair solution = optimizer.getSolution();
        final double expectedValue = -1.0;
        assertEquals(expectedValue, solution.getValue(), 1e-6);
    }

    /**
     * Test the MultiDirectional optimizer with a function with no bracketing solution.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = NoBracketingException.class)
    public void testFunctionWithNoBracketingSolution() throws Exception {
        final double min = -5.0;
        final double max = 5.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.0);

        optimizer.optimize(new TestFunctionNoBracketing(), new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
    }

    /**
     * Test the MultiDirectional optimizer with a function that cannot be evaluated.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionThatCannotBeEvaluated() throws Exception {
        final double min = 0.0;
        final double max = 1.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.5);

        optimizer.optimize(new TestFunctionCannotBeEvaluated(), new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
    }

    /**
     * Test the MultiDirectional optimizer with a function that exceeds the maximum number of evaluations.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testFunctionThatExceedsMaxEvaluations() throws Exception {
        final double min = 0.0;
        final double max = 1.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.5);

        optimizer.setMaxEvaluations(1);
        optimizer.optimize(testFunction, new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
    }

    /**
     * Test the MultiDirectional optimizer with a function that exceeds the maximum number of iterations.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = TooManyIterationsException.class)
    public void testFunctionThatExceedsMaxIterations() throws Exception {
        final double min = 0.0;
        final double max = 1.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.5);

        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
    }

    /**
     * Test the MultiDirectional optimizer with a null function.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = NullArgumentException.class)
    public void testNullFunction() throws Exception {
        final double min = 0.0;
        final double max = 1.0;
        final double[] bounds = {min, max};
        final InitialGuess initialGuess = new InitialGuess(0.5);

        optimizer.optimize(null, new SimpleBounds(bounds), GoalType.MINIMIZE, initialGuess);
    }

    /**
     * Test the MultiDirectional optimizer with a function with an unbounded domain.
     *
     * @throws Exception if the optimization fails
     */
    @Test(expected = UnboundedDomainException.class)
    public void testFunctionWithUnboundedDomain() throws Exception {
        optimizer.optimize(new TestFunctionWithUnboundedDomain(), new SimpleBounds(null, null), GoalType.MINIMIZE, null);
    }

}

class TestFunction implements ObjectiveFunction {

    @Override
    public double value(double[] point) {
        return Math.pow(point[0], 2) + Math.pow(point[1], 2) - 1.0;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}

class TestFunctionNoBracketing implements ObjectiveFunction {

    @Override
    public double value(double[] point) {
        if (point[0] < 0.0) {
            return -Math.pow(point[0], 2);
        } else {
            return Math.pow(point[0], 2);
        }
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

}

class TestFunctionCannotBeEvaluated implements ObjectiveFunction {

    @Override
    public double value(double[] point) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

}

class TestFunctionWithUnboundedDomain implements ObjectiveFunction {

    @Override
    public double value(double[] point) {
        if (point[0] > 0.0) {
            return Math.log(point[0]);
        } else {
            throw new UnboundedDomainException();
        }
    }

    @Override
    public int getNumberOfVariables() {
        return 1;
    }

}