import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolve_Basic() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -2.0, 2.0);
        Assert.assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(1.0 / x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(0);
        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_FunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 0.0, 0.0);
    }

    @Test
    public void testSolve_MinMaxOutOfOrder() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI / 2.0 - x);
            }
        };
        BisectionSolver solver = new BisectionSolver();
        Assert.assertThrows(IllegalArgumentException.class, () -> solver.solve(f, 2.0, 0.0));
    }

    @Test
    public void testSolve_MinOrMaxTooClose() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        Assert.assertThrows(IllegalArgumentException.class, () -> solver.solve(f, 0.0, 0.0));
    }

    @Test
    public void testSolve_MinOrMaxTooFar() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1.0;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -10.0, 10.0);
        Assert.assertTrue(Math.abs(Precision.sub(result, 0.0), 1e-6) > 1.0);
    }
}