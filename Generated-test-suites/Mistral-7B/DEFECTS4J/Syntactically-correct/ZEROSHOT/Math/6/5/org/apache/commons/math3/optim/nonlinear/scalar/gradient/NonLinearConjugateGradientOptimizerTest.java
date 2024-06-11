package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testOptimize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * (Math.sin(x) * Math.cos(x) - Math.cos(x) * Math.sin(x));
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());

        double[] startPoint = new double[]{3.0};
        optimizer.setInitialStep(1.0);
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MINIMIZE);

        Assert.assertEquals(Math.PI / 4, result.getPoint()[0], 1e-6);
        Assert.assertEquals(0.25, result.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithIllegalUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.ILLEGAL_UPDATE_FORMULA,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithNullLineSearchSolver() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                null);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeWithNullPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver(),
                null);
    }

    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());
        optimizer.setInitialStep(-1.0);
        Assert.assertEquals(1.0, optimizer.getInitialStep(), 1e-6);
        optimizer.setInitialStep(2.0);
        Assert.assertEquals(2.0, optimizer.getInitialStep(), 1e-6);
    }

    @Test
    public void testDoOptimize() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * (Math.sin(x) * Math.cos(x) - Math.cos(x) * Math.sin(x));
            }
        };

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());

        double[] startPoint = new double[]{3.0};
        optimizer.setInitialStep(1.0);

        // check initial state
        Assert.assertArrayEquals(startPoint, optimizer.getPoint(), 1e-6);
        Assert.assertEquals(Double.NaN, optimizer.getValue(), 1e-6);

        optimizer.optimize(function, startPoint, GoalType.MINIMIZE);

        // check final state
        Assert.assertArrayEquals(new double[]{Math.PI / 4}, optimizer.getPoint(), 1e-6);
        Assert.assertEquals(0.25, optimizer.getValue(), 1e-6);
    }

    @Test
    public void testPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(1e-6),
                new BrentSolver());

        double[] startPoint = new double[]{1.0, 1.0};
        optimizer.setInitialStep(1.0);

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(x[0]) + Math.sin(x[1]), 2);
            }

            @Override
            public double derivative(double[] x) {
                double[] gradient = new double[2];
                for (int i = 0; i < 2; ++i) {
                    gradient[i] = 2 * Math.sin(x[i]) * (Math.sin(x[0]) + Math.sin(x[1]));
                }
                return Arrays.equals(gradient, computeObjectiveGradient(x)) ? 0 : Double.NaN;
            }
        };

        Preconditioner preconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                for (int i = 0; i < variables.length; ++i) {
                    r[i] /= 2;
                }
                return r;
            }
        };

        optimizer.setPreconditioner(preconditioner);
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MINIMIZE);

        Assert.assertArrayEquals(new double[]{Math.PI / 4, Math.PI / 4}, result.getPoint(), 1e-6);
        Assert.assertEquals(0.25, result.getValue(), 1e-6);
    }
}

class Preconditioner implements NonLinearConjugateGradientOptimizer.Preconditioner {

    @Override
    public double[] precondition(double[] variables, double[] r) {
        for (int i = 0; i < variables.length; ++i) {
            r[i] /= 2;
        }
        return r;
    }
}