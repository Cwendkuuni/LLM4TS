package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.exception.MaxCountExceededException;
import org.apache.commons.math3.util.exception.ShiftNotPositiveException;
import org.apache.commons.math3.util.exception.SingularMatrixException;
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
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.449489742783179, -0.898942475107333}, {-0.898942475107333, 0.449489742783179}});
        RealMatrix actualP = transformer.getP();
        PrecisionUtils.assertEquals(expectedP, actualP, 1e-14);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.449489742783179, 0.898942475107333}, {-0.898942475107333, 0.449489742783179}});
        RealMatrix actualPT = transformer.getPT();
        PrecisionUtils.assertEquals(expectedPT, actualPT, 1e-14);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.935694495835798, 1.295527263284858, -0.138062595857652},
                {1.295527263284858, 0.624699625224325, 1.323246540423355}, {-0.138062595857652, 1.323246540423355, 1.0}});
        RealMatrix actualT = transformer.getT();
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-14);
    }

    @Test(expected = SingularMatrixException.class)
    public void testTransformWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        new SchurTransformer(matrix).transform();
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIteration() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.setMaxIterations(1);
        transformer.transform();
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.935694495835798, 1.295527263284858, -0.138062595857652},
                {1.295527263284858, 0.624699625224325, 1.323246540423355}, {-0.138062595857652, 1.323246540423355, 1.0}});
        RealMatrix actualT = transformer.getT();
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-14);

        expectedT = new Array2DRowRealMatrix(new double[][]{{-0.449489742783179, 0.898942475107333}, {-0.898942475107333, 0.449489742783179}});
        RealMatrix actualP = transformer.getP();
        PrecisionUtils.assertEquals(expectedT, transformer.getPT(), 1e-14);
        PrecisionUtils.assertEquals(expectedP, transformer.getP(), 1e-14);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        int expected = 1;
        int actual = transformer.findSmallSubDiagonalElement(2, transformer.getNorm());
        assertEquals(expected, actual);

        expected = 3;
        actual = transformer.findSmallSubDiagonalElement(0, transformer.getNorm());
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSmallSubDiagonalElementWithNegativeIndex() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        transformer.findSmallSubDiagonalElement(-1, transformer.getNorm());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSmallSubDiagonalElementWithNonSquareMatrix() {
        double[][] input = {{1.0, 2.0}, {2.0}};
        try {
            new SchurTransformer(new Array2DRowRealMatrix(input)).findSmallSubDiagonalElement(0, 0.0);
        } catch (NonSquareMatrixException e) {
            throw new AssertionError("Should not reach this point", e);
        }
    }

    @Test
    public void testComputeShift() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(2, 4, 10, shift);
        assertEquals(1.15274069112381, shift.x, 1e-14);
        assertEquals(1.15274069112381, shift.y, 1e-14);
        assertEquals(-0.4375 * 1.15274069112381 * 1.15274069112381, shift.w, 1e-14);
        assertEquals(1.15274069112381, shift.exShift, 1e-14);
    }

    @Test(expected = ShiftNotPositiveException.class)
    public void testComputeShiftWithNegativeShift() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        ShiftInfo shift = new SchurTransformer.ShiftInfo();
        transformer.computeShift(2, 4, 10, shift);
        shift.x = -1.0;
        transformer.computeShift(2, 4, 10, shift);
    }

    @Test
    public void testInitQRStep() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        double[] hVec = new double[3];
        int im = transformer.initQRStep(2, 4, new SchurTransformer.ShiftInfo(), hVec);
        assertEquals(3, im);
        assertEquals(0.266666666666667, hVec[0], 1e-14);
        assertEquals(-1.13333333333333, hVec[1], 1e-14);
        assertEquals(0.266666666666667, hVec[2], 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitQRStepWithNonSquareMatrix() {
        double[][] input = {{1.0, 2.0}, {2.0}};
        try {
            new SchurTransformer(new Array2DRowRealMatrix(input)).initQRStep(0, 0, new SchurTransformer.ShiftInfo(), new double[3]);
        } catch (NonSquareMatrixException e) {
            throw new AssertionError("Should not reach this point", e);
        }
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] input = {{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 5.0, 6.0}, {3.0, 5.0, 1.0, 7.0}, {4.0, 6.0, 7.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(input));
        double[] hVec = new double[3];
        int im = transformer.initQRStep(2, 4, new SchurTransformer.ShiftInfo(), hVec);
        transformer.performDoubleQRStep(2, im, 4, new SchurTransformer.ShiftInfo(), hVec);
        double[][] expectedMatrix = {{0.408248290463863, -0.816496580927726, -0.395947019231024},
                {-0.816496580927726, 0.408248290463863, 0.816496580927726},
                {-0.395947019231024, 0.816496580927726, 0.999999999999994}};
        RealMatrix actualMatrix = transformer.getT();
        PrecisionUtils.assertEquals(expectedMatrix, actualMatrix, 1e-14);

        expectedMatrix = new Array2DRowRealMatrix(new double[][]{{0.449489742783179, 0.898942475107333}, {-0.898942475107333, 0.449489742783179}});
        RealMatrix actualP = transformer.getP();
        PrecisionUtils.assertEquals(expectedMatrix, actualP, 1e-14);
    }
}