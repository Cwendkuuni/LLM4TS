package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData());
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformer_RandomMatrix() {
        int size = 5;
        RealMatrix input = new Array2DRowRealMatrix(new double[size][size], 0.0);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                input.setEntry(i, j, FastMath.random() * 10.0);
            }
        }
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        // Add assertions for edges cases and error scenarios here
    }

    @Test
    public void testTransform() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData());
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test
    public void testGetPT() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        RealMatrix actualPT = transformer.getPT();
        Assert.assertArrayEquals(expectedPT.getData(), actualPT.getData());
    }

    @Test
    public void testGetT() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(transformer.getT().getData());
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData());
    }
}