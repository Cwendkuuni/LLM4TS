package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    /**
     * Test the construction of the SVD of a given matrix, and access to the
     * various parts of the decomposition.
     */
    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealVector singularValues = svd.getSingularValues();

        // test the dimensions
        assertEquals(matrix.getRowDimension(), U.getRowDimension());
        assertEquals(matrix.getColumnDimension(), U.getColumnDimension());
        assertEquals(matrix.getRowDimension(), S.getRowDimension());
        assertEquals(matrix.getColumnDimension(), S.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), V.getRowDimension());
        assertEquals(matrix.getColumnDimension(), V.getColumnDimension());

        // test the singular values
        assertNotNull(singularValues);
        assertEquals(3, singularValues.getDimension());
        assertEquals(1.0, singularValues.getEntry(0), 1e-14);
        assertEquals(5.432103, singularValues.getEntry(1), 1e-14);
        assertEquals(0.0, singularValues.getEntry(2), 1e-14);

        // test the matrices
        assertNotNull(U);
        assertTrue(U.isOrthogonal());

        assertNotNull(S);
        assertTrue(S.isDiagonal());
        assertEquals(3, S.getRowDimension());
        assertEquals(3, S.getColumnDimension());

        assertNotNull(V);
        assertTrue(V.isOrthogonal());

        // test the decomposed matrix
        RealMatrix A = matrix.multiply(V);
        RealMatrix UT = U.transpose();
        RealMatrix USV = UT.multiply(S).multiply(V);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(3);
        RealMatrix error = A.subtract(USV);
        assertTrue(error.getMaxEntry().getValue() <= 1e-12);

    }

    /**
     * Test the construction of the SVD of a given matrix, and access to the
     * various parts of the decomposition with one less singular value than the
     * original matrix.
     */
    @Test
    public void testSingularValueDecompositionTruncated() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealVector singularValues = svd.getSingularValues();

        // test the dimensions
        assertEquals(matrix.getRowDimension(), U.getRowDimension());
        assertEquals(matrix.getColumnDimension(), U.getColumnDimension());
        assertEquals(matrix.getRowDimension(), S.getRowDimension());
        assertEquals(2, S.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), V.getRowDimension());
        assertEquals(matrix.getColumnDimension(), V.getColumnDimension());

        // test the singular values
        assertNotNull(singularValues);
        assertEquals(2, singularValues.getDimension());
        assertEquals(1.0, singularValues.getEntry(0), 1e-14);
        assertEquals(7.746004, singularValues.getEntry(1), 1e-14);

        // test the matrices
        assertNotNull(U);
        assertTrue(U.isOrthogonal());

        assertNotNull(S);
        assertTrue(S.isDiagonal());
        assertEquals(2, S.getRowDimension());
        assertEquals(2, S.getColumnDimension());

        assertNotNull(V);
        assertTrue(V.isOrthogonal());

        // test the decomposed matrix
        RealMatrix A = matrix.multiply(V);
        RealMatrix UT = U.transpose();
        RealMatrix USV = UT.multiply(S).multiply(V);
        RealMatrix error = A.subtract(USV);
        assertTrue(error.getMaxEntry().getValue() <= 1e-12);

    }

    /**
     * Test the construction of the SVD of a given matrix, and access to the
     * various parts of the decomposition with one less singular value than the
     * number of columns of the original matrix.
     */
    @Test
    public void testSingularValueDecompositionSingular() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealVector singularValues = svd.getSingularValues();

        // test the dimensions
        assertEquals(matrix.getRowDimension(), U.getRowDimension());
        assertEquals(matrix.getColumnDimension(), U.getColumnDimension());
        assertEquals(matrix.getRowDimension(), S.getRowDimension());
        assertEquals(1, S.getColumnDimension());
        assertEquals(matrix.getColumnDimension() - 1, V.getRowDimension());
        assertEquals(2, V.getColumnDimension());

        // test the singular values
        assertNotNull(singularValues);
        assertEquals(1, singularValues.getDimension());
        assertEquals(1.0, singularValues.getEntry(0), 1e-14);

        // test the matrices
        assertNotNull(U);
        assertTrue(U.isOrthogonal());

        assertNotNull(S);
        assertTrue(S.isDiagonal());
        assertEquals(1, S.getRowDimension());
        assertEquals(1, S.getColumnDimension());

        assertNotNull(V);
        assertTrue(V.isOrthogonal());

        // test the decomposed matrix
        RealMatrix A = matrix.multiply(V);
        RealMatrix UT = U.transpose();
        RealMatrix USV = UT.multiply(S).multiply(V);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(matrix.getRowDimension());
        RealMatrix error = A.subtract(USV);
        assertTrue(error.getMaxEntry().getValue() <= 1e-12);

    }

    /**
     * Test the construction of the SVD of a given matrix, and access to the
     * various parts of the decomposition with one less singular value than the
     * number of rows of the original matrix.
     */
    @Test
    public void testSingularValueDecompositionUndersized() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0},
                {7.0, 8.0}
        });

        try {
            new SingularValueDecompositionImpl(matrix, 2);
            fail("Expected an InvalidMatrixException");
        } catch (Exception e) {
            assertEquals(InvalidMatrixException.class, e.getClass());
        }

    }

    /**
     * Test the construction of the SVD of a given non-square matrix.
     */
    @Test
    public void testSingularValueDecompositionNonSquare() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {4.0, 5.0},
                {}
        });

        try {
            new SingularValueDecompositionImpl(matrix, 2);
            fail("Expected an InvalidMatrixException");
        } catch (Exception e) {
            assertEquals(InvalidMatrixException.class, e.getClass());
        }

    }

    /**
     * Test the pseudo-inverse of the SVD.
     */
    @Test
    public void testPseudoInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(3, x.getDimension());
        RealVector xExpected = new ArrayRealVector(new double[]{1.110223, -0.559612, 0.112524});
        assertEquals(xExpected, x, 1e-12);

        RealVector b2 = new ArrayRealVector(new double[]{1.0, 2.0, 0.0});
        RealVector x2 = solver.solve(b2);
        assertEquals(3, x2.getDimension());
        RealVector x2Expected = new ArrayRealVector(new double[]{1.110223, -0.559612, 0.0});
        assertEquals(x2Expected, x2, 1e-12);

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {1.0, 2.0, 3.0}
        });
        RealMatrix X = solver.getInverse().multiply(B);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {2.244898, 0.873394, 0.277447, -0.277447},
                {-0.873394, -2.244898, -0.277447, 0.277447},
                {0.277447, 0.277447, 1.5, -0.5},
                {-0.277447, 0.277447, -0.5, 1.5}
        });
        assertTrue(X.similar(expected, 1e-12));

    }

    /**
     * Test the pseudo-inverse of the SVD when the matrix is singular.
     */
    @Test
    public void testPseudoInverseSingular() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 0.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        assertEquals(3, x.getDimension());
        RealVector xExpected = new ArrayRealVector(new double[]{1.110223, -0.559612, 0.0});
        assertEquals(xExpected, x, 1e-12);

        RealVector b2 = new ArrayRealVector(new double[]{1.0, 2.0, 0.0});
        RealVector x2 = solver.solve(b2);
        assertEquals(3, x2.getDimension());
        RealVector x2Expected = new ArrayRealVector(new double[]{1.110223, -0.559612, 0.0});
        assertEquals(x2Expected, x2, 1e-12);

        RealMatrix B = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0}
        });
        RealMatrix X = solver.getInverse().multiply(B);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                {1.838131, -0.771523, 0.203431, -0.203431, 0.0},
                {-0.771523, -1.838131, 0.203431, 0.203431, 0.0},
                {0.203431, 0.203431, 1.5, -0.5, 0.0},
                {-0.203431, 0.203431, -0.5, 1.5, 0.0},
                {0.0, 0.0, 0.0, 0.0, 0.0}
        });
        assertTrue(X.similar(expected, 1e-12));

    }

}