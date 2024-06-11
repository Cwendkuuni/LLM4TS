import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.exception.DecompositionFailedException;
import org.apache.commons.math3.exception.NotSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class CholeskyDecompositionImplTest {

    @Test
    public void testDefaultConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 2.0, 3.0},
                {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(choleskyDecomposition);
    }

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 2.0, 3.0},
                {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = null;
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NotSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            // expected
        }
        assertNull(choleskyDecomposition);

        matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 4.0}});
        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(lt.getRowDimension(), l.getColumnDimension());
        assertEquals(lt.getColumnDimension(), l.getRowDimension());
        assertArrayEquals(new double[][]{
                {2.0 / Math.sqrt(4.0), 0.0, 0.0},
                {-1.0 / Math.sqrt(4.0), 1.0 / Math.sqrt(2.0), 0.0},
                {-1.0 / Math.sqrt(4.0), 0.0, 1.0 / Math.sqrt(2.0)}},
                l.getData(), 0.0);

        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{2.0 / Math.sqrt(4.0), -1.0 / Math.sqrt(4.0),
                -1.0 / Math.sqrt(4.0)},
                {0.0, 1.0 / Math.sqrt(2.0), 0.0},
                {0.0, 0.0, 1.0 / Math.sqrt(2.0)}});
        assertArrayEquals(expectedLT.getData(), lt.getData(), 0.0);
    }

    @Test
    public void testConstructorWithMatrixAndThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0e-10, 2.0}, {2.0, 2.0, 3.0},
                {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 1.0e-5, 1.0e-15);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();
        assertEquals(lt.getRowDimension(), l.getColumnDimension());
        assertEquals(lt.getColumnDimension(), l.getRowDimension());
        assertArrayEquals(new double[][]{
                {Math.sqrt(1.0), 0.0, 0.0},
                {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(1.0), 0.0},
                {-1.0 / Math.sqrt(2.0), 0.0, 1.0 / Math.sqrt(2.0)}},
                l.getData(), 0.0);

        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(1.0), -1.0 / Math.sqrt(2.0),
                -1.0 / Math.sqrt(2.0)},
                {0.0, 1.0 / Math.sqrt(1.0), 0.0},
                {0.0, 0.0, 1.0 / Math.sqrt(2.0)}});
        assertArrayEquals(expectedLT.getData(), lt.getData(), 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, -1.0, 2.0},
                {2.0, 2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0},
                {2.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testConstructorWithNotSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}, {3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 2.0, 3.0},
                {2.0, 3.0, 4.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(Math.pow(1.0, 3) * 2.0 * 2.0, choleskyDecomposition.getDeterminant(), 1.0e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        choleskyDecomposition.getSolver().solve(null);
    }

    @Test
    public void testSolveWithRealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector x = new RealVectorImpl(new double[]{1.0, 1.0}, false);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0}, false);
        RealVector y = choleskyDecomposition.getSolver().solve(b);
        RealVector expectedY = new RealVectorImpl(new double[]{1.0, 0.5}, false);
        assertArrayEquals(expectedY.getData(), y.getData(), 1.0e-14);
        RealVector xSolution = choleskyDecomposition.getSolver().solve(x);
        assertEquals(1.0, xSolution.getEntry(0), 1.0e-14);
        assertEquals(0.5, xSolution.getEntry(1), 1.0e-14);
    }

    @Test
    public void testSolveWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.5}});
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 / 2.0}, {-1.5 / 3.0, 1.0}});
        assertArrayEquals(expectedX.getData(), x.getData(), 1.0e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        choleskyDecomposition.getSolver().solve(null);
    }

    @Test
    public void testGetInverse() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][]{{3.0 / 2.0, -1.0 / 2.0},
                {-1.0 / 2.0, 1.0 / 2.0}});
        assertArrayEquals(expectedInverse.getData(), inverse.getData(), 1.0e-14);
    }

}