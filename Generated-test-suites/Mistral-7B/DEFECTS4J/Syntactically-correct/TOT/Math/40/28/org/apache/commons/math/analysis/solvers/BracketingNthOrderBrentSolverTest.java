package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        function = new PolynomialFunction(1, -1, 1);
    }

    @Test
    public void testRootOfPolynomialFunction() throws NumberIsTooSmallException {
        double x = solver.solve(100, function, -2, 2);
        assertEquals(0.0, x, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNoBracketing() throws NumberIsTooSmallException {
        function = new PolynomialFunction(1, -1, 0);
        solver.solve(100, function, 0, 0);
    }

    @Test(expected = NoBracketingException.class)
    public void testNegativeInterval() throws Exception {
        function = new PolynomialFunction(1, -1, 1);
        solver.solve(100, function, -2, -1);
    }

    @Test
    public void testAllowedSolution() throws NumberIsTooSmallException {
        solver = new BracketingNthOrderBrentSolver(1e-8, 5, AllowedSolution.LEFT_SIDE);
        double x = solver.solve(100, function, -2, 2);
        assertEquals(-1.0, x, 1e-8);
    }

    @Test
    public void testMaximalOrder() throws NumberIsTooSmallException {
        solver = new BracketingNthOrderBrentSolver(1e-8, 10);
        double x = solver.solve(100, function, -2, 2);
        assertEquals(0.0, x, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testNegativeMaximalOrder() throws NumberIsTooSmallException {
        solver = new BracketingNthOrderBrentSolver(1e-8, -3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFunction() {
        solver.solve(100, null, -2, 2);
    }

    @Test
    public void testFunctionValueAccuracy() throws NumberIsTooSmallException {
        solver = new BracketingNthOrderBrentSolver(1e-8, 5, 1e-9, 5);
        double x = solver.solve(100, function, -2, 2);
        assertEquals(0.0, x, 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeFunctionValueAccuracy() {
        solver = new BracketingNthOrderBrentSolver(1e-8, 5, -1e-9, 5);
    }

    private static class PolynomialFunction implements UnivariateFunction {

        private final double a;
        private final double b;
        private final double c;

        public PolynomialFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }

        @Override
        public double derivative(double x) {
            return 2 * a * x + b;
        }
    }

}