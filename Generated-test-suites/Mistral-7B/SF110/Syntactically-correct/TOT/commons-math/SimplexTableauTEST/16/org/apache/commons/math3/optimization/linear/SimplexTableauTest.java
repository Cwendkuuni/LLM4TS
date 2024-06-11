package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    private SimplexTableau tableau;

    @Before
    public void setUp() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, -2.0, 3.0}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{0.0, 2.0, 1.0}), Relationship.LEQ, 1.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 1.0, 1.0}), Relationship.GEQ, 2.0));
        tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
    }

    @Test
    public void testBasicTableau() {
        assertEquals(3, tableau.getOriginalNumDecisionVariables());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(10, tableau.maxUlps);
        assertEquals(1e-10, tableau.epsilon);
        assertNotNull(tableau.f);
        assertNotNull(tableau.constraints);
        assertArrayEquals(new double[]{1.0, -2.0, 3.0}, tableau.f.getCoefficients().toArray(), 0.0);
        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testCreateTableau() {
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0},
                {1.0, -2.0, 3.0, 0.0, 0.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0}
        });
        RealMatrix tableau = this.tableau.createTableau(true);
        assertEquals(expectedTableau, tableau);
    }

    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 2.0, 3.0}), Relationship.GEQ, 1.0));
        originalConstraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 2.0, -3.0}), Relationship.LEQ, 2.0));
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);
        LinearConstraint constraint1 = normalizedConstraints.get(0);
        LinearConstraint constraint2 = normalizedConstraints.get(1);
        assertEquals(ConstraintStatus.MEETS, constraint1.getStatus());
        assertEquals(ConstraintStatus.MEETS, constraint2.getStatus());
        assertEquals(new RealVector(new double[]{1.0, 2.0, -3.0}), constraint2.getCoefficients());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableauWithInvalidConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealVector(new double[]{1.0, -2.0, 3.0}), 1.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{0.0, 2.0, 1.0}), Relationship.GEQ, Double.NaN));
        new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10);
    }

    @Test
    public void testGetSolution() {
        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();
        assertEquals(2, coefficients.length);
        assertEquals(4.0, coefficients[0], 1e-10);
        assertEquals(3.0, coefficients[1], 1e-10);
        assertEquals(5.0, tableau.f.getValue(coefficients), 1e-10);
    }

    @Test
    public void testDropPhase1Objective() {
        tableau.dropPhase1Objective();
        assertEquals(1, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testIsOptimal() {
        assertTrue(tableau.isOptimal());
    }
}