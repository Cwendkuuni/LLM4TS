import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.direct.AbstractSimplex;
import org.apache.commons.math3.optim.direct.GoalType;
import org.apache.commons.math3.optim.direct.SimplexOptimizer;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimization() {
        // Set up the test.
        double[] startPoint = new double[]{1.0, 2.0};
        double minValue = 3.0;
        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 2);

        AbstractSimplex simplex = new SimpleSimplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-4));

        // Call the optimization.
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        // Check the result.
        assertEquals(minValue, result.getValue(), 1e-4);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-4);
    }

    @Test
    public void testNoImprovement() {
        // Set up the test.
        double[] startPoint = new double[]{1.0, 2.0};
        double minValue = 3.0;
        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, -2.0, 1.0}, 2);

        AbstractSimplex simplex = new SimpleSimplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-4));

        // Call the optimization with a start point that is not optimal.
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);

        // Check the result.
        assertNotEquals(minValue, result.getValue(), 1e-4);
        // The optimization should not have converged, so it should return the same point as the start point.
        assertArrayEquals(startPoint, result.getPoint(), 1e-4);
    }

    @Test
    public void testMaximization() {
        // Set up the test.
        double[] startPoint = new double[]{1.0, 2.0};
        double maxValue = 3.0;
        MultivariateFunction function = new PolynomialFunction(new double[]{1.0, 1.0, -1.0, 1.0}, 2);

        AbstractSimplex simplex = new SimpleSimplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-4, 1e-4));

        // Call the optimization.
        PointValuePair result = optimizer.optimize(100, function, GoalType.MAXIMIZE, startPoint);

        // Check the result.
        assertEquals(maxValue, result.getValue(), 1e-4);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-4);
    }
}

class SimpleSimplex extends AbstractSimplex {
    public SimpleSimplex(int size) {
        super(size);
    }

    @Override
    public void build(double[] startPoint) {
        // Implementation of the simplex algorithm is not important for this test.
    }

    @Override
    public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        // Implementation of the simplex algorithm is not important for this test.
    }

    @Override
    public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {
        // Implementation of the simplex algorithm is not important for this test.
    }
}