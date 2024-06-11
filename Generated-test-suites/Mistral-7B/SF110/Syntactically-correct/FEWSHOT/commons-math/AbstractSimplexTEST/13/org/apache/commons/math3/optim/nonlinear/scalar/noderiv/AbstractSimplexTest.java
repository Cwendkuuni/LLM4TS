package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.NonlinearFunction;
import org.apache.commons.math3.optim.InitialGuessFactory;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.Precision;

public class AbstractSimplexTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexNegativeDimension() {
        new AbstractSimplex(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexNullConfiguration() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroConfiguration() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexZeroVertex() {
        new AbstractSimplex(new double[]{0.0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedReferenceSimplexLength() {
        double[][] referenceSimplex = new double[1][1];
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedReferenceSimplexColumn() {
        double[][] referenceSimplex = new double[]{
                {1.0},
                {2.0, 1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexMismatchedReferenceSimplexInequalVertices() {
        double[][] referenceSimplex = new double[]{
                {1.0},
                {1.0}
        };
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testAbstractSimplexHypercubeSteps() {
        int dimensions = 3;
        double sideLength = 2.0;
        double[] steps = AbstractSimplex.createHypercubeSteps(dimensions, sideLength);
        PointValuePair[] expected = new PointValuePair[dimensions + 1];
        for (int i = 0; i < dimensions + 1; ++i) {
            double[] coordinates = new double[dimensions];
            for (int j = 0; j < dimensions; ++j) {
                coordinates[j] = (j + 1) * sideLength / (dimensions + 1);
            }
            expected[i] = new PointValuePair(coordinates, Double.NaN);
        }

        AbstractSimplex simplex = new AbstractSimplex(steps);
        PointValuePair[] actual = simplex.getPoints();

        for (int i = 0; i < dimensions + 1; ++i) {
            PointValuePair expectedPoint = expected[i];
            PointValuePair actualPoint = actual[i];
            assertArrayEquals(expectedPoint.getPointRef(), actualPoint.getPointRef());
        }
    }

    @Test
    public void testAbstractSimplexReferenceSimplex() {
        int dimensions = 2;
        double[][] referenceSimplex = new double[][]{
                {1.0, 0.0},
                {0.5, 1.0},
                {0.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        PointValuePair[] actual = simplex.getPoints();

        for (int i = 0; i < dimensions + 1; ++i) {
            PointValuePair expectedPoint = new PointValuePair(referenceSimplex[i], Double.NaN);
            PointValuePair actualPoint = actual[i];
            assertArrayEquals(expectedPoint.getPointRef(), actualPoint.getPointRef());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexGetPointInvalidIndex() {
        int dimensions = 2;
        double[][] referenceSimplex = new double[][]{
                {1.0, 0.0},
                {0.5, 1.0},
                {0.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testAbstractSimplexSetPointInvalidIndex() {
        int dimensions = 2;
        double[][] referenceSimplex = new double[][]{
                {1.0, 0.0},
                {0.5, 1.0},
                {0.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, Double.NaN);
        simplex.setPoint(-1, point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexSetPointsInvalidSize() {
        int dimensions = 2;
        double[][] referenceSimplex = new double[][]{
                {1.0, 0.0},
                {0.5, 1.0},
                {0.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        PointValuePair[] points = new PointValuePair[dimensions];
        simplex.setPoints(points);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        int dimensions = 2;
        double[][] referenceSimplex = new double[][]{
                {1.0, 0.0},
                {0.5, 1.0},
                {0.0, 1.0}
        };
        AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
        PointValuePair newPoint = new PointValuePair(new double[]{0.25, 0.75}, Double.NaN);
        Comparator<PointValuePair> comparator = new PointValuePairComparator();

        simplex.replaceWorstPoint(newPoint, comparator);

        PointValuePair[] actual = simplex.getPoints();
        PointValuePair expectedNewPoint = new PointValuePair(new double[]{0.25, 0.75}, Double.NaN);
        PointValuePair expectedOldWorstPoint = simplex.getPoint(0);

        int indexOfNewPoint = -1;
        for (int i = 0; i < dimensions + 1; ++i) {
            if (Arrays.equals(actual[i].getPointRef(), expectedNewPoint.getPointRef())) {
                indexOfNewPoint = i;
                break;
            }
        }

        assertEquals(1, indexOfNewPoint);
        assertArrayEquals(expectedOldWorstPoint.getPointRef(), actual[0].getPointRef());
    }

    @Test
    public void testAbstractSimplexIterate() {
        int dimensions = 1;
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        MultivariateFunction function = new PolynomialFunction(1, 1, -1.0, 2.0);
        Comparator<PointValuePair> comparator = new PointValuePairComparator();

        simplex.iterate(function, comparator);

        PointValuePair[] actual = simplex.getPoints();
        PointValuePair expectedMinimum = new PointValuePair(new double[]{0.0}, function.value(new double[]{0.0}), true);

        assertEquals(1, simplex.getSize());
        assertEquals(expectedMinimum.getPointRef(), actual[0].getPointRef());
        assertEquals(expectedMinimum.getValue(), actual[0].getValue(), 1e-10);
        assertEquals(expectedMinimum.isSolution(), actual[0].isSolution());
    }

    @Test
    public void testAbstractSimplexIterateNoImprovement() {
        int dimensions = 1;
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        MultivariateFunction function = new PolynomialFunction(1, 1, -1.0, 2.0);
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public PointValuePair getInitialGuess(int dimension) {
                return new PointValuePair(new double[]{0.0}, Double.NaN);
            }
        };
        Comparator<PointValuePair> comparator = new PointValuePairComparator();

        simplex.build(initialGuess.getInitialGuess(dimensions));
        simplex.iterate(function, comparator);

        PointValuePair[] actual = simplex.getPoints();
        PointValuePair expectedInitialGuess = new PointValuePair(new double[]{0.0}, Double.NaN);

        assertEquals(1, simplex.getSize());
        assertEquals(expectedInitialGuess.getPointRef(), actual[0].getPointRef());
        assertNull(actual[0].getValue());
        assertFalse(actual[0].isSolution());
    }

    @Test
    public void testAbstractSimplexIterateFunctionThrowsException() {
        int dimensions = 1;
        double[] steps = new double[]{1.0};
        AbstractSimplex simplex = new AbstractSimplex(steps);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] args) throws Exception {
                throw new RuntimeException("Function threw an exception");
            }

            @Override
            public int getNumberOfVariables() {
                return dimensions;
            }
        };
        Comparator<PointValuePair> comparator = new PointValuePairComparator();

        simplex.build(new double[]{0.0});
        simplex.iterate(function, comparator);

        // Should not reach this point, as an exception was expected to be thrown during the iteration process
        fail("Function threw an exception, but the test did not detect it");
    }
}