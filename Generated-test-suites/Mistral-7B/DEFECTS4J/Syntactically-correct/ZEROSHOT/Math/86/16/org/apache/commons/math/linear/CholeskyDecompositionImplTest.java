import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorThrowsExceptions() throws Exception {
        // Test non-square matrix
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 2},
                {3, 4, 5}
        });
        try {
            new CholeskyDecompositionImpl(nonSquareMatrix);
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        // Test not symmetric matrix
        RealMatrix notSymmetricMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 1},
                {1, 2}
        });
        try {
            new CholeskyDecompositionImpl(notSymmetricMatrix, 1.0e-15, 1.0e-10);
            Assert.fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // Expected
        }

        // Test not positive definite matrix
        RealMatrix notPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 0},
                {0, -1}
        });
        try {
            new CholeskyDecompositionImpl(notPositiveDefiniteMatrix, 1.0e-15, 1.0e-10);
            Assert.fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 0.1, 0.05},
                {0.1, 1, 0.1},
                {0.05, 0.1, 1}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
        CholeskyDecomposition expected = new CholeskyDecomposition() {
            @Override
            public RealMatrix getL() {
                return new RealMatrixImpl(new double[][]{
                        {1.0, 0, 0},
                        {0.447213595522388, 1.0, 0},
                        {0.175781185195512, 0.632450249864104, 1.0}
                }, false);
            }

            @Override
            public RealMatrix getLT() {
                return new RealMatrixImpl(new double[][]{
                        {1.0, 0.447213595522388, 0.175781185195512},
                        {0, 1.0, 0.632450249864104},
                        {0, 0, 1.0}
                }, false);
            }
        };
        Assert.assertEquals(expected.getL(), choleskyDecomposition.getL());
        Assert.assertEquals(expected.getLT(), choleskyDecomposition.getLT());
        Assert.assertEquals(expected.getDeterminant(), choleskyDecomposition.getDeterminant(), 1e-15);
    }

    @Test
    public void testSolver() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 0.1, 0.05},
                {0.1, 1, 0.1},
                {0.05, 0.1, 1}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-15, 1.0e-10);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        // Test solve(RealVector)
        double[] b = new double[]{1, 2, 3};
        double[] result = solver.solve(new RealVectorImpl(b, false));
        double[] expected = new double[]{0.948683298057675, 0.0, 1.0099999999999997};
        Assert.assertArrayEquals(expected, result, 1e-15);

        // Test solve(RealVectorImpl)
        RealVectorImpl bVector = new RealVectorImpl(b, false);
        RealVector resultVector = solver.solve(bVector);
        RealVector expectedVector = new RealVectorImpl(expected, false);
        Assert.assertEquals(expectedVector, resultVector);

        // Test solve(RealMatrix)
        RealMatrix bMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1, 2},
                {3, 4}
        });
        RealMatrix resultMatrix = solver.solve(bMatrix);
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {0.948683298057675, -0.253592321252628},
                {1.0099999999999997, 0.812315475258925}
        });
        Assert.assertEquals(expectedMatrix, resultMatrix);

        // Test solve(RealMatrixImpl)
        RealMatrixImpl bMatrixImpl = new RealMatrixImpl(new double[][]{
                {1, 2},
                {3, 4}
        }, false);
        RealMatrix resultMatrixImpl = solver.solve(bMatrixImpl);
        Assert.assertEquals(expectedMatrix, resultMatrixImpl);
    }

}