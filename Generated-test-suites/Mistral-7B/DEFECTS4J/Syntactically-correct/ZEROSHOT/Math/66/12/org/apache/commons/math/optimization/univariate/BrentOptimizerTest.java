import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMin() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -3.14159265, 3.14159265, 0.0);
        double expected = Math.PI * Math.PI;
        assertEquals(expected, result.getValue(), 1e-6);
        assertEquals(Math.PI, result.getPoint(), 1e-6);
    }

    @Test
    public void testBrentOptimizerMax() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -3.14159265, 3.14159265, 0.0);
        double expected = -(Math.PI * Math.PI);
        assertEquals(expected, result.getValue(), 1e-6);
        assertEquals(-Math.PI, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAccuracy() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-0.01);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };
        optimizer.optimize(f, GoalType.MINIMIZE, 0, 1, 0.5);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBrentOptimizerMaxIterations() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2 * x) * Math.sin(Math.PI / 2 * x);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, GoalType.MINIMIZE, -1, 1, 0.5);
    }
}