package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.NoFeasibleSolutionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    /**
     * Test for {@link SimplexSolver#getPivotColumn(SimplexTableau)}.
     */
    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -(x[0] + x[1]);
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, 1),
                new LinearConstraint(1, 2, -1),
                new LinearConstraint(0, 1, 0),
                new LinearConstraint(0, 2, 0)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        Integer expected = 0;
        Integer actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);

        tableau.setEntry(0, 1, -1);
        expected = 1;
        actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);

        tableau.setEntry(0, 2, 3);
        expected = 2;
        actual = solver.getPivotColumn(tableau);
        assertEquals(expected, actual);

        tableau.setEntry(0, 0, 0);
        assertNull(solver.getPivotColumn(tableau));
    }

    /**
     * Test for {@link SimplexSolver#getPivotRow(SimplexTableau, int)}.
     */
    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -(x[0] + 2 * x[1]);
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, 3),
                new LinearConstraint(1, 2, 2),
                new LinearConstraint(0, 1, 1),
                new LinearConstraint(0, 2, 1)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        Integer expected = 0;
        Integer actual = solver.getPivotRow(tableau, 1);
        assertEquals(expected, actual);

        expected = 1;
        tableau.setEntry(1, 1, 1);
        actual = solver.getPivotRow(tableau, 1);
        assertEquals(expected, actual);

        expected = 2;
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        actual = solver.getPivotRow(tableau, 1);
        assertEquals(expected, actual);

        expected = null;
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 0, 0);
        actual = solver.getPivotRow(tableau, 1);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link SimplexSolver#doIteration(SimplexTableau)}.
     */
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -(x[0] + x[1]);
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, 1),
                new LinearConstraint(1, 2, -1),
                new LinearConstraint(0, 1, 0),
                new LinearConstraint(0, 2, 0)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        solver.doIteration(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getBasicVariableCount());
        assertEquals(1, tableau.getNumBasicConstraints());
        assertEquals(3, tableau.getNumNonBasicVariables());
        assertEquals(5, tableau.getNumConstraints());

        PointValuePair solution = solver.doOptimize();
        assertEquals(0d, solution.getPoint()[0], 1e-6);
        assertEquals(-1d, solution.getPoint()[1], 1e-6);
        assertEquals(-1d, solution.getValue(), 1e-6);

        solver.doIteration(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(2, tableau.getBasicVariableCount());
        assertEquals(2, tableau.getNumBasicConstraints());
        assertEquals(0, tableau.getNumNonBasicVariables());
        assertEquals(5, tableau.getNumConstraints());

        tableau.setEntry(1, 1, -1);
        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getBasicVariableCount());
        assertEquals(2, tableau.getNumBasicConstraints());
        assertEquals(2, tableau.getNumNonBasicVariables());
        assertEquals(5, tableau.getNumConstraints());
    }

    /**
     * Test for {@link SimplexSolver#solvePhase1(SimplexTableau)}.
     */
    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -(x[0] + x[1]);
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, 1),
                new LinearConstraint(1, 2, -1),
                new LinearConstraint(0, 1, 0),
                new LinearConstraint(0, 2, 0)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        solver.solvePhase1(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getBasicVariableCount());
        assertEquals(1, tableau.getNumBasicConstraints());
        assertEquals(3, tableau.getNumNonBasicVariables());
        assertEquals(5, tableau.getNumConstraints());

        tableau.dropPhase1Objective();

        PointValuePair solution = solver.doOptimize();
        assertEquals(0d, solution.getPoint()[0], 1e-6);
        assertEquals(-1d, solution.getPoint()[1], 1e-6);
        assertEquals(-1d, solution.getValue(), 1e-6);

        tableau.setEntry(1, 1, 1);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();

        solution = solver.doOptimize();
        assertEquals(1d, solution.getPoint()[0], 1e-6);
        assertEquals(0d, solution.getPoint()[1], 1e-6);
        assertEquals(0d, solution.getValue(), 1e-6);

        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, -1);
        solver.solvePhase1(tableau);

        assertNull(solver.doOptimize());
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -x[0];
                }

                @Override
                public int getNumberOfVariables() {
                    return 1;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, -1),
                new LinearConstraint(0, 1, 0)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        solver.doIteration(tableau);

        throw new AssertionError("Test should have thrown UnboundedSolutionException");
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -x[0];
                }

                @Override
                public int getNumberOfVariables() {
                    return 1;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(-1, 1, 1)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            10
        );

        solver.solvePhase1(tableau);
        throw new AssertionError("Test should have thrown NoFeasibleSolutionException");
    }

    /**
     * Test for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() {
        SimplexTableau tableau = new SimplexTableau(
            new LinearObjectiveFunction() {
                @Override
                public double value(double[] x) {
                    return -(x[0] + x[1]);
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }
            },
            new LinearConstraint[] {
                new LinearConstraint(1, 1, 1),
                new LinearConstraint(1, 2, -1),
                new LinearConstraint(0, 1, 0),
                new LinearConstraint(0, 2, 0)
            },
            LinearOptimizer.GOAL_TYPE_MINIMIZE,
            false,
            1e-6,
            1
        );

        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();

        for (int i = 0; i < 100; i++) {
            solver.doIteration(tableau);
        }

        throw new AssertionError("Test should have thrown MaxCountExceededException");
    }
}