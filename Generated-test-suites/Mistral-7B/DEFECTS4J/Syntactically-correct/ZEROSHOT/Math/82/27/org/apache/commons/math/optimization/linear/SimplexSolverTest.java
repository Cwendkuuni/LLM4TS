import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.simple.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.nonlinear.simple.exception.UnboundedSolutionException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.linear.SimplexSolver.*;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(DEFAULT_EPSILON, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        double customEpsilon = 0.001;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        Assert.assertEquals(customEpsilon, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{},
            null,
            DEFAULT_EPSILON
        );
        Integer pivotColumn = getPivotColumn(tableau);
        Assert.assertEquals(1, pivotColumn.intValue());
    }

    @Test(expected = OptimizationException.class)
    public void testGetPivotColumnThrowsExceptionWhenNoNegativeCoefficients() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, 1, 3}, 0),
            new LinearConstraint[]{},
            null,
            DEFAULT_EPSILON
        );
        getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        Integer pivotRow = getPivotRow(0, tableau);
        Assert.assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        doIteration(tableau);
        Assert.assertEquals(1, tableau.getHeight());
        Assert.assertEquals(1, tableau.getEntry(0, 2), 0.0);
        Assert.assertEquals(0, tableau.getEntry(0, 1), 0.0);
        Assert.assertEquals(0, tableau.getEntry(0, 0), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationThrowsExceptionWhenNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        solvePhase1(tableau);
        assertTrue(isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1ThrowsExceptionWhenNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.LEQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        solvePhase1(tableau);
    }

    @Test
    public void testIsOptimal() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(
            3,
            new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0),
            new LinearConstraint[]{
                new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
                new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1)
            },
            null,
            DEFAULT_EPSILON
        );
        solvePhase1(tableau);
        tableau.discardArtificialVariables();
        assertTrue(isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{0, 1}, Relationship.LEQ, 1),
            new LinearConstraint(new double[]{2, 0}, Relationship.LEQ, 1),
            new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1)
        };
        LinearObjectiveFunction goal = new LinearObjectiveFunction(new double[]{-1, -2, 3}, 0);

        PointValuePair solution = new SimplexSolver().doOptimize(goal, constraints);
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{0, 1, 1}, 0);
        assertEquals(expectedSolution, solution);
    }
}