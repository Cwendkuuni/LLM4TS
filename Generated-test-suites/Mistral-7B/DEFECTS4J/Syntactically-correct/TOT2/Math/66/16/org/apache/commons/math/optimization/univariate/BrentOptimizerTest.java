import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeType;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        testFunction = new TestFunction();
    }

    @Test
    public void testMinimizeSimpleFunction() {
        double expectedResult = 1.0;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected minimum found.", expectedResult, actualResult, tolerance);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        double min = 0.0;
        double max = 1.0;
        double startValue = 0.5;
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        UnivariateRealFunction failingFunction = new FailingFunction();
        optimizer.optimize(failingFunction, GoalType.MINIMIZE, 0.0, 1.0, 0.5);
    }

    @Test
    public void testEdgeCases() {
        // Minimize a constant function
        double expectedResult = 0.0;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 0.0;
        double startValue = 0.0;
        PointValuePair result = optimizer.optimize(new ConstantFunction(), GoalType.MINIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected minimum found.", expectedResult, actualResult, tolerance);

        // Minimize a function with a single local minimum
        double expectedResultSingleMinimum = 1.0;
        double toleranceSingleMinimum = 1e-6;
        double minSingleMinimum = -1.0;
        double maxSingleMinimum = 1.0;
        double startValueSingleMinimum = 0.0;
        PointValuePair resultSingleMinimum = optimizer.optimize(new SingleMinimumFunction(), GoalType.MINIMIZE, minSingleMinimum, maxSingleMinimum, startValueSingleMinimum);
        double actualResultSingleMinimum = resultSingleMinimum.getPoint();
        assertEquals("Unexpected minimum found.", expectedResultSingleMinimum, actualResultSingleMinimum, toleranceSingleMinimum);

        // Minimize a function with multiple local minima
        double expectedResultMultipleMinima = 2.0;
        double toleranceMultipleMinima = 1e-6;
        double minMultipleMinima = -3.0;
        double maxMultipleMinima = 3.0;
        double startValueMultipleMinima = 1.0;
        PointValuePair resultMultipleMinima = optimizer.optimizer(new MultipleMinimaFunction(), GoalType.MINIMIZE, minMultipleMinima, maxMultipleMinima, startValueMultipleMinima);
        double actualResultMultipleMinima = resultMultipleMinima.getPoint();
        assertEquals("Unexpected minimum found.", expectedResultMultipleMinima, actualResultMultipleMinima, toleranceMultipleMinima);
    }

    @Test
    public void testMaximizeFunction() {
        double expectedResult = 2.0;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MAXIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected maximum found.", expectedResult, actualResult, tolerance);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        double expectedResult = 2.0;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        PointValuePair result = optimizer.optimize(new SimpleMaximumFunction(), GoalType.MAXIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected maximum found.", expectedResult, actualResult, tolerance);
    }

    @Test
    public void testMaxIterations() {
        double expectedResult = 1.5;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 3.0;
        double startValue = 2.0;
        optimizer.setMaxIterations(3);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected result.", expectedResult, actualResult, tolerance);
    }

    @Test
    public void testAbsoluteAccuracy() {
        double expectedResult = 1.1;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        optimizer.setAbsoluteAccuracy(0.1);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected result.", expectedResult, actualResult, tolerance);
    }

    @Test
    public void testRelativeAccuracy() {
        double expectedResult = 1.2;
        double tolerance = 1e-6;
        double min = 0.0;
        double max = 2.0;
        double startValue = 1.5;
        optimizer.setRelativeAccuracy(0.1);
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE, min, max, startValue);
        double actualResult = result.getPoint();
        assertEquals("Unexpected result.", expectedResult, actualResult, tolerance);
    }

    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - Math.sin(Math.PI * x) + E.E;
        }
    }

    private static class FailingFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            throw new RuntimeException("Function evaluation error.");
        }
    }

    private static class ConstantFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return 0.0;
        }
    }

    private static class SingleMinimumFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }
    }

    private static class MultipleMinimaFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            double value = Math.pow(x - 1.0, 2) + Math.pow(x - 2.0, 2) / 4.0;
            return value;
        }
    }

    private static class SimpleMaximumFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.max(Math.sin(Math.PI * x), Math.cos(Math.PI * x));
        }
    }
}