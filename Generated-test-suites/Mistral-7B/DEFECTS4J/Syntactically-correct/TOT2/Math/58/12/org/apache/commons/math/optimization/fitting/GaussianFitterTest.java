import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.GaussianFunction;
import org.apache.commons.math3.optim.nonlinear.MaxEval;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.convergence.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.convergence.IterationNumber;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.optimize.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.fitting.GaussianFitter;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;

/**
 * Test cases for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Test fitting a simple Gaussian to a set of points.
     */
    @Test
    public void testFitSimpleGaussian() {
        // Prepare data
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 4.5, 5.5, 6.5, 8.5 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(data);
        double[] parameters = fitter.fit();

        // Verify results
        double expectedMean = 3.0;
        double expectedStdDev = 1.0;
        double expectedAmplitude = 1.5;
        assertEquals("Unexpected mean", expectedMean, parameters[1], 1e-5);
        assertEquals("Unexpected standard deviation", expectedStdDev, parameters[2], 1e-5);
        assertEquals("Unexpected amplitude", expectedAmplitude, parameters[0], 1e-5);

        // Verify fitted function
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFunction function = new GaussianFunction(parameters);
        PointValuePair bestPoint = optimizer.optimize(function, new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), new MaxEval(100), new IterationNumber(100), new StoppingCondition(1e-8));
        double x = (double) bestPoint.getX();
        double y = function.value(x, parameters);
        assertEquals("Unexpected function value", yData[0], y, 1e-5);
    }

    /**
     * Test fitting a Gaussian with negative amplitude to a set of points.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeAmplitudeGaussian() {
        // Prepare data
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 4.5, 5.5, 6.5, 8.5 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(data);
        double[] parameters = fitter.fit(-1.0, new double[] { 3.0, 1.0 });
    }

    /**
     * Test fitting a Gaussian with empty data set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitEmptyData() {
        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit();
    }

    /**
     * Test fitting a Gaussian with too few initial guesses.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitTooFewInitialGuesses() {
        // Prepare data
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 4.5, 5.5, 6.5, 8.5 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(data);
        fitter.fit(new double[] { 1.0 });
    }

    /**
     * Test fitting a Gaussian with too many initial guesses.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFitTooManyInitialGuesses() {
        // Prepare data
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 4.5, 5.5, 6.5, 8.5 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(data);
        fitter.fit(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0, 6.0 });
    }

    /**
     * Test fitting a Gaussian with null observations.
     */
    @Test(expected = NullPointerException.class)
    public void testFitNullObservations() {
        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    /**
     * Test fitting a Gaussian with null initial guesses.
     */
    @Test(expected = NullPointerException.class)
    public void testFitNullInitialGuesses() {
        // Prepare data
        double[] xData = { 1.0, 2.0, 3.0, 4.0, 5.0 };
        double[] yData = { 2.5, 4.5, 5.5, 6.5, 8.5 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Execute test
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(data);
        fitter.fit((double[]) null);
    }
}