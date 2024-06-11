import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new Quadric(2.0, -2.0, new double[]{-1.0, 1.0}), null);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0}, {1.0}}), Relationship.LEQ, 0.0);
        constraints.add(constraint1);
        SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        // Check the initial state
        Assert.assertEquals(1, simplexTableau.getNumObjectiveFunctions());
        Assert.assertEquals(2, simplexTableau.getOriginalNumDecisionVariables());
        Assert.assertEquals(1, simplexTableau.getNumSlackVariables());
        Assert.assertEquals(1, simplexTableau.getNumArtificialVariables());
        Assert.assertEquals(13, simplexTableau.getWidth());
        Assert.assertEquals(14, simplexTableau.getHeight());

        // Test getBasicRow
        Assert.assertEquals(0, simplexTableau.getBasicRow(0));
        Assert.assertEquals(null, simplexTableau.getBasicRow(2));
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction objectiveFunction = new QuadraticObjectiveFunction(new Quadric(2.0, -2.0, new double[]{-1.0, 1.0}), null);
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0}, {1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0}, {1.0}}), Relationship.GEQ, 1.0);
        Collection<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(constraint1);
        originalConstraints.add(constraint2);

        SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, originalConstraints, GoalType.MINIMIZE, false, 1e-6, 10);
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(originalConstraints);

        Assert.assertEquals(2, normalizedConstraints.size());
        Assert.assertFalse(normalizedConstraints.get(0).getValue() > 0.0);
        Assert.assertTrue(normalizedConstraints.get(1).getValue() > 0.0);
    }

    @Test
    public void testGetSolution() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 3.0}}), new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}}), new double[]{-1.0, 1.0});
        Collection<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0}, {1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}), Relationship.GEQ, 2.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        PointValuePair solution = simplexTableau.getSolution();

        Assert.assertArrayEquals(new double[]{1.0, 0.0}, solution.getPoint(), 1e-6);
        Assert.assertEquals(-3.0, solution.getValue(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideRowWithZeroDivisor() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {2.0, 3.0}}), new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {1.0, 0.0}}), new double[]{-1.0, 1.0});
        Collection<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0}, {1.0}}), Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}), Relationship.GEQ, 2.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        simplexTableau.divideRow(0, 0.0);
    }

    @Test
    public void testDropPhase1Objective() {
        LinearObjectiveFunction objectiveFunction1 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, 1.0}, {3.0, -1.0}}), new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}}), new double[]{0.0, 1.0, 1.0});
        LinearObjectiveFunction objectiveFunction2 = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}}), new double[]{1.0});
        Collection<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}}), Relationship.EQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {1.0, 1.0}}), Relationship.LEQ, 1.0);
        LinearConstraint constraint3 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{3.0, -1.0}}), Relationship.GEQ, 1.0);
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);
        SimplexTableau simplexTableau = new SimplexTableau(objectiveFunction1, constraints, GoalType.MINIMIZE, false, 1e-6, 10);

        simplexTableau.dropPhase1Objective();

        SimplexTableau expectedSimplexTableau = new SimplexTableau(objectiveFunction2, constraints, GoalType.MINIMIZE, false, 1e-6, 10);
        Assert.assertArrayEquals(expectedSimplexTableau.getData(), simplexTableau.getData());
    }
}