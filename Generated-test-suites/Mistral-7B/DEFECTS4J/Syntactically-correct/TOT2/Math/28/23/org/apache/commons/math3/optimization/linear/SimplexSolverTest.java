import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_DefaultConstructor_SimpleExample() {
        PointFunction func = new PointFunction() {
            public double value(double[] x) {
                return x[0] * 2 + x[1] * 3;
            }
        };

        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[] {1.0, 1.0}, 0.0),
                new LinearConstraint(1.0, new double[] {-1.0, 1.0}, 0.0),
                new LinearConstraint(-1.0, new double[] {1.0, 1.0}, 2.0),
                new LinearConstraint(-1.0, new double[] {-1.0, 1.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(func, constraints);

        Assert.assertEquals(0.0, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(1.5, solution.getPoint()[1], 1e-6);
        Assert.assertEquals(-3.5, solution.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_CustomConstructor_SimpleExample() {
        PointFunction func = new PointFunction() {
            public double value(double[] x) {
                return x[0] * 2 + x[1] * 3;
            }
        };

        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[] {1.0, 1.0}, 0.0),
                new LinearConstraint(1.0, new double[] {-1.0, 1.0}, 0.0),
                new LinearConstraint(-1.0, new double[] {1.0, 1.0}, 2.0),
                new LinearConstraint(-1.0, new double[] {-1.0, 1.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver(1e-7, 50);
        PointValuePair solution = solver.optimize(func, constraints);

        Assert.assertEquals(0.0, solution.getPoint()[0], 1e-7);
        Assert.assertEquals(1.5, solution.getPoint()[1], 1e-7);
        Assert.assertEquals(-3.5, solution.getValue(), 1e-7);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() {
        PointFunction func = new PointFunction() {
            public double value(double[] x) {
                return x[0] * 2 + x[1] * 3;
            }
        };

        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[] {1.0, 1.0}, 0.0),
                new LinearConstraint(1.0, new double[] {-1.0, 1.0}, 0.0),
                new LinearConstraint(-1.0, new double[] {1.0, 1.0}, 2.0),
                new LinearConstraint(-1.0, new double[] {-1.0, 1.0}, 1.0)
        };

        SimplexSolver solver = new SimplexSolver(1e-6, 1);
        solver.optimize(func, constraints);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() {
        PointFunction func = new PointFunction() {
            public double value(double[] x) {
                return x[0];
            }
        };

        LinearConstraint[] constraints = new LinearConstraint[] {};

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(func, constraints);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() {
        PointFunction func = new PointFunction() {
            public double value(double[] x) {
                return -x[0] + x[1];
            }
        };

        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(1.0, new double[] {1.0, 1.0}, 0.0),
                new LinearConstraint(-1.0, new double[] {1.0, 1.0}, 0.0)
        };

        SimplexSolver solver = new SimplexSolver();
        solver.optimize(func, constraints);
    }
}