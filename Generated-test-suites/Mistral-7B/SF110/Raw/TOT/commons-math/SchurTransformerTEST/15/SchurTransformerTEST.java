package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(3);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals("Expected P to be identity matrix", expectedP, actualP);
        assertEquals("Expected T to be identity matrix", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformer_RandomMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {2.5, 1.2, 0.3},
                {-1.5, 2.8, 0.9},
                {1.7, -2.6, 4.3}
        });
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();

        // Check that the transformation was successful
        RealMatrix originalP = input.clone();
        RealMatrix originalT = HessenbergTransformer.transform(input);
        assertEquals("Unexpected P after transformation", originalP, expectedP);
        assertEquals("Unexpected T after transformation", originalT, expectedT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_MaxIterationsReached() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        new SchurTransformer(input);
    }
}