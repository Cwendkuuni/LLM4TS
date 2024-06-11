import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.simple.TestFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealPointValuePair[] simplex;
    private Comparator<RealPointValuePair> comparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getFunctionValue() - o2.getFunctionValue());
            }
        };
    }

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        // Test with default coefficients
        optimizer.optimize(TestFunctions.ROSENBROCK, new RealPointValuePair(new double[]{2.0, 2.0}));
        Assert.assertEquals(0, simplex[0].getFunctionValue(), 0.001);
        Assert.assertEquals(new double[]{0.0, 0.0}, simplex[0].getPointRef(), 0.001);

        // Test with custom coefficients
        optimizer = new MultiDirectional(3.0, 0.2);
        optimizer.optimize(TestFunctions.ROSENBROCK, new RealPointValuePair(new double[]{2.0, 2.0}));
        Assert.assertEquals(0, simplex[0].getFunctionValue(), 0.001);
        Assert.assertEquals(new double[]{0.0, 0.0}, simplex[0].getPointRef(), 0.001);

        // Test convergence
        optimizer = new MultiDirectional();
        optimizer.setMaxIterations(10);
        optimizer.optimize(TestFunctions.SINE, new RealPointValuePair(new double[]{0.5}));
        Assert.assertTrue(optimizer.isConverged());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsReachedException() throws Exception {
        optimizer.setMaxIterations(0);
        optimizer.optimize(TestFunctions.ROSENBROCK, new RealPointValuePair(new double[]{2.0, 2.0}));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        optimizer.optimize(new FunctionWithException(), new RealPointValuePair(new double[]{0.0}));
    }

    @Test
    public void testGetters() {
        Assert.assertEquals(2.0, optimizer.getKhi(), 0.01);
        Assert.assertEquals(0.5, optimizer.getGamma(), 0.01);
    }

    private static class FunctionWithException implements org.apache.commons.math3.optim.UnaryFunction {

        @Override
        public double value(double x) throws Exception {
            throw new Exception("Function evaluation exception");
        }

        @Override
        public RealPointValuePair gradient(double x) {
            return new RealPointValuePair(new double[]{Double.NaN}, Double.NaN);
        }

        @Override
        public RealPointValuePair gradient(double[] x) {
            return new RealPointValuePair(x, Double.NaN);
        }

        @Override
        public RealPointValuePair value(RealPointValuePair point) throws Exception {
            throw new Exception("Function evaluation exception");
        }

        @Override
        public RealPointValuePair value(RealPointValuePair point, double step) throws Exception {
            throw new Exception("Function evaluation exception");
        }
    }

}