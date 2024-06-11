package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.ShiftInfo;
import org.apache.commons.math3.util.Utils;
import org.junit.Test;

/**
 * Tests the SchurTransformer class.
 */
public class SchurTransformerTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        assertEquals(3, transformer.getP().getRowDimension());
        assertEquals(3, transformer.getP().getColumnDimension());
        assertEquals(3, transformer.getT().getRowDimension());
        assertEquals(3, transformer.getT().getColumnDimension());
        assertNotNull(transformer.getCachedP());
        assertNotNull(transformer.getCachedT());
        assertNotNull(transformer.getCachedPt());
    }

    /**
     * Tests the getP() method.
     */
    @Test
    public void testGetP() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix p = transformer.getP();
        assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, p.getData(), 0.0);
    }

    /**
     * Tests the getPT() method.
     */
    @Test
    public void testGetPT() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix pt = transformer.getPT();
        assertArrayEquals(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}}, pt.getData(), 0.0);
    }

    /**
     * Tests the getT() method.
     */
    @Test
    public void testGetT() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix t = transformer.getT();
        assertArrayEquals(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}}, t.getData(), 0.0);
    }

    /**
     * Tests the transform() method.
     */
    @Test
    public void testTransform() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        transformer.transform();
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertArrayEquals(new double[][]{{0.44954723, 0.74167446, 0.43662244},
                {-0.74167446, 0.53905046, 0.67321068},
                {0.43662244, 0.67321068, -0.73749793}}, p.getData(), 0.0);
        assertArrayEquals(new double[][]{{0.44954723, -0.74167446, 0.43662244},
                {-0.74167446, 0.53905046, -0.67321068},
                {0.43662244, 0.67321068, -0.73749793}}, pt.getData(), 0.0);
        assertArrayEquals(new double[][]{{0.44954723, 0.0, -0.44954723},
                {0.0, 0.53905046, 0.0},
                {-0.44954723, 0.0, 1.10453593}}, t.getData(), 0.0);
    }

    /**
     * Tests the getNorm() method.
     */
    @Test
    public void testGetNorm() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        assertEquals(33.0, transformer.getNorm(), 0.0);
    }

    /**
     * Tests the findSmallSubDiagonalElement() method.
     */
    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        assertEquals(1, transformer.findSmallSubDiagonalElement(1, transformer.getNorm()));
    }

    /**
     * Tests the computeShift() method.
     */
    @Test
    public void testComputeShift() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}}));
        ShiftInfo shift = new ShiftInfo();
        transformer.computeShift(0, 0, 0, shift);
        assertEquals(1.0, shift.x, 0.0);
        assertEquals(1.0, shift.y, 0.0);
        assertEquals(0.0, shift.w, 0.0);
        assertEquals(1.0, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 1, shift);
        assertEquals(2.0, shift.x, 0.0);
        assertEquals(2.0, shift.y, 0.0);
        assertEquals(0.0, shift.w, 0.0);
        assertEquals(2.0, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 10, shift);
        assertEquals(3.375, shift.x, 0.0);
        assertEquals(3.375, shift.y, 0.0);
        assertEquals(-1.375, shift.w, 0.0);
        assertEquals(4.75, shift.exShift, 0.0);

        transformer.computeShift(1, 1, 30, shift);
        assertEquals(4.64, shift.x, 0.0);
        assertEquals(4.64, shift.y, 0.0);
        assertEquals(1.12, shift.w, 0.0);
        assertEquals(9.76, shift.exShift, 0.0);
    }

    /**
     * Tests the initQRStep() method.
     */
    @Test
    public void testInitQRStep() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        double[] hVec = new double[3];
        int il = transformer.findSmallSubDiagonalElement(1, transformer.getNorm());
        int im = transformer.initQRStep(il, 2, shift, hVec);
        assertEquals(2, im);
    }

    /**
     * Tests the performDoubleQRStep() method.
     */
    @Test
    public void testPerformDoubleQRStep() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);
        double[] hVec = new double[3];
        int il = transformer.findSmallSubDiagonalElement(1, transformer.getNorm());
        int im = transformer.initQRStep(il, 2, shift, hVec);
        transformer.performDoubleQRStep(il, im, 2, shift, hVec);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertArrayEquals(new double[][]{{0.44954723, 0.74167446, 0.43662244},
                {-0.74167446, 0.53905046, 0.67321068},
                {0.43662244, 0.67321068, -0.73749793}}, p.getData(), 0.0);
        assertArrayEquals(new double[][]{{0.44954723, -0.74167446, 0.43662244},
                {-0.74167446, 0.53905046, -0.67321068},
                {0.43662244, 0.67321068, -0.73749793}}, pt.getData(), 0.0);
        assertArrayEquals(new double[][]{{0.44954723, 0.0, -0.44954723},
                {0.0, 0.53905046, 0.0},
                {-0.44954723, 0.0, 1.10453593}}, t.getData(), 0.0);
    }

    /**
     * Tests the constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] inputMatrix = {
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        };
        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        new SchurTransformer(input);
    }
}