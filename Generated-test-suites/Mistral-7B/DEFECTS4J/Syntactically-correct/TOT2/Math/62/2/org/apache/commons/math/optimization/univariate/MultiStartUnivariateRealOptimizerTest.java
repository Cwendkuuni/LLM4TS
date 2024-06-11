package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoRealSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateRealObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePairCollection;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-6;
    private static final double ACCURACY = 1e-5;

    private UnivariateRealFunction f;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        f = new SinFunction();
        optimizer = new MultiStartUnivariateRealOptimizer<>(new FminUnivariateRealOptimizer<>(), 5, new MersenneTwister());
        optimizer.setMaxEvaluations(30);
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testMinimalFunction() throws Exception {
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        double min = optimizer.optimize(f, GoalType.MINIMIZE, bounds).getValue();
        Assert.assertEquals(0.0, min, EPSILON);
    }

    @Test
    public void testMaximalFunction() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        double max = optimizer.optimize(f, GoalType.MAXIMIZE, bounds).getValue();
        Assert.assertEquals(1.0, max, EPSILON);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new SinFunctionWithException(), GoalType.MINIMIZE, bounds);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new DivergentFunction(), GoalType.MINIMIZE, bounds);
    }

    @Test
    public void testDifferentStartingPoints() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        PointValuePair[] optima = optimizer.optimize(f, GoalType.MINIMIZE, bounds, null);
        List<Double> minima = new ArrayList<>();
        for (PointValuePair pvp : optima) {
            if (pvp != null) {
                minima.add(pvp.getPoint().getDouble());
            }
        }
        Collection<Double> expectedMinima = Arrays.asList(0.0, 0.25, 0.5, 0.75, 1.0);
        Collection<Double> actualMinima = new ArrayList<>(minima);
        Assert.assertEquals(expectedMinima, actualMinima);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        optimizer.setMaxEvaluations(2);
        optimizer.optimize(f, GoalType.MINIMIZE, bounds);
        Assert.assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testNoSolution() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        optimizer.setMaxEvaluations(10);
        UnivariateRealObjectiveFunction function = new NoSolutionFunction();
        PointValuePair pvp = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
        Assert.assertNull(pvp);
    }

    @Test
    public void testMaxEvaluationsWithException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(0.0, 1.0);
        optimizer.setMaxEvaluations(1);
        UnivariateRealObjectiveFunction function = new MaxEvalExceededFunction();
        PointValuePair pvp = optimizer.optimize(function, GoalType.MINIMIZE, bounds);
        Assert.assertNull(pvp);
    }

    @Test
    public void testEmptyoptimizer() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> emptyOptimizer = new MultiStartUnivariateRealOptimizer<>(null, 5, new MersenneTwister());
        Assert.assertNull(emptyOptimizer.optimize(new SinFunction(), GoalType.MINIMIZE, new SimpleBounds(0.0, 1.0)));
    }

    @Test
    public void testEmptyoptimizerWithMaxEvaluations() {
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> emptyOptimizer = new MultiStartUnivariateRealOptimizer<>(null, 5, new MersenneTwister());
        emptyOptimizer.setMaxEvaluations(10);
        Assert.assertNull(emptyOptimizer.optimize(new SinFunction(), GoalType.MINIMIZE, new SimpleBounds(0.0, 1.0)));
    }

    private static class SinFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    private static class SinFunctionWithException implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException("function evaluation exception");
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }

    private static class DivergentFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            throw new RuntimeException();
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }
    }

    private static class NoSolutionFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) throws NoRealSolutionException {
            throw new NoRealSolutionException();
        }

        @Override
        public double derivative(double x) {
            return 0.0;
        }
    }

    private static class MaxEvalExceededFunction implements UnivariateRealFunction {

        private int maxEvaluationsUsed = 0;

        @Override
        public double value(double x) throws MaxEvalExceededException {
            maxEvaluationsUsed++;
            if (maxEvaluationsUsed > 1) {
                throw new MaxEvalExceededException();
            }
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            maxEvaluationsUsed++;
            return Math.cos(x);
        }
    }

    private static class AveragedStartingPointGenerator implements UnivariateOptimizer.StartingPointGenerator<UnivariateRealFunction> {

        private double lowerBound;
        private double upperBound;

        public AveragedStartingPointGenerator(double lowerBound, double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public PointValuePair generateStartingPoint(UnivariateRealFunction objectiveFunction) {
            return new PointValuePair(lowerBound + (upperBound - lowerBound)/2.0, objectiveFunction.value(lowerBound + (upperBound - lowerBound)/2.0));
        }

        @Override
        public Collection<PointValuePair> generateStartingPoints(int numberOfStartingPoints, UnivariateRealFunction objectiveFunction) {
            PointValuePairCollection result = new PointValuePairCollection();
            for (int i = 0; i < numberOfStartingPoints; i++) {
                result.add(generateStartingPoint(objectiveFunction));
            }
            return result;
        }
    }
}