import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        Assert.assertEquals(3, tableau.getOriginalNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertEquals(11, tableau.getWidth());
        Assert.assertEquals(3, tableau.getHeight());
        Assert.assertNotNull(tableau.tableau);
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "x2", "s0", "s1", "a0", "a1", "RHS");
        List<String> actualColumnLabels = tableau.columnLabels;

        Assert.assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        Assert.assertArrayEquals(new double[]{-1.0, 2.0, -3.0, 0.0, -3.0, 2.0, 1.0, 1.0, 0.0}, tableau.getData()[0]);
        Assert.assertEquals(1.0, tableau.getData()[1][1]);
        Assert.assertEquals(1.0, tableau.getData()[1][6]);
        Assert.assertEquals(-3.0, tableau.getData()[2][0]);
        Assert.assertEquals(-3.0, tableau.getData()[2][6]);
        Assert.assertEquals(2.0, tableau.getData()[2][2]);
        Assert.assertEquals(0.0, tableau.getData()[2][7]);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-2.0, 1.0}), Relationship.GEQ, 3.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);
        List<LinearConstraint> normalizedConstraints = new SimplexTableau(f, originalConstraints, GoalType.MINIMIZE, false, 1e-6, 10).constraints;

        Assert.assertEquals(2, normalizedConstraints.size());
        Assert.assertEquals(constraint1.getCoefficients().toString(), normalizedConstraints.get(0).getCoefficients().toString());
        Assert.assertEquals(Relationship.LEQ, normalizedConstraints.get(0).getRelationship());
        Assert.assertEquals(-2.0, normalizedConstraints.get(0).getValue(), 1e-6);

        Assert.assertEquals(constraint2.getCoefficients().toString(), normalizedConstraints.get(1).getCoefficients().toString());
        Assert.assertEquals(Relationship.GEQ, normalizedConstraints.get(1).getRelationship());
        Assert.assertEquals(3.0, normalizedConstraints.get(1).getValue(), 1e-6);
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        Assert.assertEquals(2, tableau.getBasicRow(0));
        Assert.assertEquals(3, tableau.getBasicRow(1));
        Assert.assertEquals(null, tableau.getBasicRow(2));
        Assert.assertEquals(null, tableau.getBasicRow(7));
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        tableau.dropPhase1Objective();

        Assert.assertEquals(10, tableau.getWidth());
        Assert.assertEquals(9, tableau.getHeight());
        Assert.assertArrayEquals(new double[]{1.0, -1.0, -2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[0]);
        Assert.assertEquals(0.0, tableau.getData()[1][0], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][1], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][2], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][3], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][4], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][5], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][6], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][7], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][8], 1e-6);
        Assert.assertEquals(0.0, tableau.getData()[1][9], 1e-6);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        tableau.dropPhase1Objective();

        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        LinearConstraint constraint1 = new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new ArrayRealVector(new double[]{-2.0, 1.0}), Relationship.GEQ, 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        tableau.dropPhase1Objective();

        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0, -1.0 / 3.0}, solution.getPoint(), 1e-6);
        Assert.assertEquals(-2.0 / 3.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        tableau.divideRow(0, 2.0);

        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][0], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][1], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][2], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][3], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][4], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][5], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][6], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][7], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][8], 1e-6);
        Assert.assertEquals(1.0 / 2.0, tableau.getData()[0][9], 1e-6);
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction f = new QuadraticObjectiveFunction(new double[]{-1.0, 2.0, -3.0}, new double[]{1.0, -1.0, -2.0}, 2.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{-3.0, 2.0}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{3.0, 1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        tableau.subtractRow(0, 1, 1.0);

        Assert.assertEquals(-1.0, tableau.getData()[0][0], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][1], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][2], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][3], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][4], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][5], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][6], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][7], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][8], 1e-6);
        Assert.assertEquals(-1.0, tableau.getData()[0][9], 1e-6);
    }
}