package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertNotNull(p);
        assertNotNull(pt);
        assertNotNull(t);
        assertEquals(p.getRowDimension(), p.getColumnDimension());
        assertEquals(pt.getRowDimension(), pt.getColumnDimension());
        assertEquals(t.getRowDimension(), t.getColumnDimension());
        assertEquals(matrix.getRowDimension(), p.getRowDimension());
        assertEquals(matrix.getColumnDimension(), p.getColumnDimension());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        double[][] expectedP = {{0.7363119343301687, 0.6089386397794241, -0.4286435925315787},
                {0.6089386397794241, 0.823498643582811, 0.3824866953660551},
                {-0.4286435925315787, 0.3824866953660551, 0.8436852373298556}};
        double[][] expectedT = {{1.0, 0.0, 0.0},
                {-1.382335376835134, 1.0, 0.0},
                {-2.171547634723122, -1.382335376835134, 1.0}};

        for (int i = 0; i < p.getRowDimension(); i++) {
            for (int j = 0; j < p.getColumnDimension(); j++) {
                assertEquals(expectedP[i][j], p.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }

        for (int i = 0; i < t.getRowDimension(); i++) {
            for (int j = 0; j < t.getColumnDimension(); j++) {
                assertEquals(expectedT[i][j], t.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }

        for (int i = 0; i < pt.getRowDimension(); i++) {
            for (int j = 0; j < pt.getColumnDimension(); j++) {
                assertEquals(expectedT[i][j], pt.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxCountExceededException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }
}