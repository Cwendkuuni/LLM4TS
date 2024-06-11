import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.optim.initializer.StartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtIterativeOptimizer;
import org.apache.commons.math3.optim.nonlinear.levenbergmarquardt.LevenbergMarquardtOptimizer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.math3.optim.fitting.GaussianFitter.ParameterGuesser.*;
import static org.junit.Assert.assertArrayEquals;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private double[] observedPointsX;
    private double[] observedPointsY;

    public GaussianFitterTest(double[] observedPointsX, double[] observedPointsY) {
        this.observedPointsX = observedPointsX;
        this.observedPointsY = observedPointsY;
    }

    @Parameters(name = "{index}: ({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{4.0254623, 531026.0}, new double[]{531026.0, 4.0254623}},
                {new double[]{4.03128248, 984167.0}, new double[]{984167.0, 4.03128248}},
                {new double[]{4.03839603, 1887233.0}, new double[]{1887233.0, 4.03839603}},
                {new double[]{4.04421621, 2687152.0}, new double[]{2687152.0, 4.04421621}},
                {new double[]{4.05132976, 3461228.0}, new double[]{3461228.0, 4.05132976}},
                {new double[]{4.05326982, 3580526.0}, new double[]{3580526.0, 4.05326982}},
                {new double[]{4.05779662, 3439750.0}, new double[]{3439750.0, 4.05779662}},
                {new double[]{4.0636168, 2877648.0}, new double[]{2877648.0, 4.0636168}},
                {new double[]{4.06943698, 2175960.0}, new double[]{2175960.0, 4.06943698}},
                {new double[]{4.07525716, 1447024.0}, new double[]{1447024.0, 4.07525716}},
                {new double[]{4.08237071, 717104.0}, new double[]{717104.0, 4.08237071}},
                {new double[]{4.08366408, 620014.0}, new double[]{620014.0, 4.08366408}},
        });
    }

    @Test
    public void testFitWithCustomOptimizer() {
        // Given
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter gaussianFitter = new GaussianFitter(optimizer);
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }

    @Test
    public void testFitWithDefaultOptimizer() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservedPointsX() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        double[] observedPointsY = new double[10];

        // When & Then
        gaussianFitter.fit((double[]) null, observedPointsY);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitWithNullObservedPointsY() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        double[] observedPointsX = new double[10];

        // When & Then
        gaussianFitter.fit(observedPointsX, (double[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithLessThanThreeObservedPoints() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        double[] observedPointsX = new double[2];
        double[] observedPointsY = new double[2];

        // When & Then
        gaussianFitter.fit(observedPointsX, observedPointsY);
    }

    @Test
    public void testFitWithGaussNewtonOptimizer() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        gaussianFitter.setOptimizer(new GaussNewtonOptimizer());
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }

    @Test
    public void testFitWithLevenbergMarquardtIterativeOptimizer() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        gaussianFitter.setOptimizer(new LevenbergMarquardtIterativeOptimizer());
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }

    @Test
    public void testFitWithAveragedStartingPointInitializer() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        gaussianFitter.setOptimizer(new LevenbergMarquardtOptimizer());
        gaussianFitter.setInitialGuessStrategy(new AveragedStartingPointInitializer(10));
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }

    @Test
    public void testFitWithStartingPointInitializer() {
        // Given
        GaussianFitter gaussianFitter = new GaussianFitter();
        gaussianFitter.setOptimizer(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[]{1.0, 1.0, 1.0};
        gaussianFitter.setInitialGuessStrategy(new StartlingPointInitializer(initialGuess));
        gaussianFitter.addObservedPoint(observedPointsX[0], observedPointsY[0]);
        for (int i = 1; i < observedPointsX.length; i++) {
            gaussianFitter.addObservedPoint(observedPointsX[i], observedPointsY[i]);
        }

        // When
        double[] parameters = gaussianFitter.fit();

        // Then
        double[] expectedParameters = basicGuess(new WeightedObservedPoint[observedPointsX.length][], observedPointsX, observedPointsY);
        assertArrayEquals(expectedParameters, parameters, 0.00001);
    }
}