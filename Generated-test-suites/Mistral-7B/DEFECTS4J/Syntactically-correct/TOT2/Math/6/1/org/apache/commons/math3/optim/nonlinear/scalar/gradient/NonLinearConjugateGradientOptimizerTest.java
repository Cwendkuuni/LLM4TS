package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.conjugate.NonlinearConjugateGradient;
import org.apache.commons.math3.optim.nonlinear.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.conjugate.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.LUDecompositionPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.CholeskyDecompositionPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.FactorizableMatrixPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.JacobiPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.SymmetricMatrixPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.SquareRootPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.DiagonalPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.NormalEquationsPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.QRDecompositionPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.SingularValueDecompositionPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.EigenvalueDecompositionPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.CholeskyFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.LUFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.QRDecompositionFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.SingularValueDecompositionFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.EigenvalueDecompositionFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.CholeskyFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.LUFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.QRDecompositionFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.SingularValueDecompositionFactorPreconditioner;
import org.apache.commons.math3.optim.nonlinear.linear.factorizable.EigenvalueDecompositionFactorPreconditioner;

public class NonLinearConjugateGradientOptimizerTest {

    @org.junit.Test
    public void testNonLinearConjugateGradientOptimizer() {
        // Test with default settings
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.pow(Math.sin(x), 2);
            }

            @Override
            public double derivative(double x) {
                return 2 * x + 2 * Math.cos(x);
            }
        };
        NonlinearOptimizer optimizer = new NonlinearConjugateGradient(
                new ConjugateGradientFormula.PolakRibiere(),
                new IdentityPreconditioner(),
                new BrentSolver(),
                new SimpleBounds(0.0, 10.0),
                GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), new MaxEval(100));
        assertEquals(1.5707963267948966, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        // Test with custom settings
        optimizer = new NonlinearConjugateGradient(
                new ConjugateGradientFormula.FletcherReeves(),
                new LUDecompositionPreconditioner(),
                null,
                new SimpleBounds(-10.0, 10.0),
                GoalType.MAXIMIZE);
        result = optimizer.optimize(function, new InitialGuess(0.0), new MaxEval(100));
        assertEquals(-1.5707963267948966, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        // Test with error scenarios
        optimizer = new NonlinearConjugateGradient(
                null,
                new DiagonalPreconditioner(new double[]{1.0, 2.0}),
                null,
                null,
                GoalType.MINIMIZE);
        try {
            optimizer.optimize(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }, new InitialGuess(0.5), new MaxEval(100));
            fail("IllegalStateException expected but not thrown.");
        } catch (MathIllegalStateException e) {
            // Expected
        }

        optimizer = new NonlinearConjugateGradient(
                new ConjugateGradientFormula.PolakRibiere(),
                new NormalEquationsPreconditioner(),
                null,
                new SimpleBounds(-10.0, 10.0),
                GoalType.MINIMIZE);
        try {
            optimizer.optimize(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    return x * x;
                }

                @Override
                public double derivative(double x) {
                    return 2 * x;
                }
            }, new InitialGuess(0.5), new MaxEval(100));
            fail("IllegalArgumentException expected but not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}