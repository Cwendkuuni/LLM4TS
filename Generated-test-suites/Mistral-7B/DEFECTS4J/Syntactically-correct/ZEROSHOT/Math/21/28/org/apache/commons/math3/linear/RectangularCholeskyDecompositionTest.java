import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SolverType;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixRankException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(2, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
    }

    @Test
    public void testSmallMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5}, {0.5, 1.1}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.1);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(2, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.0}, {0.0, 2.2}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(2, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.3063653858264},
                    {0.0, 1.0, 0.8164965809277}, {0.0, 0.0, 1.532522212205}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(3, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSymmetricPositiveSemidefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 1.5, 1.0}, {1.0, 1.0, 1.5}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.3063653858264},
                    {0.0, 1.0, 0.8164965809277}, {0.0, 0.0, 1.0}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(3, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSymmetricNegativeDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 1.5, 1.0}, {1.0, 1.0, 0.5}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
    }

    @Test
    public void testSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5, 0.5}, {0.5, 1.1, 0.5}, {0.5, 0.5, 1.1}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(3, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5, 0.5}, {0.5, 1.1, 0.5}, {0.5, 0.5, 1.1}, {0.5, 0.5, 1.1}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // pass
        }
    }

    @Test
    public void testRankDeficientMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5, 0.5}, {0.5, 1.1, 0.5}, {0.5, 0.5, 0.5}});
        try {
            RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.0, 0.0, 0.5}});
            RealMatrix result = decomposer.getRootMatrix();
            assertEquals(expectedResult, result);
            assertEquals(3, decomposer.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSingularValueDecompositionComparison() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5, 0.5}, {0.5, 1.1, 0.5}, {0.5, 0.5, 0.5}});
        try {
            RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            RealMatrix rootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
            SingularValueDecomposition singularValueDecomposition = new SingularValueDecomposition(matrix, SolverType.COL_ORTHOGONAL);
            RealMatrix singularValueMatrix = singularValueDecomposition.getV();
            RealMatrix expectedResult = new Array2DRowRealMatrix(singularValueMatrix.getData());
            RealMatrix result = rootMatrix;
            for (int i = 0; i < result.getRowDimension(); i++) {
                RealVector expectedRow = expectedResult.getRowVector(i);
                RealVector resultRow = result.getRowVector(i);
                for (int j = 0; j < resultRow.getDimension(); j++) {
                    assertEquals(expectedRow.getEntry(j), resultRow.getEntry(j), 1e-10);
                }
            }
            assertEquals(3, rectangularCholeskyDecomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException | MatrixRankException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testEigenvalueDecompositionComparison() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.5, 0.5}, {0.5, 1.1, 0.5}, {0.5, 0.5, 0.5}});
        try {
            RealMatrix rootMatrix = new RectangularCholeskyDecomposition(matrix, 0.0).getRootMatrix();
            EigenDecomposition eigenDecomposition = new EigenDecomposition(matrix);
            RealMatrix eigenvectors = eigenDecomposition.getV();
            for (int i = 0; i < eigenvectors.getRowDimension(); i++) {
                RealVector expectedRow = eigenvectors.getRowVector(i);
                RealVector resultRow = rootMatrix.getColumnVector(i);
                for (int j = 0; j < resultRow.getDimension(); j++) {
                    assertEquals(expectedRow.getEntry(j), resultRow.getEntry(j), 1e-10);
                }
            }
            assertEquals(3, new RectangularCholeskyDecomposition(matrix, 0.0).getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception");
        }
    }

}