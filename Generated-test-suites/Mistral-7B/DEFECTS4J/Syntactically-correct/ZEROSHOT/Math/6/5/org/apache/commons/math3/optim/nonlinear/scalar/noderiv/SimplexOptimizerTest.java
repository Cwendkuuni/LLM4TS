import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.TestFunctions;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.SimplexOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimization() {
        // Test with a simple function.
        MultivariateFunction function = new TestFunctions.SphereFunction(2);
        AbstractSimplex simplex = new TestFunctions.Simplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[] {simplextoArray(simplex)});

        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testComplexOptimization() {
        // Test with a more complex function.
        MultivariateFunction function = new TestFunctions.RosenbrockFunction(2);
        AbstractSimplex simplex = new TestFunctions.Simplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        PointValuePair result = optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[] {simplextoArray(simplex)});

        Assert.assertEquals(-1.0, result.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 2.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testNullInputs() {
        AbstractSimplex simplex = null;
        MultivariateFunction function = null;

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        try {
            optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[] {simplextoArray(simplex)});
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            optimizer.optimize(1000, function, GoalType.MINIMIZE, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            optimizer.optimize(1000, function, null, new Object[] {simplextoArray(simplex)});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[] {null});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyData() {
        MultivariateFunction function = new TestFunctions.SphereFunction(2);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        try {
            optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[0]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptySimplex() {
        MultivariateFunction function = new TestFunctions.SphereFunction(2);
        AbstractSimplex simplex = new TestFunctions.EmptySimplex(3);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));

        try {
            optimizer.optimize(1000, function, GoalType.MINIMIZE, new Object[] {simplextoArray(simplex)});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private double[] simplextoArray(AbstractSimplex simplex) {
        double[] result = new double[simplex.getSize() * (simplex.getDimension() + 1) / 2];
        int index = 0;
        for (int i = 0; i < simplex.getSize(); i++) {
            double[] point = simplex.getPoint(i);
            System.arraycopy(point, 0, result, index, point.length);
            index += point.length;
        }
        return result;
    }
}