package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 4.0},
                {3.0, 4.0, 9.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.847858271, 0.545541561}, {0.0, -0.545541561, 0.513124572}});
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.847858271, 0.545541561, 3.741657386}, {0.545541561, 1.354152354, -0.816496587}, {3.741657386, -0.816496587, 1.212372331}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualPT = transformer.getPT();
        RealMatrix actualT = transformer.getT();

        assertEquals(expectedP, actualP);
        assertEquals(expectedPT, actualPT);
        assertEquals(expectedT, actualT);

        // Test getP(), getPT(), and getT() with cached matrices
        RealMatrix cachedP = transformer.getP();
        RealMatrix cachedPT = transformer.getPT();
        RealMatrix cachedT = transformer.getT();

        assertEquals(cachedP, actualP);
        assertEquals(cachedPT, actualPT);
        assertEquals(cachedT, actualT);

        // Test for non-square matrix exception
        try {
            SchurTransformer transformer2 = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetNorm() {
        double[][] matrix = {
                {1.0, 0.0, 0.0},
                {0.0, 5.0, 4.0},
                {0.0, 4.0, 9.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        double expectedNorm = 1.0 + 5.0 + 4.0 + 4.0 + 4.0 + 9.0 = 23.0;
        double actualNorm = transformer.getNorm();

        assertEquals(expectedNorm, actualNorm, 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 4.0},
                {3.0, 4.0, 9.0}
        };
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));

        int expectedIl = 0;
        int actualIl = transformer.findSmallSubDiagonalElement(expectedIl, transformer.getNorm());

        assertEquals(expectedIl, actualIl);

        expectedIl = 1;
        actualIl = transformer.findSmallSubDiagonalElement(expectedIl, transformer.getNorm());

        assertEquals(expectedIl, actualIl);

        expectedIl = 2;
        actualIl = transformer.findSmallSubDiagonalElement(expectedIl, transformer.getNorm());

        assertEquals(expectedIl, actualIl);
    }

    @Test
    public void testComputeShift() {
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 4.0},
                {3.0, 4.0, 9.0}
        };
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));

        ShiftInfo expectedShiftInfo = new ShiftInfo();
        expectedShiftInfo.x = 5.0;
        expectedShiftInfo.y = 1.0;
        expectedShiftInfo.w = -0.25;
        expectedShiftInfo.exShift = 0.0;

        int iteration = 0;
        ShiftInfo actualShiftInfo = transformer.computeShift(2, 2, iteration, new ShiftInfo());

        assertEquals(expectedShiftInfo.x, actualShiftInfo.x, 0.0);
        assertEquals(expectedShiftInfo.y, actualShiftInfo.y, 0.0);
        assertEquals(expectedShiftInfo.w, actualShiftInfo.w, 0.0);
        assertEquals(expectedShiftInfo.exShift, actualShiftInfo.exShift, 0.0);
    }

    @Test
    public void testInitQRStep() {
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 4.0},
                {3.0, 4.0, 9.0}
        };
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));

        double[] hVec = new double[3];
        int expectedIm = 1;

        int actualIm = transformer.initQRStep(0, 2, new ShiftInfo(), hVec);

        assertEquals(expectedIm, actualIm);
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] matrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 4.0},
                {3.0, 4.0, 9.0}
        };
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));

        double[] hVec = new double[3];
        int il = 0;
        int im = 1;
        int iu = 2;
        ShiftInfo shiftInfo = new ShiftInfo();

        transformer.initQRStep(il, iu, shiftInfo, hVec);
        transformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        double[] expectedHVec = new double[]{-0.545541561, 1.354152354, -0.816496587};

        assertArrayEquals(expectedHVec, hVec, 0.0);

        transformer.transform();
        RealMatrix actualT = transformer.getT();

        double[] actualTRows = actualT.getColumnVector(0).toArray();
        double[] expectedTRows = new double[]{-0.847858271, 0.545541561, 3.741657386};

        assertArrayEquals(expectedTRows, actualTRows, 0.0);
    }
}

class SchurTransformer {
    // ... (original code)
}