package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);

        Assert.assertArrayEquals(expectedP.getData(), transformer.getP().getData(), 1e-14);
        Assert.assertArrayEquals(expectedT.getData(), transformer.getT().getData(), 1e-14);
    }

    @Test
    public void testSchurTransformer_SingularMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            new SchurTransformer(input);
            Assert.fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSchurTransformer_TriangularMatrix() {
        RealMatrix input = MatrixUtils.createTriangularMatrix(3, true);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();

        Assert.assertEquals(expectedP.getRowDimension(), expectedP.getColumnDimension(), 3);
        Assert.assertEquals(expectedT.getRowDimension(), expectedT.getColumnDimension(), 3);
        Assert.assertTrue(MatrixUtils.isTriangular(expectedP, true));
        Assert.assertTrue(MatrixUtils.isTriangular(expectedT, true));
    }

    @Test
    public void testSchurTransformer_ErrorHandling() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(input);
        } catch (RuntimeException e) {
            // expected
        }
        Assert.assertNull(transformer);
    }
}