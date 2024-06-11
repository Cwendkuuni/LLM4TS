import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.convert.LinearObjectiveFunctionConverter;
import org.apache.commons.math3.optim.nonlinear.converters.GoalType;
import org.apache.commons.math3.optim.nonlinear.converters.MaxEval;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleBound;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleConstraints;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleLinearObjectiveFunctionImpl;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleLinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleMaxCountExceededException;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleUnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.converters.SimpleNoFeasibleSolutionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolver() throws Exception {
        // Test a simple problem
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0),
                new LinearConstraint(new double[]{1.0}, 0.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        SimpleLinearObjectiveFunction linObj = new SimpleLinearObjectiveFunctionImpl(
                LinearObjectiveFunctionConverter.convert(obj),
                LinearObjectiveFunctionConverter.convert(goalType)
        );
        SimpleLinearConstraintSet linConstraints = new SimpleLinearConstraintSet(
                LinearObjectiveFunctionConverter.convert(constraints),
                MaxEval.UNLIMITED,
                MaxEval.UNLIMITED
        );

        SimpleBound[] bounds = new SimpleBound[2];
        bounds[0] = new SimpleBound(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        bounds[1] = new SimpleBound(0.0, Double.POSITIVE_INFINITY);

        PointValuePair result = solver.optimize(
                linConstraints,
                bounds,
                linObj,
                new MaxCountExceededException(1),
                new UnboundedSolutionException(),
                new NoFeasibleSolutionException()
        );

        assertEquals(0.0, result.getValue(), epsilon);
        assertEquals(new double[]{1.0, 0.0}, result.getPoint(), epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0),
                new LinearConstraint(new double[]{1.0}, 0.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        SimpleLinearObjectiveFunction linObj = new SimpleLinearObjectiveFunctionImpl(
                LinearObjectiveFunctionConverter.convert(obj),
                LinearObjectiveFunctionConverter.convert(goalType)
        );
        SimpleLinearConstraintSet linConstraints = new SimpleLinearConstraintSet(
                LinearObjectiveFunctionConverter.convert(constraints),
                MaxEval.UNLIMITED,
                MaxEval.UNLIMITED
        );

        SimpleBound[] bounds = new SimpleBound[2];
        bounds[0] = new SimpleBound(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        bounds[1] = new SimpleBound(0.0, Double.POSITIVE_INFINITY);

        solver.optimize(
                linConstraints,
                bounds,
                linObj,
                new MaxCountExceededException(1),
                new UnboundedSolutionException(),
                new NoFeasibleSolutionException()
        );
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0),
                new LinearConstraint(new double[]{1.0}, 0.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        SimpleLinearObjectiveFunction linObj = new SimpleLinearObjectiveFunctionImpl(
                LinearObjectiveFunctionConverter.convert(obj),
                LinearObjectiveFunctionConverter.convert(goalType)
        );
        SimpleLinearConstraintSet linConstraints = new SimpleLinearConstraintSet(
                LinearObjectiveFunctionConverter.convert(constraints),
                MaxEval.ONE,
                MaxEval.UNLIMITED
        );

        SimpleBound[] bounds = new SimpleBound[2];
        bounds[0] = new SimpleBound(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        bounds[1] = new SimpleBound(0.0, Double.POSITIVE_INFINITY);

        solver.optimize(
                linConstraints,
                bounds,
                linObj,
                new MaxCountExceededException(1),
                new UnboundedSolutionException(),
                new NoFeasibleSolutionException()
        );
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0),
                new LinearConstraint(new double[]{1.0}, 0.0),
                new LinearConstraint(new double[]{-1.0, 1.0}, 1.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();

        SimpleLinearObjectiveFunction linObj = new SimpleLinearObjectiveFunctionImpl(
                LinearObjectiveFunctionConverter.convert(obj),
                LinearObjectiveFunctionConverter.convert(goalType)
        );
        SimpleLinearConstraintSet linConstraints = new SimpleLinearConstraintSet(
                LinearObjectiveFunctionConverter.convert(constraints),
                MaxEval.UNLIMITED,
                MaxEval.UNLIMITED
        );

        SimpleBound[] bounds = new SimpleBound[2];
        bounds[0] = new SimpleBound(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        bounds[1] = new SimpleBound(-1.0, Double.POSITIVE_INFINITY);

        solver.optimize(
                linConstraints,
                bounds,
                linObj,
                new MaxCountExceededException(1),
                new UnboundedSolutionException(),
                new NoFeasibleSolutionException()
        );
    }
}