import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionMismatchException;
import org.apache.commons.math3.solver.BisectionSolver;
import org.apache.commons.math3.solver.UnivariateRealSolver;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() throws Exception {
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - x - 1;
            }
        };

        // Test for simple function
        UnivariateRealSolver solver = new BisectionSolver();
        double root = solver.solve(f1, -2.0, 2.0);
        Assert.assertEquals(2.0, root, 1e-6);

        // Test for function with multiple roots
        solver = new BisectionSolver();
        root = solver.solve(f2, -5.0, 5.0);
        Assert.assertEquals(-1.5707963267948966, root, 1e-5);

        // Test for function with negative roots
        solver = new BisectionSolver();
        root = solver.solve(f1, -4.0, 0.0);
        Assert.assertEquals(-2.0, root, 1e-6);

        // Test for function with roots very close to interval bounds
        solver = new BisectionSolver();
        root = solver.solve(f3, 0.5, 1.5);
        Assert.assertEquals(1.0, root, 1e-6);

        // Test for function with roots very close to each other
        solver = new BisectionSolver();
        root = solver.solve(f3, 0.9, 1.1);
        Assert.assertEquals(1.0, root, 1e-6);

        // Test for function with a root very close to maximal iteration count
        solver = new BisectionSolver();
        try {
            root = solver.solve(f3, 0.0, 1.5 * Math.E);
            Assert.fail();
        } catch (MaxIterationsExceededException e) {
            //expected
        }

        // Test for function with a root at the interval boundary
        solver = new BisectionSolver();
        try {
            root = solver.solve(f1, -2.0, -2.0);
            Assert.fail();
        } catch (NoRootsException e) {
            //expected
        }

        // Test for function with a root very close to the initial guess
        solver = new BisectionSolver();
        try {
            root = solver.solve(f3, 0.99, 1.01);
            Assert.fail();
        } catch (NumberIsTooSmallException e) {
            //expected
        }

        // Test for function with a root very close to the absolute accuracy
        solver = new BisectionSolver();
        try {
            root = solver.solve(f3, 0.999999999999, 1.000000000001);
            Assert.fail();
        } catch (PrecisionMismatchException e) {
            //expected
        }
    }
}