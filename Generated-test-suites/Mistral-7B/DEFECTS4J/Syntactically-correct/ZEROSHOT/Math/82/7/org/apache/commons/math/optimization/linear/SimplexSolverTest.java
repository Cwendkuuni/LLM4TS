import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint[] constraints;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1, -2, 3}, new double[]{0, 0, 1});
        constraints = new LinearConstraint[3];
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1, 1, 1}, Relationship.LEQ, 1);
        constraints[0] = constraint1;
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1, -1, 1}, Relationship.LEQ, 1);
        constraints[1] = constraint2;
        LinearConstraint constraint3 = new LinearConstraint(new double[]{1, 1, -1}, Relationship.LEQ, 1);
        constraints[2] = constraint3;
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver simplexSolver = new SimplexSolver();
        assertEquals(1.0e-6, simplexSolver.epsilon, 0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver simplexSolver = new SimplexSolver(0.001);
        assertEquals(0.001, simplexSolver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        Integer expected = 0;
        Integer actual = simplexSolver.getPivotColumn(tableau);
        assertEquals(expected, actual);

        tableau.setValue(1, 1, -2);
        expected = 1;
        actual = simplexSolver.getPivotColumn(tableau);
        assertEquals(expected, actual);

        tableau.setValue(1, 1, 2);
        expected = null;
        actual = simplexSolver.getPivotColumn(tableau);
        assertNull(actual);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        Integer expectedRow1 = 1;
        Integer expectedRow2 = 2;
        Integer expectedRow3 = null;

        Integer actualRow1 = simplexSolver.getPivotRow(0, tableau);
        assertEquals(expectedRow1, actualRow1);

        tableau.setValue(2, 0, -1);
        Integer actualRow2 = simplexSolver.getPivotRow(0, tableau);
        assertEquals(expectedRow2, actualRow2);

        tableau.setValue(3, 0, 1);
        Integer actualRow3 = simplexSolver.getPivotRow(0, tableau);
        assertNull(actualRow3);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        simplexSolver.doIteration(tableau);

        double expectedEntry11 = 1.0 / (2.0 * -1.0);
        double expectedEntry12 = -1.0 / (2.0 * -1.0);
        double expectedEntry13 = (1.0 - expectedEntry11) / 1.0;

        double[][] expectedTableau = {
                {expectedEntry11, expectedEntry12, expectedEntry13, 0.0, 0.0, 0.0},
                {-1.0 / 2.0, -1.0 / 2.0, 1.0 / 2.0, 0.0, 0.0, 0.0},
                {0.5, -0.5, 1.5, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 1.0}
        };

        double[][] actualTableau = tableau.getTableau();
        for (int i = 0; i < actualTableau.length; i++) {
            for (int j = 0; j < actualTableau[i].length; j++) {
                assertEquals(expectedTableau[i][j], actualTableau[i][j], 0);
            }
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        assertFalse(simplexSolver.isPhase1Solved(tableau));

        tableau.setValue(1, 1, 0);
        tableau.setValue(2, 1, 0);
        tableau.setValue(2, 2, 0);
        assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1Unbounded() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2, 3}, new double[]{0, 0, 1}), constraints, null, null, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        simplexSolver.discardArtificialVariables(tableau);
        simplexSolver.doIteration(tableau);
        simplexSolver.solvePhase1(tableau);
        simplexSolver.discardArtificialVariables(tableau);

        tableau.setValue(0, 0, 1);
        tableau.setValue(0, 1, -1);
        tableau.setValue(0, 2, 1);
        simplexSolver.doOptimize();
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        simplexSolver.discardArtificialVariables(tableau);

        tableau.setValue(1, 1, 1);
        tableau.setValue(1, 2, -1);
        simplexSolver.doIteration(tableau);

        double[] expectedSolution = new double[]{1.0 / 3.0, 2.0 / 3.0};
        PointValuePair actualSolution = simplexSolver.doOptimize();
        RealPointValuePair expectedResult = new RealPointValuePair(expectedSolution, 1.0 / 3.0);
        RealPointValuePair actualResult = (RealPointValuePair) actualSolution;

        assertEquals(expectedResult.getPoint(), actualResult.getPoint(), 0);
        assertEquals(expectedResult.getValue(), actualResult.getValue(), 0);
    }
}