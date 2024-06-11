package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.GeneralVectorFunction;
import org.apache.commons.math3.optim.nonlinear.vector.LevenbergMarquardtOptimizer;

import org.apache.commons.math.optimization.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.LeastSquaresProblem;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test optimization with a simple linear regression problem.
     */
    @org.junit.Test
    public void testLinearRegression() {
        // Set up problem
        int n = 3;
        int m = 1;
        double[][] X = new double[][] {{1, 1}, {1, 2}, {1, 3}};
        double[] y = new double[] {2, 3, 4};
        final LeastSquaresProblem problem = new LeastSquaresProblem(new GeneralVectorFunction(new Polynomials.Polynomial(m, 1)),
                                                                    X, y);

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();
                lmo.setMaxIterations(100);
                double[] initialGuess = new double[m];
                PointValuePair result = lmo.optimize(problem.getFunction(), initialGuess);
                return new VectorialPointValuePair(result.getPoint(), problem.getValue(result.getPoint()));
            }
        };
        // Set up test data
        double[] startPoint = new double[m];
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1;
        }
        double[] target = new double[n];
        for (int i = 0; i < n; i++) {
            target[i] = y[i];
        }

        // Run test
        PointValuePair optimizedPoint = optimizer.optimize(problem.getFunction(), target, weights, startPoint).getPoint();
        RealVector expectedResult = new Array2DRowRealMatrix(1, m).setEntry(0, 0, 1).getColumnVector();
        assertEquals(expectedResult, optimizedPoint);

        // Check gradients
        double[] residuals = new double[n];
        optimizer.updateResidualsAndCost();
        double[] jacobian = optimizer.jacobian;
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(n, m).setEntry(0, 0, 1).getMatrix();
        assertEquals(expectedJacobian.transpose(), new LUDecompositionImpl(expectedJacobian).getSolver().getInverse());
        for (int i = 0; i < n; i++) {
            assertEquals(jacobian[i], expectedJacobian.getEntryMatrix(i, 0).transpose(), 1e-12);
        }
    }

    /**
     * Test optimization with a nonlinear problem.
     */
    @org.junit.Test
    public void testNonlinearRegression() {
        // Set up problem
        int n = 3;
        int m = 2;
        double[][] X = new double[][] {{1, 1}, {1, 2}, {1, 3}};
        double[] y = new double[] {Math.exp(1), Math.exp(2), Math.exp(3)};
        final GeneralVectorFunction f =
                new GeneralVectorFunction(new Polynomials.Polynomial(m, 2)) {
                    @Override
                    public double value(RealVector x) throws FunctionEvaluationException {
                        double[] xArray = x.toArray();
                        return super.value(xArray) + Math.random();
                    }
                };
        final LeastSquaresProblem problem = new LeastSquaresProblem(f, X, y);

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();
                lmo.setMaxIterations(100);
                double[] initialGuess = new double[m];
                PointValuePair result = lmo.optimize(problem.getFunction(), initialGuess);
                return new VectorialPointValuePair(result.getPoint(), problem.getValue(result.getPoint()));
            }
        };

        // Set up test data
        double[] startPoint = new double[m];
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1;
        }
        double[] target = new double[n];
        for (int i = 0; i < n; i++) {
            target[i] = y[i];
        }

        // Run test
        PointValuePair optimizedPoint = optimizer.optimize(problem.getFunction(), target, weights, startPoint).getPoint();
        RealMatrix expectedResult = new Array2DRowRealMatrix(1, m).setEntry(0, 0, 1).setEntry(0, 1, 1).getMatrix();
        assertEquals(expectedResult, optimizedPoint);

        // Check gradients
        double[] residuals = new double[n];
        optimizer.updateResidualsAndCost();
        double[][] jacobian = optimizer.jacobian;
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(n, m).setEntry(0, 0, 1).setEntry(0, 1, 1).getMatrix();
        assertEquals(expectedJacobian.transpose(), new LUDecompositionImpl(expectedJacobian).getSolver().getInverse());
        for (int i = 0; i < n; i++) {
            assertEquals(jacobian[i], expectedJacobian.getEntryMatrix(i, 0).transpose(), 1e-12);
        }
    }

    /**
     * Test optimization with invalid input.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        // Set up problem
        int n = 3;
        int m = 1;
        double[][] X = new double[][] {{1, 1}, {1, 2}, {1, 3}};
        double[] y = new double[] {2, 3, 4};
        final GeneralVectorFunction f = new GeneralVectorFunction(new Polynomials.Polynomial(m, 1)) {
            @Override
            public double value(RealVector x) throws FunctionEvaluationException {
                throw new UnboundedDomainException();
            }
        };
        final LeastSquaresProblem problem = new LeastSquaresProblem(f, X, y);

        // Set up optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        // Set up test data
        double[] startPoint = new double[m];
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1;
        }
        double[] target = new double[n];
        for (int i = 0; i < n; i++) {
            target[i] = y[i];
        }

        // Run test
        optimizer.optimize(problem.getFunction(), target, weights, startPoint);
    }
}