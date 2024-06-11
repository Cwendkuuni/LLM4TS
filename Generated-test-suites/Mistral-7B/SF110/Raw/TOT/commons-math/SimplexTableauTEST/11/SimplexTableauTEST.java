package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testValidConstruction() {
        // Test construction with valid arguments
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.5, 1.5, -2.5}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);
        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1e-12, tableau.epsilon, 0.0);
        assertEquals(10, tableau.maxUlps);
        assertNotNull(tableau.tableau);
    }

    @Test
    public void testInvalidConstruction() {
        // Test construction with invalid arguments (null objective function)
        assertNull(new SimplexTableau(null, Arrays.asList(new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 2.0)), GoalType.MINIMIZE, false, 1e-12));

        // Test construction with invalid arguments (empty constraint list)
        assertNull(new SimplexTableau(new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, 3.0})), Collections.emptyList(), GoalType.MINIMIZE, false, 1e-12));
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.5, 1.5, -2.5}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        List<String> expectedColumnLabels = Arrays.asList("Z", "x0", "x1", "x2", "s0", "s1");
        List<String> actualColumnLabels = new ArrayList<>(tableau.columnLabels);
        assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.5, 1.5, -2.5}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, -2.0, 3.0, 0.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0},
                {0.0, 0.0, 0.0, 1.0, -1.0, 0.0, 1.0, 0.0, 0.0, -1.0, -1.0, -1.0, 2.0},
                {0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0, 1.0, 0.0, 0.0, 1.0, -1.0, 1.0}
        });

        RealMatrix actualTableau = tableau.tableau;
        assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{-1.0, 1.0, -1.0}), Relationship.GEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{-2.0, 1.0, 1.0}), Relationship.LEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);

        LinearConstraint expectedConstraint1 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, -1.0}), Relationship.LEQ, -2.0);
        LinearConstraint expectedConstraint2 = new LinearConstraint(new RealVector(new double[]{1.0, -1.0, 1.0}), Relationship.GEQ, 1.0);
        List<LinearConstraint> expectedConstraints = Arrays.asList(expectedConstraint1, expectedConstraint2);
        List<LinearConstraint> actualConstraints = tableau.constraints;

        assertEquals(expectedConstraints, actualConstraints);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new RealVector(new double[]{-1.0, -2.0, 3.0}));
        LinearConstraint constraint1 = new LinearConstraint(new RealVector(new double[]{1.0, 1.0, -1.0}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new RealVector(new double[]{0.5, 1.5, -2.5}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objFunc, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-12);
        tableau.dropPhase1Objective();

        double[] expectedSolution = new double[]{0.0, 2.0, 1.5};
        PointValuePair expectedPointValuePair = new PointValuePair(new RealVector(expectedSolution), -5.5);
        PointValuePair actualPointValuePair = tableau.getSolution();

        assertEquals(expectedPointValuePair, actualPointValuePair);
    }
}