import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testConstruction() {
        LinearObjectiveFunction objFunc = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}}), 3.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {2.0, 1.0}}), Relationship.LEQ, 1.0));
        SimplexTableau tableau = new SimplexTableau(objFunc, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);
        // Check tableau dimensions and other properties
        assertEquals(11, tableau.getHeight());
        assertEquals(11, tableau.getWidth());
        // Check optimization result
        PointValuePair solution = tableau.getSolution();
        assertEquals(new double[]{1.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(5.0, solution.getValue(), 1e-6);
    }

    // Add test cases for edge cases and exception handling
}