import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstruction() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}), 5.0);
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}), Relationship.LEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, false, 1e-5);
        // Add checks for other instance variables here
        assertTrue(tableau.isOptimal());
    }
}