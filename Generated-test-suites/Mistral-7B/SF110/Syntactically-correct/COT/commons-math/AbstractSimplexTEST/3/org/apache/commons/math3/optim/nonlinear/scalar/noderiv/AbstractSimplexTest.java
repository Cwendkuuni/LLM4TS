package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(1);
            assertEquals(1, simplex.getDimension());
            assertEquals(1, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplex_int_double() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(2, 1.0);
            assertEquals(2, simplex.getDimension());
            assertEquals(3, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplex_doubleArray() {
        try {
            double[] steps = {1.0, 1.0, 1.0};
            AbstractSimplex simplex = new AbstractSimplex(steps);
            assertEquals(3, simplex.getDimension());
            assertEquals(4, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplex_doubleArrayArray() {
        try {
            double[][] referenceSimplex = {{1.0, 1.0, 0.0}, {1.0, 0.0, 1.0}, {0.0, 1.0, 1.0}};
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
            assertEquals(3, simplex.getDimension());
            assertEquals(4, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_doubleArrayNull() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayZeroLength() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_doubleArrayNegativeDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0, 2.0});
    }

    @Test(expected = EqualVerticesInSimplexException.class)
    public void testAbstractSimplex_doubleArrayEqualVertices() {
        double[] steps = {1.0, 0.0, 0.0};
        try {
            AbstractSimplex simplex = new AbstractSimplex(steps);
        } catch (Exception e) {
            assertTrue(e instanceof EqualVerticesInSimplexException);
        }
    }

    @Test(expected = EqualVerticesInSimplexException.class)
    public void testAbstractSimplex_doubleArray2D_equalVertices() {
        double[][] referenceSimplex = {{1.0, 1.0, 0.0}, {1.0, 0.0, 1.0}, {0.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        try {
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        } catch (Exception e) {
            assertTrue(e instanceof EqualVerticesInSimplexException);
        }
    }

    @Test(expected = EqualVerticesInSimplexException.class)
    public void testAbstractSimplex_doubleArray2D_equalVertices_differentIndices() {
        double[][] referenceSimplex = {{1.0, 1.0, 0.0}, {1.0, 0.0, 1.0}, {0.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        try {
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        } catch (Exception e) {
            assertTrue(e instanceof EqualVerticesInSimplexException);
        }
    }
}