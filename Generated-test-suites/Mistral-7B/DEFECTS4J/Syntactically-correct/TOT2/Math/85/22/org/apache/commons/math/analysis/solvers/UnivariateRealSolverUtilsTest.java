import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_PolynomialFunction() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0}, new double[]{0.0});
        double x0 = -2.0;
        double x1 = 2.0;

        // Test default solver
        try {
            double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
            assertEquals(0.0, solution, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test solver with specific accuracy
        try {
            UnivariateRealSolver solver = new BrentSolver();
            solver.setAbsoluteAccuracy(1e-8);
            double solution = solver.solve(function, x0, x1);
            assertEquals(0.0, solution, 1e-8);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test solver with custom solver
        try {
            UnivariateRealSolver solver = new SecantSolver();
            double solution = solver.solve(function, x0, x1);
            assertEquals(0.0, solution, 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_ExceptionScenario() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 0.0, 0.0, 1.0}, new double[]{0.0});
        double x0 = 0.0;
        double x1 = 0.0;

        try {
            UnivariateRealSolverUtils.solve(function, x0, x1);
            fail("Expected FunctionEvaluationException or ConvergenceException");
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testBracket() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, -1.0}, new double[]{0.0});
        double initial = 1.0;
        double lowerBound = -1.0;
        double upperBound = 1.0;

        try {
            double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            assertEquals(initial - 1.0, bracketingValues[0], 1e-6);
            assertEquals(initial + 1.0, bracketingValues[1], 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBracket_ExceptionScenario() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 0.0, 0.0, 1.0}, new double[]{0.0});
        double initial = 0.0;
        double lowerBound = 0.0;
        double upperBound = 0.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

}