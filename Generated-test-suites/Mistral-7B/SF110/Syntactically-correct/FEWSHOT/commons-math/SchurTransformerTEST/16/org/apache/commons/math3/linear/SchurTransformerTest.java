package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0 }, { 3.0 } });
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerGetters() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 2.0, 3.0 }, { 2.0, 3.0, 4.0 }, { 3.0, 4.0, 5.0 } });
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertEquals(p.getRowDimension(), p.getColumnDimension(), 3);
        assertEquals(pt.getRowDimension(), pt.getColumnDimension(), 3);
        assertEquals(t.getRowDimension(), t.getColumnDimension(), 3);

        assertTrue(MatrixUtils.isIdentityMatrix(pt.multiply(p)));
        assertTrue(MatrixUtils.isIdentityMatrix(p.multiply(pt)));
        assertTrue(MatrixUtils.isIdentityMatrix(t.transpose().multiply(t)));
    }

    @Test
    public void testSchurTransformerTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 2.0, 1.0, 1.0 }, { 1.0, 2.0, 1.0 }, { 1.0, 1.0, 2.0 } });
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        assertEquals(p.getRowDimension(), p.getColumnDimension(), 3);
        assertEquals(t.getRowDimension(), t.getColumnDimension(), 3);

        // Check that the transformation was performed correctly
        double[][] expectedT = { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.121125551812737 } };
        double[][] expectedP = { { 0.5773502691896258, -0.5773502691896258, 0.5773502691896258 },
                { -0.5773502691896258, 0.5773502691896258, -0.5773502691896258 },
                { 0.5773502691896258, 0.5773502691896258, 0.8164966132043211 } };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expectedT[i][j], t.getEntry(i, j), 1.0e-12);
                assertEquals(expectedP[i][j], p.getEntry(i, j), 1.0e-12);
            }
        }

        // Check that the precision is preserved
        double epsilon = Precision.EPSILON;
        double smallValue = 1.0e-20;
        double norm = 0.0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                norm += Utils.abs(t.getEntry(i, j));
                assertEquals(PrecisionUtils.isClose(expectedT[i][j], t.getEntry(i, j), epsilon * norm), true);
            }
        }
    }
}