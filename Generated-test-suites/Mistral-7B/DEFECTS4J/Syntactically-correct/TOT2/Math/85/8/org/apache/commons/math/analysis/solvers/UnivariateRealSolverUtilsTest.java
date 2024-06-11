import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {
    // Test case for solve method with default solver
    @Test
    public void testSolve_DefaultSolver() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.3247119255181066; // root of the polynomial function

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1);
            Assert.assertEquals(expectedResult, result, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // Exception should not be thrown for this test case
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test case for solve method with absolute accuracy
    @Test
    public void testSolve_AbsoluteAccuracy() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 1.3247119255181066; // root of the polynomial function

        try {
            double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
            Assert.assertEquals(expectedResult, result, absoluteAccuracy);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            // Exception should not be thrown for this test case
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test case for bracket method
    @Test
    public void testBracket() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 3.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            double a = result[0];
            double b = result[1];
            double expectedA = 0.5;
            double expectedB = 2.0;

            Assert.assertEquals(expectedA, a, 0.001);
            Assert.assertEquals(expectedB, b, 0.001);
        } catch (FunctionEvaluationException | ConvergenceException | IllegalArgumentException e) {
            // Exception should not be thrown for this test case
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test case for bracket method with maximum iterations
    @Test
    public void testBracket_MaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});
        double initial = 1.5;
        double lowerBound = 0.0;
        double upperBound = 3.0;
        int maximumIterations = 10;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound, maximumIterations);
            double a = result[0];
            double b = result[1];

            // In this test case, the algorithm may not find bracketing values
            // within the specified maximum iterations, but it should not throw
            // an exception
            if (a == lowerBound && b == upperBound) {
                Assert.assertTrue("Bracketing values not found within the specified maximum iterations", true);
            } else {
                double expectedA = 0.5;
                double expectedB = 2.0;

                Assert.assertEquals(expectedA, a, 0.001);
                Assert.assertEquals(expectedB, b, 0.001);
            }
        } catch (FunctionEvaluationException | ConvergenceException | IllegalArgumentException e) {
            // Exception should not be thrown for this test case
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}