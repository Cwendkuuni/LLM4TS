import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.optim.nonlinear.functions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1.0E-6, 1.0E-6);
        function = new Pow2();
    }

    @Test
    public void testMinimize() {
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getLowerBound() {
                return -10.0;
            }

            @Override
            public double getUpperBound() {
                return 10.0;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(problem);
        assertEquals(-1.0, solution.getPoint(), 1.0E-6);
        assertEquals(0.0, solution.getValue(), 1.0E-6);
    }

    @Test
    public void testMinimizeWithInitialValue() {
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getLowerBound() {
                return -10.0;
            }

            @Override
            public double getUpperBound() {
                return 10.0;
            }

            @Override
            public ValueInitializer<Double> getInitialValue() {
                return () -> 5.0;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(problem);
        assertEquals(-1.0, solution.getPoint(), 1.0E-6);
        assertEquals(0.0, solution.getValue(), 1.0E-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testIllegalAbsoluteThreshold() {
        optimizer = new BrentOptimizer(-1.0, 1.0E-6, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIllegalRelativeThreshold() {
        optimizer = new BrentOptimizer(0.0, 1.0E-6, null);
    }

    @Test
    public void testMaximize() {
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return new Pow3();
            }

            @Override
            public double getLowerBound() {
                return -10.0;
            }

            @Override
            public double getUpperBound() {
                return 10.0;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };

        UnivariatePointValuePair solution = optimizer.optimize(problem);
        assertEquals(1.0, solution.getPoint(), 1.0E-6);
        assertEquals(8.0, solution.getValue(), 1.0E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaximizeWithMinimizeGoalType() {
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public UnivariateFunction getFunction() {
                return function;
            }

            @Override
            public double getLowerBound() {
                return -10.0;
            }

            @Override
            public double getUpperBound() {
                return 10.0;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };

        optimizer.optimize(problem);
    }
}