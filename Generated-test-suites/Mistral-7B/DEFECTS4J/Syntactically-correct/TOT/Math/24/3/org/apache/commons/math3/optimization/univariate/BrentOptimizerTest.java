package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test function {@code Math.pow(x, 2)} with {@code x >= 0}.
     */
    @Test
    public void testBrentOptimizerForPositiveX() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x, 2);
        PointValuePair result = optimizer.optimize(f, new InitialGuess(2.0), GoalType.MINIMIZE);
        assertEquals("Expected minimum is 4", 4.0, result.getPoint(), 1e-6);
    }

    /**
     * Test function {@code Math.pow(x, 2)} with {@code x < 0}.
     */
    @Test
    public void testBrentOptimizerForNegativeX() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.pow(x, 2);
        try {
            optimizer.optimize(f, new InitialGuess(-2.0), GoalType.MINIMIZE);
            fail("Expected NumberIsDefinedForNegativeException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test function {@code Math.abs(x)} with {@code x = 0}.
     */
    @Test
    public void testBrentOptimizerForZeroX() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.abs(x);
        try {
            optimizer.optimize(f, new InitialGuess(0.0), GoalType.MINIMIZE);
            fail("Expected NumberIsDefinedForZeroException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test function {@code Math.sin(x)} with {@code x = 0}.
     */
    @Test
    public void testBrentOptimizerForSinXZero() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.sin(x);
        try {
            optimizer.optimize(f, new InitialGuess(0.0), GoalType.MINIMIZE);
            fail("Expected NumberIsDefinedForZeroException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test function {@code Math.sin(x)} with {@code x = 1.57 (pi)}.
     */
    @Test
    public void testBrentOptimizerForPi() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.sin(x);
        PointValuePair result = optimizer.optimize(f, new InitialGuess(1.57), GoalType.MINIMIZE);
        assertEquals("Expected minimum is 0", 0.0, result.getPoint(), 1e-6);
    }

    /**
     * Test function {@code Math.log(x)} with {@code x = e}.
     */
    @Test
    public void testBrentOptimizerForE() {
        UnivariateOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariateFunction f = x -> Math.log(x);
        PointValuePair result = optimizer.optimize(f, new InitialGuess(Math.E), GoalType.MINIMIZE);
        assertEquals("Expected minimum is 0", 0.0, result.getPoint(), 1e-6);
    }
}