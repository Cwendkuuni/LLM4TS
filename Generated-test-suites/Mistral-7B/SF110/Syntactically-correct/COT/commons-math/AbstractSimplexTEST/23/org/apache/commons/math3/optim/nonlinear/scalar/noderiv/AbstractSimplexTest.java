package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

    private final int dimension;

    public AbstractSimplexTest(int dimension) {
        this.dimension = dimension;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1},
                {2},
                {3}
        });
    }

    @Test
    public void testConstructorWithDimension() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullSteps() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructorWithZeroSteps() {
        new AbstractSimplex(new double[0]);
    }

    @Test
    public void testConstructorWithSteps() {
        double[] steps = new double[dimension];
        AbstractSimplex simplex = new AbstractSimplex(steps);
        assertEquals(dimension, simplex.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSteps() {
        double[] steps = new double[]{-1, 2};
        new AbstractSimplex(steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedReferenceSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {2.0, 3.0, 4.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        assertEquals(dimension + 1, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // Implement test method for abstract iterate() method
    }

    @Test
    public void testBuild() {
        MultivariateFunction function = new FunctionWithOnlyOneMaximum();
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        double[] startPoint = new double[dimension];
        simplex.build(startPoint);
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            double expectedValue = function.value(vertex.getPointRef());
            assertEquals(expectedValue, vertex.getValue(), 0.001);
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction function = new FunctionWithOnlyOneMaximum();
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[dimension]);
        simplex.evaluate(function, null);
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair vertex = simplex.getPoint(i);
            double expectedValue = function.value(vertex.getPointRef());
            assertEquals(expectedValue, vertex.getValue(), 0.001);
        }
    }

    @Test
    public void testReplaceWorstPoint() {
        // Implement test method for replaceWorstPoint() method
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[dimension]);
        simplex.evaluate(new FunctionWithOnlyOneMaximum(), null);
        PointValuePair[] expectedPoints = new PointValuePair[dimension + 1];
        System.arraycopy(simplex.getPoints(), 0, expectedPoints, 0, expectedPoints.length);
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        simplex.build(new double[dimension]);
        simplex.evaluate(new FunctionWithOnlyOneMaximum(), null);
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair expectedPoint = simplex.getPoints()[i];
            PointValuePair actualPoint = simplex.getPoint(i);
            assertArrayEquals(expectedPoint.getPointRef(), actualPoint.getPointRef());
        }
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        double[] newPoint = new double[dimension];
        simplex.setPoint(0, new PointValuePair(newPoint, 1.0));
        PointValuePair expectedPoint = new PointValuePair(newPoint, 1.0);
        PointValuePair actualPoint = simplex.getPoint(0);
        assertArrayEquals(expectedPoint.getPointRef(), actualPoint.getPointRef());
    }

    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(dimension);
        PointValuePair[] newPoints = new PointValuePair[dimension + 1];
        simplex.setPoints(newPoints);
        PointValuePair[] expectedPoints = new Points(dimension + 1).toArray(new PointValuePair[0]);
        assertArrayEquals(expectedPoints, simplex.getPoints());
    }

    private static class FunctionWithOnlyOneMaximum implements MultivariateFunction {

        @Override
        public double value(double[] point) {
            double maxValue = Double.NEGATIVE_INFINITY;
            int maxIndex = -1;
            for (int i = 0; i < dimension; ++i) {
                if (point[i] > maxValue) {
                    maxValue = point[i];
                    maxIndex = i;
                }
            }
            return maxValue;
        }

        @Override
        public int getNumberOfVariables() {
            return dimension;
        }
    }
}