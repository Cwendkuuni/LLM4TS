package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumObjectiveFunctions());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(3, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {1.0, 2.0}}), Relationship.GEQ, 1.0);
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);
        SimplexTableau tableau = new SimplexTableau(function, originalConstraints, GoalType.MAXIMIZE, false, 1.0E-6);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
        assertEquals(2, normalizedConstraints.size());
        LinearConstraint normalizedConstraint1 = normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = normalizedConstraints.get(1);
        assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, -1.0}}), Relationship.GEQ, 0.0), normalizedConstraint1);
        assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {1.0, 2.0}}), Relationship.LEQ, 1.0), normalizedConstraint2);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0, 1.0}, {-1.0, 1.0, -1.0}, {0.0, 0.0, 1.0}}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, -1.0}, {1.0, -1.0, 1.0}, {-1.0, 1.0, 1.0}}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1.0E-6);
        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{0.5, 1.5, 1.0}, solution.getPoint(), 1.0E-6);
        assertEquals(7.5, solution.getValue(), 1.0E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, -1.0E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstraints() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}}), 1.0);
        SimplexTableau tableau = new SimplexTableau(function, null, GoalType.MAXIMIZE, false, 1.0E-6);
    }
}