package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionCollection;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintCollection.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionCollection.ObjectiveType;
import org.apache.commons.math3.optim.nonlinear.scalar.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SimplexTableauTest {

    private LinearObjectiveFunction f;
    private List<LinearConstraint> constraints;

    @Before
    public void setup() {
        // Set up test data for SimplexTableau
        double[][] coefficientsA = {{1.0, 2.0, 3.0}, {-1.0, 1.0, 2.0}, {-3.0, -2.0, 1.0}};
        double[] objectiveCoefficients = {2.0, -1.0, 1.0};
        double constantTerm = 5.0;

        f = new LinearObjectiveFunction(coefficientsA, objectiveCoefficients, constantTerm);

        List<LinearConstraint> constraintsList = new ArrayList<>();
        LinearConstraint c1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}, {-3.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint c2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}, {-3.0, 2.0}}), Relationship.GEQ, 0.0);
        LinearConstraint c3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, -1.0}, {-3.0, 2.0}}), Relationship.EQ, 0.0);
        constraintsList.add(c1);
        constraintsList.add(c2);
        constraintsList.add(c3);
        constraints = constraintsList;
    }

    @Test
    public void testSimplexTableauConstruction() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        assertEquals("Expected number of decision variables", 3, tableau.getNumDecisionVariables());
        assertEquals("Expected number of slack variables", 3, tableau.getNumSlackVariables());
        assertEquals("Expected number of artificial variables", 0, tableau.getNumArtificialVariables());
        assertEquals("Expected number of objective functions", 1, tableau.getNumObjectiveFunctions());
        assertTrue("Expected non-null tableau data", tableau.getData() != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauInvalidConstraintCollection() {
        List<LinearConstraint> constraints2 = new ArrayList<>();
        constraints2.add(null);
        new SimplexTableau(f, constraints2, GoalType.MAXIMIZE, false, 1e-6, 10);
    }

    @Test
    public void testSimplexTableauIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        assertTrue("Expected optimal solution", tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauNotOptimal() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 2.0}}), null, 0.0),
            constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        assertFalse("Expected non-optimal solution", tableau.isOptimal());
    }

    @Test
    public void testSimplexTableauGetSolution() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        PointValuePair solution = tableau.getSolution();
        assertEquals("Expected optimal solution", 4.0, solution.getPoint()[0], 1e-6);
        assertEquals("Expected optimal solution", 5.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testSimplexTableauDropphase1Objective() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        tableau.dropPhase1Objective();
        assertEquals("Expected number of objective functions after dropPhase1Objective", 0, tableau.getNumObjectiveFunctions());
        assertEquals("Expected number of artificial variables after dropPhase1Objective", 0, tableau.getNumArtificialVariables());
    }

    @Test
    public void testSimplexTableauDivideRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        int dividendRow = 0;
        double divisor = 2.0;
        tableau.divideRow(dividendRow, divisor);
        double entry = tableau.getEntry(dividendRow, 0);
        assertTrue("Expected divided entry", Precision.equals(entry, 0.5, 1e-6));
    }

    @Test
    public void testSimplexTableauSubtractRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = -1.0;
        tableau.subtractRow(minuendRow, subtrahendRow, multiple);
        double entry = tableau.getEntry(minuendRow, 0);
        assertTrue("Expected subtracted entry", Precision.equals(entry, -1.5, 1e-6));
    }

    @Test
    public void testSimplexTableauNormalizeConstraints() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);
        assertEquals("Expected normalized constraint number", 3, normalizedConstraints.size());
        for (LinearConstraint constraint : normalizedConstraints) {
            assertEquals("Expected normalized constraint value", constraint.getValue(), 0.0, 1e-6);
        }
        for (int i = 0; i < normalizedConstraints.size(); ++i) {
            LinearConstraint constraint = normalizedConstraints.get(i);
            if (constraint.getRelationship() == Relationship.EQ) {
                assertTrue("Expected normalized constraint to be equal", constraint.getCoefficients().isIdentityMatrix());
            } else {
                assertTrue("Expected normalized constraint to have non-zero coefficients", !Arrays.stream(constraint.getCoefficients.getData()).allMatch(d -> d == 0.0));
            }
        }
    }

    @Test
    public void testSimplexTableauCreateTableau() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        RealMatrix expectedTableau = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 0.0, 1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0, 1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0, 1.0, 1.0, 1.0},
            {0.0, -1.0, -1.0 / 3.0, 1.0, 1.0 / 3.0, -1.0, -1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 2.0 / 3.0, 0.0, -1.0 / 3.0, 0.0, -1.0 / 3.0, -1.0 / 3.0, -1.0 / 3.0, -1.0, 1.0, 1.0, 1.0},
            {2.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 1.0},
            {0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0}
        });
        RealMatrix tableauData = tableau.getData();
        assertEquals("Expected tableau data", expectedTableau, tableauData);
    }
}