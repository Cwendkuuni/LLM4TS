import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.arithmetic.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSetFitnessFunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        // Test default constructor
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertArrayEquals(new int[]{AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS,
                Integer.MAX_VALUE, Integer.MAX_VALUE}, optimizer.getIterationLimits());

        // Test constructor with custom settings
        AbstractLeastSquaresOptimizer optimizer2 = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException();
            }
        };
        optimizer2.setMaxIterations(10);
        optimizer2.setMaxEvaluations(5);
        Assert.assertEquals(10, optimizer2.getMaxIterations());
        Assert.assertEquals(5, optimizer2.getMaxEvaluations());

        // Test updates after optimization
        MultivariateFunction function = (x -> new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        }.value(new double[]{2.0, 3.0}));
        AbstractLeastSquaresOptimizer optimizer3 = new AbstractLeastSquaresOptimizer();
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        Assert.assertArrayEquals(new double[]{1.5, 1.5}, optimizer3.getPoint(), 0.001);
        Assert.assertEquals(Math.sqrt(2.0), optimizer3.getCost(), 0.001);

        // Test constraints
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, -1.0}, 0.0));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 0.0));
        optimizer3.setConstraints(constraints);
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        Assert.assertArrayEquals(new double[]{0.5, 0.5}, optimizer3.getPoint(), 0.001);
        Assert.assertEquals(Math.sqrt(2.0 / 2.0), optimizer3.getCost(), 0.001);

        // Test bounds
        optimizer3.setConstraints(null);
        optimizer3.setBounds(new SimpleBounds(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0}));
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        Assert.assertArrayEquals(new double[]{0.5, 0.5}, optimizer3.getPoint(), 0.001);
        Assert.assertEquals(Math.sqrt(2.0 / 2.0), optimizer3.getCost(), 0.001);

        // Test objective function with custom goal type
        ObjectiveFunction customGoalFunction = (f, x) -> {
            double value = f.value(x);
            double[] gradient = f.valueGradient(x);
            return new PointValuePair(value, gradient);
        };
        optimizer3.setObjectiveFunction(customGoalFunction);
        optimizer3.setGoalType(GoalType.MINIMIZE);
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        Assert.assertArrayEquals(new double[]{0.5, 0.5}, optimizer3.getPoint(), 0.001);
        Assert.assertEquals(Math.sqrt(2.0 / 2.0), optimizer3.getCost(), 0.001);

        // Test cost getters
        optimizer3.setObjectiveFunction(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) * Math.pow(Math.E, Math.pow(x[0], 2.0))
                        + Math.pow(Math.E, Math.pow(x[1], 2.0));
            }

            @Override
            public double[] valueGradient(double[] x) {
                double[] gradient = new double[x.length];
                for (int i = 0; i < gradient.length; i++) {
                    gradient[i] = 2.0 * ((i == 0) ? Math.E * Math.pow(x[0], 2.0) : Math.E * Math.pow(x[1], 2.0));
                }
                return gradient;
            }
        });
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        double expectedCost = Math.PI * Math.PI * Math.E * Math.E + Math.E * Math.E;
        Assert.assertEquals(expectedCost, optimizer3.getCost(), 0.001);
        Assert.assertEquals(expectedCost, optimizer3.getRMS(), 0.001);
        Assert.assertEquals(expectedCost, optimizer3.getChiSquare(), 0.001);

        // Test covariances and errors
        optimizer3.setVerbose(true);
        optimizer3.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
        double[][] covariances = optimizer3.getCovariances();
        double[] errors = optimizer3.guessParametersErrors();
        Random random = new Random();
        double[] initialPoint = new double[]{random.nextGaussian(), random.nextGaussian()};
        AbstractLeastSquaresOptimizer optimizer4 = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                throw new UnsupportedOperationException();
            }
        };
        optimizer4.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{initialPoint[0], initialPoint[1]});
        double[] initialErrors = optimizer4.guessParametersErrors();
        Assert.assertArrayEquals(covariances, optimizer3.getCovariances(), 0.001);
        Assert.assertArrayEquals(errors, optimizer3.guessParametersErrors(), 0.001);
        Assert.assertArrayEquals(initialErrors, optimizer4.guessParametersErrors(), 0.001);
    }

}