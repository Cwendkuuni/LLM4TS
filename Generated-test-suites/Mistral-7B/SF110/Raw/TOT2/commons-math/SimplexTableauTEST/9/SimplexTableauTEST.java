import org.apache.commons.math3.optimization.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, 5.0);
        List<LinearConstraint> constraints = new ArrayList<>();
        LinearConstraint constraint1 = new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 1.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{2.0, 2.0}, Relationship.GEQ, 3.0);
        constraints.add(constraint1);
        constraints.add(constraint2);

        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        assertEquals(objectiveFunction, tableau.getF());
        assertEquals(constraints, tableau.getConstraints());
        assertTrue(tableau.isRestrictToNonNegative());
        assertEquals(1.0E-6, tableau.getEpsilon(), 1e-8);
        assertEquals(10, tableau.getMaxUlps());
    }
}