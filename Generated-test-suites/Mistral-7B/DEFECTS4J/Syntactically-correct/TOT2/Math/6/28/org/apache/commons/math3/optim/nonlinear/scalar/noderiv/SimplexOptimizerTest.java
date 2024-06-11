import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testSimpleOptimization() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
            }
        };

        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        OptimizationData data = new OptimizationData(new InitialGuess() {
            public double[] getInitialPoint() {
                return new double[]{-1.0, -1.0};
            }
        }, new SimplexOptimizer.AbstractSimplex() {
            @Override
            public void build(double[] initialPoint) {
                // Default implementation is sufficient for this test.
            }
        });

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, data);
        assertEquals(new PointValuePair(-2.0, -5.0), result, 1e-6);
    }

    @Test
    public void testExceptionWhenNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        try {
            optimizer.optimize(100, null, GoalType.MINIMIZE, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testExceptionWhenNullData() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        try {
            optimizer.optimize(100, new MultivariateFunction() {
                public double value(double[] x) {
                    return 0.0;
                }
            }, GoalType.MINIMIZE, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testExceptionWhenNullInitialPoint() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        OptimizationData data = new OptimizationData();
        try {
            optimizer.optimize(100, new MultivariateFunction() {
                public double value(double[] x) {
                    return 0.0;
                }
            }, GoalType.MINIMIZE, data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExceptionWhenNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        OptimizationData data = new OptimizationData();
        data.setParameter(SimplexOptimizer.AbstractSimplex.class, null);
        try {
            optimizer.optimize(100, new MultivariateFunction() {
                public double value(double[] x) {
                    return 0.0;
                }
            }, GoalType.MINIMIZE, data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExceptionWhenNullGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        OptimizationData data = new OptimizationData();
        try {
            optimizer.optimize(100, new MultivariateFunction() {
                public double value(double[] x) {
                    return 0.0;
                }
            }, null, data);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExceptionWhenNegativeNumberOfEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
        try {
            optimizer.optimize(-1, new MultivariateFunction() {
                public double value(double[] x) {
                    return 0.0;
                }
            }, GoalType.MINIMIZE, new OptimizationData());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}