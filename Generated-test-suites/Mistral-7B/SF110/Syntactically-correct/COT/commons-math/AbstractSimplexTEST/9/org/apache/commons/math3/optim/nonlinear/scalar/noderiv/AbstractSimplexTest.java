package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

    private int dimension;
    private AbstractSimplex simplex;

    @Parameterized.Parameters(name = "Testing AbstractSimplex with dimension={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1},
                {2},
                {3}
        });
    }

    @Parameterized.Constructor
    public AbstractSimplexTest(int dimension) {
        this.dimension = dimension;
    }

    @Test
    public void testAbstractSimplexConstructorWithInt() {
        simplex = new AbstractSimplex(dimension);
        assertEquals(dimension, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNull() {
        simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithZeroDimension() {
        simplex = new AbstractSimplex(0);
    }

    @Test
    public void testAbstractSimplexConstructorWithIntAndSideLength() {
        simplex = new AbstractSimplex(dimension, 1.0);
        assertEquals(dimension, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexConstructorWithNullStartConfiguration() {
        simplex = new AbstractSimplex(dimension, null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithEmptyStartConfiguration() {
        simplex = new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexConstructorWithZeroStep() {
        double[] steps = new double[dimension];
        Arrays.fill(steps, 0.0);
        simplex = new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithInvalidStartConfigurationLength() {
        double[] steps = new double[dimension + 1];
        simplex = new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexConstructorWithInvalidStartConfigurationDimension() {
        double[][] referenceSimplex = new double[dimension][dimension];
        simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithEqualVerticesInSimplex() {
        double[][] referenceSimplex = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            Arrays.fill(referenceSimplex[i], 0.0);
        }
        simplex = new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        assertEquals(dimension, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // TODO: Implement this test
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[dimension];
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < dimension + 1; i++) {
            PointValuePair point = points[i];
            assertEquals(startPoint, point.getPointRef());
        }
    }

    @Test
    public void testEvaluate() {
        // TODO: Implement this test
    }

    @Test
    public void testReplaceWorstPoint() {
        // TODO: Implement this test
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = simplex.getPoints();
        assertEquals(dimension + 1, points.length);
        for (int i = 0; i < dimension + 1; i++) {
            PointValuePair point = points[i];
            assertNotNull(point.getPointRef());
            assertNull(point.getValue());
        }
    }

    @Test
    public void testGetPoint() {
        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point.getPointRef());
        assertNull(point.getValue());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange2() {
        simplex.getPoint(dimension);
    }

    @Test
    public void testSetPoint() {
        PointValuePair point = new PointValuePair(new double[dimension], 1.0);
        simplex.setPoint(0, point);
        PointValuePair newPoint = simplex.getPoint(0);
        assertArrayEquals(point.getPointRef(), newPoint.getPointRef());
        assertEquals(point.getValue(), newPoint.getValue());
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange() {
        PointValuePair point = new PointValuePair(new double[dimension], 1.0);
        simplex.setPoint(-1, point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointOutOfRange2() {
        PointValuePair point = new PointValuePair(new double[dimension], 1.0);
        simplex.setPoint(dimension, point);
    }

    @Test
    public void testSetPoints() {
        PointValuePair[] newPoints = new PointValuePair[dimension + 1];
        for (int i = 0; i < dimension + 1; i++) {
            newPoints[i] = new PointValuePair(new double[dimension], i);
        }
        simplex.setPoints(newPoints);
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < dimension + 1; i++) {
            PointValuePair point = points[i];
            assertArrayEquals(newPoints[i].getPointRef(), point.getPointRef());
            assertEquals(newPoints[i].getValue(), point.getValue());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsWithInvalidLength() {
        PointValuePair[] newPoints = new PointValuePair[dimension];
        simplex.setPoints(newPoints);
    }

}