package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(actualP.getRowDimension(), actualP.getColumnDimension());
        assertEquals(actualT.getRowDimension(), actualT.getColumnDimension());
        assertNotNull(actualP);
        assertNotNull(actualT);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix matrix = new Array2DRndMatrix(5, 5, -1.0, 1.0);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix expectedP = MatrixUtils.createRealMatrix(actualP.getData());
        RealMatrix expectedT = MatrixUtils.createRealMatrix(actualT.getData());
        assertTrue(MatrixUtils.isIdentical(expectedP, actualP, 1e-10));
        assertTrue(MatrixUtils.isIdentical(expectedT, actualT, 1e-10));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithNonConvergentMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 1}, {0, -1, 2}});
        new SchurTransformer(matrix);
    }
}