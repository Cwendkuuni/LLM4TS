package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        int dim = 3;
        AbstractSimplex simplex = new AbstractSimplex(dim);
        assertEquals(dim, simplex.getDimension());
        assertEquals(dim + 1, simplex.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNull() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZero() {
        new AbstractSimplex(0);
    }

    @Test
    public void testAbstractSimplexWithSteps() {
        int dim = 3;
        double sideLength = 2.0;
        AbstractSimplex simplex = new AbstractSimplex(dim, sideLength);
        assertEquals(dim, simplex.getDimension());
        assertEquals(dim + 1, simplex.getSize());
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroSteps() {
        int dim = 3;
        double[] steps = new double[dim];
        Arrays.fill(steps, 0.0);
        new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithMismatchedSteps() {
        int dim = 3;
        double[] steps = new double[dim + 1];
        new AbstractSimplex(steps);
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplex() {
        int dim = 2;
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {2.0, 1.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(dim, simplex.getDimension());
        assertEquals(dim + 1, simplex.getSize());
        for (int i = 0; i < dim; ++i) {
            double[] expected = referenceSimplex[i];
            double[] actual = simplex.startConfiguration[i];
            assertArrayEquals(expected, actual);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithNegativeReferencePoint() {
        int dim = 2;
        double[][] referenceSimplex = new double[][]{{-1.0, 2.0}, {2.0, -1.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithMismatchedReferencePointDimension() {
        int dim = 2;
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {3.0, 2.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualReferencePoints() {
        int dim = 2;
        double[][] referenceSimplex = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexGetters() {
        int dim = 2;
        AbstractSimplex simplex = new AbstractSimplex(dim);
        assertEquals(dim, simplex.getDimension());
        assertEquals(dim + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexSetters() {
        int dim = 2;
        AbstractSimplex simplex = new AbstractSimplex(dim);
        double[] newPoint = new double[]{1.0, 2.0};
        simplex.setPoint(0, new PointValuePair(newPoint, 0.0));
        PointValuePair expected = new PointValuePair(newPoint, 0.0);
        PointValuePair actual = simplex.getPoint(0);
        assertEquals(expected, actual);

        simplex.setPoints(new PointValuePair[]{new PointValuePair(new double[]{3.0, 4.0}, 5.0), new PointValuePair(new double[]{6.0, 7.0}, 8.0)});
        PointValuePair[] expectedPoints = new PointValuePair[]{new PointValuePair(new double[]{3.0, 4.0}, 5.0), new PointValuePair(new double[]{6.0, 7.0}, 8.0)};
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }
}