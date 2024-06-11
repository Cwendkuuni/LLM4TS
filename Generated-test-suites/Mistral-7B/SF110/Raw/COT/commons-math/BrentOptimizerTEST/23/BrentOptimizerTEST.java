import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goal.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMatcher;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer.ConvergenceChecker;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };

        try {
            new BrentOptimizer(-0.1, 0.0);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            new BrentOptimizer(0.0, 1.0);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        new BrentOptimizer(0.1, 0.01);
        new BrentOptimizer(0.1, 0.01, convergenceChecker);
    }

    @Test
    public void testDoOptimize() {
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return FastMath.sin(x) - x;
            }
        };

        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 0.0;
            }
        };

        GoalType goalType = new GoalTypeMatcher(GoalType.MINIMIZE);

        BrentOptimizer brentOptimizer = new BrentOptimizer(0.1, 0.01, null);
        PointValuePair optimizationResult = brentOptimizer.optimize(objectiveFunction, initialGuess, goalType);

        double expectedX = -1.413718854938224;
        double expectedValue = -1.413718750839739;

        Assert.assertEquals(expectedX, optimizationResult.getPoint(), 0.00001);
        Assert.assertEquals(expectedValue, optimizationResult.getValue(), 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullObjectiveFunction() {
        new BrentOptimizer(0.1, 0.01, null).optimize(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullInitialGuess() {
        new BrentOptimizer(0.1, 0.01, null).optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeWithNullGoalType() {
        new BrentOptimizer(0.1, 0.01, null).optimize(new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        }, new InitialGuess() {
            @Override
            public double getInitialGuess() {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testBest() {
        PointValuePair a = new PointValuePair(0.0, 0.0);
        PointValuePair b = new PointValuePair(1.0, 1.0);
        PointValuePair c = new PointValuePair(2.0, 2.0);

        boolean isMinim = true;
        PointValuePair expectedResult = (a == null) ? b : (b == null ? a : (a.getValue() <= b.getValue()) ? a : b);

        assertEquals(expectedResult, BrentOptimizer.this.best(a, b, isMinim));
    }

    @Test
    public void testBestWithNullArguments() {
        PointValuePair expectedResult = null;

        assertEquals(expectedResult, BrentOptimizer.this.best(null, null, true));
        assertEquals(expectedResult, BrentOptimizer.this.best(null, null, false));
        assertEquals(expectedResult, BrentOptimizer.this.best(null, new PointValuePair(0.0, 0.0), true));
        assertEquals(expectedResult, BrentOptimizer.this.best(new PointValuePair(0.0, 0.0), null, true));
        assertEquals(expectedResult, BrentOptimizer.this.best(new PointValuePair(0.0, 0.0), new PointValuePair(0.0, 0.0), false));
    }
}