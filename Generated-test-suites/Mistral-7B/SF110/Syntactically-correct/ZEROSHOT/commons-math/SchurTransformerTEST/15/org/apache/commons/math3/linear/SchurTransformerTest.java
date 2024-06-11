package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.735758582523534, 0.578483922088096, 0.131311509228713},
                {-0.578483922088096, 0.861227593350336, -0.180142765961616},
                {-0.131311509228713, -0.180142765961616, 0.973382309951235}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-0.927307692309831, -0.387298334547724, 0.0},
                {-0.387298334547724, 5.36692382258137, -2.15443463494167},
                {0.0, -2.15443463494167, 5.51298862286126}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertNotNull(actualP);
        assertNotNull(actualT);
        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
        fail("Should throw NonSquareMatrixException");
    }

    @Test
    public void testGetPT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}}));
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.735758582523534, -0.578483922088096, -0.131311509228713},
                {-0.578483922088096, 0.861227593350336, -0.180142765961616},
                {-0.131311509228713, -0.180142765961616, 0.973382309951235}});

        RealMatrix actualPT = transformer.getPT();

        assertNotNull(actualPT);
        assertArrayEquals(expectedPT.getData(), actualPT.getData(), 0.0);
    }

    @Test
    public void testGetT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}}));
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-0.927307692309831, -0.387298334547724, 0.0},
                {-0.387298334547724, 5.36692382258137, -2.15443463494167},
                {0.0, -2.15443463494167, 5.51298862286126}});

        RealMatrix actualT = transformer.getT();

        assertNotNull(actualT);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }
}