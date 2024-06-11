package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    public void testBaseSecantSolver() {
        // Test RegulaFalsiSolver
        testSolver(new RegulaFalsiSolver(1e-6), testFunction1());
        testSolver(new RegulaFalsiSolver(1e-6, 10), testFunction1());
        testSolver(new RegulaFalsiSolver(1e-6, 10, 1e-6), testFunction1());
        testSolver(new RegulaFalsiSolver(0.1, 1e-6), testFunction1());
        testSolver(new RegulaFalsiSolver(0.1, 1e-6, 1e-6), testFunction1());
        testSolver(new RegulaFalsiSolver(0.1, 1e-6, 1e-6, 10), testFunction1());
        testSolver(new RegulaFalsiSolver(0.1, 1e-6, 1e-6, 10, 1000), testFunction1());

        // Test IllinoisSolver
        testSolver(new IllinoisSolver(1e-6), testFunction1());
        testSolver(new IllinoisSolver(1e-6, 10), testFunction1());
        testSolver(new IllinoisSolver(1e-6, 10, 1e-6), testFunction1());
        testSolver(new IllinoisSolver(0.1, 1e-6), testFunction1());
        testSolver(new IllinoisSolver(0.1, 1e-6, 1e-6), testFunction1());
        testSolver(new IllinoisSolver(0.1, 1e-6, 1e-6, 10), testFunction1());
        testSolver(new IllinoisSolver(0.1, 1e-6, 1e-6, 10, 1000), testFunction1());

        // Test PegasusSolver
        testSolver(new PegasusSolver(1e-6), testFunction1());
        testSolver(new PegasusSolver(1e-6, 10), testFunction1());
        testSolver(new PegasusSolver(1e-6, 10, 1e-6), testFunction1());
        testSolver(new PegasusSolver(0.1, 1e-6), testFunction1());
        testSolver(new PegasusSolver(0.1, 1e-6, 1e-6), testFunction1());
        testSolver(new PegasusSolver(0.1, 1e-6, 1e-6, 10), testFunction1());
        testSolver(new PegasusSolver(0.1, 1e-6, 1e-6, 10, 1000), testFunction1());
    }

    private UnivariateRealFunction testFunction1() {
        return new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
    }

    private void testSolver(BaseSecantSolver solver, UnivariateRealFunction function) {
        double x1 = 1.0;
        double x2 = -1.0;
        double root = 2.0;
        double tolerance = 1e-10;

        try {
            double solution = solver.solve(100, function, x1, x2, x1);
            assertEquals(root, solution, tolerance);
        } catch (NoBracketingException e) {
            // The solver should not throw NoBracketingException for this test case
            fail("NoBracketingException was thrown: " + e.getMessage());
        } catch (MaxIterationsExceededException e) {
            // The solver should not throw MaxIterationsExceededException for this test case
            fail("MaxIterationsExceededException was thrown: " + e.getMessage());
        } catch (PrecisionException e) {
            // The solver should throw PrecisionException if it doesn't find the root within the given tolerance
            assertEquals(Precision.SAFE_MIN, solver.getAbsoluteAccuracy(), 0.0);
            assertEquals(Precision.SAFE_MIN, solver.getRelativeAccuracy(), 0.0);
            assertEquals(Precision.SAFE_MIN, solver.getFunctionValueAccuracy(), 0.0);
            assertEquals(Math.abs(solver.getLastIncrement()), Math.abs(solver.getLastStepSize()), 0.0);
            assertTrue(Math.abs(solver.getLastFunctionValue()) < tolerance);
            assertTrue(Math.abs(solver.getLastObjectiveValue()) < tolerance);
        }
    }

}