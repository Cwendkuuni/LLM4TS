package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LevenbergMarquardtIterator;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.preconditions.IdentityPreconditioner;
import org.apache.commons.math3.optim.preconditions.Preconditioner;
import org.junit.Test;
import org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testMinimization() {
        // Define function to optimize
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 2.0, 2.0);
                }
                return sum;
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = 2.0 * (x[i] - 2.0);
                }
                return grad;
            }
        };

        // Initialize optimizer
        Preconditioner preconditioner = new IdentityPreconditioner();
        Optimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                preconditioner);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(1000);

        // Optimize
        double[] startPoint = {3.0, 3.0, 3.0};
        PointValuePair solution = optimizer.optimize(testFunction, startPoint);

        // Check results
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(2.0, solution.getPoint()[1], 1e-6);
        assertEquals(2.0, solution.getPoint()[2], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testInvalidUpdateFormula() {
        // Initialize optimizer with invalid update formula
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.INVALID);

        // Attempt to optimize
        UnivariateFunction testFunction = new TestFunction();
        double[] startPoint = {0.0};
        optimizer.optimize(testFunction, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPreconditioner() {
        // Initialize optimizer with null preconditioner
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                null);

        // Attempt to optimize
        UnivariateFunction testFunction = new TestFunction();
        double[] startPoint = {0.0};
        optimizer.optimize(testFunction, startPoint);
    }

    @Test
    public void testMaxEvaluations() {
        // Define function to optimize
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = 2.0 * (x[i] - 2.0);
                }
                return grad;
            }
        };

        // Initialize optimizer
        Preconditioner preconditioner = new IdentityPreconditioner();
        Optimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                preconditioner);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(1);

        // Optimize
        double[] startPoint = {3.0, 3.0, 3.0};
        PointValuePair solution = optimizer.optimize(testFunction, startPoint);

        // Check results
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(solution);
    }

    @Test
    public void testLineSearchFailure() {
        // Define function to optimize
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = -(1.0 / (Math.PI * x[i] * Math.PI * x[i]) + 1.0 / (Math.PI * x[i]));
                }
                return grad;
            }
        };

        // Initialize optimizer
        Preconditioner preconditioner = new IdentityPreconditioner();
        Optimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(100);

        // Attempt to optimize with a function whose root is not bracketed
        double[] startPoint = {0.01, 0.01};
        PointValuePair solution = optimizer.optimize(testFunction, startPoint);

        // Check results
        assertNull(solution);
    }

    @Test
    public void testConvergenceChecker() {
        // Define function to optimize
        UnivariateFunction testFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    grad[i] = 2.0 * (x[i] - 2.0);
                }
                return grad;
            }
        };

        // Initialize optimizer
        Preconditioner preconditioner = new IdentityPreconditioner();
        Optimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                preconditioner);
        optimizer.setInitialStep(1.0);
        optimizer.setMaxEvaluations(100);

        // Attempt to optimize with a convergence checker that throws an exception
        Optimizer optimizerWithBadChecker = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new MyBadValueChecker(),
                new BrentSolver(),
                preconditioner);
        optimizerWithBadChecker.setInitialStep(1.0);
        optimizerWithBadChecker.setMaxEvaluations(100);

        double[] startPoint = {3.0, 3.0, 3.0};
        try {
            optimizerWithBadChecker.optimize(testFunction, startPoint);
            fail("An exception should have been thrown.");
        } catch (Exception e) {
            // Expected
        }

        // Optimize with the original optimizer
        PointValuePair solution = optimizer.optimize(testFunction, startPoint);

        // Check results
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(2.0, solution.getPoint()[0], 1e-6);
        assertEquals(2.0, solution.getPoint()[1], 1e-6);
        assertEquals(2.0, solution.getPoint()[2], 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    /**
     * Convergence checker that throws an exception.
     */
    private static class MyBadValueChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
            throw new RuntimeException("Test failure.");
        }
    }

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x) - 1.0, 2.0);
        }

        @Override
        public double[] gradient(double[] x) {
            double[] grad = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                grad[i] = -2.0 * Math.sin(x[i]) * (1.0 - Math.cos(x[i]));
            }
            return grad;
        }
    }
}