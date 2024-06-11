package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.MathArrays;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testThrowsExceptionForNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNull(transformer.getP());
        assertNull(transformer.getPT());
        assertNull(transformer.getT());
    }

    @Test
    public void testSmallMatrix() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0}, {2.0, 2.5, 4.5}, {3.0, 4.5, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, -0.8414503531625801},
                {0.0, 1.0, 0.4214283465812901}, {-0.8414503531625801, 0.4214283465812901, 0.382683432365134}});
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {2.0, 2.641033653625862, 3.731315149532971},
                {3.0, 3.731315149532971, 4.229988871606183}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualPT = transformer.getPT();
        RealMatrix actualT = transformer.getT();

        PrecisionUtils.assertEquals(expectedP, actualP, 1e-12, 1e-12);
        PrecisionUtils.assertEquals(expectedPT, actualPT, 1e-12, 1e-12);
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-12, 1e-12);
    }

    @Test
    public void testLargeMatrix() {
        double[][] matrixData = new double[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                matrixData[i][j] = Math.random();
            }
        }
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[100][100]);
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[100][100]);

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                double value = Math.random();
                expectedP.setEntry(i, j, value);
                expectedT.setEntry(i, j, value);
            }
        }

        RealMatrix actualP = transformer.getP();
        RealMatrix actualPT = transformer.getPT();
        RealMatrix actualT = transformer.getT();

        PrecisionUtils.assertEquals(expectedP, actualP, 1e-12, 1e-12);
        PrecisionUtils.assertEquals(expectedPT, actualPT, 1e-12, 1e-12);
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-12, 1e-12);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testConvergenceFailedException() {
        double[][] matrixData = new double[][]{{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}