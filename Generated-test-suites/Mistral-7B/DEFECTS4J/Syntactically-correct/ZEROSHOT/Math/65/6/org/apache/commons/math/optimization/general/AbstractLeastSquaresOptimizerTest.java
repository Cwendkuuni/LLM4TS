import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer(); // Using LevenbergMarquardtOptimizer as an example
    }

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertArrayEquals(new int[]{100}, optimizer.getMaxIterations());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        Assert.assertArrayEquals(new int[]{5}, optimizer.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIterationsNegativeValue() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-1);
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        double[][] jacobian = new double[2][3];
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.updateJacobian();
        Assert.assertEquals(jacobian, optimizer.getJacobian());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianFunctionEvaluationException() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new ThrowingMockFunction();
        optimizer.setFunction(function);
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] point = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.optimize(function, target, weights, point);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, optimizer.getResiduals());
        Assert.assertEquals(Math.sqrt(2.0), optimizer.getCost(), 1e-6);
    }

    @Test
    public void testGetRMS() {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        Assert.assertEquals(Math.sqrt(2.0), optimizer.getRMS(), 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        Assert.assertEquals(2.0, optimizer.getChiSquare(), 1e-6);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        double[][] covariances = optimizer.getCovariances();
        Assert.assertArrayEquals(new double[][]{
                {0.5, 0.0},
                {0.0, 0.5}
        }, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        optimizer.setFunction(function);
        optimizer.optimize(function, target, weights, new double[]{0.0, 0.0});
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertArrayEquals(new double[]{Math.sqrt(0.5), Math.sqrt(0.5)}, errors);
    }

    @Test
    public void testOptimizeSimpleExample() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{3.0, 3.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        Assert.assertEquals(new double[]{3.0, 3.0}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new ThrowingMockFunction();
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        function.setConvergenceChecker(new MockConvergenceChecker(false));
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullFunction() {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        PointValuePair result = optimizer.optimize(null, target, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullTarget() {
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        PointValuePair result = optimizer.optimize(function, null, weights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullWeights() {
        double[] target = new double[]{1.0, 2.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        PointValuePair result = optimizer.optimize(function, target, null, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNullStartPoint() {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        PointValuePair result = optimizer.optimize(function, target, weights, null);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeInvalidBounds() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractLeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        DifferentiableMultivariateVectorialFunction function = new MockFunction();
        UnivariateOptimizer univariateOptimizer = new Minimizer(new MockFunction2(5), new SimpleBounds(10.0, 20.0));
        optimizer.setUnivariateOptimizer(univariateOptimizer);
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
    }

    private static class MockFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            double[] result = new double[2];
            result[0] = point[0] * point[0] + point[1] * point[1];
            result[1] = point[0] * point[0] - point[1] * point[1];
            return result;
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MockJacobianFunction();
        }

        private class MockJacobianFunction implements MultivariateMatrixFunction {

            @Override
            public double[] value(double[] point) throws FunctionEvaluationException {
                double[][] result = new double[2][2];
                result[0][0] = 2.0 * point[0];
                result[0][1] = 2.0 * point[1];
                result[1][0] = 2.0 * point[0];
                result[1][1] = -2.0 * point[1];
                return result;
            }

        }

        @Override
        public String getName() {
            return "MockFunction";
        }

    }

    private static class ThrowingMockFunction extends MockFunction {

        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(point);
        }

    }

    private static class MockConvergenceChecker implements VectorialConvergenceChecker {

        private final boolean shouldThrow;

        public MockConvergenceChecker(boolean shouldThrow) {
            this.shouldThrow = shouldThrow;
        }

        @Override
        public boolean isConverged(double[] x, double[] f, double[] gradient, double precision) {
            if (shouldThrow) {
                throw new RuntimeException("Simulated convergence check failure");
            }
            return Math.abs(FunctionUtils.sum(f)) < precision;
        }

    }

    private static class MockFunction2 implements UnivariateFunction {

        private final double a;

        public MockFunction2(double a) {
            this.a = a;
        }

        @Override
        public double value(double x) {
            return a * Math.sin(x) - x;
        }

        @Override
        public double[] derivative(double x) {
            return new double[]{a * Math.cos(x)};
        }

        @Override
        public String getName() {
            return "MockFunction2";
        }

    }

    private static class Minimizer extends UnivariateOptimizer {

        private AbstractLeastSquaresOptimizer optimizer;

        public Minimizer(UnivariateFunction function, SimpleBounds bounds) {
            super(function, bounds);
            optimizer = new LevenbergMarquardtOptimizer();
        }

        @Override
        public PointValuePair optimize() throws OptimizationException, FunctionEvaluationException {
            double[] point = getInitialGuess();
            AbstractLeastSquaresOptimizer.VectorialPointValuePair result = optimizer.optimize(
                    new MockFunction()::value, getTargetValues(), getWeights(), point
            );
            return new PointValuePair(point, result.getValue());
        }

    }

}