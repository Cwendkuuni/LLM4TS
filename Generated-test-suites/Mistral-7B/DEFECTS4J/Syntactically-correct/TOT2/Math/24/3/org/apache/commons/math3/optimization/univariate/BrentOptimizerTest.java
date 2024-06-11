package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization. SimpleBounds;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.Log;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testBrentOptimizer() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7, null);
        assertEquals(GoalType.MINIMIZE, opt.getGoalType());
        assertEquals(1e-5, opt.getRelativeThreshold(), 1e-7);
        assertEquals(1e-7, opt.getAbsoluteThreshold(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}.
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooSmallException
     */
    @Test
    public void testBrentOptimizer_Double_Double() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7);
        assertEquals(GoalType.MINIMIZE, opt.getGoalType());
        assertEquals(1e-5, opt.getRelativeThreshold(), 1e-7);
        assertEquals(1e-7, opt.getAbsoluteThreshold(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testDoOptimize_Minimize_SimpleBounds() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7, null);
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        UnivariateOptimizerResult result = opt.optimize(new BracketedRealValuedFunction("f", x -> Math.pow(x, 2) - 4 * x + 4), bounds);
        assertEquals(-2.0, result.getPoint().getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testDoOptimize_Minimize_BracketedRealValuedFunction() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7, null);
        UnivariateFunction function = x -> Math.exp(-x) - E.E;
        UnivariateOptimizerResult result = opt.optimize(function, null, null);
        assertEquals(-1.1752011936438015, result.getPoint().getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testDoOptimize_Maximize_SimpleBounds() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7, null);
        opt.setGoalType(GoalType.MAXIMIZE);
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        UnivariateOptimizerResult result = opt.optimize(new BracketedRealValuedFunction("f", x -> Math.pow(x, 2) + 4 * x + 4), bounds);
        assertEquals(2.0, result.getPoint().getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     */
    @Test
    public void testDoOptimize_Maximize_BracketedRealValuedFunction() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, 1e-7, null);
        opt.setGoalType(GoalType.MAXIMIZE);
        UnivariateFunction function = x -> Log.log(Math.E, x);
        UnivariateOptimizerResult result = opt.optimize(function, null, null);
        assertEquals(1.0, result.getPoint().getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDoOptimize_NegativeAbsThreshold() {
        BrentOptimizer opt = new BrentOptimizer(1e-5, -1e-7, null);
    }

    /**
     * Test method for {@link BrentOptimizer#doOptimize()}.
     * @throws NotStrictlyPositiveException
     * @throws NumberIsTooSmallException
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testDoOptimize_RelativeThresholdTooSmall() {
        BrentOptimizer opt = new BrentOptimizer(1e-10, 1e-7, null);
    }
}