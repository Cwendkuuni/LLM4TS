package org.apache.commons.math.optimization.general;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorFormat;
import org.apache.commons.math3.optim.PointValuePair;

public class AbstractLeastSquaresOptimizerTest extends TestCase {

    public void testAbstractLeastSquaresOptimizer() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                Assert.fail("doOptimize should not be called from test case.");
                return null;
            }
        };

        // Default constructor
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(50);

        // Test with valid input
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0}, false);

        DifferentiableMultivariateVectorialFunction f = new LinearRegressionFunction(A, b);
        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        PointValuePair result = optimizer.optimize(f, target, weights, startPoint);

        Assert.assertEquals(3, result.getValue().length);
        Assert.assertEquals(3, result.getPoint().length);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getValue(), 0.001);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0}, result.getPoint(), 0.001);

        // Test with maximal number of iterations exceeded
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                Assert.fail("doOptimize should not be called from test case.");
                return null;
            }
        };
        optimizer.setMaxIterations(0);

        try {
            optimizer.optimize(f, target, weights, startPoint);
            fail("Expected MaxIterationsExceededException.");
        } catch (MaxIterationsExceededException e) {
            // expected
        }

        // Test with maximal number of evaluations exceeded
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                Assert.fail("doOptimize should not be called from test case.");
                return null;
            }
        };
        optimizer.setMaxEvaluations(0);

        try {
            optimizer.optimize(f, target, weights, startPoint);
            fail("Expected MaxEvaluationsExceededException.");
        } catch (MaxEvaluationsExceededException e) {
            // expected
        }

        // Test with invalid input (wrong dimensions)
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                Assert.fail("doOptimize should not be called from test case.");
                return null;
            }
        };

        try {
            optimizer.optimize(new LinearRegressionFunction(A, b), new double[]{1.0}, new double[]{1.0}, new double[]{2.0});
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid target or weights dimensions.", e.getMessage());
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(A, b), new double[]{1.0, 2.0}, new double[]{1.0, 1.0},
                              new double[]{3.0});
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid start point dimension.", e.getMessage());
        }
    }

    private static class LinearRegressionFunction implements DifferentiableMultivariateVectorialFunction {
        private final RealMatrix A;
        private final RealVector b;

        public LinearRegressionFunction(RealMatrix A, RealVector b) {
            this.A = A;
            this.b = b;
        }

        @Override
        public double[] value(double[] x) throws FunctionEvaluationException {
            RealVector xVec = VectorFormat.toRealVector(x, false);
            RealVector y = A.operate(xVec);
            double[] result = new double[y.getDimension()];
            for (int i = 0; i < result.length; i++) {
                result[i] = y.getEntry(i) - b.getEntry(i);
            }
            return result;
        }

        @Override
        public MultivariateMatrixFunction jacobian() throws FunctionEvaluationException {
            return new MultivariateMatrixFunction() {
                @Override
                public double[][] value(double[] x) throws FunctionEvaluationException {
                    return A.getData();
                }
            };
        }
    }
}