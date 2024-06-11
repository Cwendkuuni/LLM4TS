package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        try {
            SchurTransformer transformer = new SchurTransformer(matrix);
            RealMatrix p = transformer.getP();
            RealMatrix pt = transformer.getPT();
            RealMatrix t = transformer.getT();

            // check dimensions
            assertEquals("P should be square", p.getRowDimension(), p.getColumnDimension());
            assertEquals("PT should be square", pt.getRowDimension(), pt.getColumnDimension());
            assertEquals("T should be square", t.getRowDimension(), t.getColumnDimension());

            // check identity
            RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
            RealMatrix pInv = MatrixUtils.inverse(p);
            RealMatrix pTimesPinverse = MatrixUtils.multiply(p, pInv);
            assertEquals("P * P^-1 should be identity", identity, pTimesPinverse, 1e-12);
            RealMatrix pinvTimesP = MatrixUtils.multiply(pInv, p);
            assertEquals("P^-1 * P should be identity", identity, pinvTimesP, 1e-12);

            // check PT * T = I
            RealMatrix ptTimesT = MatrixUtils.multiply(pt, t);
            assertEquals("PT * T should be identity", identity, ptTimesT, 1e-12);

            // check T * PT = I
            RealMatrix tTimesPt = MatrixUtils.multiply(t, pt);
            assertEquals("T * PT should be identity", identity, tTimesPt, 1e-12);

            // check P and PT are orthogonal
            RealMatrix pTranspose = pt;
            RealMatrix pMinusPTranspose = MatrixUtils.subtract(p, pTranspose);
            RealMatrix pMinusPTransposeSquared = MatrixUtils.multiply(pMinusPTranspose, pMinusPTranspose);
            assertEquals("P - PT^T should be zero", MatrixUtils.createRealIdentity(3), pMinusPTransposeSquared, 1e-12);

            // check T is upper triangular
            for (int i = 0; i < t.getRowDimension(); i++) {
                for (int j = i + 1; j < t.getColumnDimension(); j++) {
                    assertTrue("T[" + i + ", " + j + "] should be zero", Precision.isZero(t.getEntry(i, j), 1e-12));
                }
            }

            // check P is orthogonal
            for (int i = 0; i < p.getRowDimension(); i++) {
                for (int j = i; j < p.getColumnDimension(); j++) {
                    for (int k = 0; k < p.getRowDimension(); k++) {
                        double sum = 0.0;
                        for (int l = 0; l < p.getColumnDimension(); l++) {
                            sum += p.getEntry(i, l) * p.getEntry(k, l);
                        }
                        assertEquals("Row " + i + " column " + j + " should sum to delta(" + i + ", " + j + ")",
                                (i == j ? 1.0 : 0.0), sum, 1e-12);
                    }
                }
            }

            // test exceptions
            try {
                new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                        {1.0, 2.0},
                        {2.0, 2.0}
                }));
                fail("Expected NonSquareMatrixException");
            } catch (NonSquareMatrixException e) {
                // expected
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}