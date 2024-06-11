package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testIdentityMatrix() throws Exception {
        RealMatrix A = MatrixUtils.createIdentityMatrix(5);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A);

        RealMatrix U = svd.getU();
        RealMatrix V = svd.getV();
        RealMatrix S = svd.getS();

        RealMatrix UTranspose = U.transpose();
        RealMatrix VT = V.transpose();

        RealMatrix product = U.multiply(S).multiply(VT);
        assertEquals("Check product USV^T equals original matrix", A, product, 0.0);
    }

    @Test
    public void testSingularValues() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A, 3);

        double[] singularValues = svd.getSingularValues();
        assertArrayEquals("Check singular values", new double[]{1.41421356, 1.41421356, 0.0}, singularValues, 0.001);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValuesException() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        new SingularValueDecompositionImpl(A, 3);
    }

    @Test
    public void testSolveExample() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A);

        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 1.0});
        RealVector x = svd.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.73205081, -0.66120938, 0.36596476});

        assertArrayEquals("Check solution X", expected.toArray(), x.toArray(), 0.001);
    }

    @Test
    public void testRankDeficiency() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {0.0, 1.0, 1.0}, {0.0, 0.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A, 4);

        int rank = svd.getRank();
        assertEquals("Check rank = 3", 3, rank);

        RealMatrix C = svd.getCovariance(0);
        double[] values = StatUtils.eigenvalues(C);
        assertArrayEquals("Check eigenvalues", new double[]{0.0, 2.59825067, 3.42116239}, values, 0.001);
    }

    @Test
    public void testDeterminant() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A);

        double determinant = svd.getDeterminant();
        assertEquals("Check determinant = 1.0", 1.0, determinant, 0.0);

        A = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
        svd = new SingularValueDecompositionImpl(A);

        determinant = svd.getDeterminant();
        assertEquals("Check determinant = 0.0", 0.0, determinant, 0.0);
    }

    @Test
    public void testNonSquareMatrix() throws Exception {
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(A, 1);

        RealMatrix U = svd.getU();
        RealMatrix V = svd.getV();
        RealMatrix S = svd.getS();

        assertEquals("Check dimensions of U", 1, U.getRowDimension());
        assertEquals("Check dimensions of U", 1, U.getColumnDimension());

        assertEquals("Check dimensions of V", 1, V.getRowDimension());
        assertEquals("Check dimensions of V", 2, V.getColumnDimension());

        assertEquals("Check dimensions of S", 1, S.getRowDimension());
        assertEquals("Check dimensions of S", 1, S.getColumnDimension());

        double[] singularValues = svd.getSingularValues();
        assertArrayEquals("Check singular values", new double[]{1.0}, singularValues, 0.001);

        RealVector x = svd.getSolver().solve(new ArrayRealVector(new double[]{0.0}));
        assertArrayEquals("Check solution X", new double[]{1.0}, x.toArray(), 0.001);
    }
}