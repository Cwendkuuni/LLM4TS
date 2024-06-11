import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_IdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixBuilder.eye(3, 3);
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix expectedU = RealMatrixBuilder.identity(3, 3);
        RealMatrix expectedS = RealMatrixBuilder.eye(3, 3).scalarMultiply(Math.sqrt(new double[]{1.0, 1.0, 1.0}));
        RealMatrix expectedV = RealMatrixBuilder.identity(3, 3);

        RealMatrix actualU = singularValueDecomposition.getU();
        RealMatrix actualS = singularValueDecomposition.getS();
        RealMatrix actualV = singularValueDecomposition.getV();

        assertEquals(expectedU, actualU);
        assertEquals(expectedS, actualS);
        assertEquals(expectedV, actualV);
    }

    @Test
    public void testSingularValueDecompositionImpl_RandomMatrix() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixBuilder.randn(3, 3);
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix expectedU = singularValueDecomposition.getU();
        RealMatrix expectedS = singularValueDecomposition.getS();
        RealMatrix expectedV = singularValueDecomposition.getV();

        // Singular Value Decomposition is not unique, so there can be different U, V matrices
        RealMatrix actualU1 = singularValueDecomposition.getU();
        RealMatrix actualS1 = singularValueDecomposition.getS();
        RealMatrix actualV1 = singularValueDecomposition.getV();

        SingularValueDecompositionImpl singularValueDecomposition2 = new SingularValueDecompositionImpl(matrix.transpose());
        RealMatrix actualU2 = singularValueDecomposition2.getU();
        RealMatrix actualS2 = singularValueDecomposition2.getS();
        RealMatrix actualV2 = singularValueDecomposition2.getV();

        // Test that the product of U, S, V is equal to the original matrix
        RealMatrix productUSV = actualU1.multiply(actualS1).multiply(actualV1.transpose());
        assertEquals(matrix, productUSV, 1e-9);

        productUSV = actualU2.multiply(actualS2).multiply(actualV2.transpose());
        assertEquals(matrix, productUSV, 1e-9);

        // Test that the inverse of S is equal to the pseudo-inverse of the original matrix
        RealMatrix expectedPseudoInverse = actualV1.transpose().multiply(actualU1.multiply(MatrixUtils.createDiagonalMatrix(Math.pow(Math.sqrt(new double[]{1.0, 1.0, 1.0})).toArray(new double[3]))));
        RealMatrix actualPseudoInverse = singularValueDecomposition.getSolver().getInverse();

        assertEquals(expectedPseudoInverse, actualPseudoInverse);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_NonSquareMatrix() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixBuilder.randn(3, 2);
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_SingularMatrix() throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixBuilder.randn(3, 3).multiply(0.5);
        new SingularValueDecompositionImpl(matrix);
    }
}