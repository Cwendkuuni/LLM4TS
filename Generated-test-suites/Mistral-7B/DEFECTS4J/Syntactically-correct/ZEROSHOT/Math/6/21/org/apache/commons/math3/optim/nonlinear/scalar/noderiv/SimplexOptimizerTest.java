import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.optimizers.AbstractSimplex;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.SimpleOptimizationData;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimizer() {
        // Test with a simple function
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        AbstractSimplex simplex = new SimpleXOver();

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 1e-5));

        PointValuePair result = optimizer.optimize(new MaxEval(100), f, GoalType.MINIMIZE,
                new OptimizationData[]{
                        new SimpleOptimizationData(new InitialGuess(new double[]{2.0, 2.0})),
                        simplex
                });

        assertEquals(3.0, result.getValue(), 1e-5);
        assertTrue(result.getPoint()[0] < 2.0);
        assertTrue(result.getPoint()[1] < 2.0);
    }
}