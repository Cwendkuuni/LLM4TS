import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.TestFunctions;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.optimize.AbstractSimplex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    private AbstractSimplex simplex;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        simplex = new NelderMeadSimplex();
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));
    }

    @Test
    public void testRosebrockFunction() {
        MultivariateFunction function = new TestFunctions.RosebrockFunction();
        double[] startPoint = {2.0, 2.0, -2.0};
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = optimizer.optimize(1000, function, goalType, startPoint);

        Assert.assertEquals(0.0, solution.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solution.getPoint(), 1e-5);
    }

    @Test
    public void testSphereFunction() {
        MultivariateFunction function = new TestFunctions.SphereFunction();
        double[] startPoint = {0.1, 0.1};
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = optimizer.optimize(1000, function, goalType, startPoint);

        Assert.assertEquals(0.0, solution.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-5);
    }

    @Test
    public void testAckleyFunction() {
        MultivariateFunction function = new TestFunctions.AckleyFunction();
        double[] startPoint = {0.1, 0.1};
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = optimizer.optimize(1000, function, goalType, startPoint);

        Assert.assertEquals(-20.0, solution.getValue(), 1e-5);
        Assert.assertArrayEquals(new double[]{-0.8, -0.8}, solution.getPoint(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(1000, new TestFunctions.SphereFunction(), GoalType.MINIMIZE, new double[]{0.1, 0.1});
    }
}