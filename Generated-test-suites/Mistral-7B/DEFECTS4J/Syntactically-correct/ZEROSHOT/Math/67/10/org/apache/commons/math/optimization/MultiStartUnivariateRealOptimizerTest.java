import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointsToTest;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        UnivariateRealOptimizer wrappedOptimizer = new FibonacciOptimizer();
        optimizer = new MultiStartUnivariateRealOptimizer(wrappedOptimizer, 3, new FibonacciRandomGenerator());
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return -2 * Math.sin(x) * Math.cos(x);
            }
        };
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        double min = -5.0;
        double max = 5.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max);
        assertEquals(Math.PI / 2, result, 0.001);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 2, Math.PI / 2}, optima, 0.001);
        assertArrayEquals(new double[]{Math.pow(Math.sin(Math.PI / 2), 2),
                Math.pow(Math.sin(Math.PI / 4), 2), Math.pow(Math.sin(3 * Math.PI / 4), 2)},
                optimaValues, 0.001);
    }

    @Test
    public void testOptimizeWithStartValue() throws ConvergenceException, FunctionEvaluationException {
        double min = -5.0;
        double max = 5.0;
        double startValue = 1.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(Math.PI / 2, result, 0.001);
        double[] optima = optimizer.getOptima();
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 2, Math.PI / 2}, optima, 0.001);
        assertArrayEquals(new double[]{Math.pow(Math.sin(Math.PI / 2), 2),
                Math.pow(Math.sin(Math.PI / 4), 2), Math.pow(Math.sin(3 * Math.PI / 4), 2)},
                optimaValues, 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction functionWithException = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        optimizer.optimize(functionWithException, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction functionWithException = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    throw new ConvergenceException("Convergence exception");
                }
                return 0.0;
            }
        };
        optimizer.optimize(functionWithException, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testErrorMessages() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction functionWithException = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Function evaluation error");
            }
        };
        try {
            optimizer.optimize(functionWithException, GoalType.MINIMIZE, 0.0, 1.0);
        } catch (FunctionEvaluationException fex) {
            assertEquals("Function evaluation error", fex.getMessage());
        }

        UnivariateRealFunction functionWithMaxIterations = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (optimizer.getIterationCount() > 100) {
                    throw new ConvergenceException("Maximal iteration count reached");
                }
                return 0.0;
            }
        };
        try {
            optimizer.optimize(functionWithMaxIterations, GoalType.MINIMIZE, 0.0, 1.0, 100);
        } catch (ConvergenceException ce) {
            assertEquals("Maximal iteration count reached", ce.getMessage());
        }
    }

    @Test
    public void testIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
        optimizer = new MultiStartUnivariateRealOptimizer(null, 3, new FibonacciRandomGenerator());
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        } catch (IllegalArgumentException iae) {
            assertEquals("wrappedOptimizer is null", iae.getMessage());
        }

        optimizer = new MultiStartUnivariateRealOptimizer(optimizer, 0, new FibonacciRandomGenerator());
        try {
            optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        } catch (IllegalArgumentException iae) {
            assertEquals("starts must be greater than 1", iae.getMessage());
        }
    }

    @Test
    public void testMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        optimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new FibonacciRandomGenerator());
        optimizer.setMaxIterations(5);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(5 * 3, optimizer.getIterationCount());
    }

    @Test
    public void testMaxEvaluations() throws ConvergenceException, FunctionEvaluationException {
        optimizer = new MultiStartUnivariateRealOptimizer(optimizer, 3, new FibonacciRandomGenerator());
        optimizer.setMaxEvaluations(5 * 3);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(5 * 3, optimizer.getEvaluations());
    }

    @Test
    public void testGetOptima() throws ConvergenceException, FunctionEvaluationException {
        double[] optima = optimizer.getOptima();
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN}, optima);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        optima = optimizer.getOptima();
        assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 2, Math.PI / 2}, optima, 0.001);
    }

    @Test
    public void testGetOptimaValues() throws ConvergenceException, FunctionEvaluationException {
        double[] optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN}, optimaValues);
        optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        optimaValues = optimizer.getOptimaValues();
        assertArrayEquals(new double[]{Math.pow(Math.sin(Math.PI / 2), 2),
                Math.pow(Math.sin(Math.PI / 4), 2), Math.pow(Math.sin(3 * Math.PI / 4), 2)},
                optimaValues, 0.001);
    }
}

class FibonacciRandomGenerator implements RandomGenerator {
    private int counter = 0;

    @Override
    public double nextGaussian() {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public double nextExponential(double mu) {
        // This is a simple random number generator that simulates
        // the Fibonacci distribution. It's not very good, but it's
        // simple and fast.
        counter++;
        if (counter % 5 == 0) {
            return Math.random();
        } else {
            return (counter % 3 + 1) / 2.0;
        }
    }

    @Override
    public double nextUniform(double min, double max) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public long nextLong(long min, long max) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public int nextInt(int min, int max) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    @Override
    public void setSeed(long seed) {
        throw new UnsupportedOperationException("Method not supported.");
    }
}