package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);

        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        // Test getting matrices
        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix ptMatrix = schurTransformer.getPT();
        RealMatrix tMatrix = schurTransformer.getT();

        // Test getting P, PT, and T matrices are the same as the original ones
        Assert.assertArrayEquals(matrixData, pMatrix.getData().clone());
        Assert.assertArrayEquals(ptMatrix.getData().clone(), MatrixUtils.transpose(pMatrix.getData()).clone());
        Assert.assertArrayEquals(matrixData, tMatrix.getData().clone());

        // Test transform method
        SchurTransformer schurTransformer2 = new SchurTransformer(matrix);
        assertTrue(Double.doubleEquals(schurTransformer.getNorm(), schurTransformer2.getNorm(), 0.0));

        // Test exception for non-square matrix
        try {
            new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        // Test MaxCountExceededException
        try {
            SchurTransformer schurTransformer3 = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}));
            schurTransformer3.transform();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}