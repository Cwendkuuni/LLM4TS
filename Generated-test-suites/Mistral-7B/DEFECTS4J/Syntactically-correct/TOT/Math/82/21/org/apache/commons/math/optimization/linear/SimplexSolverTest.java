package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint constraint;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 1.0});
        constraint = new LinearConstraint(new double[]{1.0, 1.0}, 2.0, null);
    }

    @Test
    public void testOneDimensionalProblem() throws MaxIterationsExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] f = {{0.0, -1.0}, {-1.0, -1.0}};
        LinearConstraint[] constraints = {constraint};
        PointValuePair expectedSolution = new PointValuePair(0.0, -2.0);
        RealPointValuePair actualSolution = simplexSolver.doOptimize();
        Assert.assertEquals(expectedSolution.getValue(), actualSolution.getValue(), 1e-6);
        Assert.assertEquals(expectedSolution.getPoint(), actualSolution.getPoint(), 1e-6);
    }

    @Test
    public void testTwoDimensionalProblem() throws MaxIterationsExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        double[][] f = {{0.0, -1.0, -1.0}, {-1.0, 0.0, -1.0}, {-1.0, -1.0, 0.0}};
        LinearConstraint[] constraints = {constraint, constraint, constraint};
        PointValuePair expectedSolution = new PointValuePair(0.5, 0.5, -2.0);
        RealPointValuePair actualSolution = simplexSolver.doOptimize();
        Assert.assertArrayEquals(expectedSolution.getPoint(), actualSolution.getPoint(), 1e-6);
        Assert.assertEquals(expectedSolution.getValue(), actualSolution.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidObjectiveFunction() {
        simplexSolver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws UnboundedSolutionException {
        double[][] f = {{Double.POSITIVE_INFINITY, 0.0}, {-1.0, 0.0}};
        LinearConstraint[] constraints = new LinearConstraint[0];
        simplexSolver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws NoFeasibleSolutionException {
        double[][] f = {{Double.NEGATIVE_INFINITY, 0.0}, {-1.0, 0.0}};
        LinearConstraint[] constraints = new LinearConstraint[0];
        simplexSolver.doOptimize();
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException {
        simplexSolver = new SimplexSolver(1e-12);
        double[][] f = {{0.0, -1.0}, {-1.0, -1.0}};
        LinearConstraint[] constraints = {constraint};
        simplexSolver.setMaxIterations(1);
        simplexSolver.doOptimize();
    }
}