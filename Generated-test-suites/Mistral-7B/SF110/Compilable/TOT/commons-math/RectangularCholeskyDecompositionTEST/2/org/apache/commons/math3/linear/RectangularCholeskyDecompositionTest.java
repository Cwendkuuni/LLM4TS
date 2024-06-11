package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        // Positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][]{{1.41421356, 0.70710678}, {0.70710678, 1.41421356}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals(expectedRoot, actualRoot);
        assertEquals(2, decomposition.getRank());
    }
}