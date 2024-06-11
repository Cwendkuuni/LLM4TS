import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * relative and absolute threshold.
     */
    @Test
    public void testBrentOptimizer() {
        // Create optimizer.
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);

        // Test optimization of a simple function.
        ObjectiveFunction objFunc = new SimpleFunction();
        UnivariateOptimizerResult result = optimizer.optimize(objFunc, -10.0, 10.0, null);
        assertTrue(result.isSuccess());
        PointValuePair expected = new PointValuePair(3.0, 1.5d);
        assertEquals(expected.getValue(), result.getOptimum(), 0.01);
        assertEquals(expected.getPoint(), result.getPoint(), 0.01);

        // Test optimization of a function with a local minimum.
        ObjectiveFunction objFuncLocalMinimum = new LocalMinimumFunction();
        UnivariateOptimizerResult resultLocalMinimum = optimizer.optimize(objFuncLocalMinimum, -10.0, 10.0, null);
        assertTrue(resultLocalMinimum.isSuccess());
        assertEquals(expectedLocalMinimum.getValue(), resultLocalMinimum.getOptimum(), 0.01);
        assertEquals(expectedLocalMinimum.getPoint(), resultLocalMinimum.getPoint(), 0.01);

        // Test optimization of a function with a local maximum.
        ObjectiveFunction objFuncLocalMaximum = new LocalMaximumFunction();
        UnivariateOptimizerResult resultLocalMaximum = optimizer.optimize(objFuncLocalMaximum, -10.0, 10.0, null);
        assertFalse(resultLocalMaximum.isSuccess());
        assertNull(resultLocalMaximum.getOptimum());
        assertEquals(3.0, resultLocalMaximum.getPoint(), 0.01);

        // Test optimization of a function with bounds.
        ObjectiveFunction objFuncWithBounds = new BoundsFunction();
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(1, 2, Relationship.LEQ, 1.0));
        UnivariateOptimizerResult resultWithBounds = optimizer.optimize(objFuncWithBounds, null, constraints, null);
        assertTrue(resultWithBounds.isSuccess());
        PointValuePair expectedWithBounds = new PointValuePair(1.0, -2.5d);
        assertEquals(expectedWithBounds.getValue(), resultWithBounds.getOptimum(), 0.01);
        assertEquals(expectedWithBounds.getPoint(), resultWithBounds.getPoint(), 0.01);

        // Test optimization of a function with a discontinuity.
        ObjectiveFunction objFuncDiscontinuity = new DiscontinuityFunction();
        UnivariateOptimizerResult resultDiscontinuity = optimizer.optimize(objFuncDiscontinuity, -10.0, 10.0, null);
        assertFalse(resultDiscontinuity.isSuccess());
        assertNull(resultDiscontinuity.getOptimum());
        assertNull(resultDiscontinuity.getPoint());
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double)} with
     * default relative and absolute threshold.
     */
    @Test
    public void testBrentOptimizerDefault() {
        // Create optimizer.
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test optimization of a simple function.
        ObjectiveFunction objFunc = new SimpleFunction();
        UnivariateOptimizerResult result = optimizer.optimize(objFunc, -10.0, 10.0, null);
        assertTrue(result.isSuccess());
        PointValuePair expected = new PointValuePair(3.0, 1.5d);
        assertEquals(expected.getValue(), result.getOptimum(), 0.02);
        assertEquals(expected.getPoint(), result.getPoint(), 0.02);

        // Test optimization of a function with a local minimum.
        ObjectiveFunction objFuncLocalMinimum = new LocalMinimumFunction();
        UnivariateOptimizerResult resultLocalMinimum = optimizer.optimize(objFuncLocalMinimum, -10.0, 10.0, null);
        assertTrue(resultLocalMinimum.isSuccess());
        assertEquals(expectedLocalMinimum.getValue(), resultLocalMinimum.getOptimum(), 0.02);
        assertEquals(expectedLocalMinimum.getPoint(), resultLocalMinimum.getPoint(), 0.02);

        // Test optimization of a function with a local maximum.
        ObjectiveFunction objFuncLocalMaximum = new LocalMaximumFunction();
        UnivariateOptimizerResult resultLocalMaximum = optimizer.optimize(objFuncLocalMaximum, -10.0, 10.0, null);
        assertFalse(resultLocalMaximum.isSuccess());
        assertNull(resultLocalMaximum.getOptimum());
        assertEquals(3.0, resultLocalMaximum.getPoint(), 0.02);

        // Test optimization of a function with bounds.
        ObjectiveFunction objFuncWithBounds = new BoundsFunction();
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(new LinearConstraint(1, 2, Relationship.LEQ, 1.0));
        UnivariateOptimizerResult resultWithBounds = optimizer.optimize(objFuncWithBounds, null, constraints, null);
        assertTrue(resultWithBounds.isSuccess());
        PointValuePair expectedWithBounds = new PointValuePair(1.0, -2.5d);
        assertEquals(expectedWithBounds.getValue(), resultWithBounds.getOptimum(), 0.04);
        assertEquals(expectedWithBounds.getPoint(), resultWithBounds.getPoint(), 0.04);

        // Test optimization of a function with a discontinuity.
        ObjectiveFunction objFuncDiscontinuity = new DiscontinuityFunction();
        UnivariateOptimizerResult resultDiscontinuity = optimizer.optimize(objFuncDiscontinuity, -10.0, 10.0, null);
        assertFalse(resultDiscontinuity.isSuccess());
        assertNull(resultDiscontinuity.getOptimum());
        assertNull(resultDiscontinuity.getPoint());
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with a custom convergence checker.
     */
    @Test
    public void testBrentOptimizerCustomChecker() {
        // Define a custom convergence checker.
        class MyCustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < 0.0001;
            }
        }

        // Create optimizer.
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, new MyCustomConvergenceChecker());

        // Test optimization of a simple function.
        ObjectiveFunction objFunc = new SimpleFunction();
        UnivariateOptimizerResult result = optimizer.optimize(objFunc, -10.0, 10.0, null);
        assertTrue(result.isSuccess());
        PointValuePair expected = new PointValuePair(3.0, 1.5d);
        assertEquals(expected.getValue(), result.getOptimum(), 0.01);
        assertEquals(expected.getPoint(), result.getPoint(), 0.01);
    }

    /**
     * Simple objective function for testing.
     */
    public static class SimpleFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return 1.5 * Math.pow(x - 3.0, 2);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Objective function with a local minimum.
     */
    public static class LocalMinimumFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.abs(x), 3) - 2;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Objective function with a local maximum.
     */
    public static class LocalMaximumFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return -Math.pow(Math.abs(x), 3) - 2;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MAXIMIZE;
        }
    }

    /**
     * Objective function with bounds.
     */
    public static class BoundsFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            return -2 * x + 2;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }

        @Override
        public double[] getBounds() {
            return new double[]{-5.0, 5.0};
        }
    }

    /**
     * Objective function with a discontinuity.
     */
    public static class DiscontinuityFunction implements ObjectiveFunction {
        @Override
        public double value(double x) {
            if (x < 0) {
                throw new IllegalArgumentException("Function is not defined for x < 0");
            }
            return Math.pow(Math.abs(x), 0.5);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }
    }

    /**
     * Expected result for local minimum test.
     */
    private static final PointValuePair expectedLocalMinimum = new PointValuePair(2.0, 0.0);
}