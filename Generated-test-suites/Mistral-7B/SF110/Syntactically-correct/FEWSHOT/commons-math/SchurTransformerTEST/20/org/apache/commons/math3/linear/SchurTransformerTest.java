package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.ShiftInfo;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.9999999999999998, 0.0}, {-0.0, 0.9999999999999998}});
        RealMatrix result = transformer.getP();
        assertEquals(expected, result);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.9999999999999998, 2.0}, {0.0, 0.9999999999999998}});
        RealMatrix result = transformer.getPT();
        assertEquals(expected, result);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -0.9999999999999999}});
        RealMatrix result = transformer.getT();
        assertEquals(expected, result);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsReached() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{5.0, 3.0, 0.0}, {3.0, 5.0, 0.0}, {0.0, 0.0, 10.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 5.0, 6.0, 7.0}, {3.0, 6.0, 8.0, 9.0}, {4.0, 7.0, 9.0, 10.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIndex = 0;
        int resultIndex = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, resultIndex);

        expectedIndex = 2;
        resultIndex = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, resultIndex);

        expectedIndex = 3;
        resultIndex = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, resultIndex);

        expectedIndex = 1;
        resultIndex = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, resultIndex);

        expectedIndex = 10;
        resultIndex = transformer.findSmallSubDiagonalElement(expectedIndex, transformer.getNorm());
        assertEquals(expectedIndex, resultIndex);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 5.0, 6.0, 7.0}, {3.0, 6.0, 8.0, 9.0}, {4.0, 7.0, 9.0, 10.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIndex = 2;
        double[] hVec = new double[3];
        int resultIndex = transformer.initQRStep(expectedIndex, matrix.getRowDimension(), new ShiftInfo(), hVec);
        assertEquals(expectedIndex, resultIndex);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 5.0, 6.0, 7.0}, {3.0, 6.0, 8.0, 9.0}, {4.0, 7.0, 9.0, 10.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int il = 1;
        int im = 2;
        int iu = matrix.getRowDimension() - 1;
        ShiftInfo shift = new ShiftInfo();
        double[] hVec = new double[3];
        transformer.initQRStep(il, iu, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);

        double[] expectedVector = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        RealVector resultVector = transformer.getT().getColumnVector(il);
        assertArrayEquals(expectedVector, resultVector.toArray(), 0.0);

        expectedVector = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        resultVector = transformer.getT().getColumnVector(im);
        assertArrayEquals(expectedVector, resultVector.toArray(), 0.0);

        expectedVector = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        resultVector = transformer.getT().getColumnVector(iu);
        assertArrayEquals(expectedVector, resultVector.toArray(), 0.0);

        double[][] expectedMatrix = new double[][]{{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                      {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                      {0.0, 0.0, 0.7320508075688772, 0.4594445125253639, 0.0, 0.0, 0.0, 0.0},
                                                      {0.0, 0.0, 0.4594445125253639, 0.7320508075688772, 0.0, 0.0, 0.0, 0.0},
                                                      {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0},
                                                      {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0},
                                                      {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0},
                                                      {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0}};
        RealMatrix resultMatrix = transformer.getT();
        assertArrayEquals(expectedMatrix, resultMatrix.getData(), 0.0);
    }
}