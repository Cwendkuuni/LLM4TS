package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer.BrentOptimizerBuilder;
import org.apache.commons.math3.optimization.univariate.BracketedUnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BracketedUnivariateFunctionAdapter;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.special.Log;

/**
 * Test file for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testBrentOptimizer() {
        // Test with default ConvergenceChecker.
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = new BracketedUnivariateFunctionAdapter(
                new Log(Math.E), new SimpleBounds(1, 2));
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(E.E, result.getPoint(), 1e-7);
        
        // Test with custom ConvergenceChecker.
        BrentOptimizerBuilder builder = new BrentOptimizerBuilder();
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, last, current) -> {
            assertFalse(iter > 100);
            assertEquals(last, current);
            return true;
        };
        optimizer = builder.setAbsTolerance(1e-5).setRelTolerance(1e-5)
                .setConvergenceChecker(checker).build();
        function = new BracketedUnivariateFunctionAdapter(
                x -> Math.pow(x, 2) - 2, new SimpleBounds(-3, 3));
        result = optimizer.optimize(function);
        assertEquals(-1.4142135623730955, result.getPoint(), 1e-6);
        
        // Test with incorrect arguments.
        try {
            optimizer = new BrentOptimizer(-1e-5, 1e-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        
        try {
            optimizer = new BrentOptimizer(1e-5, -1e-5);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
        
        try {
            optimizer = new BrentOptimizer(1e-5, 1e-5, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            optimizer = new BrentOptimizer(1e-5, 1e-5, (ConvergenceChecker<MaxEval>) null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction)}.
     */
    @org.junit.Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        // Test with BracketedUnivariateFunction.
        UnivariateFunction function = new BracketedUnivariateFunctionAdapter(
                x -> Math.pow(x, 2) - 2, new SimpleBounds(-3, 3));
        UnivariatePointValuePair result = optimizer.optimize(function);
        assertEquals(-1.4142135623730955, result.getPoint(), 1e-6);

        // Test with UnivariateFunction that does not bracket minimum.
        function = x -> Math.pow(x, 2) - 5;
        result = optimizer.optimize(function);
        fail("Expected IllegalArgumentException, but got " + result);

        // Test with UnivariateFunction that does not converge.
        function = new BracketedUnivariateFunctionAdapter(
                x -> Math.sin(x) / x, new SimpleBounds(1, 10)) {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            result = optimizer.optimize(function);
            fail("Expected IllegalStateException, but got " + result);
        } catch (IllegalStateException e) {
            // expected
        }

        // Test with UnivariateFunction that exceptions.
        function = x -> {
            throw new IllegalArgumentException();
        };
        try {
            result = optimizer.optimize(function);
            fail("Expected IllegalArgumentException, but got " + result);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, GoalType)}.
     */
    @org.junit.Test
    public void testOptimizeGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);

        // Test with BracketedUnivariateFunction and GoalType.MINIMIZE.
        UnivariateFunction function = new BracketedUnivariateFunctionAdapter(
                x -> Math.pow(x, 2) - 2, new SimpleBounds(-3, 3));
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1.4142135623730955, result.getPoint(), 1e-6);

        // Test with BracketedUnivariateFunction and GoalType.MAXIMIZE.
        function = new BracketedUnivariateFunctionAdapter(x -> Math.pow(x, 2),
                new SimpleBounds(0, 2));
        result = optimizer.optimize(function, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getPoint(), 1e-6);

        // Test with UnivariateFunction that does not bracket minimum and GoalType.MINIMIZE.
        function = x -> Math.pow(x, 2) - 5;
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE);
            fail("Expected IllegalArgumentException, but got " + result);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with UnivariateFunction that does not converge and GoalType.MINIMIZE.
        function = new BracketedUnivariateFunctionAdapter(
                x -> Math.sin(x) / x, new SimpleBounds(1, 10)) {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE);
            fail("Expected IllegalStateException, but got " + result);
        } catch (IllegalStateException e) {
            // expected
        }

        // Test with UnivariateFunction that exceptions and GoalType.MINIMIZE.
        function = x -> {
            throw new IllegalArgumentException();
        };
        try {
            result = optimizer.optimize(function, GoalType.MINIMIZE);
            fail("Expected IllegalArgumentException, but got " + result);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with UnivariateFunction that exceptions and GoalType.MAXIMIZE.
        function = x -> {
            throw new IllegalArgumentException();
        };
        try {
            result = optimizer.optimize(function, GoalType.MAXIMIZE);
            fail("Expected IllegalArgumentException, but got " + result);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}