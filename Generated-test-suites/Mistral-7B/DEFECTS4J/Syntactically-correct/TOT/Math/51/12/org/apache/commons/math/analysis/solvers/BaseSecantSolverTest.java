package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    /**
     * Test default constructor with default method.
     */
    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom accuracies and method.
     */
    @Test
    public void testCustomConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-9, 1e-10, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test constructor with custom accuracies, function value accuracy, and method.
     */
    @Test
    public void testCustomConstructorWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-9, 1e-10, 1e-11, BaseSecantSolver.Method.PEGASUS);
        assertNotNull(solver);
    }

    /**
     * Test solving a simple function.
     */
    @Test
    public void testSimpleFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(-1.0, solver.solve(10, function, -2.0, 2.0, 0.0), 1e-6);
    }

    /**
     * Test solving a function with roots on both sides of the interval.
     */
    @Test
    public void testRootsOnBothSides() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        try {
            solver.solve(10, function, -2.0, 2.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a root below the interval.
     */
    @Test
    public void testRootBelowInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, -2.0, -1.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a root above the interval.
     */
    @Test
    public void testRootAboveInterval() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, 1.0, 2.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a root on the boundary of the interval.
     */
    @Test
    public void testRootOnBoundary() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        assertEquals(-1.0, solver.solve(10, function, 0.0, 1.0, 1e-6), 1e-6);
    }

    /**
     * Test solving a discontinuous function.
     */
    @Test
    public void testDiscontinuousFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x < 0) {
                    return x * x;
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, -1.0, 1.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a singularity.
     */
    @Test
    public void testSingularity() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Divide by zero");
                } else {
                    return 1 / x;
                }
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, -1.0, 1.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a very steep slope.
     */
    @Test
    public void testVerySteepSlope() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Precision.EPSILON * Math.exp(1 / (x + 1));
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, -10.0, 1.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test solving a function with a very flat slope.
     */
    @Test
    public void testVeryFlatSlope() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Precision.EPSILON * Math.exp(-1 / (x + 1));
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.PEGASUS);
        try {
            solver.solve(10, function, -10.0, 1.0, 0.0);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }
}