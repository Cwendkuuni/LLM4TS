import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert.*;
import org.junit.Test;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction function = new QuadraticObjectiveFunction(new Quadric(-1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 1.0));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(function, constraints, GoalType.MAXIMIZE, false, 1e-9, 10);
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(6, tableau.getNumDecisionVariables());
        assertEquals(3, tableau.getNumSlackVariables());
        assertEquals(0, tableau.getNumArtificialVariables());
        PointValuePair solution = tableau.getSolution();
        assertArrayEquals(new double[]{-0.6, 0.4}, solution.getPoint(), 0.001);
        assertEquals(4.26, solution.getValue(), 0.001);
    }
}