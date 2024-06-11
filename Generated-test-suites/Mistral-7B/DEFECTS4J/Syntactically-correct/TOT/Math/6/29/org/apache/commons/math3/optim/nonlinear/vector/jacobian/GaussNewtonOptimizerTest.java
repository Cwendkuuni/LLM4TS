package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.initializer.StartPointStrategy;
import org.apache.commons.math3.optim.nonlinear.Gradient;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.gradient.FiniteDifferenceGradient;
import org.apache.commons.math3.optim.point.PointValuePairMaker;
import org.apache.commons.math3.optim.strategies.DifferenceStrategy;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test a simple optimization problem with known solution.
     */
    @Test
    public void testOptimizeSimpleProblem() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        // Define the objective function and its gradient.
        PolynomialFunction f = new PolynomialFunction(2, -1.0,
                new double[]{-4.0, 4.0, -4.0, 4.0});
        Gradient g = new FiniteDifferenceGradient(new DifferenceStrategy(1.0e-6, 1.0e-6));

        // Define the initial point and the exact solution.
        double[] x0 = new double[]{1.0, 1.0};
        double[] x = new double[]{1.5, 1.5};

        // Test the optimization algorithm.
        PointValuePair result = optimizer.optimize(f, x0, null);
        PointValuePair expected = new PointValuePairMaker().make(x);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
    }

    /**
     * Test optimization with wrong arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(null, new double[]{0.0}, null);
    }

    /**
     * Test optimization with wrong arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new LinearFunction(2), null, null);
    }

    /**
     * Test optimization with wrong arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWrongNumberOfDimensions() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new LinearFunction(1), new double[]{0.0, 0.0}, null);
    }

    /**
     * Test optimization with wrong arguments.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizeFunctionThrowsException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new PolynomialFunction(2, -1.0,
                new double[]{-4.0, 4.0, -4.0, 4.0, Double.NaN}), new double[]{0.0, 0.0}, null);
    }

    /**
     * Test optimization with wrong arguments.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeSingularProblem() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.optimize(new LinearFunction(2), new double[]{0.0, 0.0}, null);
    }

    /**
     * Test optimization with wrong convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtOptimizer(false);
        optimizer.optimize(new LinearFunction(2), new double[]{0.0}, null);
    }

    /**
     * Test optimization with a random start point.
     */
    @Test
    public void testOptimizeRandomStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        // Define the objective function and its gradient.
        PolynomialFunction f = new PolynomialFunction(2, -1.0,
                new double[]{-4.0, 4.0, -4.0, 4.0});
        Gradient g = new FiniteDifferenceGradient(new DifferenceStrategy(1.0e-6, 1.0e-6));

        // Define the initial point and the exact solution.
        double[] x0 = new double[]{1.0, 1.0};
        double[] x = new double[]{3.0, 3.0};

        // Test the optimization algorithm.
        PointValuePair result = optimizer.optimize(f, x0, null);
        PointValuePair expected = new PointValuePairMaker().make(x);
        assertNotNull(result);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1.0e-6);

        // Test with a different initial point.
        x = new double[]{2.0, 2.0};
        expected = new PointValuePairMaker().make(x);
        result = optimizer.optimize(f, x0, null);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
    }

    /**
     * Test optimization with different convergence checker.
     */
    @Test
    public void testOptimizeCustomConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new MyCustomConvergenceChecker());

        // Define the objective function and its gradient.
        PolynomialFunction f = new PolynomialFunction(2, -1.0,
                new double[]{-4.0, 4.0, -4.0, 4.0});
        Gradient g = new FiniteDifferenceGradient(new DifferenceStrategy(1.0e-6, 1.0e-6));

        // Define the initial point and the exact solution.
        double[] x0 = new double[]{1.0, 1.0};
        double[] x = new double[]{1.5, 1.5};

        // Test the optimization algorithm.
        PointValuePair result = optimizer.optimize(f, x0, null);
        PointValuePair expected = new PointValuePairMaker().make(x);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
    }
}

class MyCustomConvergenceChecker implements ConvergenceChecker<PointVectorValuePair> {
    @Override
    public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
        return current.getValue() < previous.getValue() & current.getValue() < 0.0;
    }
}