package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NoderivFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NonlinearConstraintChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    private UnivariateFunction testFunction;
    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new NoderivFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double[] gradient(double x) {
                double[] result = new double[2];
                result[0] = -2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x) * Math.sin(x);
                result[1] = null;
                return result;
            }
        };
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8),
                new BrentMinimizer(new SimpleValueChecker(1e-8)),
                null);
    }

    @Test
    public void testMinimizeSimpleFunction() {
        PointValuePair result = optimizer.optimize(testFunction, new PointValuePair(0, testFunction.value(0)));
        assertEquals("Function value", 0.0, result.getValue(), 1e-8);
        assertArrayEquals("Solution coordinates", new double[]{Math.PI / 2, Math.PI / 2}, result.getPoint(), 1e-8);
    }

    @Test
    public void testMinimizeFunctionWithConstraint() {
        UnivariateFunction constraint = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };

        NonlinearConstraint nonlinearConstraint = new NonlinearConstraint(constraint, 0.0);
        LinearConstraint linearConstraint = new LinearConstraint(new double[]{1.0}, new double[]{0.0}, null);
        LinearObjectiveFunction linearObjectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{1.0});

        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8),
                new BrentMinimizer(new SimpleValueChecker(1e-8)),
                null);
        optimizer.addConstraint(nonlinearConstraint);
        optimizer.setLinearConstraints(new LinearConstraint[]{linearConstraint});
        optimizer.setLinearObjectiveFunction(linearObjectiveFunction);

        PointValuePair result = optimizer.optimize(testFunction, new PointValuePair(0, testFunction.value(0)));
        assertEquals("Function value", 0.0, result.getValue(), 1e-8);
        assertArrayEquals("Solution coordinates", new double[]{Math.PI / 2}, result.getPoint(), 1e-8);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-8),
                new MaxEval(1),
                null);
        optimizer.optimize(testFunction, new PointValuePair(0, testFunction.value(0)));
    }
}