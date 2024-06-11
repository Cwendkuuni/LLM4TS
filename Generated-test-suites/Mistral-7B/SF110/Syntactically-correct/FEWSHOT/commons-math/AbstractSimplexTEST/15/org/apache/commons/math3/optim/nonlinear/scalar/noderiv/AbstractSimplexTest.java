package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;

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
    public void testAbstractSimplexZeroSteps() {
        try {
            new AbstractSimplex(new double[0]);
            fail();
        } catch (ZeroException e) {
            // expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDifferentLengthStartConfiguration() {
        try {
            new AbstractSimplex(new double[][]{new double[3], new double[2]});
            fail();
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVertices() {
        try {
            new AbstractSimplex(new double[][]{new double[]{1, 2}, new double[]{1, 2}});
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexZeroLengthReferenceSimplex() {
        try {
            new AbstractSimplex(new double[][]{});
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexDifferentLengthReferenceSimpleVertices() {
        try {
            new AbstractSimplex(new double[][]{{1, 2}, {1, 3}});
            fail();
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexEqualVerticesInReferenceSimple() {
        try {
            new AbstractSimplex(new double[][]{{1, 2}, {1, 2}});
            fail();
        } catch (MathIllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testAbstractSimplex() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        assertEquals(5, simplex.getDimension());
        assertEquals(6, simplex.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexIncorrectDimension() {
        AbstractSimplex simplex = new AbstractSimplex(5);
        try {
            simplex.iterate(new MultivariateFunction() {
                public double value(double[] point) {
                    return 0.0;
                }
            }, null);
            fail();
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        PointValuePair[] points = simplex.getPoints();
        assertEquals(points.length, 4);
        for (int i = 0; i < points.length; ++i) {
            PointValuePair point = points[i];
            double[] pointRef = point.getPointRef();
            for (int j = 0; j < pointRef.length; ++j) {
                assertEquals(startPoint[j] + (i == 0 ? 0.0 : ((i - 1) % 3 + 1) * 1.0), pointRef[j], 1e-9);
            }
        }
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0) + Math.pow(point[2], 2.0);
            }
        };
        simplex.evaluate(evaluationFunction, null);
        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        double worstValue = worstPoint.getValue();
        assertTrue(worstValue > 14.0);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        MultivariateFunction evaluationFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2.0) + Math.pow(point[1], 2.0) + Math.pow(point[2], 2.0);
            }
        };
        simplex.evaluate(evaluationFunction, null);
        PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
        simplex.replaceWorstPoint(new PointValuePair(new double[]{4.0, 5.0, 6.0}, 100.0), null);
        PointValuePair newWorstPoint = simplex.getPoint(simplex.getSize() - 1);
        assertNotSame(worstPoint, newWorstPoint);
        assertEquals(newWorstPoint.getValue(), 100.0, 1e-9);
    }
}