package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals("Unexpected P matrix", expectedP, actualP);
        assertEquals("Unexpected T matrix", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithSingularMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createSingularMatrix(3);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertNotNull("Transformer should have calculated matrices", actualP);
        assertNotNull("Transformer should have calculated matrices", actualT);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {0.1, 0.2, 0.3},
                {0.4, 0.5, 0.6},
                {0.7, 0.8, 0.9}
        });
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertNotNull("Transformer should have calculated matrices", actualP);
        assertNotNull("Transformer should have calculated matrices", actualT);

        double[][] expectedP = {
                {0.6327988461274388, 0.1187522525380227, -0.7745966692360323},
                {0.1187522525380227, 0.8594931881383838, -0.408248290463863},
                {-0.7745966692360323, -0.408248290463863, 0.3853556783428634}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("Unexpected P[" + i + "][" + j + "]", expectedP[i][j], actualP.getEntry(i, j), 0.001);
            }
        }

        double[][] expectedT = {
                {-0.4494897427831788, -0.7059221456318495, 0.4323127388503816},
                {-0.7059221456318495, 0.6065326677595763, -0.5225952563387955},
                {0.4323127388503816, -0.5225952563387955, 0.7320506329296435}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("Unexpected T[" + i + "][" + j + "]", expectedT[i][j], actualT.getEntry(i, j), 0.001);
            }
        }
    }
}