import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearSimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Test class for {@link NonLinearConjugateGradientOptimizer}.
 */
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    private UnivariateObjectiveFunction objectiveFunction;

    @Before
    public void setUp() {
        objectiveFunction = new UnivariateObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double[] gradient(double x) {
                return new double[] {2 * x};
            }
        };
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.POLAK_RIBIERE,
                new NonLinearSimpleValueChecker(1e-6, 1e-6),
                new BrentSolver());
        optimizer.setInitialStep(0.1);
    }

    @After
    public void tearDown() {
        optimizer = null;
        objectiveFunction = null;
    }

    @Test
    public void testOptimizeOneVariable() {
        double[] startPoint = {1.0};
        NonLinearPointValuePair result = optimizer.optimize(objectiveFunction, new NonLinearConstraintSet(), startPoint);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testNonConvergence() {
        double[] startPoint = {1.0};
        optimizer.setMaxIterations(1);
        optimizer.optimize(objectiveFunction, new NonLinearConstraintSet(), startPoint);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDivergence() {
        double[] startPoint = {1.0};
        optimizer.setMaxIterations(1);
        optimizer.setInitialStep(Double.POSITIVE_INFINITY);
        optimizer.optimize(objectiveFunction, new NonLinearConstraintSet(), startPoint);
    }

    @Test
    public void testOptimizeTwoVariables() {
        double[] startPoint = {1.0, 1.0};
        NonLinearPointValuePair result = optimizer.optimize(new TwoVariablesObjectiveFunction(), new NonLinearConstraintSet(), startPoint);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testPreconditioner() {
        double[] startPoint = {1.0, 1.0};
        NonLinearPointValuePair result = optimizer.optimize(new PreconditionerObjectiveFunction(), new NonLinearConstraintSet(), startPoint);
        assertEquals(0.0, result.getPoint()[0], 1e-6);
        assertEquals(0.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);

        double[] preconditionedPoint = new double[2];
        Arrays.fill(preconditionedPoint, 1.0);
        double[] preconditionedGradient = new double[2];
        Arrays.fill(preconditionedGradient, 1.0);

        double[] r = optimizer.precondition(preconditionedPoint, preconditionedGradient);
        Arrays.assertArrayEquals(new double[]{0.0, 0.0}, r);
    }

    interface UnivariateObjectiveFunction extends NonLinearObjectiveFunction {
    }

    static class TwoVariablesObjectiveFunction implements UnivariateObjectiveFunction {
        @Override
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }

        @Override
        public double[] gradient(double[] x) {
            return new double[]{2 * x[0], 2 * x[1]};
        }
    }

    static class PreconditionerObjectiveFunction implements UnivariateObjectiveFunction {
        @Override
        public double value(double x) {
            return x * x;
        }

        @Override
        public double[] gradient(double x) {
            return new double[]{2 * x};
        }

        @Override
        public Preconditioner getPreconditioner() {
            return new Preconditioner() {
                @Override
                public double[] precondition(double[] variables, double[] r) {
                    Arrays.fill(variables, 1.0);
                    double[] result = new double[r.length];
                    System.arraycopy(r, 0, result, 0, r.length);
                    Arrays.fill(variables, 2.0);
                    for (int i = 0; i < r.length; ++i) {
                        result[i] *= 2.0;
                    }
                    return result;
                }
            };
        }
    }
}

class AbstractScalarDifferentiableOptimizer extends NonLinearSolver {
    // Empty class.
}

enum ConjugateGradientFormula {
    FLETCHER_REEVES, POLAK_RIBIERE
}

interface Preconditioner {
    double[] precondition(double[] variables, double[] r);
}