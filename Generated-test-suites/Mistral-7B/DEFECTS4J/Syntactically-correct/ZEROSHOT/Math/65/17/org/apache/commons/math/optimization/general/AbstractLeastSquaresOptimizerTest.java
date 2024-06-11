import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSparseMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test default constructor
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        // Test setting and getting max iterations
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());

        // Test setting and getting max evaluations
        optimizer.setMaxEvaluations(20);
        Assert.assertEquals(20, optimizer.getMaxEvaluations());

        // Test setting and getting convergence checker
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());

        // Test incrementing iterations counter
        try {
            optimizer.incrementIterationsCounter();
            Assert.assertEquals(1, optimizer.getIterations());
        } catch (OptimizationException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test updating jacobian
        DifferentiableMultivariateVectorialFunction function = new MultivariateVectorFunction(new double[][]{
                {Math.pow(x, 2), Math.sin(x)},
                {Math.cos(x), Math.exp(x)}
        });
        try {
            optimizer.updateJacobian();
            Assert.assertNotNull(optimizer.jacobian);
            Assert.assertEquals(4, optimizer.jacobian.length);
        } catch (FunctionEvaluationException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test updating residuals and cost
        double[] target = new double[]{1, 0};
        double[] weights = new double[]{1, 1};
        double[] startPoint = new double[]{1, 1};
        double[] residuals = new double[]{0, 0};
        try {
            optimizer.updateResidualsAndCost();
            Assert.assertEquals(Math.sqrt(2), optimizer.cost, 0.01);
        } catch (FunctionEvaluationException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test getRMS
        Assert.assertEquals(Math.sqrt(2), optimizer.getRMS(), 0.01);

        // Test getChiSquare
        Assert.assertEquals(2, optimizer.getChiSquare(), 0.01);

        // Test getCovariances
        try {
            double[][] covariances = optimizer.getCovariances();
            Assert.assertNotNull(covariances);
            Assert.assertEquals(2, covariances.length);
            Assert.assertEquals(2, covariances[0].length);
        } catch (FunctionEvaluationException | OptimizationException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test guessParametersErrors
        try {
            double[] errors = optimizer.guessParametersErrors();
            Assert.assertArrayEquals(new double[]{Math.sqrt(2) / Math.sqrt(2.0 + 1.0), Math.sqrt(2) / Math.sqrt(2.0 + 1.0)}, errors, 0.01);
        } catch (FunctionEvaluationException | OptimizationException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test optimize
        PointValuePair result = null;
        try {
            result = optimizer.optimize(function, target, weights, startPoint);
            Assert.assertNotNull(result.getPoint());
            Assert.assertArrayEquals(new double[]{0, 0}, result.getPoint(), 0.01);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test doOptimize (abstract method)
        // Cannot test AbstractLeastSquaresOptimizer directly

        // Test LevenbergMarquardtOptimizer
        AbstractLeastSquaresOptimizer levMarqOptimizer = new LevenbergMarquardtOptimizer();
        levMarqOptimizer.setMaxIterations(10);
        levMarqOptimizer.setMaxEvaluations(20);
        levMarqOptimizer.setConvergenceChecker(checker);

        result = null;
        try {
            result = levMarqOptimizer.optimize(function, target, weights, startPoint);
            Assert.assertNotNull(result.getPoint());
            Assert.assertArrayEquals(new double[]{0, 0}, result.getPoint(), 0.01);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            // Should not throw an exception
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

    }

}