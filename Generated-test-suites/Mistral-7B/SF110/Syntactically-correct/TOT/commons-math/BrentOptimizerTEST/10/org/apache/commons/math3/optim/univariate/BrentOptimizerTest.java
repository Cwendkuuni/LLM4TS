import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerWithValidInput() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x + 2;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair solution = optimizer.optimize(function, -5.0, 5.0);

        assertEquals(2.0, solution.getPoint(), 1.0e-6);
        assertEquals(-2.9999999999999996, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testBrentOptimizerMinimization() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        UnivariatePointValuePair solution = optimizer.optimize(function, 1.0, 5.0);

        assertEquals(1.0, solution.getPoint(), 1.0e-6);
        assertEquals(0.0, solution.getValue(), 1.0e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1.0e-6, 1.0e-6, null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, -1.0e-6, null);
    }

    @Test(expected = ConvergenceException.class)
    public void testBrentOptimizerConvergenceException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, -Math.log(Math.abs(x) + 0.01)) - Math.log(Math.abs(x) + 0.01);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, null);
        optimizer.optimize(function, -1.0, 1.0);
    }
}