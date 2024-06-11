package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.util.Arrays;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplex_int() {
        AbstractSimplex simplex = new SimplexFinderNelderMead(1);
        assertEquals(1, simplex.getDimension());
        simplex = new SimplexFinderNelderMead(2);
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplex_int_double() {
        AbstractSimplex simplex = new SimplexFinderNelderMead(3, 1.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test(expected = NullArgumentException.class)
    public void testAbstractSimplex_doubleArrayNull() {
        new SimplexFinderNelderMead(null);
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayEmpty() {
        new SimplexFinderNelderMead(new double[0]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_doubleArrayIncorrectLength() {
        new SimplexFinderNelderMead(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0});
    }

    @Test(expected = ZeroException.class)
    public void testAbstractSimplex_doubleArrayEqualVertices() {
        new SimplexFinderNelderMead(new double[]{1.0, 1.0, 2.0, 2.0});
    }

    @Test
    public void testAbstractSimplex_doubleArray() {
        AbstractSimplex simplex = new SimplexFinderNelderMead(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testAbstractSimplex_doubleArrayReferenceSimplex() {
        double[][] referenceSimplex = {
                {1.0, 0.0},
                {0.0, 1.0},
                {-1.0, -1.0},
                {1.0, -1.0},
                {-1.0, 1.0}
        };
        AbstractSimplex simplex = new SimplexFinderNelderMead(referenceSimplex);
        assertEquals(3, simplex.getDimension());
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointRef = point.getPointRef();
            assertArrayEquals(referenceSimplex[i], pointRef, 0.001);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testAbstractSimplex_referenceSimplexEmpty() {
        new SimplexFinderNelderMead(new double[0][]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_referenceSimplexIncorrectLength() {
        new SimplexFinderNelderMead(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0},
                {1.0, 1.0}
        });
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAbstractSimplex_referenceSimplexFirstVertexMismatchedLength() {
        new SimplexFinderNelderMead(new double[][]{
                {1.0},
                {1.0, 0.0},
                {1.0, 1.0}
        });
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testAbstractSimplex_referenceSimplexEqualVertices() {
        double[][] referenceSimplex = {
                {1.0, 0.0},
                {1.0, 0.0},
                {0.0, 1.0}
        };
        new SimplexFinderNelderMead(referenceSimplex);
    }

    @Test
    public void testIterate() {
        MultivariateFunction f = f -> FunctionUtils.pow(Math.PI * Math.sin(f[0]) + Math.PI * Math.cos(f[1]), 2);
        AbstractSimplex simplex = new SimplexFinderNelderMead(new double[]{1.0, 1.0});
        simplex.iterate(f, Comparator.comparingDouble(p -> p.getValue()));
        assertEquals(-3.141592653589793, simplex.getPoint(0).getValue(), 0.001);
        assertEquals(-3.141592653589793, simplex.getPoint(1).getValue(), 0.001);
    }

    @Test
    public void testBuild() {
        double[] startPoint = {1.0, 1.0};
        AbstractSimplex simplex = new SimplexFinderNelderMead(3);
        simplex.build(startPoint);
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointRef = point.getPointRef();
            assertArrayEquals(Arrays.add(startPoint, this.getStartConfiguration(i)), pointRef, 0.001);
        }
    }

    @Test
    public void testEvaluate() {
        MultivariateFunction f = f -> Math.pow(Math.sin(f[0]) + Math.sin(f[1]), 2);
        AbstractSimplex simplex = new SimplexFinderNelderMead(new double[]{1.0, 1.0});
        simplex.build(new double[]{1.0, 1.0});
        simplex.evaluate(f, Comparator.comparingDouble(p -> p.getValue()));
        assertEquals(-1.0, simplex.getPoint(0).getValue(), 0.001);
        assertEquals(-1.0, simplex.getPoint(1).getValue(), 0.001);
        assertEquals(0.0, simplex.getPoint(2).getValue(), 0.001);
    }

    @Test
    public void testReplaceWorstPoint() {
        double[] startPoint = {1.0, 1.0};
        AbstractSimplex simplex = new SimplexFinderNelderMead(3);
        simplex.build(startPoint);
        simplex.evaluate(f -> Math.pow(Math.sin(f[0]) + Math.sin(f[1]), 2), Comparator.comparingDouble(p -> p.getValue()));
        PointValuePair worstPoint = simplex.getPoint(2);
        PointValuePair newPoint = new PointValuePair(new double[]{2.0, 2.0}, 0.0);
        simplex.replaceWorstPoint(newPoint, Comparator.comparingDouble(p -> p.getValue()));
        assertEquals(2.0, simplex.getPoint(2).getPointRef()[0], 0.001);
        assertEquals(2.0, simplex.getPoint(2).getPointRef()[1], 0.001);
        assertEquals(0.0, simplex.getPoint(2).getValue(), 0.001);
    }

    @Test
    public void testSetPoint() {
        double[] startPoint = {1.0, 1.0};
        AbstractSimplex simplex = new SimplexFinderNelderMead(3);
        simplex.build(startPoint);
        simplex.setPoint(1, new PointValuePair(new double[]{2.0, 2.0}, 0.0));
        assertEquals(2.0, simplex.getPoint(1).getPointRef()[0], 0.001);
        assertEquals(2.0, simplex.getPoint(1).getPointRef()[1], 0.001);
        assertEquals(0.0, simplex.getPoint(1).getValue(), 0.001);
    }

    @Test
    public void testSetPoints() {
        double[][] referencePoints = {
                {1.0, 1.0},
                {2.0, 2.0},
                {3.0, 3.0}
        };
        AbstractSimplex simplex = new SimplexFinderNelderMead(3);
        simplex.setPoints(Arrays.asList(referencePoints).toArray(new PointValuePair[0]));
        for (int i = 0; i < simplex.getSize(); ++i) {
            PointValuePair point = simplex.getPoint(i);
            double[] pointRef = point.getPointRef();
            assertArrayEquals(referencePoints[i], pointRef, 0.001);
        }
    }

    private double[] getStartConfiguration(int index) {
        if (index == 0) {
            return new double[0];
        }
        double[] vertexI = new double[this.dimension];
        for (int k = 0; k < this.dimension; ++k) {
            vertexI[k] = 1.0;
        }
        return vertexI;
    }
}