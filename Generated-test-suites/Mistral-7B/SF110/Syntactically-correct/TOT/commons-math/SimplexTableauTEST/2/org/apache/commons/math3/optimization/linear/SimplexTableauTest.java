package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.GEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.LEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(A.getColumnVector(4), Relationship.EQ, 5.0);
        LinearConstraint[] constraints = {constraint1, constraint2, constraint3};
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(3, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testSimplexTableauNormalization() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(A.getColumnVector(4), Relationship.EQ, 5.0);
        LinearConstraint[] constraints = {constraint1, constraint2, constraint3};
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
        LinearConstraintSet linearConstraintSet = new LinearConstraintSet(constraints);
        GoalTypeConstraint goalTypeConstraint = new GoalTypeConstraint(GoalType.MAXIMIZE, linearConstraintSet);
        LinearObjectiveFunction normalizedFunction = tableau.normalizeObjectiveFunction(goalTypeConstraint);
        RealVector normalizedObjectiveCoefficients = normalizedFunction.getCoefficients();
        assertEquals(-1.0, normalizedObjectiveCoefficients.getEntry(0), 1e-6);
        assertEquals(2.0, normalizedObjectiveCoefficients.getEntry(1), 1e-6);
        assertEquals(-3.0, normalizedObjectiveCoefficients.getEntry(2), 1e-6);
        assertEquals(4.0, normalizedObjectiveCoefficients.getEntry(3), 1e-6);
        assertEquals(5.0, normalizedObjectiveCoefficients.getEntry(4), 1e-6);
        assertEquals(-(normalizedObjectiveCoefficients.sum()), normalizedObjectiveCoefficients.getEntry(5), 1e-6);
    }

    @Test
    public void testSimplexTableauCreationWithNegativeConstraints() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.LEQ, -3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.GEQ, -1.0);
        LinearConstraint[] constraints = {constraint1, constraint2};
        try {
            new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimplexTableauCreateTableau() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(A.getColumnVector(4), Relationship.EQ, 5.0);
        LinearConstraint[] constraints = {constraint1, constraint2, constraint3};
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
        RealMatrix expectedTableau = Array2DRowRealMatrix.buildIdentity(10);
        expectedTableau.setEntry(0, 0, -1.0);
        expectedTableau.setEntry(1, 1, 1.0);
        expectedTableau.setEntry(1, 5, 1.0);
        expectedTableau.setEntry(2, 4, 1.0);
        expectedTableau.setEntry(3, 4, -1.0);
        expectedTableau.setEntry(4, 4, -1.0);
        expectedTableau.setEntry(5, 4, -1.0);
        expectedTableau.setEntry(6, 4, -1.0);
        expectedTableau.setEntry(7, 4, -1.0);
        expectedTableau.setEntry(8, 4, -1.0);
        expectedTableau.setEntry(9, 4, -1.0);
        assertEquals(expectedTableau, tableau.getData());
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(A.getColumnVector(4), Relationship.EQ, 5.0);
        LinearConstraint[] constraints = {constraint1, constraint2, constraint3};
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
        tableau.dropPhase1Objective();
        RealMatrix expectedTableau = Array2DRowRealMatrix.identity(5);
        expectedTableau.setEntry(0, 4, -1.0);
        expectedTableau.setEntry(1, 4, 1.0);
        expectedTableau.setEntry(2, 4, 1.0);
        expectedTableau.setEntry(3, 0, -1.0);
        expectedTableau.setEntry(3, 1, 1.0);
        expectedTableau.setEntry(3, 4, 1.0);
        expectedTableau.setEntry(4, 0, -1.0);
        expectedTableau.setEntry(4, 1, 1.0);
        expectedTableau.setEntry(4, 4, 1.0);
        assertEquals(expectedTableau, tableau.getData());
    }

    @Test
    public void testSimplexTableauGetSolution() {
        RealMatrix A = Array2DRowRealMatrix.identity(5);
        LinearObjectiveFunction f = new LinearObjectiveFunction(VectorUtils.createVector(new double[]{-1.0, 2.0, -3.0, 4.0, 5.0}));
        LinearConstraint constraint1 = new LinearConstraint(A.getColumnVector(0), Relationship.LEQ, 3.0);
        LinearConstraint constraint2 = new LinearConstraint(A.getColumnVector(1), Relationship.GEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(A.getColumnVector(4), Relationship.EQ, 5.0);
        LinearConstraint[] constraints = {constraint1, constraint2, constraint3};
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraints), GoalType.MAXIMIZE, false, 1e-6);
        PointValuePair solution = tableau.getSolution();
        RealVector expectedSolution = VectorUtils.createVector(new double[]{1.0, 0.0, 0.0, 0.0, 1.0});
        assertEquals(expectedSolution, solution.getPoint());
        assertEquals(10.0, solution.getValue(), 1e-6);
    }
}