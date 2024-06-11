package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.MalfunctionException;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.goaltypes.Maximize;
import org.apache.commons.math3.optim.nonlinear.goaltypes.Minimize;
import org.apache.commons.math3.optim.points.PointValuePairCollection;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.SimpleConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.local.NelderMeadSimplex;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws Exception
     */
    @org.junit.Test
    public void testOptimize() throws Exception {
        // Test with a simple optimizer (Nelder-Mead)
        UnivariateRealFunction f = new PolynomialFunction(1, new double[]{-1.0, 1.0, 1.0});
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
            new MultiStartUnivariateRealOptimizer<>(new NelderMeadSimplex<>(), 10,
                                                    new org.apache.commons.math3.random.MersenneTwisterFastRandom());

        // Test minimization
        GoalType goalTypeMin = Minimize.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        PointValuePairCollection pvs = optimizer.optimize(f, goalTypeMin, min, max);
        assertEquals(1, pvs.size());
        UnivariateRealPointValuePair pvp = (UnivariateRealPointValuePair) pvs.get(0);
        assertEquals(0.0, pvp.getX(), 1e-9);
        assertEquals(-2.0, pvp.getValue(), 1e-9);

        // Test maximization
        GoalType goalTypeMax = Maximize.MAXIMIZE;
        PointValuePairCollection pvsMax = optimizer.optimize(f, goalTypeMax, min, max);
        assertEquals(1, pvsMax.size());
        UnivariateRealPointValuePair pvpMax = (UnivariateRealPointValuePair) pvsMax.get(0);
        assertEquals(0.0, pvpMax.getX(), 1e-9);
        assertEquals(2.0, pvpMax.getValue(), 1e-9);

        // Test with bounds
        UnivariateRealFunction fWithBounds = new PolynomialFunction(1, new double[]{-5.0, 5.0, 1.0});
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        optimizer = new MultiStartUnivariateRealOptimizer<>(new NelderMeadSimplex<>(), 10,
                                                            new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        optimizer.setConvergenceChecker(new SimpleConvergenceChecker(1e-8));
        PointValuePairCollection pvsWithBounds = optimizer.optimize(fWithBounds, goalTypeMin, bounds.getMin(),
                                                                    bounds.getMax());
        assertEquals(1, pvsWithBounds.size());
        UnivariateRealPointValuePair pvpWithBounds = (UnivariateRealPointValuePair) pvsWithBounds.get(0);
        assertEquals(0.0, pvpWithBounds.getX(), 1e-9);
        assertEquals(-1.5, pvpWithBounds.getValue(), 1e-9);

        // Test convergence
        UnivariateRealFunction fWithNoRoots = new PolynomialFunction(1, new double[]{-1.0, 1.0, 1.0, 1.0});
        optimizer = new MultiStartUnivariateRealOptimizer<>(new NelderMeadSimplex<>(), 10,
                                                            new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        PointValuePairCollection pvsNoRoots = optimizer.optimize(fWithNoRoots, goalTypeMin, min, max);
        assertNull(pvsNoRoots.get(0));

        // Test maxEvaluations
        optimizer = new MultiStartUnivariateRealOptimizer<>(new NelderMeadSimplex<>(), 1,
                                                            new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        f = new PolynomialFunction(1, new double[]{-5.0, 5.0, 1.0});
        optimizer.setConvergenceChecker(new SimpleConvergenceChecker(1e-8));
        try {
            optimizer.optimize(f, goalTypeMin, min, max);
            fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }
    }
}