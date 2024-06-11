import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizerTest;
import org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-5);
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(10);
        Assert.assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(-5);
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeSimpleFunction() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        MultivariateFunction function = (x) -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
        PointValuePair result = optimizer.optimize(function, new double[]{2.0, 2.0}, null, new double[]{0.0, 0.0});
        Assert.assertEquals(new double[]{1.0, 1.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
        Assert.assertEquals(0, optimizer.getIterations());
        Assert.assertEquals(0, optimizer.getEvaluations());
        Assert.assertArrayEquals(new double[2][2], optimizer.jacobian, 1e-6);
        Assert.assertEquals(0.0, optimizer.getCost(), 1e-6);
    }

    @Test
    public void testOptimizeFunctionWithBounds() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = (x) -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2) - 4, 2);
        InitialGuess initialGuess = new double[]{1.0, 1.0};
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});
        PointValuePair result = optimizer.optimize(objectiveFunction, null, null, initialGuess, bounds, GoalType.MINIMIZE);
        Assert.assertEquals(new double[]{2.0, 2.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(-4.0, result.getValue(), 1e-6);
        Assert.assertEquals(3, optimizer.getIterations());
        Assert.assertEquals(8, optimizer.getEvaluations());
        Assert.assertArrayEquals(new double[][]{{-1.275291274436517E-3, 4.592096392362413E-3},
                {4.592096392362413E-3, -1.275291274436517E-3}}, optimizer.jacobian, 1e-6);
        Assert.assertEquals(1.6484375966299667E-3, optimizer.getCost(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = (x) -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
        PointValuePair result = optimizer.optimize(objectiveFunction, new double[]{0.0, 0.0}, null, new double[]{0.0, 0.0}, null, GoalType.MINIMIZE, 1);
        Assert.fail();
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = (x) -> Math.pow(Math.pow(x[0], 2) + Math.pow(x[1], 2), 0.5);
        PointValuePair result = optimizer.optimize(objectiveFunction, new double[]{0.0, 0.0}, null, new double[]{0.0, 0.0}, null, GoalType.MINIMIZE, 1, 1);
        Assert.fail();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = (x) -> { throw new FunctionEvaluationException("Function evaluation error"); };
        PointValuePair result = optimizer.optimize(objectiveFunction, new double[]{0.0, 0.0}, null, new double[]{0.0, 0.0}, null, GoalType.MINIMIZE);
        Assert.fail();
    }
}