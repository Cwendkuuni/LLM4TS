import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.initializer.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSimple;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    private MultivariateFunction function;

    @Before
    public void setup() {
        optimizer = new SimplexOptimizer(0.001d, 1.0e-6d);
        function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }
        };
    }

    @Test
    public void testOptimizeSimpleFunction() {
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {2.0, 2.0};
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Comparator<PointValuePair> getComparator() {
                return new Comparator<PointValuePair>() {
                    @Override
                    public int compare(PointValuePair o1, PointValuePair o2) {
                        return Double.compare(o1.getValue(), o2.getValue());
                    }
                };
            }

            @Override
            public AbstractSimplex getAbstractSimplex() {
                return new SimpleSimplex(3);
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new DistanceInitializer(function, startPoint);
            }
        };

        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), 1e-6);
        Assert.assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 1e-6);
    }

    @Test
    public void testOptimizeFunctionWithConvergenceChecker() {
        GoalType goalType = GoalType.MINIMIZE;
        double[] startPoint = {2.0, 2.0};
        double relativeTolerance = 1e-6;
        double absoluteTolerance = 1.0e-8;
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(relativeTolerance, absoluteTolerance);
        PointValuePair expectedResult = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        OptimizationData optimizationData = new OptimizationData() {
            @Override
            public Comparator<PointValuePair> getComparator() {
                return new Comparator<PointValuePair>() {
                    @Override
                    public int compare(PointValuePair o1, PointValuePair o2) {
                        return Double.compare(o1.getValue(), o2.getValue());
                    }
                };
            }

            @Override
            public AbstractSimplex getAbstractSimplex() {
                return new SimpleSimplex(3);
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new DistanceInitializer(function, startPoint);
            }

            @Override
            public ConvergenceChecker<PointValuePair> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        PointValuePair result = optimizer.optimize(100, function, goalType, optimizationData);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), relativeTolerance);
        Assert.assertArrayEquals(expectedResult.getPoint(), result.getPoint(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(100, null, GoalType.MINIMIZE, new Object[]{});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullOptimizationData() {
        optimizer.optimize(100, function, null, new Object[]{});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullGoalType() {
        optimizer.optimize(100, function, null, new Object[]{});
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2.0, 2.0, 100, GoalTypeSimple.MINIMIZE},
                {3.0, 3.0, 100, GoalTypeSimple.MAXIMIZE},
                {1.5, 1.5, 100, GoalTypeSimple.MINIMIZE}
        });
    }

    @Test
    public void testOptimizeWithDifferentInitialPoints(double[] startPoint1, int maxEval, GoalType goalType) {
        PointValuePair expectedResult1 = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        OptimizationData optimizationData1 = new OptimizationData() {
            @Override
            public Comparator<PointValuePair> getComparator() {
                return new Comparator<PointValuePair>() {
                    @Override
                    public int compare(PointValuePair o1, PointValuePair o2) {
                        return Double.compare(o1.getValue(), o2.getValue());
                    }
                };
            }

            @Override
            public AbstractSimplex getAbstractSimplex() {
                return new SimpleSimplex(3);
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new DistanceInitializer(function, startPoint1);
            }
        };

        PointValuePair result1 = optimizer.optimize(maxEval, function, goalType, optimizationData1);
        Assert.assertEquals(expectedResult1.getValue(), result1.getValue(), 1e-6);
        Assert.assertArrayEquals(expectedResult1.getPoint(), result1.getPoint(), 1e-6);

        PointValuePair expectedResult2 = new PointValuePair(new double[]{2.5, 2.5}, 3.0);
        OptimizationData optimizationData2 = new OptimizationData() {
            @Override
            public Comparator<PointValuePair> getComparator() {
                return new Comparator<PointValuePair>() {
                    @Override
                    public int compare(PointValuePair o1, PointValuePair o2) {
                        return Double.compare(o1.getValue(), o2.getValue());
                    }
                };
            }

            @Override
            public AbstractSimplex getAbstractSimplex() {
                return new SimpleSimplex(3);
            }

            @Override
            public InitialGuess getInitialGuess() {
                return new DistanceInitializer(function, new double[]{2.5, 2.5});
            }
        };

        PointValuePair result2 = optimizer.optimize(maxEval, function, goalType, optimizationData2);
        Assert.assertEquals(expectedResult2.getValue(), result2.getValue(), 1e-6);
        Assert.assertArrayEquals(expectedResult2.getPoint(), result2.getPoint(), 1e-6);
    }

    @After
    public void cleanup() {
        optimizer = null;
        function = null;
    }
}

class SimpleSimplex extends AbstractSimplex {
    public SimpleSimplex(int size) {
        super(size);
    }

    @Override
    public void build(double[] p) {
        // Simple implementation of the simplex method
        // This is not an optimized version, it is only meant for testing
        PointValuePair[] points = new PointValuePair[size + 1];
        points[0] = new PointValuePair(p, function.value(p));
        for (int i = 1; i <= size; i++) {
            double[] pNew = new double[size];
            for (int j = 0; j < size; j++) {
                pNew[j] = points[i - 1].getPoint()[j] + (points[i - 1].getPoint()[j] - points[i - 2].getPoint()[j]) / (size + 1);
            }
            points[i] = new PointValuePair(pNew, function.value(pNew));
        }
        setPoints(points);
    }

    @Override
    public void evaluate(MultivariateFunction objectiveFunction, Comparator<PointValuePair> comparator) {
        // No implementation needed
    }

    @Override
    public PointValuePair getPoint(int index) {
        return getPoints()[index];
    }
}