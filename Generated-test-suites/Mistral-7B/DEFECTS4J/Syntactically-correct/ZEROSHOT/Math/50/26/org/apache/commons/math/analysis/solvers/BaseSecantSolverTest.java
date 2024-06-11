package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestException;
import org.junit.Test;
import org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver_RegulaFalsi() {
        UnivariateRealFunction function = new FunctionUnderTest();
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        try {
            double root = solver.solve(10, function, -10.0, 10.0, 0.0);
            assertEquals(3.0, Precision.round(root, 3));
        } catch (NoBracketException e) {
            fail("Expected a solution, but no bracketing was found.");
        } catch (MaxIterationsExceededException e) {
            fail("Expected a solution, but the maximum number of iterations was exceeded.");
        }
    }

    @Test
    public void testBaseSecantSolver_Illinois() {
        UnivariateRealFunction function = new FunctionUnderTest();
        BaseSecantSolver solver = new IllinoisSolver(1e-6, new IllinoisSolver.IllinoisMethod());
        try {
            double root = solver.solve(10, function, -10.0, 10.0, 0.0);
            assertEquals(3.0, Precision.round(root, 3));
        } catch (NoBracketException e) {
            fail("Expected a solution, but no bracketing was found.");
        } catch (MaxIterationsExceededException e) {
            fail("Expected a solution, but the maximum number of iterations was exceeded.");
        }
    }

    @Test
    public void testBaseSecantSolver_Pegasus() {
        UnivariateRealFunction function = new FunctionUnderTest();
        BaseSecantSolver solver = new PegasusSolver(1e-6, new PegasusSolver.PegasusMethod());
        try {
            double root = solver.solve(10, function, -10.0, 10.0, 0.0);
            assertEquals(3.0, Precision.round(root, 3));
        } catch (NoBracketException e) {
            fail("Expected a solution, but no bracketing was found.");
        } catch (MaxIterationsExceededException e) {
            fail("Expected a solution, but the maximum number of iterations was exceeded.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBaseSecantSolver_NoAllowedSolution() {
        UnivariateRealFunction function = new FunctionUnderTest();
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod()) {
            @Override
            protected AllowedSolution getAllowedSolution() {
                return AllowedSolution.NO_SIDE;
            }
        };
        solver.solve(10, function, -10.0, 10.0, 0.0);
    }

}

class FunctionUnderTest implements UnivariateRealFunction {

    @Override
    public double value(double x) {
        return x * x - 3.0;
    }

}