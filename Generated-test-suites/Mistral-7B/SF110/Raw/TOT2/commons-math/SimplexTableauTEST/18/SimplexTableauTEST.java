import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, true, 1.0E-6, 10);
        // Basic functionality check
        assertEquals("Expected 3 decision variables and 2 slack variables", 5, tableau.getNumDecisionVariables());
        assertEquals("Expected 1 constraint and 0 artificial variables", 1, tableau.getNumSlackVariables());
        assertEquals("Expected 0 artificial variables", 0, tableau.getNumArtificialVariables());
        // Edge case: Empty constraint list
        SimplexTableau tableau2 = new SimplexTableau(f, Collections.emptyList(), GoalType.MINIMIZE, false, 1.0E-6, 10);
        assertEquals("Expected 0 constraints", 0, tableau2.getNumSlackVariables());
        // Error scenario: Non-linear objective function
        LinearObjectiveFunction fErr = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, Math.pow(x, 2.0)}}), 5.0);
        try {
            new SimplexTableau(fErr, constraints, GoalType.MAXIMIZE, true, 1.0E-6, 10);
            fail("Expected IllegalArgumentException due to non-linear objective function");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}