package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    @Test
    public void testSimpleExample() {
        // create problem
        double[][] f = {{-7.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // solve problem
        RealPointValuePair solution = optimizer.doOptimize();

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0}}).getColumnVector(0);
        double expectedValue = -15.0 / 3.0;
        Assert.assertEquals(expectedSolution, solution.getPoint(), epsilon);
        Assert.assertEquals(expectedValue, solution.getValue(), epsilon);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded() {
        // create problem
        double[][] f = {{-7.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // set max iterations to 1
        optimizer.setMaxIterations(1);

        // try to solve problem
        optimizer.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        // create problem
        double[][] f = {{-7.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // modify problem to have an unbounded solution
        double[][] fMod = new double[][] {{3.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        SimplexTableau tableau = new SimplexTableau(fMod, constraints, goal, restrictToNonNegative, epsilon);
        optimizer.solvePhase1(tableau);

        // try to solve problem
        optimizer.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        // create problem
        double[][] f = {{-7.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        // modify problem to have no feasible solution
        double[][] fMod = new double[][] {{3.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -5.0}};
        SimplexTableau tableau = new SimplexTableau(fMod, constraints, goal, restrictToNonNegative, epsilon);
        optimizer.solvePhase1(tableau);

        // try to solve problem
        optimizer.doIteration(tableau);
    }

    @Test
    public void testFeasibleSolution() {
        // create problem
        double[][] f = {{-7.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // modify problem to have a feasible solution
        double[][] fMod = new double[][] {{0.0, -1.0, 3.0}, {-1.0, -5.0, 2.0}, {3.0, 2.0, -6.0}};
        SimplexTableau tableau = new SimplexTableau(fMod, constraints, goal, restrictToNonNegative, epsilon);
        optimizer.solvePhase1(tableau);

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0}}).getColumnVector(0);
        double expectedValue = -15.0 / 3.0;
        Assert.assertEquals(expectedSolution, tableau.getSolution().getPoint(), epsilon);
        Assert.assertEquals(expectedValue, tableau.getSolution().getValue(), epsilon);
    }

    @Test
    public void testOptimalSolution() {
        // create problem
        double[][] f = {{-10.0, -2.0, 3.0}, {-2.0, -8.0, 4.0}, {3.0, 4.0, -6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // solve problem
        RealPointValuePair solution = optimizer.doOptimize();

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{0.5, 1.0, 0.5}}).getColumnVector(0);
        double expectedValue = 1.0;
        Assert.assertEquals(expectedSolution, solution.getPoint(), epsilon);
        Assert.assertEquals(expectedValue, solution.getValue(), epsilon);
    }

    @Test
    public void testNegativeCoefficients() {
        // create problem
        double[][] f = {{1.0, -1.0, -3.0}, {-1.0, 2.0, -2.0}, {3.0, -2.0, 6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, -1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // solve problem
        RealPointValuePair solution = optimizer.doOptimize();

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0 / 3.0, 1.0 / 2.0, 1.0 / 3.0}}).getColumnVector(0);
        double expectedValue = 1.0 / 3.0;
        Assert.assertEquals(expectedSolution, solution.getPoint(), epsilon);
        Assert.assertEquals(expectedValue, solution.getValue(), epsilon);
    }

    @Test
    public void testMixedConstraints() {
        // create problem
        double[][] f = {{1.0, -1.0, -3.0}, {-1.0, 2.0, -2.0}, {3.0, -2.0, 6.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {-1.0, 1.0, 1.0}, {-1.0, -1.0, -1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), 0.0)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // solve problem
        RealPointValuePair solution = optimizer.doOptimize();

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0 / 3.0, 1.0 / 2.0, 1.0 / 3.0}}).getColumnVector(0);
        double expectedValue = 1.0 / 3.0;
        Assert.assertEquals(expectedSolution, solution.getPoint(), epsilon);
        Assert.assertEquals(expectedValue, solution.getValue(), epsilon);
    }

    @Test
    public void testMaximumObjectiveFunction() {
        // create problem
        double[][] f = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        double[][] constraints = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        LinearConstraint[] goal = new LinearConstraint[] {new LinearConstraint(new RealVector(3.0, true), Double.POSITIVE_INFINITY)};
        boolean restrictToNonNegative = true;
        double epsilon = 1e-6;

        SimplexSolver optimizer = new SimplexSolver(epsilon);

        // solve problem
        RealPointValuePair solution = optimizer.doOptimize();

        // check solution
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0, 1.0, 1.0}}).getColumnVector(0);
        double expectedValue = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expectedSolution, solution.getPoint(), epsilon);
        Assert.assertEquals(expectedValue, solution.getValue(), epsilon);
    }
}