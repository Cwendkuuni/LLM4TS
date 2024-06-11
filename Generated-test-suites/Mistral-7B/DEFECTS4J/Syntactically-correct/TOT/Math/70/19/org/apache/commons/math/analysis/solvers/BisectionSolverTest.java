package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRecalledException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverWithSimpleFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };

        BisectionSolver solver = new BisectionSolver();
        try {
            double root = solver.solve(f, -2.5, 2.5);
            assertEquals("The calculated root is not correct.", -1.0, root, 1e-6);
        } catch (MaxIterationsExceededException e) {
            fail("Bisection Solver should have found the root within the given iterations.");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverWithFunctionThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException("Exception message");
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBisectionSolverWithRootTooClose() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        solver.solve(f, 0.0, 0.0);
    }

    @Test(expected = PrecisionRecalledException.class)
    public void testBisectionSolverWithPrecisionRecalled() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, Math.sin(x));
            }
        };

        BisectionSolver solver = new BisectionSolver();
        solver.setAbsoluteAccuracy(1e-15);
        solver.solve(f, 0.0, 1.0);
    }
}