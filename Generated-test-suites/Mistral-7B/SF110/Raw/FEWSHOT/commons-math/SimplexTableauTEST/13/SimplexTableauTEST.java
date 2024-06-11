package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.io.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauInitialization() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-9;
        int maxUlps = 12;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(6, tableau.getOriginalNumDecisionVariables());
        assertEquals(3, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());
        assertEquals(epsilon, tableau.getEpsilon(), maxUlps);
        assertEquals(maxUlps, tableau.getMaxUlps());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-9;
        int maxUlps = 12;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0, -1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 2.0, 2.0},
                {0.0, 0.0, 0.0, -1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, -1.0, 0.0, 1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 1.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        });
        RealMatrix tableau = tableau.getTableau();
        MatrixUtils.assertEquals(expectedTableau, tableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-9;
        int maxUlps = 12;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        LinearConstraint[] normalizedConstraints = tableau.getConstraints().toArray(new LinearConstraint[0]);
        LinearConstraint constraint1Norm = normalizedConstraints[0];
        LinearConstraint constraint2Norm = normalizedConstraints[1];
        assertEquals(ConstraintQuality.QUALIFIED, constraint1Norm.getQuality());
        assertEquals(ConstraintQuality.QUALIFIED, constraint2Norm.getQuality());
        assertTrue(constraint1Norm.getValue() >= 0.0);
        assertTrue(constraint2Norm.getValue() >= 0.0);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-9;
        int maxUlps = 12;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        tableau.dropPhase1Objective();
        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, 2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1.0E-9;
        int maxUlps = 12;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        PointValuePair solution = tableau.getSolution();
        assertEquals(3, solution.getPoint().length);
        assertTrue(solution.getValue() >= 0.0);
    }
}