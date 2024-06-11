package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplex() {
        try {
            AbstractSimplex simplex1 = new AbstractSimplex(1);
            assertEquals(1, simplex1.getDimension());
            assertEquals(2, simplex1.getSize());
            simplex1.iterate(mockFunction(), mockComparator());
            PointValuePair[] points = simplex1.getPoints();
            assertEquals(2, points.length);
            assertEquals(0.0, points[0].getPointRef()[0], 1e-9);
            assertEquals(NaN.NaN, points[0].getValue());
            assertEquals(1.0, points[1].getPointRef()[0], 1e-9);
            assertEquals(NaN.NaN, points[1].getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            AbstractSimplex simplex2 = new AbstractSimplex(2, 2.0);
            assertEquals(2, simplex2.getDimension());
            assertEquals(3, simplex2.getSize());
            simplex2.iterate(mockFunction(), mockComparator());
            PointValuePair[] points = simplex2.getPoints();
            assertEquals(3, points.length);
            assertEquals(0.0, points[0].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[0].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[0].getValue());
            assertEquals(2.0, points[1].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[1].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[1].getValue());
            assertEquals(2.0, points[2].getPointRef()[0], 1e-9);
            assertEquals(2.0, points[2].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[2].getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            AbstractSimplex simplex3 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
            assertEquals(3, simplex3.getDimension());
            assertEquals(4, simplex3.getSize());
            simplex3.iterate(mockFunction(), mockComparator());
            PointValuePair[] points = simplex3.getPoints();
            assertEquals(4, points.length);
            assertEquals(0.0, points[0].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[0].getPointRef()[1], 1e-9);
            assertEquals(0.0, points[0].getPointRef()[2], 1e-9);
            assertEquals(NaN.NaN, points[0].getValue());
            assertEquals(1.0, points[1].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[1].getPointRef()[1], 1e-9);
            assertEquals(0.0, points[1].getPointRef()[2], 1e-9);
            assertEquals(NaN.NaN, points[1].getValue());
            assertEquals(1.0, points[2].getPointRef()[0], 1e-9);
            assertEquals(1.0, points[2].getPointRef()[1], 1e-9);
            assertEquals(0.0, points[2].getPointRef()[2], 1e-9);
            assertEquals(NaN.NaN, points[2].getValue());
            assertEquals(1.0, points[3].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[3].getPointRef()[1], 1e-9);
            assertEquals(1.0, points[3].getPointRef()[2], 1e-9);
            assertEquals(NaN.NaN, points[3].getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            AbstractSimplex simplex4 = new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}});
            assertEquals(2, simplex4.getDimension());
            assertEquals(4, simplex4.getSize());
            simplex4.iterate(mockFunction(), mockComparator());
            PointValuePair[] points = simplex4.getPoints();
            assertEquals(4, points.length);
            assertEquals(0.0, points[0].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[0].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[0].getValue());
            assertEquals(1.0, points[1].getPointRef()[0], 1e-9);
            assertEquals(1.0, points[1].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[1].getValue());
            assertEquals(2.0, points[2].getPointRef()[0], 1e-9);
            assertEquals(2.0, points[2].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[2].getValue());
            assertEquals(3.0, points[3].getPointRef()[0], 1e-9);
            assertEquals(3.0, points[3].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[3].getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            AbstractSimplex simplex5 = new AbstractSimplex(new AbstractSimplex(2, 2.0));
            assertEquals(2, simplex5.getDimension());
            assertEquals(3, simplex5.getSize());
            simplex5.iterate(mockFunction(), mockComparator());
            PointValuePair[] points = simplex5.getPoints();
            assertEquals(3, points.length);
            assertEquals(0.0, points[0].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[0].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[0].getValue());
            assertEquals(2.0, points[1].getPointRef()[0], 1e-9);
            assertEquals(0.0, points[1].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[1].getValue());
            assertEquals(2.0, points[2].getPointRef()[0], 1e-9);
            assertEquals(2.0, points[2].getPointRef()[1], 1e-9);
            assertEquals(NaN.NaN, points[2].getValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullArgumentException() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroException() {
        new AbstractSimplex(0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDimensionMismatchException() {
        try {
            new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0});
        } catch (Exception e) {
            assertEquals("Dimensions must agree", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexNotStrictlyPositiveException() {
        try {
            new AbstractSimplex(new double[][]{});
        } catch (Exception e) {
            assertEquals("Simplex needs at least one point", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexMathIllegalArgumentException() {
        try {
            new AbstractSimplex(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        } catch (Exception e) {
            assertEquals("Vertices must not be equal", e.getMessage());
            throw e;
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexOutOfRangeException() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0));
        } catch (OutOfRangeException e) {
            assertEquals(-1, e.getActualIndex());
            assertEquals(0, e.getMinIndex());
            assertEquals(3, e.getMaxIndex());
            throw e;
        }

        try {
            simplex.setPoint(4, new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0));
        } catch (OutOfRangeException e) {
            assertEquals(4, e.getActualIndex());
            assertEquals(0, e.getMinIndex());
            assertEquals(3, e.getMaxIndex());
            throw e;
        }
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = new PointValuePair[]{
                new PointValuePair(new double[]{1.1, 2.1, 3.1}, 1.1),
                new PointValuePair(new double[]{1.2, 2.2, 3.2}, 1.2),
                new PointValuePair(new double[]{1.3, 2.3, 3.3}, 1.3)};
        simplex.setPoints(points);
        assertEquals(3, simplex.getSize());
        PointValuePair[] simplexPoints = simplex.getPoints();
        assertEquals(1.1, simplexPoints[0].getPointRef()[0], 1e-9);
        assertEquals(1.1, simplexPoints[0].getValue());
        assertEquals(1.2, simplexPoints[1].getPointRef()[0], 1e-9);
        assertEquals(1.2, simplexPoints[1].getValue());
        assertEquals(1.3, simplexPoints[2].getPointRef()[0], 1e-9);
        assertEquals(1.3, simplexPoints[2].getValue());
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        simplex.iterate(mockFunction(), mockComparator());
        PointValuePair worstPoint = simplex.getPoints()[0];
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.1, 2.1, 3.1}, 1.1), mockComparator());
        PointValuePair[] simplexPoints = simplex.getPoints();
        assertEquals(0, Arrays.binarySearch(simplexFitnesses(simplexPoints), worstPoint.getValue()));
        assertEquals(1, Arrays.binarySearch(simplexFitnesses(simplexPoints), new PointValuePair(new double[]{1.1, 2.1, 3.1}, 1.1).getValue()));
    }

    private double[] simplexFitnesses(PointValuePair[] points) {
        double[] fitnesses = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            fitnesses[i] = points[i].getValue();
        }
        return fitnesses;
    }

    private MultivariateFunction mockFunction() {
        return f -> Math.pow(f[0], 2) + Math.pow(f[1], 2);
    }

    private Comparator<PointValuePair> mockComparator() {
        return (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());
    }
}