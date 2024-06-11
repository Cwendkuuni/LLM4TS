package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {0.0, 1.0}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 1.0}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransformWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(3);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransformWithSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 1.5}, {1.5, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.741, -0.436}, {-0.436, 0.741}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.732, 0.0}, {0.0, 1.732}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP, 1.0E-9);
        assertEquals(expectedT, actualT, 1.0E-9);
    }

    @Test
    public void testTransformWithDiagonalMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.447, 0.0}, {-0.447, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{2.0, 0.0}, {0.0, 3.0}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP, 1.0E-9);
        assertEquals(expectedT, actualT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithNonConvergentMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {0.0, 1.0}});
        new SchurTransformer(matrix);
    }
}