import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunctionWithConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    /**
     * Test the MultiDirectional optimizer with a simple function.
     */
    @Test
    public void testSimpleFunction() {
        final MultiDirectional optimizer = new MultiDirectional();
        final ObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, 3.0);
        final SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, new double[]{2.0, 2.0});
        try {
            final PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
            assertEquals("Expected minimum to be at point (-2.0, 2.0) and value 3.0.",
                         new PointValuePair(-2.0, 2.0, 3.0), result, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the MultiDirectional optimizer with a function with local minimum and maximum.
     */
    @Test
    public void testFunctionWithLocalMinAndMax() {
        final MultiDirectional optimizer = new MultiDirectional();
        final ObjectiveFunction function = new FunctionWithLocalMinAndMax();
        final SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, new double[]{2.0, 2.0});
        try {
            final PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
            assertEquals("Expected minimum to be at point (-2.0, 2.0) and value -1.5.",
                         new PointValuePair(-2.0, 2.0, -1.5), result, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the MultiDirectional optimizer with a function with an infinite value.
     */
    @Test
    public void testFunctionWithInfiniteValue() {
        final MultiDirectional optimizer = new MultiDirectional();
        final ObjectiveFunction function = new FunctionWithInfiniteValue();
        final SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, new double[]{2.0, 2.0});
        try {
            optimizer.optimize(function, bounds, GoalType.MINIMIZE);
            fail("Expected an UnboundedDomainException.");
        } catch (UnboundedDomainException e) {
            // expected
        }
    }

    /**
     * Test the MultiDirectional optimizer with a function with a domain constraint.
     */
    @Test
    public void testFunctionWithDomainConstraint() {
        final MultiDirectional optimizer = new MultiDirectional();
        final ObjectiveFunction function = new FunctionWithDomainConstraint();
        final LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 0.0));
        final SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, new double[]{2.0, 2.0});
        try {
            final PointValuePair result = optimizer.optimize(new LinearObjectiveFunctionWithConstraints(function, constraints),
                                                             bounds, GoalType.MINIMIZE);
            assertEquals("Expected minimum to be at point (2.0, 2.0) and value -4.0.",
                         new PointValuePair(2.0, 2.0, -4.0), result, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the MultiDirectional optimizer with a function having a large number of variables.
     */
    @Test
    public void testFunctionWithLargeNumberOfVariables() {
        final MultiDirectional optimizer = new MultiDirectional();
        final ObjectiveFunction function = new FunctionWithLargeNumberOfVariables();
        final SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, new double[]{50.0, 50.0});
        try {
            final PointValuePair result = optimizer.optimize(function, bounds, GoalType.MINIMIZE);
            assertEquals("Expected minimum to be at point (0.0, 0.0) and value 0.0.",
                         new PointValuePair(0.0, 0.0, 0.0), result, 0.001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}

class FunctionWithLocalMinAndMax implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        if (x[0] > 1.0 || x[1] > 1.0) {
            return Double.POSITIVE_INFINITY;
        }
        if (x[0] < -1.0 || x[1] < -1.0) {
            return -Double.POSITIVE_INFINITY;
        }
        return Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0) - 1.5;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}

class FunctionWithInfiniteValue implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}

class FunctionWithDomainConstraint implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        if (x[0] < 0.0 || x[1] < 0.0) {
            throw new FunctionEvaluationException("The point is not in the domain.", null);
        }
        return Math.pow(x[0], 2.0) + Math.pow(x[1], 2.0) - 4.0;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}

class FunctionWithLargeNumberOfVariables implements ObjectiveFunction {

    @Override
    public double value(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; ++i) {
            sum += Math.pow(x[i], 2.0);
        }
        return sum / x.length;
    }

    @Override
    public int getNumberOfVariables() {
        return 50;
    }

}