package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionValue;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Tests fitting of Gaussian function to provided points.
     */
    @Test
    public void testFit() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 1),
            new WeightedObservedPoint(4.03128248, 984167.0, 1),
            new WeightedObservedPoint(4.03839603, 1887233.0, 1),
            new WeightedObservedPoint(4.04421621, 2687152.0, 1),
            new WeightedObservedPoint(4.05132976, 3461228.0, 1),
            new WeightedObservedPoint(4.05326982, 3580526.0, 1),
            new WeightedObservedPoint(4.05779662, 3439750.0, 1),
            new WeightedObservedPoint(4.0636168, 2877648.0, 1),
            new WeightedObservedPoint(4.06943698, 2175960.0, 1),
            new WeightedObservedPoint(4.07525716, 1447024.0, 1),
            new WeightedObservedPoint(4.08237071, 717104.0, 1),
            new WeightedObservedPoint(4.08366408, 620014.0, 1)
        };

        GaussianFitter fitter = new GaussianFitter(new BroydenFletcherGoldfarbShanno());
        double[] parameters = fitter.fit(points);
        assertEquals(3, parameters.length);

        NonlinearOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        UnivariateFunction f = new GaussianFunction(parameters);

        PointValuePairCollection result = optimizer.optimize(f, null, GoalType.MINIMIZE);
        PointValuePair point = result.getPoint();
        assertEquals("Norm", point.getX(), parameters[0], 1e-5);
        assertEquals("Mean", point.getX() + 1, parameters[1], 1e-5);
        assertEquals("Sigma", parameters[2], point.getX() - 1, 1e-5);

        double expectedSumSquareError = 0;
        for (WeightedObservedPoint wp : points) {
            expectedSumSquareError += Math.pow(wp.getY() - f.value(wp.getX()), 2.0) * wp.getWeight();
        }

        assertEquals("Total sum of squared errors", expectedSumSquareError, StatUtils.sumOfSquares(points, 1), 1e-5);
    }

    /**
     * Tests fitting of Gaussian function to provided points with constraint.
     */
    @Test
    public void testFitWithConstraint() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.0, 1.0, 1),
            new WeightedObservedPoint(3.0, 0.5, 1),
            new WeightedObservedPoint(4.0, 2.0, 1),
            new WeightedObservedPoint(5.0, 3.0, 1)
        };
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {
                new LinearConstraint(1.0, 1.0, 2.0),
                new LinearConstraint(1.0, 2.0, -1.0),
                new LinearConstraint(1.0, 3.0, 1.0)
        });

        GaussianFitter fitter = new GaussianFitter(new BroydenFletcherGoldfarbShanno());
        fitter.addConstraints(constraints);
        double[] initialGuess = new double[] { 1.0, 3.0, 1.0 };
        double[] parameters = fitter.fit(points, initialGuess);

        assertEquals(3, parameters.length);

        NonlinearOptimizer optimizer = new BroydenFletcherGoldfarbShanno();
        UnivariateFunction f = new GaussianFunction(parameters);

        PointValuePairCollection result = optimizer.optimize(f, null, GoalType.MINIMIZE);
        PointValuePair point = result.getPoint();

        assertEquals("Norm", point.getX(), parameters[0], 1e-5);
        assertEquals("Mean", point.getX() + 1, parameters[1], 1e-5);
        assertEquals("Sigma", parameters[2], point.getX() - 1, 1e-5);

        for (WeightedObservedPoint wp : points) {
            double error = wp.getY() - f.value(wp.getX());
            assertEquals("Error at point: " + wp.getX(), 0.0, error, 1e-5);
        }
    }

    /**
     * Tests that fitting method throws NoRootsException when no valid solution is found.
     */
    @Test
    public void testFitNoRootsException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(0.0, 1.0, 1),
            new WeightedObservedPoint(1.0, -1.0, 1),
            new WeightedObservedPoint(2.0, 3.0, 1)
        };

        GaussianFitter fitter = new GaussianFitter(new BroydenFletcherGoldfarbShanno());
        assertThrows(NoRootsException.class, () -> fitter.fit(points));
    }

    /**
     * Tests that fitting method throws UnsupportedOperationException when the
     * provided optimizer is not supported.
     */
    @Test
    public void testFitUnsupportedOperationException() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(3.0, 2.0, 1),
            new WeightedObservedPoint(4.0, 3.0, 1),
            new WeightedObservedPoint(5.0, 4.0, 1)
        };

        GaussianFitter fitter = new GaussianFitter(new Object() {});
        assertThrows(UnsupportedOperationException.class, () -> fitter.fit(points));
    }

    /**
     * Tests that fitting method throws NullArgumentException when no points are
     * provided.
     */
    @Test
    public void testFitNullArgumentException() {
        GaussianFitter fitter = new GaussianFitter(new BroydenFletcherGoldfarbShanno());
        assertThrows(NullArgumentException.class, () -> fitter.fit(null));
    }

    /**
     * Gaussian function for testing.
     */
    private static class GaussianFunction implements UnivariateFunction {
        private final double[] params;

        public GaussianFunction(double[] params) {
            this.params = params;
        }

        @Override
        public double value(double x) throws NumberIsTooSmallException {
            if (x < params[1] - 3 * params[2] || x > params[1] + 3 * params[2]) {
                throw new NumberIsTooSmallException();
            }
            return Math.exp(-Math.pow(x - params[1], 2) / (2 * Math.pow(params[2], 2)));
        }
    }
}