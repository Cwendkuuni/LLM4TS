package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.PowellOptimizer;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for the Powell Optimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test that the Powell Optimizer correctly finds the minimum of a simple
     * quadratic function.
     */
    @org.junit.Test
    public void testQuadratic() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 4, 1});
        PointValuePair expected = new PointValuePair(new double[]{1.0}, 1.0);

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), null);
        assertEquals(expected.getPoint(), Precision.round(result.getPoint()[0], 5));
        assertEquals(expected.getValue(), Precision.round(result.getValue(), 5));
    }

    /**
     * Test that the Powell Optimizer correctly finds the minimum of a simple
     * polynomial function with a linear constraint.
     */
    @org.junit.Test
    public void testPolynomialWithLinearConstraint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 2, 1, 3});
        LinearConstraint constraint = new LinearConstraint(1, 2, 2, 0);
        PointValuePair expected = new PointValuePair(new double[]{1.0}, 0.0);

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint});
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), constraints);
        assertEquals(expected.getPoint(), Precision.round(result.getPoint()[0], 5));
        assertEquals(expected.getValue(), Precision.round(result.getValue(), 5));
    }

    /**
     * Test that the Powell Optimizer correctly finds the minimum of a simple
     * linear objective function with linear constraints.
     */
    @org.junit.Test
    public void testLinearWithLinearConstraints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new LinearObjectiveFunction(new double[]{-1, 2, 1}, new double[]{1, 2});
        LinearConstraint constraint1 = new LinearConstraint(1, 1, 0, 0);
        LinearConstraint constraint2 = new LinearConstraint(1, 2, 0, 2);
        PointValuePair expected = new LinearPointValuePair(new double[]{1.0, 1.0}, 1.0);

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint1, constraint2});
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{2.0, 2.0}), constraints);
        assertEquals(expected.getPoint(), result.getPoint(), 0.0);
        assertEquals(expected.getValue(), result.getValue(), 0.0);
    }

    /**
     * Test that the Powell Optimizer correctly finds the minimum of a simple
     * objective function with a non-linear constraint.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testNonlinearConstraint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 4, 1});
        UnivariateFunction g = new PolynomialFunction(new double[]{3, -1, 1});
        PointValuePair expected = null;

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{});
        try {
            PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), new LinearConstraint(g, 1e-5));
            fail("Expected UnsupportedOperationException but got " + result);
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
        assertNull(expected);
    }

    /**
     * Test that the Powell Optimizer correctly handles user-defined
     * convergence checker.
     */
    @org.junit.Test
    public void testUserDefinedConvergenceChecker() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5,
                new PowellOptimizer.CustomConvergenceChecker());

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 4, 1});
        PointValuePair expected = new PointValuePair(new double[]{1.0}, 1.0);

        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), null);
        assertEquals(expected.getPoint(), Precision.round(result.getPoint()[0], 5));
        assertEquals(expected.getValue(), Precision.round(result.getValue(), 5));
    }

    /**
     * Test that the Powell Optimizer correctly handles user-defined convergence
     * checker and linear constraints.
     */
    @org.junit.Test
    public void testUserDefinedConvergenceCheckerWithLinearConstraints() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5,
                new PowellOptimizer.CustomConvergenceCheckerWithLinearConstraints());

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 2, 1, 3});
        LinearConstraint constraint = new LinearConstraint(1, 2, 2, 0);
        PointValuePair expected = new PointValuePair(new double[]{1.0}, 0.0);

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[]{constraint});
        PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), constraints);
        assertEquals(expected.getPoint(), Precision.round(result.getPoint()[0], 5));
        assertEquals(expected.getValue(), Precision.round(result.getValue(), 5));
    }

    /**
     * Test that the Powell Optimizer correctly handles user-defined convergence
     * checker and non-linear constraints.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testUserDefinedConvergenceCheckerWithNonlinearConstraint() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5,
                new PowellOptimizer.CustomConvergenceCheckerWithNonlinearConstraints());

        UnivariateFunction f = new PolynomialFunction(new double[]{-1, 4, 1});
        UnivariateFunction g = new PolynomialFunction(new double[]{3, -1, 1});
        PointValuePair expected = null;

        try {
            PointValuePair result = optimizer.optimize(f, new SimpleBounds(0.0, 2.0), new LinearConstraint(g, 1e-5));
            fail("Expected UnsupportedOperationException but got " + result);
        } catch (UnsupportedOperationException ex) {
            // Expected
        }
        assertNull(expected);
    }

    /**
     * Custom convergence checker that always returns true.
     */
    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return true;
        }
    }

    /**
     * Custom convergence checker with linear constraints that always returns true.
     */
    private static class CustomConvergenceCheckerWithLinearConstraints implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return true;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current, LinearConstraintSet constraints) {
            return true;
        }
    }

    /**
     * Custom convergence checker with non-linear constraints that always returns true.
     */
    private static class CustomConvergenceCheckerWithNonlinearConstraints implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return true;
        }

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current, UnivariateFunction constraint) {
            return true;
        }
    }

}