package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_squareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.742942481723422, -0.572899231000431, -0.226167234513531},
                {0.422586849676284, 0.818142256793923, 0.407912792557647},
                {-0.226167234513531, 0.407912792557647, 0.742942481723422}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.59819415643719, 1.41421369131916},
                {3.0, 1.41421369131916, 3.59819415643719}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("P matrix is not as expected", expectedP, actualP);
        assertEquals("T matrix is not as expected", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_nonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualT = transformer.getT();

        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.59819415643719, 1.41421369131916},
                {3.0, 1.41421369131916, 3.59819415643719}});

        assertEquals("T matrix is not as expected", expectedT, actualT);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualPT = transformer.getPT();

        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.59819415643719, 1.41421369131916},
                {3.0, 1.41421369131916, 3.59819415643719}});

        assertEquals("PT matrix is not as expected", expectedPT, actualPT);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualP = transformer.getP();

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.742942481723422, -0.572899231000431, -0.226167234513531},
                {0.422586849676284, 0.818142256793923, 0.407912792557647},
                {-0.226167234513531, 0.407912792557647, 0.742942481723422}});

        assertEquals("P matrix is not as expected", expectedP, actualP);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_maxIterationsReached() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.1}, {1.1, 1.2}}));
        int actualIl = transformer.findSmallSubDiagonalElement(0, 0.0);
        int expectedIl = 0;

        assertEquals("Il is not as expected", expectedIl, actualIl);
    }

    @Test
    public void testFindSmallSubDiagonalElement_largeSubDiagonalElement() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.1}, {1.0, 1.1, 1.2}, {1.1, 1.2, 1.3}}));
        int actualIl = transformer.findSmallSubDiagonalElement(0, 0.0);
        int expectedIl = 1;

        assertEquals("Il is not as expected", expectedIl, actualIl);
    }

    @Test
    public void testFindSmallSubDiagonalElement_zeroDeterminant() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}}));
        int actualIl = transformer.findSmallSubDiagonalElement(0, 0.0);
        int expectedIl = 0;

        assertEquals("Il is not as expected", expectedIl, actualIl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSmallSubDiagonalElement_nonSquareMatrix() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.1}}));
        transformer.findSmallSubDiagonalElement(0, 0.0);
    }

    @Test
    public void testComputeShift() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.1}, {1.1, 1.2}}));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 0, 0, shiftInfo);

        double expectedX = 1.1;
        double expectedY = 1.0;
        double expectedW = -0.0;
        double expectedExShift = 0.0;

        assertEquals("x is not as expected", expectedX, shiftInfo.x, 0.0);
        assertEquals("y is not as expected", expectedY, shiftInfo.y, 0.0);
        assertEquals("w is not as expected", expectedW, shiftInfo.w, 0.0);
        assertEquals("exShift is not as expected", expectedExShift, shiftInfo.exShift, 0.0);
    }

    @Test
    public void testComputeShift_10Iterations() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.1}, {1.1, 1.2}}));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 0, 10, shiftInfo);

        double expectedX = -0.362686564544623;
        double expectedY = -0.362686564544623;
        double expectedW = -0.13659594110231;
        double expectedExShift = -0.362686564544623;

        assertEquals("x is not as expected", expectedX, shiftInfo.x, 0.0);
        assertEquals("y is not as expected", expectedY, shiftInfo.y, 0.0);
        assertEquals("w is not as expected", expectedW, shiftInfo.w, 0.0);
        assertEquals("exShift is not as expected", expectedExShift, shiftInfo.exShift, 0.0);
    }

    @Test
    public void testComputeShift_30Iterations() {
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 1.1}, {1.1, 1.2}}));
        ShiftInfo shiftInfo = new SchurTransformer.ShiftInfo();
        transformer.computeShift(0, 0, 30, shiftInfo);

        double expectedX = 0.964;
        double expectedY = 0.964;
        double expectedW = -0.4375;

        assertEquals("x is not as expected", expectedX, shiftInfo.x, 0.0);
        assertEquals("y is not as expected", expectedY, shiftInfo.y, 0.0);
        assertEquals("w is not as expected", expectedW, shiftInfo.w, 0.0);
        assertEquals("exShift is not as expected", 0.0, shiftInfo.exShift, 0.0);
    }
}