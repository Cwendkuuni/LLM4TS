import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new Quad2D(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}, {-2.0, 3.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        assertEquals(8, tableau.getWidth());
        assertEquals(4, tableau.getHeight());

        assertArrayEquals(new double[]{-1.0, 0.0, 2.0, 3.0, 1.0, 0.0, 0.0, 0.0}, tableau.getData()[0], 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 2.0, -1.0, 0.0, 3.0, 1.0, -3.0}, tableau.getData()[1], 0.001);
        assertArrayEquals(new double[]{0.0, 3.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[2], 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[3], 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0}, tableau.getData()[4], 0.001);

        assertEquals(2, tableau.getOriginalNumDecisionVariables());
        assertEquals(1, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());

        assertEquals("x-", tableau.columnLabels.get(2));
        assertEquals("s0", tableau.columnLabels.get(5));
        assertEquals("s1", tableau.columnLabels.get(6));
        assertEquals("a0", tableau.columnLabels.get(7));
    }

    @Test
    public void testSimplexTableauMethods() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new Quad2D(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}, {-2.0, 3.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        tableau.dropPhase1Objective();

        assertEquals(3, tableau.getWidth() - tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getHeight() - 1);

        assertEquals(1.0 / 12.0, tableau.getEntry(1, 1), 0.001);
        assertEquals(5.0 / 12.0, tableau.getEntry(1, 2), 0.001);
        assertEquals(1.0 / 6.0, tableau.getEntry(1, 3), 0.001);
        assertEquals(-2.0 / 3.0, tableau.getEntry(2, 1), 0.001);
        assertEquals(1.0 / 2.0, tableau.getEntry(2, 2), 0.001);
        assertEquals(-1.0 / 6.0, tableau.getEntry(2, 3), 0.001);
        assertEquals(1.0 / 4.0, tableau.getEntry(3, 1), 0.001);
        assertEquals(1.0 / 4.0, tableau.getEntry(3, 2), 0.001);
        assertEquals(-1.0 / 2.0, tableau.getEntry(3, 3), 0.001);

        assertEquals(PointValuePair.make(new double[]{1.0 / 4.0, 1.0 / 4.0}, 11.0 / 12.0), tableau.getSolution());

        tableau.subtractRow(0, 1, 1.0);

        assertEquals(-1.0 / 6.0, tableau.getEntry(0, 2), 0.001);
        assertEquals(-1.0 / 6.0, tableau.getEntry(1, 2), 0.001);
        assertEquals(-1.0 / 3.0, tableau.getEntry(2, 2), 0.001);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new Quad2D(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {-3.0, 1.0}, {-2.0, 3.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        tableau.dropPhase1Objective();

        tableau.divideRow(0, 2.0);

        assertTrue(tableau.isOptimal());

        tableau.setEntry(0, 2, -1.0);

        assertFalse(tableau.isOptimal());
    }
}