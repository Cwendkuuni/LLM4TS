import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {-3.0, -2.0, 1.0}}), 5.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);

        assertEquals(3, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(5.0, tableau.epsilon, 0.0);
        assertEquals(10, tableau.maxUlps);
        assertEquals(3, tableau.getHeight());
        assertEquals(5, tableau.getWidth());
        assertArrayEquals(new double[]{-1.0, 2.0, 3.0, -3.0, -1.0, 1.0, 1.0, 2.0, 3.0, 5.0, 2.0, 2.0, 3.0, 3.0}, tableau.getData()[0], 0.001);
        assertArrayEquals(new double[]{-3.0, -2.0, 1.0, 1.0, 1.0, -1.0, -1.0, 3.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}, tableau.getData()[1], 0.001);
        assertArrayEquals(new double[]{1.0, 2.0, -1.0, -1.0}, tableau.getData()[2], 0.001);
        assertArrayEquals(new double[]{1.0, 2.0}, tableau.getData()[3], 0.001);
        assertArrayEquals(new double[]{-1.0, 1.0}, tableau.getData()[4], 0.001);
        assertEquals(2.0, tableau.getEntry(1, 5), 0.001);
        assertEquals(3.0, tableau.getEntry(2, 5), 0.001);
        assertEquals(2.0, tableau.getEntry(3, 5), 0.001);
        assertEquals(2.0, tableau.getEntry(3, 2), 0.001);
        assertEquals(3.0, tableau.getEntry(2, 2), 0.001);
    }

    @Test
    public void testSimplexTableauNormalizeConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {-3.0, -2.0, 1.0}}), 5.0);
        Collection<LinearConstraint> originalConstraints = new ArrayList<>();
        originalConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0));
        originalConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, originalConstraints, GoalType.MAXIMIZE, false, 0.001);

        LinearConstraint constraint1 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0);
        LinearConstraint constraint2 = new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0);
        List<LinearConstraint> expectedNormalizedConstraints = new ArrayList<>();
        expectedNormalizedConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.GEQ, -2.0));
        expectedNormalizedConstraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.LEQ, 3.0));

        List<LinearConstraint> normalizedConstraints = tableau.normalizeConstraints(originalConstraints);

        assertEquals(expectedNormalizedConstraints.size(), normalizedConstraints.size());
        for (int i = 0; i < normalizedConstraints.size(); i++) {
            assertEquals(expectedNormalizedConstraints.get(i), normalizedConstraints.get(i));
        }
    }

    @Test
    public void testSimplexTableauGetInvertedCoefficientSum() {
        RealVector coefficients = new ArrayRealVector(new double[]{-1.0, 2.0, 3.0});
        double result = SimplexTableau.getInvertedCoefficientSum(coefficients.toArray());
        assertEquals(-4.0, result, 0.001);
    }

    @Test
    public void testSimplexTableauDropPhase1Objective() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {-3.0, -2.0, 1.0}}), 5.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        tableau.dropPhase1Objective();

        assertEquals(2, tableau.getNumObjectiveFunctions());
    }

    @Test
    public void testSimplexTableauIsOptimal() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {-3.0, -2.0, 1.0}}), 5.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        tableau.dropPhase1Objective();

        PointValuePair solution = tableau.getSolution();
        tableau.isOptimal();

        assertTrue(solution.getValue() > 0.0);
    }

    @Test
    public void testSimplexTableauGetSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 3.0}, {-3.0, -2.0, 1.0}}), 5.0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {-1.0, 1.0}}), Relationship.LEQ, 2.0));
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{-2.0, 1.0}, {3.0, -1.0}}), Relationship.GEQ, 3.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 0.001);
        tableau.dropPhase1Objective();

        PointValuePair solution = tableau.getSolution();
        double[] coefficients = solution.getPoint();

        assertEquals(3, coefficients.length);
        assertEquals(2.0 / 3.0, coefficients[0], 0.001);
        assertEquals(-1.0 / 3.0, coefficients[1], 0.001);
        assertEquals(-1.0 / 3.0, coefficients[2], 0.001);
        assertEquals(5.0, solution.getValue(), 0.001);
    }
}