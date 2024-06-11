package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.rel.Relationship;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(2, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1.0E-6, tableau.epsilon, 1.0E-8);
        assertEquals(10, tableau.maxUlps);
        assertEquals(3, tableau.getWidth());
        assertEquals(3, tableau.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructionInvalidObjectiveFunction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), null);
        LinearConstraint constraint = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        new SimplexTableau(objectiveFunction, Arrays.asList(constraint), GoalType.MAXIMIZE, false, 1.0E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexTableauConstructionInvalidConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, Double.NaN);
        new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
    }

    @Test
    public void testSimplexTableauInitializeColumnLabels() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        List<String> expectedColumnLabels = Arrays.asList("Z", "W", "x0", "x1", "x-");
        assertEquals(expectedColumnLabels, tableau.columnLabels);
    }

    @Test
    public void testSimplexTableauCreateTableau() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 1.0, 1.0, 1.0, -1.0, 0.0, 0.0},
                {2.0, -3.0, 0.0, 0.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                {1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, -1.0, 0.0, 0.0}});
        assertEquals(expectedTableau, tableau.tableau);
    }

    @Test
    public void testSimplexTableauNormalizeConstraints() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        List<LinearConstraint> expectedConstraints = Arrays.asList(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.GEQ, 0.0),
                new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 1.0));
        assertEquals(expectedConstraints, tableau.constraints);
    }

    @Test
    public void testSimplexTableauGetSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        PointValuePair solution = tableau.getSolution();
        double[] expectedCoefficients = {1.0 / 3.0, -1.0 / 3.0};
        assertEquals(expectedCoefficients, solution.getPoint(), 1.0E-6);
        assertEquals(3.0, solution.getValue(), 1.0E-6);
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {-3.0, 1.0}}), 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, -1.0}}), Relationship.GEQ, 1.0);
        SimplexTableau tableau = new SimplexTableau(objectiveFunction1, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        tableau.dropPhase1Objective();
        LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{3.0 / 3.0, 1.0 / 3.0}, {-1.0 / 3.0, 2.0 / 3.0}}), 1.0);
        SimplexTableau expectedTableau = new SimplexTableau(objectiveFunction2, Arrays.asList(constraint1, constraint2), GoalType.MAXIMIZE, false, 1.0E-6);
        assertEquals(expectedTableau.tableau.getData(), tableau.tableau.getData());
    }
}