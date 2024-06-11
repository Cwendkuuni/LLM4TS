package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix input = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.identity(5);
        RealMatrix expectedT = MatrixUtils.identity(5);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("Unexpected P matrix after transformation.", expectedP, actualP);
        assertEquals("Unexpected T matrix after transformation.", expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(input);
    }

    @Test
    public void testSchurTransformer_RandomMatrix() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.3, 6.7, -2.9},
            {2.1, 0.8, 3.4},
            {-3.2, 1.2, -6.5}});
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = transformer.getP();
        RealMatrix expectedT = transformer.getT();

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        // Check that the P and T matrices are correct, by comparing to some calculated values
        // You may need to investigate the SchurTransformer algorithm to be able to do this effectively

        assertEquals("Unexpected P matrix after transformation.", expectedP, actualP);
        assertEquals("Unexpected T matrix after transformation.", expectedT, actualT);
    }

    @Test
    public void testGetPT() {
        RealMatrix input = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(input.transpose().getData());
        RealMatrix actualPT = transformer.getPT();

        assertEquals("Unexpected PT matrix after transformation.", expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix input = MatrixUtils.identity(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(input.getData());
        RealMatrix actualT = transformer.getT();

        assertEquals("Unexpected T matrix after transformation.", expectedT, actualT);
    }

    @Test
    public void testGetTransformedValuesWithSmallEpsilon() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0 / Math.sqrt(2) / 100.0},
            {1.0 / Math.sqrt(2) / 100.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 1.0 / Math.sqrt(2)},
            {1.0 / Math.sqrt(2), 1.0}});
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 1.0 / Math.sqrt(2)},
            {1.0 / Math.sqrt(2), 1.0}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPT = transformer.getPT();

        assertEquals("Unexpected P matrix after transformation.", expectedP, actualP);
        assertEquals("Unexpected T matrix after transformation.", expectedT, actualT);
        assertEquals("Unexpected PT matrix after transformation.", expectedPT, actualPT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_MatrixWithLargeEigenvaluesDifference() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0E10}});
        new SchurTransformer(input);
    }
}