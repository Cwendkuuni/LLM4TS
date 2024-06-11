package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the AbstractLeastSquaresOptimizer class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new TestLeastSquaresOptimizer();
    }

    /**
     * Test optimization with valid input.
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testOptimizationValidInput() throws Exception {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        Assert.assertArrayEquals(new double[] {1.0, 1.0}, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test optimization with invalid input (wrong number of dimensions).
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWrongNumberOfDimensions() throws Exception {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with wrong convergence checker.
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWrongConvergenceChecker() throws Exception {
        optimizer.setConvergenceChecker(null);
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test optimization with wrong number of iterations.
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testOptimizationWrongNumberOfIterations() throws Exception {
        optimizer.setMaxIterations(0);
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            Assert.fail("An exception should have been thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test optimization with wrong number of evaluations.
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testOptimizationWrongNumberOfEvaluations() throws Exception {
        optimizer.setMaxEvaluations(0);
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        try {
            optimizer.optimize(function, target, weights, startPoint);
            Assert.fail("An exception should have been thrown");
        } catch (MaxEvaluationsExceededException e) {
            // Expected
        }
    }

    /**
     * Test optimization with a non-differentiable function.
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testOptimizationNonDifferentiableFunction() throws Exception {
        optimizer.setConvergenceChecker(null);
        FunctionNonDifferentiable f = new FunctionNonDifferentiable();
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that always returns INF.
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizationFunctionAlwaysReturnsINF() throws Exception {
        optimizer.setMaxEvaluations(1);
        FunctionAlwaysReturnsINF f = new FunctionAlwaysReturnsINF();
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that always returns NaN.
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizationFunctionAlwaysReturnsNaN() throws Exception {
        optimizer.setMaxIterations(1);
        FunctionAlwaysReturnsNaN f = new FunctionAlwaysReturnsNaN();
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that throws an exception.
     * @throws Exception if an error occurs during optimization
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizationFunctionThrowsException() throws Exception {
        optimizer.setMaxIterations(1);
        FunctionThrowsException f = new FunctionThrowsException();
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        optimizer.optimize(f, target, weights, startPoint);
    }

    /**
     * Test optimization with a function that has a solution but is too close to a singularity.
     * @throws Exception if an error occurs during optimization
     */
    @Test
    public void testOptimizationFunctionSingularity() throws Exception {
        optimizer.setMaxIterations(1000);
        FunctionSingularity f = new FunctionSingularity();
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0};

        PointValuePair result = optimizer.optimize(f, target, weights, startPoint);

        Assert.assertTrue(result.getValue() > 0.999);
        Assert.assertTrue(result.getValue() < 1.001);
    }
}

class TestLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    // Empty test implementation
}

class FunctionNonDifferentiable implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function is not differentiable at this point", x);
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function is not differentiable at this point", x);
    }

    @Override
    public RealMatrix jacobian(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Function is not differentiable at this point", x);
    }
}

class FunctionAlwaysReturnsINF implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(LocalizedFormats.NEGATIVE_VALUE_FOR_NON_NEGATIVE_FUNCTION);
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        return new double[] {Double.POSITIVE_INFINITY};
    }

    @Override
    public RealMatrix jacobian(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(LocalizedFormats.NEGATIVE_VALUE_FOR_NON_NEGATIVE_FUNCTION);
    }
}

class FunctionAlwaysReturnsNaN implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(LocalizedFormats.NAN_RESULT_FOR_NON_NAN_FUNCTION);
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        throw new FunctionEvaluationException(LocalizedFormats.NAN_RESULT_FOR_NON_NAN_FUNCTION);
    }

    @Override
    public RealMatrix jacobian(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(LocalizedFormats.NAN_RESULT_FOR_NON_NAN_FUNCTION);
    }
}

class FunctionThrowsException implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        throw new RuntimeException("Exception thrown by function");
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        throw new RuntimeException("Exception thrown by function");
    }

    @Override
    public RealMatrix jacobian(double[] x) throws FunctionEvaluationException {
        throw new RuntimeException("Exception thrown by function");
    }
}

class FunctionSingularity implements DifferentiableMultivariateVectorialFunction {

    @Override
    public double value(double[] x) throws FunctionEvaluationException {
        return Math.sqrt(Math.abs(x[0] * x[0] + x[1] * x[1])) + 1.0;
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) throws FunctionEvaluationException {
        if (index == 0) {
            grad[0] = 2.0 * x[0] / (Math.sqrt(x[0] * x[0] + x[1] * x[1]) + 1.0);
        } else {
            grad[1] = 2.0 * x[1] / (Math.sqrt(x[0] * x[0] + x[1] * x[1]) + 1.0);
        }
        return grad;
    }

    @Override
    public RealMatrix jacobian(double[] x) throws FunctionEvaluationException {
        RealMatrix jacobian = MatrixUtils.createRealMatrix(2, 2);
        jacobian.setEntry(0, 0, 2.0 * x[0] / (Math.sqrt(x[0] * x[0] + x[1] * x[1]) + 1.0));
        jacobian.setEntry(0, 1, 0.0);
        jacobian.setEntry(1, 0, 0.0);
        jacobian.setEntry(1, 1, 2.0 * x[1] / (Math.sqrt(x[0] * x[0] + x[1] * x[1]) + 1.0));
        return jacobian;
    }
}