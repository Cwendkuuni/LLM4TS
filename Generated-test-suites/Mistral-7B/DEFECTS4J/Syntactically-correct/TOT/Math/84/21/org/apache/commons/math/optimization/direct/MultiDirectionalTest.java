package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FooFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleScalarProblem;
import org.junit.Test;

/**
 * Test class for MultiDirectional class.
 */
public class MultiDirectionalTest {

    /**
     * Test method for {@link MultiDirectional#MultiDirectional()} constructor.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiDirectionalDefaultConstructor() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        assertInstanceOf(MultiDirectional.class, optimizer);
        assertEquals(2.0, optimizer.khi, 0.001);
        assertEquals(0.5, optimizer.gamma, 0.001);
    }

    /**
     * Test method for {@link MultiDirectional#MultiDirectional(double, double)} constructor.
     * @throws Exception if an error occurs
     */
    @Test
    public void testMultiDirectionalCustomConstructor() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(1.5, 0.3);
        assertInstanceOf(MultiDirectional.class, optimizer);
        assertEquals(1.5, optimizer.khi, 0.001);
        assertEquals(0.3, optimizer.gamma, 0.001);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        FooFunction function = new FooFunction();
        SimpleScalarProblem problem = new SimpleScalarProblem(function, new double[]{-100.0, -100.0}, new double[]{100.0, 100.0}, null, SimpleGoalType.MINIMIZE);
        optimizer.setMaxFunctionEvaluations(10);
        optimizer.optimize(problem, new NelderMeadSimplex(new double[]{-50.0, -50.0}, new double[]{50.0, 50.0}));
        PointValuePair best = optimizer.getSolution();
        double expectedX[] = {-2.23606797749979, -1.94467381248262};
        double expectedF = -1.38199622761283;
        assertEquals(expectedX.length, best.getPoint().length);
        assertArrayEquals(expectedX, best.getPoint(), 0.01);
        assertEquals(expectedF, best.getValue(), 0.01);
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with illegal argument.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexIllegalArgument() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        FooFunction function = new FooFunction();
        SimpleScalarProblem problem = new SimpleScalarProblem(function, null, null, null, SimpleGoalType.MINIMIZE);
        optimizer.optimize(problem, new NelderMeadSimplex(new double[]{-50.0, -50.0}, new double[]{50.0, 50.0}));
    }

    /**
     * Test method for {@link MultiDirectional#iterateSimplex(Comparator)} method with convergence.
     * @throws Exception if an error occurs
     */
    @Test
    public void testIterateSimplexConvergence() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        FooFunction function = new FooFunction();
        SimpleScalarProblem problem = new SimpleScalarProblem(function, new double[]{0.0}, new double[]{0.0}, null, SimpleGoalType.MINIMIZE);
        optimizer.setMaxFunctionEvaluations(1000);
        optimizer.optimize(problem, new NelderMeadSimplex(new double[]{0.0}, new double[]{0.0}));
        PointValuePair best = optimizer.getSolution();
        double expectedX[] = {0.0};
        double expectedF = 0.0;
        assertEquals(expectedX.length, best.getPoint().length);
        assertArrayEquals(expectedX, best.getPoint(), 0.01);
        assertEquals(expectedF, best.getValue(), 0.01);
    }

}