import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        function = f -> FunctionUtils.square(f);
    }

    @Test
    public void testSquareRootWithAllowedSolutionAnySide() throws NumberIsTooSmallException {
        double[] expectedSolutions = {1.5, 2.5};
        double[] inputs = {1.0, 3.0};
        for (int i = 0; i < inputs.length; ++i) {
            double result = solver.solve(10, function, inputs[i], 4.0, AllowedSolution.ANY_SIDE);
            Assert.assertEquals(expectedSolutions[i], result, 1e-6);
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSquareRootWithIntervalNotBracketingTheRoot() {
        solver.solve(10, function, 4.0, 3.0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSquareRootWithIntervalNotBracketingTheRootWithException() {
        BracketingNthOrderBrentSolver solverWithoutException = new BracketingNthOrderBrentSolver(1e-6, 5);
        solverWithoutException.solve(10, function, 4.0, 3.0);
    }

    @Test
    public void testLogarithmWithAllowedSolutionBelowSide() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-6, 1e-6, AllowedSolution.BELOW_SIDE);
        double input = 10.0;
        double expectedResult = Math.log(2.0);
        double result = solver.solve(10, function, input, input * 2.0);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testLogarithmWithRelativeAccuracyBelowZero() {
        solver = new BracketingNthOrderBrentSolver(-1e-6, 5, 1e-6, 1e-6, AllowedSolution.BELOW_SIDE);
    }

    @Test
    public void testLogarithmWithAbsoluteAccuracyBelowZero() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, -1e-6, 1e-6, AllowedSolution.BELOW_SIDE);
        double input = 10.0;
        double expectedResult = Math.log(2.0);
        double result = solver.solve(10, function, input, input * 2.0);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testLogarithmWithFunctionValueAccuracyBelowZero() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-6, -1e-6, AllowedSolution.BELOW_SIDE);
        double input = 10.0;
        double expectedResult = Math.log(2.0);
        double result = solver.solve(10, function, input, input * 2.0);
        Assert.assertEquals(expectedResult, result, 1e-6);
    }
}