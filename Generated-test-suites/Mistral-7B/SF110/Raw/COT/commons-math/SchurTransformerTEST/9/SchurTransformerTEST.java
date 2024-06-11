package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.eye;
import static org.apache.commons.math3.linear.MatrixUtils.scalarMult;
import static org.apache.commons.math3.linear.RealMatrixUtils.subtract;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = eye(3, 2);
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = createRealMatrix(new double[][]{{0.342020145072783, -0.61542262536747},
                {-0.61542262536747, 0.342020145072783}});
        RealMatrix result = transformer.getP();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = createRealMatrix(new double[][]{{0.342020145072783, 0.61542262536747},
                {-0.61542262536747, 0.342020145072783}});
        RealMatrix result = transformer.getPT();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = createRealMatrix(new double[][]{{0.5, 1.5}, {1.5, 0.5}});
        RealMatrix result = transformer.getT();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTransform_IdentityMatrix() {
        RealMatrix matrix = eye(3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = eye(3);
        RealMatrix result = transformer.getCachedT();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testTransform_NullMatrix() {
        RealMatrix matrix = null;
        try {
            new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxIterations() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.setMaxIterations(0);
        transformer.transform();
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expected = 5.0;
        double result = transformer.getNorm();
        Assert.assertEquals(expected, result, 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expected = 1;
        int result = transformer.findSmallSubDiagonalElement(1, 5.0);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testComputeShift() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        ShiftInfo expected = new ShiftInfo();
        ShiftInfo result = transformer.computeShift(1, 1, 1, expected);
        Assert.assertEquals(expected.x, result.x, 0.0);
        Assert.assertEquals(expected.y, result.y, 0.0);
        Assert.assertEquals(expected.w, result.w, 0.0);
        Assert.assertEquals(expected.exShift, result.exShift, 0.0);
    }

    @Test
    public void testInitQRStep() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[] hVec = new double[3];
        int expected = 1;
        int result = transformer.initQRStep(1, 2, new ShiftInfo(), hVec);
        Assert.assertEquals(expected, result);
        Assert.assertEquals(0.0, hVec[0], 0.0);
        Assert.assertEquals(1.0, hVec[1], 0.0);
        Assert.assertEquals(3.0, hVec[2], 0.0);
    }

    @Test
    public void testPerformDoubleQRStep() {
        RealMatrix matrix = new RealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        ShiftInfo shift = new ShiftInfo();
        double[] hVec = new double[3];
        int il = 1;
        int im = 2;
        int iu = 3;
        transformer.initQRStep(il, iu, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);
        RealMatrix expected = createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {0.0, 0.816496580927726, -2.58800125615127},
                {0.0, -2.58800125615127, 0.816496580927726}});
        RealMatrix result = transformer.getCachedT();
        Assert.assertEquals(expected, result);
    }
}