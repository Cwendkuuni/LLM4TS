package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{0.4472136022706633, 0.8164965809277261, 1.221729300322523},
                {-0.7745966692414835, 1.3723870754922946, 1.9963969104238231},
                {-1.221729300322523, -0.9317152548268651, 3.240969651762339}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not as expected", expectedRoot, actualRoot);
        assertEquals("Rank is not as expected", 3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -10.0}});
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.001);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithDifferentMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            new RectangularCholeskyDecomposition(MatrixUtils.createRealMatrix(3, 3), matrix.getEntry(0, 0));
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithSameMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix, 0.0);
            RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix root1 = decomposition1.getRootMatrix();
            RealMatrix root2 = decomposition2.getRootMatrix();
            assertEquals("Root matrices are not equal", root1, root2);
            int rank1 = decomposition1.getRank();
            int rank2 = decomposition2.getRank();
            assertEquals("Ranks are not equal", rank1, rank2);
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}