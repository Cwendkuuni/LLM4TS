package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsFoundException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSimpleExample() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(function, -3.0, 3.0);
            assertEquals(0.0, root, 1e-6);
        } catch (NoRootsFoundException e) {
            fail("Expected a root to be found.");
        }
    }

    @Test(expected = NoRootsFoundException.class)
    public void testNoRootsFound() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };

        IllinoisSolver solver = new IllinoisSolver(1e-6);
        solver.solve(function, -5.0, 5.0);
    }

    @Test(expected = PrecisionException.class)
    public void testPrecisionException() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        IllinoisSolver solver = new IllinoisSolver(1e-12, 100);
        solver.solve(function, 0.0, 10.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) - Math.exp(-2 * x);
            }
            @Override
            public double derivative(double x) {
                return (-2 * Math.exp(-2 * x) + Math.exp(-x));
            }
        };

        IllinoisSolver solver = new IllinoisSolver(1e-6, 5);
        solver.solve(function, 0.0, 5.0);
    }

    @Test
    public void testAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.5, 2) - 0.25;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, AllowedSolution.LEFT_SIDE);
        try {
            double root = solver.solve(function, 0.0, 2.0);
            assertEquals(1.0, root, 1e-6);
        } catch (NoRootsFoundException e) {
            fail("Expected a root to be found on the left side.");
        }
    }

    @Test(expected = NoRootsFoundException.class)
    public void testAllowedSolutionRightSide() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.5, 2) - 0.25;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6, AllowedSolution.RIGHT_SIDE);
        solver.solve(function, 2.0, 3.0);
    }
}