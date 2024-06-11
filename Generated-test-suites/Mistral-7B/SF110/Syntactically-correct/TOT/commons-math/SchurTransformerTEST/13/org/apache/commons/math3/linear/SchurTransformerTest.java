package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.apache.commons.math3.linear.MatrixUtils.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerIdentityMatrix() {
        RealMatrix input = identity(3);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = identity(3);
        RealMatrix expectedT = identity(3);
        RealMatrix expectedPt = transpose(identity(3));

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 1e-14);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 1e-14);
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 1e-14);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerNonSquareMatrix() {
        RealMatrix input = create(3, 2);
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformerRandomMatrix() {
        RealMatrix input = createRandomMatrix(3, 3);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();
        RealMatrix expectedPt = transformer.getPT();

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();

        double[][] actualPData = actualP.getData();
        double[][] expectedPData = expectedP.getData();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(MathUtils.round(expectedPData[i][j], 10), MathUtils.round(actualPData[i][j], 10), 1e-14);
            }
        }

        double[][] actualTData = actualT.getData();
        double[][] expectedTData = expectedT.getData();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(MathUtils.round(expectedTData[i][j], 10), MathUtils.round(actualTData[i][j], 10), 1e-14);
            }
        }

        double[][] actualPtData = actualPt.getData();
        double[][] expectedPtData = expectedPt.getData();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(MathUtils.round(expectedPtData[i][j], 10), MathUtils.round(actualPtData[i][j], 10), 1e-14);
            }
        }
    }

    @Test
    public void testSchurTransformerDiagonalMatrix() {
        RealMatrix input = create(3, 3, x -> PrecisionUtils.round(x, 5));
        setIdentityDiagonal(input);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = identity(3);
        RealMatrix expectedT = input;
        RealMatrix expectedPt = transpose(identity(3));

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 1e-14);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 1e-14);
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 1e-14);
    }

    @Test
    public void testSchurTransformerSingularMatrix() {
        RealMatrix input = create(3, 3);
        setDiagonal(input, 0.0);
        set(input, 0, 0, 1.0);
        SchurTransformer transformer = new SchurTransformer(input);
        Assert.assertNull(transformer.getP());
        Assert.assertNull(transformer.getT());
        Assert.assertNull(transformer.getPT());
    }
}