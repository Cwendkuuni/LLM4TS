package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RectangularCholeskyDecompositionTest {

    private final RealMatrix originalMatrix;
    private final double small;

    public RectangularCholeskyDecompositionTest(RealMatrix matrix, double small) {
        this.originalMatrix = matrix;
        this.small = small;
    }

    @Parameterized.Parameters(name = "{index}: small={small}, rank={rank}, expectedRootMatrix={expectedRootMatrix}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                new Object[]{new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 4.0}}), 0.0, new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}}), 1},
                new Object[]{new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 7.0}}), 0.0, null, 0},
                new Object[]{new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.5}}), 0.0, null, 0},
                new Object[]{new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.5}}), 1.0E-10, new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 1.5}, {0.0, 1.5, 2.5}}), 3},
                new Object[]{new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}), 1.0E-8, new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}), 2}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testConstruction() {
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(originalMatrix, small);
            assertNotNull(decomposition.getRootMatrix());
            assertEquals(getRank(), decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Non-positive definite matrix", e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(originalMatrix, small);
        assertEquals(getRank(), decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(originalMatrix, small);
        RealMatrix expectedRootMatrix = (RealMatrix)getData()[getIndex()] [2];
        assertArrayEquals(expectedRootMatrix.getData(), decomposition.getRootMatrix().getData());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}}), small);
    }

    @Test
    public void testNonPositiveDiagonalElements() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}}), small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("-1.0", e.getNegativeElement()[0][1]);
            assertEquals(1, e.getNegativeElement().length);
        }
    }

    @Test
    public void testZeroRank() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}}), small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("0.0, 0.0", e.getNegativeElement()[0][0]);
            assertEquals(1, e.getNegativeElement().length);
        }
    }

    @Test
    public void testSmallNegativeElements() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, MathUtils.nextDown(0.0)}}), small);
        assertEquals(2, decomposition.getRank());
        assertNull(decomposition.getRootMatrix());
    }

    @Test
    public void testRootMatrixWithSmallNegativeElements() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, MathUtils.nextDown(0.0)}}), 1.0E-10);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("Non-positive definite matrix", e.getMessage());
        }
    }

    @Test
    public void testNegativeDeterminant() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, -1.0}}), small);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("-1.0", e.getMessage().split("=")[1].split(" ")[0]);
        }
    }

    @Test
    public void testCompareWithMatrixUtilsCholesky() {
        RealMatrix originalMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(originalMatrix, 0.0);
        RealMatrix matrixUtilsCholesky = MatrixUtils.cholesky(originalMatrix);
        assertArrayEquals(matrixUtilsCholesky.getData(), decomposition.getRootMatrix().getData());
    }

    @Test
    public void testCompareWithLAPACK() {
        RealMatrix originalMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 4.0}});
        RealMatrix cholesky = MatrixUtils.createRealMatrix(2, 2);
        DenseMatrix.dgemm(Op.NO_TRANSPOSE, Op.NO_TRANSPOSE, 1.0, originalMatrix, originalMatrix, 0.0, cholesky, 0);
        cholesky = new RealMatrix(cholesky.transpose());
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(originalMatrix, 0.0);
        assertArrayEquals(cholesky.getData(), decomposition.getRootMatrix().getData());
    }

    public int getIndex() {
        return (Integer)getData()[0][0];
    }

    public int getRank() {
        return (Integer)getData()[0][1];
    }
}