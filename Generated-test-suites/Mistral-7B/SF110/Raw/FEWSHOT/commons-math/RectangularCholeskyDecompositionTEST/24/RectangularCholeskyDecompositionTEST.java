package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, -1.0}, {-1.0, 2.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixExceptionSmall() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testGetRank() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"\"foo\"\" and \"\"bar\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testDecomposePositiveDefiniteMatrix() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 3.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        final double[][] expected = new double[][]{{1.4142135625, -0.7142857143}, {-0.7142857143, 1.6825647059}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-12);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecomposeSymmetricPositiveDefiniteMatrix() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        final double[][] expected = new double[][]{{1.4142135625, 0.7142857143}, {0.7142857143, 1.4142135625}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-12);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testDecomposeSymmetricPositiveDefiniteMatrixWithSmallValues() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{3.0, 1.0}, {1.0, 3.0}});
        final double small = 1e-8;
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        final double[][] expected = new double[][]{{1.7320508076, 0.5773502692}, {0.5773502692, 1.7320508076}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-12);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testSwapRows() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 3.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        final int[] index = decomposition.index;
        final int swapR = 1;
        final int tmpIndex = index[swapR];
        index[swapR] = index[0];
        index[0] = tmpIndex;
        final double[][] b = decomposition.b;
        final double[] tmpRow = b[swapR];
        b[swapR] = b[0];
        b[0] = tmpRow;
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        final double[][] expected = new double[][]{{1.4142135625, -0.7142857143}, {-0.7142857143, 1.6825647059}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-12);
        final int expectedRank = 2;
        assertEquals(expectedRank, decomposition.getRank());
        index[swapR] = tmpIndex;
        index[0] = index[swapR];
        b[swapR] = b[0];
        b[0] = tmpRow;
    }

    @Test
    public void testNoSwapRows() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 3.0}});
        final RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        final int[] index = decomposition.index;
        final boolean loop = false;
        decomposition.loop = loop;
        final RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(2, rootMatrix.getRowDimension());
        assertEquals(2, rootMatrix.getColumnDimension());
        final double[][] expected = new double[][]{{1.4142135625, -0.7142857143}, {-0.7142857143, 1.6825647059}};
        assertArrayEquals(expected, rootMatrix.getData(), 1e-12);
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testNegativeElementOnDiagonal() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, -1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().startsWith("Negative diagonal element"));
        }
    }

    @Test
    public void testNegativeElementOnSubdiagonal() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0, 0.0}, {0.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().startsWith("Negative subdiagonal element"));
        }
    }

    @Test
    public void testZeroElementOnDiagonal() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 0.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(Precision.isClose(e.getValue(), 0.0, 1e-12));
            assertTrue(Precision.isClose(e.getRow(), 1, 1e-12));
            assertTrue(Precision.isClose(e.getSmall(), 0.0, 1e-12));
        }
    }

    @Test
    public void testZeroElementOnSubdiagonal() {
        final RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0, 0.0}, {0.0, 3.0, 0.0}, {0.0, 0.0, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(Precision.isClose(e.getValue(), 0.0, 1e-12));
            assertTrue(Precision.isClose(e.getRow(), 2, 1e-12));
            assertTrue(Precision.isClose(e.getSmall(), 0.0, 1e-12));
        }
    }
}