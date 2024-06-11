package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionException;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerIdentityMatrix() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer schurTransformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerNonSquareMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformerDiagonalMatrix() {
        RealMatrix input = MatrixUtils.createDiagonalMatrix(new double[]{1.0, 2.5, 3.0, 4.0, 5.0});
        SchurTransformer schurTransformer = new SchurTransformer(input);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        double[][] expectedPData = new double[][]{
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}
        };

        Assert.assertArrayEquals(expectedPData, actualP.getData(), 0.0);

        double[][] expectedTData = new double[][]{
                {1.5, 0.0, 0.0, 0.0, 0.0},
                {0.0, 2.5, 0.0, 0.0, 0.0},
                {0.0, 0.0, 3.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 4.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 5.0}
        };

        Assert.assertArrayEquals(expectedTData, actualT.getData(), 0.0);
    }

    @Test
    public void testSchurTransformerSymmetricMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        SchurTransformer schurTransformer = new SchurTransformer(input);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        double[][] expectedPData = new double[][]{
                {0.5, 0.5},
                {0.5, 0.5}
        };

        Assert.assertArrayEquals(expectedPData, actualP.getData(), 0.0);

        double[][] expectedTData = new double[][]{
                {1.1213671819949501, 0.0},
                {0.0, 1.1213671819949501}
        };

        Assert.assertArrayEquals(expectedTData, actualT.getData(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerMaxIterationsExceeded() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[]{
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0
        });
        SchurTransformer schurTransformer = new SchurTransformer(input);
    }
}