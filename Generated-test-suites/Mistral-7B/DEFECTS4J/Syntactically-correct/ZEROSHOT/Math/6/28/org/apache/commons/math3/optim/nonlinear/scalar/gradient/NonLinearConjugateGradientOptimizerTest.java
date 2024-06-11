package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.simple.*;
import org.apache.commons.math3.optim.initializer.RandomStart;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.conjugategradient.Preconditioner;

import java.util.Random;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula)}.
     */
    @ org.junit.Test
    public void testConstructor() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver)}.
     */
    @ org.junit.Test
    public void testConstructorWithCheckerAndSolver() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver()
        );
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#NonLinearConjugateGradientOptimizer(ConjugateGradientFormula, ConvergenceChecker, UnivariateSolver, Preconditioner)}.
     */
    @ org.junit.Test
    public void testConstructorWithCheckerAndSolverAndPreconditioner() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        assertNotNull(optimizer);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#setInitialStep(double)}.
     */
    @ org.junit.Test
    public void testSetInitialStep() {
        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.setInitialStep(2.5);
        assertEquals(2.5, optimizer.getInitialStep(), 0.0);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @ org.junit.Test
    public void testDoOptimize() throws OptimizationException {
        FunctionFunction f = new FunctionFunction("testFunction", 1) {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2 * x - 2};
            }
        };

        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver()
        );

        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair result = optimizer.optimize(f, new SimpleValueChecker(), new RandomStart(new Random(), 1));
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(-1.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link NonLinearConjugateGradientOptimizer#doOptimize()}.
     */
    @ org.junit.Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeWithIllegalInitialStep() {
        FunctionFunction f = new FunctionFunction("testFunction", 1) {
            @Override
            public double value(double x) {
                return x * x - 2 * x + 1;
            }

            @Override
            public double[] gradient(double x) {
                return new double[]{2 * x - 2};
            }
        };

        ConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
        optimizer.setInitialStep(-1.0);
        optimizer.optimize(f, new SimpleValueChecker(), new RandomStart(new Random(), 1));
    }
}

class FunctionFunction implements UnivariateFunction {
    private String name;
    private int dimension;

    public FunctionFunction(String name, int dimension) {
        this.name = name;
        this.dimension = dimension;
    }

    public double value(double x) {
        throw new UnsupportedOperationException("value not implemented");
    }

    public double[] gradient(double x) {
        throw new UnsupportedOperationException("gradient not implemented");
    }

    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }
}