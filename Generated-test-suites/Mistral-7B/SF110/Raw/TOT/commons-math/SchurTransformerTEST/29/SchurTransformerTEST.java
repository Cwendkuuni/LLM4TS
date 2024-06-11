package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("The transformed P should be the same as the input matrix.", expectedP, actualP);
        assertEquals("The transformed T should be a upper triangular matrix.", actualT.isUpperTriangular(), true);
        assertEquals("The transformed T should be equal to the transpose of the transformed P.", actualT.equals(actualP.transpose()), true);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.random(2, 3);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        RealMatrix inputMatrix = MatrixUtils.random(5, 5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();
        RealMatrix expectedPT = transformer.getPT();

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPT = transformer.getPT();

        assertEquals("The transformed P should be the same as the calculated P.", expectedP, actualP);
        assertEquals("The transformed T should be a upper triangular matrix.", actualT.isUpperTriangular(), true);
        assertEquals("The transformed PT should be the same as the transpose of the transformed T.", actualPT.equals(actualT.transpose()), true);

        // Test for the accuracy of the transformed matrix
        double epsilon = Precision.EPSILON;
        for (int i = 0; i < expectedP.getRowDimension(); ++i) {
            for (int j = 0; j < expectedP.getColumnDimension(); ++j) {
                double diff = FastMath.abs(expectedP.getEntry(i, j) - actualP.getEntry(i, j));
                if (diff > epsilon) {
                    fail("The transformed P does not match the calculated P: |P[" + i + ", " + j + "] = " + diff
                            + " instead of " + expectedP.getEntry(i, j) + ".");
                }
            }
        }

        for (int i = 0; i < expectedT.getRowDimension(); ++i) {
            for (int j = 0; j < expectedT.getColumnDimension(); ++j) {
                double diff = FastMath.abs(expectedT.getEntry(i, j) - actualT.getEntry(i, j));
                if (diff > epsilon) {
                    fail("The transformed T does not match the calculated T: |T[" + i + ", " + j + "] = " + diff
                            + " instead of " + expectedT.getEntry(i, j) + ".");
                }
            }
        }

        for (int i = 0; i < expectedPT.getRowDimension(); ++i) {
            for (int j = 0; j < expectedPT.getColumnDimension(); ++j) {
                double diff = FastMath.abs(expectedPT.getEntry(i, j) - actualPT.getEntry(i, j));
                if (diff > epsilon) {
                    fail("The transformed PT does not match the calculated PT: |PT[" + i + ", " + j + "] = " + diff
                            + " instead of " + expectedPT.getEntry(i, j) + ".");
                }
            }
        }
    }
}