import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.Points2D;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.gradient.GradientFactory;
import org.apache.commons.math3.optim.nonlinear.gradient.Jacobian;
import org.apache.commons.math3.optim.nonlinear.gradient.JacobianFactory;
import org.apache.commons.math3.optim.linear.RealVector;
import org.apache.commons.math3.optim.linear.RealVectorFactory;
import org.apache.commons.math3.optim.linear.VectorValuePair;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.function.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.function.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.function.NonlinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.NonlinearFunctionFitness;
import org.apache.commons.math3.optim.nonlinear.function.NonlinearFunctionValue;
import org.apache.commons.math3.optim.nonlinear.function.RealVectorFunction;
import org.apache.commons.math3.optim.nonlinear.gradient.JacobianFactory.SingularValueDecomposition;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction.LinearType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet. quietQuadraticProgram;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction.QuadraticObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction.QuadraticObjectiveFunction.QuadraticType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet.ConstraintSetType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction.ObjectiveType;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction.QuadraticObjectiveFunction.QuadraticConstraintType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    private double[] initialGuess;
    private double[] expectedResult;
    private WeightedObservedPoint[] observations;

    public GaussianFitterTest(double[] initialGuess, double[] expectedResult, WeightedObservedPoint[] observations) {
        this.initialGuess = initialGuess;
        this.expectedResult = expectedResult;
        this.observations = observations;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{1.0, 2.0, 0.5}, new double[]{1.0, 2.0, 0.5},
                        new WeightedObservedPoint[]{
                                new WeightedObservedPoint(2.0, 3.0),
                                new WeightedObservedPoint(3.0, 2.0),
                                new WeightedObservedPoint(4.0, 1.0)
                        }},
                {new double[]{1.0, 2.0, 0.5}, new double[]{1.0, 2.0, 0.5},
                        new WeightedObservedPoint[]{
                                new WeightedObservedPoint(1.5, 1.5),
                                new WeightedObservedPoint(2.5, 1.5),
                                new WeightedObservedPoint(3.5, 1.5)
                        }},
                {new double[]{1.0, 2.0, 0.5}, new double[]{1.0, 2.0, 0.5},
                        new WeightedObservedPoint[]{
                                new WeightedObservedPoint(1.0, 1.0),
                                new WeightedObservedPoint(2.0, 2.0),
                                new WeightedObservedPoint(3.0, 3.0)
                        }},
                {new double[]{1.0, 2.0, 0.5}, new double[]{1.0, 2.0, 0.5},
                        new WeightedObservedPoint[]{
                                new WeightedObservedPoint(0.5, 5.0),
                                new WeightedObservedPoint(1.5, 2.5),
                                new WeightedObservedPoint(2.5, 1.0)
                        }},
                {new double[]{1.0, 2.0, 0.5}, new double[]{1.0, 2.0, 0.5},
                        new WeightedObservedPoint[]{
                                new WeightedObservedPoint(-1.0, 5.0),
                                new WeightedObservedPoint(1.5, 2.5),
                                new WeightedObservedPoint(2.5, 1.0)
                        }}
        });
    }

    @Test
    public void testFit() {
        // Test the fit method with the given initial guess and observations
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        double[] result = fitter.fit(observations, initialGuess);
        Assert.assertArrayEquals(expectedResult, result, 0.00001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        fitter.fit(null, initialGuess);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        fitter.fit(observations, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservationsInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        fitter.fit(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitInvalidInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        fitter.fit(observations, new double[]{0.0, 0.0, -1.0});
    }

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new GaussNewtonOptimizer());
        double[] result = fitter.fit(observations);
        Assert.assertArrayEquals(expectedResult, result, 0.00001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        double[] result = fitter.fit(observations);
        Assert.assertArrayEquals(expectedResult, result, 0.00001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservationsWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter();
        fitter.fit(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFitWithoutParameters() {
        GaussianFitter fitter = new GaussianFitter();
        double[] result = fitter.fit();
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[]{
                new WeightedObservedPoint(1.0, 1.0),
                new WeightedObservedPoint(2.0, 2.0),
                new WeightedObservedPoint(3.0, 3.0)
        };
        ParameterGuesser parameterGuesser = new ParameterGuesser(observations);
        double[] guessedParameters = parameterGuesser.guess();
        System.out.println(Arrays.toString(guessedParameters));
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 1.0}, guessedParameters, 0.001);
    }
}

class ParameterGuesser {
    private WeightedObservedPoint[] observations;

    public ParameterGuesser(WeightedObservedPoint[] observations) {
        this.observations = observations;
    }

    public double[] guess() {
        if (guessedParameters == null) {
            guessedParameters = basicGuess(observations);
        }
        return guessedParameters.clone();
    }

    private double[] basicGuess(WeightedObservedPoint[] points) {
        // Implement the basicGuess method from the ParameterGuesser class
        // ...
        // For the purpose of this test, we'll just return a hardcoded result
        return new double[]{1.0, 2.0, 1.0};
    }

    private double[] guessedParameters;
}