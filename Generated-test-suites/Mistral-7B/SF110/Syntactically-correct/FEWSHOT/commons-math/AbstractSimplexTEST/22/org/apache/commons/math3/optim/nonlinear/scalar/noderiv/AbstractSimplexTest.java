package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConstructor() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexEmptyConstructor() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroStepConstructor() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidNumberOfPointsInReferenceSimplex() {
        new AbstractSimplex(new double[][]{});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexReferenceSimplexIncorrectDimension() {
        new AbstractSimplex(new double[][]{{1.0}});
    }

    @Test
    public void testAbstractSimplexHypercubeSteps() {
        final int n = 5;
        final AbstractSimplex simplex = new AbstractSimplex(n);
        assertEquals(n, simplex.getDimension());
        assertEquals(n + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexReferenceSimplex() {
        final double[][] referenceSimplex = {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0},
            {7.0, 8.0}
        };

        final AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(referenceSimplex.length - 1, simplex.getDimension());
        assertEquals(referenceSimplex.length, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidStartPoint() {
        final AbstractSimplex simplex = new AbstractSimplex(new int[5]);
    }

    @Test
    public void testAbstractSimplexBuildWithNullComparator() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(null);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexBuildWithInvalidStartPoint() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0});
    }

    @Test
    public void testAbstractSimplexBuild() {
        final double sideLength = 2.0;
        final AbstractSimplex simplex = new AbstractSimplex(5, sideLength);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());

        final double[][] startConfiguration = simplex.getStartConfiguration();
        for (int i = 0; i < simplex.getDimension(); ++i) {
            for (int j = 0; j < simplex.getDimension(); ++j) {
                assertEquals(sideLength, startConfiguration[i][j], 0.0);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexEvaluateWithNullComparator() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.evaluate(null, null);
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        final MultivariateFunction evaluationFunction = mock(MultivariateFunction.class);
        final Comparator<PointValuePair> comparator = mock(Comparator.class);
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(evaluationFunction, comparator);

        verify(evaluationFunction, times(6)).value(anyArray());
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        final PointValuePair worstPoint = new PointValuePair(new double[]{1.0, 2.0}, 10.0);
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.replaceWorstPoint(worstPoint, mock(Comparator.class));

        final PointValuePair[] points = simplex.getPoints();
        assertEquals(worstPoint, points[5]);
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{0.0, 0.0});

        final double[] point = simplex.getPoint(0).getPointRef();
        assertArrayEquals(new double[]{0.0}, point, 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointOutOfBounds() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{0.0, 0.0});

        simplex.getPoint(-1);
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{0.0, 0.0});

        final double[] point = new double[]{1.0, 2.0};
        simplex.setPoint(0, new PointValuePair(point, 3.0));

        final PointValuePair pointValuePair = simplex.getPoint(0);
        assertArrayEquals(point, pointValuePair.getPointRef(), 0.0);
        assertEquals(3.0, pointValuePair.getValue(), 0.0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointOutOfBounds() {
        final AbstractSimplex simplex = new AbstractSimplex(5);
        simplex.build(new double[]{0.0, 0.0});

        simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 2.0}, 3.0));
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        final AbstractSimplex simplex = new AbstractSimplex(5);

        final PointValuePair[] points = new PointValuePair[]{
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{3.0, 4.0}, 1.0),
            new PointValuePair(new double[]{5.0, 6.0}, 2.0),
            new PointValuePair(new double[]{7.0, 8.0}, 4.0),
            new PointValuePair(new double[]{9.0, 10.0}, 5.0)
        };

        simplex.setPoints(points);

        final PointValuePair[] expectedPoints = simplex.getPoints();
        assertArrayEquals(points, expectedPoints);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsWithInvalidSize() {
        final AbstractSimplex simplex = new AbstractSimplex(5);

        final PointValuePair[] points = new PointValuePair[]{
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0),
            new PointValuePair(new double[]{1.0, 2.0}, 3.0)
        };

        simplex.setPoints(points);
    }

}