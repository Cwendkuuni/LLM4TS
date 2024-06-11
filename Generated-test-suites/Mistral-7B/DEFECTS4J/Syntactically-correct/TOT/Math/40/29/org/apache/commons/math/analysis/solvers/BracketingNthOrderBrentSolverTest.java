package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class BracketingNthOrderBrentSolverTest {

    private static final double RELATIVE_ACCURACY = 1e-3;
    private static final double ABSOLUTE_ACCURACY = 1e-5;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-6;
    private static final int MAXIMAL_ORDER = 5;

    private BracketingNthOrderBrentSolver solver;

    @Parameterized.Parameters(name = "{index}: function={0} with interval={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SquareFunction(), new double[]{0.5, 1.5, 2.5}},
                {new SinFunction(), new double[]{-1, 0, 1}},
                {new CosFunction(), new double[]{-3, -1, 1}},
                {new ExpFunction(), new double[]{0.5, 1.0, 2.0}},
                {new LogFunction(), new double[]{2.0, 3.0, 4.0}},
                {new CubicFunction(), new double[]{-1, 0, 1}},
                {new QuarticFunction(), new double[]{0.5, 1.5, 2.5}}
        });
    }

    @Parameterized.Constructor
    public BracketingNthOrderBrentSolverTest(UnivariateFunction function, double[] interval) {
        this.solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
    }

    @Before
    public void setUp() {
        solver.solve(100, function, interval[0], interval[1]);
    }

    @Test
    public void testSolutionIsRoot() {
        assertEquals(function.value(solver.getRoot()), 0.0, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testAllowedSolutionIsRespected() {
        if (function instanceof AllowedSolutionFunction) {
            AllowedSolutionFunction allowedFunction = (AllowedSolutionFunction) function;
            switch (allowedFunction.getAllowedSolution()) {
                case LEFT_SIDE:
                    assertEquals(solver.getRoot(), interval[0], ABSOLUTE_ACCURACY);
                    break;
                case RIGHT_SIDE:
                    assertEquals(solver.getRoot(), interval[1], ABSOLUTE_ACCURACY);
                    break;
                case BELOW_SIDE:
                    assertEquals(solver.getFunctionValue(), 0.0, FUNCTION_VALUE_ACCURACY);
                    assertEquals(solver.getRoot(), interval[0], ABSOLUTE_ACCURACY);
                    break;
                case ABOVE_SIDE:
                    assertEquals(solver.getFunctionValue(), 0.0, FUNCTION_VALUE_ACCURACY);
                    assertEquals(solver.getRoot(), interval[1], ABSOLUTE_ACCURACY);
                    break;
                default:
                    // any_side should be handled by the default logic in the solver
            }
        }
    }

    @Test
    public void testIterationCountIsWithinLimit() {
        assertEquals(solver.getIterationCount(), lessThanOrEqual(100));
    }

    @Test(expected = NoBracketingException.class)
    public void testThrowsNoBracketingException() {
        // ensure the given interval does not bracket the root
        UnivariateFunction nonBracketingFunction = new NonBracketingFunction();
        solver.solve(100, nonBracketingFunction, 0.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testThrowsNumberIsTooSmallException() {
        solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, 0);
        solver.solve(100, function, interval[0], interval[1]);
    }

    @Test(expected = MathInternalError.class)
    public void testThrowsMathInternalError() {
        solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER + 1);
        solver.solve(100, function, interval[0], interval[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testThrowsMaxCountExceededException() {
        solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        solver.setMaxIterations(0);
        solver.solve(100, function, interval[0], interval[1]);
    }

    private int lessThanOrEqual(int value) {
        return value;
    }

}

interface UnivariateFunction {
    double value(double x);
}

class SquareFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x;
    }
}

class SinFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.sin(x);
    }
}

class CosFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.cos(x);
    }
}

class ExpFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.exp(x);
    }
}

class LogFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.log(x);
    }
}

class CubicFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x * x - 2 * x + 1;
    }
}

class QuarticFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x * x * x - 6 * x * x + 8 * x - 3;
    }
}

class AllowedSolutionFunction implements UnivariateFunction {
    private final AllowedSolution allowedSolution;

    public AllowedSolutionFunction(AllowedSolution allowedSolution) {
        this.allowedSolution = allowedSolution;
    }

    public AllowedSolution getAllowedSolution() {
        return allowedSolution;
    }

    @Override
    public double value(double x) {
        if (allowedSolution == AllowedSolution.LEFT_SIDE) {
            return -Math.pow(x - 1, 2) + 1;
        } else if (allowedSolution == AllowedSolution.RIGHT_SIDE) {
            return Math.pow(x + 1, 2) - 1;
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

class NonBracketingFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.sqrt(Math.abs(x - 1));
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE,
    ANY_SIDE
}