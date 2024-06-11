package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex() {
        try {
            new AbstractSimplex(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new AbstractSimplex(new int[0]);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }

        // Test default constructor
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
        
        // Test constructor with side length
        AbstractSimplex simplex2 = new AbstractSimplex(3, 2.5);
        assertEquals(3, simplex2.getDimension());
        
        // Test constructor with steps
        double[] steps = {1.0, 1.5, 2.0};
        AbstractSimplex simplex3 = new AbstractSimplex(steps);
        assertEquals(3, simplex3.getDimension());
        
        // Test constructor with referenceSimplex
        double[][] referenceSimplex = {
            {0.0, 0.0, 0.0},
            {0.5, 0.5, 0.5},
            {1.0, 1.0, 1.0}
        };
        AbstractSimplex simplex4 = new AbstractSimplex(referenceSimplex);
        assertEquals(3, simplex4.getDimension());
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

    @Test
    public void testIterate() {
        // Test abstract method, cannot be tested directly
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);

        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (int i = 0; i < points.length; ++i) {
            assertEquals(i + 1, points[i].getIndex());
        }

        PointValuePair startPointPair = simplex.getPoint(0);
        assertArrayEquals(startPoint, startPointPair.getPointRef());
        assertNaN(startPointPair.getValue());

        for (int i = 1; i < points.length; ++i) {
            PointValuePair pointI = simplex.getPoint(i);
            double[] pointIRef = pointI.getPointRef();
            double[] startPointRef = simplex.getPoint(0).getPointRef();
            for (int j = 0; j < simplex.getDimension(); ++j) {
                assertEquals(startPointRef[j] + simplex.startConfiguration[i - 1][j], pointIRef[j]);
            }
            assertNaN(pointI.getValue());
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        simplex.evaluate(evaluationFunction, null);

        PointValuePair[] points = simplex.getPoints();
        for (int i = 0; i < points.length; ++i) {
            double[] pointRef = points[i].getPointRef();
            double value = points[i].getValue();
            assertEquals(evaluationFunction.value(pointRef), value, 1e-8);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        PointValuePair worstPoint = simplex.getPoint(2);
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0}, Double.NEGATIVE_INFINITY);

        simplex.replaceWorstPoint(newPoint, null);

        PointValuePair[] points = simplex.getPoints();
        assertNotSame(worstPoint, points[2]);
        assertEquals(newPoint, points[2]);
        assertEquals(-Double.MAX_VALUE, points[2].getValue(), 1e-8);
    }

    @Test
    public void testGetPoints() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplesex = new AbstractSimplex(3);
        simplesex.build(new double[]{0.0, 0.0, 0.0});
        simplesex.evaluate(evaluationFunction, null);

        PointValuePair[] points = simplesex.getPoints();
        assertEquals(4, points.length);

        for (int i = 0; i < points.length; ++i) {
            double[] pointRef = points[i].getPointRef();
            double value = points[i].getValue();
            assertEquals(i + 1, points[i].getIndex());
            assertEquals(evaluationFunction.value(pointRef), value, 1e-8);
        }
    }

    @Test
    public void testGetPoint() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplesex = new AbstractSimplex(3);
        simplesex.build(new double[]{0.0, 0.0, 0.0});
        simplesex.evaluate(evaluationFunction, null);

        PointValuePair point = simplesex.getPoint(1);
        double[] pointRef = point.getPointRef();
        double value = point.getValue();
        assertEquals(2, point.getIndex());
        assertEquals(evaluationFunction.value(pointRef), value, 1e-8);
    }

    @Test
    public void testSetPoint() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplesex = new AbstractSimplex(3);
        simplesex.build(new double[]{0.0, 0.0, 0.0});

        PointValuePair point = simplesex.getPoint(2);
        double[] newPoint = {1.0, 1.0};

        simplesex.setPoint(2, new PointValuePair(newPoint, Double.NaN));

        PointValuePair[] points = simplesex.getPoints();
        assertNotSame(point, points[2]);
        assertEquals(newPoint, points[2].getPointRef());
        assertNaN(points[2].getValue());
    }

    @Test
    public void testSetPoints() {
        MultivariateFunction evaluationFunction = new FunctionWithTwoVariables();
        AbstractSimplex simplesex = new AbstractSimplex(3);
        simplesex.build(new double[]{0.0, 0.0, 0.0});

        PointValuePair[] newPoints = {
            new PointValuePair(new double[]{1.0, 1.0}, Double.NaN),
            new PointValuePair(new double[]{2.0, 2.0}, Double.NaN),
            new PointValuePair(new double[]{3.0, 3.0}, Double.NaN)
        };

        simplesex.setPoints(newPoints);

        PointValuePair[] points = simplesex.getPoints();
        assertArrayEquals(newPoints, points);
    }

    @Test
    public void testCreateHypercubeSteps() {
        assertEquals(3, AbstractSimplex.createHypercubeSteps(3, 1.0).length);
        assertEquals(3, AbstractSimplex.createHypercubeSteps(1, 1.0).length);
        try {
            AbstractSimplex.createHypercubeSteps(0, 1.0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // expected
        }
    }
}

class FunctionWithTwoVariables implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        double x1 = x[0];
        double x2 = x[1];

        return x1 * x1 + x2 * x2;
    }
}