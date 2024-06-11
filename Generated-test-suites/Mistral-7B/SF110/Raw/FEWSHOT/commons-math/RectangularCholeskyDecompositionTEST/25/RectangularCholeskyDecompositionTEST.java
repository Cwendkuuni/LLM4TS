package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.ConstantFactories;
import org.apache.commons.math3.util.Precision;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.44948974278317, 0.816496580927726, 0.0},
                    {-0.44948974278317, -1.63299212244939, 3.0}
            }, root.getData(), 0.001);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSmallValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0 - ConstantFactories.EPSILON}});
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1.0e-10);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.44948974278317, 0.816496580927726, 0.0},
                    {-0.44948974278317, -1.63299212244939, 3.0}
            }, root.getData(), 0.001);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0 - 2.0 * ConstantFactories.EPSILON}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 5.0}, {3.0, 5.0, 5.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testSwapping() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            RealMatrix root = rcd.getRootMatrix();
            int rank = rcd.getRank();
            assertEquals(3, rank);
            assertEquals(3, root.getRowDimension());
            assertEquals(rank, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {1.0, 0.0, 0.0},
                    {0.44948974278317, 0.816496580927726, 0.0},
                    {-0.44948974278317, -1.63299212244939, 3.0}
            }, root.getData(), 0.001);

            // Swap rows 1 and 2
            double[][] data = matrix.getData();
            double[] tmp = data[1];
            data[1] = data[2];
            data[2] = tmp;

            try {
                RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(matrix);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

            // Swap rows back
            tmp = data[1];
            data[1] = data[2];
            data[2] = tmp;
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRankDeficiency() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 8.0}, {5.0, 7.0, 9.0}});
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            int rank = rcd.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            RealMatrix subMatrix = matrix.subMatrix(0, 2, 0, 2);
            RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(subMatrix);
            int rank2 = rcd2.getRank();
            assertEquals(2, rank2);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRankExceeded() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}, {4.0, 7.0, 8.0}});
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
            int rank = rcd.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            RealMatrix subMatrix = matrix.subMatrix(0, 3, 0, 3);
            RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(subMatrix);
            int rank2 = rcd2.getRank();
            assertEquals(3, rank2);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            RealMatrix subMatrix2 = matrix.subMatrix(0, 3, 1, 3);
            RectangularCholeskyDecomposition rcd3 = new RectangularCholeskyDecomposition(subMatrix2);
            int rank3 = rcd3.getRank();
            assertEquals(2, rank3);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        try {
            RectangularCholeskyDecomposition rcd1 = new RectangularCholeskyDecomposition(matrix1);
            RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(matrix2);
            assertTrue(rcd1.equals(rcd2));
        } catch (NonPositiveDefiniteMatrixException e1) {
            // Expected
        } catch (NonPositiveDefiniteMatrixException e2) {
            // Expected
        }

        matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.1}, {3.0, 6.0, 10.0}});
        assertFalse(rcd1.equals(rcd2));

        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0 + ConstantFactories.EPSILON}});
        assertTrue(rcd1.equals(rcd2));

        RealMatrix matrix4 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0 + 2.0 * ConstantFactories.EPSILON}});
        assertFalse(rcd1.equals(rcd2));

        RealMatrix matrix5 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}, {4.0, 7.0, 8.0}});
        assertFalse(rcd1.equals(rcd2));
    }
}