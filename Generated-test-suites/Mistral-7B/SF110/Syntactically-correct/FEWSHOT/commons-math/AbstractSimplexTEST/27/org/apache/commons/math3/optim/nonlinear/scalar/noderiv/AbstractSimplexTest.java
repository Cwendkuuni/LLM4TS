package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.multivariate.MultivariateFunction;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.initialguess.SimplexInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleX;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.PointValuePairComparator;

import java.util.Arrays;

public class AbstractSimplexTest {
    @Test
    public void testAbstractSimplex() {
        assertEquals(0, new AbstractSimplex(0).getDimension());
        assertEquals(1, new AbstractSimplex(1).getDimension());
        assertEquals(2, new AbstractSimplex(2).getDimension());
        AbstractSimplex simplex1 = new AbstractSimplex(2, 1.0);
        assertEquals(2, simplex1.getDimension());
        assertEquals(3, simplex1.getSize());
        AbstractSimplex simplex2 = new AbstractSimplex(new double[]{1.0, 1.0, 1.0});
        assertEquals(3, simplex2.getDimension());
        assertEquals(4, simplex2.getSize());
        AbstractSimplex simplex3 = new AbstractSimplex(new double[][]{
                {0.0, 0.0},
                {1.0, 0.0},
                {0.5, 0.5}
        });
        assertEquals(2, simplex3.getDimension());
        assertEquals(3, simplex3.getSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroSteps() {
        new AbstractSimplex(new double[]{0.0});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroSizeSimpleX() {
        new SimpleX(0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDimensionMismatchReferenceSimpleX() {
        new AbstractSimplex(new double[][]{
                {0.0, 0.0},
                {1.0, 0.0},
                {1.0, 1.0}
        });
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesInSimpleX() {
        new AbstractSimplex(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 2.0};
        AbstractSimplex simplex = new SimpleX(3);
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        for (PointValuePair point : points) {
            assertNotNull(point.getPointRef());
        }
        for (int i = 0; i < points.length; ++i) {
            PointValuePair expectedPoint = new PointValuePair(Arrays.copyOfRange(startPoint, 0, i + 1));
            assertEquals(expectedPoint.getPoint(), points[i].getPointRef());
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(evaluationFunction, new PointValuePairComparator());
        double[][] expectedPoints = new double[][]{
                {1.0, 1.0},
                {0.0, 2.0},
                {1.0, 0.0},
                {2.0, 1.0}
        };
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] expectedPoint = expectedPoints[i];
            assertArrayEquals(expectedPoint, point.getPointRef());
        }
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double expectedValue = evaluationFunction.value(expectedPoints[i]);
            assertEquals(expectedValue, point.getValue(), 1e-6);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, new PointValuePairComparator());
        PointValuePair worstPoint = simplex.getPoint(0);
        double[] worstPointValue = worstPoint.getPointRef();
        simplex.replaceWorstPoint(new PointValuePair(new double[]{3.0, 3.0}, 100.0), new PointValuePairComparator());
        PointValuePair newWorstPoint = simplex.getPoint(0);
        double[] newWorstPointValue = newWorstPoint.getPointRef();
        assertTrue(Arrays.equals(new double[]{3.0, 3.0}, newWorstPointValue));
        assertTrue(worstPointValue[0] < newWorstPointValue[0]);
        assertTrue(worstPointValue[1] < newWorstPointValue[1]);
    }

    @Test
    public void testGetPoints() {
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, new PointValuePairComparator());
        PointValuePair[] expectedPoints = new PointValuePair[]{
                new PointValuePair(new double[]{1.0, 1.0}, Double.NaN),
                new PointValuePair(new double[]{0.0, 2.0}, Double.NaN),
                new PointValuePair(new double[]{1.0, 0.0}, Double.NaN),
                new PointValuePair(new double[]{2.0, 1.0}, Double.NaN)
        };
        PointValuePair[] actualPoints = simplex.getPoints();
        for (int i = 0; i < actualPoints.length; ++i) {
            assertEquals(expectedPoints[i].getPoint(), actualPoints[i].getPointRef());
        }
    }

    @Test
    public void testGetPoint() {
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, new PointValuePairComparator());
        PointValuePair point = simplex.getPoint(1);
        double[] expectedPoint = new double[]{0.0, 2.0};
        assertArrayEquals(expectedPoint, point.getPointRef());
    }

    @Test
    public void testSetPoint() {
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, new PointValuePairComparator());
        PointValuePair point = new PointValuePair(new double[]{3.0, 3.0}, 100.0);
        simplex.setPoint(1, point);
        double[] expectedPoint = new double[]{3.0, 3.0};
        PointValuePair actualPoint = simplex.getPoint(1);
        assertArrayEquals(expectedPoint, actualPoint.getPointRef());
        assertEquals(100.0, actualPoint.getValue(), 1e-6);
    }

    @Test
    public void testSetPoints() {
        SimpleX simplex = new SimpleX(3);
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(new ObjectiveFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        }, new PointValuePairComparator());
        PointValuePair[] newPoints = new PointValuePair[]{
                new PointValuePair(new double[]{1.5, 1.5}, Double.NaN),
                new PointValuePair(new double[]{2.5, 2.5}, Double.NaN),
                new PointValuePair(new double[]{0.5, 0.5}, Double.NaN)
        };
        simplex.setPoints(newPoints);
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair expectedPoint = newPoints[i];
            PointValuePair actualPoint = simplex.getPoint(i);
            assertArrayEquals(expectedPoint.getPoint(), actualPoint.getPointRef());
        }
    }
}