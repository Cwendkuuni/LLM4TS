package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.HessenbergTransformer;
import org.apache.commons.math3.util.ShiftInfo;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] matrixData = {{1.0, 2.0, 3.0},
                                {2.0, 4.0, 5.0},
                                {3.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        Assert.assertNotNull(p);
        Assert.assertNotNull(pt);
        Assert.assertNotNull(t);

        double[][] pData = p.getData();
        double[][] tData = t.getData();

        // Check that the original matrix is transformed correctly
        for (int i = 0; i < tData.length; i++) {
            for (int j = 0; j < tData[i].length; j++) {
                assertEquals(Precision.SAFE_EQ, matrixData[i][j], tData[i][j], Precision.EPSILON);
            }
        }

        // Check that P and PT are the transposes of each other
        for (int i = 0; i < pData.length; i++) {
            for (int j = 0; j < pData[i].length; j++) {
                assertEquals(Precision.SAFE_EQ, pData[i][j], ptData[j][i], Precision.EPSILON);
            }
        }

        // Check that the Schur transformation was performed correctly
        // by checking that T is upper triangular and symmetric
        for (int i = 0; i < tData.length; i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(Precision.SAFE_EQ, 0.0, tData[i][j], Precision.EPSILON);
            }
            for (int j = i; j < tData[i].length; j++) {
                assertEquals(Precision.SAFE_EQ, tData[i][j], tData[j][i], Precision.EPSILON);
            }
        }

        // Test exception cases
        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[3][2]));
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[2][3]));
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        try {
            SchurTransformer transformer2 = new SchurTransformer(matrix);
            transformer2.transform(); // Should not reach here
            Assert.fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

}