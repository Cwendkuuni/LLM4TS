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
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {{0.0324654236776313, -0.456193252699898, 0.861805321343759},
                {-0.456193252699898, 0.736623435229758, -0.408283123893354},
                {0.861805321343759, -0.408283123893354, 0.0324654236776313}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0}, {2.0}});
        new SchurTransformer(matrix);
    }

    @org.junit.Test
    public void testGetP() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        RealMatrix actualP = transformer.getP();
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {{0.0324654236776313, -0.456193252699898, 0.861805321343759},
                {-0.456193252699898, 0.736623435229758, -0.408283123893354},
                {0.861805321343759, -0.408283123893354, 0.0324654236776313}});

        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }

    @org.junit.Test
    public void testGetPT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        RealMatrix actualPT = transformer.getPT();
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][] {{0.0324654236776313, -0.456193252699898, 0.861805321343759},
                {-0.456193252699898, 0.736623435229758, -0.408283123893354},
                {0.861805321343759, -0.408283123893354, 0.0324654236776313}});

        assertArrayEquals(expectedPT.getData(), actualPT.getData(), 0.0);
    }

    @org.junit.Test
    public void testGetT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        RealMatrix actualT = transformer.getT();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @org.junit.Test
    public void testTransform() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {{0.0324654236776313, -0.456193252699898, 0.861805321343759},
                {-0.456193252699898, 0.736623435229758, -0.408283123893354},
                {0.861805321343759, -0.408283123893354, 0.0324654236776313}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxCountExceededException() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        transformer.transform(); // Calling transform method should throw MaxCountExceededException
    }

    @org.junit.Test
    public void testGetNorm() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        double expectedNorm = 23.6438735244978;
        double actualNorm = transformer.getNorm();

        assertEquals(expectedNorm, actualNorm, 0.0);
    }

    @org.junit.Test
    public void testFindSmallSubDiagonalElement() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        int expectedIl = 2;
        int actualIl = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());

        assertEquals(expectedIl, actualIl);
    }

    @org.junit.Test
    public void testComputeShift() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        double x = transformer.getT()[2][2];
        double y = transformer.getT()[1][1];
        double w = 0.0;
        ShiftInfo shift = new SchiftInfo();
        transformer.computeShift(2, 2, 0, shift);

        double expectedExShift = 1.73205080756888;
        double expectedX = 1.5;
        double expectedY = 1.5;
        double expectedW = 0.25;

        assertEquals(expectedExShift, shift.exShift, 0.0);
        assertEquals(expectedX, shift.x, 0.0);
        assertEquals(expectedY, shift.y, 0.0);
        assertEquals(expectedW, shift.w, 0.0);
    }

    @org.junit.Test
    public void testInitQRStep() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        double[] hVec = new double[3];
        int expectedIm = 0;
        int actualIm = transformer.initQRStep(2, 2, new ShiftInfo(), hVec);

        assertEquals(expectedIm, actualIm);
    }

    @org.junit.Test
    public void testPerformDoubleQRStep() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}}));
        double[] hVec = new double[3];
        int il = 2;
        int im = 0;
        int iu = 2;
        ShiftInfo shift = new ShiftInfo();
        transformer.initQRStep(il, iu, shift, hVec);
        transformer.performDoubleQRStep(il, im, iu, shift, hVec);

        double[] expectedT = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0};
        double[] actualT = Arrays.stream(transformer.getT().getData()).mapToDouble(d -> d).toArray();

        assertArrayEquals(expectedT, actualT);
    }
}

class SchiftInfo
{
    double x;
    double y;
    double w;
    double exShift;
}