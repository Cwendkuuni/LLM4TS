package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.UtilEpsilon;
import org.junit.Test;

public class SchurTransformerTest
{

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53571315, -0.85749475, 0.0}, {-0.85749475, 0.53571315, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix p = transformer.getP();
        assertNotNull(p);
        assertArrayEquals(expectedP.getData(), p.getData(), 0.0);
    }

    @Test
    public void testGetPT()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.53571315, -0.85749475, 0.0}, {-0.85749475, 0.53571315, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix pt = transformer.getPT();
        assertNotNull(pt);
        assertArrayEquals(expectedPT.getData(), pt.getData(), 0.0);
    }

    @Test
    public void testGetT()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 1.0, -2.0}, {1.0, -2.0, -3.0}});
        RealMatrix t = transformer.getT();
        assertNotNull(t);
        assertArrayEquals(expectedT.getData(), t.getData(), 0.0);
    }

    @Test
    public void testTransform()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedP = {{0.53571315, -0.85749475, 0.0}, {-0.85749475, 0.53571315, 0.0}, {0.0, 0.0, 1.0}};
        double[][] expectedT = {{3.0, 2.0, 1.0}, {2.0, 1.0, -2.0}, {1.0, -2.0, -3.0}};
        double[][] expectedPt = {{0.53571315, -0.85749475, 0.0}, {-0.85749475, 0.53571315, 0.0}, {0.0, 0.0, 1.0}};

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertArrayEquals(expectedP, p.getData(), UtilEpsilon.getMachineEpsilon());
        assertArrayEquals(expectedT, t.getData(), UtilEpsilon.getMachineEpsilon());
        assertArrayEquals(expectedPt, pt.getData(), UtilEpsilon.getMachineEpsilon());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIterationsExceeded()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.setMaxIterations(0);
        transformer.transform();
    }

    @Test
    public void testFindSmallSubDiagonalElement()
    {
        double[][] matrix = {{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 1.0, 6.0, 7.0, 8.0}, {3.0, 6.0, 1.0, 9.0, 10.0}, {4.0, 7.0, 9.0, 1.0, 11.0}, {5.0, 8.0, 10.0, 11.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));

        int expectedIndex = 2;
        int index = transformer.findSmallSubDiagonalElement(3, Precision.EPSILON);
        assertEquals(expectedIndex, index);
    }

    @Test
    public void testComputeShift()
    {
        double[][] matrix = {{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 1.0, 6.0, 7.0, 8.0}, {3.0, 6.0, 1.0, 9.0, 10.0}, {4.0, 7.0, 9.0, 1.0, 11.0}, {5.0, 8.0, 10.0, 11.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        transformer.computeShift(2, 4, 0, shiftInfo);

        double expectedX = 1.0;
        double expectedW = 0.0;
        double expectedY = 1.0;
        double expectedExShift = 0.75;

        assertEquals(expectedX, shiftInfo.x, Precision.SAFE_EPSILON);
        assertEquals(expectedW, shiftInfo.w, Precision.SAFE_EPSILON);
        assertEquals(expectedY, shiftInfo.y, Precision.SAFE_EPSILON);
        assertEquals(expectedExShift, shiftInfo.exShift, Precision.SAFE_EPSILON);
    }

    @Test
    public void testInitQRStep()
    {
        double[][] matrix = {{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 1.0, 6.0, 7.0, 8.0}, {3.0, 6.0, 1.0, 9.0, 10.0}, {4.0, 7.0, 9.0, 1.0, 11.0}, {5.0, 8.0, 10.0, 11.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        double[] hVec = new double[3];

        int expectedIndex = 2;
        int index = transformer.initQRStep(3, 4, shiftInfo(), hVec);

        assertEquals(expectedIndex, index);
    }

    @Test
    public void testPerformDoubleQRStep()
    {
        double[][] matrix = {{1.0, 2.0, 3.0, 4.0, 5.0}, {2.0, 1.0, 6.0, 7.0, 8.0}, {3.0, 6.0, 1.0, 9.0, 10.0}, {4.0, 7.0, 9.0, 1.0, 11.0}, {5.0, 8.0, 10.0, 11.0, 1.0}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        double[] hVec = new double[3];
        int il = 3;
        int im = 4;
        int iu = 5;
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();

        transformer.performDoubleQRStep(il, im, iu, shiftInfo, hVec);

        double[] expectedP = {-0.85749475, 0.53571315, 0.0, 0.0, 0.0};
        double[] expectedT = {3.0, 2.0, 1.0, 0.0, 0.0};
        double[] expectedPt = {-0.85749475, 0.53571315, 0.0, 0.0, 0.0};

        assertArrayEquals(expectedP, transformer.getP().getData(), Precision.SAFE_EPSILON);
        assertArrayEquals(expectedT, transformer.getT().getData(), Precision.SAFE_EPSILON);
        assertArrayEquals(expectedPt, transformer.getPT().getData(), Precision.SAFE_EPSILON);
    }

    private ShiftInfo shiftInfo()
    {
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        shiftInfo.x = 0.0;
        shiftInfo.y = 0.0;
        shiftInfo.w = 0.0;
        shiftInfo.exShift = 0.0;
        return shiftInfo;
    }
}