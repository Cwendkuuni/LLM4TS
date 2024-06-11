import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.initializer.SimpleXInitializer;
import org.apache.commons.math3.optim.point.PointValuePairFactory;
import org.apache.commons.math3.optim.simplex.SimpleXEvaluator;
import org.apache.commons.math3.optim.simplex.SimpleXSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.apache.commons.math3.optim.point.PointValuePairFactory.INSTANCE;

public class SimplexOptimizerTest {

    private MultivariateFunction testFunction;
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        testFunction = new RastriginFunction();
        optimizer = new SimplexOptimizer(100, 0.01);
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-6, 100));
    }

    @Test
    public void testNullInput() {
        Assert.assertThrows(NullArgumentException.class, () -> optimizer.optimize(1, null, GoalType.MINIMIZE, new double[0]));
    }

    @Test
    public void testEmptyInput() {
        Assert.assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(1, testFunction, GoalType.MINIMIZE, new double[0]));
    }

    @Test
    public void testMinimization() throws Exception {
        double[] startPoint = new double[]{3.1415, 2.71828};
        PointValuePair expectedResult = INSTANCE.wrap(new double[]{0.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new SimpleXInitializer(new double[2], 0.1);
            }

            @Override
            public boolean isParameterModified() {
                return false;
            }
        };
        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, optimizationData);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testMaximization() throws Exception {
        double[] startPoint = new double[]{-3.1415, -2.71828};
        PointValuePair expectedResult = INSTANCE.wrap(new double[]{0.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new SimpleXInitializer(new double[2], 0.1);
            }

            @Override
            public boolean isParameterModified() {
                return false;
            }
        };
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MAXIMIZE, optimizationData);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testConvergenceChecker() throws Exception {
        double[] startPoint = new double[]{3.1415, 2.71828};
        double rel = 1e-6;
        double abs = 100;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(rel, abs);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new SimpleXInitializer(new double[2], 0.1);
            }

            @Override
            public boolean isParameterModified() {
                return false;
            }
        };
        optimizer = new SimplexOptimizer(convergenceChecker);

        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, optimizationData);

        PointValuePair previous = result;
        optimizer.iterate();
        result = optimizer.getCurrentPoint();

        Assert.assertFalse(convergenceChecker.isConverged(0, previous, result));

        for (int i = 0; i < 100; i++) {
            optimizer.iterate();
            result = optimizer.getCurrentPoint();
            previous = result;
            Assert.assertTrue(convergenceChecker.isConverged(i + 1, previous, result));
        }
    }

    @Test
    public void testLargeNumberOfEvaluations() throws Exception {
        double[] startPoint = new double[]{3.1415, 2.71828};
        PointValuePair expectedResult = INSTANCE.wrap(new double[]{0.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new SimpleXInitializer(new double[2], 0.1);
            }

            @Override
            public boolean isParameterModified() {
                return false;
            }
        };

        int maxEval = 100000;

        PointValuePair result = optimizer.optimize(maxEval, testFunction, GoalType.MINIMIZE, optimizationData);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testRandomStartPoint() throws Exception {
        double[] startPoint = new double[]{Math.random(), Math.random()};
        PointValuePair expectedResult = INSTANCE.wrap(new double[]{0.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Object getParameter() {
                return new SimpleXInitializer(new double[2], 0.1);
            }

            @Override
            public boolean isParameterModified() {
                return false;
            }
        };
        PointValuePair result = optimizer.optimize(100, testFunction, GoalType.MINIMIZE, optimizationData);
        Assert.assertEquals(expectedResult, result);
    }

    private static class RastriginFunction implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (double coord : point) {
                sum += Math.pow(coord * Math.PI, 2.0) - Math.cos(2 * Math.PI * coord) + 10.0;
            }
            sum += 10.0 * point.length;
            return sum;
        }

        @Override
        public double[] gradient(double[] point) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getNumberOfVariables() {
            return point.length;
        }
    }
}

class AbstractSimplex {
    public void build(double[] point) {
        // Implementation not provided.
    }

    public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
        // Implementation not provided.
    }

    public int getSize() {
        // Implementation not provided.
        return 0;
    }

    public PointValuePair getPoint(int index) {
        // Implementation not provided.
        return null;
    }

    public PointValuePair[] getPoints() {
        // Implementation not provided.
        return new PointValuePair[0];
    }

    public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
        // Implementation not provided.
    }
}

class SimpleXSolver extends AbstractSimplex {
    @Override
    public void build(double[] point) {
        // Implementation not provided.
    }

    @Override
    public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
        // Implementation not provided.
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public PointValuePair getPoint(int index) {
        switch (index) {
            case 0:
                return INSTANCE.wrap(new double[]{1.0, 1.0, 1.0}, 1.0);
            case 1:
                return INSTANCE.wrap(new double[]{-1.0, 1.0, 1.0}, 1.1);
            case 2:
                return INSTANCE.wrap(new double[]{1.0, -1.0, 1.0}, 1.2);
            case 3:
                return INSTANCE.wrap(new double[]{-1.0, -1.0, 1.0}, 1.3);
            case 4:
                return INSTANCE.wrap(new double[]{1.0, 1.0, -1.0}, 1.4);
            case 5:
                return INSTANCE.wrap(new double[]{-1.0, 1.0, -1.0}, 1.5);
            case 6:
                return INSTANCE.wrap(new double[]{1.0, -1.0, -1.0}, 1.6);
            default:
                return INSTANCE.wrap(new double[]{0.0, 0.0, 0.0}, Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public PointValuePair[] getPoints() {
        return new PointValuePair[]{
            INSTANCE.wrap(new double[]{1.0, 1.0, 1.0}, 1.0),
            INSTANCE.wrap(new double[]{-1.0, 1.0, 1.0}, 1.1),
            INSTANCE.wrap(new double[]{1.0, -1.0, 1.0}, 1.2),
            INSTANCE.wrap(new double[]{-1.0, -1.0, 1.0}, 1.3),
            INSTANCE.wrap(new double[]{1.0, 1.0, -1.0}, 1.4),
            INSTANCE.wrap(new double[]{-1.0, 1.0, -1.0}, 1.5),
            INSTANCE.wrap(new double[]{1.0, -1.0, -1.0}, 1.6)
        };
    }

    @Override
    public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {
        // Implementation not provided.
    }
}

class SimpleXEvaluator implements MultivariateFunction {
    @Override
    public double value(double[] point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[] gradient(double[] point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOfVariables() {
        return 0;
    }

    public double evaluate(AbstractSimplex simplex) {
        SimpleXSolver simplexSolver = (SimpleXSolver) simplex;
        MultivariateFunction function = new SimpleXFunction();
        return function.value(simplexSolver.getPoint(0).getPoint());
    }
}

class SimpleXFunction implements MultivariateFunction {
    @Override
    public double value(double[] point) {
        return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0) - 1.0;
    }

    @Override
    public double[] gradient(double[] point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }
}