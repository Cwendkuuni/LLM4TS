package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws NotStrictlyPositiveException, DimensionMismatchException, MathIllegalArgumentException, ZeroException {
        // Test with a simplex created using zero-based steps
        simplex = new AbstractSimplex(4, 2.0);
    }

    @Test
    public void testGetDimension() {
        assertEquals(4, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(5, simplex.getSize());
    }

    @Test
    public void testIterateWithNullFunction() {
        try {
            simplex.iterate(null, null);
            fail("Expecting a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testIterateWithNullComparator() {
        try {
            simplex.iterate(new PolynomialFunction(new double[]{-1.0, 1.0}, 1), null);
            fail("Expecting a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testBuildAndEvaluateWithNullStartPoint() {
        try {
            simplex.build(null);
            fail("Expecting a DimensionMismatchException or a NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        try {
            simplex.evaluate(null, null);
            fail("Expecting a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testBuildAndEvaluateWithZeroStartPoint() {
        try {
            simplex = new AbstractSimplex(4, 2.0, new double[]{0.0, 0.0, 0.0, 0.0});
        } catch (ZeroException e) {
            fail("A zero start point should be accepted for a non-empty simplex");
        }

        try {
            simplex.evaluate(new PolynomialFunction(new double[]{-1.0, 1.0}, 1), null);
            fail("Expecting an AssertionError");
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointWithNullPointValuePair() {
        try {
            simplex.replaceWorstPoint(null, null);
            fail("Expecting a NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointWithDuplicatePointValuePair() {
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);

        try {
            simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);
            fail("Expecting a MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointWithPointValuePairNotInSimplex() {
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);

        try {
            simplex.replaceWorstPoint(new PointValuePair(new double[]{0.0, 0.0}, 1.0), null);
            fail("Expecting a MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointWithPointValuePairAlreadyInDifferentPosition() {
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{2.0, 2.0}, 0.5), null);

        try {
            simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0}, 1.0), null);
            fail("Expecting a MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // expected
        }
    }
}