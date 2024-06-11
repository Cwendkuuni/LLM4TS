package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import java.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        int n = 3;
        AbstractSimplex simplex1 = new AbstractSimplex(n);
        AbstractSimplex simplex2 = new AbstractSimplex(n, 2.0);
        AbstractSimplex simplex3 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        AbstractSimplex simplex4 = new AbstractSimplex(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {1.0, 2.0}});

        assertEquals(n, simplex1.getDimension());
        assertEquals(n + 1, simplex1.getSize());

        assertEquals(n, simplex2.getDimension());
        assertEquals(n + 1, simplex2.getSize());

        assertEquals(n, simplex3.getDimension());
        assertEquals(n + 1, simplex3.getSize());

        assertEquals(n, simplex4.getDimension());
        assertEquals(n + 1, simplex4.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexEmptySteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplexLength() {
        new AbstractSimplex(new double[][]{});
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplexFirst() {
        new AbstractSimplex(new double[][]{{0.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidReferenceSimplexFirstLength() {
        new AbstractSimplex(new double[][]{{1.0}, {2.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidReferenceSimplexDifferentLengths() {
        new AbstractSimplex(new double[][]{{1.0}, {2.0, 3.0}});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexInvalidReferenceSimplexEqualVertices() {
        new AbstractSimplex(new double[][]{{1.0}, {1.0}});
    }

    @Test
    public void testBuild() {
        int n = 3;
        double sideLength = 2.0;
        AbstractSimplex simplex = new AbstractSimplex(n);
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        simplex.build(startPoint);

        for (int i = 0; i < n + 1; ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointRef = point.getPointRef();
            assertEquals(startPoint, pointRef);

            if (i > 0) {
                double[] expectedPoint = new double[n];
                System.arraycopy(startPoint, 0, expectedPoint, 0, n);
                expectedPoint[i - 1] += sideLength;
                assertEquals(expectedPoint, pointRef);
            }
        }
    }

    @Test
    public void testEvaluate() {
        int n = 2;
        MultivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0}, 0.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(f, comparator);

        PointValuePair worstPoint = simplex.getPoint(n);
        PointValuePair bestPoint = simplex.getPoint(0);

        double worstValue = worstPoint.getValue();
        double bestValue = bestPoint.getValue();

        assertNotNull(worstPoint);
        assertNotNull(bestPoint);
        assertFalse(Double.isNaN(worstValue));
        assertFalse(Double.isNaN(bestValue));
        assertTrue(worstValue > bestValue);
    }

    @Test
    public void testReplaceWorstPoint() {
        int n = 2;
        MultivariateFunction f = new PolynomialFunction(new double[]{1.0, 1.0}, 0.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        simplex.build(new double[]{0.0, 0.0});
        PointValuePair newPoint = new PointValuePair(new double[]{1.5, 1.5}, Double.NaN);

        simplex.evaluate(f, comparator);
        PointValuePair worstPoint = simplex.getPoint(n);

        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair replacedPoint = simplex.getPoint(n);
        assertEquals(newPoint, replacedPoint);
    }

    @Test
    public void testGetPoints() {
        int n = 2;
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        simplex.build(new double[]{0.0, 0.0});

        PointValuePair[] points = simplex.getPoints();

        for (int i = 0; i < points.length; ++i) {
            PointValuePair point = points[i];
            double[] pointRef = point.getPointRef();
            assertEquals(simplex.getPoint(i), point);
            assertArrayEquals(pointRef, simplex.getPoint(i).getPointRef());
        }
    }
}