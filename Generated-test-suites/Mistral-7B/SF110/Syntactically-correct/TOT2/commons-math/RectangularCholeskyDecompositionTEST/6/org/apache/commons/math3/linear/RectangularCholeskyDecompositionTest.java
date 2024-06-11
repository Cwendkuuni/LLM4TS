package org.apache.commons.math3.linear;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.junit.jupiter.api.*;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithNullMatrix() {
        RealMatrix matrix = null;
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testRectangularCholeskyDecompositionWithValidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();
        assertEquals(2, rank);
        MatrixUtils.assertEquals(
                new RealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.89443}, {0.0, 0.44721}}),
                root);
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmallArgument() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1.0e-10);
        RealMatrix root = decomp.getRootMatrix();
        int rank = decomp.getRank();
        assertEquals(2, rank);
        MatrixUtils.assertEquals(
                new RealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.89443}, {0.0, 0.44721}}),
                root);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        int expectedRank = 2;
        int actualRank = decomp.getRank();
        assertEquals(expectedRank, actualRank);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}});
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.89443}, {0.0, 0.44721}});
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix result = decomp.getRootMatrix();
        MatrixUtils.assertEquals(expectedResult, result);
    }

    // Add more test cases for edge cases and exceptions as required

    @AfterAll
    public static void cleanUp() {
        // Perform any clean up tasks necessary
    }
}