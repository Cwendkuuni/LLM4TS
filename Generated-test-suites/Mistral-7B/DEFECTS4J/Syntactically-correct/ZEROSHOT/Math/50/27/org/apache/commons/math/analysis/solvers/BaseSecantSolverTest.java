package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test solver with Math.sqrt(x) function.
     */
    @Test
    public void testSqrtFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(0.0, solver.solve(10, f, -1.0, 1.0, 0.0), 1e-6);
    }

    /**
     * Test solver with Math.sqrt(x) function and specified allowed solution.
     */
    @Test
    public void testSqrtFunctionAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, solver.solve(10, f, -1.0, 1.0, 0.0), 1e-6);

        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(10, f, -1.0, 1.0, 0.0));

        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, -1.0, 1.0, 0.0));
    }

    /**
     * Test solver with Math.sin(x) function.
     */
    @Test
    public void testSinFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(Math.PI / 2.0);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);
        assertEquals(Math.PI / 2.0, solver.solve(10, f, -Math.PI, Math.PI, 0.0), 1e-6);
    }

    /**
     * Test solver with Math.sin(x) function, invalid input.
     */
    @Test
    public void testSinFunctionInvalidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.sin(Math.PI / 2.0);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.BELOW_SIDE);
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(10, f, -Math.PI, Math.PI, 0.0));

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, -Math.PI, Math.PI, 0.0));
    }

    /**
     * Test solver with Math.exp(x) function.
     */
    @Test
    public void testExpFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertEquals(Math.E, solver.solve(10, f, -10.0, 10.0, 0.0), 1e-6);
    }

    /**
     * Test solver with Math.exp(x) function, invalid input.
     */
    @Test
    public void testExpFunctionInvalidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        assertThrows(NumberIsTooSmallException.class, () -> solver.solve(10, f, -10.0, 10.0, 0.0));

        solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, -10.0, 10.0, 0.0));
    }

    /**
     * Test solver with an undefined function.
     */
    @Test
    public void testUndefinedFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, -10.0, 10.0, 0.0));
    }

    /**
     * Test solver with an undefined function and valid input.
     */
    @Test
    public void testUndefinedFunctionValidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, -10.0, 10.0, 0.0));

        solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, -10.0, 10.0, 0.0));
    }

    /**
     * Test solver with an undefined function and invalid input.
     */
    @Test
    public void testUndefinedFunctionInvalidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.ANY_SIDE);
        assertThrows(UnsupportedOperationException.class, () -> solver.solve(10, f, Double.NaN, Double.NaN, 0.0));
    }

    /**
     * Test solver with a function with a singularity.
     */
    @Test
    public void testSingularityFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        assertEquals(0.0, solver.solve(10, f, 1.0, 2.0, 1.0), 1e-6);
    }

    /**
     * Test solver with a function with a singularity and valid input.
     */
    @Test
    public void testSingularityFunctionValidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, solver.solve(10, f, 1.0, 2.0, 1.0), 1e-6);

        solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.ABOVE_SIDE);
        assertThrows(NumberIsTooLargeException.class, () -> solver.solve(10, f, 1.0, 2.0, 1.0));
    }

    /**
     * Test solver with a function with a singularity and invalid input.
     */
    @Test
    public void testSingularityFunctionInvalidInput() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, AllowedSolution.ANY_SIDE);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(10, f, Double.NaN, Double.NaN, 0.0));
    }

    /**
     * Test solver with a function with a singularity and relative accuracy.
     */
    @Test
    public void testSingularityFunctionRelativeAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x - 1));
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-3, Method.PEGASUS);
        assertEquals(0.0, solver.solve(10, f, 1.0, 2.0, 1.0), 1e-3);
    }

    /**
     * Test solver with a function that has roots at the bounds.
     */
    @Test
    public void testFunctionWithRootsAtBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertEquals(-2.0, solver.solve(10, f, -3.0, 3.0, 0.0), 1e-6);
    }

    /**
     * Test solver with a function that has roots at the bounds and specified
     * allowed solution.
     */
    @Test
    public void testFunctionWithRootsAtBoundsAllowedSolution() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, solver.solve(10, f, -3.0, 3.0, 0.0), 1e-6);

        solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, solver.solve(10, f, -3.0, 3.0, 0.0), 1e-6);
    }
}