import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragePointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker.SimpleConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private MultivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        function = new TestFunction();
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE,
                new OptimizationData[]{new OptimizationData(new AveragePointInitializer(3))});
        assertEquals(-0.5, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.0, -1.0, 1.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testConvergence() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleConvergenceChecker(1e-6));
        PointValuePair[] previous = new PointValuePair[3];
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 3.0);
        previous[0] = point;
        previous[1] = new PointValuePair(new double[]{-1.0, -1.0, 1.0}, 1.0);
        previous[2] = new PointValuePair(new double[]{0.0, 0.0, 1.0}, 2.0);

        Simplex simplex = new Simplex(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }
        }, 3);

        optimizer.parseOptimizationData(new OptimizationData[]{new OptimizationData(simplex)});

        optimizer.doOptimize();

        assertFalse(optimizer.isConverged());
    }
}

class TestFunction implements MultivariateFunction {

    @Override
    public double value(double[] x) {
        double sum = 0.0;
        for (double d : x) {
            sum += Math.pow(d - 0.5, 2.0);
        }
        return sum;
    }
}