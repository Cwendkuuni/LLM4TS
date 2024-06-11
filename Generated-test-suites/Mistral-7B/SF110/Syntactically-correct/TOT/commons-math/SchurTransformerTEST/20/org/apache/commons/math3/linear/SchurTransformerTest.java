package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        Assert.assertEquals("The transformed matrix P should be the identity matrix", p, new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        Assert.assertEquals("The transformed matrix T should be a triangular matrix", t.isTriangular(), true);
        Assert.assertEquals("The transformed matrix PT should be the transpose of the transformed matrix P", pt, p.transpose());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0}, {0.0, 1.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformer_RandomMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.5, -0.2, 0.3}, {0.2, 0.6, 0.1}, {-0.3, 0.1, 0.7}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        // Check that the transformed matrix P and PT are orthogonal
        RealMatrix pTranspose = p.transpose();
        RealVector pProduct = pt.multiply(p);
        RealVector identityVector = new ArrayRealVector(p.getRowDimension(), 1.0);
        Assert.assertEquals("The transformed matrix PT * P should be the identity matrix", pProduct, PrecisionUtils.createEqualEpsilonArray(identityVector, 1e-8));

        // Check that the transformed matrix T is upper triangular
        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                Assert.assertEquals("The elements below the diagonal should be zero", 0.0, t.getEntry(i, j), 1e-8);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_Divergence() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformer_SameElements() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        Assert.assertEquals("The transformed matrix P should be the same as the input matrix", matrix, p);
        Assert.assertEquals("The transformed matrix T should be a diagonal matrix", t.isDiagonal(), true);
        Assert.assertEquals("The transformed matrix PT should be the transpose of the transformed matrix P", matrix.transpose(), pt);
    }
}