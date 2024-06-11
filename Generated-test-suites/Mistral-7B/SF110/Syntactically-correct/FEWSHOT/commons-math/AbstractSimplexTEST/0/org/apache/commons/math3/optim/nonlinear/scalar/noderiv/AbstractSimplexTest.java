package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.MinimizeGoalType;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexIterationData;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleX;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstruction() {
        int n = 3;
        AbstractSimplex simplex = new SimpleX(n);
        assertEquals(n, simplex.getDimension());
        assertEquals(n + 1, simplex.getSize());
        assertNull(simplex.getPoints());

        simplex = new SimpleX(n, 2.0);
        assertEquals(n, simplex.getDimension());
        assertEquals(n + 1, simplex.getSize());
        assertNotNull(simplex.getPoints());

        double[] steps = new double[]{1.0, 1.0, 2.0};
        simplex = new SimpleX(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
        assertNotNull(simplex.getPoints());

        double[][] referenceSimplex = {
            {0.0, 0.0, 0.0},
            {1.0, 0.0, 0.0},
            {0.5, 0.5, 0.0},
            {1.0, 1.0, 0.0},
            {0.5, 0.0, 0.5},
            {0.0, 1.0, 0.5},
            {0.5, 1.0, 0.5}
        };
        simplex = new SimpleX(referenceSimplex);
        assertEquals(3, simplex.getDimension());
        assertEquals(8, simplex.getSize());
        assertNotNull(simplex.getPoints());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullSteps() {
        new SimpleX(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroSteps() {
        new SimpleX(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroInitialConfiguration() {
        new SimpleX(new double[3][0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexNegativeInitialConfiguration() {
        new SimpleX(new double[3][-1]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedReferenceSimpleLength() {
        new SimpleX(new double[3][2]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedReferenceSimpleWidth() {
        new SimpleX(new double[4][3]);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesInSimpleX() {
        double[][] referenceSimple = {
            {0.0, 0.0},
            {1.0, 1.0},
            {1.0, 1.0}
        };
        new SimpleX(referenceSimple);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new SimpleX(3);
        assertEquals(3, simplex.getDimension());

        simplex = new SimpleX(3, 2.0);
        assertEquals(3, simplex.getDimension());

        simplex = new SimpleX(new double[]{1.0, 1.0, 2.0});
        assertEquals(3, simplex.getDimension());

        simplex = new SimpleX(new double[3][3]);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new SimpleX(3);
        assertEquals(4, simplex.getSize());

        simplex = new SimpleX(3, 2.0);
        assertEquals(4, simplex.getSize());

        simplex = new SimpleX(new double[]{1.0, 1.0, 2.0});
        assertEquals(4, simplex.getSize());

        simplex = new SimpleX(new double[3][3]);
        assertEquals(10, simplex.getSize());
    }

    @Test
    public void testSetAndGetPoints() {
        AbstractSimplex simplex = new SimpleX(3);
        PointValuePair[] points = new PointValuePair[10];

        for (int i = 0; i < 10; ++i) {
            points[i] = new PointValuePair(new double[]{i / 3.0, i % 3.0}, i);
        }

        simplex.setPoints(points);
        assertArrayEquals(points, simplex.getPoints());
    }

    @Test
    public void testSetAndGetPoint() {
        AbstractSimplex simplex = new SimpleX(3);
        double[] point = new double[]{1.0 / 3.0, 2.0 / 3.0};
        PointValuePair expectedPoint = new PointValuePair(point, 0.0);

        simplex.setPoint(0, expectedPoint);
        PointValuePair actualPoint = simplex.getPoint(0);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetAndGetPointOutOfRange() {
        AbstractSimplex simplex = new SimpleX(3);
        simplex.setPoint(-1, new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 0.0));
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new SimpleX(3);
        double[] startPoint = new double[]{1.0, 1.0};

        simplex.build(startPoint);
        PointValuePair expectedPoint = new PointValuePair(startPoint, 0.0);
        PointValuePair actualPoint = simplex.getPoint(0);
        assertEquals(expectedPoint, actualPoint);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (int i = 0; i < 4; ++i) {
            PointValuePair expectedPointI = new PointValuePair(new double[]{i / 3.0, i % 3.0}, Double.NaN);
            PointValuePair actualPointI = points[i];
            assertEquals(expectedPointI, actualPointI);
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new SimpleX(3);
        double sideLength = 2.0;

        PointValuePair[] points = new PointValuePair[10];
        for (int i = 0; i < 10; ++i) {
            double[] point = new double[]{i / 3.0, i % 3.0};
            double value = Math.pow(i, 2.0);
            points[i] = new PointValuePair(point, value);
        }

        simplex.setPoints(points);
        MultivariateFunction evaluationFunction = new FunctionWithGradient() {
            @Override
            public double value(double[] point) {
                return point[0] * point[1];
            }

            @Override
            public double[] gradient(double[] point) {
                return new double[]{point[1], point[0]};
            }
        };

        simplex.evaluate(evaluationFunction, new MinimizeGoalType());
        PointValuePair[] expectedPoints = new PointValuePair[10];
        for (int i = 0; i < 10; ++i) {
            double[] point = new double[]{i / 3.0, i % 3.0};
            double value = Math.pow(i, 2.0);
            expectedPoints[i] = new PointValuePair(point, value);
        }

        PointValuePair[] actualPoints = simplex.getPoints();
        assertArrayEquals(expectedPoints, actualPoints);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new SimpleX(3);
        double[] point = new double[]{1.0 / 3.0, 2.0 / 3.0};
        PointValuePair expectedPoint = new PointValuePair(point, 0.0);
        PointValuePair worstPoint = new PointValuePair(new double[]{2.0 / 3.0, 1.0 / 3.0}, Double.POSITIVE_INFINITY);

        simplex.setPoint(0, expectedPoint);
        simplex.setPoint(1, new PointValuePair(new double[]{0.5, 0.5}, Double.NaN));
        simplex.setPoint(2, worstPoint);

        simplex.replaceWorstPoint(expectedPoint, new MinimizeGoalType());

        PointValuePair actualPoint = simplex.getPoint(2);
        assertEquals(expectedPoint, actualPoint);
    }

    @Test(expected = NullPointerException.class)
    public void testReplaceWorstPointNullComparator() {
        AbstractSimplex simplex = new SimpleX(3);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, 0.0), null);
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new SimpleX(3);
        MultivariateFunction evaluationFunction = new FunctionWithGradient() {
            @Override
            public double value(double[] point) {
                return point[0] * point[1];
            }

            @Override
            public double[] gradient(double[] point) {
                return new double[]{point[1], point[0]};
            }
        };

        simplex.iterate(evaluationFunction, new MinimizeGoalType());

        PointValuePair[] expectedPoints = new PointValuePair[10];
        for (int i = 0; i < 10; ++i) {
            double[] point = new double[]{i / 3.0, i % 3.0};
            double value = Math.pow(i, 2.0);
            expectedPoints[i] = new PointValuePair(point, value);
        }

        PointValuePair[] actualPoints = simplex.getPoints();
        assertArrayEquals(expectedPoints, actualPoints);
    }
}