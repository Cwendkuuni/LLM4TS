import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.nonlinear.GoalType;
import org.apache.commons.math3.optimization.nonlinear.constraint.LinearConstraintSet;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearConstraintCollection;
import org.apache.commons.math3.optimization.nonlinear.linear.LinearObjectiveFunctionCollection;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.getEpsilon(), 0);
        Assert.assertEquals(10, solver.getMaxUlps());

        SimplexSolver solverWithArgs = new SimplexSolver(0.001, 5);
        Assert.assertEquals(0.001, solverWithArgs.getEpsilon(), 0);
        Assert.assertEquals(5, solverWithArgs.getMaxUlps());
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        Integer result = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertNotNull(result);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            tableau.setEntry(0, i, 10000);
        }
        SimplexSolver.getInstance().getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        Integer result = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPivotRowWithNullTableau() {
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.getPivotRow(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotRowWithInvalidColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.getPivotRow(tableau, tableau.getWidth());
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.doIteration(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationWithMaxIterationsReached() throws MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.setMaxIterations(1);
        for (int i = 0; i < 10; i++) {
            solver.doIteration(tableau);
        }
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunctionCollection(),
                new LinearConstraintSet(), GoalType.MINIMIZE, false, 1e-6, 10);
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            tableau.setEntry(0, i, 10000);
        }
        SimplexSolver solver = SimplexSolver.getInstance();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{4, 3});
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2, Relationship.LEQ);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, 2}, 5, Relationship.LEQ);
        LinearConstraintCollection cc = new LinearConstraintCollection();
        cc.add(c1);
        cc.add(c2);
        LinearConstraintSet cs = new LinearConstraintSet(cc);
        LinearObjectiveFunctionCollection fo = new LinearObjectiveFunctionCollection();
        fo.add(f);
        SimplexTableau tableau = new SimplexTableau(fo, cs, GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new PointValuePair(new double[]{0.5, 1.0}, 4.0), solution, 0.0001);
    }

    @Test
    public void testDoOptimizeWithFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{4, 3});
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2, Relationship.LEQ);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, 2}, 5, Relationship.LEQ);
        LinearConstraintCollection cc = new LinearConstraintCollection();
        cc.add(c1);
        cc.add(c2);
        LinearConstraintSet cs = new LinearConstraintSet(cc);
        LinearObjectiveFunctionCollection fo = new LinearObjectiveFunctionCollection();
        fo.add(f);
        SimplexTableau tableau = new SimplexTableau(fo, cs, GoalType.MINIMIZE, false, 1e-6, 10);
        SimplexSolver solver = SimplexSolver.getInstance();
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(new PointValuePair(new double[]{0.5, 1.0}, 4.0), solution, 0.0001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, new double[]{4, 3});
        LinearConstraint c1 = new LinearConstraint(new double[]{1, 1}, 2, Relationship.LEQ);
        LinearConstraint c2 = new LinearConstraint(new double[]{1, 2}, 5, Relationship.LEQ);
        LinearConstraintCollection cc = new LinearConstraintCollection();
        cc.add(c1);
        cc.add(c2);
        LinearConstraintSet cs = new LinearConstraintSet(cc);
        LinearObjectiveFunctionCollection fo = new LinearObjectiveFunctionCollection();
        fo.add(f);
        SimplexTableau tableau = new SimplexTableau(fo, cs, GoalType.MINIMIZE, false, 1e-6, 10);
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            tableau.setEntry(0, i, 10000);
        }
        SimplexSolver solver = SimplexSolver.getInstance();
        PointValuePair solution = solver.doOptimize();
    }

}