import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(1, tableau.getOriginalNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());

        assertTrue(tableau.isOptimal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructorIllegalArgument() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, -1);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 2.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint1);
            add(constraint2);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(2, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);

        assertEquals(2, normalizedConstraints.size());
        assertEquals(1, normalizedConstraints.get(0).getCoefficients().getDimension());
        assertEquals(Relationship.LEQ, normalizedConstraints.get(0).getRelationship());
        assertEquals(-3.0, normalizedConstraints.get(0).getValue(), 1.0E-12);

        assertEquals(1, normalizedConstraints.get(1).getCoefficients().getDimension());
        assertEquals(Relationship.GEQ, normalizedConstraints.get(1).getRelationship());
        assertEquals(2.0, normalizedConstraints.get(1).getValue(), 1.0E-12);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 2.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint1);
            add(constraint2);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        RealMatrix tableauMatrix = tableau.createTableau(true);

        assertEquals(4, tableauMatrix.getRowDimension());
        assertEquals(5, tableauMatrix.getColumnDimension());
        assertEquals(-1.0, tableauMatrix.getEntry(0, 0), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(0, 1), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(0, 2), 1.0E-12);
        assertEquals(-1.0, tableauMatrix.getEntry(0, 3), 1.0E-12);
        assertEquals(-3.0, tableauMatrix.getEntry(0, 4), 1.0E-12);

        assertEquals(1.0, tableauMatrix.getEntry(1, 0), 1.0E-12);
        assertEquals(-1.0, tableauMatrix.getEntry(1, 1), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(1, 2), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(1, 3), 1.0E-12);
        assertEquals(-3.0, tableauMatrix.getEntry(1, 4), 1.0E-12);

        assertEquals(0.0, tableauMatrix.getEntry(2, 0), 1.0E-12);
        assertEquals(-1.0, tableauMatrix.getEntry(2, 1), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(2, 2), 1.0E-12);
        assertEquals(1.0, tableauMatrix.getEntry(2, 3), 1.0E-12);
        assertEquals(2.0, tableauMatrix.getEntry(2, 4), 1.0E-12);

        assertEquals(0.0, tableauMatrix.getEntry(3, 0), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(3, 1), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(3, 2), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(3, 3), 1.0E-12);
        assertEquals(0.0, tableauMatrix.getEntry(3, 4), 1.0E-12);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 2.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint1);
            add(constraint2);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        int col = 0;
        int row = tableau.getBasicRow(col);
        assertEquals(row, 0);

        col = 1;
        row = tableau.getBasicRow(col);
        assertEquals(row, 1);

        col = 2;
        row = tableau.getBasicRow(col);
        assertEquals(row, 2);

        col = 3;
        row = tableau.getBasicRow(col);
        assertNull(row);

        col = 4;
        row = tableau.getBasicRow(col);
        assertNull(row);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 2.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint1);
            add(constraint2);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        assertTrue(tableau.isOptimal());

        tableau.setEntry(2, 3, 1.0);
        assertFalse(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new ArrayRealVector(new double[]{1.0, 2.0}));
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 2.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>() {{
            add(constraint1);
            add(constraint2);
        }};

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12);

        PointValuePair solution = tableau.getSolution();

        assertEquals(2, solution.getPoint().length);
        assertEquals(0.0, solution.getPoint()[0], 1.0E-12);
        assertEquals(0.0, solution.getPoint()[1], 1.0E-12);
        assertEquals(3.0, solution.getValue(), 1.0E-12);
    }
}