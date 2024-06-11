import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new QuadraticForm(new double[][]{{1.0, 2.0}, {2.0, 3.0}}, 4.0));
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), Relationship.LEQ, 2.0));
        SimplexTableau tableau = new SimplexTableau(func, constraints, GoalType.MINIMIZE, false, 1e-5);
        assertNotNull(tableau);
    }
}