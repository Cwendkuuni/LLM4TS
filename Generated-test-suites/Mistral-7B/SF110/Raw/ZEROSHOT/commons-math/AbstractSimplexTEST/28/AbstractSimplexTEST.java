package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        try {
            AbstractSimplex simplex1 = new AbstractSimplex(1);
            assertEquals(1, simplex1.getDimension());
            AbstractSimplex simplex2 = new AbstractSimplex(2, 1.0);
            assertEquals(2, simplex2.getDimension());
            AbstractSimplex simplex3 = new AbstractSimplex(3);
            assertEquals(3, simplex3.getDimension());
            AbstractSimplex simplex4 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
            assertEquals(3, simplex4.getDimension());
            AbstractSimplex simplex5 = new AbstractSimplex(new double[][]{
                    {1.0, 0.0},
                    {0.0, 1.0}
            });
            assertEquals(2, simplex5.getDimension());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() throws Exception {
        // Abstract class, can't test directly
    }

    @Test
    public void testBuild() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        for (PointValuePair point : points) {
            assertNotNull(point.getValue());
        }
    }

    @Test
    public void testEvaluate() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        MultivariateFunction function = new FunctionWithTwoVariables();
        simplex.evaluate(function, null);
        for (PointValuePair point : simplex.getPoints()) {
            assertNotNull(point.getValue());
        }
    }

    @Test
    public void testReplaceWorstPoint() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair pointToReplace = simplex.getPoints()[0];
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        simplex.replaceWorstPoint(pointToReplace, comparator);
        PointValuePair worstPoint = simplex.getPoints()[3];
        assertEquals(pointToReplace, worstPoint);
    }

    @Test
    public void testGetPoint() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(0);
        assertEquals(startPoint, point.getPointRef());
    }

    @Test
    public void testSetPoint() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        double[] newPoint = {4.0, 5.0, 6.0};
        simplex.setPoint(0, new PointValuePair(newPoint, Double.NaN));
        PointValuePair point = simplex.getPoint(0);
        assertEquals(newPoint, point.getPointRef());
    }

    @Test
    public void testSetPoints() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[][] newPoints = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        simplex.setPoints(new PointValuePair[newPoints.length][newPoints[0].length]);
        for (int i = 0; i < newPoints.length; i++) {
            simplex.setPoint(i, new PointValuePair(newPoints[i], Double.NaN));
        }
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < newPoints.length; i++) {
            PointValuePair point = points[i];
            assertArrayEquals(newPoints[i], point.getPointRef());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testZeroException() {
        new AbstractSimplex(0);
        new AbstractSimplex(new double[0]);
        new AbstractSimplex(new double[][]{{}, {}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        new AbstractSimplex(new double[]{1.0, 2.0, 3.0}, new double[]{2.0, 2.0});
        new AbstractSimplex(new double[][]{{1.0, 2.0}, {1.0, 3.0}});
        new AbstractSimplex(new double[][]{{1.0, 2.0}, {1.0, 2.0, 3.0}});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testMathIllegalArgumentException() {
        new AbstractSimplex(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
    }

    private static class FunctionWithTwoVariables implements MultivariateFunction {

        @Override
        public double value(double[] x) {
            return x[0] * x[0] + x[1] * x[1];
        }
    }
}