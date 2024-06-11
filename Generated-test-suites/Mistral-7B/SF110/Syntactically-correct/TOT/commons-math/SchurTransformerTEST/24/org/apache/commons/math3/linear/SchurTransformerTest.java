package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix actualP = transformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        RealMatrix expectedT = MatrixUtils.identity(5);
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.randomRealMatrix(new int[]{2, 3});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix matrix = MatrixUtils.randomRealMatrix(new int[]{5, 5});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = transformer.getP();
        RealMatrix actualP = new SchurTransformer(matrix.transpose()).getP().transpose();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        RealMatrix expectedT = transformer.getT();
        RealMatrix actualT = new SchurTransformer(matrix.transpose()).getT().transpose();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
        RealMatrix actualPt = transformer.getPT();
        RealMatrix expectedPt = actualP.transpose().multiply(actualT);
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 0.0);
    }
}