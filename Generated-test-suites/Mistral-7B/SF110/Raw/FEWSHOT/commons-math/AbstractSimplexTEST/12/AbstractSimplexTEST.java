package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstruction() {
        AbstractSimplex simplex1 = new AbstractSimplex(3);
        AbstractSimplex simplex2 = new AbstractSimplex(3, 2.0);
        AbstractSimplex simplex3 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        AbstractSimplex simplex4 = new AbstractSimplex(new double[][]{{1.0, 1.0}, {2.0, 2.0}});

        assertEquals(3, simplex1.getDimension());
        assertEquals(3, simplex1.getSize());
        assertEquals(3, simplex2.getDimension());
        assertEquals(4, simplex2.getSize());
        assertEquals(3, simplex3.getDimension());
        assertEquals(4, simplex3.getSize());
        assertEquals(2, simplex4.getDimension());
        assertEquals(3, simplex4.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullArgumentException() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroException() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroSteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDimensionMismatchException() {
        new AbstractSimplex(new double[2][3]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexStartConfigurationLengthMismatch() {
        new AbstractSimplex(new double[][]{new double[3], new double[2]});
    }

    @Test(expected = EqualVerticesInSimplexException.class)
    public void testAbstractSimplexEqualVerticesInSimplexException() {
        new AbstractSimplex(new double[][]{new double[3], new double[3]});
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterate() {
        // Abstract class, cannot be instantiated to test
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);

        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            assertNotNull(vertex);
            assertArrayEquals(startPoint, vertex.getPointRef());
        }
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        // Abstract class, cannot be instantiated to test
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        // Abstract class, cannot be instantiated to test
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3, 2.0);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] expectedPoints = new PointValuePair[4];
        for (int i = 0; i < 4; ++i) {
            double[] point = new double[3];
            System.arraycopy(startPoint, 0, point, 0, 3);
            expectedPoints[i] = new PointValuePair(point, Double.NaN);
        }

        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertArrayEquals(expectedPoints, points);
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        AbstractSimplex simp = new AbstractSimplex(3, 2.0);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simp.build(startPoint);

        for (int i = 0; i < simp.getSize(); ++i) {
            PointValuePair point = simp.getPoint(i);
            assertNotNull(point);
            assertEquals(i + 1, point.getIndex());
            assertArrayEquals(startPoint, point.getPointRef());
        }
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        AbstractSimplex simp = new AbstractSimplex(3, 2.0);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simp.build(startPoint);

        PointValuePair point = new PointValuePair(new double[]{3.0, 4.0, 5.0}, Double.NaN);
        simp.setPoint(1, point);

        PointValuePair expectedPoint = new PointValuePair(new double[]{2.0, 2.0, 3.0}, Double.NaN);
        PointValuePair actualPoint = simp.getPoint(1);
        assertNotNull(actualPoint);
        assertEquals(expectedPoint.getPointRef(), actualPoint.getPointRef());
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        AbstractSimplex simp = new AbstractSimplex(3, 2.0);

        PointValuePair[] newPoints = new PointValuePair[4];
        for (int i = 0; i < 4; ++i) {
            double[] point = new double[3];
            point[i] = i;
            newPoints[i] = new PointValuePair(point, Double.NaN);
        }

        simp.setPoints(newPoints);

        PointValuePair[] expectedPoints = new PointValuePair[4];
        System.arraycopy(newPoints, 0, expectedPoints, 0, 4);
        PointValuePair[] actualPoints = simp.getPoints();
        assertArrayEquals(expectedPoints, actualPoints);
    }
}