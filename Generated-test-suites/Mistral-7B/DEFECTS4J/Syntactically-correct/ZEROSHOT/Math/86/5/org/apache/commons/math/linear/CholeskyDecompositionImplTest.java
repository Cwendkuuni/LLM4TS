import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;

/**
 * JUnit test file for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    @org.junit.Test
    public void testCholeskyDecomposition() throws Exception {
        // Test with identity matrix
        RealMatrix identityMatrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(identityMatrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix result = l.multiply(lt);
        Assert.assertEquals(IdentityMatrix.IDENTITY_MATRIX, result);

        // Test with a random positive definite matrix
        double[][] randomMatrixData = {
                {1.0, 2.0, 2.0},
                {2.0, 3.0, 3.0},
                {2.0, 3.0, 4.0}
        };
        RealMatrix randomMatrix = new RealMatrixImpl(randomMatrixData);
        CholeskyDecomposition randomDecomposition = new CholeskyDecompositionImpl(randomMatrix);
        RealMatrix randomL = randomDecomposition.getL();
        RealMatrix randomLT = randomDecomposition.getLT();
        RealMatrix randomResult = randomL.multiply(randomLT);
        Assert.assertEquals(randomMatrix, randomResult);

        // Test exception cases
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(2));
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException ignored) {
        }

        try {
            RealMatrix nonSymmetricMatrix = new RealMatrixImpl(new double[][]{
                    {1.0, 2.0},
                    {2.0, 1.0}
            });
            new CholeskyDecompositionImpl(nonSymmetricMatrix);
            Assert.fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException ignored) {
        }

        try {
            RealMatrix notPositiveDefiniteMatrix = new RealMatrixImpl(new double[][]{
                    {1.0, 2.0, 2.0},
                    {2.0, -1.0, 3.0},
                    {2.0, 3.0, -1.0}
            });
            new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
            Assert.fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException ignored) {
        }

        // Test solving linear equations
        RealMatrix testMatrix = new RealMatrixImpl(new double[][]{
                {4.0, 1.0, 0.0},
                {1.0, 4.0, 1.0},
                {0.0, 1.0, 4.0}
        });
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        RealVector vector1 = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector solution1 = lt.solve(vector1);
        RealVector expectedSolution1 = new ArrayRealVector(new double[]{0.5, 0.0, 0.5});
        Assert.assertArrayEquals("Solution 1", expectedSolution1.toArray(), solution1.toArray(), 0.0);

        RealVector vector2 = new ArrayRealVector(new double[]{0.0, 1.0, 0.0});
        RealVector solution2 = lt.solve(vector2);
        RealVector expectedSolution2 = new ArrayRealVector(new double[]{0.3333333333333333, 1.0, -0.3333333333333333});
        Assert.assertArrayEquals("Solution 2", expectedSolution2.toArray(), solution2.toArray(), 0.0);

        RealVector vector3 = new ArrayRealVector(new double[]{0.0, 0.0, 1.0});
        RealVector solution3 = lt.solve(vector3);
        RealVector expectedSolution3 = new ArrayRealVector(new double[]{0.6666666666666667, -0.3333333333333333, 0.6666666666666667});
        Assert.assertArrayEquals("Solution 3", expectedSolution3.toArray(), solution3.toArray(), 0.0);

        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector x = l.solve(b);
        Assert.assertArrayEquals("Solution X", new double[]{0.5, 0.0, 0.5}, x.toArray(), 0.0);

        RealMatrix testMatrix2 = new RealMatrixImpl(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 3.0}
        });
        CholeskyDecomposition choleskyDecomposition2 = new CholeskyDecompositionImpl(testMatrix2);
        RealMatrix l2 = choleskyDecomposition2.getL();
        RealMatrix lt2 = choleskyDecomposition2.getLT();

        RealVector vector1_2 = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector solution1_2 = lt2.solve(vector1_2);
        RealVector expectedSolution1_2 = new ArrayRealVector(new double[]{0.3333333333333333, 0.0, 0.3333333333333333});
        Assert.assertArrayEquals("Solution 1 for matrix 2", expectedSolution1_2.toArray(), solution1_2.toArray(), 0.0);

        RealVector vector2_2 = new ArrayRealVector(new double[]{0.0, 1.0, 0.0});
        RealVector solution2_2 = lt2.solve(vector2_2);
        RealVector expectedSolution2_2 = new ArrayRealVector(new double[]{0.0, 1.111111111111111, 0.0});
        Assert.assertArrayEquals("Solution 2 for matrix 2", expectedSolution2_2.toArray(), solution2_2.toArray(), 0.0);

        RealVector vector3_2 = new ArrayRealVector(new double[]{0.0, 0.0, 1.0});
        RealVector solution3_2 = lt2.solve(vector3_2);
        RealVector expectedSolution3_2 = new ArrayRealVector(new double[]{0.8944271909999005, -0.4472135954999502, 0.8944271909999005});
        Assert.assertArrayEquals("Solution 3 for matrix 2", expectedSolution3_2.toArray(), solution3_2.toArray(), 0.0);

        RealVector b2 = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector x2 = l2.solve(b2);
        Assert.assertArrayEquals("Solution X for matrix 2", new double[]{0.632455536028736, 0.367811023531264, 0.632455536028736}, x2.toArray(), 0.0);
    }
}