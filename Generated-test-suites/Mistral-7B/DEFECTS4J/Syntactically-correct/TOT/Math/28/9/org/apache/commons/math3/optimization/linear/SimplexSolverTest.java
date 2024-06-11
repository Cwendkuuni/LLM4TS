package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBound;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() throws UnboundedSolutionException, NoFeasibleSolutionException, MaxCountExceededException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{-3.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0)
        };
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints, SimpleBound.unbounded(), SimpleBound.unbounded());
        assertEquals(0.5, solution.getPoint()[0], 0.01);
        assertEquals(-1.0, solution.getPoint()[1], 0.01);
        assertEquals(-2.5, solution.getValue(), 0.01);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws UnboundedSolutionException, NoFeasibleSolutionException, MaxCountExceededException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objFunction, constraints, SimpleBound.unbounded(), SimpleBound.unbounded());
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws UnboundedSolutionException, NoFeasibleSolutionException, MaxCountExceededException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0, 0.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
                new LinearConstraint(new double[]{1.0, 1.0}, 0.0)
        };
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objFunction, constraints, SimpleBound.unbounded(), SimpleBound.unbounded());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws UnboundedSolutionException, NoFeasibleSolutionException, MaxCountExceededException {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{-3.0, -1.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(0);
        solver.optimize(objFunction, constraints, SimpleBound.unbounded(), SimpleBound.unbounded());
    }

}