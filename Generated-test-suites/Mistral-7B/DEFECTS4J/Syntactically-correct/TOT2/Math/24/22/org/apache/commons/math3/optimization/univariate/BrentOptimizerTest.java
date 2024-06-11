import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

    @Test
    public void testEmptyConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithDefaults() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCheckers() {
        FunctionChecker functionChecker = new FunctionChecker();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, functionChecker);
        assertNotNull(optimizer);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testMinimizeSimpleFunction() {
        ObjectiveFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimplePoint(0), GoalType.MINIMIZE);
        assertEquals(0.5, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizeFunctionWithInitialValueCloseToTheMinimum() {
        ObjectiveFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimplePoint(0.3), GoalType.MINIMIZE);
        assertEquals(0.5, result.getPoint(), 1e-6);
    }

    @Test
    public void testMinimizeFunctionWithInitialValueFarFromTheMinimum() {
        ObjectiveFunction function = new SimpleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimplePoint(2), GoalType.MINIMIZE);
        assertEquals(0.5, result.getPoint(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithAbsolutThresholdLessThanZero() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-6, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithRelativThresholdLessThanZero() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithRelativThresholdLessThanMachineEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-9, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinimizeFunctionWithAbsolutThresholdLessThanMachineEpsilon() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-9);
    }

    @Test
    public void testMinimizeFunctionWithLimitCycle() {
        ObjectiveFunction function = new LimitCycleFunction();
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        PointValuePair result = optimizer.optimize(function, new SimplePoint(0), GoalType.MINIMIZE);
        assertEquals(0, result.getPoint(), 1e-6);
    }
}

class FunctionChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        return iteration > 100;
    }
}

class SimpleFunction implements ObjectiveFunction {
    @Override
    public double value(double x) {
        return x * x - 1;
    }
}

class SimplePoint implements PointValuePair {
    private double point;

    public SimplePoint(double point) {
        this.point = point;
    }

    @Override
    public double getPoint() {
        return point;
    }

    @Override
    public double getValue() {
        return value(point);
    }
}

class LimitCycleFunction implements ObjectiveFunction {
    private int count = 0;

    @Override
    public double value(double x) {
        if (count % 2 == 0) {
            count++;
            return x * x;
        } else {
            count++;
            return -x * x;
        }
    }
}