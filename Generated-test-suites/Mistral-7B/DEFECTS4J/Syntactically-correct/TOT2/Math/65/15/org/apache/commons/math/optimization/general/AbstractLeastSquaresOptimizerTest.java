import org.apache.commons.math3.optim.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optim.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.simple.SimpleFunction;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        DifferentiableMultivariateVectorialFunction function = new SimpleFunction(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, v -> Math.pow(v[0] * v[0] + v[1] * v[1], 2));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(function, target, weights, startPoint);

        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, optimizer.getPoint(), 0.001);
        Assert.assertArrayEquals(target, optimizer.getObjective(), 0.001);

        optimizer.setMaxIterations(5);
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        Assert.assertEquals(5, optimizer.getIterations());
        Assert.assertArrayEquals(result.getPoint(), optimizer.getPoint(), 0.001);
        Assert.assertArrayEquals(result.getValue(), optimizer.getObjective(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededException() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        DifferentiableMultivariateVectorialFunction function = new SimpleFunction(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, v -> Math.pow(v[0] * v[0] + v[1] * v[1], 2));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(function, target, weights, startPoint);

        optimizer.incrementIterationsCounter();

        optimizer.optimize(function, target, weights, startPoint);

        optimizer.optimize(function, target, weights, startPoint);

        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(3);
        DifferentiableMultivariateVectorialFunction function = new SimpleFunction(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}, v -> Math.pow(v[0] * v[0] + v[1] * v[1], 2));
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(function, target, weights, startPoint);
        optimizer.optimize(function, target, weights, startPoint);
        optimizer.optimize(function, target, weights, startPoint);
    }
}