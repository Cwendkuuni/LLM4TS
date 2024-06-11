package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.eye(5);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.eye(5);
        RealMatrix expectedT = MatrixUtils.eye(5);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData());
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0},
                {2.0, 3.0},
                {4.0, 5.0, 6.0}
        });
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithDifferentInputMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new RealMatrix(new double[][]{
                {-0.56175806, 0.81649658},
                {0.81649658, 0.56175806}
        });
        RealMatrix expectedT = new RealMatrix(new double[][]{
                {1.41421374, -0.71211873},
                {-0.71211873, 1.41421374}
        });

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData());
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test
    public void testSchurTransformerWithLargeInputMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(100, 100);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new RealMatrix(100, 100);
        RealMatrix expectedT = new RealMatrix(100, 100);

        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                expectedP.setEntry(i, j, Precision.random(1000000.0));
                expectedT.setEntry(i, j, Precision.random(1000000.0));
            }
        }

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData());
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test
    public void testSchurTransformerWithZeroDiagonalMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {0.0, 0.0},
                {0.0, 0.0}
        });
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[]{
                {1.0, 0.0},
                {0.0, 1.0}
        });

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData());
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test
    public void testSchurTransformerWithSmallSubDiagonalElement() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 0.1}
        });
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[]{
                {0.9999999983170089, 0.03420201203330275},
                {0.03420201203330275, 0.9999999967333926}
        });
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[]{
                {1.000000001221103, -0.000000001221103},
                {-0.000000001221103, 1.000000001221103}
        });

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData());
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }
}