package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.initializer.AmoebaInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Amoeba;
import org.apache.commons.math3.optim.stopping.StoppingCriterion;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.convergence.SimpleConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.initializer.GaussianRandomInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.stopping.IterationLimitStoppingCriterion;

public class BaseOptimizerTest {

    /**
     * Test getMaxEvaluations method
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(100, maxEvaluations);
    }

    /**
     * Test getEvaluations method
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(100);
        int initialEvaluations = optimizer.getEvaluations();
        assertEquals(0, initialEvaluations);
        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        }, new SimpleBounds(0.0, 10.0), new SimpleConvergenceChecker(1.0e-6, 100), new IterationLimitStoppingCriterion(100));
        int evaluations = optimizer.getEvaluations();
        assertEquals(100, evaluations);
    }

    /**
     * Test getConvergenceChecker method
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(100);
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();
        assertNull(convergenceChecker);
        optimizer = createAmoebaOptimizer(100);
        convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(SimpleConvergenceChecker.class, convergenceChecker.getClass());
    }

    /**
     * Creates an Amoeba optimizer with a given number of maxEvaluations
     *
     * @param maxEvaluations the maximal number of function evaluations
     * @return the created optimizer
     */
    private BaseOptimizer<PointValuePair> createAmoebaOptimizer(int maxEvaluations) {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };
        UnivariateOptimizer optimizer = new Amoeba(objectiveFunction, new AmoebaInitializer(), new SimpleBounds(0.0, 10.0), new IterationLimitStoppingCriterion(maxEvaluations), GoalType.MINIMIZE);
        return (BaseOptimizer<PointValuePair>) optimizer;
    }

    /**
     * Test the case when the convergence checker is set and the optimization
     * converges
     */
    @Test
    public void testConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(100);
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleConvergenceChecker(1.0e-6, 100);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double value(double[] x) {
                return x[0] * x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        }, new SimpleBounds(0.0, 10.0), convergenceChecker, new IterationLimitStoppingCriterion(100));
        double solution = optimizer.getValue();
        assertEquals(0.0, solution, 1.0e-6);
        int evaluations = optimizer.getEvaluations();
        assertEquals(100, evaluations);
    }

    /**
     * Test the case when the convergence checker is set but the optimization
     * does not converge
     */
    @Test(expected = org.apache.commons.math3.optim.MaxEvalExceededException.class)
    public void testConvergenceCheckerNoConvergence() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(10);
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleConvergenceChecker(1.0e-6, 100);
        optimizer.setConvergenceChecker(convergenceChecker);
        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) / x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        }, new SimpleBounds(-1.0, 1.0), convergenceChecker, new IterationLimitStoppingCriterion(10));
    }

    /**
     * Test the case when the convergence checker is not set
     */
    @Test
    public void testNoConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = createAmoebaOptimizer(100);
        optimizer.optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI * x) / x;
            }

            @Override
            public double value(double[] x) {
                return Math.sin(Math.PI * x[0]) / x[0];
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        }, new SimpleBounds(-1.0, 1.0), new IterationLimitStoppingCriterion(100), GoalType.MINIMIZE);
        double solution = optimizer.getValue();
        assertNull(solution);
        int evaluations = optimizer.getEvaluations();
        assertEquals(100, evaluations);
    }
}