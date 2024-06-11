import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.functions.SineFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        UnivariateFunction function = new SineFunction(0.5, 1);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);

        UnivariatePointValuePair result = optimizer.optimize(function, 0.5, 1.5);

        double expected = 1.0;
        assertEquals(expected, result.getPoint(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerThrowsExceptionWithInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(0, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerThrowsExceptionWithInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerThrowsExceptionWithInvalidStartValue() {
        UnivariateFunction function = f -> f.value(new double[]{f.getArgument()[0]});
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerThrowsExceptionWithInvalidIntervalEnd() {
        UnivariateFunction function = f -> f.value(new double[]{f.getArgument()[0]});
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.optimize(function, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }
}