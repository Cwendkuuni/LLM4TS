import org.apache.commons.math3.optim.initialization.Guess;
import org.apache.commons.math3.optim.initialization.SimpleBounds;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.interpolators.BSplineInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.fitting.GaussianFitter;
import org.apache.commons.math3.optim.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    private static final double EPSILON = 1e-7;
    private GaussianFitter fitter;
    private UnivariateFunction gaussianFunction;
    private WeightedObservedPoint[] observedPoints;

    @Before
    public void setUp() {
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFunction = new Gaussian.Parametric();
        observedPoints = new WeightedObservedPoint[] {
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
                new WeightedObservedPoint(4.08888888, 500000.0, 1.0) // Edge case
        };
    }

    @Test
    public void testFit() {
        double[] parameters = fitter.fit(observedPoints);
        PointValuePair fitResult = gaussianFunction.valueAndDerivative(observedPoints[0].getX(), parameters, null);
        Assert.assertEquals(observedPoints[0].getY(), fitResult.getValue(), EPSILON);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeFitX() {
        double x = Double.NEGATIVE_INFINITY;
        fitter.fit(new WeightedObservedPoint[] {new WeightedObservedPoint(x, 0.0, 1.0)});
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeFitY() {
        double y = Double.NEGATIVE_INFINITY;
        fitter.fit(new WeightedObservedPoint[] {new WeightedObservedPoint(0.0, y, 1.0)});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObservedPoints() {
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithTooFewObservedPoints() {
        WeightedObservedPoint[] observedPoints = {new WeightedObservedPoint(0.0, 0.0, 1.0)};
        fitter.fit(observedPoints);
    }

    @Test
    public void testGuessParameters() {
        double[] guessParameters = new ParameterGuesser(observedPoints).guess();
        PointValuePair fitResult = gaussianFunction.valueAndDerivative(observedPoints[0].getX(), guessParameters, null);
        Assert.assertEquals(observedPoints[0].getY(), fitResult.getValue(), EPSILON);
    }
}