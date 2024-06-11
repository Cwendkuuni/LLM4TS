import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) * Math.sin(x) + Math.cos(x) * Math.cos(x) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * (Math.cos(x) * Math.cos(x) - Math.sin(x) * Math.sin(x));
            }
        };
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithIllegalArgument() {
        new BracketingNthOrderBrentSolver(1, 3);
    }

    @Test
    public void testSolveSimple() {
        double root = solver.solve(10, function, -3, 3);
        Assert.assertEquals(1.5707963267948966, root, 1e-6);
    }

    @Test
    public void testSolveWithStartValue() {
        double root = solver.solve(10, function, -3, 3, 0.5);
        Assert.assertEquals(1.5707963267948966, root, 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithUnbracketingInterval() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(10, function, 2, 1);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolveWithMaxEval() throws MaxCountExceededException {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        solver.solve(2, function, -3, 3);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, function, -3, 3);
        Assert.assertEquals(2.5850981063250615, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolutionAndStartValue() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(10, function, -3, 3, 0.5);
        Assert.assertEquals(2.5850981063250615, root, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithIllegalAllowedSolution() {
        solver = new BracketingNthOrderBrentSolver(1e-6, 5, 12);
        solver.solve(10, function, -3, 3);
    }

    @Test
    public void testGetMaximalOrder() {
        Assert.assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithDifferentPrecisions() {
        double absAccuracy = 1e-5;
        double relAccuracy = 1e-4;
        solver = new BracketingNthOrderBrentSolver(relAccuracy, absAccuracy, 5);
        double root = solver.solve(10, function, -3, 3);
        Assert.assertEquals(1.5707963267948966, root, absAccuracy);
    }

    @Test
    public void testSolveWithDifferentFunctionValueAccuracy() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        double functionValueAccuracy = 1e-5;
        solver = new BracketingNthOrderBrentSolver(relAccuracy, absAccuracy, functionValueAccuracy, 5);
        double root = solver.solve(10, function, -3, 3);
        Assert.assertEquals(1.5707963267948966, root, absAccuracy);
    }

    @Test
    public void testGetFunctionValueAccuracy() {
        Assert.assertEquals(0, Precision.safeEqual(solver.getFunctionValueAccuracy(), 0.0));
        double functionValueAccuracy = 1e-5;
        solver = new BracketingNthOrderBrentSolver(1e-4, 1e-6, functionValueAccuracy, 5);
        Assert.assertEquals(functionValueAccuracy, solver.getFunctionValueAccuracy(), 1e-6);
    }

    @Test
    public void testGetRelativeAccuracy() {
        Assert.assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-6);
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        Assert.assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-6);
    }

    @Test
    public void testToString() {
        Assert.assertTrue(FunctionUtils.checkToString(solver));
    }

}