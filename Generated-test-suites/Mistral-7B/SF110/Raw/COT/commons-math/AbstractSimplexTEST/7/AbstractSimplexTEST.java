package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.go.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.go.PointValuePair;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testConstructor_int() {
        try {
            new AbstractSimplex(5);
        } catch (Exception e) {
            fail("Expected no exception when creating a new instance with an int argument.");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_intNull() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_intZero() {
        new AbstractSimplex(0);
    }

    @Test
    public void testConstructor_intWithSideLength() {
        try {
            new AbstractSimplex(5, 1.0);
        } catch (Exception e) {
            fail("Expected no exception when creating a new instance with an int and a double argument.");
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_intArrayNull() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_intArrayZero() {
        new AbstractSimplex(new double[]{0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_intArrayInvalidLength() {
        new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_doubleArrayInvalidDimension() {
        new AbstractSimplex(new double[][]{
                new double[]{1.0, 2.0},
                new double[]{1.0, 1.0},
                new double[]{1.0, 1.0}
        });
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_doubleArrayEqualVertices() {
        new AbstractSimplex(new double[][]{
                new double[]{1.0, 2.0},
                new double[]{1.0, 2.0}
        });
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        assertEquals(5, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        assertEquals(6, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateNullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIterateNullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new MultivariateFunction() {}, null);
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        simplex.iterate(function, comparator);

        PointValuePair worstPoint = simplex.getPoint(5);
        assertNotNull(worstPoint);
        assertNotNull(worstPoint.getValue());
        assertEquals(-15.0, worstPoint.getValue(), 0.001);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);

        PointValuePair worstPoint = simplex.getPoint(5);
        assertNotNull(worstPoint);
        assertNotNull(worstPoint.getValue());
        assertEquals(-Double.POSITIVE_INFINITY, worstPoint.getValue(), 0.0);

        for (int i = 0; i < simplex.getDimension(); ++i) {
            PointValuePair point = simplex.getPoint(i + 1);
            double[] pointValues = point.getPointRef();
            assertEquals(startPoint[i], pointValues[i], 0.0);
            assertEquals(Double.NaN, point.getValue(), 0.0);
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        simplex.build(new double[]{1.0, 2.0});
        simplex.evaluate(function, comparator);

        PointValuePair bestPoint = simplex.getPoint(0);
        assertNotNull(bestPoint);
        assertNotNull(bestPoint.getValue());
        assertEquals(0.0, bestPoint.getValue(), 0.001);

        for (int i = 1; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointValues = point.getPointRef();
            double expectedValue = Math.pow(pointValues[0], 2) + Math.pow(pointValues[1], 2);
            assertNotNull(point);
            assertNotNull(point.getValue());
            assertEquals(expectedValue, point.getValue(), 0.001);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 2.0}, 1.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair newWorstPoint = simplex.getPoint(5);
        assertEquals(newPoint, newWorstPoint);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(simpleX.getSize(), points.length);
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair point = simplex.getPoint(0);
        assertNotNull(point);
        assertNotNull(point.getValue());
        assertEquals(0.0, point.getValue(), 0.001);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair point = simplex.getPoint(0);
        double[] newPointValues = {2.0, 3.0};
        simplex.setPoint(0, new PointValuePair(newPointValues, 25.0));

        PointValuePair newPoint = simplex.getPoint(0);
        assertNotNull(newPoint);
        assertNotNull(newPoint.getValue());
        assertEquals(25.0, newPoint.getValue(), 0.001);
        assertArrayEquals(newPointValues, newPoint.getPointRef());

        PointValuePair oldPoint = simplex.getPoint(1);
        assertNotNull(oldPoint);
        assertNotNull(oldPoint.getValue());
        assertEquals(0.0, oldPoint.getValue(), 0.001);
        assertArrayEquals(new double[]{1.0, 2.0}, oldPoint.getPointRef());
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0});
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        }, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });

        PointValuePair[] newPoints = new PointValuePair[]{
                new PointValuePair(new double[]{3.0, 4.0}, 100.0),
                new PointValuePair(new double[]{5.0, 6.0}, 200.0),
                new PointValuePair(new double[]{7.0, 8.0}, 300.0)
        };

        simplex.setPoints(newPoints);

        PointValuePair[] points = simplex.getPoints();
        assertNotNull(points);
        assertEquals(newPoints.length, points.length);

        for (int i = 0; i < newPoints.length; ++i) {
            PointValuePair point = points[i];
            PointValuePair newPoint = newPoints[i];
            assertEquals(newPoint, point);
        }
    }
}