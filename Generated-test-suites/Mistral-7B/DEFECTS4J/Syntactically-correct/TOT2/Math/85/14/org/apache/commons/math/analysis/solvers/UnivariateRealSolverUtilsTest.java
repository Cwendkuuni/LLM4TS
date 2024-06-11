import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_PolynomialFunction() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 4., 0.});
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = -0.5;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() throws Exception {
        UnivariateRealFunction f = x -> Math.sin(x);
        double x0 = 0.0;
        double x1 = Math.PI;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = x -> 1 / x;
        double x0 = 0.0;
        double x1 = Double.POSITIVE_INFINITY;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolve_DefaultSolver() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4.0;
        double x0 = 1.0;
        double x1 = 3.0;
        double expectedResult = 2.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test
    public void testSolve_CustomAbsoluteAccuracy() throws Exception {
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4.0;
        double x0 = 1.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 2.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);

        Assert.assertEquals(expectedResult, result, absoluteAccuracy);
    }

    @Test
    public void testBracket_FunctionWithRoot() throws Exception {
        UnivariateRealFunction function = x -> Math.pow(x, 2) - 4.0;
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;

        double[] bracketingValues = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);

        Assert.assertArrayEquals(new double[]{1.937320511863471, 2.5}, bracketingValues, 0.001);
    }

    @Test
    public void testBracket_FunctionWithoutRoot() throws Exception {
        UnivariateRealFunction function = x -> Math.pow(x, 2) + 1;
        double initial = 1.5;
        double lowerBound = 1.0;
        double upperBound = 3.0;

        try {
            UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
            Assert.fail();
        } catch (ConvergenceException ce) {
            // Expected
        }
    }

    @Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double expectedResult = 1.5;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        Assert.assertEquals(expectedResult, result, 0.001);
    }
}
