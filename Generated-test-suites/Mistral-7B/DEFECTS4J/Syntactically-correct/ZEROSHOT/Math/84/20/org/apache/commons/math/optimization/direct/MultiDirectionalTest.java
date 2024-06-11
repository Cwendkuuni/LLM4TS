import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.function.Add;
import org.apache.commons.math3.optim.function.Constants;
import org.apache.commons.math3.optim.function.FunctionUtils;
import org.apache.commons.math3.optim.function.LinearFunction;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() {
        SimpleOptimizationTest test = new SimpleOptimizationTest();

        // Test with default coefficients
        MultiDirectional optimizer = new MultiDirectional();
        test.testOptimization(optimizer, new LinearFunction(new double[]{-1.0, 1.0}, 2.0),
                new PointValuePairList(new PointValuePair[]{
                        new RealPointValuePair(new double[]{-1.0, 1.0}, 2.0),
                        new RealPointValuePair(new double[]{-0.5, 1.5}, 1.5),
                        new RealPointValuePair(new double[]{-0.5, 0.5}, 0.5),
                        new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                        new RealPointValuePair(new double[]{0.5, 0.5}, 0.5),
                        new RealPointValuePair(new double[]{0.5, 1.5}, 1.5),
                        new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
                }), 50, 1e-6, 1e-6);
        test.testMaxEvaluations(optimizer, new LinearFunction(new double[]{-1.0, 1.0}, 2.0));

        // Test with custom coefficients
        MultiDirectional optimizerCustom = new MultiDirectional(3.0, 0.8);
        test.testOptimization(optimizerCustom, new LinearFunction(new double[]{-1.0, 1.0}, 2.0),
                new PointValuePairList(new PointValuePair[]{
                        new RealPointValuePair(new double[]{-1.0, 1.0}, 2.0),
                        new RealPointValuePair(new double[]{-0.5, 1.5}, 1.5),
                        new RealPointValuePair(new double[]{-0.5, 0.5}, 0.5),
                        new RealPointValuePair(new double[]{0.0, 0.0}, 0.0),
                        new RealPointValuePair(new double[]{0.5, 0.5}, 0.5),
                        new RealPointValuePair(new double[]{0.5, 1.5}, 1.5),
                        new RealPointValuePair(new double[]{1.0, 1.0}, 2.0)
                }), 50, 1e-6, 1e-6);
        test.testMaxEvaluations(optimizerCustom, new LinearFunction(new double[]{-1.0, 1.0}, 2.0));

        // Test with a custom function
        MultiDirectional optimizerCustomFunction = new MultiDirectional();
        FunctionUtils.checkedAdd = (f, g) -> new Add(f, g);
        FunctionUtils.checkedMult = (a, b) -> a * b;
        optimizerCustomFunction.setMaxIterations(100);
        optimizerCustomFunction.setConvergenceChecker(new RealConvergenceChecker(1e-7));

        double[] x = new double[]{1.0, 1.0};
        double fx = 1.0;
        LinearFunction f = new LinearFunction(x, FunctionUtils.checkedMult(Constants.ONE,
                FunctionUtils.checkedSub(FunctionUtils.checkedAdd(x, new double[]{1.0, -1.0}),
                        new double[]{-1.0, 1.0})));
        test.testOptimization(optimizerCustomFunction, f,
                new PointValuePairList(new PointValuePair[]{
                        new RealPointValuePair(x, fx),
                        new RealPointValuePair(new double[]{1.1, 0.9}, 0.1),
                        new RealPointValuePair(new double[]{1.2, 0.8}, 0.04)
                }), 50, 1e-6, 1e-6);
        test.testMaxEvaluations(optimizerCustomFunction, f);

        // Test with illegal arguments
        try {
            new MultiDirectional(0.0, 0.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new MultiDirectional(-1.0, 1.0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizerCustom.setMaxIterations(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizerCustom.setConvergenceChecker(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[]{
                new RealPointValuePair(new double[]{0.1, 0.1}, 0.1),
                new RealPointValuePair(new double[]{0.2, 0.2}, 0.2),
                new RealPointValuePair(new double[]{0.3, 0.3}, 0.3),
                new RealPointValuePair(new double[]{0.4, 0.4}, 0.4)
        };

        // Reflection
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);
        assertNotNull(reflected);
        assertEquals(original[1].getPointRef(), reflected.getPointRef(), 0.0);

        // Expansion
        RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 2.0, null);
        assertNotNull(expanded);
        assertFalse(FunctionUtils.isEqual(expanded.getFvalue(), original[0].getFvalue(), 0.001));

        // Contraction
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, null);
        assertNotNull(contracted);
        assertFalse(FunctionUtils.isEqual(contracted.getFvalue(), original[0].getFvalue(), 0.001));

        // Bad reflection coefficient
        try {
            optimizer.evaluateNewSimplex(original, -1.0, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Bad contraction coefficient
        try {
            optimizer.evaluateNewSimplex(original, 2.1, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}