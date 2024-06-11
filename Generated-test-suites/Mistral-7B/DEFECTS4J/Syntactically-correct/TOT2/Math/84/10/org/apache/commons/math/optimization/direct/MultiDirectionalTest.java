
package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.simple.*;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizer() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer, new MyFunction());
        test.testOptimization();
        assertTrue(test.isSolutionFound());
    }

    @Test
    public void testMultiDirectionalOptimizerWithIllegalArgument() {
        MultiDirectional optimizer = new MultiDirectional(1.0, -0.5); // Contraction coefficient < 0
        assertThrows(IllegalArgumentException.class, () -> new MultiDirectionalTest().new MultiDirectionalTestInnerClass(optimizer));
    }

    @Test
    public void testMultiDirectionalOptimizerWithFunctionEvaluationException() {
        MultiDirectional optimizer = new MultiDirectional();
        MyFunctionWithException function = new MyFunctionWithException();
        SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer, function);
        test.testOptimization();
        assertThrows(FunctionEvaluationException.class, test::isSolutionFound);
    }

    @Test
    public void testMultiDirectionalOptimizerWithSimpleBoundingMethod() throws Exception {
        double min = -10.0;
        double max = 10.0;

        MultiDirectional optimizer = new MultiDirectional();
        SimpleBoundingMethod boundary = new SimpleBoundingMethod(new ObjectiveFunction(new MyFunction()), min, max,
            null, GoalType.MINIMIZE, new SimpleStoppingCondition(100), optimizer);
        boundary.optimize();
        assertEquals(-3.0, boundary.getMinimum(), 0.01);
    }

    static class MyFunction implements UnivariateFunction {
        @Override
        public double value(double x) {
            return x * x - 3 * x + 2;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 3;
        }
    }

    static class MyFunctionWithException implements UnivariateFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x < 0) {
                throw new FunctionEvaluationException("Function evaluation failed");
            }
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    static class MultiDirectionalTestInnerClass {
        MultiDirectionalTestInnerClass(MultiDirectional optimizer) {
            SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer, new MyFunctionWithConstraints());
            test.testOptimization();
            assertTrue(test.isSolutionFound());
        }
    }

    static class MyFunctionWithConstraints implements UnivariateFunction {
        @Override
        public double value(double x) {
            if (x < 0) {
                throw new IllegalArgumentException("x must be non-negative");
            }
            return x * x;
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

}
