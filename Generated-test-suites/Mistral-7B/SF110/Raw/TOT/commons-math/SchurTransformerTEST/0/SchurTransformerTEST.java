package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.random(3, 5);
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix inputMatrix = MatrixUtils.random(5, 5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPT = transformer.getPT();

        // Test that multiplication of P and T matrices returns the original input matrix
        RealMatrix expectedInput = inputMatrix.copy();
        RealMatrix expectedOutput = MatrixUtils.createRealMatrix(transformer.matrixT);
        RealMatrix productPT = actualP.multiply(expectedOutput);
        Assert.assertArrayEquals(expectedInput.getData(), productPT.getData(), 0.0);
    }

    @Test
    public void testSchurTransformerWithSingularMatrix() {
        RealMatrix inputMatrix = MatrixUtils.singular(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        // Test if the transformer throws an exception when the input matrix is singular
        Assert.assertThrows(MaxCountExceededException.class, () -> transformer.transform());

        // Test if the getT() method returns the identity matrix when the input matrix is singular
        RealMatrix actualT = transformer.getT();
        Assert.assertEquals(MatrixUtils.identity(5), actualT);
    }
}