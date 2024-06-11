package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_NonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();
        assertEquals(p, pt.transpose());
        assertEquals(t, transformer.getT());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        // Check that the Schur transformation was performed correctly
        double epsilon = Precision.EPSILON;
        double[][] expectedT = {
                {2.41421377905869, -1.0, 0.0},
                {-1.0, 2.0, -1.0},
                {0.0, -1.0, 2.95425691511152}};
        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < t.getColumnDimension(); j++) {
                assertEquals(expectedT[i][j], t.getEntry(i, j), epsilon);
            }
        }

        double[][] expectedP = {
                {0.577350269189626, 0.577350269189626, 0.577350269189626},
                {0.577350269189626, -0.816496580927726, 0.30601132326339},
                {0.577350269189626, 0.30601132326339, 0.816496580927726}};
        for (int i = 0; i < p.getRowDimension(); i++) {
            for (int j = 0; j < p.getColumnDimension(); j++) {
                assertEquals(expectedP[i][j], p.getEntry(i, j), epsilon);
            }
        }

        // Check that the Schur transformation preserves vector lengths
        RealVector x = Utils.createColumnVector(new double[]{1.0, 1.0, 1.0});
        RealVector y = MatrixUtils.mult(p, MatrixUtils.mult(t, x));
        assertEquals(1.0, PrecisionUtils.doubleValue(y.norm()), epsilon);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxIterations() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.setMAX_ITERATIONS(0);
        transformer.transform();
    }
}