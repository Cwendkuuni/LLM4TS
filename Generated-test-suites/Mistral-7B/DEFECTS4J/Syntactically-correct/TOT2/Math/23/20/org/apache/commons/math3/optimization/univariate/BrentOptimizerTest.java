package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_Minimization() {
        // Testing minimization with a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new SquareFunction();
        PointValuePair result = optimizer.optimize(function, 0, 5);
        assertEquals(2.0, result.getKey().doubleValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_Minimization_InitialGuess() {
        // Testing minimization with a simple function and initial guess
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new SquareFunction();
        PointValuePair initialGuess = new PointValuePair(3.0, 9.0);
        PointValuePair result = optimizer.optimize(function, initialGuess);
        assertEquals(2.0, result.getKey().doubleValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_Minimization_InvalidRelThreshold() {
        // Testing exception with invalid relative threshold
        new BrentOptimizer(0.0, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_Minimization_InvalidAbsThreshold() {
        // Testing exception with invalid absolute threshold
        new BrentOptimizer(1e-6, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizer_Minimization_InvalidConvergenceChecker() {
        // Testing exception with invalid convergence checker
        ConvergenceChecker<PointValuePair> invalidChecker = new Object();
        new BrentOptimizer(1e-6, 1e-6, invalidChecker);
    }

    @Test
    public void testBrentOptimizer_Maximization() {
        // Testing maximization with a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        FunctionFunction function = new SquareFunction();
        PointValuePair result = optimizer.optimize(function, 0, 5);
        assertEquals(5.0, result.getKey().doubleValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_Maximization_InitialGuess() {
        // Testing maximization with a simple function and initial guess
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        FunctionFunction function = new SquareFunction();
        PointValuePair initialGuess = new PointValuePair(3.0, 9.0);
        PointValuePair result = optimizer.optimize(function, initialGuess);
        assertEquals(5.0, result.getKey().doubleValue(), 1e-6);
    }

    @Test
    public void testBrentOptimizer_Minimization_ConvergenceChecker() {
        // Testing custom convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new FunctionWithCustomChecker();
        ConvergenceChecker<PointValuePair> customChecker = new Object();
        optimizer.setConvergenceChecker(customChecker);
        PointValuePair result = optimizer.optimize(function, 0, 10);
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizer_Maximization_ConvergenceChecker() {
        // Testing custom convergence checker for maximization
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        FunctionFunction function = new FunctionWithCustomChecker();
        ConvergenceChecker<PointValuePair> customChecker = new Object();
        optimizer.setConvergenceChecker(customChecker);
        PointValuePair result = optimizer.optimize(function, 0, 10);
        assertNotNull(result);
    }

    @Test
    public void testBrentOptimizer_Minimization_OutOfBound() {
        // Testing minimization with an out-of-bound function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        FunctionFunction function = new OutOfBoundFunction();
        try {
            optimizer.optimize(function, 0, 5);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    public void testBrentOptimizer_Maximization_OutOfBound() {
        // Testing maximization with an out-of-bound function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        FunctionFunction function = new OutOfBoundFunction();
        try {
            optimizer.optimize(function, 0, 5);
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }
}

class FunctionFunction implements UnivariateFunction {
    public double value(double x) {
        return x * x;
    }
}

class SquareFunction implements UnivariateFunction {
    public double value(double x) {
        return x * x;
    }
}

class FunctionWithCustomChecker implements UnivariateFunction {
    public double value(double x) {
        return Math.sin(Math.PI * x) / (1 + x * x);
    }
}

class OutOfBoundFunction implements UnivariateFunction {
    public double value(double x) {
        throw new IllegalArgumentException("Function not defined for this x.");
    }
}