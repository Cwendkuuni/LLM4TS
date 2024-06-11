package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        try {
            new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}
        });
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        Assert.assertEquals(expectedP, p);
        Assert.assertEquals(expectedT, t);
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 2.5, 3.0, 4.5, 6.0});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {
                {0.9999999999999997, 0.0, 0.0, 0.0, 0.0},
                {0.0, 0.9999999999999997, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.9999999999999998, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.9999999999999999, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}
        });
        RealMatrix expectedT = MatrixUtils.createRealDiagonalMatrix(new double[]{2.5, 3.0, 4.5, 5.5, 6.0});
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        Assert.assertEquals(expectedP, p);
        Assert.assertEquals(expectedT, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsReached() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0},
        });
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}