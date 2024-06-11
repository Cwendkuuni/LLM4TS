package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.Assert;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Category(org.apache.commons.math3.linear.CholeskyDecompositionTest.class)
public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 3.0, 4.0}, {3.0, 5.0, 5.0}, {4.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(inputMatrix);
        // Additional assertions if needed
    }

    @Test
    public void testConstructorMatrixSmall() throws NonPositiveDefiniteMatrixException {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 3.0, 4.0}, {3.0, 5.0, 5.0}, {4.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(inputMatrix, 0.001);
        // Additional assertions if needed
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {-1.0, 3.0}});
        RealMatrix actualRootMatrix = objectUnderTest.getRootMatrix();
        assertEquals(expectedRootMatrix, actualRootMatrix);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(matrix);
        int expectedRank = 2;
        int actualRank = objectUnderTest.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        // Create and call the constructor
        new RectangularCholeskyDecomposition(inputMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{}});
        // Create and call the constructor
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(inputMatrix));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentNumberOfRowsAndColumns() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        // Create and call the constructor
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(inputMatrix));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSmallParameter() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        // Create and call the constructor with a negative small parameter
        assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(inputMatrix, -0.1));
    }

    @Test
    public void testRankMinimumNumberOfRows() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(inputMatrix);
        int expectedRank = 2;
        int actualRank = objectUnderTest.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testSymmetricRootMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition objectUnderTest = new RectangularCholeskyDecomposition(inputMatrix);
        RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0}, {2.0, 4.0}});
        RealMatrix actualRootMatrix = objectUnderTest.getRootMatrix();
        assertEquals(expectedRootMatrix, actualRootMatrix);
        assertEquals(expectedRootMatrix.transpose(), actualRootMatrix);
    }
}