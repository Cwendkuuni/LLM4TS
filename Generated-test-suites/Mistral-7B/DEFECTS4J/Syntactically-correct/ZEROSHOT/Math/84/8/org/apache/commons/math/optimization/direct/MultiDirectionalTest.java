import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.direct.DirectSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.function.Functions;
import org.apache.commons.math3.optim.nonlinear.math.VectorUtilities;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        // Test with default constructor
        MultiDirectional optimizer = new MultiDirectional();
        double[] x = {-100.0, -100.0};
        SimpleBounds bounds = new SimpleBounds(x, x);
        double[] result = optimizer.optimize(Functions.SIN, bounds, 100, null);
        double expected = 0.0;
        Assert.assertArrayEquals(expected, result, 0.0001);

        // Test with custom constructor
        MultiDirectional optimizerCustom = new MultiDirectional(2.5, 0.6);
        double[] xCustom = {0.0, 0.0};
        SimpleBounds boundsCustom = new SimpleBounds(xCustom, xCustom);
        double[] function = {1.0, 1.0};
        PointValuePair pvp = new PointValuePair(VectorUtilities.add(xCustom, function), 1.0);
        simpleAdd(optimizerCustom.simplex, pvp);
        simpleSort(optimizerCustom.simplex, new PointValuePairComparator());
        double[] resultCustom = optimizerCustom.optimize(Functions.ROSENBROCK, boundsCustom, 100, null);
        double expectedCustom = new double[]{2.1369332361241323E-16, 1.0E-4};
        Assert.assertArrayEquals(expectedCustom, resultCustom, 0.001);

        // Test with FunctionEvaluationException
        MultiDirectional optimizerThrow = new MultiDirectional();
        double[] xThrow = {0.0, 0.0};
        SimpleBounds boundsThrow = new SimpleBounds(xThrow, xThrow);
        FunctionEvaluationException fex = new FunctionEvaluationException("Function evaluation error");
        OptimizationException oex = new OptimizationException("Optimization error");
        try {
            optimizerThrow.optimize(Functions.THROW_FUNCTION, boundsThrow, 100, fex);
        } catch (OptimizationException e) {
            Assert.assertTrue(e instanceof OptimizationException);
            Assert.assertEquals("Function evaluation error", e.getMessage());
        }

        // Test with OptimizationException
        MultiDirectional optimizerThrown = new MultiDirectional();
        double[] xThrown = {0.0, 0.0};
        SimpleBounds boundsThrown = new SimpleBounds(xThrow, xThrow);
        OptimizationException oexThrow = new OptimizationException("Optimization error");
        try {
            optimizerThrown.optimize(Functions.ROSENBROCK, boundsThrown, 100, oex);
        } catch (FunctionEvaluationException e) {
            Assert.assertTrue(e instanceof FunctionEvaluationException);
            Assert.assertEquals("Function evaluation error", e.getMessage());
        }

        // Test with IllegalArgumentException
        MultiDirectional optimizerIllegal = new MultiDirectional();
        double[] xIllegal = null;
        SimpleBounds boundsIllegal = new SimpleBounds(xIllegal, xIllegal);
        IllegalArgumentException iae = new IllegalArgumentException("Bounds cannot be null");
        try {
            optimizerIllegal.optimize(Functions.ROSENBROCK, boundsIllegal, 100, null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Bounds cannot be null", e.getMessage());
        }
    }

    private void simpleAdd(RealPointValuePair[] simplex, PointValuePair p) {
        for (int i = 0; i < simplex.length; i++) {
            if (simplex[i] == null) {
                simplex[i] = new RealPointValuePair(p.getPoint(), p.getFunctionValue(), false);
                break;
            }
        }
    }

    private void simpleSort(RealPointValuePair[] simplex, Comparator<RealPointValuePair> comparator) {
        Arrays.sort(simplex, comparator);
    }
}