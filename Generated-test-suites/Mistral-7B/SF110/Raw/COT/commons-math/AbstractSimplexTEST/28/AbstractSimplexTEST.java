package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class AbstractSimplexTest {

    private int dimension;
    private AbstractSimplex simplex;

    @Parameterized.Parameters(name = "{index}: AbstractSimplex with dimension={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1},
                {2},
                {3},
        });
    }

    @Parameterized.Constructor
    public AbstractSimplexTest(int dimension) {
        this.dimension = dimension;
        simplex = new SimplexNelderMead(dimension); // Use the concrete implementation of AbstractSimplex
    }

    @Test
    public void testGetDimension() {
        assertEquals("Getting the dimension of the simplex should return the correct value.", dimension, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals("The size of the simplex should be the dimension plus one.", dimension + 1, simplex.getSize());
    }

    @Test
    public void testIterate() {
        MultivariateFunction function = new Add();
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        simplex.iterate(function, comparator);

        // Check if the simplex has been sorted
        PointValuePair worstPoint = simplex.getPoint(dimension);
        for (int i = 0; i < dimension; ++i) {
            PointValuePair currentPoint = simplex.getPoint(i);
            assertTrue("The simplex should be sorted in ascending order.", currentPoint.getValue() <= worstPoint.getValue());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testBuildWithInvalidDimension() {
        double[] startPoint = new double[dimension + 1];
        simplex.build(startPoint);
    }

    @Test
    public void testBuild() {
        double[] startPoint = new double[dimension];
        simplex.build(startPoint);

        for (int i = 0; i < dimension + 1; ++i) {
            PointValuePair point = simplex.getPoint(i);
            assertNotNull("Each point in the simplex should not be null.", point);
            assertEquals("The point's dimension should match the simplex's dimension.", dimension, point.getPointRef().length);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPointOutOfRange() {
        simplex.getPoint(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplexWithZeroDimension() {
        new AbstractSimplex(0);
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplexWithNullStartConfiguration() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplexWithZeroLengthStartConfiguration() {
        new AbstractSimplex(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplexWithMismatchedReferenceSimplexDimension() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {1.0, 3.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplexWithEqualVerticesInSimplex() {
        double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {1.0, 2.0}};
        new AbstractSimplex(referenceSimplex);
    }

    @Test
    public void testReplaceWorstPoint() {
        PointValuePair pointToReplace = simplex.getPoint(dimension - 1);
        PointValuePair newPoint = new PointValuePair(new double[dimension], -1.0);
        simplex.replaceWorstPoint(newPoint, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        assertNotSame("The worst point in the simplex should not be the same after replacement.", pointToReplace, simplex.getPoint(dimension - 1));
    }

    @Test
    public void testGetPoints() {
        PointValuePair[] points = simplex.getPoints();
        assertEquals("The number of points in the simplex should match its size.", dimension + 1, points.length);
    }
}