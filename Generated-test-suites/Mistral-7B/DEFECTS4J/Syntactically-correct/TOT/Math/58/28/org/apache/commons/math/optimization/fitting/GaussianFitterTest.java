package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussianNewtonOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class GaussianFitterTest {
    private static final double EPSILON = 1e-9;
    private OptimizationProblem problem;
    private WeightedObservedPoint[] points;
    private double[] expectedParameters;

    @Before
    public void setUp() {
        problem = new OptimizationProblem() {
            public double[] getParameterVector() {
                return new double[]{0.0, 0.0, 0.0};
            }

            public ObjectiveFunction getObjectiveFunction() {
                return new ObjectiveFunction() {
                    public double value(double[] param) {
                        UnivariateFunction function = new Gaussian.Parametric(param[0], param[1], param[2]);
                        double sumSquaredErrors = 0.0;
                        for (WeightedObservedPoint p : points) {
                            sumSquaredErrors += Math.pow(p.getX() - function.value(p.getX()), 2.0);
                        }
                        return sumSquaredErrors;
                    }

                    public int getNumberOfVariables() {
                        return 3;
                    }
                };
            }
        };
        points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
            new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
            new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
            new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
            new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
            new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
            new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
            new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
            new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
            new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
            new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
            new WeightedObservedPoint(4.08366408, 620014.0, 1.0),
        };
        expectedParameters = new double[]{500000.0, 4.05, 0.2};
    }

    @Test
    public void testFit() throws OptimaException, NumberIsTooSmallException {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(points);
        double[] parameters = fitter.fit();
        assertArrayEquals(expectedParameters, parameters, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullParameterInitialGuess() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyObservedPoints() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullObservedPoints() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitTooFewObservedPoints() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(new WeightedObservedPoint(0.0, 0.0, 1.0));
        fitter.fit();
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitOutOfRangeX() throws OutOfRangeException {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] pointsWithNegativeX = new WeightedObservedPoint[] {
            new WeightedObservedPoint(-1.0, 531026.0, 1.0),
            new WeightedObservedPoint(-2.0, 984167.0, 1.0)
        };
        fitter.addObservedPoints(pointsWithNegativeX);
        assertThrows(OutOfRangeException.class, () -> fitter.fit());
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitOutOfRangeY() throws OutOfRangeException {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] pointsWithOutOfRangeY = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0, -1.0, 1.0),
            new WeightedObservedPoint(4.0, -2.0, 1.0)
        };
        fitter.addObservedPoints(pointsWithOutOfRangeY);
        assertThrows(OutOfRangeException.class, () -> fitter.fit());
    }

    @Test(expected = OutOfRangeException.class)
    public void testFitZeroParameterInitialGuess() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(new double[]{0.0, 0.0, 0.0});
        assertThrows(OutOfRangeException.class, fitter::getObjectiveFunction);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testFitMaxCountExceeded() {
        Optimizer optimizer = new GaussianNewtonOptimizer(1);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(points);
        assertThrows(MaxCountExceededException.class, () -> fitter.fit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullOptimizer() {
        GaussianFitter fitter = new GaussianFitter(null);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit());
    }

    @Test
    public void testFitWithZeroWeightObservedPoints() {
        Optimizer optimizer = new GaussianNewtonOptimizer(100);
        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] pointsWithZeroWeights = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 0.0),
            new WeightedObservedPoint(4.03128248, 984167.0, 0.0),
            new WeightedObservedPoint(4.03839603, 1887233.0, 0.0),
            new WeightedObservedPoint(4.04421621, 2687152.0, 0.0),
            new WeightedObservedPoint(4.05132976, 3461228.0, 0.0),
            new WeightedObservedPoint(4.05326982, 3580526.0, 0.0),
            new WeightedObservedPoint(4.05779662, 3439750.0, 0.0),
            new WeightedObservedPoint(4.0636168, 2877648.0, 0.0),
            new WeightedObservedPoint(4.06943698, 2175960.0, 0.0),
            new WeightedObservedPoint(4.07525716, 1447024.0, 0.0),
            new WeightedObservedPoint(4.08237071, 717104.0, 0.0),
            new WeightedObservedPoint(4.08366408, 620014.0, 0.0),
        };
        fitter.addObservedPoints(pointsWithZeroWeights);
        fitter.setGoalType(GoalType.MINIMIZE);
        double[] parameters = fitter.fit();
        assertEquals(0.0, parameters[0], EPSILON);
        assertEquals(0.0, parameters[1], EPSILON);
        assertEquals(0.0, parameters[2], EPSILON);
    }
}