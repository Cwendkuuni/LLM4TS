package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0 / 3.0}, {-2.0 / 3.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0 / 3.0}, {-2.0 / 3.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1.0, -2.0 / 3.0}, {2.0 / 3.0, 1.0}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 0.0}, {1.0, 4.0, 1.0}, {0.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.5, 1.5, 0.0}, {0.5, 2.5, 1.5}, {0.0, -1.5, 2.5}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT, 1e-14);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}, {1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0)}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedP = {{1.0 / Math.sqrt(2.0), -1.0 / Math.sqrt(2.0)}, {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}};
        double[][] expectedT = {{1.0 / Math.sqrt(2.0), 0.0}, {0.0, 1.0 / Math.sqrt(2.0)}};
        double[][] expectedPt = {{1.0 / Math.sqrt(2.0), 0.0}, {0.0, -1.0 / Math.sqrt(2.0)}};

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();

        assertEquals(MatrixUtils.createRealMatrix(expectedP), actualP);
        assertEquals(MatrixUtils.createRealMatrix(expectedT), actualT, 1e-14);
        assertEquals(MatrixUtils.createRealMatrix(expectedPt), actualPt, 1e-14);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 4.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIndex = 1;
        int actualIndex = transformer.findSmallSubDiagonalElement(2, transformer.getNorm());
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testComputeShift() {
        double x = 1.0;
        double y = 2.0;
        double w = 3.0;
        double shift = new SchurTransformer.ShiftInfo();
        SchurTransformer.computeShift(0, 0, 0, shift);
        assertEquals(0.0, shift.x, 1e-14);
        assertEquals(0.0, shift.y, 1e-14);
        assertEquals(0.0, shift.w, 1e-14);
        assertEquals(0.0, shift.exShift, 1e-14);

        shift.x = x;
        shift.y = y;
        shift.w = w;
        SchurTransformer.computeShift(10, 10, 0, shift);
        assertEquals(x + 0.75 * (y - x), shift.x, 1e-14);
        assertEquals(y + 0.75 * (y - x), shift.y, 1e-14);
        assertEquals(-0.4375 * (x + y) * (x + y), shift.w, 1e-14);
        assertEquals(x + 0.75 * (y - x), shift.exShift, 1e-14);

        shift.x = x;
        shift.y = y;
        shift.w = w;
        SchurTransformer.computeShift(30, 10, 0, shift);
        assertEquals(x - w / ((y - x) / 2.0 + FastMath.sqrt(w * (w + 4.0 * (x * x + y * y)))), shift.x, 1e-14);
        assertEquals(x - w / ((y - x) / 2.0 + FastMath.sqrt(w * (w + 4.0 * (x * x + y * y)))), shift.y, 1e-14);
        assertEquals(x - w / ((y - x) / 2.0 + FastMath.sqrt(w * (w + 4.0 * (x * x + y * y)))), shift.w, 1e-14);
        assertEquals(-(x - w / ((y - x) / 2.0 + FastMath.sqrt(w * (w + 4.0 * (x * x + y * y))))), shift.exShift, 1e-14);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxCountExceededException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
    }

    @Test
    public void testInitQRStep() {
        double[] hVec = new double[3];
        double x = 1.0;
        double y = 2.0;
        double z = 3.0;
        double w = 4.0;

        int expectedIndex = 1;
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{x, y, z}}));
        int actualIndex = transformer.initQRStep(1, 3, null, hVec);
        assertEquals(expectedIndex, actualIndex);

        x = 0.1;
        y = 0.2;
        z = 0.3;
        w = 0.4;
        expectedIndex = 0;
        transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{x, y}, {z, w}}));
        actualIndex = transformer.initQRStep(1, 3, null, hVec);
        assertEquals(expectedIndex, actualIndex);

        x = 0.1;
        y = 0.2;
        z = 0.3;
        w = 0.4;
        double lhs = 1e-14 * (FastMath.abs(x) + FastMath.abs(y) + FastMath.abs(z));
        double rhs = FastMath.abs(w) + FastMath.abs(x) + FastMath.abs(y) + FastMath.abs(z) + FastMath.abs(x) + FastMath.abs(y) + FastMath.abs(z);
        expectedIndex = 1;
        transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{x, y}, {z, w}}));
        actualIndex = transformer.initQRStep(1, 3, null, hVec);
        assertTrue(lhs < Precision.safeMult(Precision.safeAbs(hVec[1]), rhs));
    }

    @Test
    public void testPerformDoubleQRStep() {
        double p = 1.0;
        double q = 2.0;
        double r = 3.0;
        double s = 4.0;
        double z = 5.0;
        double[] hVec = new double[3];
        double x = 6.0;
        double y = 7.0;
        double w = 8.0;

        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{p, q, r}, {q, w, z}}));
        int im = 1;
        hVec[0] = (r * w - w) / q + p;
        hVec[1] = w - p - q;
        hVec[2] = z;

        SchurTransformer.performDoubleQRStep(1, im, 3, null, hVec);

        double[][] expectedP = {{1.0, 0.0, -(q / (2.0 * p)) * r}, {0.0, 1.0, (q / (2.0 * p))}, {0.0, 0.0, 1.0}};
        double[][] expectedT = {{1.0, -(q / (2.0 * p)) * r, 0.0}, {(q / (2.0 * p)), 1.0 + (r * r) / (2.0 * p), -q / (2.0 * p)}, {0.0, q / (2.0 * p), 1.0}};

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals(MatrixUtils.createRealMatrix(expectedP), actualP);
        assertEquals(MatrixUtils.createRealMatrix(expectedT), actualT, 1e-14);
    }
}