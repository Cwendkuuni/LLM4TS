import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.conjugate.ConjugateGradientFormula;
import org.apache.commons.math3.optim.nonlinear.scalar.nonlinearfunction.AbstractNonlinearScalarFunction;
import org.apache.commons.math3.special.Constant;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        final UnivariateFunction objectiveFunction = new AbstractNonlinearScalarFunction("f", x -> Math.pow(x, 2) - 4 * x + 4) {
            @Override
            public double value(double x) {
                return super.value(x);
            }
        };

        final ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        final GoalType goalType = GoalType.MINIMIZE;
        final UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(objectiveFunction, new InitialGuess(0.0));
        final PointValuePair solution = optimizer.optimize(problem);
        final double expectedSolution = Constant.PI.doubleValue();

        assertEquals(goalType, problem.getGoalType());
        assertEquals(expectedSolution, solution.getPoint()[0], 0.001);
        assertEquals(4.0, solution.getValue(), 0.001);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerWithPreconditioner() {
        final UnivariateFunction objectiveFunction = new AbstractNonlinearScalarFunction("f", x -> Math.pow(x, 2) - 4 * x + 4) {
            @Override
            public double value(double x) {
                return super.value(x);
            }
        };

        final ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new Preconditioner() {
                    @Override
                    public double[] precondition(double[] variables, double[] r) {
                        final double[] preconditioned = new double[variables.length];
                        for (int i = 0; i < variables.length; i++) {
                            preconditioned[i] = r[i] / (2 * variables[i] + 1);
                        }
                        return preconditioned;
                    }
                });

        final GoalType goalType = GoalType.MINIMIZE;
        final UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(objectiveFunction, new InitialGuess(1.0));
        final PointValuePair solution = optimizer.optimize(problem);
        final double expectedSolution = (Math.sqrt(2) - 1) / 2;

        assertEquals(goalType, problem.getGoalType());
        assertEquals(expectedSolution, solution.getPoint()[0], 0.001);
        assertEquals(4.0, solution.getValue(), 0.001);
    }

    @Test
    public void testNonLinearConjugateGradientOptimizerWithConjugateGradientFormulaPolakRibiere() {
        final UnivariateFunction objectiveFunction = x -> Math.pow(x, 2);

        final ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner());

        final GoalType goalType = GoalType.MINIMIZE;
        final UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(objectiveFunction, new InitialGuess(1.0));
        final PointValuePair solution = optimizer.optimize(problem);
        final double expectedSolution = 0.0;

        assertEquals(goalType, problem.getGoalType());
        assertEquals(expectedSolution, solution.getPoint()[0], 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);

        final ConjugateGradientOptimizer optimizerWithInitialStep = new ConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner(), 0.1);
        final PointValuePair solutionWithInitialStep = optimizerWithInitialStep.optimize(problem);
        assertEquals(expectedSolution, solutionWithInitialStep.getPoint()[0], 0.001);
        assertEquals(0.0, solutionWithInitialStep.getValue(), 0.001);
    }

    @Test
    public void testConvergenceChecker() {
        final UnivariateFunction objectiveFunction = x -> Math.pow(x, 2);

        final ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                if (iteration > 10) {
                    return true;
                }
                return current.getValue() < previous.getValue();
            }
        };

        final ConjugateGradientOptimizer optimizer = new ConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                convergenceChecker,
                new BrentSolver(),
                new IdentityPreconditioner());

        final GoalType goalType = GoalType.MINIMIZE;
        final UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem(objectiveFunction, new InitialGuess(1.0));
        final PointValuePair solution = optimizer.optimize(problem);

        assertFalse(convergenceChecker.converged(optimizer.getIterations(), solution, null));

        optimizer.setMaxEvaluations(11);
        final PointValuePair solutionWithConvergence = optimizer.optimize(problem);
        assertTrue(convergenceChecker.converged(optimizer.getIterations(), solutionWithConvergence, null));
    }
}