import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonUnaryOptimizeException;
import org.apache.commons.math3.optim.UnivariateOptimizer.FindingType;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertEquals(0.3626925, result.getPoint(), 1e-6);
        assertEquals(-0.0326925, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerWithInitialGuess() {
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);

        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), new InitialGuess(0.5));
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertEquals(0.3626925, result.getPoint(), 1e-6);
        assertEquals(-0.0326925, result.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerZeroRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerZeroRelativeThresholdAndNegativeAbsThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1.0, 1.0);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() throws Exception {
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, new SimpleConvergenceChecker());

        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertEquals(0.3626925, result.getPoint(), 1e-6);
        assertEquals(-0.0326925, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMaxIterations() throws Exception {
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null, 10);

        try {
            optimizer.optimize(function, new SimpleBounds(0.0, 1.0), null);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        PointValuePair result = optimizer.getLastIterationResult();
        assertNotNull(result);
        assertEquals(0.6, result.getPoint(), 1e-6);
        assertEquals(-0.1, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerFindsMinimum() {
        UnivariateFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);

        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertEquals(0.3626925, result.getPoint(), 1e-6);
        assertEquals(-0.0326925, result.getValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizerFindsMaximum() {
        UnivariateFunction function = new SimpleFunctionMax();
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);

        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.optimize(function, new SimpleBounds(0.0, 1.0), null);
        PointValuePair result = optimizer.getResult();
        assertNotNull(result);
        assertEquals(0.6371225, result.getPoint(), 1e-6);
        assertEquals(0.0371225, result.getValue(), 1e-6);
    }

    private static class SimpleFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
        }
    }

    private static class SimpleFunctionMax implements UnivariateFunction {
        @Override
        public double value(double x) {
            return -Math.pow(x, 3) + 2 * x * Math.pow(Math.E, x);
        }
    }

    private static class SimpleConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
            return iteration > 10;
        }
    }
}