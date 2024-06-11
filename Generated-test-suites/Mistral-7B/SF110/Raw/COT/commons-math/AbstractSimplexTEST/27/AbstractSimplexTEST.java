package org.apache.commons.math3.optim.nonlinear.scalar.noderiv.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testConstructor_Int() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertNull(simplex.getPoints());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_ZeroSteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_ZeroVertex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] {0.0});
        assertEquals(1, simplex.getDimension());
        assertNull(simplex.getPoints());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testConstructor_EqualVertices() {
        AbstractSimplex simplex = new AbstractSimplex(new double[] {0.0, 0.0});
        assertEquals(2, simplex.getDimension());
        assertNull(simplex.getPoints());
    }

    @Test
    public void testConstructor_ReferenceSimplex() {
        double[][] referenceSimplex = {
            {1.0, 2.0},
            {2.0, 3.0},
            {3.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        PointValuePair[] points = simplex.getPoints();
        assertEquals(3, points.length);
        assertEquals(new PointValuePair(new double[] {1.0, 2.0}, 0.0), points[0]);
        assertEquals(new PointValuePair(new double[] {2.0, 3.0}, Double.NaN), points[1]);
        assertEquals(new PointValuePair(new double[] {3.0, 1.0}, Double.NaN), points[2]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_EmptyReferenceSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[0][0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_MismatchedReferenceSimplexDimensions() {
        double[][] referenceSimplex = {
            {1.0, 2.0},
            {2.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_MismatchedReferenceSimplexCorrespondingVertices() {
        double[][] referenceSimplex = {
            {1.0, 2.0},
            {2.0, 1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(4, simplex.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterate_NullFunction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIterate_NullComparator() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        }, null);
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return 0.0;
            }
        };
        simplex.iterate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(new PointValuePair(new double[] {0.0, 0.0}, 0.0), points[0]);
        for (int i = 1; i < 4; ++i) {
            assertTrue(Double.isNaN(points[i].getValue()));
        }
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[] {1.0, 2.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(new PointValuePair(new double[] {1.0, 2.0}, 0.0), points[0]);
        for (int i = 1; i < 4; ++i) {
            assertEquals(startPoint[i], points[i].getPointRef()[0]);
            assertTrue(Double.isNaN(points[i].getValue()));
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.sin(point[0]) * Math.sin(point[1]);
            }
        };
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(new PointValuePair(new double[] {0.0, 0.0}, Math.sin(0.0) * Math.sin(0.0), false), points[0]);
        for (int i = 1; i < 4; ++i) {
            assertTrue(Double.isNaN(points[i].getValue()));
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair pointValuePair = new PointValuePair(new double[] {1.0, 1.0}, -1.0);
        simplex.replaceWorstPoint(pointValuePair, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(pointValuePair, points[3]);
        assertEquals(new PointValuePair(new double[] {0.0, 0.0}, 0.0), points[0]);
        for (int i = 1; i < 3; ++i) {
            assertTrue(Double.isNaN(points[i].getValue()));
        }
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (int i = 0; i < 4; ++i) {
            assertTrue(points[i] instanceof PointValuePair);
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair point = simplex.getPoint(0);
        assertEquals(new PointValuePair(new double[] {0.0, 0.0}, 0.0), point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_NegativeIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_GreaterIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        simplex.getPoint(4);
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair point = new PointValuePair(new double[] {1.0, 1.0}, Double.NaN);
        simplex.setPoint(0, point);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        assertEquals(point, points[0]);
        assertTrue(Double.isNaN(points[1].getValue()));
        assertTrue(Double.isNaN(points[2].getValue()));
        assertTrue(Double.isNaN(points[3].getValue()));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_NegativeIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        simplex.setPoint(-1, new PointValuePair(new double[] {1.0, 1.0}, Double.NaN));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_GreaterIndex() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        simplex.setPoint(4, new PointValuePair(new double[] {1.0, 1.0}, Double.NaN));
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair[] points = new PointValuePair[] {
            new PointValuePair(new double[] {1.0, 1.0}, Double.NaN),
            new PointValuePair(new double[] {2.0, 2.0}, Double.NaN),
            new PointValuePair(new double[] {3.0, 3.0}, Double.NaN)
        };
        simplex.setPoints(points);
        PointValuePair[] simplexPoints = simplex.getPoints();
        assertEquals(4, simplexPoints.length);
        for (int i = 0; i < 4; ++i) {
            assertEquals(points[i], simplexPoints[i]);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_MismatchedPointsDimension() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] {0.0, 0.0});
        PointValuePair[] points = new PointValuePair[] {
            new PointValuePair(new double[] {1.0}, Double.NaN),
            new PointValuePair(new double[] {1.0, 1.0}, Double.NaN),
            new PointValuePair(new double[] {1.0, 1.0, 1.0}, Double.NaN)
        };
        simplex.setPoints(points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(3, 1.0);
        assertEquals(3, steps.length);
        assertEquals(1.0, steps[0], 0.0);
        assertEquals(1.0, steps[1], 0.0);
        assertEquals(1.0, steps[2], 0.0);
    }
}