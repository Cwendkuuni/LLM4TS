package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.convergence.AbstractConvergenceChecker;
import org.apache.commons.math3.optim.convergence.IterationData;
import org.apache.commons.math3.optim.initializer.StartPointSelector;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.junit.Test;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /**
     * Test method for {@link AbstractLeastSquaresOptimizer#optimize(DifferentiableMultivariateVectorialFunction, double[], double[], double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testOptimize() throws Exception {
        // Test with a simple function
        ObjectiveFunction function = new SimpleFunction();
        double[] target = new double[] {1.0};
        double[] weights = new double[] {1.0};
        double[] startPoint = new double[] {0.0};

        // Define the test optimizer
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new BrentOptimizer(new AbstractConvergenceChecker() {
                    @Override
                    public IterationData checkConvergence(int iteration, double fxNew, double[] xNew) {
                        return new IterationData(fxNew, xNew, IterationData.CONVERGED, null);
                    }
                }, GoalType.MINIMIZE, new StartPointSelector() {
                    @Override
                    public double[] getStartPoint() {
                        return startPoint;
                    }
                }).optimize(function, target, weights, startPoint);
            }
        };

        // Perform the optimization
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // Check the result
        assertArrayEquals(new double[] {1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    /**
     * Test method for {@link AbstractLeastSquaresOptimizer#optimize(DifferentiableMultivariateVectorialFunction, double[], double[], double[])}.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeUnboundedDomain() throws Exception {
        ObjectiveFunction function = new UnboundedFunction();
        double[] target = new double[] {};
        double[] weights = new double[] {};
        double[] startPoint = new double[] {0.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test method for {@link AbstractLeastSquaresOptimizer#optimize(DifferentiableMultivariateVectorialFunction, double[], double[], double[])}.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeIllegalArgument() throws Exception {
        ObjectiveFunction function = new SimpleFunction();
        double[] target = new double[] {1.0};
        double[] weights = new double[] {1.0, 2.0};
        double[] startPoint = new double[] {0.0, 0.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.optimize(function, target, weights, startPoint);
    }

    /**
     * Test method for {@link AbstractLeastSquaresOptimizer#optimize(DifferentiableMultivariateVectorialFunction, double[], double[], double[])}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testOptimizeEdgeCases() throws Exception {
        // Test with a simple function with different signs for errors
        ObjectiveFunction function = new EdgeFunction();
        double[] target = new double[] {1.0, -1.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {0.0, 0.0};

        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new BrentOptimizer(new AbstractConvergenceChecker() {
                    @Override
                    public IterationData checkConvergence(int iteration, double fxNew, double[] xNew) {
                        return new IterationData(fxNew, xNew, IterationData.CONVERGED, null);
                    }
                }, GoalType.MINIMIZE, new StartPointSelector() {
                    @Override
                    public double[] getStartPoint() {
                        return startPoint;
                    }
                }).optimize(function, target, weights, startPoint);
            }
        };

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // Check the result
        assertArrayEquals(new double[] {0.5, -0.5}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

}

class SimpleFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Math.pow(Math.PI - x[0], 2);
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        // Not used
        return null;
    }
}

class UnboundedFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        // Not used
        return null;
    }
}

class EdgeFunction implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Math.pow(Math.PI - x[0], 2) - Math.pow(Math.PI + x[1], 2);
    }

    @Override
    public double[] value(double[] x, double[] grad, int index) {
        // Not used
        return null;
    }
}