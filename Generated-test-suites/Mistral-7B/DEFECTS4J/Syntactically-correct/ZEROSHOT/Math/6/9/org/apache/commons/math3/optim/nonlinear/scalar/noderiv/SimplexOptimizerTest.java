import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.TestValues;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.goal.GoalType.MINIMIZE;
import static org.apache.commons.math3.optim.goal.GoalType.MAXIMIZE;
import static org.apache.commons.math3.optim.initializer.RandomInitializer.FACTORY;
import static org.apache.commons.math3.optim.point.PointValuePair.pair;
import static org.apache.commons.math3.optim.simplextour.SimpleSimplex.*;
import static org.apache.commons.math3.optim.simplextour.SimpleValueChecker.DEFAULT;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimizerMinimizingFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return -Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, NEWTON, MAXIMUM_ITERATIONS, 1.0e-6, DEFAULT);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, MINIMIZE, new double[2]);
        PointValuePair expected = TestValues.SIN;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }

    @Test
    public void testSimpleOptimizerMaximizingFunction() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, NEWTON, MAXIMUM_ITERATIONS, 1.0e-6, DEFAULT);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, MAXIMIZE, new double[2]);
        PointValuePair expected = TestValues.SIN_SQUARE;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }

    @Test
    public void testSimpleOptimizerConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, NEWTON, MAXIMUM_ITERATIONS, 1.0e-5, new SimpleValueChecker(1.0e-6, 0.0));
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0e-6, 0.0));
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, MAXIMIZE, new double[2]);
        PointValuePair expected = TestValues.SIN_SQUARE;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }

    @Test
    public void testSimpleOptimizerRandomStart() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, RANDOM_SEARCH, MAXIMUM_ITERATIONS, 1.0e-6, DEFAULT);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, MAXIMIZE, new double[2]);
        PointValuePair expected = TestValues.SIN_SQUARE;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }

    @Test
    public void testSimpleOptimizerInitialGuess() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, NEWTON, MAXIMUM_ITERATIONS, 1.0e-6, DEFAULT);
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair initialGuess = pair(0.1, 0.2);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public OptimizationData setInitialGuess(OptimizationData data) {
                data.setInitialGuess(initialGuess);
                return data;
            }
        };
        PointValuePair result = optimizer.optimize(100, function, MAXIMIZE, optimizationData);
        PointValuePair expected = TestValues.SIN_SQUARE;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }

    @Test
    public void testSimpleOptimizerInitialGuessWithSimplex() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2);
            }
        };
        AbstractSimplex simplex = new SimpleSimplex(2, NEWTON, MAXIMUM_ITERATIONS, 1.0e-6, DEFAULT);
        OptimizationData optimizationData = FACTORY.createOptimizationData(new double[2]);
        optimizationData.setInitialGuess(pair(0.1, 0.2));
        optimizationData.setFunction(function);
        simplex.setSize(function.getNumberOfVariables());
        simplex.build(optimizationData.getInitialGuess());
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(100, function, MAXIMIZE, optimizationData);
        PointValuePair expected = TestValues.SIN_SQUARE;
        Assert.assertEquals("Optimum found at wrong point.", expected, result, 1e-6);
    }
}