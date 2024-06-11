import org.junit.Test;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.linear.*;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new RealPolynomialFunction(new QuadraticRealFunction(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, new double[]{1.0, 2.0})));
        List<LinearConstraint> constraints = Arrays.asList(new LinearConstraint(new RealVector(new double[]{3.0, 4.0}), Relationship.LEQ, 10.0), new LinearConstraint(new RealVector(new double[]{-1.0, 1.0}), Relationship.GEQ, 5.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1e-6, 10);

        assertEquals(GoalType.MAXIMIZE, tableau.getGoalType());
        assertTrue(tableau.isRestrictToNonNegative());
        assertEquals(1e-6, tableau.getEpsilon());
        assertEquals(10, tableau.getMaxUlps());
        assertEquals(f, tableau.getObjectiveFunction());
        assertEquals(2, tableau.getNumObjectiveFunctions());
        assertEquals(5, tableau.getNumDecisionVariables());
        assertEquals(2, tableau.getNumSlackVariables());
        assertEquals(1, tableau.getNumArtificialVariables());
    }
}