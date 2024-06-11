import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.*;

import java.util.Random;

public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testCholeskyDecomposition() throws Exception {
        // Test with default thresholds
        RealMatrix matrix = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
       assertEquals(matrix, l.multiply(lt));
        RealMatrix inverse = l.getInverse();
        RealMatrix product = matrix.multiply(inverse);
        assertTrue(Precision.isClose(1.0, product.getDeterminant().doubleValue(), 1e-10));

        // Test with custom thresholds
        double relativeThreshold = 1.0e-8;
        double absoluteThreshold = 1.0e-12;
        RealMatrix matrixCustom = createRandomSymmetricPositiveDefiniteMatrix(5);
        CholeskyDecomposition decompositionCustom = new CholeskyDecompositionImpl(matrixCustom, relativeThreshold, absoluteThreshold);
        RealMatrix lCustom = decompositionCustom.getL();
        RealMatrix ltCustom = decompositionCustom.getLT();
        assertEquals(matrixCustom, lCustom.multiply(ltCustom));
        RealMatrix inverseCustom = lCustom.getInverse();
        RealMatrix productCustom = matrixCustom.multiply(inverseCustom);
        assertTrue(Precision.isClose(1.0, productCustom.getDeterminant().doubleValue(), 1e-10));

        // Test exception when matrix is not square
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {3.0, 4.0, 5.0}
            }));
            fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }

        // Test exception when matrix is not symmetric
        double[][] matrixNotSymmetric = {
                {1.0, 1.0},
                {2.0, 3.0}
        };
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(matrixNotSymmetric));
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test exception when matrix is not positive definite
        double[][] matrixNotPosDefinite = {
                {1.0, 0.0},
                {0.0, -1.0}
        };
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(matrixNotPosDefinite));
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    private RealMatrix createRandomSymmetricPositiveDefiniteMatrix(int size) {
        Random random = new Random();
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                data[i][j] = data[j][i] = random.nextGaussian();
                data[i][i] += data[i][i] * random.nextDouble();
            }
        }
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        matrix = matrix.transpose().multiply(matrix);
        return matrix;
    }
}