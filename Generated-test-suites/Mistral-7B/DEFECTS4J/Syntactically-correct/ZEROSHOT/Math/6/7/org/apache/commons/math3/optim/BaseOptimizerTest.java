package org.apache.commons.math3.optimization.base;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for {@link BaseOptimizer} interface.
 *
 * @author John Doe
 */
public class BaseOptimizerTest {

    private BaseOptimizer<Object> optimizer;
    private ConvergenceChecker<Object> convergenceChecker;

    @Before
    public void init() {
        optimizer = new BaseMockOptimizer<>();
        convergenceChecker = new ConvergenceChecker<Object>() {
            @Override
            public boolean checkConvergence(Object point, double fx) {
                return fx <= 1e-6;
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertNotNull(maxEvaluations);
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        optimizer.optimize(new PointValuePair(new Point(0.0), 0.0));
        int evaluations = optimizer.getEvaluations();
        Assert.assertEquals(1, evaluations);
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        Assert.assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(10);
        int maxEvaluations = optimizer.getMaxEvaluations();
        Assert.assertEquals(10, maxEvaluations);
    }

    /**
     * Test setConvergenceChecker method.
     */
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<Object> newChecker = new ConvergenceChecker<Object>() {
            @Override
            public boolean checkConvergence(Object point, double fx) {
                return fx <= 1e-8;
            }
        };
        optimizer.setConvergenceChecker(newChecker);
        Assert.assertNotSame(convergenceChecker, optimizer.getConvergenceChecker());
    }
}

class PointValuePair {
    private Point point;
    private double value;

    public PointValuePair(Point point, double value) {
        this.point = point;
        this.value = value;
    }

    public Point getPoint() {
        return point;
    }

    public double getValue() {
        return value;
    }
}

class Point {
    private double[] coords;

    public Point(double[] coords) {
        this.coords = coords;
    }

    public double[] getCoordinates() {
        return coords;
    }
}

class BaseMockOptimizer<PAIR> implements BaseOptimizer<PAIR> {

    private int evaluations;
    private int maxEvaluations = MaxEval.UNLIMITED;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Override
    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    @Override
    public int getEvaluations() {
        return evaluations;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
        this.convergenceChecker = convergenceChecker;
    }

    @Override
    public PAIR optimize(PAIR initialGuess) {
        evaluations++;
        return new PointValuePair(new Point(new double[]{0.0}), 0.0);
    }
}