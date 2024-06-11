package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Arrays;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexConstruction() {
        try {
            AbstractSimplex simplex1 = new AbstractSimplex(5);
            AbstractSimplex simplex2 = new AbstractSimplex(5, 1.5);
            AbstractSimplex simplex3 = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
            AbstractSimplex simplex4 = new AbstractSimplex(new double[][]{{1.1, 2.2}, {2.2, 3.3}, {3.3, 4.4}});
            int dimension1 = simplex1.getDimension();
            int dimension2 = simplex2.getDimension();
            int dimension3 = simplex3.getDimension();
            int dimension4 = simplex4.getDimension();
            assertEquals("Dimension of simplex1 is incorrect", 5, dimension1);
            assertEquals("Dimension of simplex2 is incorrect", 5, dimension2);
            assertEquals("Dimension of simplex3 is incorrect", 3, dimension3);
            assertEquals("Dimension of simplex4 is incorrect", 2, dimension4);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullInput() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroInput() {
        new AbstractSimplex(0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexInputMismatch() {
        new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8}, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexInvalidReferenceSimplex() {
        new AbstractSimplex(new double[][]{{1.1, 2.2}, {2.2, 3.3}, {3.3, 4.4}, {4.4, 5.5}});
    }

    @Test
    public void testAbstractSimplexGetAndSetSize() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        int size = simplex.getSize();
        assertEquals("Size of simplex is incorrect", 5, size);
        simplex.setSize(3);
        size = simplex.getSize();
        assertEquals("Size of simplex is not updated", 3, size);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexOutOfRangeIndex() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        simplex.setPoint(-1, new PointValuePair(new double[]{0.1, 0.2}, 0.0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexOutOfRangeIndex2() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        simplex.setPoint(6, new PointValuePair(new double[]{0.1, 0.2}, 0.0));
    }

    @Test
    public void testAbstractSimplexGetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        PointValuePair point = simplex.getPoint(0);
        double[] expectedPoint = new double[]{1.2, 3.4};
        assertArrayEquals("Point retrieved is not equal to expected", expectedPoint, point.getPointRef());
    }

    @Test
    public void testAbstractSimplexSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        double[] newPoint = new double[]{0.1, 0.2};
        simplex.setPoint(0, new PointValuePair(newPoint, 0.0));
        PointValuePair point = simplex.getPoint(0);
        assertArrayEquals("Point not updated", newPoint, point.getPointRef());
    }

    @Test
    public void testAbstractSimplexSetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        PointValuePair[] newPoints = new PointValuePair[5];
        for (int i = 0; i < newPoints.length; i++) {
            newPoints[i] = new PointValuePair(new double[]{i + 1.2, i + 3.4}, i * 2.0);
        }
        simplex.setPoints(newPoints);
        for (int i = 0; i < newPoints.length; i++) {
            PointValuePair point = simplex.getPoint(i);
            assertArrayEquals("Point not updated", newPoints[i].getPointRef(), point.getPointRef());
            assertEquals("Function value not updated", newPoints[i].getValue(), point.getValue(), 1e-10);
        }
    }

    @Test
    public void testAbstractSimplexEvaluate() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        MultivariateFunction function = new PolynomialFunction(new double[][]{{-2.0, 2.0}, {2.0, -2.0}}, 2) {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        double fMin = simplex.getPoint(0).getValue();
        double[] pointMin = simplex.getPoint(0).getPointRef();
        assertEquals("Minimum function value is incorrect", 14.4, fMin, 1e-10);
        assertArrayEquals("Minimum point is not correct", new double[]{2.4, 2.4}, pointMin);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        MultivariateFunction function = new PolynomialFunction(new double[][]{{-2.0, 2.0}, {2.0, -2.0}}, 2) {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        PointValuePair worstPoint = simplex.getPoint(4);
        double[] newPoint = new double[]{3.5, 4.5};
        simplex.replaceWorstPoint(new PointValuePair(newPoint, function.value(newPoint)), new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        PointValuePair newWorstPoint = simplex.getPoint(4);
        assertArrayEquals("New worst point is not updated", newPoint, newWorstPoint.getPointRef());
        assertEquals("Function value of new worst point is not updated", 25.0, newWorstPoint.getValue(), 1e-10);
    }

    @Test
    public void testAbstractSimplexIterate() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        MultivariateFunction function = new PolynomialFunction(new double[][]{{-2.0, 2.0}, {2.0, -2.0}}, 2) {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        simplex.iterate(function, comparator);
        PointValuePair bestPoint = simplex.getPoint(0);
        double fMin = bestPoint.getValue();
        double[] pointMin = bestPoint.getPointRef();
        assertEquals("Minimum function value is incorrect", 14.4, fMin, 1e-10);
        assertArrayEquals("Minimum point is not correct", new double[]{2.4, 2.4}, pointMin);
    }

    @Test
    public void testAbstractSimplexMaxEval() throws Exception {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.2, 3.4, 5.6, 7.8, 9.0});
        ObjectiveFunction objectiveFunction = new ObjectiveFunction("Test function") {
            @Override
            public double value(double[] x) {
                return Math.sin(x[0]) * Math.cos(x[1]) + 1;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MAXIMIZE;
            }
        };
        InitialGuess initialGuess = new InitialGuess(1.0, new double[]{1.0, 1.0});
        MaxEval maxEval = new MaxEval(objectiveFunction, simplex, initialGuess, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair p1, PointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });
        try {
            maxEval.optimize();
            fail("MaxEval should have thrown an exception");
        } catch (MaxEvalException e) {
            // Expected exception
        }
    }
}