import org.apache.commons.math3.optim.DirectSearchOptimizer;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {

        // Test with default constructor
        MultiDirectional md = new MultiDirectional();
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2);
        double x = 0.5;
        double expectedResult = 0.25;

        RealPointValuePair result = md.optimize(f, new SimpleBounds(x - 1, x + 1), new MaxEval(100));
        assertEquals(expectedResult, result.getPoint()[0], 0.01);

        // Test with custom constructor
        MultiDirectional mdCustom = new MultiDirectional(3.0, 0.6);
        UnivariateFunction g = new LinearFunction(new double[]{-1.0, 1.0}, 1);
        double y = 2.0;
        double expectedResultCustom = 1.2;

        RealPointValuePair resultCustom = mdCustom.optimize(g, new SimpleBounds(y - 1, y + 1), new MaxEval(100));
        assertEquals(expectedResultCustom, resultCustom.getPoint()[0], 0.01);

        // Test exception handling: FunctionEvaluationException
        UnivariateFunction h = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 3);
        RealPointValuePair initialPoint = new RealPointValuePair(new double[]{-2.0}, 0.0, false);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        MaxEval maxEval = new MaxEval(100);

        try {
            md.optimize(h, bounds, maxEval, initialPoint);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test exception handling: OptimizationException
        UnivariateFunction i = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2);
        RealPointValuePair initialPointOptimizationException = new RealPointValuePair(new double[]{Double.POSITIVE_INFINITY}, 0.0, false);
        SimpleBounds boundsOptimizationException = new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        MaxEval maxEvalOptimizationException = new MaxEval(1);

        try {
            md.optimize(i, boundsOptimizationException, maxEvalOptimizationException, initialPointOptimizationException);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }

        // Test exception handling: IllegalArgumentException
        UnivariateFunction j = null;
        RealPointValuePair initialPointIllegalArgumentException = new RealPointValuePair(new double[]{0.0}, 0.0, false);
        SimpleBounds boundsIllegalArgumentException = new SimpleBounds(0.0, 0.0);
        MaxEval maxEvalIllegalArgumentException = new MaxEval(100);

        try {
            md.optimize(j, boundsIllegalArgumentException, maxEvalIllegalArgumentException, initialPointIllegalArgumentException);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

    }
}