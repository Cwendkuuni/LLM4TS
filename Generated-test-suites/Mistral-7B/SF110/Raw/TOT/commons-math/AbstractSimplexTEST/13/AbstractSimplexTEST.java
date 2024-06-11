package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.initializer.StartingPointStrategy;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

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
    public void testAbstractSimplexWithNonStrictlyPositiveSteps() {
        try {
            new AbstractSimplex(new double[]{-1.0, 2.0});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVertices() {
        try {
            new AbstractSimplex(new double[]{-1.0, 0.0, 1.0}, 1.0);
            fail("Expected EqualVerticesInSimplexException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEmptyReferenceSimpleX() {
        try {
            new AbstractSimplex(new double[][]{});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithReferenceSimpleXWithEqualVertices() {
        try {
            new AbstractSimplex(new double[][]{
                    {1.0, 0.0},
                    {1.0, 0.0}
            });
            fail("Expected EqualVerticesInSimpleXException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithReferenceSimpleXWithMismatchedSize() {
        try {
            new AbstractSimplex(new double[][]{
                    {1.0, 0.0},
                    {1.0, 0.0, 0.0}
            });
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithCorrectArguments() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testIterateWithNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        try {
            simplex.iterate((MultivariateFunction)null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIterateWithNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        try {
            simplex.iterate(null, (Comparator<PointValuePair>)null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBuildWithNullStartPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        try {
            simplex.build((double[])null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBuildWithStartPointWithMismatchedSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        try {
            simplex.build(new double[]{1.0});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithNullFunction() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        try {
            simples.evaluate((MultivariateFunction)null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithNullComparator() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        try {
            simples.evaluate(null, (Comparator<PointValuePair>)null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReplaceWorstPointWithNullPointValuePair() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        try {
            simples.replaceWorstPoint(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReplaceWorstPointWithNullComparator() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        try {
            simples.replaceWorstPoint(new PointValuePair(new double[]{0.0}, 0.0), (Comparator<PointValuePair>)null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        PointValuePair[] points = simples.getPoints();
        assertEquals(3, points.length);
    }

    @Test
    public void testGetPointWithInvalidIndex() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        simples.build(new double[]{0.0, 0.0});
        try {
            simples.getPoint(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointWithInvalidIndex() {
        AbstractSimplex simples = new AbstractSimplex(new double[]{-1.0, 1.0}, 2.0);
        PointValuePair point = new PointValuePair(new double[]{0.0}, 0.0);
        try {
            simples.setPoint(-1, point);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsWithNullArray() {
        AbstractSimplex simples = new AbstractSimplex(3);
        try {
            simples.setPoints((PointValuePair[])null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsWithSizeMismatch() {
        AbstractSimplex simples = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[4];
        try {
            simples.setPoints(points);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}