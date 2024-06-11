package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testConstructorWithSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.73205080756888, -0.69282032302755}, {-0.69282032302755, 0.73205080756888}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.4142137412507, 0.0}, {0.0, 1.4142137412507}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}, {3.0, 4.0}});
        new SchurTransformer(inputMatrix);
    }

    @Test(expected = org.apache.commons.math3.exception.MaxCountExceededException.class)
    public void testTransformWithMaxIterationsReached() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        transformer.transform();
        transformer.transform();
    }

    @Test
    public void testGetPMethod() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.73205080756888, -0.69282032302755}, {-0.69282032302755, 0.73205080756888}});
        RealMatrix actualP = transformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }

    @Test
    public void testGetPTMethod() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.73205080756888, -0.69282032302755}, {-0.69282032302755, 0.73205080756888}});
        RealMatrix actualPT = transformer.getPT();
        Assert.assertArrayEquals(expectedPT.getData(), actualPT.getData(), 0.0);
    }

    @Test
    public void testGetTMethod() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.4142137412507, 0.0}, {0.0, 1.4142137412507}});
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 0.0, 0.0}, {2.0, 1.0, 0.5, 0.0}, {0.0, 0.5, 1.0, 0.5}, {0.0, 0.0, 0.5, 1.0}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(inputMatrix));
        int expectedIndex = 2;
        int actualIndex = transformer.findSmallSubDiagonalElement(expectedIndex, 4.0);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testComputeShift() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 0.0, 0.0}, {2.0, 1.0, 0.5, 0.0}, {0.0, 0.5, 1.0, 0.5}, {0.0, 0.0, 0.5, 1.0}};
        ShiftInfo shift = new SchurTransformer(MatrixUtils.createRealMatrix(inputMatrix)).new ShiftInfo();
        double expectedX = 0.25;
        double expectedY = 0.25;
        double expectedW = -0.0625;
        double expectedExShift = 0.0;
        Assert.assertEquals(expectedX, shift.x, 0.0);
        Assert.assertEquals(expectedY, shift.y, 0.0);
        Assert.assertEquals(expectedW, shift.w, 0.0);
        Assert.assertEquals(expectedExShift, shift.exShift, 0.0);

        shift.x = 3.0;
        shift.y = 2.0;
        shift.w = 1.0;
        shift.iteration = 10;
        SchurTransformer.computeShift(0, 3, shift, null);
        double expectedShiftX = 1.5;
        double expectedShiftY = 1.5;
        double expectedShiftW = -0.75;
        Assert.assertEquals(expectedShiftX, shift.x, 0.0);
        Assert.assertEquals(expectedShiftY, shift.y, 0.0);
        Assert.assertEquals(expectedShiftW, shift.w, 0.0);

        shift.iteration = 30;
        SchurTransformer.computeShift(0, 3, shift, null);
        double expectedExShift = 1.5;
        Assert.assertEquals(expectedExShift, shift.exShift, 0.0);
    }

    @Test
    public void testInitQRStep() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 0.0, 0.0}, {2.0, 1.0, 0.5, 0.0}, {0.0, 0.5, 1.0, 0.5}, {0.0, 0.0, 0.5, 1.0}};
        double[] hVec = new double[3];
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(inputMatrix));
        int expectedIndex = 2;
        int actualIndex = transformer.initQRStep(expectedIndex, 4, transformer.new ShiftInfo(), hVec);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testPerformDoubleQRStep() {
        double[][] inputMatrix = new double[][]{{1.0, 2.0, 0.0, 0.0}, {2.0, 1.0, 0.5, 0.0}, {0.0, 0.5, 1.0, 0.5}, {0.0, 0.0, 0.5, 1.0}};
        double[] hVec = new double[3];
        int il = 2;
        int im = 3;
        int iu = 4;
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(inputMatrix));
        transformer.performDoubleQRStep(il, im, iu, transformer.new ShiftInfo(), hVec);

        double[] expectedVector1 = {-0.70710678118655, 0.0, 0.70710678118655};
        Assert.assertArrayEquals(expectedVector1, hVec, 0.0);

        double[] expectedVector2 = {-0.40824829046386, -0.81649658092773, 0.40824829046386};
        Assert.assertArrayEquals(expectedVector2, transformer.matrixT, 0.0);
    }
}