import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test cases for CholeskyDecompositionImpl class
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test the default constructor with a valid square matrix
     */
    @org.junit.Test
    public void testDefaultConstructorWithValidSquareMatrix() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        Assert.assertEquals(matrix, lt.transpose());
    }

    /**
     * Test the constructor with a non-square matrix
     */
    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with a non-symmetric matrix
     */
    @org.junit.Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0},
                {3.0, 1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    /**
     * Test the constructor with a non-positive definite matrix
     */
    @org.junit.Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0},
                {2.0, -1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    /**
     * Test getDeterminant method
     */
    @org.junit.Test
    public void testGetDeterminant() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(determinant, 36.0, 0.0);
    }

    /**
     * Test getSolver method
     */
    @org.junit.Test
    public void testGetSolver() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1.0, 0.428571429, -0.214285714}, x, 0.0);

        RealVector rv = new RealVectorImpl(b);
        RealVector rx = solver.solve(rv);
        Assert.assertEquals(rx.getClass(), RealVectorImpl.class);
        Assert.assertArrayEquals(rx.getDataRef(), new double[]{1.0, 0.428571429, -0.214285714}, 0.0);
    }

    /**
     * Test getSolver with a singular matrix
     */
    @org.junit.Test(expected = SingularMatrixException.class)
    public void testGetSolverWithSingularMatrix() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 6.0},
                {3.0, 6.0, 1.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        double[] b = new double[]{1.0, 2.0, 3.0};
        solver.solve(b);
    }

    /**
     * Test solving a system of linear equations with a large matrix
     */
    @org.junit.Test
    public void testSolveLargeSystem() throws Exception {
        int size = 1000;
        double[][] matrixData = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrixData[i][j] = 1.0;
                } else if (i < j) {
                    matrixData[i][j] = 0.5;
                } else {
                    matrixData[i][j] = -0.5;
                }
            }
        }

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        double[] b = new double[size];
        for (int i = 0; i < size; i++) {
            b[i] = i;
        }

        double[] x = solver.solve(b);
        for (int i = 0; i < size; i++) {
            Assert.assertEquals(i + 1, x[i], 0.0);
        }
    }

    /**
     * Test solving a system of linear equations with a sparse matrix
     */
    @org.junit.Test
    public void testSolveSparseSystem() throws Exception {
        int size = 10;
        double[][] matrixData = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    matrixData[i][j] = 1.0;
                } else if (Math.abs(Math.random() - 0.5) < 0.1) {
                    matrixData[i][j] = Math.random();
                }
            }
        }

        RealMatrix matrix = MatrixUtils.createSparseRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        double[] b = new double[size];
        for (int i = 0; i < size; i++) {
            b[i] = i;
        }

        double[] x = solver.solve(b);
        for (int i = 0; i < size; i++) {
            Assert.assertEquals(i + 1, x[i], 0.0);
        }
    }

    /**
     * Test getInverse method
     */
    @org.junit.Test
    public void testGetInverse() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = choleskyDecomposition.getInverse();

        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
                {0.346596721, -0.428571429, 0.2734375},
                {-0.428571429, 0.483602871, -0.236067977},
                {0.2734375, -0.236067977, 0.433012701}
        });

        Assert.assertEquals(expected, inverse);
    }

    /**
     * Test private Solver class
     */
    @org.junit.Test
    public void testPrivateSolverClass() {
        double[][] matrixData = new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0}
        };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();

        Method method = CholeskyDecompositionImpl.class.getDeclaredMethod("solver");
        method.setAccessible(true);
        Object solverInstance = method.invoke(choleskyDecomposition);

        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector x = (RealVector) method.invoke(solverInstance, new Object[]{b});

        Assert.assertEquals(x.getClass(), RealVectorImpl.class);
        Assert.assertEquals(x.getDimension(), 3);
        Assert.assertArrayEquals(x.getDataRef(), new double[]{1.0, 0.428571429, -0.214285714}, 0.0);
    }

}