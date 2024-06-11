import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getVT();

        // Check that the product USV^T is equal to the original matrix
        RealMatrix product = U.multiply(S).multiply(V);
        assertEquals("The product USV^T should be equal to the original matrix", matrix, product);

        // Check that the singular values are correct
        double[] singularValues = svd.getSingularValues();
        assertArrayEquals("Singular values should be 0.0, 3.91436975333715, 8.51473854920561", new double[]{0.0, 3.91436975333715, 8.51473854920561}, singularValues);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = SingularValueDecompositionException.class)
    public void testSingularValueDecompositionException() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 0.0, 9.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetRank() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        assertEquals("The rank of the matrix should be 3", 3, rank);
    }

    @Test
    public void testGetConditionNumber() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        assertTrue("The condition number should be greater than 1", conditionNumber > 1);
    }

    @Test
    public void testGetNorm() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        assertEquals("The norm of the matrix should be the first singular value", 8.51473854920561, norm, 1e-15);
    }

    @Test
    public void testGetCovariance() throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(5.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.482113347194344, 0.426412950315561, 0.371326878557131},
                {0.426412950315561, 0.388895881983132, 0.342552457346343},
                {0.371326878557131, 0.342552457346343, 0.285714286775582}});
        MatrixUtils.assertEquals("The covariance matrix should be equal to the expected covariance matrix", expectedCovariance, covariance);
    }

}