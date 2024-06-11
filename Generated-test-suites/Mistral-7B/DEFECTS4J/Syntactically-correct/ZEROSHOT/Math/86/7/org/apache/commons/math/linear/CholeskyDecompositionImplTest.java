package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.junit.Assert;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.createRealVector;

public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testConstructors() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        Assert.assertEquals(matrix, cholesky.getMatrix());
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = createRandomMatrix(2, 3);
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = createRandomMatrix(3, 3);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 1.0);
        matrix.setEntry(2, 2, 1.0);
        matrix.setEntry(1, 1, 0.9);
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = createRandomMatrix(3, 3);
        matrix.setEntry(0, 0, -1.0);
        new CholeskyDecompositionImpl(matrix);
    }

    @org.junit.Test
    public void testGetL() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix L = cholesky.getL();
        Assert.assertEquals(L.getRowDimension(), L.getColumnDimension());
        Assert.assertArrayEquals(matrix.getData(), L.transpose().getData());
    }

    @org.junit.Test
    public void testGetLT() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix LT = cholesky.getLT();
        Assert.assertEquals(matrix.getRowDimension(), LT.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), LT.getColumnDimension());
        Assert.assertEquals(matrix, LT.transpose().multiply(LT));
    }

    @org.junit.Test
    public void testGetDeterminant() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();
        Assert.assertNotZero(determinant);
    }

    @org.junit.Test
    public void testSolver() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(4);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealVector b = createRealVector(4);
        RealVector x = cholesky.getSolver().solve(b);
        RealVector y = matrix.operate(x);
        Assert.assertArrayEquals(b.getData(), y.getData(), 1e-15);
    }

    @org.junit.Test
    public void testInverse() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(4);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = cholesky.getSolver().getInverse();
        RealMatrix product = matrix.multiply(inverse);
        Assert.assertTrue(product.isIdentityMatrix());
    }

    @org.junit.Test
    public void testLUDecompositionComparison() {
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(4);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        LUDecomposition lu = new LUDecomposition(matrix);
        RealMatrix L = cholesky.getL();
        RealMatrix U = createRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                       {lu.getL11(), lu.getL12(), lu.getL13(), lu.getL14()},
                                                       {lu.getL21(), lu.getL22(), lu.getL23(), lu.getL24()},
                                                       {lu.getL31(), lu.getL32(), lu.getL33(), lu.getL34()},
                                                       {lu.getL41(), lu.getL42(), lu.getL43(), lu.getL44()}});
        Assert.assertTrue(L.isSimilar(cholesky.getL()));
        Assert.assertTrue(U.isSimilar(cholesky.getSolver().getInverse()));
    }

    private RealMatrix createRandomMatrix(int rows, int columns) {
        double[][] data = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = Math.random();
            }
        }
        return createRealMatrix(data);
    }

    private RealVector createRealVector(int size) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = Math.random();
        }
        return new RealVectorImpl(data, false);
    }

    private RealMatrix createRandomSymmetricPositiveDefiniteMatrix(int size) {
        RealMatrix matrix = createRandomMatrix(size, size);
        RealMatrix transpose = matrix.transpose();
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                double temp = matrix.getEntry(i, j);
                double diagonal = matrix.getEntry(i, i);
                if (temp < 0.0) {
                    temp *= -1.0;
                }
                matrix.setEntry(i, j, temp + diagonal);
                transpose.setEntry(j, i, temp + diagonal);
            }
        }
        return matrix;
    }

}