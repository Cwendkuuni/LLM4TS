package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testInitWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix actual = transformer.getP();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{5.36633341355266, 3.04381356163161, 0.0},
                {3.04381356163161, 2.36623619393252, 3.0},
                {0.0, 3.0, 0.63245553602876}});
        RealMatrix actual = transformer.getT();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{5.36633341355266, 2.36623619393252, 0.63245553602876},
                {3.04381356163161, 1.0, 3.0},
                {0.0, 4.0, 0.63245553602876}});
        RealMatrix actual = transformer.getPT();
        assertEquals(expected, actual);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{5.36633341355266, 3.04381356163161, 0.0},
                {3.04381356163161, 2.36623619393252, 3.0},
                {0.0, 3.0, 0.63245553602876}});
        RealMatrix actual = transformer.getT();
        assertEquals(expected, actual);

        expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        actual = transformer.getP();
        assertEquals(expected, actual);

        expected = new Array2DRowRealMatrix(new double[][]{{5.36633341355266, 2.36623619393252, 0.63245553602876},
                {3.04381356163161, 1.0, 3.0},
                {0.0, 4.0, 0.63245553602876}});
        actual = transformer.getPT();
        assertEquals(expected, actual);
    }
}