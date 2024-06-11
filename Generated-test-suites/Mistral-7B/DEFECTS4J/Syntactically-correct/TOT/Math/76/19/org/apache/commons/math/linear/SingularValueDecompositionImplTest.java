package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the constructor with a 2x2 matrix.
     */
    @Test
    public void test2x2() {
        double[][] matrixData = new double[][] { {1.0, 2.0}, {3.0, 4.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix);
            RealMatrix u = decomposition.getU();
            RealMatrix s = decomposition.getS();
            RealMatrix v = decomposition.getV();
            RealMatrix ut = decomposition.getUT();
            RealMatrix st = decomposition.getS();
            RealMatrix vt = decomposition.getVT();
            RealVector sv = decomposition.getSingularValues();

            assertEquals("U should be orthogonal", 1.0, u.determinant(), 1E-12);
            assertEquals("V^T should be orthogonal", 1.0, vt.determinant(), 1E-12);
            assertEquals("UsV^T should be equal to the identity",
                         1.0, u.multiply(vt).determinant(), 1E-12);
            assertEquals("U^TsV should be equal to the original matrix",
                         matrix, ut.multiply(s).multiply(v), 1E-6);
            assertEquals("Singular values should be correct", 5.0, sv[0], 1E-6);
            assertEquals("Singular values should be correct", 3.0, sv[1], 1E-6);

            // test getSolver
            DecompositionSolver solver = decomposition.getSolver();
            RealVector b = new Array2DRowRealVector(new double[] {1.0, 2.0});
            RealVector x = solver.solve(b);
            assertEquals("x should be equal to U^Tb", 2.9999999999999996, x.getEntry(0),
                         1E-6);
            assertEquals("x should be equal to U^Tb", 0.9999999999999994, x.getEntry(1),
                         1E-6);

            RealMatrix bm = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0},
                {3.0, 4.0}});
            RealMatrix xm = solver.solve(bm);
            assertEquals("x should be equal to U^Tb",
                         new Array2DRowRealMatrix(new double[][] {{5.999999999999999,
                         1.9999999999999997}, {-1.9999999999999997, 5.999999999999999}}),
                         xm, 1E-6);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the constructor with a 3x3 matrix.
     */
    @Test
    public void test3x3() {
        double[][] matrixData = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix u = decomposition.getU();
            RealMatrix s = decomposition.getS();
            RealMatrix v = decomposition.getV();
            RealMatrix ut = decomposition.getUT();
            RealMatrix st = decomposition.getS();
            RealMatrix vt = decomposition.getVT();
            RealVector sv = decomposition.getSingularValues();

            assertEquals("U should be orthogonal", 1.0, u.determinant(), 1E-12);
            assertEquals("V^T should be orthogonal", 1.0, vt.determinant(), 1E-12);
            assertEquals("UsV^T should be equal to the identity",
                         1.0, u.multiply(vt).determinant(), 1E-12);
            assertEquals("U^TsV should be equal to the original matrix",
                         matrix, ut.multiply(s).multiply(v), 1E-6);
            assertEquals("Singular values should be correct", 11.875722945133037,
                         sv[0], 1E-6);
            assertEquals("Singular values should be correct", 0.0, sv[1], 1E-6);
            assertEquals("Singular values should be correct", 0.0, sv[2], 1E-6);

            // test getSolver
            DecompositionSolver solver = decomposition.getSolver();
            RealVector b = new Array2DRowRealVector(new double[] {1.0, 2.0, 3.0});
            RealVector x = solver.solve(b);
            assertEquals("x should be equal to U^Tb",
                         new Array2DRowRealMatrix(new double[][] {{1.0000000000000001,
                         0.0, -0.0}, {-0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}).getColumnVector(0),
                         x, 1E-6);

            RealMatrix bm = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
            RealMatrix xm = solver.solve(bm);
            assertEquals("x should be equal to U^Tb",
                         new Array2DRowRealMatrix(new double[][] {{1.0000000000000001,
                         0.0, -0.0}, {-0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}).multiply(bm),
                         xm, 1E-6);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the constructor with a singular matrix (3x3).
     */
    @Test
    public void testSingularMatrix3x3() {
        double[][] matrixData = new double[][] { {0.0, 1.0, 1.0}, {1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            new SingularValueDecompositionImpl(matrix, 3);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    /**
     * Test the constructor with a non-square matrix (2x3).
     */
    @Test
    public void testNonSquareMatrix2x3() {
        double[][] matrixData = new double[][] { {1.0, 2.0}, {3.0, 4.0, 5.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            new SingularValueDecompositionImpl(matrix, 2);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    /**
     * Test the getConditionNumber method.
     */
    @Test
    public void testGetConditionNumber() {
        double[][] matrixData = new double[][] { {1.0, 2.0}, {3.0, 4.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix);
            double conditionNumber = decomposition.getConditionNumber();
            assertEquals("Condition number should be correct", 6.480740697327255,
                         conditionNumber, 1E-6);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getRank method.
     */
    @Test
    public void testGetRank() {
        double[][] matrixData = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix, 2);
            int rank = decomposition.getRank();
            assertEquals("Rank should be correct", 3, rank);

            // test rank = Math.max(m, n)
            SingularValueDecompositionImpl decomposition2 =
                new SingularValueDecompositionImpl(matrix);
            int rank2 = decomposition2.getRank();
            assertEquals("Rank should be correct", 3, rank2);

            // test rank = min(r, t)
            SingularValueDecompositionImpl decomposition3 =
                new SingularValueDecompositionImpl(matrix, 2);
            int rank3 = decomposition3.getRank();
            assertEquals("Rank should be correct", 3, rank3);

            // test rank = 0
            double[][] matrixData2 = new double[][] { {0.0, 0.0}, {0.0, 0.0} };
            RealMatrix matrix2 = new Array2DRowRealMatrix(matrixData2, false);
            SingularValueDecompositionImpl decomposition4 =
                new SingularValueDecompositionImpl(matrix2);
            int rank4 = decomposition4.getRank();
            assertEquals("Rank should be correct", 0, rank4);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getCovariance method.
     */
    @Test
    public void testGetCovariance() {
        double[][] matrixData = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix covariance = decomposition.getCovariance(0.1);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 1.0},
                {1.0, 1.0}, {1.0, 1.0}});
            assertEquals("Covariance should be correct", expected, covariance, 1E-12);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[][] matrixData2 = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0} };
        RealMatrix matrix2 = new Array2DRowRealMatrix(matrixData2, false);
        try {
            SingularValueDecompositionImpl decomposition2 =
                new SingularValueDecompositionImpl(matrix2, 2);
            RealMatrix covariance2 = decomposition2.getCovariance(0.5);
            RealMatrix expected2 = new Array2DRowRealMatrix(new double[][] {{1.5, 1.5},
                {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}});
            assertEquals("Covariance should be correct", expected2, covariance2, 1E-12);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        double[][] matrixData3 = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}, {13.0, 14.0, 15.0} };
        RealMatrix matrix3 = new Array2DRowRealMatrix(matrixData3, false);
        try {
            SingularValueDecompositionImpl decomposition3 =
                new SingularValueDecompositionImpl(matrix3, 2);
            RealMatrix covariance3 = decomposition3.getCovariance(1.0);
            RealMatrix expected3 = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0},
                {0.0, 1.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}});
            assertEquals("Covariance should be correct", expected3, covariance3, 1E-12);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getNorm method.
     */
    @Test
    public void testGetNorm() {
        double[][] matrixData = new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0} };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        try {
            SingularValueDecompositionImpl decomposition =
                new SingularValueDecompositionImpl(matrix, 2);
            double norm = decomposition.getNorm();
            assertEquals("Norm should be correct", 15.0, norm, 1E-6);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

}