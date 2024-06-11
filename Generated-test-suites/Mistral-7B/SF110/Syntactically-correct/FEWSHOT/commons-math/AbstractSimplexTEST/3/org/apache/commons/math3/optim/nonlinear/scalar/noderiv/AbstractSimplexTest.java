package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderim.SimplexIterationException;
import org.apache.commons.math3.optim.point.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

    private final int dimension;
    private final AbstractSimplex simplex;

    public AbstractSimplexTest(int dimension) {
        this.dimension = dimension;
        this.simplex = new Simplex(dimension);
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1},
                {2},
                {3},
                {10}
        });
    }

    @Test
    public void testGetDimension() {
        assertEquals("Getting wrong dimension", dimension, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals("Getting wrong size", dimension + 1, simplex.getSize());
    }

    @Test
    public void testIterateNullFunction() {
        try {
            simplex.iterate(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIterateNullComparator() {
        try {
            simplex.iterate(new MultivariateFunction() {
                @Override
                public double value(Point point) {
                    return 0.0;
                }

                @Override
                public int getNumberOfVariables() {
                    return dimension;
                }

                @Override
                public void gradient(Point point, double[] grad) {
                    // not implemented
                }

                @Override
                public void valueGradient(Point point, double[] grad) {
                    // not implemented
                }

                @Override
                public void hessian(Point point, double[][] hessian) {
                    // not implemented
                }

                @Override
                public PointValuePair gradientDescentStep(Point point, double stepSize) {
                    return null;
                }

                @Override
                public PointValuePair minimize(Point startPoint, double stepSize, GoalType goalType, Comparator<PointValuePair> comparator, List<LinearConstraint> constraints) {
                    return null;
                }
            }, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[dimension];
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertArrayEquals("Expected the same points", startPoint, points[0].getPointRef());
        for (int i = 1; i < points.length; ++i) {
            double[] point = points[i].getPointRef();
            for (int j = 0; j < dimension; ++j) {
                assertEquals("Expected all points to have the same dimensions", startPoint[j], point[j], 0.001);
            }
        }
    }

    @Test
    public void testEvaluateNullFunction() {
        try {
            simplex.evaluate(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateNullComparator() {
        LinearObjectiveFunction linearFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-2.0, -1.0});
        try {
            simplex.evaluate(linearFunction, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointNullPointValuePair() {
        try {
            simplex.replaceWorstPoint(null, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPointNullComparator() {
        try {
            simplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 2.0}, Double.POSITIVE_INFINITY), null);
            fail("Expected a SimplexIterationException");
        } catch (SimplexIterationException e) {
            // expected
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        double[] p1 = new double[]{1.0, 2.0};
        double[] p2 = new double[]{3.0, 4.0};
        PointValuePair worstPoint = new PointValuePair(p1, 10.0);
        simplex.replaceWorstPoint(new PointValuePair(p2, 5.0), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return (int) (Double.compare(o1.getValue(), o2.getValue()));
            }
        });
        PointValuePair newWorstPoint = simplex.getPoint(dimension);
        assertArrayEquals("Expected the new worst point", p2, newWorstPoint.getPointRef());
        assertEquals("Expected the new worst point value", 5.0, newWorstPoint.getValue(), 0.001);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointNegativeIndex() {
        simplex.setPoint(-1, new PointValuePair(new double[]{1.0, 2.0}, 3.0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPointIndexOutOfRange() {
        simplex.setPoint(dimension + 1, new PointValuePair(new double[]{1.0, 2.0}, 3.0));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsNull() {
        simplex.setPoints(null);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPointsIncorrectSize() {
        simplex.setPoints(new PointValuePair[dimension + 2]);
    }
}