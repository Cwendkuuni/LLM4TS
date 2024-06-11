package org.apache.commons.math3.linear.test;

import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test case for the constructor that takes a RealMatrix as an argument
        double[][] inputMatrix = { {1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0} };
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.6, 1.41421356, 0.0}, {0.3, 1.35355339, 1.57079633}});
        assertEquals(expectedResult, rcd.getRootMatrix());
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        // Test case for the constructor that takes a RealMatrix and a small argument
        double[][] inputMatrix = { {1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0} };
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        try {
            RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 0.001);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // test passed
        }
    }

    @Test
    public void testGetRank() {
        // Test case for the getRank() method
        double[][] inputMatrix = { {1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0} };
        RealMatrix matrix = MatrixUtils.createRealMatrix(inputMatrix);
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, rcd.getRank());

        double[][] singularMatrix = { {0.0, 1.0}, {1.0, 0.0} };
        RealMatrix singularMatrixObj = MatrixUtils.createRealMatrix(singularMatrix);
        try {
            RectangularCholeskyDecomposition rcdSingular = new RectangularCholeskyDecomposition(singularMatrixObj);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // test passed
        }

        double[][] emptyMatrix = {};
        RealMatrix emptyMatrixObj = MatrixUtils.createRealMatrix(emptyMatrix);
        try {
            RectangularCholeskyDecomposition rcdEmpty = new RectangularCholeskyDecomposition(emptyMatrixObj);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // test passed
        }
    }
}