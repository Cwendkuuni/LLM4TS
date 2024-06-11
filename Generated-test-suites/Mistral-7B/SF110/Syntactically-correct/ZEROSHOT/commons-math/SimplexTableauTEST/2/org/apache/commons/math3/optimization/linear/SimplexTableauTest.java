import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertEquals(objectiveFunction, tableau.getFunction());
        Assert.assertEquals(constraints, tableau.getConstraints());
        Assert.assertEquals(false, tableau.isRestrictToNonNegative());
        Assert.assertEquals(1e-6, tableau.getEpsilon(), 0.0);
        Assert.assertEquals(10, tableau.getMaxUlps());
        Assert.assertNull(tableau.getTableau());
        Assert.assertEquals(2, tableau.getNumDecisionVariables());
        Assert.assertEquals(0, tableau.getNumSlackVariables());
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
    }

    @Test
    public void testSimplexTableauConstructionWithRestrictToNonNegative() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, true, 1e-6);

        Assert.assertEquals(objectiveFunction, tableau.getFunction());
        Assert.assertEquals(constraints, tableau.getConstraints());
        Assert.assertEquals(true, tableau.isRestrictToNonNegative());
        Assert.assertEquals(1e-6, tableau.getEpsilon(), 0.0);
        Assert.assertEquals(10, tableau.getMaxUlps());
        Assert.assertNull(tableau.getTableau());
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(0, tableau.getNumSlackVariables());
        Assert.assertEquals(0, tableau.getNumArtificialVariables());
    }

    @Test
    public void testSimplexTableauConstructionWithTwoObjectiveFunctions() {
        LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), 4.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(new LinearComboOptimizer(objectiveFunction1, objectiveFunction2), constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertNotNull(tableau.getTableau());
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(2, tableau.getNumArtificialVariables());
        Assert.assertEquals(1e-6, tableau.getEpsilon(), 0.0);
        Assert.assertEquals(10, tableau.getMaxUlps());
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
        RealMatrix matrix = tableau.createTableau(true);

        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(6, matrix.getColumnDimension());
        Assert.assertArrayEquals(new double[]{-1.0, 1.0, 1.0, 5.0, 3.0, 4.0, 3.0, 2.0, 5.0, 1.0, 1.0, 1.0}, getValuesFromMatrix(matrix), 0.001);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);
        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(constraints);

        Assert.assertEquals(2, normalizedConstraints.size());
        Assert.assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-2.0, 1.0}}), Relationship.GEQ, -3.0), normalizedConstraints.get(0));
        Assert.assertEquals(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0), normalizedConstraints.get(1));
    }

    @Test
    public void testGetBasicRow() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertEquals(0, tableau.getBasicRow(0));
        Assert.assertEquals(1, tableau.getBasicRow(1));
        Assert.assertEquals(null, tableau.getBasicRow(2));
        Assert.assertEquals(null, tableau.getBasicRow(3));
        Assert.assertEquals(null, tableau.getBasicRow(4));
        Assert.assertEquals(null, tableau.getBasicRow(5));
        Assert.assertEquals(null, tableau.getBasicRow(6));
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);

        Assert.assertEquals(false, tableau.isOptimal());

        tableau.dropPhase1Objective();
        Assert.assertEquals(true, tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6);

        PointValuePair solution = tableau.getSolution();
        Assert.assertArrayEquals(new double[]{2.0 / 3.0, 1.0 / 3.0}, solution.getPoint(), 0.001);
        Assert.assertEquals(5.0 / 3.0, solution.getValue(), 0.001);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {-2.0, 1.0}}), 4.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 3.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {1.0, 1.0}}), Relationship.GEQ, 2.0));

        SimplexTableau tableau = new SimplexTableau(new LinearComboOptimizer(objectiveFunction1, objectiveFunction2), constraints, GoalType.MINIMIZE, false, 1e-6);

        tableau.dropPhase1Objective();

        Assert.assertEquals(1, tableau.getNumObjectiveFunctions());
        Assert.assertArrayEquals(new double[]{-1.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, -1.0 / 3.0, -2.0 / 3.0, 1.0 / 3.0}, getValuesFromMatrix(tableau.getTableau()).getData()[0], 0.001);
    }

    private double[] getValuesFromMatrix(RealMatrix matrix) {
        double[] values = new double[matrix.getRowDimension() * matrix.getColumnDimension()];
        int index = 0;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            System.arraycopy(matrix.getRowVector(i).toArray(), 0, values, index, matrix.getColumnDimension());
            index += matrix.getColumnDimension();
        }
        return values;
    }
}