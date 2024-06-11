import org.apache.commons.math3.linear.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecomposition decomposition;

    @Before
    public void setUp() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            decomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("SingularValueDecompositionImpl initialization failed.");
        }
    }

    @After
    public void tearDown() {
        if (decomposition != null) {
            decomposition = null;
        }
        if (matrix != null) {
            matrix = null;
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix u = decomposition.getU();
        assertEquals(3, u.getRowDimension());
        assertEquals(3, u.getColumnDimension());

        // Check orthogonal property
        RealMatrix uT = u.transpose();
        RealMatrix product = u.multiply(uT);
        assertEquals(MatrixUtils.IDENTITY_MATRIX, product);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix ut = decomposition.getUT();
        assertEquals(3, ut.getRowDimension());
        assertEquals(3, ut.getColumnDimension());

        // Check orthogonal property
        RealMatrix utu = ut.multiply(ut);
        assertEquals(MatrixUtils.IDENTITY_MATRIX, utu);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix s = decomposition.getS();
        assertEquals(3, s.getRowDimension());
        assertEquals(3, s.getColumnDimension());
        assertEquals(1.0, s.getEntry(0, 0), 1e-14);
        assertEquals(Math.sqrt(25.0 + 25.0), s.getEntry(1, 1), 1e-14);
        assertEquals(Math.sqrt(136.0 + 136.0), s.getEntry(2, 2), 1e-14);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        double[] singularValues = decomposition.getSingularValues();
        assertEquals(3, singularValues.length);
        assertEquals(1.0, singularValues[0], 1e-14);
        assertEquals(Math.sqrt(25.0 + 25.0), singularValues[1], 1e-14);
        assertEquals(Math.sqrt(136.0 + 136.0), singularValues[2], 1e-14);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix v = decomposition.getV();
        assertEquals(3, v.getRowDimension());
        assertEquals(3, v.getColumnDimension());

        // Check orthogonal property
        RealMatrix vt = v.transpose();
        RealMatrix product = v.multiply(vt);
        assertEquals(MatrixUtils.IDENTITY_MATRIX, product);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix vt = decomposition.getVT();
        assertEquals(3, vt.getRowDimension());
        assertEquals(3, vt.getColumnDimension());

        // Check orthogonal property
        RealMatrix vtv = vt.multiply(vt);
        assertEquals(MatrixUtils.IDENTITY_MATRIX, vtv);
    }

    @Test
    public void testCovariance() throws InvalidMatrixException {
        double minSingularValue = 2.0;
        RealMatrix covariance = decomposition.getCovariance(minSingularValue);
        assertEquals(3, covariance.getRowDimension());
        assertEquals(3, covariance.getColumnDimension());

        // Perform singular value decomposition of covariance matrix to ensure
        // result is correct
        SingularValueDecomposition covarianceSvd = new SingularValueDecompositionImpl(covariance);
        RealMatrix covarianceU = covarianceSvd.getU();
        double[] covarianceS = covarianceSvd.getSingularValues().clone();
        RealMatrix covarianceV = covarianceSvd.getV();

        // Check U and V from SVD are similar to U and V from original SVD
        RealMatrix uCheck = MatrixUtils.createRealMatrix(decomposition.getU().getData());
        assertEquals(covarianceU, uCheck);
        assertEquals(covarianceV, decomposition.getV());

        // Check singular values are smaller than minSingularValue
        for (int i = 0; i < covarianceS.length; i++) {
            assertTrue(covarianceS[i] < minSingularValue);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetNormWithSingularityException()
            throws InvalidMatrixException {
        RealMatrix singularityExceptionMatrix = new Array2DRowRealMatrix(
                new double[][] {{1.0}}, new int[] {1, 1});
        SingularValueDecompositionImpl singularityExceptionDecomposition =
                new SingularValueDecompositionImpl(singularityExceptionMatrix);
        singularityExceptionDecomposition.getNorm();
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        double conditionNumber = decomposition.getConditionNumber();
        assertEquals(Math.sqrt(1.0 / 0.00324082), conditionNumber, 1e-14);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        int rank = decomposition.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        DecompositionSolver solver = decomposition.getSolver();
        assertTrue(solver instanceof SingularValueDecompositionImpl.Solver);
    }

}