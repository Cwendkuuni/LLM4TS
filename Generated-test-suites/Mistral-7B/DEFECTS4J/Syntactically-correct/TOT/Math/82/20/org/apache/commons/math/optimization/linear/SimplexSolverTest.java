import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.maximization.GoalType;
import org.apache.commons.math3.optim.nonlinear.Constraint;
import org.apache.commons.math3.optim.nonlinear.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleConstraints;
import org.apache.commons.math3.optim.nonlinear.SimpleObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintFactory;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunctionFactory;
import org.apache.commons.math3.util.MathUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{-3.0, 1.0})
                .setC(5.0)
                .getValue();

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {
                LinearConstraintFactory.createGreaterThan(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}
                }, 3.0),
                LinearConstraintFactory.createLessThan(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}
                }, 1.0)
        });

        GoalType goalType = GoalType.MAXIMIZE;
        SimplexSolver solver = new SimplexSolver(0.001);

        PointValuePair solution = solver.optimize(f, constraints, goalType, null, null);

        assertEquals(5.0, solution.getPoint()[0], 0.001);
        assertEquals(1.0, solution.getPoint()[1], 0.001);
        assertEquals(5.0, solution.getValue(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNoConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{-3.0, 1.0})
                .setC(5.0)
                .getValue();

        SimplexSolver solver = new SimplexSolver(0.001);

        solver.optimize(f, null, GoalType.MAXIMIZE, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{-3.0, 1.0})
                .setC(5.0)
                .getValue();

        SimplexSolver solver = new SimplexSolver(0.001);

        solver.optimize(f, null, GoalType.MINIMIZE, null, null);
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY})
                .setC(Double.NEGATIVE_INFINITY)
                .getValue();

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {});

        SimplexSolver solver = new SimplexSolver(0.001);

        try {
            solver.optimize(f, constraints, GoalType.MAXIMIZE, null, null);
            assertTrue(false);
        } catch (UnboundedSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{3.0, 1.0})
                .setC(5.0)
                .getValue();

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {
                LinearConstraintFactory.createGreaterThan(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}
                }, 3.0),
                LinearConstraintFactory.createLessThan(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}
                }, 1.0)
        });

        SimplexSolver solver = new SimplexSolver(0.001);

        try {
            solver.optimize(f, constraints, GoalType.MAXIMIZE, null, null);
            assertTrue(false);
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testNullObject() {
        LinearObjectiveFunction f = null;

        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {});

        SimplexSolver solver = new SimplexSolver(0.001);

        try {
            solver.optimize(f, constraints, GoalType.MAXIMIZE, null, null);
            assertTrue(false);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{-3.0, 1.0})
                .setC(5.0)
                .getValue();

        LinearConstraint[] constraints = new LinearConstraint[]{null};

        SimplexSolver solver = new SimplexSolver(0.001);

        try {
            solver.optimize(f, new LinearConstraintSet(constraints), GoalType.MAXIMIZE, null, null);
            assertTrue(false);
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunctionFactory()
                .setA(new double[][]{{2.0, 3.0}, {4.0, 1.0}})
                .setB(new double[]{-3.0, 1.0})
                .setC(5.0)
                .getValue();

        SimplexSolver solver = new SimplexSolver(0.001);

        try {
            PointValuePair solution = solver.optimize(f, null, GoalType.MAXIMIZE, null, null);
            assertNull(solution);
        } catch (OptimizationException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCustomObjectiveFunction() {
        ObjectiveFunction objFun = new SimpleObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return 2 * x[0] + 3 * x[1];
            }
        };

        Constraint[] constraints = new Constraint[]{
                SimpleConstraints.createGreaterThan(0, new double[]{1.0})
        };

        GoalType goalType = GoalType.MAXIMIZE;
        SimplexSolver solver = new SimplexSolver(0.001);

        PointValuePair solution = solver.optimize(objFun, new LinearConstraintSet(constraints), goalType, null, null);

        assertEquals(0.0, solution.getPoint()[0], 0.001);
        assertEquals(-0.5, solution.getPoint()[1], 0.001);
        assertEquals(1.0, solution.getValue(), 0.001);
    }
}