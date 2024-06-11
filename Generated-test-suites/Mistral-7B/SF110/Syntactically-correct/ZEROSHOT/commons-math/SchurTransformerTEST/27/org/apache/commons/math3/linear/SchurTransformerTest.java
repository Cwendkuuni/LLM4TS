package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53571313, 0.85455292, 0.0}, {0.85455292, 0.53571313, 0.0}, {-0.08164586, 0.0, 1.}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-1.81193392, -3.33786563}, {-3.33786563, -6.43982126}});
        RealMatrix expectedPT = expectedP.transpose();

        assertEquals("getP() failed", expectedP, schurTransformer.getP());
        assertEquals("getPT() failed", expectedPT, schurTransformer.getPT());
        assertEquals("getT() failed", expectedT, schurTransformer.getT());

        try {
            new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testGetMinimalUpperTriangularMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix minimalUpperTriangularMatrix = schurTransformer.getT();

        // Test that the minimal upper triangular matrix is symmetric
        RealMatrix transposeMatrix = minimalUpperTriangularMatrix.transpose();
       assertArrayEquals("The minimal upper triangular matrix should be symmetric", minimalUpperTriangularMatrix.getData(), transposeMatrix.getData());
    }

    @Test
    public void testGetTransformedMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix transformedMatrix = schurTransformer.getPT();

        RealMatrix expectedTransformedMatrix = new Array2DRowRealMatrix(new double[][]{{1.53571313, -0.53571313, -1.81193392},
                                                                               {-0.53571313, 1.53571313, -3.33786563},
                                                                               {-1.81193392, -3.33786563, 1.}});
        assertArrayEquals("getPT() failed", expectedTransformedMatrix.getData(), transformedMatrix.getData());
    }
}