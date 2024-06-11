package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.point.PointValuePair;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstruction() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals("Dimension incorrectly set.", 3, simplex.getDimension());
        try {
            new AbstractSimplex(3, -1.0);
            fail("Expected NegativeException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        try {
            new AbstractSimplex(new int[0]);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
        double[] steps = new double[] { 1.0, 2.0, 0.5 };
        AbstractSimplex simplex2 = new AbstractSimplex(steps);
        assertEquals("Dimension incorrectly set.", 3, simplex2.getDimension());
        try {
            new AbstractSimplex(new double[] { 1.0, 2.0, 2.0, 0.5 });
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
        try {
            new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
        AbstractSimplex simplex3 = new AbstractSimplex(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        assertEquals("Dimension incorrectly set.", 3, simplex3.getDimension());
        try {
            new AbstractSimplex(new double[][] {
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}
            });
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
        AbstractSimplex simplex4 = new AbstractSimplex(createHypercubeSteps(4, 2.0));
        assertEquals("Dimension incorrectly set.", 4, simplex4.getDimension());
    }

    @Test
    public void testAbstractSimplexMethods() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] { 1.0, 1.0, 1.0 });
        assertEquals("Size incorrectly set.", 4, simplex.getSize());
        PointValuePair point1 = simplex.getPoint(0);
        PointValuePair point2 = simplex.getPoint(1);
        PointValuePair point3 = simplex.getPoint(2);
        PointValuePair point4 = simplex.getPoint(3);
        assertEquals("Point values are NaN.", !Double.isNaN(point1.getValue()), true);
        assertEquals("Point values are NaN.", !Double.isNaN(point2.getValue()), true);
        assertEquals("Point values are NaN.", !Double.isNaN(point3.getValue()), true);
        assertEquals("Point values are NaN.", !Double.isNaN(point4.getValue()), true);
        simplex.evaluate(new QuadraticFunction(new double[] { 1.0, 1.0 }), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        double[] pointValues = new double[4];
        for (int i = 0; i < 4; ++i) {
            pointValues[i] = simplex.getPoint(i).getValue();
        }
        PointValuePair worstPoint = new PointValuePair(new double[3], Double.MAX_VALUE);
        simplex.replaceWorstPoint(simplex.getPoint(3), worstPoint.getComparator());
        PointValuePair[] points = simplex.getPoints();
        Arrays.sort(points, worstPoint.getComparator());
        for (int i = 0; i < 4; ++i) {
            assertEquals("Point " + i + " value is incorrect.", pointValues[i], points[i].getValue(), 1e-12);
        }
        simplex.setPoint(3, new PointValuePair(new double[] { 0.1, 0.1, 0.1 }, 1.0));
        simplex.setPoints(new PointValuePair[] {
            new PointValuePair(new double[] { 1.1, 1.1, 1.1 }, Double.NaN),
            new PointValuePair(new double[] { 2.1, 2.1, 2.1 }, Double.NaN),
            new PointValuePair(new double[] { 3.1, 3.1, 3.1 }, Double.NaN),
            new PointValuePair(new double[] { 4.1, 4.1, 4.1 }, 1.0)
        });
    }

    @Test(expected = MaxCountExceededException.class)
    public void testAbstractSimplexIterate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.iterate(new QuadraticFunction(new double[] { 1.0, 1.0 }), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
    }

    @Test
    public void testAbstractSimplexToString() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[] { 1.0, 1.0, 1.0 });
        String toString = simplex.toString();
        assertTrue("ToString method did not include 'AbstractSimplex@' and hashcode.", toString.startsWith("AbstractSimplex@"));
        assertTrue("ToString method did not include 'dimension=3, size=4'.", toString.contains("dimension=3, size=4"));
    }

    private static Function createQuadraticFunction(double[] coefficients) {
        return new QuadraticFunction(coefficients);
    }

    private static class QuadraticFunction implements MultivariateFunction {
        private final double[] coefficients;

        public QuadraticFunction(double[] coefficients) {
            this.coefficients = coefficients;
        }

        @Override
        public double value(double[] point) {
            double result = 0.0;
            for (int i = 0; i < coefficients.length; ++i) {
                result += coefficients[i] * Precision.pow(point[i], 2.0);
            }
            return result;
        }

        @Override
        public int getNumberOfVariables() {
            return coefficients.length;
        }

        @Override
        public int getNumberOfConstraints() {
            return 0;
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }

        @Override
        public int getDimension() {
            return coefficients.length;
        }
    }
}