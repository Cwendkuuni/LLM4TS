package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithNullArguments() {
        try {
            new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroDimensions() {
        try {
            new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroSteps() {
        try {
            new AbstractSimplex(new double[0]);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVertices() {
        try {
            new AbstractSimplex(new double[] {0.0, 0.0, 0.0, 0.0});
            fail("Expected ZeroException or DimensionMismatchException");
        } catch (ZeroException | DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplexFail() {
        try {
            new AbstractSimplex(new double[][] {{1.0, 2.0}, {1.0, 2.0}});
            fail("Expected NotStrictlyPositiveException or DimensionMismatchException");
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithCorrectArguments() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] {1.0, 1.0, 2.0, 2.0});
        assertEquals(3, simplex.getDimension());

        double[][] referenceSimplex = new double[][] {{1.0, 1.0}, {1.0, 2.0}, {2.0, 1.0}, {2.0, 2.0}};
        simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(3, simplex.getDimension());

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);

        PointValuePair point1 = points[0];
        assertEquals(1.0, point1.getPointRef()[0], 0.001);
        assertEquals(1.0, point1.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, point1.getValue());

        PointValuePair point2 = points[1];
        assertEquals(1.0, point2.getPointRef()[0], 0.001);
        assertEquals(2.0, point2.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, point2.getValue());

        PointValuePair point3 = points[2];
        assertEquals(2.0, point3.getPointRef()[0], 0.001);
        assertEquals(1.0, point3.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, point3.getValue());

        PointValuePair point4 = points[3];
        assertEquals(2.0, point4.getPointRef()[0], 0.001);
        assertEquals(2.0, point4.getPointRef()[1], 0.001);
        assertEquals(Double.NaN, point4.getValue());
    }
}