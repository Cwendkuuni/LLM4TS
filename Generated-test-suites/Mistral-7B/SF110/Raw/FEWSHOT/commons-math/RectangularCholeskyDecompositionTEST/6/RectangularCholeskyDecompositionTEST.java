package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecompositionPositiveDefinite() {
        final double[][] inputMatrixData = { { 2.0, 1.0, 0.0 }, { 1.0, 2.0, 1.0 }, { 0.0, 1.0, 2.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(inputMatrix);
            RealMatrix resultMatrix = decomp.getRootMatrix();
            assertTrue(decomp.getRank() == 3);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][] { { 1.4142135625, 0.7071067812, 0.0 },
                    { 0.7071067812, 1.4142135625, 0.7071067812 }, { 0.0, 0.7071067812, 1.4142135625 } }, false);
            assertEquals(expectedResult, resultMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testDecompositionSingular() {
        final double[][] inputMatrixData = { { 2.0, 1.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(inputMatrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDecompositionNonSquare() {
        final double[][] inputMatrixData = { { 2.0, 1.0 }, { 1.0, 2.0 }, { 0.0, 1.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(inputMatrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testDecompositionDiagonal() {
        final double[][] inputMatrixData = { { 2.0, 0.0 }, { 0.0, 3.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(inputMatrix);
            RealMatrix resultMatrix = decomp.getRootMatrix();
            assertTrue(decomp.getRank() == 2);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][] { { 1.4142135625, 0.0 }, { 0.0, 1.7320508076 } }, false);
            assertEquals(expectedResult, resultMatrix);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testDecompositionSmallEigenvalues() {
        final double[][] inputMatrixData = { { 2.0, 1.0, 0.0 }, { 1.0, 2.0, 1.0 }, { 0.0, 1.0, 0.0001 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(inputMatrix, 1e-8);
            RealMatrix resultMatrix = decomp.getRootMatrix();
            assertTrue(decomp.getRank() == 3);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][] { { 1.4142135625, 0.7071067812, 0.0 },
                    { 0.7071067812, 1.4142135625, 0.7071067812 }, { 0.0, 0.7071067812, 0.9999999997 } }, false);
            assertEquals(expectedResult, resultMatrix, 1e-8);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException");
        }
    }
}