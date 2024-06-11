import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{3, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, Relation.LEQUAL, 1),
                new LinearConstraint(new double[]{1, 1}, Relation.LEQUAL, 2)};
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{0.0, 1.0}, new double[]{0.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{0.0, 1.0}, new double[]{0.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.doOptimize();
    }

    @Test
    public void testDoIteration() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{3, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, Relation.LEQUAL, 1),
                new LinearConstraint(new double[]{1, 1}, Relation.LEQUAL, 2)};
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, 1e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        Assert.assertEquals(1, tableau.getNumBasicVariables());
        Assert.assertEquals(1, tableau.getBasicRow(0).intValue());
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{3, 2}),
                new LinearConstraint[0], null, null, 1e-6, 10);
        Integer result = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(0, result.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{3, 2}),
                new LinearConstraint[0], null, null, 1e-6, 10);
        Integer result = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(1, result.intValue());
    }

    @Test
    public void testSolvePhase1() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{3, 2});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, Relation.LEQUAL, 1),
                new LinearConstraint(new double[]{1, 1}, Relation.LEQUAL, 2)};
        SimplexTableau tableau = new SimplexTableau(obj, constraints, null, null, 1e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        Assert.assertEquals(1, tableau.getNumBasicVariables());
        Assert.assertEquals(1, tableau.getBasicRow(0).intValue());
        Assert.assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), 1e-6);
    }

    @Test
    public void testSetters() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1e-6, solver.getEpsilon(), 1e-6);
        Assert.assertEquals(10, solver.getMaxUlps(), 1);
        solver.setEpsilon(0.001);
        solver.setMaxUlps(20);
        Assert.assertEquals(0.001, solver.getEpsilon(), 1e-6);
        Assert.assertEquals(20, solver.getMaxUlps(), 1);
    }
}