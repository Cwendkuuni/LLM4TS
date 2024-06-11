package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = Utils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();
        assertEquals(p, pt.transpose());
        assertEquals(t, t.transpose());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = Utils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix pBefore = transformer.getP().copy();
        RealMatrix tBefore = transformer.getT().copy();
        transformer.transform();

        RealMatrix pAfter = transformer.getP();
        RealMatrix tAfter = transformer.getT();

        RealVector pBeforeColumn0 = pBefore.getColumnVector(0);
        RealVector pAfterColumn0 = pAfter.getColumnVector(0);
        assertEquals(pBeforeColumn0, pAfterColumn0, Precision.EPSILON);

        RealVector pBeforeColumn4 = pBefore.getColumnVector(4);
        RealVector pAfterColumn4 = pAfter.getColumnVector(4);
        assertEquals(pBeforeColumn4, pAfterColumn4, Precision.EPSILON);

        RealMatrix tBeforeUpperTriangular = new MatrixUtils.SubMatrix(tBefore, 0, 0, tBefore.getRowDimension(), tBefore.getColumnRange(0, tBefore.getColumnDimension() - 1));
        RealMatrix tAfterUpperTriangular = new MatrixUtils.SubMatrix(tAfter, 0, 0, tAfter.getRowDimension(), tAfter.getColumnRange(0, tAfter.getColumnDimension() - 1));
        assertEquals(tBeforeUpperTriangular, tAfterUpperTriangular);

        RealVector tBeforeDiagonal = tBefore.getColumnVector(tBefore.getColumnDimension() - 1);
        RealVector tAfterDiagonal = tAfter.getColumnVector(tAfter.getColumnDimension() - 1);
        assertEquals(tBeforeDiagonal, tAfterDiagonal, Precision.EPSILON);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_ConvergenceFailed() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0, 1.0}, {0.0, 0.0}});
        new SchurTransformer(matrix);
    }

}