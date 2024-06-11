import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointsToTest;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.functions.AckleyFunction;
import org.apache.commons.math3.optim.nonlinear.functions.RastriginFunction;
import org.apache.commons.math3.optim.nonlinear.functions.SphereFunction;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     */
    @Test
    public void testBrentOptimizerConstructorWithoutChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, InitialGuess, PointsToTest)}.
     */
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        // Test minimization of a simple function.
        UnivariateFunction function = new SphereFunction(10);
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(5), new PointsToTest(5));
        assertEquals(3.14159265359, result.getPoint(), 0.001);
        assertEquals(1.25274063438e-6, result.getValue(), 1e-8);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize(function, new InitialGuess(5), new PointsToTest(5));
        assertEquals(9.74877961265, result.getPoint(), 0.001);
        assertEquals(6.53313772071e-6, result.getValue(), 1e-8);

        // Test minimization of a function with multiple local minima.
        function = new RastriginFunction(5);
        result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(-5), new PointsToTest(5));
        assertEquals(Math.PI / 4, result.getPoint()[0], 0.01);
        assertEquals(-11.19615875, result.getValue(), 1e-4);

        // Test minimization of a function with a global minimum at a boundary.
        function = new AckleyFunction(-5, 5);
        result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(-5), new PointsToTest(5));
        assertEquals(-5, result.getPoint()[0], 0.01);
        assertEquals(-20.15617352, result.getValue(), 1e-4);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, InitialGuess)}.
     */
    @Test
    public void testOptimizeWithNoPointsToTest() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        // Test minimization of a simple function.
        UnivariateFunction function = new SphereFunction(10);
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(5));
        assertEquals(3.14159265359, result.getPoint(), 0.001);
        assertEquals(1.25274063438e-6, result.getValue(), 1e-8);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize(function, new InitialGuess(5));
        assertEquals(9.74877961265, result.getPoint(), 0.001);
        assertEquals(6.53313772071e-6, result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithoutInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new SphereFunction(10);
        optimizer.optimize(GoalType.MINIMIZE, function);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, PointsToTest)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNoInitialGuess() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new SphereFunction(10);
        optimizer.optimize(GoalType.MINIMIZE, function, new PointsToTest(5));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, InitialGuess, double, double)}.
     */
    @Test
    public void testOptimizeWithRelativeAndAbsoluteTolerances() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        // Test minimization of a simple function.
        UnivariateFunction function = new SphereFunction(10);
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(5), 1e-7, 1e-7);
        assertEquals(3.14159265359, result.getPoint(), 0.001);
        assertEquals(1.25274063438e-7, result.getValue(), 1e-9);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize(function, new InitialGuess(5), 1e-7, 1e-7);
        assertEquals(9.74877961265, result.getPoint(), 0.001);
        assertEquals(6.53313772071e-7, result.getValue(), 1e-9);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, InitialGuess, double)}.
     */
    @Test
    public void testOptimizeWithOnlyRelativeTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0);

        // Test minimization of a simple function.
        UnivariateFunction function = new SphereFunction(10);
        PointValuePair result = optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(5), 1e-7);
        assertEquals(3.14159265359, result.getPoint(), 0.001);
        assertEquals(1.25274063438e-6, result.getValue(), 1e-8);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        result = optimizer.optimize(function, new InitialGuess(5), 1e-7);
        assertEquals(9.74877961265, result.getPoint(), 0.001);
        assertEquals(6.53313772071e-6, result.getValue(), 1e-8);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(GoalType, UnivariateFunction, InitialGuess, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithOnlyAbsoluteTolerance() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-6);

        // Test minimization of a simple function.
        UnivariateFunction function = new SphereFunction(10);
        optimizer.optimize(GoalType.MINIMIZE, function, new InitialGuess(5), 1e-7);

        // Test maximization of a simple function.
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(function, new InitialGuess(5), 1e-7);
    }

}