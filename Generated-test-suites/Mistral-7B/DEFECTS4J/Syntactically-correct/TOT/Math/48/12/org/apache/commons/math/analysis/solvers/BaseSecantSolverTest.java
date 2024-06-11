package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtil;
import org.apache.commons.math3.util.UnivariateFunctionUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test a simple function with known roots.
     */
    @Test
    public void testSimpleFunction() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 1.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double root = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
        assertEquals(0.0, root, absoluteAccuracy);
    }

    /**
     * Test a simple function with known roots, but with one boundary condition
     * that results in an error.
     */
    @Test
    public void testSimpleFunctionError() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double min = 2.0;
        final double max = 2.0;
        final double startValue = 1.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        assertThrows(ConvergenceException.class, () -> solver.solve(maxEval, function, min, max, startValue, allowedSolution));
    }

    /**
     * Test a function with a single root that is near a boundary.
     */
    @Test
    public void testRootNearBoundary() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double min = -3.0;
        final double max = 3.0;
        final double startValue = -2.5;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double root = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
        assertEquals(0.0, root, absoluteAccuracy);
    }

    /**
     * Test a function with a single root that is at a boundary.
     */
    @Test
    public void testRootAtBoundary() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        final double min = -3.0;
        final double max = 3.0;
        final double startValue = -3.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double root = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
        assertEquals(0.0, root, absoluteAccuracy);
    }

    /**
     * Test a function with no real roots.
     */
    @Test
    public void testNoRealRoots() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0 * Math.sqrt(x);
            }
        };
        final double min = -3.0;
        final double max = 3.0;
        final double startValue = 1.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        assertFalse(Precision.isClose(solver.solve(maxEval, function, min, max, startValue, allowedSolution), 0.0, absoluteAccuracy));
    }

    /**
     * Test a function with a vertical asymptote.
     */
    @Test
    public void testVerticalAsymptote() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        final double min = -10.0;
        final double max = 10.0;
        final double startValue = -5.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        assertFalse(Precision.isClose(solver.solve(maxEval, function, min, max, startValue, allowedSolution), 0.0, absoluteAccuracy));
    }

    /**
     * Test a function with a horizontal asymptote.
     */
    @Test
    public void testHorizontalAsymptote() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        final double min = -TestUtil.PI;
        final double max = TestUtil.PI;
        final double startValue = -TestUtil.PI;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        assertFalse(Precision.isClose(solver.solve(maxEval, function, min, max, startValue, allowedSolution), 0.0, absoluteAccuracy));
    }

    /**
     * Test a function with many roots.
     */
    @Test
    public void testMultipleRoots() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3.0 * x * x + 2.0 * x - 2.0;
            }
        };
        final double min = -2.0;
        final double max = 2.0;
        final double startValue = 1.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double[] roots = UnivariateFunctionUtils.roots(function, min, max);
        for (double root : roots) {
            double rootSolution = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
            assertEquals(root, rootSolution, absoluteAccuracy);
        }
    }

    /**
     * Test a function with many roots, but one of them is near a boundary.
     */
    @Test
    public void testMultipleRootsBoundary() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3.0 * x * x + 2.0 * x - 2.0;
            }
        };
        final double min = -3.0;
        final double max = 3.0;
        final double startValue = -2.5;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double[] roots = UnivariateFunctionUtils.roots(function, min, max);
        for (double root : roots) {
            double rootSolution = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
            assertEquals(root, rootSolution, absoluteAccuracy);
        }
    }

    /**
     * Test a function with many roots, and one of them is at a boundary.
     */
    @Test
    public void testMultipleRootsBoundary2() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x * x - 3.0 * x * x + 2.0 * x - 2.0;
            }
        };
        final double min = -3.0;
        final double max = 3.0;
        final double startValue = -3.0;
        final double absoluteAccuracy = 1e-8;
        final int maxEval = 1000;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final BaseSecantSolver solver = new BaseSecantSolver(absoluteAccuracy, method.REGULA_FALSI) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f, double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue, allowedSolution);
            }
        };
        double[] roots = UnivariateFunctionUtils.roots(function, min, max);
        for (double root : roots) {
            double rootSolution = solver.solve(maxEval, function, min, max, startValue, allowedSolution);
            assertEquals(root, rootSolution, absoluteAccuracy);
        }
    }

    private enum method { REGULA_FALSI, ILLINOIS, PEGASUS };

}