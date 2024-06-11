package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testExceptionWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(4);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        // Check that the original matrix and the transformed matrices are equal
        assertEquals(matrix, MatrixUtils.add(MatrixUtils.add(MatrixUtils.subtract(MatrixUtils.transpose(t), pt), p), MatrixUtils.createRealIdentityMatrix(4)));
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(matrix);
        } catch (MaxCountExceededException e) {
            // expected
        }
        assertNull(transformer.getP());
        assertNull(transformer.getPT());
        assertNull(transformer.getT());
    }

    @Test
    public void testRandomMatrix() {
        int dimension = 10;
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(dimension, dimension);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        // Check that the original matrix and the transformed matrices are close
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                double difference = FastMath.abs(matrix.getEntry(i, j) - t.getEntry(i, j));
                assertTrue("Difference between original and transformed matrix entries is too large: " + difference, difference < 1e-12 * Precision.MAX_VALUE);
            }
        }
    }
}