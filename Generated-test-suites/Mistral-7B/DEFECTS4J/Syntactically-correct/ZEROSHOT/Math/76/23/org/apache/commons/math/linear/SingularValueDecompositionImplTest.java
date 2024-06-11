import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    /**
     * Test SingularValueDecompositionImpl constructor with valid matrix.
     * @throws Exception if test fails
     */
    @Test
    public void testConstructorValidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
        assertTrue(Precision.isClose(svd.getSingularValues()[0], Math.sqrt(5.0)));
        assertTrue(Precision.isClose(svd.getSingularValues()[1], Math.sqrt(5.0)));
        RealMatrix u = svd.getU();
        RealMatrix v = svd.getVT();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.7071067811865475, -0.7071067811865475},
                {0.7071067811865475, 0.7071067811865475}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.7071067811865476, 0.7071067811865476},
                {0.7071067811865476, 0.7071067811865476}});
        assertArrayEquals(expectedU.getData(), u.getData(), 0.0);
        assertArrayEquals(expectedV.getData(), v.getData(), 0.0);
    }

    /**
     * Test SingularValueDecompositionImpl constructor with identity matrix.
     * @throws Exception if test fails
     */
    @Test
    public void testConstructorIdentityMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
        assertTrue(Precision.isClose(svd.getSingularValues()[0], 1.0));
        assertTrue(Precision.isClose(svd.getSingularValues()[1], 1.0));
        RealMatrix u = svd.getU();
        RealMatrix v = svd.getVT();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        assertArrayEquals(expectedU.getData(), u.getData(), 0.0);
        assertArrayEquals(expectedV.getData(), v.getData(), 0.0);
    }

    /**
     * Test SingularValueDecompositionImpl constructor with singular matrix.
     * @throws Exception if test fails
     */
    @Test(expected = InvalidMatrixException.class)
    public void testConstructorSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 1}, {1, 0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getRank method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {3, 4, 5}});
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 2);
        assertEquals(2, svd2.getRank());
    }

    /**
     * Test getSingularValues method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertArrayEquals(new double[]{Math.sqrt(5.0), Math.sqrt(5.0)}, svd.getSingularValues(), 0.0);
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {3, 4, 5}});
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 2);
        assertArrayEquals(new double[]{Math.sqrt(10.0), Math.sqrt(10.0)}, svd2.getSingularValues(), 0.0);
    }

    /**
     * Test getUT method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetUT() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{0.7071067811865475, -0.7071067811865475},
                {-0.7071067811865475, 0.7071067811865475}});
        assertArrayEquals(expectedUT.getData(), ut.getData(), 0.0);
    }

    /**
     * Test getU method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetU() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.7071067811865475, -0.7071067811865475},
                {0.7071067811865475, 0.7071067811865475}});
        assertArrayEquals(expectedU.getData(), u.getData(), 0.0);
    }

    /**
     * Test getV method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetV() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.7071067811865476, 0.7071067811865476},
                {0.7071067811865476, 0.7071067811865476}});
        assertArrayEquals(expectedV.getData(), v.getData(), 0.0);
    }

    /**
     * Test getVT method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetVT() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.7071067811865476, 0.7071067811865476},
                {0.7071067811865476, -0.7071067811865475}});
        assertArrayEquals(expectedVt.getData(), vt.getData(), 0.0);
    }

    /**
     * Test getS method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetS() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(5.0), 0},
                {0, Math.sqrt(5.0)}});
        assertArrayEquals(expectedS.getData(), s.getData(), 0.0);
    }

    /**
     * Test getCovariance method with valid input.
     * @throws Exception if test fails
     */
    @Test
    public void testGetCovarianceValidInput() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.7071067811865475, -0.7071067811865475},
                {-0.7071067811865475, 0.7071067811865475}});
        assertArrayEquals(expectedCovariance.getData(), covariance.getData(), 0.0);
    }

    /**
     * Test getCovariance method with invalid input.
     * @throws Exception if test fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceInvalidInput() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    /**
     * Test getNorm method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(5.0), svd.getNorm(), 0.0);
    }

    /**
     * Test getConditionNumber method.
     * @throws Exception if test fails
     */
    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(5.0) / Math.sqrt(1.0), svd.getConditionNumber(), 0.0);
    }

    /**
     * Test Solver class.
     * @throws Exception if test fails
     */
    @Test
    public void testSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        Solver solver = svd.getSolver();
        double[] b = new double[]{5.0, 6.0};
        RealVector x = solver.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{1.0 / Math.sqrt(5.0), -1.0 / Math.sqrt(5.0)});
        assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);
    }
}