import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.test.AbstractUnivariateRealTester;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentSolverWithDefaultParameters() throws NumberIsTooSmallException {
        UnivariateFunction f = new MyFunction();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals("Root found: " + root, 0.0, root, 1e-6);
    }

    @Test
    public void testBrentSolverWithCustomParameters() throws NumberIsTooSmallException {
        UnivariateFunction f = new MyFunction();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals("Root found: " + root, 0.0, root, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentSolverWithInvalidMaximalOrder() throws NumberIsTooSmallException {
        UnivariateFunction f = new MyFunction();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @Test
    public void testBrentSolverWithCustomAllowedSolution() throws NumberIsTooSmallException {
        UnivariateFunction f = new MyFunction();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7, AllowedSolution.BELOW_SIDE);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertTrue("Root found: " + root + " is below zero", root < 0);
    }

    @Test
    public void testBrentSolverWithFunctionValueAccuracy() throws NumberIsTooSmallException {
        UnivariateFunction f = new MyFunctionWithError();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7, 1e-8);
        double root = solver.solve(100, f, -1.5, 1.5);
        assertEquals("Root found: " + root, 0.0, root, 1e-8);
    }

    private static class MyFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 2;
        }

    }

    private static class MyFunctionWithError implements UnivariateFunction {

        @Override
        public double value(double x) {
            return x * x - 2 + Math.random() * 0.01;
        }

    }

}