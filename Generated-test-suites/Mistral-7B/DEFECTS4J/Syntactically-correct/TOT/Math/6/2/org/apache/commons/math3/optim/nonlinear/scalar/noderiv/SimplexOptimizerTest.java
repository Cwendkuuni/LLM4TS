package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
    private AbstractSimplex simplex;
    private MultivariateFunction function;
    private SimplexOptimizer optimizer;
    private PointValuePair expectedResult;

    @Before
    public void setUp() {
        // Create a simplex with 3 dimensions
        simplex = new AmoebaSimplex(3);
        // Create a multivariate function for testing
        function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i] - 2.0, 2.0);
                }
                return sum;
            }
        };
        optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-6, 1e-6));
    }

    @Test
    public void testSimpleOptimization() {
        // Set the simplex for the optimizer
        optimizer.setSimplex(simplex);
        // Optimize the function
        PointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, null);
        // Check the result is correct
        expectedResult = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 3.0);
        assertEquals("Incorrect optimization result", expectedResult, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        optimizer.optimize(100, null, GoalType.MINIMIZE, null);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testFunctionWithUnboundedDomain() {
        MultivariateFunction functionWithUnboundedDomain = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                if (x[0] > 5.0) {
                    throw new UnboundedDomainException();
                }
                return Math.pow(x[0], 2.0);
            }
        };
        optimizer.optimize(100, functionWithUnboundedDomain, GoalType.MINIMIZE, null);
    }
}