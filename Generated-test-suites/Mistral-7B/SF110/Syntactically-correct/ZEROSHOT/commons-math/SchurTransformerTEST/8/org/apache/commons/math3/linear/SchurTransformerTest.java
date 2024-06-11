package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.ShiftInfo;

import java.lang.reflect.Array;

public class SchurTransformerTest
{
    @org.junit.Test
    public void testSchurTransformer()
    {
        // Test constructor with valid input
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 9.0}
        });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        // Test getters for P, T, and PT matrices
        assertNotNull(p);
        assertNotNull(t);
        assertNotNull(pt);

        // Test dimensions of P, T, and PT matrices
        assertEquals(matrix.getRowDimension(), p.getRowDimension());
        assertEquals(matrix.getColumnDimension(), p.getColumnDimension());
        assertEquals(matrix.getRowDimension(), t.getRowDimension());
        assertEquals(matrix.getColumnDimension(), t.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), pt.getColumnDimension());
        assertEquals(matrix.getRowDimension(), pt.getRowDimension());

        // Test transformation
        double[][] expectedP = new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 0.81649658, -0.58259888},
                {0.0, 0.58259888, 0.81649658}
        };
        double[][] expectedT = new double[][]{
                {1.0, 2.0, 3.0},
                {-0.54598757, 0.81649658, -0.58259888},
                {-0.58259888, -0.58259888, 1.38320403}
        };
        double[][] expectedPt = new double[][]{
                {1.0, -0.58259888, -0.54598757},
                {0.81649658, 0.81649658, 0.81649658},
                {-0.58259888, -0.58259888, 0.58259888}
        };

        for (int i = 0; i < p.getRowDimension(); ++i) {
            for (int j = 0; j < p.getColumnDimension(); ++j) {
                assertEquals(expectedP[i][j], p.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }

        for (int i = 0; i < t.getRowDimension(); ++i) {
            for (int j = 0; j < t.getColumnDimension(); ++j) {
                assertEquals(expectedT[i][j], t.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }

        for (int i = 0; i < pt.getRowDimension(); ++i) {
            for (int j = 0; j < pt.getColumnDimension(); ++j) {
                assertEquals(expectedPt[i][j], pt.getEntry(i, j), Precision.SAFE_EPSILON);
            }
        }

        // Test exception when input matrix is not square
        try {
            new SchurTransformer(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {2.0}
            }));
            fail("Expected NonSquareMatrixException to be thrown");
        }
        catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    // Add more test methods here as needed for comprehensive testing
}