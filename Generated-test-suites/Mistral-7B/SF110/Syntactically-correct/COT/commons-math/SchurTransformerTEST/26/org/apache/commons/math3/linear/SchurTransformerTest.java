package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testInitWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4, 5}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertEquals(p.getRowDimension(), p.getColumnDimension());
        assertEquals(pt.getRowDimension(), pt.getColumnDimension());
        assertEquals(t.getRowDimension(), t.getColumnDimension());

        assertEquals(p, pt.transpose());

        assertArrayEquals(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        }, p.getData());

        assertArrayEquals(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        }, t.getData());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        double[][] expectedT = new double[][]{{1.0, 0.0}, {0.0, 0.9685551387331524}};
        double[][] expectedP = new double[][]{{0.752516695215532, -0.481626162289479},
                {-0.481626162289479, 0.752516695215532}};

        RealMatrix t = transformer.getT();
        RealMatrix p = transformer.getP();

        assertArrayEquals(expectedT, t.getData(), 0.0000001);
        assertArrayEquals(expectedP, p.getData(), 0.0000001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithConvergenceFailed() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1, 1}, {1, 1}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {2, 2}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        double norm = transformer.getNorm();

        assertEquals(13.0, norm, 0.000001);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        int startIdx = 3;
        int expectedIndex = 3;
        int actualIndex = transformer.findSmallSubDiagonalElement(startIdx, transformer.getNorm());

        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testComputeShift() {
        double x = 1.5;
        double y = 2.0;
        double w = 3.5;
        int iteration = 1;

        SchurTransformer.ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        SchurTransformer.computeShift(3, 5, iteration, shiftInfo);

        double exShift = 1.5;
        assertEquals(exShift, shiftInfo.exShift, 0.0);

        double xShift = 0.75;
        double yShift = 0.75;
        double wShift = -0.4375;

        assertEquals(xShift, shiftInfo.x, 0.0);
        assertEquals(yShift, shiftInfo.y, 0.0);
        assertEquals(wShift, shiftInfo.w, 0.0);

        if (iteration == 10) {
            xShift += x;
            yShift += x;
            wShift = -0.4375 * (x * x + x * x);

            assertEquals(xShift, shiftInfo.x, 0.0);
            assertEquals(yShift, shiftInfo.y, 0.0);
            assertEquals(wShift, shiftInfo.w, 0.0);
        }

        if (iteration == 30) {
            double s = (y - x) / 2.0;
            double s2 = s * s + w;
            double xNew = 0.964;

            shiftInfo.x = xNew;
            shiftInfo.y = xNew;
            shiftInfo.w = xNew;

            if (s > 0.0) {
                s = Math.sqrt(s2);
                if (y < x) {
                    s = -s;
                }

                xNew -= w / ((y - x) / 2.0 + s);

                assertEquals(xNew, shiftInfo.x, 0.0);
                assertEquals(xNew, shiftInfo.y, 0.0);
                assertEquals(xNew, shiftInfo.w, 0.0);
            }
        }
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1, 1},
                {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        int il = 2;
        int iu = 4;
        double[] hVec = new double[3];

        int expectedIndex = 2;
        int actualIndex = transformer.initQRStep(il, iu, transformer.getShiftInfo(), hVec);

        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1, 1},
                {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        int il = 2;
        int im = 3;
        int iu = 4;
        SchurTransformer.ShiftInfo shiftInfo = transformer.getShiftInfo();
        double[] hVec = new double[3];

        transformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        double[][] expectedMatrixT = new double[][]{{1.0, 0.0, 0.0, -0.0},
                {0.0, 0.9685551387331524, 0.0, 0.0},
                {0.0, 0.0, 0.9685551387331524, 0.0},
                {-0.0, 0.0, 0.0, 0.9685551387331524}};

        RealMatrix matrixT = transformer.getT();

        assertArrayEquals(expectedMatrixT, matrixT.getData(), 0.0000001);
    }
}