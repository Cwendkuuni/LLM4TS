package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.goaltypes.Maximize;
import org.apache.commons.math3.optim.nonlinear.goaltypes.Minimize;
import org.apache.commons.math3.random.MersenneTwister;

import org.junit.Test;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.optimization.InitialGuessRealVector;
import org.apache.commons.math.optimization.PointValuePairRealVector;
import org.apache.commons.math.optimization.GoalType;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testBasicFunction() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.RELATIVE;
        UnivariateRealPointValuePair bestPoint = multiOptimizer.optimize(function, goal, -10.0, 10.0);

        // Then
        assertEquals(0.0, bestPoint.getPoint().doubleValue(), 0.01);
        assertArrayEquals(new double[]{0.0}, bestPoint.getValue().toArray(), 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}
     * with given start value.
     */
    @Test
    public void testBasicFunctionWithStartValue() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.RELATIVE;
        UnivariateRealPointValuePair bestPoint = multiOptimizer.optimize(function, goal, -10.0, 10.0, 2.5);

        // Then
        assertEquals(0.0, bestPoint.getPoint().doubleValue(), 0.01);
        assertArrayEquals(new double[]{0.0}, bestPoint.getValue().toArray(), 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with negative number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfStarts() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, -1, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.RELATIVE;
        multiOptimizer.optimize(function, goal, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with max evaluations reached.
     */
    @Test
    public void testMaxEvaluationsReached() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(1);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.ABSOLUTE;
        UnivariateRealPointValuePair bestPoint = multiOptimizer.optimize(function, goal, -10.0, 10.0);

        // Then
        assertNull(bestPoint);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with function not implementing {@code UnivariateRealFunction}.
     */
    @Test(expected = ClassCastException.class)
    public void testFunctionNotImplementingUnivariateRealFunction() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        Object function = new Object() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.RELATIVE;
        multiOptimizer.optimize(function, goal, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with {@code MAX_EVAL} as goal type.
     */
    @Test
    public void testGoalTypeMaxEval() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = MaxEval.ABSOLUTE;
        UnivariateRealPointValuePair bestPoint = multiOptimizer.optimize(function, goal, -10.0, 10.0);
        int evaluations = multiOptimizer.getEvaluations();

        // Then
        assertNull(bestPoint);
        assertEquals(10, evaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with {@code ConvergenceChecker} not set.
     */
    @Test
    public void testNoConvergenceChecker() {
        // Given
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BrentOptimizer();
        optimizer.setConvergenceChecker(null);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiOptimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister(1));

        // When
        MultiStartUnivariateRealOptimizer.GoalType goal = Minimize.RELATIVE;
        UnivariateRealPointValuePair bestPoint = multiOptimizer.optimize(function, goal, -10.0, 10.0);

        // Then
        assertNull(bestPoint);
    }
}