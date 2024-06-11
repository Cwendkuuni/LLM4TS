import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.Average;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for BrentOptimizer.
 */
public class BrentOptimizerTest {

    /**
     * Test for optimize method with a simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);

        Assert.assertEquals(2.0, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for optimize method with a function having two local minima.
     */
    @Test
    public void testOptimizeFunctionWithTwoLocalMinima() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2) * (Math.sin(Math.PI * x) + 1);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);

        Assert.assertEquals(-2.4495, result.getPoint(), 1e-6);
        Assert.assertEquals(3.1316e-4, result.getValue(), 1e-6);
    }

    /**
     * Test for optimize method when the function has no real roots.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionWithNoRealRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 1;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    /**
     * Test for optimize method when the function is not defined at the start value.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionNotDefinedAtStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    throw new ArithmeticException("Function not defined for x < 0");
                }
                return Math.pow(x, 2) + Math.sin(x);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, -1.0, 1.0, 0.0);
    }

    /**
     * Test for optimize method when the function is not decreasing in the interval.
     */
    @Test
    public void testOptimizeFunctionNotDecreasing() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.cos(x);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);

        // The BrentOptimizer should not converge to a minimum in this case.
        Assert.assertTrue(Double.isNaN(result.getPoint()));
    }

    /**
     * Test for optimize method when the maximum number of iterations is reached.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);
    }

    /**
     * Test for optimize method where the tolerance argument is too small.
     */
    @Test
    public void testOptimizeToleranceTooSmall() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(1e-14);
        optimizer.setRelativeAccuracy(1e-16);
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -5.0, 5.0, 0.0);

        Assert.assertEquals(Math.sqrt(2), result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test for optimize method where the start value is too close to the minimum.
     */
    @Test
    public void testOptimizeStartValueTooClose() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2);
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 1.9, 5.0, 0.0);
    }

    /**
     * Test for optimize method where the interval contains no real roots.
     */
    @Test
    public void testOptimizeIntervalNoRealRoots() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x)) - 1;
            }
        };

        UnivariateOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
    }
}