import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.LinearConstraint;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math.optimization.nonlinear.scalar.linear.LinearConstraint;

/**
 * Test class for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /** The optimizer to use for the fitting. */
    private DifferentiableMultivariateVectorialOptimizer optimizer;

    /** The test data. */
    private WeightedObservedPoint[] testData;

    /** The expected parameters. */
    private double[] expectedParameters;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
        testData = new WeightedObservedPoint[] {
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
            new WeightedObservedPoint(4.08366408, 620014.0, 1.0)
        };
        expectedParameters = new double[] { 1.0, 4.055663, 0.336855 };
    }

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}.
     */
    @Test
    public void testConstructor() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertEquals("Expected fitter to be an instance of GaussianFitter",
                     GaussianFitter.class, fitter.getClass());
    }

    /**
     * Test method for {@link GaussianFitter#fit(double[], double[])}.
     */
    @Test
    public void testFit() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit(expectedParameters);
        assertArrayEquals("Expected fit method to return expected parameters",
                          expectedParameters, result);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] result = fitter.fit();
        assertArrayEquals("Expected fit method to return expected parameters",
                          expectedParameters, result);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     */
    @Test
    public void testFitWithFunction() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] result = fitter.fit(function, expectedParameters);
        assertArrayEquals("Expected fit method to return expected parameters",
                          expectedParameters, result);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with negative initial guesses.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeInitialGuesses() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { -1.0, -1.0, -1.0 };
        fitter.fit(function, initialGuesses);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with non-strictly positive initial guesses.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNonStrictlyPositiveInitialGuesses() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { 0.0, 1.0, 0.0 };
        fitter.fit(function, initialGuesses);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with initial guesses out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testFitWithInitialGuessesOutOfBounds() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { Double.POSITIVE_INFINITY,
                                                  Double.POSITIVE_INFINITY,
                                                  Double.POSITIVE_INFINITY };
        fitter.fit(function, initialGuesses);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with a function that doesn't implement {@link ParametricUnivariateRealFunction} or
     * its subinterfaces.
     */
    @Test(expected = ClassCastException.class)
    public void testFitWithFunctionNotImplementingParametricUnivariateRealFunction() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        Object function = new Object() {
            public double value(double x) {
                return 0.0;
            }
            public double[] gradient(double x) {
                return new double[0];
            }
        };
        double[] result = fitter.fit(function, expectedParameters);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with a function that doesn't implement {@link ParametricUnivariateRealFunction} or
     * its subinterfaces.
     */
    @Test(expected = UnconstrainedOptimizationException.class)
    public void testFitWithInvalidFunction() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        Object function = new Object() {};
        fitter.fit(function, expectedParameters);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with bounds.
     */
    @Test
    public void testFitWithBounds() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { 1.0, 4.0, 1.0 };
        SimpleBounds bounds = new SimpleBounds(new double[] { 0.0, 1.0, 0.1 },
                                                new double[] { 1.0, 10.0, 1.0 });
        fitter.optimize(function, initialGuesses, bounds, GoalType.MINIMIZE);
        double[] result = fitter.getSolution();
        assertArrayEquals("Expected fit method to return expected parameters with specified bounds",
                          expectedParameters, result);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with linear constraints.
     */
    @Test
    public void testFitWithLinearConstraints() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { 1.0, 4.0, 1.0 };
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(1.0, 1.0, 0.0),
            new LinearConstraint(2.0, 1.0, 3.0)
        };
        fitter.optimize(function, initialGuesses, constraints, GoalType.MINIMIZE);
        double[] result = fitter.getSolution();
        assertArrayEquals("Expected fit method to return expected parameters with specified linear constraints",
                          expectedParameters, result);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ParametricUnivariateRealFunction, double[])}.
     * Test with nonlinear constraints.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testFitWithNonlinearConstraints() {
        GaussianFitter fitter = new GaussianFitter(optimizer);
        ParametricUnivariateRealFunction function = new Gaussian.Parametric();
        double[] initialGuesses = new double[] { 1.0, 4.0, 1.0 };
        PointValuePair[] constraints = new PointValuePair[] {
            new PointValuePair(2.0, 3.0),
            new PointValuePair(4.0, 5.0)
        };
        fitter.optimize(function, initialGuesses, constraints, GoalType.MINIMIZE);
    }
}