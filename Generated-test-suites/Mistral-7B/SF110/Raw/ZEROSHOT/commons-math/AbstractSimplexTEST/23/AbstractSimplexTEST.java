package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.junit.Test;

import java.util.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstructorWithN() {
        int n = 3;
        AbstractSimplex simplex = new AbstractSimplex(n);
        assertEquals(n, simplex.getDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithNegativeN() {
        int n = -3;
        new AbstractSimplex(n);
    }

    @Test
    public void testAbstractSimplexConstructorWithNAndSideLength() {
        int n = 3;
        double sideLength = 2.5;
        AbstractSimplex simplex = new AbstractSimplex(n, sideLength);
        assertEquals(n, simplex.getDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithZeroSideLength() {
        int n = 3;
        new AbstractSimplex(n, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithNullStepArray() {
        int n = 3;
        double[] steps = null;
        try {
            new AbstractSimplex(steps);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithEmptyStepArray() {
        int n = 3;
        double[] steps = new double[0];
        try {
            new AbstractSimplex(steps);
        } catch (ZeroException e) {
            // expected
        }
    }

    @Test
    public void testAbstractSimplexConstructorWithStepArray() {
        int n = 3;
        double[] steps = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(n, simplex.getDimension());
        for (int i = 0; i < n; ++i) {
            assertEquals(steps[i], simplex.getPoint(i).getPointRef()[i], 1e-10);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithNonConformingStepArray() {
        int n = 3;
        double[] steps = new double[]{1.0, 2.0, 3.0, 4.0};
        try {
            new AbstractSimplex(steps);
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testAbstractSimplexConstructorWithReferenceSimplex() {
        int n = 3;
        double[][] referenceSimplex = new double[][] {
                {0.0, 0.0, 0.0},
                {1.0, 1.0, 1.0},
                {2.0, 2.0, 2.0},
                {3.0, 3.0, 3.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(n, simplex.getDimension());
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                assertEquals(referenceSimplex[i][j], simplex.getPoint(i).getPointRef()[j], 1e-10);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexConstructorWithNonConformingReferenceSimplex() {
        int n = 3;
        double[][] referenceSimplex = new double[][] {
                {0.0, 0.0, 0.0},
                {1.0, 1.0, 1.0},
                {2.0, 2.0, 2.0},
                {3.0, 3.0, 3.0},
                {4.0, 4.0, 4.0}
        };
        try {
            new AbstractSimplex(referenceSimplex);
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testAbstractSimplexGetDimension() {
        int n = 3;
        AbstractSimplex simplex = new AbstractSimplex(n);
        assertEquals(n, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplexGetSize() {
        int n = 3;
        AbstractSimplex simplex = new AbstractSimplex(n);
        assertEquals(n + 1, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexIterate() {
        // AbstractSimplex does not provide an iterate method for testing
    }

    @Test
    public void testAbstractSimplexBuild() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        for (int i = 0; i <= n; ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointCoords = point.getPointRef();
            for (int j = 0; j < n; ++j) {
                assertEquals(startPoint[j] + simplex.startConfiguration[j][j], pointCoords[j], 1e-10);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexBuildWithMismatchedStartPoint() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0, 4.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1] + point[2] * point[2];
            }
        };
        simplex.evaluate(function, null);
        for (int i = 0; i <= n; ++i) {
            PointValuePair point = simplex.getPoint(i);
            double pointValue = point.getValue();
            double[] pointCoords = point.getPointRef();
            double functionValue = function.value(pointCoords);
            assertEquals(functionValue, pointValue, 1e-10);
        }
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1] + point[2] * point[2];
            }
        };
        simplex.evaluate(function, null);
        PointValuePair worstPoint = simplex.getPoint(n);
        PointValuePair newPoint = new PointValuePair(new double[]{5.0, 5.0, 5.0}, 100.0, false);
        simplex.replaceWorstPoint(newPoint, null);
        assertEquals(newPoint, simplex.getPoint(n));
    }

    @Test
    public void testAbstractSimplexGetPoints() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i <= n; ++i) {
            PointValuePair point = simplex.getPoint(i);
            assertTrue(Arrays.equals(point.getPointRef(), points[i].getPointRef()));
        }
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        PointValuePair point = simplex.getPoint(1);
        double[] pointCoords = point.getPointRef();
        for (int i = 0; i < n; ++i) {
            assertEquals(startPoint[i] + simplex.startConfiguration[i][i], pointCoords[i], 1e-10);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointWithInvalidIndex() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        simplex.getPoint(-1);
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{5.0, 5.0, 5.0}, 100.0, false);
        simplex.setPoint(1, point);
        assertEquals(point, simplex.getPoint(1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointWithInvalidIndex() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        simplex.build(startPoint);
        PointValuePair point = new PointValuePair(new double[]{5.0, 5.0, 5.0}, 100.0, false);
        simplex.setPoint(-1, point);
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        PointValuePair[] newPoints = new PointValuePair[n + 1];
        for (int i = 0; i <= n; ++i) {
            newPoints[i] = new PointValuePair(new double[]{i + 1.0, i + 1.0, i + 1.0}, i * 2.0, false);
        }
        simplex.setPoints(newPoints);
        for (int i = 0; i <= n; ++i) {
            PointValuePair point = simplex.getPoint(i);
            assertTrue(Arrays.equals(newPoints[i].getPointRef(), point.getPointRef()));
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsWithMismatchedArraySize() {
        int n = 3;
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        AbstractSimplex simplex = new AbstractSimplex(n);
        PointValuePair[] newPoints = new PointValuePair[n];
        simplex.setPoints(newPoints);
    }

    @Test
    public void testAbstractSimplexCreateHypercubeSteps() {
        int n = 3;
        double[] steps = AbstractSimplex.createHypercubeSteps(n, 2.5);
        for (int i = 0; i < n; ++i) {
            assertEquals(2.5, steps[i], 1e-10);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractSimplexCreateHypercubeStepsWithNegativeN() {
        int n = -3;
        AbstractSimplex.createHypercubeSteps(n, 2.5);
    }
}