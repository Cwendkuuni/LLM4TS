package org.apache.commons.math.optimization.fitting;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a Gaussian function to a set of points.
     */
    @Test
    public void testFitFunction() {
        Double[][] data = new Double[][] { { 4.0254623, 531026.0 },
                                          { 4.03128248, 984167.0 },
                                          { 4.03839603, 1887233.0 },
                                          { 4.04421621, 2687152.0 },
                                          { 4.05132976, 3461228.0 },
                                          { 4.05326982, 3580526.0 },
                                          { 4.05779662, 3439750.0 },
                                          { 4.0636168, 2877648.0 },
                                          { 4.06943698, 2175960.0 },
                                          { 4.07525716, 1447024.0 },
                                          { 4.08237071, 717104.0 },
                                          { 4.08366408, 620014.0 } };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[13];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        DifferentiableMultivariateVectorFunction optimizer =
                new LevenbergMarquardtOptimizer();

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);

        PointValuePair solution = fitter.optimize();
        double[] parameters = solution.getPoint();

        assertEquals(3, parameters.length);
        assertEquals(531026.0, parameters[0], 0.001); // Norm
        assertEquals(4.032571, parameters[1], 0.001); // Mean
        assertEquals(0.527644, parameters[2], 0.001); // Sigma

        // Check that fitted function matches original data
        double[] xValues = new double[13];
        for (int i = 0; i < observations.length; i++) {
            xValues[i] = observations[i].getX();
        }

        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        ObjectiveFunction objectiveFunction = new LeastSquaresProblem(xValues, function, observations);
        GoalType goalType = GoalType.MINIMIZE;

        OptimizationResult result = new NelderMeadSimplex(10, new PointValuePair[10], null, null)
                .optimize(objectiveFunction, goalType, new InitialGuess(parameters));

        double[] yValues = new double[13];
        for (int i = 0; i < observations.length; i++) {
            yValues[i] = observations[i].getY();
        }

        assertArrayEquals(yValues, result.getPointValues(), 0.001);
    }

    /**
     * Test fitting a Gaussian function with zero width.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitFunctionZeroWidth() {
        Double[][] data = new Double[][] { { 4.0254623, 531026.0 },
                                          { 4.03128248, 984167.0 },
                                          { 4.03839603, 1887233.0 },
                                          { 4.04421621, 2687152.0 },
                                          { 4.05132976, 3461228.0 },
                                          { 4.05326982, 3580526.0 },
                                          { 4.05779662, 3439750.0 },
                                          { 4.0636168, 2877648.0 },
                                          { 4.06943698, 2175960.0 },
                                          { 4.07525716, 1447024.0 },
                                          { 4.08237071, 717104.0 },
                                          { 4.08366408, 620014.0 } };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[13];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        DifferentiableMultivariateVectorFunction optimizer =
                new LevenbergMarquardtOptimizer();

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        fitter.setInitialGuess(new double[] { 1.0E-20, 4.0, 1.0E-20 });
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian function with negative initial guess.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitFunctionNegativeInitialGuess() {
        Double[][] data = new Double[][] { { 4.0254623, 531026.0 },
                                          { 4.03128248, 984167.0 },
                                          { 4.03839603, 1887233.0 },
                                          { 4.04421621, 2687152.0 },
                                          { 4.05132976, 3461228.0 },
                                          { 4.05326982, 3580526.0 },
                                          { 4.05779662, 3439750.0 },
                                          { 4.0636168, 2877648.0 },
                                          { 4.06943698, 2175960.0 },
                                          { 4.07525716, 1447024.0 },
                                          { 4.08237071, 717104.0 },
                                          { 4.08366408, 620014.0 } };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[13];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(data[i][0], data[i][1], 1.0);
        }

        DifferentiableMultivariateVectorFunction optimizer =
                new LevenbergMarquardtOptimizer();

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);
        fitter.setInitialGuess(new double[] { -1.0, 4.0, -1.0 });
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian function with null observations.
     */
    @Test(expected = NullPointerException.class)
    public void testFitFunctionNullObservations() {
        DifferentiableMultivariateVectorFunction optimizer =
                new LevenbergMarquardtOptimizer();

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.fit(null);
    }
}