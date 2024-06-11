package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithNullArgumentException() {
        try {
            new AbstractSimplex(null);
            fail("Exception expected");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroDimension() {
        try {
            new AbstractSimplex(0);
            fail("Exception expected");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroStepsException() {
        double[] steps = new double[1];
        steps[0] = 0.0;
        try {
            new AbstractSimplex(steps);
            fail("Exception expected");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVerticesException() {
        double[] steps = new double[2];
        steps[0] = 1.0;
        steps[1] = 1.0;
        try {
            new AbstractSimplex(steps);
            fail("Exception expected");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithNotStrictlyPositiveException() {
        double[][] referenceSimplex = new double[1][2];
        try {
            new AbstractSimplex(referenceSimplex);
            fail("Exception expected");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithDimensionMismatchException() {
        double[][] referenceSimplex = new double[2][3];
        try {
            new AbstractSimplex(referenceSimplex);
            fail("Exception expected");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVerticesInSimplexException() {
        double[][] referenceSimplex = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        try {
            new AbstractSimplex(referenceSimplex);
            fail("Exception expected");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithGetDimension() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        assertEquals(2, test.getDimension());
    }

    @Test
    public void testAbstractSimplexWithGetSize() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        assertEquals(3, test.getSize());
    }

    @Test
    public void testAbstractSimplexWithIterate() {
        MultivariateFunction testFunction = new Add();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.iterate(testFunction, comparator);
        PointValuePair worstPoint = test.getPoint(2);
        test.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, -10.0), comparator);
        PointValuePair newWorstPoint = test.getPoint(2);
        assertEquals(-10.0, newWorstPoint.getValue(), 0.001);
        assertNotEquals(worstPoint.getValue(), newWorstPoint.getValue());
    }

    @Test
    public void testAbstractSimplexWithBuild() {
        double[] startPoint = new double[]{1.0, 1.0};
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(startPoint);
        PointValuePair worstPoint = test.getPoint(2);
        assertEquals(new double[]{2.0, 1.0}, worstPoint.getPointRef(), 0.001);
    }

    @Test
    public void testAbstractSimplexWithEvaluate() {
        MultivariateFunction testFunction = new Add();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(new double[]{0.0, 0.0});
        test.evaluate(testFunction, comparator);
        PointValuePair worstPoint = test.getPoint(2);
        assertEquals(-1.0, worstPoint.getValue(), 0.001);
    }

    @Test
    public void testAbstractSimplexWithReplaceWorstPoint() {
        MultivariateFunction testFunction = new Add();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(new double[]{0.0, 0.0});
        test.evaluate(testFunction, comparator);
        PointValuePair worstPoint = test.getPoint(2);
        test.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, -10.0), comparator);
        PointValuePair newWorstPoint = test.getPoint(2);
        assertEquals(-10.0, newWorstPoint.getValue(), 0.001);
        assertNotEquals(worstPoint.getValue(), newWorstPoint.getValue());
    }

    @Test
    public void testAbstractSimplexWithGetPoints() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(new double[]{0.0, 0.0});
        test.evaluate(new Add(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair[] points = test.getPoints();
        assertEquals(3, points.length);
        assertArrayEquals(new double[]{0.0, 0.0}, points[0].getPointRef(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, points[1].getPointRef(), 0.001);
        assertArrayEquals(new double[]{1.0, 1.0}, points[2].getPointRef(), 0.001);
    }

    @Test
    public void testAbstractSimplexWithGetPoint() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(new double[]{0.0, 0.0});
        test.evaluate(new Add(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair point = test.getPoint(0);
        assertArrayEquals(new double[]{0.0, 0.0}, point.getPointRef(), 0.001);
        point = test.getPoint(1);
        assertArrayEquals(new double[]{1.0, 1.0}, point.getPointRef(), 0.001);
        point = test.getPoint(2);
        assertArrayEquals(new double[]{1.0, 1.0}, point.getPointRef(), 0.001);
    }

    @Test
    public void testAbstractSimplexWithSetPoint() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        test.build(new double[]{0.0, 0.0});
        test.evaluate(new Add(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair point = test.getPoint(2);
        test.setPoint(2, new PointValuePair(new double[]{1.0, 1.0}, -10.0));
        PointValuePair newPoint = test.getPoint(2);
        assertEquals(-10.0, newPoint.getValue(), 0.001);
        assertNotEquals(point.getValue(), newPoint.getValue());
    }

    @Test
    public void testAbstractSimplexWithSetPoints() {
        AbstractSimplex test = new MyAbstractSimplex(2);
        PointValuePair[] points = new PointValuePair[]{
            new PointValuePair(new double[]{0.0, 0.0}, 0.0),
            new PointValuePair(new double[]{1.0, 1.0}, 1.0),
            new PointValuePair(new double[]{1.0, 1.0}, 1.0)
        };
        test.setPoints(points);
        PointValuePair[] testPoints = test.getPoints();
        assertArrayEquals(points, testPoints);
    }

    @Test
    public void testAbstractSimplexWithCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(2, 1.0);
        assertEquals(2, steps.length);
        assertEquals(1.0, steps[0], 0.001);
        assertEquals(1.0, steps[1], 0.001);
    }

    static class MyAbstractSimplex extends AbstractSimplex {
        MyAbstractSimplex(int n) {
            super(n);
        }

        @Override
        protected void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // Empty implementation
        }
    }

    static class Add implements MultivariateFunction {
        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < point.length; ++i) {
                sum += point[i] * point[i];
            }
            return sum;
        }

        @Override
        public double value(double[] point, double[] gradient, int maxEvaluations) throws MaxEvaluationsExceededException {
            throw new UnsupportedOperationException();
        }
    }
}