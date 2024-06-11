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
    public void testAbstractSimplexConstructorWithInt() {
        AbstractSimplex simplex = new Simplex(3);
        assertEquals(3, simplex.getDimension());
        assertNotNull(simplex.getPoints());
    }

    @Test
    public void testAbstractSimplexConstructorWithIntAndSideLength() {
        AbstractSimplex simplex = new Simplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
        assertNotNull(simplex.getPoints());
    }

    @Test
    public void testAbstractSimplexConstructorWithSteps() {
        double[] steps = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new Simplex(steps);
        assertEquals(3, simplex.getDimension());
        assertNotNull(simplexFace(simplex, 0));
        assertNotNull(simplexFace(simplex, 1));
        assertNotNull(simplexFace(simplex, 2));
    }

    @Test
    public void testAbstractSimplexConstructorWithReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
        AbstractSimplex simplex = new Simplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertNotNull(simplexFace(simplex, 0));
        assertNotNull(simplexFace(simplex, 1));
        assertNotNull(simplexFace(simplex, 2));
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNullSteps() {
        new Simplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithEmptySteps() {
        new Simplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexConstructorWithNegativeOrZeroSteps() {
        new Simplex(new double[]{-0.5, 1.5, 2.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedReferenceSimplexLength() {
        new Simplex(new double[][]{{0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}, {1.0}});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithMismatchedReferenceSimplexColumns() {
        new Simplex(new double[][]{{0.0, 1.0}, {1.0}, {1.0, 1.0}});
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterate() {
        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, 1.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        simplex.iterate(function, comparator);
        assertArrayEquals(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        }, simplexFace(simplex, 0), 0.001);
    }

    @Test
    public void testAbstractSimplexBuild() {
        double[] startPoint = new double[]{0.0, 0.0};
        AbstractSimplex simplex = new Simplex(3);
        simplex.build(startPoint);
        assertArrayEquals(new double[][]{
            {0.0, 0.0},
            {0.0, 1.0},
            {0.0, 2.0},
            {1.0, 0.0},
            {1.0, 1.0},
            {1.0, 2.0}
        }, simplexFace(simplex, 0), 0.001);
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, 1.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        simplex.evaluate(function, comparator);
        assertArrayEquals(new double[][]{
            {0.0, 1.0, 4.0},
            {1.0, 0.0, 4.0},
            {1.0, 1.0, 0.0}
        }, simplexFace(simplex, 0), 0.001);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, 1.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        simplex.evaluate(function, comparator);
        double worstPointValue = simplex.get(simplexFace(simplex, comparator, 2).getValue());
        PointValuePair worstPoint = simplex.get(2);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{4.0, 4.0}, Double.NaN), comparator);
        assertEquals(4.0, simplex.get(2).getValue(), 0.001);
        assertNotEquals(worstPointValue, simplex.get(2).getValue());
        assertEquals(worstPoint.getPointRef(), simplex.get(simplex.getSize() - 1).getPointRef());
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(new double[]{0.0, 1.0}, Double.NaN),
            new PointValuePair(new double[]{1.0, 0.0}, Double.NaN),
            new PointValuePair(new double[]{1.0, 1.0}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        PointValuePair expectedPoint = new PointValuePair(new double[]{1.0, 0.0}, Double.NaN);
        assertEquals(expectedPoint, simplex.getPoint(1));
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 2.0}, Double.NaN);
        simplex.setPoint(0, newPoint);
        PointValuePair expectedPoint = new PointValuePair(new double[]{2.0, 2.0}, Double.NaN);
        assertEquals(expectedPoint, simplex.getPoint(0));
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        PointValuePair[] newPoints = new PointValuePair[]{
            new PointValuePair(new double[]{3.0, 3.0}, Double.NaN),
            new PointValuePair(new double[]{4.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{5.0, 5.0}, Double.NaN)
        };
        AbstractSimplex simplex = new Simplex(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0},
            {1.0, 1.0}
        });
        simplex.setPoints(newPoints);
        PointValuePair[] expectedPoints = new PointValuePair[]{
            new PointValuePair(new double[]{3.0, 3.0}, Double.NaN),
            new PointValuePair(new double[]{4.0, 4.0}, Double.NaN),
            new PointValuePair(new double[]{5.0, 5.0}, Double.NaN)
        };
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testAbstractSimplexCreateHypercubeSteps() {
        double[] expectedSteps = new double[]{1.0, 1.0, 1.0};
        assertArrayEquals(expectedSteps, AbstractSimplex.createHypercubeSteps(3, 1.0));
    }

    private PointValuePair[] simplexFace(AbstractSimplex simplex, int index) {
        return simplexFace(simplex, simplex.getComparator(), index);
    }

    private PointValuePair[] simplexFace(AbstractSimplex simplex, Comparator<PointValuePair> comparator, int index) {
        PointValuePair[] points = simplex.getPoints();
        return new PointValuePair[]{points[index]};
    }

    private QuadraticFunction quadraticFunction(double[] coefficients) {
        return new QuadraticFunction(coefficients, 0.0);
    }
}