package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.util.Arrays;

public class SchurTransformerTest
{
    @org.junit.Test
    public void testSchurTransformerConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new SchurTransformer(matrix);
    }

    @org.junit.Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.6275602596224952, 0.7615332941522881, 0.4283289413033126},
                {-0.7615332941522881, 0.6275602596224952, -0.4283289413033126}, {0.0, 0.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @org.junit.Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.6275602596224952, -0.7615332941522881, 0.4283289413033126},
                {-0.7615332941522881, 0.6275602596224952, -0.4283289413033126}, {-0.4283289413033126, 0.4283289413033126, 1.0}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @org.junit.Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-2.44948974278317, 1.41421369134421, -0.8414709755157649},
                {1.41421369134421, -0.7071067811865515, -2.44948974278317}, {0.8414709755157649, -2.44948974278317, 1.41421369134421}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @org.junit.Test
    public void testTransformThrowsMaxCountExceededException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedException = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}}));
        try {
            transformer.transform();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
        assertSame(expectedException, transformer);
    }

    @org.junit.Test
    public void testFindSmallSubDiagonalElement() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 1.0, 1.0, 4.0}, {3.0, 1.0, 0.5, 1.0}, {4.0, 4.0, 1.0, 1.0}}));
        int expected = 2;
        int actual = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testComputeShift() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 1.0, 1.0, 4.0}, {3.0, 1.0, 0.5, 1.0}, {4.0, 4.0, 1.0, 1.0}}));
        ShiftInfo shiftInfo = new ShiftInfo();
        double[] hVec = new double[3];
        transformer.computeShift(2, 4, 0, shiftInfo);
        double expectedX = -2.4494897427831665;
        double expectedY = 1.4142136913442114;
        double expectedW = -0.707106781186551;
        double expectedExShift = -3.55271367880051;
        assertEquals(expectedX, shiftInfo.x, 0.0);
        assertEquals(expectedY, shiftInfo.y, 0.0);
        assertEquals(expectedW, shiftInfo.w, 0.0);
        assertEquals(expectedExShift, shiftInfo.exShift, 0.0);

        shiftInfo = new ShiftInfo();
        transformer.computeShift(2, 4, 10, shiftInfo);
        double expectedShiftX = 0.964;
        double expectedShiftW = 0.964;
        assertEquals(expectedShiftX, shiftInfo.x, 0.0);
        assertEquals(expectedShiftW, shiftInfo.w, 0.0);
        assertEquals(expectedShiftX, shiftInfo.y, 0.0);
    }

    @org.junit.Test
    public void testInitQRStep() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 1.0, 1.0, 4.0}, {3.0, 1.0, 0.5, 1.0}, {4.0, 4.0, 1.0, 1.0}}));
        double[] hVec = new double[3];
        int expectedIm = 2;
        int actualIm = transformer.initQRStep(2, 4, new ShiftInfo(), hVec);
        assertEquals(expectedIm, actualIm);
    }

    @org.junit.Test
    public void testPerformDoubleQRStep() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                {2.0, 1.0, 1.0, 4.0}, {3.0, 1.0, 0.5, 1.0}, {4.0, 4.0, 1.0, 1.0}}));
        int il = 2;
        int im = 2;
        int iu = 4;
        ShiftInfo shiftInfo = new ShiftInfo();
        double[] hVec = new double[3];
        transformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        SchurTransformer expectedTransformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{-2.44948974278317, 1.41421369134421, -0.8414709755157649},
                {1.41421369134421, -0.7071067811865515, -2.44948974278317}, {0.8414709755157649, -2.44948974278317, 1.41421369134421}}));
        SchurTransformer actualTransformer = transformer.clone();
        assertSame(expectedTransformer, actualTransformer);
    }
}