package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplexOptimizationData;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleX;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterationData;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {

    AbstractSimplex simplex;
    ObjectiveFunction objectiveFunction;
    Comparator<PointValuePair> comparator;

    @Before
    public void setUp() {
        this.objectiveFunction = new SimpleObjectiveFunction();
        this.comparator = new SimplexComparator();
    }

    @Test
    public void testAbstractSimplexConstructorWithSingleArgument() {
        this.simplex = new AbstractSimplex(3);
        assertEquals(3, this.simplex.getDimension());
        assertEquals(4, this.simplex.getSize());
    }

    @Test
    public void testAbstractSimplexConstructorWithTwoArguments() {
        this.simplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, this.simplex.getDimension());
        assertEquals(4, this.simplex.getSize());
    }

    @Test
    public void testAbstractSimplexConstructorWithArrayArgument() {
        double[] steps = {1.0, 2.0, 3.0};
        this.simplex = new AbstractSimplex(steps);
        assertEquals(3, this.simplex.getDimension());
        assertEquals(4, this.simplex.getSize());
        assertArrayEquals(steps, this.simplex.startConfiguration[0]);
    }

    @Test
    public void testAbstractSimplexConstructorWithReferenceSimplexArgument() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        this.simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, this.simplex.getDimension());
        assertEquals(3, this.simplex.getSize());
    }

    @Test
    public void testGetDimension() {
        this.simplex = new AbstractSimplex(3);
        assertEquals(3, this.simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        this.simplex = new AbstractSimplex(3);
        assertEquals(4, this.simplex.getSize());
    }

    @Test
    public void testIterate() {
        this.simplex = new AbstractSimplex(3);
        SimpleX simplexOptimizer = new SimpleX(this.objectiveFunction, this.comparator, this.simplex, GoalType.MINIMIZE);
        simplexOptimizer.optimize();
        assertEquals(4, this.simplex.getSize());
        PointValuePair bestPoint = this.simplex.getPoint(this.simplex.getSize() - 1);
        assertEquals(0.0, bestPoint.getValue(), 1e-6);
    }

    @Test
    public void testBuild() {
        this.simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        this.simplex.build(startPoint);
        assertEquals(4, this.simplex.getSize());
        PointValuePair bestPoint = this.simplex.getPoint(this.simplex.getSize() - 1);
        assertEquals(GoalType.MINIMIZE, bestPoint.getGoalType());
        assertEquals(0.0, bestPoint.getValue(), 1e-6);
    }

    @Test
    public void testEvaluate() {
        this.simplex = new AbstractSimplex(3);
        this.simplex.build(new double[]{1.0, 2.0, 3.0});
        this.objectiveFunction.value(new double[]{1.0, 2.0, 3.0});
        this.simplex.evaluate(this.objectiveFunction, this.comparator);
        PointValuePair bestPoint = this.simplex.getPoint(this.simplex.getSize() - 1);
        assertEquals(0.0, bestPoint.getValue(), 1e-6);
    }

    @Test
    public void testReplaceWorstPoint() {
        this.simplex = new AbstractSimplex(3);
        this.simplex.build(new double[]{1.0, 2.0, 3.0});
        this.simplex.evaluate(this.objectiveFunction, this.comparator);
        PointValuePair worstPoint = this.simplex.getPoint(0);
        double worstValue = worstPoint.getValue();
        PointValuePair newPoint = new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN);
        this.simplex.replaceWorstPoint(newPoint, this.comparator);
        PointValuePair newWorstPoint = this.simplex.getPoint(0);
        assertEquals(newPoint, newWorstPoint);
        assertEquals(worstValue, this.simplex.getPoint(1).getValue(), 1e-6);
    }

    @Test
    public void testGetPoints() {
        this.simplex = new AbstractSimplex(3);
        this.simplex.build(new double[]{1.0, 2.0, 3.0});
        this.simplex.evaluate(this.objectiveFunction, this.comparator);
        PointValuePair[] points = this.simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(this.simplex.getPoint(0), points[0]);
        assertEquals(this.simplex.getPoint(1), points[1]);
        assertEquals(this.simplex.getPoint(2), points[2]);
        assertEquals(this.simplex.getPoint(3), points[3]);
    }

    @Test
    public void testGetPoint() {
        this.simplex = new AbstractSimplex(3);
        this.simplex.build(new double[]{1.0, 2.0, 3.0});
        this.simplex.evaluate(this.objectiveFunction, this.comparator);
        PointValuePair point = this.simplex.getPoint(1);
        assertEquals(this.simplex.getPoint(1).getPointRef(), point.getPointRef());
        assertEquals(this.simplex.getPoint(1).getValue(), point.getValue(), 1e-6);
    }

    @Test
    public void testSetPoint() {
        this.simplex = new AbstractSimplex(3);
        this.simplex.build(new double[]{1.0, 2.0, 3.0});
        this.simplex.evaluate(this.objectiveFunction, this.comparator);
        PointValuePair point = new PointValuePair(new double[]{1.5, 2.5, 3.5}, Double.NaN);
        this.simplex.setPoint(1, point);
        PointValuePair newPoint = this.simplex.getPoint(1);
        assertEquals(point, newPoint);
    }

    @Test
    public void testSetPoints() {
        this.simplex = new AbstractSimplex(3);
        PointValuePair[] points = {new PointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN),
                new PointValuePair(new double[]{4.0, 5.0, 6.0}, Double.NaN)};
        this.simplex.setPoints(points);
        assertEquals(2, this.simplex.getSize());
        assertEquals(points[0], this.simplex.getPoint(0));
        assertEquals(points[1], this.simplex.getPoint(1));
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
        assertEquals(3, steps.length);
        assertEquals(2.0, steps[0], 1e-6);
        assertEquals(2.0, steps[1], 1e-6);
        assertEquals(2.0, steps[2], 1e-6);
    }

    @Test
    public void testInitializeWithNullReferenceSimplex() {
        assertThrows(NullPointerException.class, () -> new AbstractSimplex(null));
    }

    @Test
    public void testInitializeWithZeroDimensionReferenceSimplex() {
        assertThrows(NotStrictlyPositiveException.class, () -> new AbstractSimplex(new double[0][0]));
    }

    @Test
    public void testInitializeWithEmptySimpleX() {
        this.simplex = new AbstractSimplex(new SimpleX(new SimpleObjectiveFunction(), this.comparator, new PointValuePair[0], GoalType.MINIMIZE));
        assertNull(this.simplex.getPoint(0));
    }

    @Test
    public void testInitializeWithPointsOfDifferentSizes() {
        double[][] referenceSimplex = {
                {1.0, 2.0},
                {2.0, 3.0},
                {3.0}
        };
        assertThrows(DimensionMismatchException.class, () -> new AbstractSimplex(referenceSimplex));
    }

    @Test
    public void testInitializeWithEqualVerticesInSimpleX() {
        double[][] referenceSimplex = {{1.0, 2.0}, {2.0, 2.0}, {3.0, 3.0}};
        assertThrows(org.apache.commons.math3.exception.MathIllegalArgumentException.class, () -> new AbstractSimplex(referenceSimplex));
    }

    private static class SimpleObjectiveFunction implements ObjectiveFunction {
        @Override
        public double value(double[] x) {
            return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2) + Math.pow(Math.sin(Math.PI * x[2]), 2);
        }
    }

    private static class SimplexComparator implements Comparator<PointValuePair> {
        @Override
        public int compare(PointValuePair o1, PointValuePair o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
    }

    private static class SimplexIterationData implements InitialGuess {
        @Override
        public PointValuePair getInitialGuess() {
            return new PointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN);
        }
    }
}