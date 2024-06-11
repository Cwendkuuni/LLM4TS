package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception. PrecisionRequirementExceededException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private double tolerance;

    public BaseSecantSolverTest(double tolerance) {
        this.tolerance = tolerance;
    }

    @Parameterized.Parameters(name = "Tolerance = {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1e-6},
                {1e-8},
                {1e-10}
        });
    }

    @Before
    public void setUp() {
        BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY = tolerance;
    }

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateFunction f = f -> Math.pow(f, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(tolerance);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, tolerance);
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateFunction f = f -> Math.pow(f, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(tolerance);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, tolerance);
    }

    @Test
    public void testPegasusSolver() {
        UnivariateFunction f = f -> Math.pow(f, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(tolerance);
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, tolerance);
    }

    @Test
    public void testSecantSolver() {
        // The SecantSolver is not a bracketing method, it's not in the BaseSecantSolver class.
        UnivariateFunction f = f -> Math.pow(f, 2) - 1;
        SecantSolver solver = new SecantSolver(tolerance);
        assertFalse(solver instanceof BracketedUnivariateRealSolver);
        double root = solver.solve(f, -3.0);
        assertEquals(0.0, root, tolerance);
    }

    @Test
    public void testRegulaFalsiSolverException() {
        UnivariateFunction f = f -> Math.pow(f, 2);
        assertThrows(MaxIterationsExceededException.class, () -> new RegulaFalsiSolver(tolerance)
                .solve(f, -3.0, 3.0, 0));
    }

    @Test
    public void testIllinoisSolverException() {
        UnivariateFunction f = f -> Math.pow(f, 2);
        assertThrows(PrecisionRequirementExceededException.class, () -> new IllinoisSolver(tolerance)
                .solve(f, -3.0, 3.0, 0));
    }

    @Test
    public void testPegasusSolverException() {
        UnivariateFunction f = f -> Math.pow(f, 2);
        assertThrows(IllegalArgumentException.class, () -> new PegasusSolver(tolerance)
                .solve(f, -3.0, 3.0, 0));
    }
}