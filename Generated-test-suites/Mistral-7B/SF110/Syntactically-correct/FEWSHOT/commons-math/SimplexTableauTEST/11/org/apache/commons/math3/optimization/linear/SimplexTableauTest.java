package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.linear.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testInitializationWithOneObjectiveFunction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {-3.0, -2.0, -1.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getHeight());
        assertEquals(4, tableau.getWidth());
    }

    @Test
    public void testInitializationWithTwoObjectiveFunctions() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 0.0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(new LinearFunctionCollection(new LinearObjectiveFunction[] {f1, f2}), constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        assertEquals(6, tableau.getNumDecisionVariables());
        assertEquals(1, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(3, tableau.getHeight());
        assertEquals(7, tableau.getWidth());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {-3.0, -2.0, -1.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0},
                {1.0, -1.0, -1.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0, 0.0, 0.0, 0.0, -1.0, -1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0}});
        RealMatrix actualTableau = tableau.getTableau();
        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {-3.0, -2.0, -1.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {4.0, 5.0}}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals(2, normalizedConstraints.size());
        assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.GEQ, 0.0), normalizedConstraints.get(0));
        assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0), normalizedConstraints.get(1));
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {4.0, 5.0, 6.0, 7.0}, {-3.0, -2.0, -1.0, 0.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {4.0, 5.0}}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        tableau.dropPhase1Objective();
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, -1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0}});
        RealMatrix actualTableau = tableau.getTableau();
        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {-3.0, -2.0, -1.0}}), 0.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-12, 10);
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(4, coefficients.length);
        assertEquals(1.5, coefficients[0], 1E-12);
        assertEquals(2.0, coefficients[1], 1E-12);
        assertEquals(-0.5, coefficients[2], 1E-12);
        assertEquals(-0.5, coefficients[3], 1E-12);
        assertEquals(3.0, tableau.f.getValue(coefficients), 1E-12);
    }
}