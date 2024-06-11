package org.apache.commons.math3.linear;

import org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.84852813, 1.0, 0.0}, {0.27745967, 0.58778525, 1.0}});
        RealMatrix actualRoot = decomp.getRootMatrix();
        assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
        assertEquals("Rank should be 3.", 3, decomp.getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecompositionWithSmallThreshold() {
        RealMatrix matrixWithSmallThreshold = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.84852813, 1.0, 0.0}, {0.27745967, 0.58778525, 1.0}});
        RealMatrix actualRoot = new RectangularCholeskyDecomposition(matrixWithSmallThreshold, 0.0001).getRootMatrix();
        assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
        assertEquals("Rank should be 3.", 3, new RectangularCholeskyDecomposition(matrixWithSmallThreshold, 0.0001).getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecompositionWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 2.99999999, 4.0}, {3.0, 4.0, 5.0}});
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.84852813, 1.0, 0.0}, {0.27745967, 0.58778525, 1.0}});
        RealMatrix actualRoot = new RectangularCholeskyDecomposition(matrix, 0.001).getRootMatrix();
        assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
        assertEquals("Rank should be 3.", 3, new RectangularCholeskyDecomposition(matrix, 0.001).getRank());
    }
}