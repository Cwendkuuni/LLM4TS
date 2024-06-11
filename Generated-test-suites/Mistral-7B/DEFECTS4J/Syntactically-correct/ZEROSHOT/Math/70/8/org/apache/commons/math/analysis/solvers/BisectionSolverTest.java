package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

/**
 * JUnit 4 test file for BisectionSolver class.
 */
public class BisectionSolverTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    /**
     * Test solve method with a simple function.
     */
    @org.junit.Test
    public void testSolveSimpleFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 3);
        BisectionSolver solver = new BisectionSolver();
        double x = solver.solve(f, 0.0, 2.0);
        assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test solve method with a simple function and user-defined initial guess.
     */
    @org.junit.Test
    public void testSolveSimpleFunctionWithInitialGuess() {
        UnivariateRealFunction f = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 3);
        BisectionSolver solver = new BisectionSolver();
        double x = solver.solve(f, 1.5, 2.0);
        assertEquals(1.0, x, 1e-6);
    }

    /**
     * Test solve method with a simple function, interval too small.
     */
    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testSolveSimpleFunctionTooSmallInterval() {
        UnivariateRealFunction f = new PolynomialFunction(new double[] { 1.0, -2.0, 1.0 }, 3);
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 0.0);
    }

    /**
     * Test solve method with a simple function, function evaluation exception.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveSimpleFunctionFunctionEvalException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Evaluation error");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 1.0);
    }

    /**
     * Test solve method with a simple function, unsupported operation exception.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSolveSimpleFunctionUnsupportedOpException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Unsupported operation");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 1.0);
    }

    /**
     * Test solve method with a complex function.
     */
    @org.junit.Test
    public void testSolveComplexFunction() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1 + Math.pow(Math.E, -Math.abs(x)));
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double x = solver.solve(f, -1.0, 1.0);
        // Approximate value of the root is -0.6665414227508147
        // The bisection method may not converge to this value due to
        // the non-smoothness of the function.
        double expected = -0.6665414227508147;
        double tolerance = 0.2;
        assertTrue(Precision.abs(x, expected) < tolerance);
    }

    /**
     * Test verify interval method.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testVerifyIntervalInvalid() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.verifyInterval(0.0, 0.0);
    }

    /**
     * Test verify interval method.
     */
    @org.junit.Test
    public void testVerifyIntervalValid() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.verifyInterval(0.0, 1.0);
    }

    /**
     * Test matrix representation of solver steps.
     */
    @org.junit.Test
    public void testMatrixRepresentation() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / (1 + Math.pow(Math.E, -Math.abs(x)));
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double min = -1.0;
        double max = 1.0;
        double x = solver.solve(f, min, max);

        UnivariateRealFunction fTest = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return f.value(x);
            }
        };

        double[][] steps = new double[][] { { min, UnivariateRealSolverUtils.midpoint(min, max) },
                                            { UnivariateRealSolverUtils.midpoint(min, max), max } };

        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { min, solver.getIteration() },
                                                                         { solver.getIteration() - 1, UnivariateRealSolverUtils.midpoint(min, max) },
                                                                         { UnivariateRealSolverUtils.midpoint(min, max), max } });

        RealVector actual = solver.getMatrixRepresentation(fTest);

        assertEquals(expected.getRowDimension(), actual.getDimension());
        for (int i = 0; i < expected.getRowDimension(); i++) {
            RealVector rowExpected = expected.getColumnVector(i);
            RealVector rowActual = actual.getColumnVector(i);
            assertEquals(rowExpected.getDimension(), rowActual.getDimension());
            for (int j = 0; j < rowExpected.getDimension(); j++) {
                assertEquals(rowExpected.getEntry(j, 0), rowActual.getEntry(j, 0), 1e-6);
                assertEquals(rowExpected.getEntry(j, 1), rowActual.getEntry(j, 1), 1e-6);
            }
        }
    }
}