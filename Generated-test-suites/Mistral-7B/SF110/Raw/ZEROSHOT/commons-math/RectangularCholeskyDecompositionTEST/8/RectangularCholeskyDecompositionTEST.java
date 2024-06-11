package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testValidDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.73205080756888, 0.6, 0.0}, {0.0, -0.44948974278317, 0.81649658092773}});
        RealMatrix actualResult = decomposition.getRootMatrix();
        Assert.assertEquals(expectedResult.getRowDimension(), actualResult.getRowDimension());
        Assert.assertEquals(expectedResult.getColumnDimension(), actualResult.getColumnDimension());
        Assert.assertArrayEquals(expectedResult.getData(), actualResult.getData(), 0.001);
    }

    @Test
    public void testInvalidDecompositionWithNegativeValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidDecompositionWithSmallValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 0.5}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        int actualRank = decomposition.getRank();
        int expectedRank = 3;
        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testSingularValueDecompositionWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException for Identity Matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecompositionWithSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 2.0}, {2.0, 2.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Should have thrown NonPositiveDefiniteMatrixException for Symmetric Matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecompositionWithPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 2.0, 0.0}, {2.0, 4.0, 2.0}, {0.0, 2.0, 4.0}});
        RectangularCholeskyDecomposition decomposition = null;
        try {
            decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Ignore
        }
        Assert.assertNotNull(decomposition);
    }
}