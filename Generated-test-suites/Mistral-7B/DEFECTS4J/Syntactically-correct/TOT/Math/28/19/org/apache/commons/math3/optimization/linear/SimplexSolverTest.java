package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.simple.QuadraticFunction;
import org.apache.commons.math3.optim.nonlinear.simple.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optim.nonlinear.simple.linear.RealVector2D;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest extends AbstractTest {

    /**
     * Test optimization of a linear problem with a single objective function and no constraints.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testEmptyStringOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(f, constraints, null, null, 0.001, 100);
        assertEquals(-3.0, solution.getPoint()[0], 1e-6);
        assertEquals(-5.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization of a linear problem with a single objective function and a single constraint.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testSingleConstraintOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0});
        LinearConstraint c = new LinearConstraint(new double[][]{{1.0}, {3.0}}, Relationship.LEQUAL_TO, 0.0);
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(f, new LinearConstraint[] {c}, null, null, 0.001, 100);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(-5.0, solution.getValue(), 1e-6);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and no constraints.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testMultipleObjectivesEmptyConstraintsOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(functions, null, null, null, 0.001, 100);
        assertEquals(-3.0, solution.getPoint()[0], 1e-6);
        assertEquals(-5.0, solution.getValue()[0], 1e-6);
        assertEquals(0.0, solution.getValue()[1], 1e-6);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and a single constraint.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testMultipleObjectivesSingleConstraintOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearConstraint c = new LinearConstraint(new double[][]{{1.0}, {3.0}}, Relationship.LEQUAL_TO, 0.0);
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(functions, new LinearConstraint[] {c}, null, null, 0.001, 100);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(-5.0, solution.getValue()[0], 1e-6);
        assertEquals(0.0, solution.getValue()[1], 1e-6);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and multiple constraints.
     * @throws Exception if an error occurs during the optimization
     */
    @Test
    public void testMultipleObjectivesMultipleConstraintsOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0}, {3.0}}, Relationship.LEQUAL_TO, 0.0),
            new LinearConstraint(new double[][]{{2.0}}, Relationship.LEQUAL_TO, 2.0)
        };
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(functions, constraints, null, null, 0.001, 100);
        assertEquals(0.0, solution.getPoint()[0], 1e-6);
        assertEquals(-5.0, solution.getValue()[0], 1e-6);
        assertEquals(2.0, solution.getPoint()[1], 1e-6);
        assertEquals(5.0, solution.getValue()[1], 1e-6);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and a constraint with no solution.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testMultipleObjectivesInfeasibleConstraintOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearConstraint c = new LinearConstraint(new double[][]{{1.0}, {2.0}}, Relationship.LEQUAL_TO, 0.0);
        LinearOptimizer optimizer = new SimplexSolver();
        optimizer.optimize(functions, new LinearConstraint[] {c}, null, null, 0.001, 100);
    }

    /**
     * Test optimization of a linear problem with a single objective function and an unbounded solution.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testSingleObjectiveUnboundedSolutionOptimization() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        LinearOptimizer optimizer = new SimplexSolver();
        optimizer.optimize(f, constraints, null, null, 0.001, 100);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and an unbounded solution.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testMultipleObjectivesUnboundedSolutionOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearOptimizer optimizer = new SimplexSolver();
        optimizer.optimize(functions, null, null, null, 0.001, 100);
    }

    /**
     * Test optimization of a linear problem with multiple objective functions and a constraint with a maximal iteration count exceeded.
     * @throws Exception if an error occurs during the optimization
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMultipleObjectivesMaxIterationCountReachedOptimization() throws Exception {
        LinearObjectiveFunction[] functions = new LinearObjectiveFunction[] {
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {-3.0}}, new double[]{-5.0}),
            new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0}}, new double[]{5.0})
        };
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0}, {3.0}}, Relationship.LEQUAL_TO, 0.0),
            new LinearConstraint(new double[][]{{2.0}}, Relationship.LEQUAL_TO, 2.0)
        };
        LinearOptimizer optimizer = new SimplexSolver();
        optimizer.setMaxIterations(0);
        optimizer.optimize(functions, constraints, null, null, 0.001, 100);
    }
}