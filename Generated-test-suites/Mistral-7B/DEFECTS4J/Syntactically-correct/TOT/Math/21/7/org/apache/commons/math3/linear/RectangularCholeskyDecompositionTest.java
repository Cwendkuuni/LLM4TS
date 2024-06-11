package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link RectangularCholeskyDecomposition}.
 */
public class RectangularCholeskyDecompositionTest {

    /**
     * Test decomposition of a positive definite matrix.
     */
    @Test
    public void testDecompositionPositiveDefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root = decomp.getRootMatrix();
            int rank = decomp.getRank();
            assertTrue(rank == 3);
            assertEquals(3.0, Precision.doubleEquals(root.getEntry(0, 0), 3.0, 1e-8));
            assertEquals(1.5, Precision.doubleEquals(root.getEntry(1, 0), 1.5, 1e-8));
            assertEquals(1.1581138830084189, Precision.doubleEquals(root.getEntry(2, 0), 1.1581138830084189, 1e-8));
            assertEquals(2.0, Precision.doubleEquals(root.getEntry(0, 1), 2.0, 1e-8));
            assertEquals(1.414213741284114, Precision.doubleEquals(root.getEntry(1, 1), 1.414213741284114, 1e-8));
            assertEquals(1.847759972569129, Precision.doubleEquals(root.getEntry(2, 1), 1.847759972569129, 1e-8));
            assertEquals(1.5, Precision.doubleEquals(root.getEntry(0, 2), 1.5, 1e-8));
            assertEquals(3.1622776601683795, Precision.doubleEquals(root.getEntry(1, 2), 3.1622776601683795, 1e-8));
            assertEquals(4.472135954998881, Precision.doubleEquals(root.getEntry(2, 2), 4.472135954998881, 1e-8));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test decomposition of a positive semidefinite matrix with small diagonal elements.
     */
    @Test
    public void testDecompositionPositiveSemidefinite() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, 0.5, 6.0},
            {3.0, 6.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix root = decomp.getRootMatrix();
            int rank = decomp.getRank();
            assertTrue(rank == 2);
            assertEquals(3.0, Precision.doubleEquals(root.getEntry(0, 0), 3.0, 1e-8));
            assertEquals(1.5, Precision.doubleEquals(root.getEntry(0, 1), 1.5, 1e-8));
            assertEquals(1.1547005383792516, Precision.doubleEquals(root.getEntry(1, 1), 1.1547005383792516, 1e-8));
            assertEquals(2.0, Precision.doubleEquals(root.getEntry(0, 2), 0.0, 1e-8));
            assertEquals(0.0, Precision.doubleEquals(root.getEntry(1, 2), 0.0, 1e-8));
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test decomposition of a matrix with negative diagonal elements.
     */
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testDecompositionNegativeDiagonal() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 6.0},
            {3.0, 6.0, -2.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

}