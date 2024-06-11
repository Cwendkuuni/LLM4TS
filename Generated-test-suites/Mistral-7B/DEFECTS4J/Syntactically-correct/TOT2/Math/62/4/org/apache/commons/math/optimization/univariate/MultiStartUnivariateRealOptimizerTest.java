package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeValue;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMinimizationWithSingleStart() {
        UnivariateRealFunction func = new FunctionWithRootsAndMinimum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        PointValuePair result = null;
        try {
            result = optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
        } catch (MaxEvalExceededException e) {
            fail("Expected to find minimum, but the optimizer did not converge.");
        }
        assertEquals(-1.0, result.getPoint().getDouble(), 0.001);
        assertEquals(-1.4142137080363835E-14, result.getValue(), 0.0);
    }

    @Test
    public void testMinimizationWithMultipleStarts() {
        UnivariateRealFunction func = new FunctionWithRootsAndMinimum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 3, new RandomValuesGenerator());
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                assertEquals(-1.0, pair.getPoint().getDouble(), 0.001);
                assertEquals(-1.4142137080363835E-14, pair.getValue(), 0.0);
            } else {
                assertNull(pair);
            }
        }
    }

    @Test
    public void testMinimizationWithMaxEvaluations() {
        UnivariateRealFunction func = new FunctionWithRootsAndMinimum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        optimizer.setMaxEvaluations(3);
        try {
            optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
            fail("Expected ConvergenceException, but the optimizer did not throw it.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testMinimizationWithNoRoots() {
        UnivariateRealFunction func = new FunctionWithNoRoots();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        try {
            optimizer.optimize(func, GoalType.MINIMIZE, -10.0, 10.0);
            fail("Expected NoRootFoundException, but the optimizer did not throw it.");
        } catch (NoRootFoundException e) {
            // Expected
        }
    }

    @Test
    public void testMaximizationWithSingleStart() {
        UnivariateRealFunction func = new FunctionWithMaximum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        PointValuePair result = null;
        try {
            result = optimizer.optimize(func, GoalType.MAXIMIZE, -10.0, 10.0);
        } catch (MaxEvalExceededException e) {
            fail("Expected to find maximum, but the optimizer did not converge.");
        }
        assertEquals(1.0, result.getPoint().getDouble(), 0.001);
        assertEquals(1.4142137080363835E-14, result.getValue(), 0.0);
    }

    @Test
    public void testMaximizationWithMultipleStarts() {
        UnivariateRealFunction func = new FunctionWithMaximum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 3, new RandomValuesGenerator());
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                assertEquals(1.0, pair.getPoint().getDouble(), 0.001);
                assertEquals(1.4142137080363835E-14, pair.getValue(), 0.0);
            } else {
                assertNull(pair);
            }
        }
    }

    @Test
    public void testMaximizationWithMaxEvaluations() {
        UnivariateRealFunction func = new FunctionWithMaximum();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        optimizer.setMaxEvaluations(3);
        try {
            optimizer.optimize(func, GoalType.MAXIMIZE, -10.0, 10.0);
            fail("Expected ConvergenceException, but the optimizer did not throw it.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testMaximizationWithNoMaxima() {
        UnivariateRealFunction func = new FunctionWithNoMaxima();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        try {
            optimizer.optimize(func, GoalType.MAXIMIZE, -10.0, 10.0);
            fail("Expected Exception, but the optimizer did not throw it.");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testGoalTypeMixed() {
        UnivariateRealFunction func = new FunctionWithMixedGoals();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        PointValuePair result = null;
        try {
            result = optimizer.optimize(func, new GoalTypeWeightedSum(0.5, GoalType.MINIMIZE, 0.5, GoalType.MAXIMIZE), -10.0, 10.0);
        } catch (MaxEvalExceededException e) {
            fail("Expected to find minimum and maximum, but the optimizer did not converge.");
        }
        assertEquals(1.0, result.getPoint().getDouble(), 0.001);
        assertEquals(1.4142137080363835E-14, result.getValue(), 0.0);
    }

    @Test
    public void testGoalTypeMixedWithMultipleStarts() {
        UnivariateRealFunction func = new FunctionWithMixedGoals();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 3, new RandomValuesGenerator());
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        for (UnivariateRealPointValuePair pair : optima) {
            if (pair != null) {
                assertTrue(pair.getValue() > 0.0);
                assertTrue(pair.getValue() < 2.0);
            } else {
                assertNull(pair);
            }
        }
    }

    @Test
    public void testGoalTypeMixedWithMaxEvaluations() {
        UnivariateRealFunction func = new FunctionWithMixedGoals();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        optimizer.setMaxEvaluations(3);
        try {
            optimizer.optimize(func, new GoalTypeValue(GoalType.MAXIMIZE), -10.0, 10.0);
            fail("Expected ConvergenceException, but the optimizer did not throw it.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testGoalTypeMixedWithNoOptima() {
        UnivariateRealFunction func = new FunctionWithNoMixedGoals();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(
                        new FibonacciSearchOptimizer<>(), 1, new RandomValuesGenerator());
        try {
            optimizer.optimize(func, new GoalTypeValue(GoalType.MAXIMIZE), -10.0, 10.0);
            fail("Expected Exception, but the optimizer did not throw it.");
        } catch (Exception e) {
            // Expected
        }
    }

    private static class FunctionWithRootsAndMinimum implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (x == 0) {
                throw new FunctionEvaluationException("Function has a root at x = 0");
            }
            return x * (x - 1) - 0.1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 1;
        }
    }

    private static class FunctionWithMaximum implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    private static class FunctionWithNoRoots implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) / x;
        }

        @Override
        public double derivative(double x) {
            return (Math.cos(x) / x - Math.sin(x) / (x * x)) / x;
        }
    }

    private static class FunctionWithMixedGoals implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.sin(x) / (x * x) + Math.abs(Math.cos(x));
        }

        @Override
        public double derivative(double x) {
            return ((-Math.cos(x) / (x * x * x) - 3 * Math.sin(x) / (x * x)) / x)
                    + Math.cos(x) / x;
        }
    }

    private static class FunctionWithNoMixedGoals implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double derivative(double x) {
            throw new UnsupportedOperationException();
        }
    }
}