package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    // Test data for Gauss-Newton Optimizer
    private static final double[] TARGET = {0.0, 1.0, 2.0};
    private static final double[][] WEIGHT = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
    private static final double[] START_POINT = {1.0, 1.0, 1.0};

    @Test
    public void testGaussNewtonOptimizer() {
        OptimizationProblem problem = new LeastSquaresProblem(new LinearRegressionFunction(TARGET), START_POINT);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        PointVectorValuePair result = optimizer.optimize(problem);
        assertArrayEquals(new double[]{-0.5, 1.5, 1.5}, result.getPoint(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        OptimizationProblem problem = new LeastSquaresProblem(null, START_POINT);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.optimize(problem);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testEmptyWeight() {
        OptimizationProblem problem = new LeastSquaresProblem(new LinearRegressionFunction(TARGET), START_POINT);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        problem.setWeight(new Array2DRowRealMatrix(new double[0][]));
        optimizer.optimize(problem);
    }

    @Test(expected = ConvergenceException.class)
    public void testNonConvergentOptimizer() {
        OptimizationProblem problem = new LeastSquaresProblem(new NonConvergentFunction(), START_POINT);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.optimize(problem);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularNormalEquations() {
        OptimizationProblem problem = new LeastSquaresProblem(new SingularFunction(), START_POINT);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.optimize(problem);
    }

    // Helper classes for testing
    private static class LinearRegressionFunction implements OptimizationFunction {
        private final double[] target;

        public LinearRegressionFunction(double[] target) {
            this.target = target;
        }

        @Override
        public double value(RealVector point) throws MathException {
            RealVector x = new ArrayRealVector(point);
            RealMatrix jacobian = jacobianValue(x);
            double sumOfSquares = 0.0;
            for (int i = 0; i < target.length; i++) {
                double residual = target[i] - jacobian.operate(new ArrayRealVector(new double[]{i})).getEntry(0);
                sumOfSquares += residual * residual * WEIGHT[i][i];
            }
            return sumOfSquares;
        }

        @Override
        public RealVector gradient(RealVector point) throws MathException {
            RealVector x = new ArrayRealVector(point);
            RealMatrix jacobian = jacobianValue(x);
            RealVector gradient = new ArrayRealVector(nValues(x), false);
            for (int i = 0; i < nValues(x); i++) {
                gradient.setEntry(i, -2.0 * jacobian.getEntry(i, i) * WEIGHT[i][i] * jacobian.getColumnVector(i).dotProduct(x));
            }
            return gradient;
        }

        private RealMatrix jacobianValue(RealVector x) {
            RealMatrix jacobian = new Array2DRowRealMatrix(nValues(x), nValues(x));
            for (int i = 0; i < nValues(x); i++) {
                jacobian.setEntry(i, i, 1.0);
            }
            for (int i = 0; i < nValues(x); i++) {
                for (int j = 0; j < nValues(x); j++) {
                    jacobian.setEntry(i, j, WEIGHT[i][j] * x.getEntry(j));
                }
            }
            return jacobian;
        }

        private int nValues(RealVector point) {
            return point.getDimension();
        }
    }

    private static class NonConvergentFunction implements OptimizationFunction {
        @Override
        public double value(RealVector point) throws MathException {
            throw new ConvergenceException(LocalizedFormats.UNABLE_TO_CONVERGE);
        }

        @Override
        public RealVector gradient(RealVector point) throws MathException {
            throw new ConvergenceException(LocalizedFormats.UNABLE_TO_CONVERGE);
        }
    }

    private static class SingularFunction implements OptimizationFunction {
        @Override
        public double value(RealVector point) throws MathException {
            return 0.0;
        }

        @Override
        public RealVector gradient(RealVector point) throws MathException {
            RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
            LUDecomposition luDecomposition = new LUDecomposition(A);
            RealMatrix L = luDecomposition.getL();
            RealMatrix U = luDecomposition.getU();
            return U.transpose().operate(new ArrayRealVector(new double[]{1.0, 1.0}));
        }
    }
}