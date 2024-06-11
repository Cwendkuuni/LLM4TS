package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalArgumentException;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link RegulaFalsiSolver}.
     */
    @org.junit.Test
    public void testRegulaFalsiSolver() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        UnivariateFunction function = new PolynomialFunction(2, -1.0);
        double root = 1.0;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(100, function, min, max, startValue, allowedSolution);
        assertEquals(root, solution, absoluteAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link RegulaFalsiSolver} with invalid input.
     */
    @org.junit.Test
    public void testRegulaFalsiSolverInvalidInput() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = null;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;

        assertThrows(MathIllegalArgumentException.class, () -> new RegulaFalsiSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy));

        assertThrows(MathIllegalArgumentException.class, () -> new RegulaFalsiSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(10, function, min, max, startValue, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link IllinoisSolver}.
     */
    @org.junit.Test
    public void testIllinoisSolver() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        UnivariateFunction function = new PolynomialFunction(2, -1.0);
        double root = 1.0;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(100, function, min, max, startValue, allowedSolution);
        assertEquals(root, solution, absoluteAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link IllinoisSolver} with invalid input.
     */
    @org.junit.Test
    public void testIllinoisSolverInvalidInput() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = null;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;

        assertThrows(MathIllegalArgumentException.class, () -> new IllinoisSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy));

        assertThrows(MathIllegalArgumentException.class, () -> new IllinoisSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(10, function, min, max, startValue, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link PegasusSolver}.
     */
    @org.junit.Test
    public void testPegasusSolver() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        UnivariateFunction function = new PolynomialFunction(2, -1.0);
        double root = 1.0;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(100, function, min, max, startValue, allowedSolution);
        assertEquals(root, solution, absoluteAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link PegasusSolver} with invalid input.
     */
    @org.junit.Test
    public void testPegasusSolverInvalidInput() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = null;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;

        assertThrows(MathIllegalArgumentException.class, () -> new PegasusSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy));

        assertThrows(MathIllegalArgumentException.class, () -> new PegasusSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(10, function, min, max, startValue, AllowedSolution.ANY_SIDE));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link SecantSolver} with invalid input.
     */
    @org.junit.Test
    public void testSecantSolverInvalidInput() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = null;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;

        assertThrows(MathIllegalArgumentException.class, () -> new SecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy));

        assertThrows(ConvergenceException.class, () -> new SecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(10, function, min, max, startValue));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link SecantSolver} with invalid maxEval.
     */
    @org.junit.Test
    public void testSecantSolverMaxEvalInvalid() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = (x) -> Math.pow(x, 2) - 1.0;
        double min = -2.0;
        double max = 2.0;
        double startValue = 1.5;

        assertThrows(IllegalArgumentException.class, () -> new SecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(-1, function, min, max, startValue));

        assertThrows(IllegalArgumentException.class, () -> new SecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(0, function, min, max, startValue));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link SecantSolver} with an unbracketed function.
     */
    @org.junit.Test
    public void testSecantSolverUnbracketed() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        UnivariateFunction function = (x) -> Math.pow(x, 2);

        assertThrows(UnsupportedOperationException.class, () -> new SecantSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy).solve(100, function, 1.0, 1.0, 1.5));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link BaseSecantSolver} with an exact root.
     */
    @org.junit.Test
    public void testBaseSecantSolverExactRootMin() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        UnivariateFunction function = (x) -> x - 1.0;
        double root = 1.0;
        double min = root;
        double max = root + 1e-12;
        double startValue = root;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(100, function, min, max, startValue, allowedSolution);
        assertEquals(root, solution, absoluteAccuracy);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * using the {@link BaseSecantSolver} with an exact root.
     */
    @org.junit.Test
    public void testBaseSecantSolverExactRootMax() {
        double relativeAccuracy = 1e-5;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-8;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(
            relativeAccuracy, absoluteAccuracy, functionValueAccuracy);

        UnivariateFunction function = (x) -> x - 1.0;
        double root = 1.0;
        double min = root - 1e-12;
        double max = root;
        double startValue = root;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double solution = solver.solve(100, function, min, max, startValue, allowedSolution);
        assertEquals(root, solution, absoluteAccuracy);
    }
}