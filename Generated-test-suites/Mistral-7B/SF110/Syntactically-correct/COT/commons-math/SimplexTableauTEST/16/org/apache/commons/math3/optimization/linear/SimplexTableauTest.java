import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        Assert.assertEquals(2, tableau.getNumObjectiveFunctions());
        Assert.assertEquals(3, tableau.getOriginalNumDecisionVariables());
        Assert.assertEquals(2, tableau.getNumSlackVariables());
        Assert.assertEquals(1, tableau.getNumArtificialVariables());
        Assert.assertNotNull(tableau.getTableau());
    }

    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        List<String> expectedColumnLabels = Arrays.asList("W", "Z", "x0", "x1", "x2", "x-");
        List<String> actualColumnLabels = tableau.getColumnLabels();
        Assert.assertEquals(expectedColumnLabels, actualColumnLabels);
    }

    @Test
    public void testCreateTableauWithOneObjectiveFunction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{3.0, 2.0, 1.0}, 1.0);
        List<LinearConstraint> constraints = Arrays.asList(new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0),
                new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0));
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 2.0},
                {3.0, 2.0, 1.0, 0.0, 0.0, 0.0, 1.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
        });
        RealMatrix actualTableau = tableau.getTableau();
        Assert.assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testCreateTableauWithTwoObjectiveFunctions() {
        LinearObjectiveFunction f1 = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{1.0, -2.0, 1.0}, 2.0);
        List<LinearConstraint> constraints = Arrays.asList(new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0),
                new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0));
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f1, constraints, goalType, restrictToNonNegative, epsilon, maxUlps);
        tableau.addObjectiveFunction(f2);
        RealMatrix expectedTableau = new Array2DRowRealMatrix(new double[][]{
                {-1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 2.0, 0.0},
                {-1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 2.0, 0.0},
                {3.0, 2.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0},
                {1.0, -2.0, 1.0, 0.0, 0.0, 0.0, 1.0, -1.0}
        });
        RealMatrix actualTableau = tableau.getTableau();
        Assert.assertEquals(expectedTableau, actualTableau);
    }

    @Test
    public void testNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{3.0, 2.0, 1.0}, 1.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        List<LinearConstraint> expectedConstraints = Arrays.asList(
                new LinearConstraint(new double[]{-1.0, -1.0, -1.0}, Relationship.GEQ, -2.0),
                new LinearConstraint(new double[]{1.0, 1.0, -1.0}, Relationship.LEQ, 1.0)
        );
        List<LinearConstraint> actualConstraints = tableau.getConstraints();
        Assert.assertEquals(expectedConstraints, actualConstraints);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, null, goalType, restrictToNonNegative, epsilon, maxUlps);
    }

    @Test
    public void testGetWidth() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        int expectedWidth = 7;
        int actualWidth = tableau.getWidth();
        Assert.assertEquals(expectedWidth, actualWidth);
    }

    @Test
    public void testGetHeight() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        int expectedHeight = 3;
        int actualHeight = tableau.getHeight();
        Assert.assertEquals(expectedHeight, actualHeight);
    }

    @Test
    public void testGetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        double expectedEntry = -1.0;
        double actualEntry = tableau.getEntry(0, 0);
        Assert.assertEquals(expectedEntry, actualEntry, 1.0E-6);
    }

    @Test
    public void testSetEntry() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        double newEntry = 1.1;
        tableau.setEntry(0, 0, newEntry);
        double expectedEntry = 1.1;
        double actualEntry = tableau.getEntry(0, 0);
        Assert.assertEquals(expectedEntry, actualEntry, 1.0E-6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, 2.0, 1.0}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{1.0, 2.0, -1.0}, Relationship.GEQ, 1.0);
        GoalType goalType = GoalType.MAXIMIZE;
        boolean restrictToNonNegative = true;
        double epsilon = 1.0E-6;
        int maxUlps = 10;
        SimplexTableau tableau = new SimplexTableau(f, Arrays.asList(constraint1, constraint2), goalType, restrictToNonNegative, epsilon, maxUlps);
        tableau.setEntry(5, 5, 1.1);
        double actualEntry = tableau.getEntry(5, 5);
        Assert.fail();
    }
}