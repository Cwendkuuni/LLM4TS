import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction function = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, new double[]{-3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(4, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertEquals(1e-6, tableau.getEpsilon(), 1e-7);
        Assert.assertEquals(10, tableau.getMaxUlps());

        Assert.assertTrue(tableau.isInstanceOf(SimplexTableau.class));
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction function = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, new double[]{-3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        List<LinearConstraint> constraints = Arrays.asList(constraint1, constraint2);
        List<LinearConstraint> normalizedConstraints = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-6).normalizeConstraints(constraints);

        Assert.assertEquals(2, normalizedConstraints.size());
        Assert.assertNotSame(constraint1, normalizedConstraints.get(0));
        Assert.assertNotSame(constraint2, normalizedConstraints.get(1));

        LinearConstraint normalizedConstraint1 = (LinearConstraint) normalizedConstraints.get(0);
        LinearConstraint normalizedConstraint2 = (LinearConstraint) normalizedConstraints.get(1);

        Assert.assertEquals(Relationship.GEQ, normalizedConstraint1.getRelationship());
        Assert.assertEquals(-1.0 * constraint1.getValue(), normalizedConstraint1.getValue(), 1e-7);

        Assert.assertEquals(Relationship.LEQ, normalizedConstraint2.getRelationship());
        Assert.assertEquals(-1.0 * constraint2.getValue(), normalizedConstraint2.getValue(), 1e-7);
    }

    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction function = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, new double[]{-3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, -3.0, 1.0, 1.0, 1.0, -1.0, 2.0, 1.0, 1.0, 0.0, 0.0},
                {1.0, 1.0, 3.0, -1.0, 1.0, 1.0, -1.0, 2.0, 1.0, 1.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 3.0}});

        RealMatrix tableauMatrix = tableau.getTableau();

        Assert.assertArrayEquals(expectedTableau.getData(), tableauMatrix.getData(), 1e-7);
    }

    @Test
    public void testIsOptimal() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, new double[][]{
                {1.0, 2.0}, {2.0, 3.0}
        });
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        tableau.dropPhase1Objective();

        double[][] tableauData = tableau.getData();
        double mostNegativeEntry = Double.NEGATIVE_INFINITY;
        int mostNegativeColumnIndex = -1;

        for (int col = 0; col < tableauData[0].length; ++col) {
            if (tableauData[0][col] < mostNegativeEntry) {
                mostNegativeEntry = tableauData[0][col];
                mostNegativeColumnIndex = col;
            }
        }

        Assert.assertEquals(-1, mostNegativeColumnIndex);
        Assert.assertTrue(tableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction function = new QuadraticObjectiveFunction(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, new double[]{-3.0, 4.0});
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        PointValuePair solution = tableau.getSolution();

        Assert.assertArrayEquals(new double[]{-1.5, 2.5}, solution.getPoint(), 1e-7);
        Assert.assertEquals(5.5, solution.getValue(), 1e-7);
    }

    @Test
    public void testDivideRow() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, new double[][]{
                {1.0, 2.0}, {2.0, 3.0}
        });
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        int dividendRow = 1;
        double divisor = 2.0;

        tableau.divideRow(dividendRow, divisor);

        double[][] tableauData = tableau.getData();

        for (int i = 0; i < tableauData[dividendRow].length; ++i) {
            Assert.assertEquals(tableauData[0][i] / divisor, tableauData[dividendRow][i], 1e-7);
        }
    }

    @Test
    public void testSubtractRow() {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-3.0, 4.0}, new double[][]{
                {1.0, 2.0}, {2.0, 3.0}
        });
        LinearConstraint constraint1 = new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, -1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-2.0, 1.0}, Relationship.GEQ, 2.0);

        SimplexTableau tableau = new SimplexTableau(function, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1e-6);

        int minuendRow = 0;
        int subtrahendRow = 1;
        double multiple = 2.0;

        tableau.subtractRow(minuendRow, subtrahendRow, multiple);

        double[][] tableauData = tableau.getData();

        for (int j = 0; j < tableauData[minuendRow].length; ++j) {
            Assert.assertEquals(tableauData[minuendRow][j] - 2 * tableauData[subtrahendRow][j], tableauData[minuendRow][j], 1e-7);
        }
    }
}