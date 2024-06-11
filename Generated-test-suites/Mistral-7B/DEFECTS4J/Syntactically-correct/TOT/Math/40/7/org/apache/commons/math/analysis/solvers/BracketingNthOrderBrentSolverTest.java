package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BracketingNthOrderBrentSolverTest {

    /**
     * A simple, unimodal function that has a root in the interval [a,b]
     */
    static class SimpleFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 4;
        }

        @Override
        public double derivative(double x) {
             return 2 * x;
        }

        @Override
        public double root(double x0, double tolerance) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval, double[] bracketingIntervalError)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Test
    public void testSimpleFunction() {
        final double epsilon = 1e-12;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 5);
        UnivariateFunction f = new SimpleFunction();
        double[] interval = {-5.0, 5.0};
        double result;

        // test bracketing interval
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root found outside bracketing interval", interval[0], result, epsilon);

        // test function which brackets the root
        interval = new double[]{-1.0, 1.0};
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root found outside bracketing interval", 0.0, result, epsilon);

        // test function with allowed solution below root
        solver = new BracketingNthOrderBrentSolver(1e-12, 5, AllowedSolution.LEFT_SIDE);
        result = solver.solve(100, f, interval[0], interval[1]);
        assertEquals("Root not found below the root", interval[0], result, epsilon);

        // test function with allowed solution above root
        solver = new BracketingNthOrderBrentSolver(1e-12, 5, AllowedSolution.RIGHT_SIDE);
        result = solver.solve(100, f, interval[0], interval[1]);
        assertEquals("Root not found above the root", interval[1], result, epsilon);

        // test function with allowed solution below or at root
        solver = new BracketingNthOrderBrentSolver(1e-12, 5, AllowedSolution.BELOW_SIDE);
        result = solver.solve(100, f, interval[0], interval[1]);
        assertEquals("Root not found below or at the root", interval[0], result, epsilon);

        // test function with allowed solution above or at root
        solver = new BracketingNthOrderBrentSolver(1e-12, 5, AllowedSolution.ABOVE_SIDE);
        result = solver.solve(100, f, interval[0], interval[1]);
        assertEquals("Root not found above or at the root", interval[1], result, epsilon);

        // test function with small bracketing interval
        interval = new double[]{-2.0, 2.0};
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE));

        // test function with large bracketing interval
        interval = new double[]{-1000.0, 1000.0};
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root not found in large interval", 0.0, result, epsilon);

        // test function with small tolerance and large bracketing interval
        interval = new double[]{-100.0, 100.0};
        solver = new BracketingNthOrderBrentSolver(1e-20, 5);
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE));

        // test function with large tolerance and large bracketing interval
        interval = new double[]{-1000.0, 1000.0};
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root not found in large interval with large tolerance", 0.0, result, epsilon);
    }

    /**
     * A multimodal function that has multiple roots
     */
    static class MultimodalFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * Math.pow(x, 2) + Math.pow(x, 2) - 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 4 * x + 2;
        }

        @Override
        public double root(double x0, double tolerance) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval, double[] bracketingIntervalError)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Test
    public void testMultimodalFunction() {
        final double epsilon = 1e-12;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 5);
        UnivariateFunction f = new MultimodalFunction();
        double[] interval = {-3.0, 3.0};
        double result;

        // test bracketing interval that does not bracket any root
        interval = new double[]{-10.0, 10.0};
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE));

        // test bracketing interval that brackets one root
        interval = new double[]{-1.0, 1.0};
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root not found in bracketing interval", interval[0], result, epsilon);

        // test bracketing interval that brackets multiple roots
        interval = new double[]{-2.5, 2.5};
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root not found in bracketing interval", 1.0, result, epsilon);
    }

    /**
     * A unimodal function with a root close to the minimum
     */
    static class RootNearMinimumFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 0.01;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }

        @Override
        public double root(double x0, double tolerance) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step)
                throws MaxCountExceededException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public double root(double x0, double tolerance, int maxIter, double step, double[] bracketingInterval, double[] bracketingIntervalError)
                throws MaxCountExceededException, NoBracketingException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Test
    public void testRootNearMinimumFunction() {
        final double epsilon = 1e-12;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 5);
        UnivariateFunction f = new RootNearMinimumFunction();
        double[] interval = {-1.0, 1.0};
        double result;

        // test bracketing interval that brackets the root
        result = solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE);
        assertEquals("Root not found in bracketing interval", 0.005, result, epsilon);

        // test bracketing interval that does not bracket the root
        interval = new double[]{-3.0, 3.0};
        assertThrows(NoBracketingException.class, () -> solver.solve(100, f, interval[0], interval[1], AllowedSolution.ANY_SIDE));
    }
}