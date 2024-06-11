package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowsRealMatrix;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.identity(4);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.identity(4);
        RealMatrix expectedT = MatrixUtils.identity(4);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testSchurTransformerWithUpperTriangularMatrix() {
        double[][] inputMatrixData = {
            {1.0, 2.0, 0.0, 0.0},
            {2.0, 3.0, 1.0, 0.0},
            {0.0, 1.0, 3.0, 2.0},
            {0.0, 0.0, 2.0, 4.0}
        };
        RealMatrix inputMatrix = new Array2DRowsRealMatrix(inputMatrixData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowsRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0}});
        RealMatrix expectedT = new Array2DRowsRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {0.0, 3.0, 3.0, 4.0},
                {0.0, 0.0, 4.0, 4.0},
                {0.0, 0.0, 0.0, 4.0}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testSchurTransformerWithNonSquareMatrix() {
        double[][] inputMatrixData = {
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        };
        RealMatrix inputMatrix = new Array2DRowsRealMatrix(inputMatrixData);
        assertThrows(NonSquareMatrixException.class, () -> new SchurTransformer(inputMatrix));
    }

    @Test
    public void testSchurTransformerWithMaxIterations() {
        double[][] inputMatrixData = {
            {1.0, 2.0, 0.0, 0.0},
            {2.0, 3.0, 1.0, 0.0},
            {0.0, 1.0, 3.0, 2.0},
            {0.0, 0.0, 2.0, 1000000.0}
        };
        RealMatrix inputMatrix = new Array2DRowsRealMatrix(inputMatrixData);
        assertThrows(MaxCountExceededException.class, () -> new SchurTransformer(inputMatrix));
    }
}