package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    /**
     * Tests the constructor and get methods.
     */
    @Test
    public void testConstructorAndGetMethods() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        assertEquals("Resulting L matrix should be the same asexpected", new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.0},
                {0.5, 1.0, 0.5},
                {0.0, 0.5, 1.0}
        }), l);
        assertEquals("Resulting LT matrix should be the same asexpected", new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        }), lT);
    }

    /**
     * Tests the constructor with an invalid matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Tests the constructor with a non-square matrix.
     */
    @Test(expected = NotSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Tests the constructor with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Tests the getDeterminant method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        double determinant = choleskyDecomposition.getDeterminant();
        RealVector solution = solver.solve(new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0}
        }));
        assertEquals("The determinant should be equal to the product of the diagonal elements of the solution", 1.0 * 3.0, determinant, 1e-14);
    }

    /**
     * Tests the solver.
     */
    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0},
                {1.0}
        }).transpose();
        RealVector solution = solver.solve(b);
        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{
                {0.5},
                {0.0},
                {-0.5}
        });
        assertEquals("The solution should be equal to expected solution", expectedSolution, solution);
    }

    /**
     * Tests the solver with an invalid matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testSolverWithNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -1.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0},
                {1.0}
        }).transpose();
        solver.solve(b);
    }

    /**
     * Tests the solver with a non-square matrix.
     */
    @Test(expected = NotSquareMatrixException.class)
    public void testSolverWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0},
                {1.0, 1.0}
        }).transpose();
        solver.solve(b);
    }

    /**
     * Tests the solver with a non-symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testSolverWithNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.1}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0},
                {1.0}
        }).transpose();
        solver.solve(b);
    }

    /**
     * Tests the solver with a singular matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0},
                {1.0},
                {1.0}
        }).transpose();
        solver.solve(b);
    }

}