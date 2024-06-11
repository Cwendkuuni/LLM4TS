package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testAbstractSimplex_int_double() {
        AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testAbstractSimplex_doubleArray() {
        double[] steps = {1.0, 1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());

        double[] expectedSimpleX = {1.0, 1.0, 1.0, 2.0, 2.0, 2.0};
        PointValuePair[] actualSimpleX = simplex.getPoints();
        assertArrayEquals(expectedSimpleX, actualSimpleX);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_doubleArrayNull() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayZeroLength() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayZeroStep() {
        double[] steps = {0.0, 1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayMismatchedLength() {
        double[] steps = {1.0, 1.0, 1.0, 1.1};
        AbstractSimplex simplex = new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayMismatchedIndex() {
        double[] referenceSimplex = {1.0, 1.0, 1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        simplex.setPoints(new PointValuePair[1]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_doubleArrayEqualVertices() {
        double[] referenceSimplex = {1.0, 1.0, 1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        simplex.setPoints(new PointValuePair[] {new PointValuePair(new double[3], 0.0), new PointValuePair(new double[3], 0.0)});
    }
}