import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SimplexTableauTest {

    private static final double EPSILON = 1e-9;
    private SimplexTableau simplexTableau;

    @Before
    public void setup() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new RealVector(new double[]{1.0, 2.0, -3.0}));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{2.0, 1.0, -1.0}), Relationship.LEQ, 1.0));
        constraints.add(new LinearConstraint(new RealVector(new double[]{-1.0, 2.0, 1.0}), Relationship.GEQ, 0.0));
        simplexTableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, EPSILON);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(simplexTableau);
    }

    @Test
    public void testInitializeColumnLabels() {
        List<String> columnLabels = simplexTableau.getColumnLabels();
        Assert.assertEquals(13, columnLabels.size());
        Assert.assertEquals("W", columnLabels.get(0));
        Assert.assertEquals("Z", columnLabels.get(1));
        Assert.assertEquals("x0", columnLabels.get(2));
        Assert.assertEquals("x1", columnLabels.get(3));
        Assert.assertEquals("x2", columnLabels.get(4));
        Assert.assertEquals("x-", columnLabels.get(5));
        Assert.assertEquals("s0", columnLabels.get(6));
        Assert.assertEquals("s1", columnLabels.get(7));
        Assert.assertEquals("a0", columnLabels.get(8));
        Assert.assertEquals("a1", columnLabels.get(9));
        Assert.assertEquals("RHS", columnLabels.get(10));
        Assert.assertNull(columnLabels.get(11));
    }

    @Test
    public void testCreateTableau() {
        RealMatrix tableau = simplexTableau.getTableau();
        int width = tableau.getColumnDimension();
        int height = tableau.getRowDimension();
        Assert.assertEquals(height, 3);
        Assert.assertEquals(width, 13);

        // Check Z column
        Assert.assertEquals(tableau.getEntry(0, 1), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 1), -1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, 1), 0.0, EPSILON);

        // Check objective coefficients
        Assert.assertEquals(tableau.getEntry(0, 0), -1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 0), -2.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, 0), -3.0, EPSILON);

        // Check constraints
        Assert.assertEquals(tableau.getEntry(1, 2), 2.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 3), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 6), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, 2), -1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, 3), 2.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, 7), 1.0, EPSILON);

        // Check RHS values
        Assert.assertEquals(tableau.getEntry(1, width - 1), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(2, width - 1), 0.0, EPSILON);
    }

    @Test
    public void testNormalizeConstraints() {
        List<LinearConstraint> originalConstraints = simplexTableau.getConstraints();
        List<LinearConstraint> normalizedConstraints = simplexTableau.normalizeConstraints(originalConstraints);
        Assert.assertEquals(normalizedConstraints.size(), 2);

        LinearConstraint constraint1 = normalizedConstraints.get(0);
        LinearConstraint constraint2 = normalizedConstraints.get(1);

        Assert.assertEquals(constraint1.getCoefficients().getDimension(), 3);
        Assert.assertEquals(constraint1.getValue(), 0.0, EPSILON);
        Assert.assertEquals(constraint1.getRelationship(), Relationship.GEQ);

        Assert.assertEquals(constraint2.getCoefficients().getDimension(), 3);
        Assert.assertEquals(constraint2.getValue(), -1.0, EPSILON);
        Assert.assertEquals(constraint2.getRelationship(), Relationship.LEQ);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidConstraint() {
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new RealVector(new double[]{1.0, 2.0}), Relationship.EQ, Double.NaN));
        new SimplexTableau(new LinearObjectiveFunction(new RealVector()), constraints, GoalType.MAXIMIZE, false, EPSILON);
    }

    @Test
    public void testIsOptimal() {
        Assert.assertTrue(simplexTableau.isOptimal());
    }

    @Test
    public void testGetSolution() {
        PointValuePair solution = simplexTableau.getSolution();
        Assert.assertArrayEquals(new double[]{0.0, 1.0, 1.0}, solution.getPoint(), EPSILON);
        Assert.assertEquals(3.0, solution.getValue(), EPSILON);
    }

    @Test
    public void testDropPhase1Objective() {
        simplexTableau.dropPhase1Objective();
        RealMatrix tableau = simplexTableau.getTableau();
        int width = tableau.getColumnDimension();
        int height = tableau.getRowDimension();

        Assert.assertEquals(height, 2);
        Assert.assertEquals(width, 11);

        // Check Z column
        Assert.assertEquals(tableau.getEntry(0, 1), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 1), -1.0, EPSILON);

        // Check objective coefficients
        Assert.assertEquals(tableau.getEntry(0, 0), 0.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 0), 0.0, EPSILON);

        // Check constraints
        Assert.assertEquals(tableau.getEntry(0, 2), 2.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(0, 3), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 2), -1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, 3), 2.0, EPSILON);

        // Check RHS values
        Assert.assertEquals(tableau.getEntry(0, width - 1), 1.0, EPSILON);
        Assert.assertEquals(tableau.getEntry(1, width - 1), 0.0, EPSILON);
    }
}