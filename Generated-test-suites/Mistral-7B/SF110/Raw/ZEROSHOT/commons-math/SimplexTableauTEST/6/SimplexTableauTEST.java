import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.LEQ, 1.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-2.0, -1.0}}), Relationship.GEQ, 1.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(2, tableau.getNumArtificialVariables());
        Assert.assertEquals(5, tableau.getHeight());
        Assert.assertEquals(6, tableau.getWidth());
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.LEQ, 1.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-2.0, -1.0}}), Relationship.GEQ, 1.0));

        LinearObjectiveFunction fNormalized = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> normalizedConstraints = new ArrayList<>();
        normalizedConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, -1.0}}), Relationship.GEQ, -1.0));
        normalizedConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-2.0, 1.0}}), Relationship.LEQ, -1.0));

        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        SimplexTableau normalizedTableau = new SimplexTableau(fNormalized, normalizedConstraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(normalizedTableau.getConstraints(), tableau.normalizeConstraints(constraints).subList(0, 2));
    }

    @Test
    public void testGetNumObjectiveFunctions() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(1, tableau.getNumObjectiveFunctions());

        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 7.0);
        LinearObjectiveFunction f3 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{5.0, 6.0}, {7.0, 8.0}}), 7.0);
        List<LinearConstraint> constraints2 = new ArrayList<>();
        SimplexTableau tableau2 = new SimplexTableau(new LinearFunctionCollection(new LinearObjectiveFunction[]{f2, f3}), constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(2, tableau2.getNumObjectiveFunctions());
    }

    @Test
    public void testGetConstraintTypeCounts() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), Relationship.LEQ, 1.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-2.0, -1.0}}), Relationship.GEQ, 1.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(2, tableau.getConstraintTypeCounts(Relationship.LEQ));
        Assert.assertEquals(2, tableau.getConstraintTypeCounts(Relationship.GEQ));
    }

    @Test
    public void testGetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(1.0, tableau.getEntry(0, 0), 1e-10);
        Assert.assertEquals(2.0, tableau.getEntry(0, 1), 1e-10);
        Assert.assertEquals(3.0, tableau.getEntry(0, 2), 1e-10);
        Assert.assertEquals(7.0, tableau.getEntry(0, 5), 1e-10);
        Assert.assertEquals(-1.0, tableau.getEntry(1, 3), 1e-10);
        Assert.assertEquals(1.0, tableau.getEntry(1, 4), 1e-10);
        Assert.assertEquals(-1.0, tableau.getEntry(1, 5), 1e-10);
        Assert.assertEquals(1.0, tableau.getEntry(1, 6), 1e-10);
    }

    @Test
    public void testSetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        tableau.setEntry(0, 1, 10.0);
        Assert.assertEquals(10.0, tableau.getEntry(0, 1), 1e-10);
    }

    @Test
    public void testGetWidth() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(6, tableau.getWidth());
    }

    @Test
    public void testGetHeight() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        Assert.assertEquals(2, tableau.getHeight());
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}), 7.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-10, 10);
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        tableau.setEntry(0, 5, 8.0);
        tableau.setEntry(1, 3, -1.0);
        tableau.setEntry(1, 4, 1.0);
        tableau.setEntry(1, 5, -1.0);
        tableau.setEntry(1, 6, 1.0);

        Assert.assertFalse(tableau.isOptimal());

        tableau.setEntry(1, 3, -1.1);
        Assert.assertTrue(tableau.isOptimal());
    }
}