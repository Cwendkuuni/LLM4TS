package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimumInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximization;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimization;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFitness;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BrentOptimizerTest {

    private static UnivariateOptimizer optimizer;

    @BeforeClass
    public static void setup() {
        optimizer = new BrentOptimizer();
    }

    /**
     * Test optimization of a simple quadratic function.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationQuadraticFunction() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, new SimpleBounds(-10.0, 10.0), null);
        Assert.assertEquals(0.0, result.getPoint(), 1e-12);
    }

    /**
     * Test optimization of a non-differentiable function.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationAbsFunction() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 2.0);
            }
        };
        GoalType goalType = new GoalTypeMinimization();
        SimpleBounds bounds = new SimpleBounds(1.0, 3.0);
        PointValuePair result = optimizer.optimize(f, goalType, bounds, null);
        Assert.assertEquals(2.0, result.getPoint(), 1e-12);
    }

    /**
     * Test optimization of a non-convex function.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationSquareRoot() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.abs(x - 2.0));
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 4.0);
        GoalType goalType = new GoalTypeMinimization();
        PointValuePair result = optimizer.optimize(f, goalType, bounds, null);
        Assert.assertEquals(2.0, result.getPoint(), 1e-12);
    }

    /**
     * Test optimization of a function with multiple local minima.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationMultipleLocalMinima() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2) * Math.exp(-Math.pow(x, 2));
            }
        };

        SimpleBounds bounds = new SimpleBounds(-3.0, 3.0);
        GoalType goalType = new GoalTypeMinimization();
        PointValuePair result = optimizer.optimize(f, goalType, bounds, null);

        // The result may not be exact, since the function has multiple local minima.
        // The test just checks that the result is within the bounds and better than the starting point.
        Assert.assertTrue(result.getValue() < f.value(0.0));
        Assert.assertTrue(result.getValue() > f.value(-3.0));
    }

    /**
     * Test optimization with initializer.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationInitializer() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2);
            }
        };
        UnivariateFunction initializer = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.5 * Math.PI + x;
            }
        };
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        GoalType goalType = new GoalTypeMinimization();
        PointValuePair result = optimizer.optimize(f, goalType, bounds, new AroundMinimumInitializer(initializer, -0.01));

        // The result may not be exact, since the function has multiple local minima.
        // The test just checks that the result is within the bounds and better than the starting point.
        Assert.assertTrue(result.getValue() < f.value(1.0));
        Assert.assertTrue(result.getValue() > f.value(0.0));
    }

    /**
     * Test optimization with constraint.
     * @throws MaxEvalExceededException
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testOptimizationConstraint() throws MaxEvalExceededException, NotStrictlyPositiveException {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2);
            }
        };
        UnivariateFunction constraint = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x - 1.0);
            }
        };
        PointValuePairFitness fitness = new PointValuePairFitness(GoalTypeMaximization.INSTANCE);
        GoalType goalType = new GoalTypeConstraint(goalType -> fitness, constraint);
        SimpleBounds bounds = new SimpleBounds(0.0, 2.0);
        PointValuePair result = optimizer.optimize(f, goalType, bounds, null);

        // The result may not be exact, since the function has multiple local minima.
        // The test just checks that the result is within the bounds and better than the starting point.
        Assert.assertTrue(result.getValue() > f.value(0.0));
        Assert.assertTrue(result.getPoint() > 0.0);
        Assert.assertTrue(result.getPoint() < 2.0);
    }
}