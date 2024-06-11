package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.PrecisionUtils.DoubleArray;
import org.apache.commons.math3.util.PrecisionUtils.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import java.util.Arrays;

public class SchurTransformerTest
{
    @org.junit.Test
    public void testSchurTransformerConstruction()
    {
        double[][] inputMatrix = {{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 0.973666827124746}, {0.0, -0.23606797749979}, {0.973666827124746, 0.0}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.57312523820147}, {0.0, 0.816496658512257}, {0.0, -0.132456311751214}, {0.0, 0.0}, {0.0, 2.35102282538723}, {0.0, 1.17551121269261}, {0.0, -0.0736058477719175}, {0.0, 0.0}, {0.0, 1.11022302275558}, {0.0, 0.55511151137779}, {0.0, 0.0}, {0.0, 0.449489796521618}, {0.0, 0.0}, {0.0, 0.2734616124582}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("Expected and actual P matrices do not match.", expectedP, actualP);
        assertEquals("Expected and actual T matrices do not match.", expectedT, actualT);
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix()
    {
        double[][] inputMatrix = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);
    }

    @org.junit.Test
    public void testGetters()
    {
        double[][] inputMatrix = {{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertEquals("Expected P and PT to be transposes of each other.", p, pt.transpose());
    }

    @org.junit.Test
    public void testTransform()
    {
        double[][] inputMatrix = {{0.5, 0.8}, {0.8, 1.2}};
        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);

        double[][] expectedP = {{1.0, 0.0}, {0.0, 0.973666827124746}, {0.0, -0.23606797749979}, {0.973666827124746, 0.0}};
        double[][] expectedT = {
            {1.0, 0.0},
            {0.0, 1.57312523820147},
            {0.0, 0.816496658512257},
            {0.0, -0.132456311751214},
            {0.0, 0.0},
            {0.0, 2.35102282538723},
            {0.0, 1.17551121269261},
            {0.0, -0.0736058477719175},
            {0.0, 0.0},
            {0.0, 1.11022302275558},
            {0.0, 0.55511151137779},
            {0.0, 0.0},
            {0.0, 0.449489796521618},
            {0.0, 0.0},
            {0.0, 0.2734616124582}};

        double[][] actualP = transformer.getP().getData();
        double[][] actualT = transformer.getT().getData();

        assertArrayEquals("Expected and actual P matrices do not match.", expectedP, actualP);
        assertArrayEquals("Expected and actual T matrices do not match.", expectedT, actualT);
    }

    @org.junit.Test
    public void testTransform_SmallMatrix()
    {
        double[][] inputMatrix = {{0.1, 0.2}, {0.2, 0.3}};
        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);

        double[][] expectedP = {{1.0, 0.0}, {0.0, 1.0}};
        double[][] expectedT = {{1.0, 0.0}, {0.0, 1.0}};

        double[][] actualP = transformer.getP().getData();
        double[][] actualT = transformer.getT().getData();

        assertArrayEquals("Expected and actual P matrices do not match.", expectedP, actualP);
        assertArrayEquals("Expected and actual T matrices do not match.", expectedT, actualT);
    }

    @org.junit.Test
    public void testFindSmallSubDiagonalElement()
    {
        double[][] inputMatrix = {{0.5, 0.0, 0.8}, {0.0, 1.2, 0.0}, {0.8, 0.0, 1.2}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(inputMatrix));

        int expectedIl = 1;
        int actualIl = transformer.findSmallSubDiagonalElement(2, 0.0);

        assertEquals("Expected and actual il are not equal.", expectedIl, actualIl);
    }

    @org.junit.Test
    public void testComputeShift()
    {
        double[][] inputMatrix = {{0.5, 0.8}, {0.8, 1.2}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(inputMatrix));
        double[][] expectedShift = {{1.0, 0.0}, {0.0, 0.973666827124746}, {0.0, -0.23606797749979}, {0.973666827124746, 0.0}};

        double[][] actualShift = new double[4][4];
        transformer.computeShift(1, 3, 0, new SchurTransformer.ShiftInfo(actualShift[0], actualShift[1], actualShift[2], actualShift[3]));

        assertArrayEquals("Expected and actual shift matrices do not match.", expectedShift, actualShift);
    }

    @org.junit.Test
    public void testInitQRStep()
    {
        double[][] inputMatrix = {{0.5, 0.8}, {0.8, 1.2}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(inputMatrix));

        int expectedIm = 1;
        int actualIm = transformer.initQRStep(1, 3, new SchurTransformer.ShiftInfo(0.0, 0.0, 0.0, 0.0), new DoubleArray(new double[3]));

        assertEquals("Expected and actual im are not equal.", expectedIm, actualIm);
    }

    @org.junit.Test
    public void testPerformDoubleQRStep()
    {
        double[][] inputMatrix = {{0.5, 0.8}, {0.8, 1.2}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(inputMatrix));
        double[][] expectedShift = {{1.0, 0.0}, {0.0, 0.973666827124746}, {0.0, -0.23606797749979}, {0.973666827124746, 0.0}};
        double[] hVec = new double[3];

        transformer.performDoubleQRStep(1, 1, 3, new SchurTransformer.ShiftInfo(0.0, 0.0, 0.0, 0.0), hVec);

        double[][] actualShift = new double[4][4];
        transformer.computeShift(1, 3, 0, new SchurTransformer.ShiftInfo(actualShift[0], actualShift[1], actualShift[2], actualShift[3]));

        assertArrayEquals("Expected and actual shift matrices do not match.", expectedShift, actualShift);
        assertArrayEquals("Expected and actual hVec do not match.", new DoubleArray(new double[] {1.57312523820147, 0.816496658512257, 0.0}), new DoubleArray(hVec));
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxIterationsExceeded()
    {
        double[][] inputMatrix = new double[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                inputMatrix[i][j] = PrecisionUtils.nextDouble();
            }
        }

        RealMatrix inputRealMatrix = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(inputRealMatrix);
    }
}