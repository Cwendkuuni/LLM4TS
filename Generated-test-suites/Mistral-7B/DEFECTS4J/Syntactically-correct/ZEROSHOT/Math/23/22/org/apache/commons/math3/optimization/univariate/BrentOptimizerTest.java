package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This class contains test methods for the {@code BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    /**
     * Sets up a new {@code BrentOptimizer} instance and a test function for
     * testing.
     */
    @Before
    public void setUp() {
        function = new MathFunction();
        optimizer = new BrentOptimizer(1e-5, 1e-5, null);
    }

    /**
     * Tests the optimization of the function {@code f(x) = x^3 - 2x^2 + x - 1}
     * with the initial guess {@code x = 1.5}.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunction1() throws ConvergenceException {
        optimizer.setMinimizer(true);
        optimizer.optimize(function, 1.5);

        double expectedX = -1.0;
        double expectedFx = 2.0;
        UnivariatePointValuePair result = optimizer.getResult();

        Assert.assertEquals(expectedX, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedFx, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of the function {@code f(x) = x^3 - 2x^2 + x - 1}
     * with the initial guess {@code x = 0.5}.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunction2() throws ConvergenceException {
        optimizer.setMinimizer(true);
        optimizer.optimize(function, 0.5);

        double expectedX = -1.0;
        double expectedFx = 2.0;
        UnivariatePointValuePair result = optimizer.getResult();

        Assert.assertEquals(expectedX, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedFx, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of the function {@code f(x) = x^3 - 2x^2 + x - 1}
     * with the initial guess {@code x = 2.5}.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunction3() throws ConvergenceException {
        optimizer.setMinimizer(true);
        optimizer.optimize(function, 2.5);

        double expectedX = -1.0;
        double expectedFx = 2.0;
        UnivariatePointValuePair result = optimizer.getResult();

        Assert.assertEquals(expectedX, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedFx, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of the function {@code f(x) = x^3 - 2x^2 + x - 1}
     * with the initial guess {@code x = 1}.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunction4() throws ConvergenceException {
        optimizer.setMinimizer(true);
        optimizer.optimize(function, 1.0);

        double expectedX = -1.0;
        double expectedFx = 2.0;
        UnivariatePointValuePair result = optimizer.getResult();

        Assert.assertEquals(expectedX, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedFx, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of the function {@code f(x) = x^3 - 2x^2 + x - 1}
     * with the initial guess {@code x = 0}.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunction5() throws ConvergenceException {
        optimizer.setMinimizer(true);
        optimizer.optimize(function, 0.0);

        double expectedX = -1.0;
        double expectedFx = 2.0;
        UnivariatePointValuePair result = optimizer.getResult();

        Assert.assertEquals(expectedX, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedFx, result.getValue(), 1e-6);
    }

    /**
     * Tests the optimization of an unbounded function.
     * @throws ConvergenceException if the optimization did not converge.
     * @throws NotStrictlyPositiveException if the optimization parameters are invalid.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeUnboundedFunction()
            throws ConvergenceException, NotStrictlyPositiveException {
        optimizer = new BrentOptimizer(-1.0, 1e-5, null);
        optimizer.optimize(new MathFunctionUnbounded(), 1.5);
    }

    /**
     * Tests the optimization of a function with a relative threshold smaller
     * than the minimum relative tolerance.
     * @throws ConvergenceException if the optimization did not converge.
     * @throws NotStrictlyPositiveException if the optimization parameters are invalid.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeSmallRelativeThreshold()
            throws ConvergenceException, NotStrictlyPositiveException {
        optimizer = new BrentOptimizer(1e-8, 1e-5, null);
        optimizer.optimize(function, 1.5);
    }

    /**
     * Tests the optimization of a function with an absolute threshold smaller
     * than zero.
     * @throws ConvergenceException if the optimization did not converge.
     * @throws NotStrictlyPositiveException if the optimization parameters are invalid.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNegativeAbsoluteThreshold()
            throws ConvergenceException, NotStrictlyPositiveException {
        optimizer = new BrentOptimizer(1e-5, -1e-5, null);
        optimizer.optimize(function, 1.5);
    }

    /**
     * Tests the optimization of a function with a convergence checker.
     * @throws ConvergenceException if the optimization did not converge.
     */
    @Test
    public void testOptimizeFunctionWithConvergenceChecker()
            throws ConvergenceException {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker
                = new CustomConvergenceChecker();
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(function, 1.5);

        Assert.assertTrue(convergenceChecker.wasCalled());
    }
}

class MathFunction implements UnivariateFunction {

    @Override
    public double value(double x) {
        return x * x * x - 2 * x * x + x - 1;
    }
}

class MathFunctionUnbounded implements UnivariateFunction {

    @Override
    public double value(double x) {
        return x * x * x;
    }
}

class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

    private int counter = 0;

    @Override
    public boolean converged(int iter, UnivariatePointValuePair previous,
                             UnivariatePointValuePair current) {
        if (counter++ > 1) {
            return true;
        }
        return false;
    }
}