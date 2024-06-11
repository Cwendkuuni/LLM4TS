package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction function;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        function = new FibonacciSearch(new SimpleGoalType(GoalType.MINIMIZE), new SimpleBounds(0.0, 10.0));
    }

    /**
     * Test optimization with default coefficients.
     */
    @Test
    public void testDefaultCoefficients() throws Exception {
        optimizer.optimize(function);
        PointValuePair best = optimizer.getBestSolution();
        assertEquals(0.0, best.getPoint()[0], 0.001);
        assertEquals(BigDecimal.ZERO, best.getValue());
    }

    /**
     * Test optimization with specified coefficients.
     */
    @Test
    public void testSpecifiedCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        optimizer.optimize(function);
        PointValuePair best = optimizer.getBestSolution();
        assertEquals(1.0, best.getPoint()[0], 0.001);
        assertEquals(BigDecimal.valueOf(1.38629436111982953), best.getValue());
    }

    /**
     * Test optimization with function that is not bracketing the solution.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x);
            }
        };
        optimizer.optimize(function);
    }

    /**
     * Test optimization with function that exceeds the maximum number of evaluations.
     */
    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (Math.abs(x) < 1.0) {
                    return Math.log(Math.abs(x));
                } else {
                    throw new RuntimeException();
                }
            }
        };
        optimizer.setMaxIterations(10);
        optimizer.optimize(function);
    }

    /**
     * Test optimization with function that does not converge.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testDivergingFunction() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.abs(x), 2.0) / Math.log(Math.abs(x) + 1.0);
            }
        };
        optimizer.optimize(function);
    }

    /**
     * Test optimization with unbounded domain.
     */
    @Test(expected = UnboundedDomainException.class)
    public void testUnboundedDomain() throws Exception {
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                if (x > 1.0) {
                    return Double.NEGATIVE_INFINITY;
                } else {
                    return Math.log(Math.abs(x));
                }
            }
        };
        optimizer.optimize(function);
    }

}