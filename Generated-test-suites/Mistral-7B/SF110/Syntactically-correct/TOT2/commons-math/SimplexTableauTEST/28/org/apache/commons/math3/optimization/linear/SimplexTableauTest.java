import org.apache.commons.math3.optim.LinearObjectiveFunction;
import org.apache.commons.math3.optim.LinearConstraint;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {

    @Test
    public void testSimplexTableauConstructor1() {
        LinearObjectiveFunction f = new MaxEval(new LinearObjectiveFunction() {
            @Override
            public double value(RealVector x) {
                return x.dotProduct(new ArrayRealVector(new double[]{3.0, 4.0}));
            }

            @Override
            public RealVector gradient(RealVector x) {
                RealVector result = new ArrayRealVector(new double[]{6.0, 8.0});
                result.assign(x);
                return result;
            }
        });
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(new ArrayRealVector(new double[]{1.0, -1.0}), Relationship.GEQ, 0.0));
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0E-6, 10);
        // Add assertions here
    }
}