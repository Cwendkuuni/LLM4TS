package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.GoalType;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceCheckerFactory;
import org.apache.commons.math3.optim.nonlinear.de.AbstractDifferentialEvolution;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;
import org.junit.Assert;

import static org.apache.commons.math3.optim.PointValuePair.Pair;
import static org.apache.commons.math3.optim.PointValuePair.pair;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.RANDOM_1_2_RECOMBINATION;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.RANDOM_PENALTY_MUTATION;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.RANDOM_SCALE_RECOMBINATION;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.RANDOM_STRATEGY;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.RND;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.TABU_MEMORY_SIZE;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.USE_DERIVATIVES;
import static org.apache.commons.math3.optim.nonlinear.de.DifferentialEvolution.USE_TABU_LIST;

public class BaseOptimizerTest<PAIR> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        Assert.assertEquals(5, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        optimizer.optimize(new PointValuePair(new double[]{1.0}, 1.0), GoalType.MINIMIZE, new SimpleValueChecker(0.001, MaxEval.MAX_COUNT));
        Assert.assertEquals(5, optimizer.getEvaluations());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetEvaluationsBeforeOptimization() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
        Assert.assertTrue(convergenceChecker instanceof SimpleValueChecker);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConvergenceCheckerBeforeOptimization() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker);
        optimizer.optimize(new PointValuePair(new double[]{1.0}, 1.0), GoalType.MINIMIZE, convergenceChecker);
        ConvergenceChecker<PAIR> convergenceChecker2 = optimizer.getConvergenceChecker();
        Assert.assertNotNull(convergenceChecker2);
        Assert.assertNotSame(convergenceChecker, convergenceChecker2);
    }

    @Test
    public void testConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceCheckerFactory<PAIR>() {
            @Override
            public ConvergenceChecker<PAIR> createChecker(double tolerance, int maxIterations) {
                return new SimpleValueChecker<PAIR>(tolerance, maxIterations);
            }
        }.createChecker(0.001, MaxEval.MAX_COUNT);
        optimizer.setConvergenceChecker(convergenceChecker);
        PointValuePair solution = optimizer.optimize(new PointValuePair(new double[]{1.0}, 1.0), GoalType.MINIMIZE, convergenceChecker);
        Assert.assertNotNull(solution);
        Assert.assertTrue(convergenceChecker.isConverged(solution));
    }

    @Test
    public void testConvergenceCheckerError() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunction(), 5);
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceCheckerFactory<PAIR>() {
            @Override
            public ConvergenceChecker<PAIR> createChecker(double tolerance, int maxIterations) {
                throw new RuntimeException("Convergence checker broke!");
            }
        }.createChecker(0.001, MaxEval.MAX_COUNT);
        optimizer.setConvergenceChecker(convergenceChecker);
        try {
            optimizer.optimize(new PointValuePair(new double[]{1.0}, 1.0), GoalType.MINIMIZE, convergenceChecker);
            Assert.fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedDomain() {
        BaseOptimizer<PAIR> optimizer = new DifferentialEvolution<PAIR>(new MyObjectiveFunctionWithUnboundedDomain(), 5);
        optimizer.optimize(new PointValuePair(new double[]{1.0}, 1.0), GoalType.MINIMIZE, new SimpleValueChecker(0.001, MaxEval.MAX_COUNT));
        Assert.fail();
    }

    private class MyObjectiveFunction implements org.apache.commons.math3.optim.NonlinearFunction<double[], Double> {
        @Override
        public Double value(double[] point) {
            return Math.pow(point[0], 2);
        }

        @Override
        public double[] gradient(double[] point) {
            return new double[]{2 * point[0]};
        }
    }

    private class MyObjectiveFunctionWithUnboundedDomain implements org.apache.commons.math3.optim.NonlinearFunction<double[], Double> {
        @Override
        public Double value(double[] point) {
            return Math.pow(point[0], 2);
        }

        @Override
        public double[] gradient(double[] point) {
            return new double[]{2 * point[0]};
        }

        @Override
        public double[] getDomain() {
            return new double[]{-Double.MAX_VALUE, Double.MAX_VALUE};
        }
    }

    private static class DifferentialEvolution<PAIR> extends AbstractDifferentialEvolution<PAIR> {
        public DifferentialEvolution(org.apache.commons.math3.optim.NonlinearFunction<double[], Double> function, int populationSize) {
            super(function, populationSize, RANDOM_1_2_RECOMBINATION, RANDOM_PENALTY_MUTATION, RANDOM_SCALE_RECOMBINATION,
                    RANDOM_STRATEGY, RND, TABU_MEMORY_SIZE, USE_DERIVATIVES, USE_TABU_LIST);
        }
    }
}