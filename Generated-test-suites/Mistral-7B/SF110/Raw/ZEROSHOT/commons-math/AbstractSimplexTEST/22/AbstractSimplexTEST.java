package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithNullConstructor() {
        try {
            new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroConstructor() {
        try {
            new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEmptySteps() {
        try {
            new AbstractSimplex(new double[0]);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithNonZeroSteps() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, test.getDimension());
        assertNotNull(test.startConfiguration);
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplex() {
        double[][] referenceSimplex = {
            {1.0, 1.0},
            {2.0, 2.0},
            {3.0, 3.0}
        };
        AbstractSimplex test = new AbstractSimplex(referenceSimplex);
        assertEquals(2, test.getDimension());
        assertNotNull(test.startConfiguration);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithNegativeSteps() {
        new AbstractSimplex(new double[]{-1.0, 2.0, 3.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithMismatchedReferenceSimplexSize() {
        double[][] referenceSimplex = {
            {1.0, 1.0},
            {2.0, 2.0},
            {3.0, 3.0},
            {4.0, 4.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualVerticesInSimplex() {
        double[][] referenceSimplex = {
            {1.0, 1.0},
            {1.0, 1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, test.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        assertEquals(4, test.getSize());
    }

    @Test
    public void testIterateNullFunction() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.iterate(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIterateNullComparator() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction function = new DummyFunction();
        try {
            test.iterate(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBuild() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        double[] startPoint = {0.0, 0.0};
        test.build(startPoint);
        PointValuePair[] points = test.getPoints();
        assertEquals(4, points.length);
        for (PointValuePair point : points) {
            assertNotNull(point.getPointRef());
            assertNull(point.getValue());
        }
    }

    @Test
    public void testEvaluateNullFunction() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return 0;
            }
        };
        try {
            test.evaluate(null, comparator);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateNullComparator() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction function = new DummyFunction();
        try {
            test.evaluate(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction function = new DummyFunction();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        test.evaluate(function, comparator);
        // Verify that the points have been evaluated
        for (PointValuePair point : test.getPoints()) {
            assertNull(point.getValue());
        }
        // Verify that the points are sorted in order of increasing function value
        PointValuePair worstPoint = test.getPoint(test.getSize() - 1);
        PointValuePair bestPoint = test.getPoint(0);
        double worstFunctionValue = worstPoint.getValue();
        double bestFunctionValue = bestPoint.getValue();
        assertEquals("Worst point should have the highest function value", bestFunctionValue, worstFunctionValue, 0.001);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction function = new DummyFunction();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        test.evaluate(function, comparator);
        PointValuePair pointToReplace = test.getPoint(test.getSize() - 1);
        PointValuePair newPoint = new PointValuePair(new double[]{Double.NaN}, Double.NaN);
        test.replaceWorstPoint(newPoint, comparator);
        assertNull(pointToReplace.getValue());
        assertNull(newPoint.getValue());
        PointValuePair newWorstPoint = test.getPoint(test.getSize() - 1);
        assertNull(newWorstPoint.getValue());
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        test.evaluate(new DummyFunction(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        PointValuePair[] points = test.getPoints();
        for (PointValuePair point : points) {
            assertNotNull(point.getPointRef());
            assertNotNull(point.getValue());
        }
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        test.evaluate(new DummyFunction(), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        PointValuePair point = test.getPoint(0);
        assertNotNull(point.getPointRef());
        assertNotNull(point.getValue());
    }

    @Test
    public void testSetPointNullIndex() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.setPoint(-1, new PointValuePair(new double[]{Double.NaN}, Double.NaN));
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointOutOfBoundsIndex() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.setPoint(4, new PointValuePair(new double[]{Double.NaN}, Double.NaN));
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsNullArray() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.setPoints(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsDifferentSize() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.setPoints(new PointValuePair[5]);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsSameSizeButDifferentDimension() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        try {
            test.setPoints(new PointValuePair[]{
                new PointValuePair(new double[]{1.0, 2.0}, Double.NaN),
                new PointValuePair(new double[]{1.0, 2.0, 3.0}, Double.NaN)
            });
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testSetPointsSameSizeAndDimension() {
        AbstractSimplex test = new AbstractSimplex(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = new PointValuePair[4];
        for (int i = 0; i < points.length; ++i) {
            points[i] = new PointValuePair(new double[]{Double.NaN}, Double.NaN);
        }
        test.setPoints(points);
        assertEquals(4, test.getSize());
    }

    private static class DummyFunction implements MultivariateFunction {

        @Override
        public double value(double[] point) {
            return 0.0;
        }

        @Override
        public void gradient(double[] point, double[] gradient, int maxIterations) {
            // Not used
        }

        @Override
        public void gradient(double[] point, double[] gradient) {
            // Not used
        }

        @Override
        public void valueGradient(double[] point, double[] gradient, int maxIterations) {
            // Not used
        }

        @Override
        public void valueGradient(double[] point, double[] gradient) {
            // Not used
        }

        @Override
        public int getNumberOfVariables() {
            return 3;
        }

        @Override
        public String getName() {
            return "DummyFunction";
        }
    }
}