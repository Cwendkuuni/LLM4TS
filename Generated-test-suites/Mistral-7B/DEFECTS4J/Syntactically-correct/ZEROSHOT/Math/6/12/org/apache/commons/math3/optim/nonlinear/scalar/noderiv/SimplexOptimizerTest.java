import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.maxevaluations.MaxEvaluations;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeTest;
import org.apache.commons.math3.optim.nonlinear.scalar.MaxEvaluationsTest;
import org.apache.commons.math3.optim.nonlinear.vector.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.vector.InitialGuessImpl;
import org.apache.commons.math3.optim.nonlinear.vector.MaxEvaluationsImpl;
import org.apache.commons.math3.optim.nonlinear.vector.Simplex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {

    private AbstractSimplex simplex;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        simplex = new Simplex(3);
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        optimizer.setSimplex(simplex);
    }

    @Test
    public void testEmptyOptimize() {
        MultivariateFunction f = (MultivariateFunction) (x -> 0.0);
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(10, f, goalType, new Object[0]);
        Assert.assertNull(result);
    }

    @Test
    public void testMinimizeSimpleFunction() {
        MultivariateFunction f = (MultivariateFunction) (x -> x[0] * x[0] + x[1] * x[1]);
        GoalType goalType = GoalType.MINIMIZE;
        InitialGuess initialGuess = new InitialGuessImpl(new double[]{1.0, 1.0});
        OptimizationData[] optData = {initialGuess, simplex};
        PointValuePair result = optimizer.optimize(10, f, goalType, optData);
        Assert.assertArrayEquals(new double[]{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}, result.getPoint(), 0.001);
        Assert.assertEquals(f.value(result.getPoint()), 1.0, 0.001);
    }

    @Test
    public void testMaximizeSimpleFunction() {
        MultivariateFunction f = (MultivariateFunction) (x -> -x[0] * x[0] - x[1] * x[1]);
        GoalType goalType = GoalType.MAXIMIZE;
        InitialGuess initialGuess = new InitialGuessImpl(new double[]{-1.0, -1.0});
        OptimizationData[] optData = {initialGuess, simplex};
        PointValuePair result = optimizer.optimize(10, f, goalType, optData);
        Assert.assertArrayEquals(new double[]{-1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0)}, result.getPoint(), 0.001);
        Assert.assertEquals(f.value(result.getPoint()), -1.0, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        optimizer.setSimplex(null);
        optimizer.optimize(1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MINIMIZE, new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        optimizer.setSimplex(simplex);
        optimizer.optimize(1, new MultivariateFunction() {
            public double value(double[] x) {
                return 0.0;
            }
        }, GoalType.MAXIMIZING, new Object[0]);
    }

    @Test
    public void testMaxEvaluations() {
        MultivariateFunction f = (MultivariateFunction) (x -> Math.log(1 + x[0] * x[0] + x[1] * x[1]));
        GoalType goalType = GoalType.MAXIMIZE;
        InitialGuess initialGuess = new InitialGuessImpl(new double[]{0.0, 0.0});
        OptimizationData[] optData = {initialGuess, simplex};
        MaxEvaluations maxEvaluations = new MaxEvaluationsImpl(3);
        PointValuePair result = optimizer.optimize(maxEvaluations, f, goalType, optData);
        Assert.assertNotNull(result);
        MaxEvaluationsTest.assertEvaluations(maxEvaluations, optimizer);
        GoalTypeTest.assertGoalType(goalType, optimizer, result);
    }
}