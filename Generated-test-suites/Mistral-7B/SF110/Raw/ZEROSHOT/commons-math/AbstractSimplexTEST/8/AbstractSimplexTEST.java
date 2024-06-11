package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.multivariate.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionsMismatchException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.analysis.Pair;
import org.apache.commons.math3.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstruction() {
        int dimension = 3;
        AbstractSimplex abstractSimplex1 = new AbstractSimplex(dimension);
        AbstractSimplex abstractSimplex2 = new AbstractSimplex(dimension, 2.0);
        AbstractSimplex abstractSimplex3 = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        AbstractSimplex abstractSimplex4 = new AbstractSimplex(new double[][]{
            {0.1, 0.2, 0.3},
            {0.4, 0.5, 0.6},
            {0.7, 0.8, 0.9}
        });

        assertEquals(dimension, abstractSimplex1.getDimension());
        assertEquals(dimension + 1, abstractSimplex1.getSize());
        assertEquals(dimension, abstractSimplex2.getDimension());
        assertEquals(dimension + 1, abstractSimplex2.getSize());
        assertEquals(dimension, abstractSimplex3.getDimension());
        assertEquals(dimension + 1, abstractSimplex3.getSize());
        assertEquals(dimension, abstractSimplex4.getDimension());
        assertEquals(dimension + 1, abstractSimplex4.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullArguments() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroArguments() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidParameters() {
        new AbstractSimplex(-1);
        new AbstractSimplex(-1.0);
        new AbstractSimplex(new double[]{0.0, 1.0}, -1);
        new AbstractSimplex(new double[][]{
            {0.1, 0.2},
            {0.1, 0.3}
        }, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidStartConfiguration() {
        new AbstractSimplex(new double[]{0.1, 0.2, 0.3}, new double[2][3]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexReferenceSimplexInvalidDimensions() {
        new AbstractSimplex(new double[][]{
            {0.1, 0.2},
            {0.1, 0.2, 0.3}
        });
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexInvalidReferenceSimplex() {
        new AbstractSimplex(new double[][]{
            {0.1, 0.2},
            {0.1, 0.2},
            {0.1, 0.2}
        });
    }

    @Test
    public void testAbstractSimplexGetters() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});

        assertEquals(dimension, abstractSimplex.getDimension());
        assertEquals(dimension + 1, abstractSimplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterateMethod() {
        // Implement this method in your AbstractSimplex class and provide a test case here.
    }

    @Test
    public void testAbstractSimplexBuildMethod() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(dimension);
        double[] startPoint = new double[]{0.1, 0.2, 0.3};

        abstractSimplex.build(startPoint);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertNotNull(points);
        assertEquals(dimension + 1, points.length);

        PointValuePair point1 = points[0];
        assertEquals(startPoint, point1.getPointRef());
        assertNull(point1.getValue());

        for (int i = 1; i < points.length; i++) {
            PointValuePair pointI = points[i];
            double[] pointIRef = pointI.getPointRef();
            assertEquals(startPoint, pointIRef);
            assertNull(pointI.getValue());
            for (int j = 0; j < i; j++) {
                double[] pointJRef = points[j].getPointRef();
                assertArrayEquals(pointIRef, pointJRef);
                assertEquals(pointIRef[0], pointJRef[0]);
                for (int k = 1; k < dimension; k++) {
                    assertEquals(pointIRef[k] - pointJRef[k], 0.0, 0.001);
                }
            }
        }
    }

    @Test
    public void testAbstractSimplexEvaluateMethod() {
        // Implement this method in your AbstractSimplex class and provide a test case here.
    }

    @Test
    public void testAbstractSimplexReplaceWorstPointMethod() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        PointValuePair pointValuePairNew = new PointValuePair(new double[]{0.6, 0.7, 0.8}, 5.5);

        abstractSimplex.replaceWorstPoint(pointValuePairNew, null);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(dimension + 1, points.length);
        PointValuePair pointWorst = points[dimension];
        assertEquals(pointValuePairNew, pointWorst);
    }

    @Test
    public void testAbstractSimplexGetPointMethod() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});

        PointValuePair point = abstractSimplex.getPoint(0);
        assertEquals(new double[]{0.5, 1.5, 2.5}, point.getPointRef());
        assertNull(point.getValue());

        point = abstractSimplex.getPoint(dimension);
        assertNull(point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointOutOfRange() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        abstractSimplex.getPoint(-1);
    }

    @Test
    public void testAbstractSimplexSetPointMethod() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        PointValuePair pointValuePairNew = new PointValuePair(new double[]{0.6, 0.7, 0.8}, 5.5);

        abstractSimplex.setPoint(0, pointValuePairNew);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(dimension + 1, points.length);
        PointValuePair pointNew = points[0];
        assertEquals(pointValuePairNew, pointNew);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointOutOfRange() {
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        abstractSimplex.setPoint(-1, new PointValuePair(new double[]{0.6, 0.7, 0.8}, 5.5));
    }

    @Test
    public void testAbstractSimplexSetPointsMethod() {
        int dimension = 3;
        AbstractSimplex abstractSimplex = new AbstractSimplex(new double[]{0.5, 1.5, 2.5});
        PointValuePair[] newPoints = new PointValuePair[dimension + 1];

        for (int i = 0; i < newPoints.length; i++) {
            newPoints[i] = new PointValuePair(new double[]{i * 0.1, i * 0.2, i * 0.3}, i * 3.0);
        }

        abstractSimplex.setPoints(newPoints);

        PointValuePair[] points = abstractSimplex.getPoints();
        assertEquals(dimension + 1, points.length);
        for (int i = 0; i < points.length; i++) {
            PointValuePair pointI = points[i];
            assertEquals(newPoints[i], pointI);
        }
    }
}