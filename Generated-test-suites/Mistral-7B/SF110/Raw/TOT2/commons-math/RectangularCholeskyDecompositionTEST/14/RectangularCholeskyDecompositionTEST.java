package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = rcd.getRootMatrix();
        assertEquals(6.0, root.getEntry(0, 0), Precision.SAFE_EPSILON);
        assertEquals(2.0, root.getEntry(1, 0), Precision.SAFE_EPSILON);
        assertEquals(-0.5, root.getEntry(1, 1), Precision.SAFE_EPSILON);
        assertEquals(1.5, root.getEntry(2, 0), Precision.SAFE_EPSILON);
        assertEquals(1.0, root.getEntry(2, 1), Precision.SAFE_EPSILON);
        assertEquals(-0.5, root.getEntry(2, 2), Precision.SAFE_EPSILON);
        int rank = rcd.getRank();
        assertEquals(3, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNegativeDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0, 3.0}, {2.0, -4.0, 5.0}, {3.0, 5.0, -6.0}});
        try {
            new RectangularCholeskyDecomposition(matrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    // Add other test cases here...

}