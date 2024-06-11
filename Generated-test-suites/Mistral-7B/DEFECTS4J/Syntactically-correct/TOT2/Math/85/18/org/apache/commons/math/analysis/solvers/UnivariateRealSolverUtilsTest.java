import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BracketingBrentSolver;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.LassoSolver;
import org.apache.commons.math3.analysis.solvers.SecantSolver;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for UnivariateRealSolverUtils.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test the solve method with a simple quadratic function.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveQuadratic() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new QuadraticFunction(1.0, 2.0, 3.0);
        double solution = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
        Assert.assertEquals("Solution is not correct", 0.0, solution, 1e-10);
    }

    /**
     * Test the solve method with a function having multiple roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveMultipleRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new SinCosFunction();
        double[] roots = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        double x0 = roots[0];
        double x1 = roots[1];
        double solution1 = UnivariateRealSolverUtils.solve(function, x0, x1);
        Assert.assertEquals("Solution 1 is not correct", -Math.PI / 2.0, solution1, 1e-10);
        double solution2 = UnivariateRealSolverUtils.solve(function, x1, x0);
        Assert.assertEquals("Solution 2 is not correct", Math.PI / 2.0, solution2, 1e-10);
    }

    /**
     * Test the solve method with a function with no roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveNoRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbsFunction();
        UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
    }

    /**
     * Test the bracket method with a simple quadratic function.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testBracketQuadratic() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new QuadraticFunction(1.0, 2.0, 3.0);
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, -1.0, 1.0);
        double x0 = bracketingPoints[0];
        double x1 = bracketingPoints[1];
        double solution = UnivariateRealSolverUtils.solve(function, x0, x1);
        Assert.assertEquals("Solution is not correct", 0.0, solution, 1e-10);
        Assert.assertTrue("Roots are not bracketted", (function.value(x0) * function.value(x1) < 0.0));
    }

    /**
     * Test the bracket method with a function having multiple roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testBracketMultipleRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new SinCosFunction();
        double[] bracketingPoints = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
        double x0 = bracketingPoints[0];
        double x1 = bracketingPoints[1];
        double solution1 = UnivariateRealSolverUtils.solve(function, x0, x1);
        Assert.assertEquals("Solution 1 is not correct", -Math.PI / 2.0, solution1, 1e-10);
        double solution2 = UnivariateRealSolverUtils.solve(function, x1, x0);
        Assert.assertEquals("Solution 2 is not correct", Math.PI / 2.0, solution2, 1e-10);
        Assert.assertTrue("Roots are not bracketted", (function.value(x0) * function.value(x1) < 0.0));
    }

    /**
     * Test the bracket method with a function with no roots.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketNoRoots() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new AbsFunction();
        UnivariateRealSolverUtils.bracket(function, -1.0, 1.0);
    }

    /**
     * Test the midpoint method.
     */
    @Test
    public void testMidpoint() {
        Assert.assertEquals("Midpoint calculation is incorrect", 2.5, UnivariateRealSolverUtils.midpoint(1.0, 3.0), 1e-10);
    }

    /**
     * Test setup method.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetupIllegalArgumentException() {
        UnivariateRealFunction function = null;
        UnivariateRealSolverUtils.setup(function);
    }

    /**
     * Test the solve method using different solvers.
     * @throws ConvergenceException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveDifferentSolvers() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction function = new QuadraticFunction(1.0, 2.0, 3.0);
        double solutionBrent = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
        Assert.assertEquals("Solution using Brent solver is not correct", 0.0, solutionBrent, 1e-10);

        double solutionLasso = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, new LassoSolver());
        Assert.assertEquals("Solution using Lasso solver is not correct", 0.0, solutionLasso, 1e-10);

        double solutionSecant = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, new SecantSolver());
        Assert.assertEquals("Solution using Secant solver is not correct", 0.0, solutionSecant, 1e-10);

        double solutionBrentWithAccuracy = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 1e-10);
        Assert.assertEquals("Solution using Brent solver with accuracy is not correct", 0.0, solutionBrentWithAccuracy, 1e-12);

        double solutionBrentWithMaxIterations = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, new BrentSolver(100));
        Assert.assertEquals("Solution using Brent solver with max iterations is not correct", 0.0, solutionBrentWithMaxIterations, 1e-10);
    }

    /**
     * Quadratic function for testing.
     */
    private static class QuadraticFunction implements UnivariateRealFunction {

        private final double a;
        private final double b;
        private final double c;

        public QuadraticFunction(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public double value(double x) {
            return a * x * x + b * x + c;
        }
    }

    /**
     * Function for testing having multiple roots.
     */
    private static class SinCosFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x) * Math.cos(x);
        }
    }

    /**
     * Function for testing having no roots.
     */
    private static class AbsFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.abs(x);
        }
    }
}