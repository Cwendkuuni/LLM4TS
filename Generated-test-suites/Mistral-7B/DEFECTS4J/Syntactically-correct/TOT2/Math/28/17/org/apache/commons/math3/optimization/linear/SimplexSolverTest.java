package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimplexSolverTest {

    @Test
    public void testEmptyConstraints() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0}, new double[]{-1.0});
        SimplexSolver solver = new SimplexSolver();
        LinearConstraintSet constraints = new LinearConstraintSet();
        PointValuePair result = solver.optimize(obj, constraints, null);
        assertEquals(Double.NEGATIVE_INFINITY, result.getValue(), 0.0);
        assertEquals(new double[]{-1.0}, result.getPoint(), 0.0);
    }

    @Test
    public void testSimpleTest1() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{3.0, 2.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 2.0}, 3.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 2.0}, -2.0, Relation.LEQUAL);
        constraints.addConstraint(c2);
        PointValuePair result = new SimplexSolver().optimize(obj, constraints, null);
        assertEquals(2.5, result.getValue(), 0.01);
        assertEquals(new double[]{0.5, 1.5}, result.getPoint(), 0.01);
    }

    @Test
    public void testSimpleTest2() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{3.0, 2.0}, new double[]{1.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 2.0}, 3.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 2.0}, -2.0, Relation.LEQUAL);
        constraints.addConstraint(c2);
        LinearConstraint c3 = new LinearConstraint(new double[]{1.0, 2.0}, 1.0, Relation.EQUAL);
        constraints.addConstraint(c3);
        PointValuePair result = new SimplexSolver().optimize(obj, constraints, null);
        assertEquals(2.0, result.getValue(), 0.01);
        assertEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.01);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 0.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        new SimplexSolver().optimize(obj, constraints, null);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 0.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        new SimplexSolver(1e-12, 1).optimize(obj, constraints, null);
    }

    @Test
    public void testNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, -1.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        try {
            new SimplexSolver().optimize(obj, constraints, null);
            assert false;
        } catch (NoFeasibleSolutionException e) {
            // pass
        }
    }

    @Test
    public void testSolveWithUpperAndLowerBounds() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{2.0, 3.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 1.0}, 0.0, Relation.LEQUAL);
        constraints.addConstraint(c1);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relation.GEQUAL);
        constraints.addConstraint(c2);
        SimpleBounds bounds = new SimpleBounds(new double[]{0.0, 1.0}, new double[]{1.0, 2.0});
        PointValuePair result = new SimplexSolver().optimize(obj, constraints, bounds);
        assertEquals(3.0, result.getValue(), 0.01);
        assertEquals(new double[]{1.0, 2.0}, result.getPoint(), 0.01);
    }
}