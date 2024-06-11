import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with default constructor and no convergence checker.
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new MyFunction();
        UnivariateOptimizerResult result = optimizer.optimize(function, new SimpleBounds(-10, 10), new ValueInitializer(5.0));

        PointValuePair bestPoint = result.getPoint();
        double bestValue = result.getValue();
        assertEquals(0.0, bestValue, 1e-5);
        assertNotNull(bestPoint);
        assertTrue(bestPoint.isDefined());

        // Test with custom convergence checker.
        BrentOptimizer optimizerChecker = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
        UnivariateOptimizerResult resultChecker = optimizerChecker.optimize(function, new SimpleBounds(-10, 10), new ValueInitializer(5.0));

        PointValuePair bestPointChecker = resultChecker.getPoint();
        double bestValueChecker = resultChecker.getValue();
        assertEquals(0.0, bestValueChecker, 1e-5);
        assertNotNull(bestPointChecker);
        assertTrue(bestPointChecker.isDefined());

        // Test with different thresholds.
        BrentOptimizer optimizerAbs = new BrentOptimizer(1e-5, 1e-6);
        BrentOptimizer optimizerRel = new BrentOptimizer(1e-6, 1e-5);

        UnivariateOptimizerResult resultAbs = optimizerAbs.optimize(function, new SimpleBounds(-10, 10), new ValueInitializer(5.0));
        UnivariateOptimizerResult resultRel = optimizerRel.optimize(function, new SimpleBounds(-10, 10), new ValueInitializer(5.0));

        PointValuePair bestPointAbs = resultAbs.getPoint();
        PointValuePair bestPointRel = resultRel.getPoint();

        assertEquals(bestPointAbs.getPoint(), bestPointRel.getPoint(), 1e-5);

        // Test with different bounds and initial values.
        SimpleBounds bounds1 = new SimpleBounds(0.0, 10.0);
        SimpleBounds bounds2 = new SimpleBounds(-5.0, 5.0);
        ValueInitializer initialValue1 = new ValueInitializer(2.0);
        ValueInitializer initialValue2 = new ValueInitializer(7.0);

        UnivariateOptimizerResult result1 = optimizer.optimize(function, bounds1, initialValue1);
        UnivariateOptimizerResult result2 = optimizer.optimize(function, bounds2, initialValue2);

        PointValuePair bestPoint1 = result1.getPoint();
        PointValuePair bestPoint2 = result2.getPoint();

        assertNotNull(bestPoint1);
        assertNotNull(bestPoint2);

        // Test with invalid thresholds.
        try {
            new BrentOptimizer(-1e-5, 1e-5);
            fail("An exception should have been thrown.");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new BrentOptimizer(1e-5, -1e-5);
            fail("An exception should have been thrown.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    private static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.exp(-x) - 5.0;
        }

        @Override
        public int getNumberOfVariables() {
            return 1;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return previous == null || Math.abs(current.getValue() - previous.getValue()) < 1e-6;
        }
    }
}