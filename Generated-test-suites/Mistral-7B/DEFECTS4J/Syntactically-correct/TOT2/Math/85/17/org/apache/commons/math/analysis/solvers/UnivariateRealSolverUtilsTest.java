import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new MockFunction(); // A function that always returns 0
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.5; // A value between x0 and x1

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(expectedResult, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new MockFunction();
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 0.001;
        double expectedResult = 1.5 + 0.001; // A value between x0 and x1 with a higher accuracy

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(expectedResult, result, absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testSolveWithInvalidArguments() {
        UnivariateRealFunction f = null; // Null function

        assertThrows(MathRuntimeException.class, () -> UnivariateRealSolverUtils.solve(f, 1.0, 2.0));

        f = new MockFunction();
        double x0 = 1.0;
        double x1 = 1.0; // The interval is not valid

        assertThrows( IllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(f, x0, x1));
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new MockFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;

        double[] expectedResult = {0.5, 1.5}; // A pair of values satisfying the conditions

        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            assertArrayEquals(expectedResult, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new MockFunction();
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10; // Fewer iterations than needed

        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations));
    }

    @Test
    public void testBracketWithInvalidArguments() {
        UnivariateRealFunction f = null; // Null function

        assertThrows(MathRuntimeException.class, () -> UnivariateRealSolverUtils.bracket(f, 1.0, 2.0, 3.0));

        f = new MockFunction();
        double initial = 1.0;
        double lowerBound = 2.0; // The lower bound is greater than the upper bound
        double upperBound = 1.0;

        assertThrows(IllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound));
    }
}

private class MockFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
        return 0.0;
    }
}