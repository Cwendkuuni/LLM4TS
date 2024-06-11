package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonSmoothedPoint;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.initialization.RandomStart;
import org.apache.commons.math3.optim.initialization.AverageStart;
import org.apache.commons.math3.optim.initialization.CentroidStart;
import org.apache.commons.math3.optim.initialization.MinMaxStart;
import org.apache.commons.math3.optim.initialization.TriDiagonalStart;
import org.apache.commons.math3.optim.initialization.SquareStart;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValueFunctionStats;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test function f(x) = x^2.
     */
    @Test
    public void testSimpleFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };

        double[] initPoint = {3.0};
        PointValuePair result = optimizer.optimize(function, initPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertEquals(0.0, result.getPoint()[0], 1e-6);

        // Test error handling for invalid arguments.
        optimizer = new PowellOptimizer(-1.0, 1e-6);
        try {
            result = optimizer.optimize(function, initPoint);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        optimizer = new PowellOptimizer(1e-6, -1.0);
        try {
            result = optimizer.optimize(function, initPoint);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test function f(x,y) = x^2 + y^2.
     */
    @Test
    public void testMultiVariableFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, 1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };

        double[] initPoint = {3.0, 2.0};
        PointValuePair result = optimizer.optimize(function, initPoint, null);
        assertEquals(9.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test error handling for invalid arguments.
        optimizer = new PowellOptimizer(-1.0, 1e-6, 1e-6, 1e-6);
        try {
            result = optimizer.optimize(function, initPoint, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        optimizer = new PowellOptimizer(1e-6, -1.0, 1e-6, 1e-6);
        try {
            result = optimizer.optimize(function, initPoint, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        optimizer = new PowellOptimizer(1e-6, 1e-6, -1.0, 1e-6);
        try {
            result = optimizer.optimize(function, initPoint, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        optimizer = new PowellOptimizer(1e-6, 1e-6, 1e-6, -1.0);
        try {
            result = optimizer.optimize(function, initPoint, null);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    /**
     * Test PowellOptimizer with different initialization methods.
     */
    @Test
    public void testDifferentInitializationMethods() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };

        // Test using random initialization.
        InitialGuess initialGuess = new RandomStart(2);
        PointValuePair result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test using average initialization.
        initialGuess = new AverageStart(2);
        result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test using centroid initialization.
        initialGuess = new CentroidStart(2, 2);
        result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test using min-max initialization.
        initialGuess = new MinMaxStart(2, 2);
        result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test using tri-diagonal initialization.
        initialGuess = new TriDiagonalStart(2);
        result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);

        // Test using square initialization.
        initialGuess = new SquareStart(2, 2);
        result = optimizer.optimize(function, null, initialGuess);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y;
            }
        };

        class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                if (current.getValue() < 1e-4) {
                    return true;
                }
                return false;
            }
        }

        CustomConvergenceChecker customCheck = new CustomConvergenceChecker();
        PointValuePair result = optimizer.optimize(function, null, null, customCheck);
        assertArrayNotEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(1e-4, result.getValue(), 1e-4);
    }

    /**
     * Test PowellOptimizer with custom objective function.
     */
    @Test
    public void testCustomObjectiveFunction() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        double[] initPoint = {3.0, 2.0};
        PointValuePair result = optimizer.optimize(objectiveFunction, initPoint, null);
        assertEquals(10.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom objective function and custom initialization.
     */
    @Test
    public void testCustomObjectiveFunctionAndCustomInitialization() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        InitialGuess initialGuess = new CentroidStart(2, 2);
        PointValuePair result = optimizer.optimize(objectiveFunction, null, initialGuess);
        assertEquals(Math.sqrt(5.0) * Math.sqrt(5.0) + 1, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-Math.sqrt(5.0) / 2.0, Math.sqrt(5.0) / 2.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom objective function and custom convergence checker.
     */
    @Test
    public void testCustomObjectiveFunctionAndCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
            public boolean converged(int iter, PointValuePair start, PointValuePair current) {
                if (current.getValue() < 1e-4) {
                    return true;
                }
                return false;
            }
        }

        CustomConvergenceChecker customCheck = new CustomConvergenceChecker();
        PointValuePair result = optimizer.optimize(objectiveFunction, null, null, customCheck);
        assertEquals(10.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom objective function, custom initialization, and custom convergence checker.
     */
    @Test
    public void testCustomObjectiveFunctionAndCustomInitializationAndCustomConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        InitialGuess initialGuess = new CentroidStart(2, 2);
        ConvergenceChecker<PointValuePair> customCheck = new CustomConvergenceChecker();
        PointValuePair result = optimizer.optimize(objectiveFunction, null, initialGuess, customCheck);
        assertEquals(Math.sqrt(5.0) * Math.sqrt(5.0) + 1, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-Math.sqrt(5.0) / 2.0, Math.sqrt(5.0) / 2.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom objective function, custom initialization, and custom convergence checker using MaxEval.
     */
    @Test
    public void testCustomObjectiveFunctionAndCustomInitializationAndCustomConvergenceCheckerUsingMaxEval() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        InitialGuess initialGuess = new CentroidStart(2, 2);
        ConvergenceChecker<PointValuePair> customCheck = new CustomConvergenceChecker();
        PointValuePair result = optimizer.optimize(objectiveFunction, null, initialGuess, null, null, new MaxEval(100));
        assertEquals(Math.sqrt(5.0) * Math.sqrt(5.0) + 1, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-Math.sqrt(5.0) / 2.0, Math.sqrt(5.0) / 2.0}, result.getPoint(), 1e-6);
    }

    /**
     * Test PowellOptimizer with custom objective function, custom initialization, and custom convergence checker using PointValueFunctionStats.
     */
    @Test
    public void testCustomObjectiveFunctionAndCustomInitializationAndCustomConvergenceCheckerUsingPointValueFunctionStats() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        PointValueFunction objectiveFunction = new PointValueFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                return x * x + y * y + 1;
            }
        };

        InitialGuess initialGuess = new CentroidStart(2, 2);
        ConvergenceChecker<PointValuePair> customCheck = new CustomConvergenceChecker();
        PointValueFunctionStats stats = new PointValueFunctionStats();
        PointValuePair result = optimizer.optimize(objectiveFunction, null, initialGuess, null, stats);
        assertEquals(Math.sqrt(5.0) * Math.sqrt(5.0) + 1, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-Math.sqrt(5.0) / 2.0, Math.sqrt(5.0) / 2.0}, result.getPoint(), 1e-6);
        assertEquals(1, stats.getNumberOfEvaluations());
    }
}