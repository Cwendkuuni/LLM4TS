package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * A function with roots at x = 1 and x = 3.
     */
    private static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return (x - 1) * (x - 3);
        }

        @Override
        public double derivative(double x) {
            return 2 * (x - 1) - 2 * (x - 3);
        }

        @Override
        public double root(double x) {
            if (Precision.abs(x - 1) < 1e-10 && Precision.abs(x - 3) < 1e-10) {
                return x;
            }
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test Regula Falsi method.
     */
    @Test
    public void testRegulaFalsi() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(
                epsAbs, epsRel, new MyFunction());
        try {
            double result = solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
            double[] expected = {2.0, 1.0};
            assertArrayEquals(expected, new double[]{1.0, 2.0},
                              "Wrong roots found.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[0] - 1),
                         "First bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[1] - 2),
                         "Second bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getSolution() - result),
                         "Solution is not correct.");
        } catch (NoBracketException e) {
            fail("Solver did not find a bracket.");
        } catch (MaxEvalExceededException e) {
            fail("Solver exceeded max evaluation.");
        }
    }

    /**
     * Test Illinois method.
     */
    @Test
    public void testIllinois() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new IllinoisSolver<>(
                epsAbs, epsRel, new MyFunction());
        try {
            double result = solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
            double[] expected = {2.0, 1.0};
            assertArrayEquals(expected, new double[]{1.0, 2.0},
                              "Wrong roots found.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[0] - 1),
                         "First bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[1] - 2),
                         "Second bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getSolution() - result),
                         "Solution is not correct.");
        } catch (NoBracketException e) {
            fail("Solver did not find a bracket.");
        } catch (MaxEvalExceededException e) {
            fail("Solver exceeded max evaluation.");
        }
    }

    /**
     * Test Pegasus method.
     */
    @Test
    public void testPegasus() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new PegasusSolver<>(
                epsAbs, epsRel, new MyFunction());
        try {
            double result = solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
            double[] expected = {2.0, 1.0};
            assertArrayEquals(expected, new double[]{1.0, 2.0},
                              "Wrong roots found.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[0] - 1),
                         "First bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getBracket()[1] - 2),
                         "Second bracket boundary is not correct.");
            assertEquals(Precision.SAFE_EPSILON,
                         Precision.abs(solver.getSolution() - result),
                         "Solution is not correct.");
        } catch (NoBracketException e) {
            fail("Solver did not find a bracket.");
        } catch (MaxEvalExceededException e) {
            fail("Solver exceeded max evaluation.");
        }
    }

    /**
     * Test exceptions with Regula Falsi method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRegulaFalsiException() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(
                epsAbs, epsRel, new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double root(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double derivative(double x) {
                        throw new UnsupportedOperationException();
                    }
                });
        solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
    }

    /**
     * Test exceptions with Illinois method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIllinoisException() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new IllinoisSolver<>(
                epsAbs, epsRel, new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double root(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double derivative(double x) {
                        throw new UnsupportedOperationException();
                    }
                });
        solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
    }

    /**
     * Test exceptions with Pegasus method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPegasusException() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new PegasusSolver<>(
                epsAbs, epsRel, new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double root(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double derivative(double x) {
                        throw new UnsupportedOperationException();
                    }
                });
        solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
    }

    /**
     * Test when no bracketing is found.
     */
    @Test(expected = NoBracketException.class)
    public void testNoBracket() {
        double maxEval = 100;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(
                epsAbs, epsRel, new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return x * Math.sin(Math.PI * x);
                    }

                    @Override
                    public double root(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double derivative(double x) {
                        return Math.cos(Math.PI * x) + x * Math.cos(Math.PI * x);
                    }
                });
        solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
    }

    /**
     * Test when max evaluation is exceeded.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        double maxEval = 1;
        double epsAbs = 1e-6;
        double epsRel = 1e-6;
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver<>(
                epsAbs, epsRel, new UnivariateRealFunction() {
                    @Override
                    public double value(double x) {
                        return Math.sin(Math.PI * x);
                    }

                    @Override
                    public double root(double x) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public double derivative(double x) {
                        return -Math.cos(Math.PI * x);
                    }
                });
        solver.solve(maxEval, new MyFunction(), 0.0, 5.0);
    }
}