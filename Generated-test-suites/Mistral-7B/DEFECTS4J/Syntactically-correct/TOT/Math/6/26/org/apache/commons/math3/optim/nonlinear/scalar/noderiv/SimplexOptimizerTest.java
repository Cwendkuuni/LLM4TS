package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunctions;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.initialguess.RandomInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeStrategy;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing {@link SimplexOptimizer} class.
 */
public class SimplexOptimizerTest {
    /** Function to optimize. */
    private MultivariateFunction RosenbrockFunction;

    /** Optimizer. */
    private SimplexOptimizer optimizer;

    /**
     * Sets up test environment.
     */
    @Before
    public void setUp() {
        RosenbrockFunction = new MultivariateFunctions.RosenbrockFunction(2);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(new SimplexSimplex(new RandomInitialGuess(2), GoalType.MINIMIZE));
    }

    /**
     * Test optimization of simple function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        double[] startPoint = new double[]{-1.2, -1.2};

        PointValuePair result = optimizer.optimize(1000, RosenbrockFunction, GoalType.MINIMIZE, startPoint);

        Assert.assertEquals(5.249393373218849E-2, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.2, 1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization of negative result function.
     */
    @Test
    public void testOptimizeNegativeResultFunction() {
        double[] startPoint = new double[]{1.2, 1.2};

        PointValuePair result = optimizer.optimize(1000, RosenbrockFunction, GoalType.MAXIMIZE, startPoint);

        Assert.assertEquals(-5.249393373218849E-2, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{-1.2, -1.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with null objective function.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        optimizer.optimize(1000, null, GoalType.MINIMIZE, new double[]{0.0, 0.0});
    }

    /**
     * Test optimization with null optimization data.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullData() {
        optimizer.optimize(1000, RosenbrockFunction, null, null);
    }
}