import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Test with a polynomial function
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -2});
        double x0 = 0.0;
        double x1 = 1.0;
        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            Assert.assertEquals(0.0, result, 1e-6);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a function that has no real roots
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-1, 1, 1});
        double y0 = 0.0;
        double y1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(g, y0, y1);
            Assert.fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test with a function that is always positive
        UnivariateRealFunction h = new PolynomialFunction(new double[]{1, 0});
        double z0 = 0.0;
        double z1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(h, z0, z1);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -1});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            Assert.assertArrayEquals(new double[]{1.0, 2.0}, result, 1e-6);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a function that has no real roots
        UnivariateRealFunction g = new PolynomialFunction(new double[]{-1, 1, 1});
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(g, initial, lowerBound, upperBound);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with a function that is always positive
        UnivariateRealFunction h = new PolynomialFunction(new double[]{1, 0});
        initial = 1.0;
        lowerBound = 0.0;
        upperBound = 2.0;

        try {
            UnivariateRealSolverUtils.bracket(h, initial, lowerBound, upperBound);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}