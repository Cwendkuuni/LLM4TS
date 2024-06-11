package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0},
                {-4.0, 1.0, 3.0},
                {1.0, 1.0, 1.0}
        }), 1.0);

        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {-1.0, 2.0},
                {3.0, 1.0}
        }), Relationship.LEQ, 5.0);

        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {2.0, -1.0},
                {1.0, 1.0},
                {0.0, 1.0}
        }), Relationship.GEQ, 3.0);

        SimplexTableau tableau = new SimplexTableau(objFunction, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-6, 10);

        Assert.assertEquals(3, tableau.getNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());

        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(-3.0, tableau.getEntry(0, 0), 1e-6);
        Assert.assertEquals(-2.0, tableau.getEntry(1, 0), 1e-6);
        Assert.assertEquals(-1.0, tableau.getEntry(2, 0), 1e-6);
        Assert.assertEquals(5.0, tableau.getEntry(2, 2), 1e-6);
        Assert.assertEquals(3.0, tableau.getEntry(2, 3), 1e-6);

        Assert.assertArrayEquals(new double[]{1.0 / 5.0, 2.0 / 5.0, -1.0 / 5.0}, tableau.getSolution().getPoint(), 1e-6);
        Assert.assertEquals(-10.5, tableau.getSolution().getValue(), 1e-6);
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0},
                {-4.0, 1.0, 3.0},
                {1.0, 1.0, 1.0}
        }), 1.0);

        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {-1.0, 2.0},
                {3.0, 1.0}
        }), Relationship.LEQ, 5.0);

        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {2.0, -1.0},
                {1.0, 1.0},
                {0.0, 1.0}
        }), Relationship.GEQ, 3.0);

        SimplexTableau tableau = new SimplexTableau(objFunction, Arrays.asList(constraint1, constraint2), GoalType.MINIMIZE, false, 1e-6, 10);

        tableau.dropPhase1Objective();

        Assert.assertEquals(1, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(-3.0, tableau.getEntry(0, 0), 1e-6);
        Assert.assertEquals(-2.0, tableau.getEntry(1, 0), 1e-6);
        Assert.assertEquals(-1.0, tableau.getEntry(2, 0), 1e-6);
        Assert.assertEquals(5.0, tableau.getEntry(2, 2), 1e-6);
        Assert.assertEquals(3.0, tableau.getEntry(2, 3), 1e-6);

        Assert.assertArrayEquals(new double[]{1.0 / 5.0, 2.0 / 5.0}, tableau.getSolution().getPoint(), 1e-6);
        Assert.assertEquals(-6.0, tableau.getSolution().getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauInvalidObjectiveFunction() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}
        }), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {-1.0, 2.0},
                {3.0, 1.0}
        }), Relationship.LEQ, 5.0));

        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauInvalidConstraints() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{
                {-1.0, 2.0, 3.0},
                {-4.0, 1.0, 3.0},
                {1.0, 1.0, 1.0}
        }), 1.0);

        List<LinearConstraint> constraints = new ArrayList<>();

        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {-1.0, 2.0},
                {3.0, 1.0}
        }), Relationship.LEQ, 5.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {2.0, -1.0},
                {1.0, 1.0},
                {0.0, 1.0}
        }), Relationship.GEQ, 3.0));

        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{
                {-1.0, 0.0},
                {1.0, 1.0},
                {3.0, 1.0}
        }), Relationship.EQ, 5.0));

        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
    }
}