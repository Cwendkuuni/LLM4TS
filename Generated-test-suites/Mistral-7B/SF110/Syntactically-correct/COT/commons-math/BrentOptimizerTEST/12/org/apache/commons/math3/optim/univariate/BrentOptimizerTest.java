import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstructorWithRelAndAbs() {
        GoalType goalType = GoalType.MINIMIZE;
        double rel = 1.0e-6;
        double abs = 1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerConstructorWithRelAndNullChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        double rel = 1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, null);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerConstructorWithRelAbsAndChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        double rel = 1.0e-6;
        double abs = 1.0e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        assertEquals(goalType, optimizer.getGoalType());
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerConstructorWithRelBelowMinRelTolerance() {
        GoalType goalType = GoalType.MINIMIZE;
        double rel = 1.0e-8; // less than minimum relative tolerance
        double abs = 1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerConstructorWithNegativeAbs() {
        GoalType goalType = GoalType.MINIMIZE;
        double rel = 1.0e-6;
        double abs = -1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);
    }

    @Test
    public void testDoOptimizeWithSimpleFunction() {
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateFunction function = new SimplePolynomialFunction(1, -1, 1); // f(x) = x - 1
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(0.0, result.getX(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithFunctionReachingMax() {
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateFunction function = new SimplePolynomialFunction(1, 1, 1); // f(x) = x
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0);
        assertEquals(1.0, result.getX(), 0.0);
        assertEquals(1.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithFunctionReachingMin() {
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateFunction function = new SimplePolynomialFunction(1, 1, 4); // f(x) = x^2 - 4
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        assertEquals(-2.0, result.getX(), 0.0);
        assertEquals(4.0, result.getValue(), 0.0);
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        UnivariateFunction function = new SimplePolynomialFunction(1, 1, 4); // f(x) = x^2 - 4
        ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0);
        verify(checker).converged(eq(1), any(UnivariatePointValuePair.class), any(UnivariatePointValuePair.class));
        assertEquals(-2.0, result.getX(), 0.0);
        assertEquals(4.0, result.getValue(), 0.0);
    }

    @Test
    public void testBestMethod() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 4.0);
        UnivariatePointValuePair c = new UnivariatePointValuePair(3.0, 9.0);
        UnivariatePointValuePair smallest = optimizer.best(a, b, GoalType.MINIMIZE);
        assertEquals(a, smallest);

        UnivariatePointValuePair largest = optimizer.best(b, c, GoalType.MAXIMIZE);
        assertEquals(c, largest);

        UnivariatePointValuePair same = optimizer.best(a, a, GoalType.MINIMIZE);
        assertEquals(a, same);
    }

}