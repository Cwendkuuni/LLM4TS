package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

/**
 * Test class for {@link BisectionSolver}.
 */
public class BisectionSolverTest {

    /**
     * Test solver with a polynomial function.
     */
    @Test
    public void testBisectionSolverPolynomial() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});
        double accuracy = 1.0e-8;
        double expectedResult = 1.0;

        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 0.0, 2.0);
            assertEquals(expectedResult, result, accuracy);
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should have found the root within the maximum number of iterations.");
        } catch (FunctionEvaluationException e) {
            fail("The function should be evaluable at the given interval.");
        }
    }

    /**
     * Test solver with a function that has only one root.
     */
    @Test
    public void testBisectionSolverSimpleFunction() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double accuracy = 1.0e-6;
        double expectedResult = 0.0;

        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, -3.0, 3.0);
            assertEquals(expectedResult, result, accuracy);
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should have found the root within the maximum number of iterations.");
        } catch (FunctionEvaluationException e) {
            fail("The function should be evaluable at the given interval.");
        }
    }

    /**
     * Test solver with a function that has no real roots.
     */
    @Test(expected = ZeroException.class)
    public void testBisectionSolverNoRealRoots() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        double accuracy = 1.0e-6;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -2.0, 2.0);
            fail("The function has no real roots.");
        } catch (ZeroException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("The function should be evaluable at the given interval.");
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should not have exceeded the maximum number of iterations.");
        }
    }

    /**
     * Test solver with a function that has complex roots.
     */
    @Test(expected = PrecisionRequiredException.class)
    public void testBisectionSolverComplexRoots() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(Math.pow(x, 2) + 1.0) - 1.0;
            }
        };
        double accuracy = 1.0e-6;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -2.0, 2.0);
            fail("The function has complex roots.");
        } catch (PrecisionRequiredException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("The function should be evaluable at the given interval.");
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should not have exceeded the maximum number of iterations.");
        }
    }

    /**
     * Test solver with a function that is not continuous.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverNotContinuousFunction() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0.0) {
                    return Math.sin(Math.PI / x);
                } else {
                    throw new FunctionEvaluationException("Function not defined for x = " + x);
                }
            }
        };
        double accuracy = 1.0e-6;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            fail("The function is not continuous.");
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should not have exceeded the maximum number of iterations.");
        }
    }

    /**
     * Test solver with a function that throws an exception.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverThrowingFunction() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) throws RuntimeException {
                if (x > 0.0) {
                    return Math.sin(Math.PI / x);
                } else {
                    throw new RuntimeException("Function not defined for x = " + x);
                }
            }
        };
        double accuracy = 1.0e-6;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, -1.0, 1.0);
            fail("The function should have thrown an exception.");
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should not have exceeded the maximum number of iterations.");
        }
    }

    /**
     * Test solver with a function that has infinite roots.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBisectionSolverInfiniteRoots() {
        UnivariateRealFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 1.0 / x;
            }
        };
        double accuracy = 1.0e-6;

        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(function, 0.0, 1.0);
            fail("The function has infinite roots.");
        } catch (NumberIsTooSmallException e) {
            // Expected
        } catch (FunctionEvaluationException e) {
            fail("The function should be evaluable at the given interval.");
        } catch (MaxIterationsExceededException e) {
            fail("The bisection solver should not have exceeded the maximum number of iterations.");
        }
    }
}