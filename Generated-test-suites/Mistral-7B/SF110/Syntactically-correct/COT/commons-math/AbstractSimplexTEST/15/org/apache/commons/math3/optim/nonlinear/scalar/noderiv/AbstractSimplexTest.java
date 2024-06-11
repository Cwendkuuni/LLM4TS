package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.multivariate.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(5);
            assertEquals(5, simplex.getDimension());
            assertEquals(6, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplex_int_double() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(5, 2.0);
            assertEquals(5, simplex.getDimension());
            assertEquals(6, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAbstractSimplex_doubleArray() {
        try {
            double[] steps = {1.0, 1.0, 1.0, 1.0, 1.0};
            AbstractSimplex simplex = new AbstractSimplex(steps);
            assertEquals(5, simplex.getDimension());
            assertEquals(6, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_nullArray() {
        try {
            new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_zeroArray() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[0]);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_dimensionMismatch() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0, 1.0, 3.0});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_equalVertices() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0, 0.0, 0.0, 0.0, 0.0});
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplex_doubleArray_referenceSimplex() {
        try {
            double[][] referenceSimplex = {{-1.0, 1.0}, {-0.5, 0.5}, {0.5, -0.5}, {1.0, -1.0}};
            AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
            assertEquals(3, simplex.getDimension());
            assertEquals(4, simplex.getSize());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_emptySimplex() {
        try {
            AbstractSimplex simplex = new AbstractSimplex(new double[0][0]);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_dimensionMismatchReferenceSimplex() {
        try {
            double[][] referenceSimplex = {{-1.0, 1.0}, {-0.5, 0.5}, {0.5, -0.5}, {1.0, -1.0, 0.5}};
            new AbstractSimplex(referenceSimplex);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_equalVerticesInSimplex() {
        try {
            double[][] referenceSimplex = {{-1.0, 1.0}, {-1.0, 1.0}, {1.0, 1.0}};
            new AbstractSimplex(referenceSimplex);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }
}