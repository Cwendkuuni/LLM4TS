import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.InitialGuess.METHOD_RANDOM_SEED;
import static org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() throws Exception {
        // Test with a simple 2D quadratic function
        MultivariateFunction function = new PolynomialFunction(2, 2, -1.0, 2.0, 1.0, 3.0);
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {3.0, 3.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("Not used");
            }
        };

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0E-4));

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        double[] expected = {1.5, 1.5};
        Assert.assertArrayEquals(expected, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);

        // Test with a 1D function and bounds
        UnivariateFunction function1D = new PolynomialFunction(1, 1, -1.0, 2.0, 0.0, 1.0);
        double[] target1D = {0.0};
        double[] weights1D = {1.0};
        double[] startPoint1D = {3.0};
        SimpleBounds bounds = new SimpleBounds(0.0, 5.0);

        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("Not used");
            }
        };

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0E-4));
        optimizer.setInitialGuess(new InitialGuess(METHOD_RANDOM_SEED, bounds));

        result = optimizer.optimize(function1D, target1D, weights1D, startPoint1D, MINIMIZE);

        double expected1D = 1.0;
        Assert.assertEquals(expected1D, result.getValue(), 0.001);

        // Test with a function that causes function evaluation exception
        MultivariateFunction functionException = new PolynomialFunction(2, 2, -1.0, 2.0, 1.0, 3.0) {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Test Exception", x);
            }
        };

        double[] targetException = {1.0, 1.0};
        double[] weightsException = {1.0, 1.0};
        double[] startPointException = {3.0, 3.0};

        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("Not used");
            }
        };

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0E-4));

        try {
            optimizer.optimize(functionException, targetException, weightsException, startPointException);
            Assert.fail("Expected MaxEvaluationsExceededException");
        } catch (MaxEvaluationsExceededException e) {
            // Expected
        }

        // Test with a function that causes iterations exception
        MultivariateFunction functionIterationException = new PolynomialFunction(2, 2, -1.0, 2.0, 1.0, 3.0) {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new RuntimeException("Test IterationException");
            }
        };

        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("Not used");
            }
        };

        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0E-4));

        try {
            optimizer.optimize(functionIterationException, target, weights, startPoint);
            Assert.fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a function that causes optimization exception
        MultivariateFunction functionOptimizationException = new PolynomialFunction(2, 2, -1.0, 2.0, 1.0, 3.0) {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new OptimizationException("Test OptimizationException");
            }
        };

        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected PointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException("Not used");
            }
        };

        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0E-4));

        try {
            optimizer.optimize(functionOptimizationException, target, weights, startPoint);
            Assert.fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // Expected
        }

        // Test with an incorrect number of targets and weights
        double[] targetIncorrect = {1.0, 1.0, 1.0};
        double[] weightsIncorrect = {1.0, 1.0};
        double[] startPointIncorrect = {3.0, 3.0, 3.0};

        try {
            optimizer.optimize(function, targetIncorrect, weightsIncorrect, startPointIncorrect);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with an incorrect number of weights
        double[] targetCorrect = {1.0, 1.0};
        double[] weightsCorrect = {1.0};
        double[] startPointCorrect = {3.0, 3.0};

        try {
            optimizer.optimize(function, targetCorrect, weightsCorrect, startPointCorrect);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}