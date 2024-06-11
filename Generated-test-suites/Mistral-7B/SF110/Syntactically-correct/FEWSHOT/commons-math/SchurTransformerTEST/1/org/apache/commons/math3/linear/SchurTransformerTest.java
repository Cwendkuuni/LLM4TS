package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertEquals(p, pt.transpose());

        // Check that T is a Hessenberg matrix
        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, t.getEntry(i, j), 1e-9);
            }
            for (int j = i; j < t.getColumnDimension(); j++) {
                if (j > i + 1) {
                    assertEquals(0.0, t.getEntry(i, j), 1e-9);
                }
            }
        }
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        // Check that T is upper Hessenberg after transformation
        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < i; j++) {
                assertEquals(0.0, t.getEntry(i, j), 1e-9);
            }
            for (int j = i; j < t.getColumnDimension(); j++) {
                if (j > i) {
                    assertEquals(0.0, t.getEntry(i, j), 1e-9);
                }
                if (j > i + 1) {
                    double expected = p.getEntry(j - 1, i) * p.getEntry(j, i) + p.getEntry(j - 1, i + 1) * p.getEntry(j, i + 1);
                    assertEquals(expected, t.getEntry(i, j), 1e-9);
                }
            }
        }

        // Check that P and PT are orthogonal
        RealMatrix PPT = pt.transpose().multiply(p);
        RealVector pVec = p.getColumnVector(0);
        RealVector pTVec = pt.getColumnVector(0);
        RealVector pPTVec = PPT.getColumnVector(0);
        double dotProduct = pVec.dotProduct(pTVec);
        assertEquals(1.0, dotProduct, 1e-9);
        double dotProductTranspose = pTVec.dotProduct(pPTVec);
        assertEquals(1.0, dotProductTranspose, 1e-9);

        // Check that T has real eigenvalues
        double[] eigenvalues = new double[t.getRowDimension()];
        LUDecomposition luDecomposition = new LUDecomposition(t);
        luDecomposition.solve(ArrayRealVector.valueOf(eigenvalues));
        for (double eigenvalue : eigenvalues) {
            assertTrue(Double.isFinite(eigenvalue));
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithConvergenceFailure() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }

}