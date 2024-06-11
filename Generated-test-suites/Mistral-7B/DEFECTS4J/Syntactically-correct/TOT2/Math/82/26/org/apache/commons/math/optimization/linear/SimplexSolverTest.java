import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSetFiller;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionImpl;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintImpl;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintImpl;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionImpl;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSetFiller;
import org.apache.commons.math3.optim.PointValues;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;

import static org.apache.commons.math3.optim.linear.SimplexSolver.DefaultEpsilon;
import static org.apache.commons.math3.optim.linear.SimplexSolver.OptimizationDirection.MAXIMIZE;
import static org.apache.commons.math3.optim.linear.SimplexSolver.SimplexTableauType.PRIMAL_ONLY;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunctionImpl(new double[]{-1.0}, new double[]{-1.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller(constraintSet);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{0.0}, 0.0, true);
        filler.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{1.0}, 0.0, true);
        filler.add(constraint2);

        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, PRIMAL_ONLY, MAXIMIZE, filler, DefaultEpsilon);
            assertEquals(new PointValuePair(new double[]{-1.0}, -1.0), solution);
        } catch (NoFeasibleSolutionException e) {
            // expected
        } catch (UnboundedSolutionException e) {
            // expected
        } catch (MaxEvalExceededException e) {
            // unexpected
            fail("Maximum number of iterations not reached");
        }
    }

    @Test
    public void testSimpleConstraints() {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller(constraintSet);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{1.0, 1.0}, 0.0, true);
        filler.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{1.0, -1.0}, 2.0, true);
        filler.add(constraint2);

        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(objectiveFunction, PRIMAL_ONLY, MAXIMIZE, filler, DefaultEpsilon);
            assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 2.0 / 3.0), solution);
        } catch (NoFeasibleSolutionException e) {
            // unexpected
            fail("A feasible solution exists");
        } catch (UnboundedSolutionException e) {
            // unexpected
            fail("An unbounded solution exists");
        } catch (MaxEvalExceededException e) {
            // unexpected
            fail("Maximum number of iterations not reached");
        }
    }

    @Test
    public void testObjectiveFunctionGradient() {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller(constraintSet);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{1.0, 1.0}, 0.0, true);
        filler.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{1.0, -1.0}, 2.0, true);
        filler.add(constraint2);

        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction gradient = objectiveFunction.getValue();
        PointValuePair solution = solver.optimize(objectiveFunction, PRIMAL_ONLY, MAXIMIZE, filler, DefaultEpsilon);

        LinearObjectiveFunctionGradient objectiveFunctionGradient = (LinearObjectiveFunctionGradient) gradient;
        double[] expectedGradient = objectiveFunctionGradient.getValue(solution.getPoint());
        assertEquals(expectedGradient, new double[]{1.0 / 3.0, 2.0 / 3.0}, 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedObjectiveFunction() {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller(constraintSet);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{1.0, 1.0}, 0.0, true);
        filler.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{1.0, -1.0}, 2.0, true);
        filler.add(constraint2);

        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0}), PRIMAL_ONLY, MAXIMIZE, filler, DefaultEpsilon);
            fail("An unbounded solution should have been detected");
        } catch (UnboundedSolutionException e) {
            // expected
        } catch (NoFeasibleSolutionException e) {
            // unexpected
            fail("A feasible solution exists");
        } catch (MaxEvalExceededException e) {
            // unexpected
            fail("Maximum number of iterations not reached");
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunctionImpl(new double[]{-1.0, 2.0}, new double[]{0.0, 1.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearConstraintSetFiller filler = new LinearConstraintSetFiller(constraintSet);
        LinearConstraint constraint1 = new LinearConstraintImpl(new double[]{1.0, 1.0}, 0.0, true);
        filler.add(constraint1);
        LinearConstraint constraint2 = new LinearConstraintImpl(new double[]{1.0, -1.0}, Double.NEGATIVE_INFINITY, true);
        filler.add(constraint2);

        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objectiveFunction, PRIMAL_ONLY, MAXIMIZE, filler, DefaultEpsilon);
            fail("A feasible solution should not exist");
        } catch (NoFeasibleSolutionException e) {
            // expected
        } catch (UnboundedSolutionException e) {
            // unexpected
            fail("An unbounded solution exists");
        } catch (MaxEvalExceededException e) {
            // unexpected
            fail("Maximum number of iterations not reached");
        }
    }
}