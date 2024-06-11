import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testDefaultConstruction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        assertNull(optimizer.getSimplex());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1, null, GoalType.MINIMIZE, new double[3]);
    }

    @Test
    public void testSimpleConstruction() {
        AbstractSimplex simplex = new SimpleX();
        SimplexOptimizer optimizer = new SimplexOptimizer(simplext);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testMinimization() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return 2 * x[0] * x[0] + x[1] * x[1];
            }
        };
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleX());

        PointValuePair result = optimizer.optimize(10, function, GoalType.MINIMIZE, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertEquals(0.5, result.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0 / 4.0, 1.0 / 2.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testMaximization() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return 2 * x[0] * x[0] + x[1] * x[1];
            }
        };
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer(GoalType.MAXIMIZE, new SimpleX());

        PointValuePair result = optimizer.optimize(10, function, GoalType.MAXIMIZE, startPoint);
        assertEquals(GoalType.MAXIMIZE, optimizer.getGoalType());
        assertEquals(3.5, result.getValue(), 0.001);
        assertArrayEquals(new double[]{3.0 / 4.0, 1.5}, result.getPoint(), 0.001);
    }

    @Test
    public void testConvergenceChecker() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.E, -Math.pow(x[0], 2) - Math.pow(x[1], 2));
            }
        };
        double[] startPoint = {1.0, 1.0};
        double relativeTolerance = 1e-8;
        double absoluteTolerance = 1e-8;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(relativeTolerance, absoluteTolerance);
        SimplexOptimizer optimizer = new SimplexOptimizer(convergenceChecker, new SimpleX());

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testCustomSimplex() {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.E, -Math.pow(x[0], 2) - Math.pow(x[1], 2));
            }
        };
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new MySimpleX());

        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    private static class MySimpleX extends AbstractSimplex {
        @Override
        public int getSize() {
            return 2;
        }

        @Override
        public void build(double[] p) {
            super.build(p);
            this.points[0] = new Point(p[0], p[1]);
            this.points[1] = new Point(p[1], p[0]);
        }
    }

}