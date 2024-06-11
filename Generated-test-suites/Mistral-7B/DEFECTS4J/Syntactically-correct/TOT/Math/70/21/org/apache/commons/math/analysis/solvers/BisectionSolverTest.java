package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverSimple() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, -1.0, 1.0);
            assertTrue(Precision.isClose(root, 0.0, 1e-3));
        } catch (MaxIterationsExceededException e) {
            fail("Bisection solver should find root in given interval");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception");
        }
    }

    @Test
    public void testBisectionSolverEdgeCases() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / (Math.PI * x);
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            double root = solver.solve(function, 0.0, 0.0);
            fail("Bisection solver should not find root at zero");
        } catch (MaxIterationsExceededException e) {
            // expected
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception");
        } catch (NoRootsException e) {
            // expected
        }

        try {
            double root = solver.solve(function, 0.0, 1.0);
            assertTrue(Precision.isClose(root, 1.0 / Math.PI, 1e-3));
        } catch (MaxIterationsExceededException e) {
            fail("Bisection solver should find root in given interval");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception");
        }
    }

    @Test
    public void testBisectionSolverErrorScenarios() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Function evaluation exception");
            }
        };
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(function, 0.0, 1.0);
            fail("Function evaluation exception should be propagated");
        } catch (FunctionEvaluationException e) {
            // expected
        }

        UnivariateRealFunction function1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / (x - 1.0);
            }
        };
        try {
            solver.solve(function1, 0.0, 1.0);
            fail("Function should not be defined at x = 1.0");
        } catch (ZeroException e) {
            // expected
        } catch (PrecisionException e) {
            fail("Bisection solver should not throw precision exception");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not throw an exception");
        }
    }
}