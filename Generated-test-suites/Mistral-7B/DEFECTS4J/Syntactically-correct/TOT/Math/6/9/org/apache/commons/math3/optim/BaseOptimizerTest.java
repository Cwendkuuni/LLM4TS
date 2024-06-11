package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializemethod.Averse11;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.initializemethod.AbstractInitializer;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleConstraints;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuessParams;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePairGenerator;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.initializemethod.Averse11;
import org.apache.commons.math3.optimization.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.point.PointValuePair;
import org.apache.commons.math3.optimization.point.PointValuePairList;
import org.apache.commons.math3.optimization.initializemethod.InitialGuess;
import org.apache.commons.math3.optimization.initializemethod.InitialGuessParams;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    private BaseOptimizer<PAIR> optimizer;
    private ObjectiveFunction testFunction;

    @Before
    public void setUp() {
        testFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11());
        int evaluations = optimizer.getEvaluations();
        assertEquals(10, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<PAIR> convergenceChecker = new DummyConvergenceChecker<>();
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimize() {
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11());
        assertTrue(pair != null);
        assertEquals(-3.1622776601683795D, pair.getPoint().doubleValue(), 1e-6);
        assertEquals(9.0, pair.getValue().doubleValue(), 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWhenMaxEvaluationsReached() {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11());
    }

    @Test
    public void testOptimizeWithGoalTypeMinimization() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11());
        assertTrue(pair != null);
        assertEquals(-3.1622776601683795D, pair.getPoint().doubleValue(), 1e-6);
        assertEquals(9.0, pair.getValue().doubleValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithGoalTypeMaximization() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());
        optimizer.setGoalType(GoalType.MAXIMIZE);

        PointValuePair pair = optimizer.optimize(testFunction.negate(), new SimpleBounds(-10, 10), new Averse11());
        assertTrue(pair != null);
        assertEquals(3.1622776601683795D, pair.getPoint().doubleValue(), 1e-6);
        assertEquals(-9.0, pair.getValue().doubleValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithConstraints() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        SimpleConstraints constraints = new SimpleConstraints(new Double[]{-1.0, 1.0}, new Double[]{0.0});
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), constraints);
        assertTrue(pair != null);
        assertTrue(pair.getPoint().isInside(new SimpleBounds(-1, 1)));
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        PointValuePair initialGuess = new PointValuePair(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuess);
        assertTrue(pair != null);
        assertEquals(0.5, pair.getPoint().doubleValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithInitialGuessAndConstraints() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        SimpleConstraints constraints = new SimpleConstraints(new Double[]{-1.0, 1.0}, new Double[]{0.0});
        PointValuePair initialGuess = new PointValuePair(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuess, constraints);
        assertTrue(pair != null);
        assertTrue(pair.getPoint().isInside(new SimpleBounds(-1, 1)));
    }

    @Test
    public void testOptimizeWithInitialGuessParams() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        InitialGuessParams initialGuessParams = new InitialGuessParams();
        initialGuessParams.setInitialPoint(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuessParams);
        assertTrue(pair != null);
        assertEquals(0.5, pair.getPoint().doubleValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithInitialGuessParamsAndConstraints() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        SimpleConstraints constraints = new SimpleConstraints(new Double[]{-1.0, 1.0}, new Double[]{0.0});
        InitialGuessParams initialGuessParams = new InitialGuessParams();
        initialGuessParams.setInitialPoint(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuessParams, constraints);
        assertTrue(pair != null);
        assertTrue(pair.getPoint().isInside(new SimpleBounds(-1, 1)));
    }

    @Test
    public void testOptimizeWithInitialGuessGenerator() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        PointValuePairGenerator<UnivariateFunction, InitialGuess> initialGuessGenerator = () -> new PointValuePair(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuessGenerator);
        assertTrue(pair != null);
        assertEquals(0.5, pair.getPoint().doubleValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithInitialGuessGeneratorAndConstraints() {
        optimizer = new DummyOptimizer<>();
        optimizer.setMaxEvaluations(10);
        optimizer.setConvergenceChecker(new DummyConvergenceChecker<>());

        SimpleConstraints constraints = new SimpleConstraints(new Double[]{-1.0, 1.0}, new Double[]{0.0});
        PointValuePairGenerator<UnivariateFunction, InitialGuess> initialGuessGenerator = () -> new PointValuePair(new InitialGuess(0.5));
        PointValuePair pair = optimizer.optimize(testFunction, new SimpleBounds(-10, 10), new Averse11(), initialGuessGenerator, constraints);
        assertTrue(pair != null);
        assertTrue(pair.getPoint().isInside(new SimpleBounds(-1, 1)));
    }

    private static class DummyOptimizer<PAIR> implements BaseOptimizer<PAIR> {
        private int maxEvaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;
        private GoalType goalType;

        @Override
        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }

        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        @Override
        public GoalType getGoalType() {
            return goalType;
        }

        public void setGoalType(GoalType goalType) {
            this.goalType = goalType;
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer, SimpleConstraints constraints) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer, InitialGuess initialGuess) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer, InitialGuess initialGuess, SimpleConstraints constraints) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer, InitialGuess initialGuess, PointValuePairGenerator<UnivariateFunction, InitialGuess> initialGuessGenerator) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, SimpleBounds bounds, AbstractInitializer<InitialGuess> initializer, InitialGuess initialGuess, PointValuePairGenerator<UnivariateFunction, InitialGuess> initialGuessGenerator, SimpleConstraints constraints) {
            return new PointValuePair(new InitialGuess(0.0));
        }

        @Override
        public PointValuePair optimize(UnivariateFunction objectiveFunction, MaxEval maxEval) {
            return new PointValuePair(new InitialGuess(0.0));
        }
    }

    private static class DummyConvergenceChecker<PAIR> implements ConvergenceChecker<PAIR> {
        @Override
        public boolean isConverged(PAIR currentValue, PAIR previousValue) {
            return false;
        }
    }
}