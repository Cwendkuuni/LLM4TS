package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 4.0, 3.0}, {3.0, 4.0, 1.0, 2.0}, {4.0, 3.0, 2.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.538460226, 0.538460226, -0.538460226, -0.538460226},
                {-0.816496580, 0.447234316, -0.447234316, 0.816496580},
                {-0.447234316, 0.816496580, 0.816496580, 0.447234316},
                {0.447234316, -0.816496580, 0.447234316, -0.816496580}});
        RealMatrix actualP = schurTransformer.getP();
        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.001);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 4.0, 3.0}, {3.0, 4.0, 1.0, 2.0}, {4.0, 3.0, 2.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.538460226, -0.81649658, -0.447234316, 0.447234316},
                {0.538460226, 0.447234316, 0.81649658, -0.447234316},
                {-0.81649658, 0.447234316, 0.81649658, 0.447234316},
                {-0.447234316, -0.81649658, 0.447234316, 0.81649658}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertArrayEquals(expectedPT.getData(), actualPT.getData(), 0.001);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 4.0, 3.0}, {3.0, 4.0, 1.0, 2.0}, {4.0, 3.0, 2.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{0.0, 2.0, -3.0, 1.0}, {2.0, 0.0, 1.0, -2.0}, {-3.0, 1.0, 0.0, 3.0}, {1.0, -2.0, 3.0, 0.0}});
        RealMatrix actualT = schurTransformer.getT();
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSchurTransformerWithNullMatrix() {
        RealMatrix matrix = null;
        new SchurTransformer(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSchurTransformerWithEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        new SchurTransformer(matrix);
    }

    @Test
    public void testTransformWithDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.001);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.001);
    }

    @Test
    public void testTransformWithUpperTriangularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.538460226, 0.538460226, -0.538460226},
                {-0.81649658, 0.447234316, -0.447234316},
                {-0.447234316, 0.81649658, 0.81649658},
                {0.447234316, -0.81649658, 0.447234316}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{0.0, 2.0, -3.0}, {2.0, 0.0, 1.0}, {-3.0, 1.0, 0.0}});
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.001);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.001);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithNonConvergentMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0}, {1.0, 1.0, 1.0, 1.0}});
        new SchurTransformer(matrix);
    }
}