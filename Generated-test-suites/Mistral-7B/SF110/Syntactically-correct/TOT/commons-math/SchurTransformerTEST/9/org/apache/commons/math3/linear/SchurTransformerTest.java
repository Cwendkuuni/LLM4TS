package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        Assert.assertEquals(expectedP, actualP);
        Assert.assertEquals(expectedT, actualT);
    }

    @Test
    public void testSchurTransformer_RandomMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRandomMatrix(5, 5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        Assert.assertNotSame(expectedP, actualP);
        Assert.assertNotSame(expectedT, actualT);
        Assert.assertEquals(expectedP.getRowDimension(), actualP.getRowDimension());
        Assert.assertEquals(expectedP.getColumnDimension(), actualP.getColumnDimension());
        Assert.assertEquals(expectedT.getRowDimension(), actualT.getRowDimension());
        Assert.assertEquals(expectedT.getColumnDimension(), actualT.getColumnDimension());
        PrecisionUtils.assertEquals(expectedP.getEntry(0, 0), actualP.getEntry(0, 0), 1e-10);
        PrecisionUtils.assertEquals(expectedT.getEntry(0, 0), actualT.getEntry(0, 0), 1e-10);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }
}