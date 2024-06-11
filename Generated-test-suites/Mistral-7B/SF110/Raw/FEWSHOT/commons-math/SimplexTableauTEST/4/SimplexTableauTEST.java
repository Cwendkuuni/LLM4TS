package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.linear.*;
import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-2.0, 4.0}}), new double[]{-3.0, 5.0});
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 3.0}, {-1.0, 1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, 2.0}, {-1.0, 2.0}}), Relationship.GEQ, 0.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1E-12);

        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(5, tableau.getNumSlackVariables());
        assertEquals(2, tableau.getNumArtificialVariables());
        assertEquals(13, tableau.getWidth());
        assertEquals(6, tableau.getHeight());

        assertArrayEquals(new double[]{1.0, 2.0, -3.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0}, tableau.getData()[0], 1E-12);
        assertArrayEquals(new double[]{-2.0, 4.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0}, tableau.getData()[1], 1E-12);
        assertArrayEquals(new double[]{-1.0, 3.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0}, tableau.getData()[2], 1E-12);
        assertArrayEquals(new double[]{-1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0}, tableau.getData()[3], 1E-12);
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[4], 1E-12);
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[5], 1E-12);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0}, tableau.getData()[6], 1E-12);

        assertEquals(3.0, tableau.getEntry(0, 12), 1E-12);
        assertEquals(-1.0 * 3.0, tableau.getEntry(1, 12), 1E-12);
        assertEquals(0.0, tableau.getEntry(1, 13), 1E-12);
        assertEquals(-1.0 * 5.0, tableau.getEntry(2, 13), 1E-12);

        tableau.dropPhase1Objective();

        assertEquals(1, tableau.getNumObjectiveFunctions());
        assertEquals(5, tableau.getWidth());
        assertEquals(6, tableau.getHeight());

        assertArrayEquals(new double[]{1.0, 2.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0}, tableau.getData()[0], 1E-12);
        assertArrayEquals(new double[]{-2.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0}, tableau.getData()[1], 1E-12);
        assertArrayEquals(new double[]{-1.0, 3.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0}, tableau.getData()[2], 1E-12);
        assertArrayEquals(new double[]{-1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0}, tableau.getData()[3], 1E-12);
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[4], 1E-12);
        assertArrayEquals(new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, tableau.getData()[5], 1E-12);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0}, tableau.getData()[6], 1E-12);

        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, solution.getPoint(), 1E-12);
        assertEquals(-1.0 / 3.0 * (-3.0), solution.getValue(), 1E-12);
    }
}