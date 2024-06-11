package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.*;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConstructorArg() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConstructorArg() {
        new AbstractSimplex(0);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConstructorArgArray() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidNumberOfVertices() {
        new AbstractSimplex(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidNumberOfVerticesWithLength() {
        new AbstractSimplex(new double[]{-1.0});
    }

    @Test
    public void testAbstractSimplexValidConstructor() {
        int dimension = 3;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension, simplex.getDimension());
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexValidConstructorWithSideLength() {
        int dimension = 3;
        AbstractSimplex simplex = new AbstractSimplex(dimension, 1.0);
        assertEquals(dimension, simplex.getDimension());
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexValidConstructorWithSteps() {
        int dimension = 3;
        double[] steps = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(dimension, simplex.getDimension());
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInvalidReferenceSimplexDimension() {
        int dimension = 3;
        double[][] referenceSimplex = new double[2][dimension];
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplexSize() {
        int dimension = 3;
        double[][] referenceSimplex = new double[0][dimension];
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexReferenceSimplexMismatchedDimensions() {
        int dimension = 3;
        double[][] referenceSimplex = new double[2][dimension + 1];
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexReferenceSimplexValidConstructor() {
        int dimension = 3;
        double[][] referenceSimplex = new double[][]{
                {0.0, 0.0, 0.0},
                {1.0, 1.0, 1.0},
                {2.0, 2.0, 2.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(dimension, simplex.getDimension());
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testGetDimension() {
        int dimension = 3;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        int dimension = 3;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIterateNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testBuildValidConstructor() {
        int dimension = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(startPoint);
        assertEquals(dimension + 1, simplex.getSize());
        for (PointValuePair pointValuePair : simplex.getPoints()) {
            double[] point = pointValuePair.getPointRef();
            assertNotNull(point);
            for (int i = 0; i < dimension; ++i) {
                assertEquals(startPoint[i] + simplex.startConfiguration[i][i], point[i]);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildInvalidStartPointDimension() {
        int dimension = 3;
        double[] startPoint = new double[]{1.0, 2.0};
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluateValidConstructor() {
        int dimension = 2;
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) - Math.pow(Math.sin(Math.PI / 4.0), 2.0) * Math.pow(Math.cos(Math.PI / 4.0), 2.0);
            }
        };
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(function, null);
        for (PointValuePair pointValuePair : simplex.getPoints()) {
            double[] point = pointValuePair.getPointRef();
            double value = pointValuePair.getValue();
            assertEquals(function.value(point), value, 1e-9);
        }
    }

    @Test
    public void testReplaceWorstPointValidConstructor() {
        int dimension = 2;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) - Math.pow(Math.sin(Math.PI / 4.0), 2.0) * Math.pow(Math.cos(Math.PI / 4.0), 2.0);
            }
        }, null);
        PointValuePair worstPoint = simplex.getPoints()[0];
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0}, Double.NaN, false);
        simplex.replaceWorstPoint(newPoint, null);
        assertNotEquals(worstPoint, simplex.getPoints()[0]);
    }

    @Test
    public void testGetPointsValidConstructor() {
        int dimension = 2;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) - Math.pow(Math.sin(Math.PI / 4.0), 2.0) * Math.pow(Math.cos(Math.PI / 4.0), 2.0);
            }
        }, null);
        PointValuePair[] points = simplex.getPoints();
        for (PointValuePair pointValuePair : points) {
            double[] point = pointValuePair.getPointRef();
            double value = pointValuePair.getValue();
            assertEquals(4.0, value, 1e-9);
        }
    }

    @Test
    public void testGetPointValidConstructor() {
        int dimension = 2;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) - Math.pow(Math.sin(Math.PI / 4.0), 2.0) * Math.pow(Math.cos(Math.PI / 4.0), 2.0);
            }
        }, null);
        PointValuePair point = simplex.getPoint(0);
        double[] pointRef = point.getPointRef();
        assertEquals(0.0, pointRef[0], 1e-9);
        assertEquals(0.0, pointRef[1], 1e-9);
        assertEquals(Double.NaN, point.getValue());
    }

    @Test
    public void testSetPointValidConstructor() {
        int dimension = 2;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[]{0.0, 0.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.PI, 2.0) * Math.pow(Math.E, 2.0) - Math.pow(Math.sin(Math.PI / 4.0), 2.0) * Math.pow(Math.cos(Math.PI / 4.0), 2.0);
            }
        }, null);
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 10.0, false);
        simplex.setPoint(0, point);
        assertEquals(1.0, simplex.getPoint(0).getPointRef()[0], 1e-9);
        assertEquals(1.0, simplex.getPoint(0).getPointRef()[1], 1e-9);
        assertEquals(10.0, simplex.getPoint(0).getValue(), 1e-9);
    }

    @Test
    public void testSetPointsValidConstructor() {
        int dimension = 2;
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        PointValuePair[] points = new PointValuePair[3];
        for (int i = 0; i < 3; ++i) {
            points[i] = new PointValuePair(new double[]{i, i}, i * 2.0, false);
        }
        simplex.setPoints(points);
        for (int i = 0; i < 3; ++i) {
            assertEquals(i, simplex.getPoint(i).getPointRef()[0], 1e-9);
            assertEquals(i * 2.0, simplex.getPoint(i).getValue(), 1e-9);
        }
    }
}