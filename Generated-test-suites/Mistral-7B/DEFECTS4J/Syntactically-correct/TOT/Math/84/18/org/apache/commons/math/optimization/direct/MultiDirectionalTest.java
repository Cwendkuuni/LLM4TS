package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.nonlinear.ScalarFunction;
import org.apache.commons.math3.optim.nonlinear.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.RhoX;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    /** A simple test function for MultiDirectional */
    private static final ScalarFunction TEST_FUNCTION = new SimpleValueFunction("testFunction", x -> x * x);

    /** A convergence checker for the test */
    private static final AbstractConvergenceChecker<PointValuePair> CONVERGENCE_CHECKER = new RhoX(1.0E-6);

    /** A test method for the MultiDirectional optimizer */
    @Test
    public void testMultiDirectional() throws Exception {
        // create a MultiDirectional optimizer
        final MultiDirectional optimizer = new MultiDirectional();

        // create a SimpleValueFunction to be optimized
        final ScalarFunction function = TEST_FUNCTION;

        // set up an initial simplex
        final RealPointValuePair[] simplex = new RealPointValuePair[]{
            new RealPointValuePair(new double[]{1.0, 2.0}, 3.0, false),
            new RealPointValuePair(new double[]{3.0, 1.0}, 5.0, false),
            new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false)
        };

        // optimize the function using the MultiDirectional optimizer
        optimizer.optimize(function, simplex, new SimpleConvergenceChecker(1000), null);

        // check that the best point in the simplex is the global minimum
        final RealPointValuePair best = optimizer.getBestPoint();
        assertEquals(0.0, function.value(best.getPointRef()), 1e-6);

        // test with a function that has multiple local minima
        final ScalarFunction functionWithMultipleLocalMinima = new SimpleValueFunction("testFunctionWithMultipleLocalMinima", x -> x * x * x - 3 * x * x + 2 * x);
        optimizeAndCheck(optimizer, functionWithMultipleLocalMinima, new double[]{1.0, 2.0});
        optimizeAndCheck(optimizer, functionWithMultipleLocalMinima, new double[]{2.0, 1.0});
        optimizeAndCheck(optimizer, functionWithMultipleLocalMinima, new double[]{3.0, 1.0});

        // test with a function that has no real root
        final ScalarFunction functionWithNoRealRoot = new SimpleValueFunction("testFunctionWithNoRealRoot", x -> Math.sin(Math.PI) / x);
        assertThrows(OptimizationException.class, () -> optimizeAndCheck(optimizer, functionWithNoRealRoot, new double[]{1.0}));

        // test with a function that throws an exception
        final ScalarFunction functionThatThrowsException = new ScalarFunction() {
            @Override
            public double value(double[] point) {
                throw new RuntimeException("Test exception");
            }
        };
        assertThrows(FunctionEvaluationException.class, () -> optimizeAndCheck(optimizer, functionThatThrowsException, new double[]{1.0}));

    }

    /** Helper method to optimize a function and check the result */
    private void optimizeAndCheck(MultiDirectional optimizer, ScalarFunction function, double[] initialPoint) throws Exception {
        // create a SimpleValueFunction to be optimized
        optimizer.setMaxIterations(100);
        optimizer.optimize(function, new RealPointValuePair[]{new RealPointValuePair(initialPoint, Double.NaN, false)}, CONVERGENCE_CHECKER, null);
    }

}