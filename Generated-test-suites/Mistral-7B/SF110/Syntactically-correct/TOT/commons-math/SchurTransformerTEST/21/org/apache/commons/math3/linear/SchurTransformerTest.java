package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerIdentityMatrix() {
        RealMatrix input = RealMatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = RealMatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = RealMatrixUtils.createIdentityMatrix(5);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("Actual P matrix does not match expected identity matrix", expectedP, actualP);
        assertEquals("Actual T matrix does not match expected identity matrix", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerNonSquareMatrix() {
        RealMatrix input = RealMatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformerSmallMatrix() {
        RealMatrix input = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = RealMatrixUtils.createRealMatrix(new double[][]{{-0.7071067811865476, -0.7071067811865476}, {0.7071067811865476, 0.7071067811865476}});
        RealMatrix expectedT = RealMatrixUtils.createRealMatrix(new double[][]{{-0.7071067811865476, 0.0, 0.7071067811865476}, {0.0, 1.0, 0.0}, {0.7071067811865476, 0.0, -0.7071067811865476}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("Actual P matrix does not match expected result", expectedP, actualP);
        assertEquals("Actual T matrix does not match expected result", expectedT, actualT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerConvergenceFailed() {
        RealMatrix input = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        new SchurTransformer(input);
    }
}