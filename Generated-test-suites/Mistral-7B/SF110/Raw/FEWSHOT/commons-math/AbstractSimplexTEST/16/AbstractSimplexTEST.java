package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Arrays;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        try {
            new AbstractSimplex(0);
            fail("Should throw ZeroException");
        } catch (ZeroException e) {
            // Expected
        }

        try {
            new AbstractSimplex(null);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        int[] sizes = {1, 2, 3, 4};
        for (int size : sizes) {
            AbstractSimplex simplex = new AbstractSimplex(size);
            assertEquals(size, simplex.getDimension());
            assertEquals(size + 1, simplex.getSize());
        }
    }

    @Test
    public void testAbstractSimplexWithStartConfiguration() {
        double[][] referenceSimplex = {{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(referenceSimplex.length - 1, simplex.getDimension());
        assertEquals(referenceSimplex.length, simplex.getSize());
        for (int i = 0; i < referenceSimplex.length; ++i) {
            assertArrayEquals(referenceSimplex[i], simplex.getPoint(i).getPointRef());
        }
    }

    @Test
    public void testAbstractSimplexWithZeroSteps() {
        try {
            new AbstractSimplex(new double[0]);
            fail("Should throw ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVertices() {
        try {
            new AbstractSimplex(new double[]{-1.0, -1.0, 0.0, 0.0});
            fail("Should throw MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIterate() {
        // Stub MultivariateFunction and Comparator
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = mock(Comparator.class);
        when(function.value(anyArray())).thenReturn(0.0);
        when(comparator.compare(any(PointValuePair.class), any(PointValuePair.class))).thenReturn(0);

        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(function, comparator);

        verify(function, times(4)).value(anyArray());
        verifyNoMoreInteractions(function);
        verify(comparator, times(4)).compare(any(PointValuePair.class), any(PointValuePair.class));
        verifyNoMoreInteractions(comparator);
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 1.0};
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(startPoint);

        for (int i = 0; i < 4; ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            if (i == 0) {
                assertArrayEquals(startPoint, vertex.getPointRef());
            } else {
                double[] point = new double[2];
                System.arraycopy(startPoint, 0, point, 0, 2);
                int index = i - 1;
                point[index] += simplex.getStartConfiguration()[index][index];
                assertArrayEquals(point, vertex.getPointRef());
            }
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction function = mock(MultivariateFunction.class);
        Comparator<PointValuePair> comparator = new PointValuePairComparator();

        double[][] startConfiguration = {{1.0, 0.0}, {0.0, 1.0}, {-1.0, -1.0}};
        AbstractSimplex simplex = new AbstractSimplex(3, startConfiguration);
        simplex.evaluate(function, comparator);

        for (int i = 0; i < 4; ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            when(function.value(vertex.getPointRef())).thenReturn(i + 1.0);
        }

        for (int i = 0; i < 4; ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            assertEquals(i + 1.0, vertex.getValue(), 0.001);
        }

        verify(function, times(4)).value(anyArray());
        verifyNoMoreInteractions(function);
        verify(comparator, times(4)).compare(any(PointValuePair.class), any(PointValuePair.class));
        verifyNoMoreInteractions(comparator);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair worstPoint = new PointValuePair(new double[3], 10.0);
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 0.0);

        simplex.replaceWorstPoint(worstPoint, new PointValuePairComparator());

        PointValuePair oldWorstPoint = simplex.getPoint(2);
        assertArrayEquals(worstPoint.getPointRef(), oldWorstPoint.getPointRef());
        assertEquals(worstPoint.getValue(), oldWorstPoint.getValue(), 0.001);

        PointValuePair newWorstPoint = simplex.getPoint(2);
        assertArrayEquals(newPoint.getPointRef(), newWorstPoint.getPointRef());
        assertEquals(newPoint.getValue(), newWorstPoint.getValue(), 0.001);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = simplex.getPoints();

        for (int i = 0; i < 4; ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            assertEquals(vertex, points[i]);
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        simplex.setPoint(1, point);

        assertEquals(point, simplex.getPoint(1));
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 0.0);

        simplex.setPoint(1, point);

        PointValuePair vertex = simplex.getPoint(1);
        assertArrayEquals(point.getPointRef(), vertex.getPointRef());
        assertEquals(point.getValue(), vertex.getValue(), 0.001);
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair[] points = new PointValuePair[4];

        for (int i = 0; i < 4; ++i) {
            points[i] = new PointValuePair(new double[]{i, i}, i * 2.0);
        }

        simplex.setPoints(points);

        for (int i = 0; i < 4; ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            assertEquals(points[i], vertex);
        }
    }

    @Test
    public void testCreateHypercubeSteps() {
        int n = 5;
        double[] steps = AbstractSimplex.createHypercubeSteps(n, 1.0);

        for (int i = 0; i < n; ++i) {
            assertEquals(1.0, steps[i], 0.001);
        }
    }
}