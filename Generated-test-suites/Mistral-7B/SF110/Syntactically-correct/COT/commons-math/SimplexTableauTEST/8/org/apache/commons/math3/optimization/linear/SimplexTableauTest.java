package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertNotNull(tableau.getTableau());
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        List<String> expectedColumnLabels = Arrays.asList("Z", "W", "x0", "x1", "x-", "s0", "a0", "RHS");
        assertEquals(expectedColumnLabels, tableau.getColumnLabels());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 0.0},
                                                                              {1.0, -1.0, -2.0, 1.0, -1.0, 0.0, -1.0, 0.0},
                                                                              {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}});
        assertEquals(expectedTableau, tableau.getTableau());
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, -1.0));
        LinearConstraint expectedNormalizedConstraint = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, -1.0}, {-2.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MAXIMIZE, false, 1e-12);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
        assertEquals(1, normalizedConstraints.size());
        assertEquals(expectedNormalizedConstraint, normalizedConstraints.get(0));
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testGetNumDecisionVariables() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertEquals(2, tableau.getOriginalNumDecisionVariables());
        assertEquals(3, tableau.getNumDecisionVariables());
    }

    @Test
    public void testGetNumSlackVariables() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertEquals(1, tableau.getNumSlackVariables());
    }

    @Test
    public void testGetNumArtificialVariables() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        assertEquals(1, tableau.getNumArtificialVariables());
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.setEntry(1, 1, 1.0);
        assertFalse(tableau.isOptimal());
        tableau.setEntry(1, 1, 0.0);
        assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(2, coefficients.length);
        assertEquals(4.0, tableau.f.getValue(coefficients), 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDropPhase1ObjectiveWithOneObjective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();
        fail("IllegalArgumentException should be thrown");
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();
        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testEquals() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 1.0);
        List<LinearConstraint> constraints1 = new ArrayList<>();
        List<LinearConstraint> constraints2 = new ArrayList<>();
        constraints1.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        constraints2.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau1 = new SimplexTableau(f1, constraints1, GoalType.MAXIMIZE, false, 1e-12);
        SimplexTableau tableau2 = new SimplexTableau(f2, constraints2, GoalType.MAXIMIZE, false, 1e-12);
        SimplexTableau tableau3 = new SimplexTableau(f1, constraints1, GoalType.MAXIMIZE, false, 1e-12);
        assertTrue(tableau1.equals(tableau1));
        assertTrue(tableau1.equals(tableau3));
        assertTrue(tableau1.equals(tableau2));
        assertFalse(tableau1.equals("not a tableau"));
    }

}