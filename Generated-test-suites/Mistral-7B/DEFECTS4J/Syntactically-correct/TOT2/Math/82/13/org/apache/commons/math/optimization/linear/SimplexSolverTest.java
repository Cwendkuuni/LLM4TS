import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintViolation;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver.epsilon);
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        double customEpsilon = 0.0001;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertNotEquals(1.0e-6, solver.epsilon, 0.0);
        assertEquals(customEpsilon, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        Integer column = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, column.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        Integer row = new SimplexSolver().getPivotRow(1, tableau);
        assertEquals(1, row.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        new SimplexSolver().doIteration(tableau);

        SimplexTableau expectedTableau = new SimplexTableau(new double[][]{
                {1, 0, 3/2, 3/2},
                {0, 2/3, -1/3, 1/3},
                {0, 1/3, -1, 2/3},
                {0, 1/3, 1/3, -1/3},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        Assert.assertArrayEquals(expectedTableau.getMatrix(), tableau.getMatrix(), 0.0);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        tableau = new SimplexTableau(new double[][]{
                {1, 0, 3/2, 3/2},
                {0, 2/3, -1/3, 1/3},
                {0, 1/3, -1, 2/3},
                {0, 1/3, 1/3, -1/3},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        assertFalse(new SimplexSolver().isOptimal(tableau));

        tableau = new SimplexTableau(new double[][]{
                {1, 0, 3/2, 3/2},
                {0, 2/3, -1/3, 1/3},
                {0, 1/3, -1, 2/3},
                {0, 1/3, 1/3, -1/3},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        SimplexTableau expectedTableau = new SimplexTableau(new double[][]{
                {1, 0, 3/2, 3/2},
                {0, 2/3, -1/3, 1/3},
                {0, 1/3, -1, 2/3},
                {0, 1/3, 1/3, -1/3},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        Assert.assertArrayEquals(expectedTableau.getMatrix(), tableau.getMatrix(), 0.0);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        RealPointValuePair solution = new SimplexSolver().doOptimize();

        PointValuePair expectedSolution = new PointValuePair(new double[]{3/2, 3/2, 0}, -7/2);
        Assert.assertEquals(expectedSolution.getPoint(), solution.getPoint(), 0.0);
        Assert.assertEquals(expectedSolution.getValue(), solution.getValue(), 0.0);
    }

    @Test(expected = OptimizationException.class)
    public void testUnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, -1},
                {-1, 1, 3, 2},
                {-3, 2, 2, -1},
                {0, 0, 0, 1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new AssertionError("Expected OptimizationException");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
                {1, 2, 3, 4},
                {-2, 3, 1, 1},
                {-1, 1, 3, 2},
                {-3, 2, 2, 1},
                {0, 0, 0, -1}
        }, new LinearObjectiveFunctionSet(new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{-1, 0, 0, 1})
        }), GoalType.MINIMIZE, false, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new AssertionError("Expected NoFeasibleSolutionException");
    }

    @Test
    public void testLinearConstraintSet() {
        LinearConstraintSet constraintSet = new LinearConstraintSet(
                new LinearConstraint[]{
                    new LinearConstraint(new double[]{1, 2}, 3),
                    new LinearConstraint(new double[]{2, 3}, 4),
                    new LinearConstraint(new double[]{3, 4}, 5)
                });

        LinearConstraintViolation violation = new SimplexSolver().optimize(constraintSet, new LinearObjectiveFunction[]{
                new LinearObjectiveFunction(new double[]{1, 2, 3, 4})
        });

        double[] expectedViolation = new double[]{1.0, 1.0};
        Assert.assertArrayEquals(expectedViolation, violation.getViolation(), 0.0);
    }
}