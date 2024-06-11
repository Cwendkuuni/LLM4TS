import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class BrentOptimizerTest {

    @Rule
    public TestRule outRule = new SystemOutRule().consumesOutput();

    @Rule
    public TestRule errorRule = new ExceptionRule();

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setup() {
        function = new MyTestFunction();
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
    }

    @Test
    public void testOptimizeSingleRoot() {
        optimizer.setFunction(function);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(-4.0, result.getValue(), 1.0e-6);
    }

    @Test
    public void testOptimizeTwoRoots() {
        optimizer.setFunction(new MyTestFunctionWithTwoRoots());
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(1.5, result.getPoint(), 1.0e-6);
        assertEquals(-2.25, result.getValue(), 1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidRelativeThreshold() {
        optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCountReached() {
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        optimizer.setMaxIterations(10);
        optimizer.setFunction(new MyTestFunctionWithInfRoots());
        optimizer.optimize();
    }

    @Test
    public void testPrintOutput() {
        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        optimizer.setFunction(new MyTestFunctionWithPrintOutput());
        optimizer.optimize();
        String output = outRule.getLog();
        assertTrue(output.contains("Iteration 0: x = "));
        assertTrue(output.contains("Iteration 1: x = "));
        assertTrue(output.contains("Iteration 2: x = "));
    }

    @Test
    public void testConvergenceChecker() {
        class MyTestConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
            private int iterationCount;

            @Override
            public boolean converged(int iteration, UnivariatePointValuePair current, UnivariatePointValuePair best) {
                iterationCount++;
                if (iteration > 5) {
                    return true;
                }
                return false;
            }

            public int getIterationCount() {
                return iterationCount;
            }
        }

        optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new MyTestConvergenceChecker());
        optimizer.setFunction(new MyTestFunction());
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(1.0, result.getPoint(), 1.0e-6);
        assertEquals(-4.0, result.getValue(), 1.0e-6);
        assertEquals(6, ((MyTestConvergenceChecker) optimizer.getConvergenceChecker()).getIterationCount());
    }

    private static class MyTestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 4) - 4 * x * Math.pow(Math.E, x);
        }

        @Override
        public double derivative(double x) {
            return 4 * Math.pow(x, 3) - 4 * Math.E * x;
        }
    }

    private static class MyTestFunctionWithTwoRoots implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x - 1.5, 2) - 2.25;
        }

        @Override
        public double derivative(double x) {
            return 2 * (x - 1.5);
        }
    }

    private static class MyTestFunctionWithInfRoots implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.log(Math.abs(x - 1.0)) / (x - 1.0);
        }

        @Override
        public double derivative(double x) {
            if (Double.isInfinite(x - 1.0)) {
                throw new ArithmeticException("Division by zero");
            }
            return (1.0 / (x - 1.0) - Math.log(Math.abs(x - 1.0))) / (x - 1.0);
        }
    }

    private static class MyTestFunctionWithPrintOutput implements UnivariateFunction {
        @Override
        public double value(double x) {
            System.out.println("x = " + x);
            return Math.pow(x, 2) - 4.0;
        }

        @Override
        public double derivative(double x) {
            return 2.0 * x;
        }
    }
}