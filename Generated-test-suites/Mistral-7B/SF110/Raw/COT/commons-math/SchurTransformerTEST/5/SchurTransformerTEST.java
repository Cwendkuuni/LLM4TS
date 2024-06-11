package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.ShiftInfo;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix result = transformer.getP();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RealMatrix result = transformer.getPT();
        assertEquals(expected, result.transpose());
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{7.0, -2.0, 6.0}, {-2.0, 3.0, 0.0}, {6.0, 0.0, 1.0}});
        RealMatrix result = transformer.getT();
        assertEquals(expected, result);
    }

    @Test
    public void testTransformWithIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix result = transformer.getT();
        assertEquals(expected, result);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        assertEquals(expected, p);
        assertEquals(expected.transpose(), pt);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expected = 0;
        int result = transformer.findSmallSubDiagonalElement(0, 0.0);
        assertEquals(expected, result);

        expected = 1;
        result = transformer.findSmallSubDiagonalElement(1, 0.0);
        assertEquals(expected, result);

        expected = 2;
        result = transformer.findSmallSubDiagonalElement(2, 0.0);
        assertEquals(expected, result);

        expected = 2;
        result = transformer.findSmallSubDiagonalElement(2, 1.0);
        assertEquals(expected, result);
    }

    @Test(expected = org.apache.commons.math3.exception.MaxCountExceededException.class)
    public void testTransformWithMaxIterations() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }

}