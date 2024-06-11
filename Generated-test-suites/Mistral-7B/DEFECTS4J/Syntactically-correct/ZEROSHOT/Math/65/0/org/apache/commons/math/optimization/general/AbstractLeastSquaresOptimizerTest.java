import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.gradient.NonlinearConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.vector.gradient.NonlinearConjugateGradient.LineSearchType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test default constructor
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
        optimizer.setMaxEvaluations(5);
        Assert.assertEquals(5, optimizer.getMaxEvaluations());

        // Test with conjugate gradient optimizer
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -Math.pow(x[0] * x[0] + x[1] * x[1], 2);
            }

            @Override
            public double[] gradientValue(double[] x) {
                double[] grad = new double[2];
                grad[0] = -2 * x[0] * Math.pow(x[0] * x[0] + x[1] * x[1], 1);
                grad[1] = -2 * x[1] * Math.pow(x[0] * x[0] + x[1] * x[1], 1);
                return grad;
            }
        };

        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{2.0, 2.0};

        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        double[] optimalPoint = result.getPoint();
        double optimalValue = result.getValue();

        Assert.assertArrayEquals(new double[]{0.0, 0.0}, optimalPoint, 0.001);
        Assert.assertEquals(0.0, optimalValue, 0.001);

        // Test with bounds
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double[] getInitialPoint(int dimension) {
                double[] point = new double[dimension];
                new Random().nextBytes(point);
                return point;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});

        optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-6));
        optimizer.setMaxIterations(100);
        optimizer.setInitialGuess(initialGuess);
        optimizer.setBounds(bounds);
        optimizer.setGoalType(GoalType.MINIMIZE);

        NonlinearConjugateGradient optimizerAlgorithm = new NonlinearConjugateGradient(optimizer);
        optimizerAlgorithm.setLineSearchType(LineSearchType.QUADRATIC);
        optimizerAlgorithm.optimize(function, startPoint);

        result = optimizerAlgorithm.getCurrentPointValue();
        optimalPoint = result.getPoint();
        optimalValue = result.getValue();

        Assert.assertArrayEquals(new double[]{0.0, 0.0}, optimalPoint, 0.001);
        Assert.assertEquals(0.0, optimalValue, 0.001);
    }

}