package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CholeskyDecompositionImplTest {

    /**
     * Test constructor with valid input
     */
    @Test
    public void testConstructorValidInput() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lT = cholesky.getLT();
        assertNotNull(l);
        assertNotNull(lT);
        assertEquals(l.getRowDimension(), lT.getColumnDimension(), 0.0);
    }

    /**
     * Test constructor with non-square matrix
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        double[][] data = {
            {1.0, 2.0},
            {3.0, 4.0, 5.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test constructor with non-symmetric matrix
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorNonSymmetricMatrix() {
        double[][] data = {
            {1.0, 2.0},
            {3.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    /**
     * Test constructor with non-positive definite matrix
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[][] data = {
            {1.0, 2.0},
            {2.0, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    /**
     * Test getL() method
     */
    @Test
    public void testGetL() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        assertEquals(l.getRowDimension(), l.getColumnDimension(), 0.0);
        assertEquals(l.getEntry(0, 0), 1.0, 1e-15);
        assertEquals(l.getEntry(1, 0), 0.0, 1e-15);
        assertEquals(l.getEntry(1, 1), Math.sqrt(5.0), 1e-15);
        assertEquals(l.getEntry(2, 0), 0.0, 1e-15);
        assertEquals(l.getEntry(2, 1), 0.0, 1e-15);
        assertEquals(l.getEntry(2, 2), Math.sqrt(10.0), 1e-15);
    }

    /**
     * Test getLT() method
     */
    @Test
    public void testGetLT() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = cholesky.getLT();
        assertEquals(lt.getRowDimension(), lt.getColumnDimension(), 0.0);
        assertEquals(lt.getEntry(0, 0), 1.0, 1e-15);
        assertEquals(lt.getEntry(0, 1), 2.0, 1e-15);
        assertEquals(lt.getEntry(0, 2), 3.0, 1e-15);
        assertEquals(lt.getEntry(1, 0), 2.0, 1e-15);
        assertEquals(lt.getEntry(1, 1), Math.sqrt(5.0), 1e-15);
        assertEquals(lt.getEntry(1,
            2), (3.0 / Math.sqrt(5.0)), 1e-15);
        assertEquals(lt.getEntry(2, 0), 3.0, 1e-15);
        assertEquals(lt.getEntry(2, 1), (6.0 / Math.sqrt(5.0)), 1e-15);
        assertEquals(lt.getEntry(2, 2), Math.sqrt(10.0), 1e-15);
    }

    /**
     * Test getDeterminant() method
     */
    @Test
    public void testGetDeterminant() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertEquals(cholesky.getDeterminant(), 150.0, 1e-15);
    }

    /**
     * Test getSolver() method
     */
    @Test
    public void testGetSolver() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 6.0},
            {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        RealVector x = solver.solve(new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).transpose());
        assertEquals(x.getDimension(), 2, 0.0);
        assertEquals(x.getEntry(0), 0.244948974278317, 1e-15);
        assertEquals(x.getEntry(1), -0.489897948556634, 1e-15);
    }

    /**
     * Test getSolver() method with singular matrix
     */
    @Test(expected = SingularValueDecomposition.SingularMatrixException.class)
    public void testGetSolverSingularMatrix() {
        double[][] data = {
            {1.0, 2.0},
            {2.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        cholesky.getSolver().solve(new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}}).transpose());
    }

}