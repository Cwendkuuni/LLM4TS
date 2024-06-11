import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AbstractSimplex;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.multivariate.MultivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithNullArguments() {
        try {
            new AbstractSimplex(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroDimension() {
        try {
            new AbstractSimplex(0);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithZeroSteps() {
        double[] steps = new double[1];
        steps[0] = 0.0;
        try {
            new AbstractSimplex(steps);
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithEqualVertices() {
        double[] steps = new double[2];
        steps[0] = 1.0;
        steps[1] = 1.0;
        try {
            new AbstractSimplex(steps);
            fail("Expected EqualVerticesException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithNegativeSteps() {
        double[] steps = new double[2];
        steps[0] = -1.0;
        steps[1] = 1.0;
        try {
            new AbstractSimplex(steps);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplexEqualPoints() {
        double[][] referenceSimpleX = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        try {
            new AbstractSimplex(referenceSimpleX);
            fail("Expected EqualVerticesInSimpleXException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplexDifferentPoints() {
        double[][] referenceSimpleX = new double[][]{{1.0, 1.0}, {2.0, 2.0}};
        AbstractSimplex simplex = new AbstractSimplex(referenceSimpleX);
        assertEquals(2, simplex.getDimension());
        assertEquals(2, simplex.getSize());
    }

    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = new double[]{1.0, 2.0, 3.0};
        simplex.build(startPoint);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        for (PointValuePair point : points) {
            assertArrayEquals(new double[]{startPoint[0] + simplex.getStartConfiguration(point.getIndex())[0],
                    startPoint[1] + simplex.getStartConfiguration(point.getIndex())[1],
                    startPoint[2] + simplex.getStartConfiguration(point.getIndex())[2]}, point.getPointRef(), 0.001);
        }
    }

    @Test
    public void testAbstractSimplexEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        MultivariateFunction function = f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0) + Math.pow(f[2], 2.0);
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        assertEquals(3, simplex.getSize());
        PointValuePair worstPoint = simplex.getPoint(3);
        assertEquals(Double.POSITIVE_INFINITY, worstPoint.getValue(), 0.001);
    }

    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        simplex.build(new double[]{0.0, 0.0, 0.0});
        MultivariateFunction function = f -> Math.pow(f[0], 2.0) + Math.pow(f[1], 2.0) + Math.pow(f[2], 2.0);
        simplex.evaluate(function, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        PointValuePair pointToReplace = simplex.getPoint(0);
        PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 0.0);
        simplex.replaceWorstPoint(newPoint, new Comparator<PointValuePair>() {
            @Override
            public int compare(PointValuePair o1, PointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        });
        assertEquals(3, simplex.getSize());
        PointValuePair replacedPoint = simplex.getPoint(0);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, replacedPoint.getPointRef(), 0.001);
        assertEquals(0.0, replacedPoint.getValue(), 0.001);
    }
}