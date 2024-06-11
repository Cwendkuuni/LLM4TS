package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {
    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        assertEquals(p, pt.transpose());
        assertEquals(t, transformer.getT());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        // Check if the transformation was successful
        double expectedNorm = 15.0 * Math.sqrt(2.0);
        assertEquals(expectedNorm, transformer.getNorm(), 1e-12);

        // Check if the transformed matrix T is correct
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        assertEquals(expectedT, t);

        // Check if the transformed matrix P is correct
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), -1.0 / (2.0 * Math.sqrt(2.0)), 1.0 / (2.0 * Math.sqrt(2.0))},
                {-1.0 / (2.0 * Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 1.0 / (2.0 * Math.sqrt(2.0))},
                {1.0 / (2.0 * Math.sqrt(2.0)), 1.0 / (2.0 * Math.sqrt(2.0)), (Math.sqrt(2.0) - 1.0) / (2.0 * Math.sqrt(2.0))}});
        assertEquals(expectedP, p);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testConvergenceFailure() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
}