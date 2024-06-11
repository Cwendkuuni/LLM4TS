package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.MaxCountExceededException;
import org.apache.commons.math3.optim.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.nonlinear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.LinearConstraintSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 1.0}}, new double[]{1.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 5.0));
        constraints.addConstraint(new LinearConstraint(new double[]{2.0, 1.0}, Relation.LEQ, 4.0));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFunction, constraints);
        assertEquals(new double[]{0.2, 0.8}, solution.getPoint(), 0.001);
        assertEquals(3.56, solution.getValue(), 0.01);
    }

    @Test
    public void testMaxCountExceeded() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 1.0}}, new double[]{1.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver(1e-16, 1);
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(objFunction, constraints));
    }

    @Test
    public void testUnboundedSolution() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 1.0}}, new double[]{1.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(objFunction, constraints, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, null));
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 1.0}}, new double[]{1.0, 4.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQ, 1.0));
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(objFunction, constraints));
    }
}