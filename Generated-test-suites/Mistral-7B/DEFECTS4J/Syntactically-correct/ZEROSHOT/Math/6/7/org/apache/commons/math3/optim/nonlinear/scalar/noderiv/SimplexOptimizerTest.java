import org.apache.commons.math3.analysis.FunctionUtilities;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.test.MoreAsserts;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.ArrayVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorValuePair;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.initializer.ArrayRealVectorInitialGuess;
import org.apache.commons.math3.optimization.initializer.RealVectorInitialGuess;
import org.apache.commons.math3.optimization.simplextree.NelderMeadSimplex;
import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleMinimization() {
        MultivariateFunction function = new VectorFunction(2) {
            public double value(double[] x) {
                return FunctionUtilities.pow(2, x[0]) + FunctionUtilities.pow(2, x[1]);
            }
        };

        RealVectorInitialGuess initialGuess = new ArrayRealVectorInitialGuess(new double[]{1.0, 1.0});

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.001, 0.0));
        optimizer.setSimplex(new NelderMeadSimplex(2));

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{initialGuess});
        MoreAsserts.assertAreClose(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        Assert.assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = UnconstrainedOptimizationException.class)
    public void testSimpleMinimizationFailed() {
        MultivariateFunction function = new VectorFunction(2) {
            public double value(double[] x) {
                return FunctionUtilities.pow(2, x[0]) - FunctionUtilities.pow(2, x[1]);
            }
        };

        RealVectorInitialGuess initialGuess = new ArrayRealVectorInitialGuess(new double[]{1.0, 1.0});

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.001, 0.0));
        optimizer.setSimplex(new NelderMeadSimplex(2));

        optimizer.optimize(100, function, GoalType.MINIMIZE, new OptimizationData[]{initialGuess});
    }

    @Test
    public void testSimpleMaximization() {
        MultivariateFunction function = new VectorFunction(2) {
            public double value(double[] x) {
                return FunctionUtilities.pow(2, x[0]) + FunctionUtilities.pow(2, x[1]);
            }
        };

        RealVectorInitialGuess initialGuess = new ArrayRealVectorInitialGuess(new double[]{1.0, 1.0});

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(0.001, 0.0));
        optimizer.setSimplex(new NelderMeadSimplex(2));

        PointValuePair result = optimizer.optimize(100, function, GoalType.MAXIMIZE, new OptimizationData[]{initialGuess});
        MoreAsserts.assertAreClose(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result.getValue(), 0.001);
    }

}