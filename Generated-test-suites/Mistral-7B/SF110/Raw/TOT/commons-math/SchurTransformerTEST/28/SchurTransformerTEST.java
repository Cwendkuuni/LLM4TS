package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerWithIdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.identity(3);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.identity(3);
        RealMatrix expectedT = MatrixUtils.identity(3);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        Assert.assertArrayEquals("Unexpected P matrix", expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals("Unexpected T matrix", expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.identity(2, 3);
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformerWithRandomMatrix() {
        int rows = 3;
        int columns = 3;
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[rows * columns]);
        for (int i = 0; i < rows * columns; ++i) {
            inputMatrix.setEntry(i / columns, i % columns, Math.random());
        }
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix expectedPt = actualP.transpose();

        Assert.assertEquals("Unexpected size of P matrix", rows, actualP.getRowDimension());
        Assert.assertEquals("Unexpected size of P matrix", columns, actualP.getColumnDimension());
        Assert.assertEquals("Unexpected size of T matrix", rows, actualT.getRowDimension());
        Assert.assertEquals("Unexpected size of T matrix", columns, actualT.getColumnDimension());

        // Check that P is orthogonal
        RealMatrix PTranspose = expectedPt;
        RealMatrix productPT = actualP.multiply(PTranspose);
        RealMatrix identity = MatrixUtils.identity(rows);
        RealMatrix error = productPT.subtract(identity);
        double tolerance = Precision.SAFE_MIN;
        Assert.assertTrue("P is not orthogonal", MatrixUtils.isIdentityWithinTolerance(error, tolerance));

        // Check that T is upper triangular
        for (int i = 0; i < rows - 1; ++i) {
            for (int j = i + 1; j < columns; ++j) {
                double value = actualT.getEntry(i, j);
                Assert.assertEquals("Unexpected non-zero value in T", 0.0, value, 1e-10);
            }
        }
    }

    @Test
    public void testSchurTransformerConvergence() {
        double[][] inputData = new double[][]{{1.0, 0.1, 1.0}, {0.1, 3.0, -0.1}, {1.0, -0.1, 2.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputData);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        // Check that the transformation was successful
        Assert.assertArrayEquals("Unexpected P matrix", new double[][]{{0.57357761, 0.57357761, 0.0},
                {0.57357761, 0.81649658, -0.04519023}, {-0.04519023, -0.81649658, 0.57357761}},
                actualP.getData(), 0.0);

        Assert.assertArrayEquals("Unexpected T matrix", new double[][]{{1.0, 0.0, 0.0},
                {0.0, 3.0, 0.0}, {0.0, 0.0, 2.0}}, actualT.getData(), 0.0);
    }
}