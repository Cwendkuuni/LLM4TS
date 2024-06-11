package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.optim.initializer.Guess;
import org.apache.commons.math3.optim.initializer.OneSigmaOverSqrtN;
import org.apache.commons.math3.optim.initializer.ZeroVector;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.restart.Restart;
import org.apache.commons.math3.optim.restart.RestartStrategy;
import org.apache.commons.math3.optim.restart.SimpleRestartPolicy;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GaussianFitter;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializer.OneSigmaOverSqrtN;
import org.junit.Test;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}.
     */
    @Test
    public void testGaussianFitter() {
        DifferentiableMultivariateVectorialOptimizer optimizer = new LeastSquaresOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertNotNull(fitter);
    }

    /**
     * Test method for {@link GaussianFitter#fit(double[], double[], double[], double[], double[], GoalType, LinearConstraints)}.
     */
    @Test
    public void testFit() {
        double[] observationsX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976,
            4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] observationsY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0,
            2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };
        PointValuePair expectedResult = new PointValuePair(new double[] { 1.0, 4.05, 0.2 }, 4917217.769759576);
        GaussianFitter fitter = new GaussianFitter(new LeastSquaresOptimizer());
        fitter.addObservedPoint(observationsX[0], observationsY[0]);
        for (int i = 1; i < observationsX.length; i++) {
            fitter.addObservedPoint(observationsX[i], observationsY[i]);
        }
        double[] initialGuess = new double[] { 2.0, 4.0, 0.5 };
        ObjectiveFunction objectiveFunction = new Gaussian.Parametric();
        GoalType goalType = GoalType.MINIMIZE;
        LinearConstraints constraints = new LinearConstraints(new LinearConstraint[] {
                new LinearConstraint("norm >= 0")
        });
        SimpleBounds bounds = new SimpleBounds(new double[] { 0.0, -1.0, 0.0 }, new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY });
        RestartStrategy restart = new SimpleRestartPolicy();
        Restart restartOptimizer = new Restart(restart);
        Guess initialGuessFunction = new OneSigmaOverSqrtN(observationsX.length);
        Guess initialGuessParameter = new ZeroVector(3);
        PointValuePair result = fitter.fit(objectiveFunction, initialGuess, initialGuessFunction, bounds, goalType, constraints, restartOptimizer);
        assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 0.0001);
        assertEquals(expectedResult.getValue(), result.getValue(), 0.0001);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     */
    @Test
    public void testFitNoInitialGuess() {
        double[] observationsX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976,
            4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] observationsY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0,
            2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };
        GaussianFitter fitter = new GaussianFitter(new LeastSquaresOptimizer());
        fitter.addObservedPoint(observationsX[0], observationsY[0]);
        for (int i = 1; i < observationsX.length; i++) {
            fitter.addObservedPoint(observationsX[i], observationsY[i]);
        }
        double[] expectedResult = new double[] { 1.0, 4.05, 0.2 };
        PointValuePair result = fitter.fit();
        assertArrayEquals(expectedResult, result.getPoint(), 0.0001);
    }

}