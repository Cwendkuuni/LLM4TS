package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;

/**
 * Test for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /** Simple test function. */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(final double x) {
            return x * x - 2;
        }
    };

    /**
     * Test default constructor with default method.
     */
    @Test
    public void testDefaultConstructor() {
        final BaseSecantSolver solver = new BaseSecantSolver( DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 1.0e-6 );
    }

    /**
     * Test constructor with specified method.
     */
    @Test
    public void testConstructorWithSpecifiedMethod() {
        final BaseSecantSolver solver = new BaseSecantSolver( 0.0001, Method.ILLINOIS );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 1.0e-6 );
    }

    /**
     * Test constructor with all accuracy parameters.
     */
    @Test
    public void testConstructorWithAllAccuracyParameters() {
        final BaseSecantSolver solver = new BaseSecantSolver( 1.0e-8, 1.0e-6, 1.0e-8, Method.PEGASUS );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 1.0e-8 );
    }

    /**
     * Test invalid constructor.
     */
    @Test( expected = MathIllegalArgumentException.class )
    public void testInvalidConstructor() {
        new BaseSecantSolver( 1.0, Method.PEGASUS );
    }

    /**
     * Test solving with relative accuracy.
     */
    @Test
    public void testSolvingWithRelativeAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver( 0.001, Method.REGULA_FALSI );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 0.001, 1.0e-6 );
    }

    /**
     * Test solving with absolute accuracy.
     */
    @Test
    public void testSolvingWithAbsoluteAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver( 1.0e-6, Method.PEGASUS );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 1.0e-6, 1.0e-6 );
    }

    /**
     * Test solving with function value accuracy.
     */
    @Test
    public void testSolvingWithFunctionValueAccuracy() {
        final BaseSecantSolver solver = new BaseSecantSolver( 0.001, 1.0e-6, 0.001, Method.PEGASUS );
        assertEquals( 0.0, solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ), 0.001, 1.0e-6 );
    }

    /**
     * Test solving with invalid input.
     */
    @Test( expected = IllegalArgumentException.class )
    public void testSolvingWithInvalidInput() {
        final BaseSecantSolver solver = new BaseSecantSolver( 1.0, Method.PEGASUS );
        solver.solve( 10, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE );
    }

    /**
     * Test solving with max iterations exceeded.
     */
    @Test
    public void testSolvingWithMaxIterationsExceeded() {
        final BaseSecantSolver solver = new BaseSecantSolver( 1.0e-6, Method.PEGASUS );
        assertThrows( MaxIterationsExceededException.class, () -> solver.solve( 0, TEST_FUNCTION, -1.0, 1.0, AllowedSolution.ANY_SIDE ) );
    }

}