import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizer;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        LinearOptimizer opt = new SimplexSolver();
        assertEquals(1.0e-6, opt.getEpsilon(), 0.0);
    }

    @Test
    public void testCustomEpsilonConstructor() {
        LinearOptimizer opt = new SimplexSolver(0.001);
        assertEquals(0.001, opt.getEpsilon(), 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 2, 3}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);
        Integer pivotColumn = ((SimplexSolver) tableau.getSolver()).getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 2, 3}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);
        Integer pivotRow = ((SimplexSolver) tableau.getSolver()).getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 2, 3}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);

        try {
            ((SimplexSolver) tableau.getSolver()).doIteration(tableau);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        tableau.getEntry(2, 0) = 1.0;
        tableau.getEntry(2, 2) = -1.0;

        ((SimplexSolver) tableau.getSolver()).doIteration(tableau);
        assertEquals(1.0 / 2.0, tableau.getEntry(2, 1), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(2, 0), 1.0e-6);
        assertEquals(-1.0 / 2.0, tableau.getEntry(1, 1), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(1, 0), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(0, 0), 1.0e-6);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 2, 3}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);

        tableau.getEntry(2, 2) = -1.0;
        assertEquals(false, ((SimplexSolver) tableau.getSolver()).isPhase1Solved(tableau));

        tableau.getEntry(2, 2) = 0.0;
        assertEquals(true, ((SimplexSolver) tableau.getSolver()).isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 2, 3}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);

        tableau.getEntry(2, 2) = -1.0;
        assertEquals(false, ((SimplexSolver) tableau.getSolver()).isOptimal(tableau));

        tableau.getEntry(2, 2) = 0.0;
        tableau.setEntry(1, 1, -1.0e6);

        assertEquals(false, ((SimplexSolver) tableau.getSolver()).isOptimal(tableau));

        tableau.setEntry(1, 1, 0.0);
        assertEquals(true, ((SimplexSolver) tableau.getSolver()).isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{0.0, 2.0, 3.0}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);

        try {
            ((SimplexSolver) tableau.getSolver()).solvePhase1(tableau);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        tableau.setEntry(2, 0, -1.0);
        tableau.setEntry(2, 2, 1.0);

        try {
            ((SimplexSolver) tableau.getSolver()).solvePhase1(tableau);
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        tableau.setEntry(2, 2, 0.0);

        ((SimplexSolver) tableau.getSolver()).solvePhase1(tableau);
        assertEquals(0.0, tableau.getEntry(2, 2), 1.0e-6);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 1), 1.0e-6);
        assertEquals(-1.0 / 3.0, tableau.getEntry(1, 2), 1.0e-6);
        assertEquals(1.0 / 3.0, tableau.getEntry(0, 1), 1.0e-6);
        assertEquals(1.0 / 3.0, tableau.getEntry(0, 2), 1.0e-6);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{0.0, 2.0, 3.0}, null),
            new LinearConstraint[]{},
            null,
            1.0e-6);

        try {
            ((SimplexSolver) tableau.getSolver()).doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        tableau.setEntry(2, 2, 1.0);
        tableau.setEntry(2, 0, -1.0);

        try {
            ((SimplexSolver) tableau.getSolver()).doOptimize();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            // expected
        }

        tableau.setEntry(2, 2, 0.0);

        ((SimplexSolver) tableau.getSolver()).solvePhase1(tableau);
        tableau.discardArtificialVariables();

        ((SimplexSolver) tableau.getSolver()).doIteration(tableau);

        RealPointValuePair solution = ((SimplexSolver) tableau.getSolver()).doOptimize();
        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, -1.0 / 3.0);
        assertEquals(expectedSolution, solution);
    }
}