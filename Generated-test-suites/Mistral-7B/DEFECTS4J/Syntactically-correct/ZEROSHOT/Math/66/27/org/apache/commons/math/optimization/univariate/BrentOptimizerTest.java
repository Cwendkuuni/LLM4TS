import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentMinimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test function f(x) = x^2
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0}, new int[]{2});
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 1.0;

        PointValuePair solution = optimizer.optimize(function, goalType, min, max, startValue);

        double expectedResult = 0.0;
        double expectedObjectiveValue = 0.0;
        int expectedIterations = 1; // This test case is a trivial one, the optimizer should converge in one step

        assertEquals("The optimization did not find the correct solution.", expectedResult, solution.getPoint(), 1e-9);
        assertEquals("The optimization did not find the correct objective value.", expectedObjectiveValue, solution.getValue(), 1e-9);
        assertEquals("The optimization did not converge in the expected number of iterations.", expectedIterations, optimizer.getIterations());

        // Test function f(x) = |x|
        UnivariateFunction functionAbs = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(x);
            }

            @Override
            public double derivativeValue(double x, int derivative) {
                return derivative == 0 ? 1.0 : 0.0;
            }
        };
        goalType = GoalType.MINIMIZE;
        min = -1.0;
        max = 1.0;
        startValue = 0.0;

        solution = optimizer.optimize(functionAbs, goalType, min, max, startValue);

        expectedResult = 0.0;
        expectedObjectiveValue = 0.0;
        expectedIterations = 3; // This test case is more complex, the optimizer might need more iterations to converge

        assertEquals("The optimization did not find the correct solution.", expectedResult, solution.getPoint(), 1e-9);
        assertEquals("The optimization did not find the correct objective value.", expectedObjectiveValue, solution.getValue(), 1e-9);
        assertEquals("The optimization did not converge in the expected number of iterations.", expectedIterations, optimizer.getIterations());

        // Test function f(x) = x^3
        UnivariateFunction functionCubic = new PolynomialFunction(new double[]{1.0, -3.0, 3.0, -1.0}, new int[]{3});
        goalType = GoalType.MINIMIZE;
        min = -2.0;
        max = 2.0;
        startValue = 1.0;

        solution = optimizer.optimize(functionCubic, goalType, min, max, startValue);

        expectedResult = -1.0;
        expectedObjectiveValue = 1.0;
        expectedIterations = 4; // This test case is more complex, the optimizer might need more iterations to converge

        assertEquals("The optimization did not find the correct solution.", expectedResult, solution.getPoint(), 1e-9);
        assertEquals("The optimization did not find the correct objective value.", expectedObjectiveValue, solution.getValue(), 1e-9);
        assertEquals("The optimization did not converge in the expected number of iterations.", expectedIterations, optimizer.getIterations());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentMinimizerMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = x -> Math.pow(Math.sin(x), 2); // A function with many local minima
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, goalType, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBrentMinimizerFunctionEvaluationException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateFunction function = x -> {
            throw new ArithmeticException("Divide by zero error.");
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;
        optimizer.optimize(function, goalType, min, max, startValue);
    }
}