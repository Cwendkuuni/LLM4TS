import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.Relationship;
import org.junit.Assert;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new PointValuePair(new double[]{1.0, 2.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, 2.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-10);
        Assert.assertNotNull(tableau);

        // Test exception cases
        try {
            new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{}), new PointValuePair(new double[]{})), new ArrayList<>(), GoalType.MAXIMIZE, false, 1.0E-10);
            Assert.fail("Expected exception when objective function or constraints collection is empty");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Objective function or constraints collection is empty", e.getMessage());
        }

        try {
            new SimplexTableau(new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), new PointValuePair(new double[]{1.0, 2.0})), new ArrayList<>(), GoalType.MAXIMIZE, false, 1.0E-10);
            Assert.fail("Expected exception when constraints collection is empty");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Constraints collection is empty", e.getMessage());
        }
    }
}