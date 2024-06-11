package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix matrix = RealMatrixFactory.identityMatrix(5);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = RealMatrixFactory.identityMatrix(5);
        RealMatrix expectedT = RealMatrixFactory.identityMatrix(5);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertEquals("The output P matrix should be equal to the identity matrix", expectedP, actualP);
        assertEquals("The output T matrix should be equal to the identity matrix", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = RealMatrixFactory.identityMatrix(3).addColumnVector(RealMatrixFactory.columnVector(new double[] {1, 2, 3, 4}));
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerWithNonHessenbergMatrix() {
        RealMatrix matrix = RealMatrixFactory.randomMatrix(6, 6);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertFalse("The matrix should not be Hessenberg after transformation", matrix.isHessenberg());
        assertTrue("The P matrix should be orthogonal", actualP.isOrthogonal());
        assertTrue("The T matrix should be upper triangular", actualT.isUpperTriangular());
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix matrix = RealMatrixFactory.randomMatrix(5, 5);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        RealMatrix expectedP = RealMatrixFactory.identityMatrix(5);
        RealMatrix expectedT = new RealMatrix(new double[][] {
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}
        });
        assertArrayEquals("The P matrix should be equal to the identity matrix", expectedP.getData(), actualP.getData(), 0.0);
        assertArrayEquals("The T matrix should be equal to the expected matrix", expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithNonConvergentMatrix() {
        RealMatrix matrix = RealMatrixFactory.randomMatrix(10, 10);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
    }
}