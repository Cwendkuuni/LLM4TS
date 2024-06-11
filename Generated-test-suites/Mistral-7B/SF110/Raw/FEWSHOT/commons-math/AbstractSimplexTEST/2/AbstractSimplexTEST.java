package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.PointValuePair;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        try {
            new AbstractSimplex(-1);
            fail("Expected NegativeArgumentException");
        } catch (NegativeArgumentException e) {
            // Expected
        }

        try {
            new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }

        try {
            new AbstractSimplex(new double[] { 1, 2, 3, 4, 5, 6 });
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        double[] steps = new double[] { 1.0, 2.0, 3.0 };
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexFromReferenceSimplex() {
        double[][] referenceSimplex = new double[][] { {1.0, 2.0}, {3.0, 2.0}, {3.0, 4.0} };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());

        PointValuePair p1 = simplex.getPoint(0);
        assertEquals(1.0, p1.getPointRef()[0], 1e-8);
        assertEquals(1.0, p1.getValue(), 1e-8);

        PointValuePair p2 = simplex.getPoint(1);
        assertEquals(3.0, p2.getPointRef()[0], 1e-8);
        assertEquals(3.0, p2.getPointRef()[1], 1e-8);
        assertEquals(2.0, p2.getValue(), 1e-8);

        PointValuePair p3 = simplex.getPoint(2);
        assertEquals(3.0, p3.getPointRef()[0], 1e-8);
        assertEquals(4.0, p3.getPointRef()[1], 1e-8);
        assertEquals(4.0, p3.getValue(), 1e-8);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);

        double[] startPoint = new double[] { 1.0, 2.0, 3.0 };
        simplex.build(startPoint);

        PointValuePair p0 = simplex.getPoint(0);
        assertEquals(startPoint, p0.getPointRef());
        assertEquals(Double.NaN, p0.getValue());

        for (int i = 1; i < simplex.getSize(); ++i) {
            PointValuePair pi = simplex.getPoint(i);
            double[] pointI = pi.getPointRef();
            for (int j = 0; j < simplex.getDimension(); ++j) {
                assertEquals(startPoint[j] + simplex.getStartConfiguration()[i - 1][j], pointI[j], 1e-8);
            }
            assertEquals(Double.NaN, pi.getValue());
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] * point[0] + point[1] * point[1], 2.0);
            }
        };

        AbstractSimplex simplex = new AbstractSimplex(2) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Empty method
            }
        };

        simplex.build(new double[] { 0.0, 0.0 });
        simplex.evaluate(evaluationFunction, null);

        PointValuePair p0 = simplex.getPoint(0);
        assertEquals(Double.NaN, p0.getValue());
        assertEquals(0.0, p0.getPointRef()[0], 1e-8);
        assertEquals(0.0, p0.getPointRef()[1], 1e-8);

        for (int i = 1; i < simplex.getSize(); ++i) {
            PointValuePair pi = simplex.getPoint(i);
            double[] pointI = pi.getPointRef();
            double valueI = pi.getValue();

            for (int j = 0; j < simplex.getDimension(); ++j) {
                assertEquals(pointI[j], pointI[j], 1e-8);
            }

            assertEquals(evaluationFunction.value(pointI), valueI, 1e-8);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Empty method
            }
        };

        simplex.build(new double[] { 0.0, 0.0, 0.0 });

        double[] point = new double[] { 1.0, 1.0 };
        PointValuePair newPoint = new PointValuePair(point, evaluationFunction(point), false);

        simplex.replaceWorstPoint(newPoint, null);

        PointValuePair worstPoint = simplex.getPoint(2);
        assertEquals(point[0], worstPoint.getPointRef()[0], 1e-8);
        assertEquals(point[1], worstPoint.getPointRef()[1], 1e-8);
        assertEquals(newPoint.getValue(), worstPoint.getValue(), 1e-8);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(3) {
            @Override
            public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
                // Empty method
            }
        };

        simplex.build(new double[] { 0.0, 0.0, 0.0 });
        simplex.evaluate(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.sin(point[0]) * Math.cos(point[1]);
            }
        }, null);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(3, points.length);

        for (int i = 0; i < 3; ++i) {
            PointValuePair pi = points[i];
            assertEquals(simpleX(i), pi.getPointRef());
            assertEquals(simpleValue(i), pi.getValue(), 1e-8);
        }
    }

    private static double[] simpleX(int index) {
        double[] result = new double[2];
        for (int i = 0; i < 2; ++i) {
            result[i] = index * 0.5 + i;
        }
        return result;
    }

    private static double simpleValue(int index) {
        return Math.sin(simpleX(index)[0]) * Math.cos(simpleX(index)[1]);
    }

    private static double evaluationFunction(double[] point) {
        return Math.pow(point[0] * point[0] + point[1] * point[1], 2.0);
    }
}