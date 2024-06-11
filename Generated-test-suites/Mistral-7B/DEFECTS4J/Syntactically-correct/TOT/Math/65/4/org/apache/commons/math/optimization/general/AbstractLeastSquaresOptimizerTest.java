package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BFGS;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test with a simple function
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return -Math.pow(Math.sin(x[0]), 2) - Math.pow(Math.sin(x[1]), 2) + 2;
            }

            @Override
            public double[] gradient(double[] x) {
                double[] g = new double[x.length];
                for (int i = 0; i < x.length; i++) {
                    g[i] = -2 * Math.sin(x[i]);
                    if (i == 0) {
                        g[i] -= 2 * Math.cos(x[i]) * Math.sin(x[1]);
                    } else if (i == 1) {
                        g[i] -= 2 * Math.cos(x[0]) * Math.sin(x[1]);
                    }
                }
                return g;
            }
        };
        double[] target = {1.0, 1.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {2.0, 2.0};
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(f, new SimpleBounds(startPoint, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        PointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        Assert.assertArrayEquals(new double[]{Math.PI / 2, Math.PI / 2}, result.getPoint(), 1e-7);
        Assert.assertEquals(-3.141592653589793, result.getValue(), 1e-7);

        // Test with a function having only one variable
        ObjectiveFunction f1var = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]), 2) + 2;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{-2 * Math.sin(x[0])};
            }
        };
        double[] target1var = {1.0};
        double[] weights1var = {1.0};
        double[] startPoint1var = {0.0};
        AbstractLeastSquaresOptimizer optimizer1var = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(f1var, new SimpleBounds(startPoint1var, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        PointValuePair result1var = optimizer1var.optimize(f1var, target1var, weights1var, startPoint1var);
        Assert.assertEquals(Math.PI / 2, result1var.getPoint()[0], 1e-7);
        Assert.assertEquals(-1.0, result1var.getValue(), 1e-7);

        // Test with a non-converging function
        ObjectiveFunction fNonConverging = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 1.0) {
                    throw new NumberIsTooLargeException("x[0] too large: " + x[0]);
                }
                return -Math.pow(Math.sin(x[0]), 2) - Math.pow(Math.sin(x[1]), 2) + 2;
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        double[] targetNonConverging = {1.0, 1.0};
        double[] weightsNonConverging = {1.0, 1.0};
        double[] startPointNonConverging = {3.0, 3.0};
        AbstractLeastSquaresOptimizer optimizerNonConverging = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(fNonConverging, new SimpleBounds(startPointNonConverging, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValueValuePair(solution.getPoint(), solution.getValue());
            }
        };

        try {
            optimizerNonConverging.optimize(fNonConverging, targetNonConverging, weightsNonConverging, startPointNonConverging);
            Assert.fail("Expected an exception but no exception was thrown.");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        // Test with a non-differentiable function
        ObjectiveFunction fNonDifferentiable = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.abs(Math.sin(x[0]));
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not differentiable at x[0]=" + x[0]);
            }
        };
        double[] targetNonDifferentiable = {1.0};
        double[] weightsNonDifferentiable = {1.0};
        double[] startPointNonDifferentiable = {0.0};
        AbstractLeastSquaresOptimizer optimizerNonDifferentiable = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(fNonDifferentiable, new SimpleBounds(startPointNonDifferentiable, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        try {
            optimizerNonDifferentiable.optimize(fNonDifferentiable, targetNonDifferentiable, weightsNonDifferentiable, startPointNonDifferentiable);
            Assert.fail("Expected an exception but no exception was thrown.");
        } catch (FunctionEvaluationException e) {
            Assert.assertEquals("Not differentiable at x[0]=" + startPointNonDifferentiable[0], e.getMessage());
        }

        // Test with a function with invalid number of dimensions
        ObjectiveFunction fInvalidDimensions = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new IllegalArgumentException("Function fInvalidDimensions does not accept a vector of size " + x.length);
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        double[] targetInvalidDimensions = {1.0, 1.0};
        double[] weightsInvalidDimensions = {1.0, 1.0};
        double[] startPointInvalidDimensions = {1.0, 1.0, 1.0};
        AbstractLeastSquaresOptimizer optimizerInvalidDimensions = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(fInvalidDimensions, new SimpleBounds(startPointInvalidDimensions, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        try {
            optimizerInvalidDimensions.optimize(fInvalidDimensions, targetInvalidDimensions, weightsInvalidDimensions, startPointInvalidDimensions);
            Assert.fail("Expected an exception but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Function fInvalidDimensions does not accept a vector of size 3", e.getMessage());
        }

        // Test with a function having a non-positive number of degrees of freedom
        ObjectiveFunction fNonPositiveDOF = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2 * x[0]};
            }
        };
        double[] targetNonPositiveDOF = {1.0};
        double[] weightsNonPositiveDOF = {1.0};
        double[] startPointNonPositiveDOF = {1.0};
        AbstractLeastSquaresOptimizer optimizerNonPositiveDOF = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                BFGS optimizer = new BFGS(fNonPositiveDOF, new SimpleBounds(startPointNonPositiveDOF, null), GoalType.MINIMIZE, MaxEval.INFINITY, 1.0e-6);
                PointValuePair solution = optimizer.optimize();
                return new VectorialPointValuePair(solution.getPoint(), solution.getValue());
            }
        };

        try {
            optimizerNonPositiveDOF.optimize(fNonPositiveDOF, targetNonPositiveDOF, weightsNonPositiveDOF, startPointNonPositiveDOF);
            Assert.fail("Expected an exception but no exception was thrown.");
        } catch (OptimizationException e) {
            Assert.assertEquals(LocalizedFormats.NO_DEGREES_OF_FREEDOM, e.getMessage());
        }
    }

}