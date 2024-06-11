package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.identity(5);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.random(2, 3);
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix matrix = MatrixUtils.random(5, 5);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = schurTransformer.getP();
        RealMatrix expectedT = schurTransformer.getT();
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        PrecisionUtils.assertEquals(expectedP, actualP, 1e-13);
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-13);
    }

    @Test
    public void testSchurTransformerWithSingularMatrix() {
        RealMatrix matrix = MatrixUtils.singularValueDecomposition(MatrixUtils.identity(3)).getMatrixA();
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.identity(3);
        RealMatrix expectedT = MatrixUtils.singularValueDecomposition(MatrixUtils.identity(3)).getMatrixU();
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }
}