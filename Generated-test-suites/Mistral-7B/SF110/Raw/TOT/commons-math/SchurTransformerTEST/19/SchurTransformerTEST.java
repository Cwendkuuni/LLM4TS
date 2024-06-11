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
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {5.0, 3.0, 2.0},
                {1.0, 4.0, -1.0},
                {6.0, -2.0, 7.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{
                {0.430351423, 0.883100044, -0.078283773},
                {0.883100044, 0.739627205, 0.206336515},
                {-0.078283773, 0.206336515, 0.983144615}
        });
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{
                {5.0, 3.0, 2.0},
                {-3.51381762, 4.23274213, 1.39695684},
                {-0.16847698, -0.46118851, 7.24791289}
        });

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test
    public void testSchurTransformerWithSingularMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        RealMatrix expectedP = MatrixUtils.identity(2);
        RealMatrix expectedT = MatrixUtils.identity(2);

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithMatrixThatDoesNotConverge() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        new SchurTransformer(inputMatrix);
    }
}