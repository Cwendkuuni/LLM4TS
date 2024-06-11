import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] data = {
            {2.0, 1.0, 0.0},
            {1.0, 2.0, 1.0},
            {0.0, 1.0, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        assertEquals(l.getRowDimension(), l.getColumnDimension(), 3);
        assertEquals(lt.getRowDimension(), lt.getColumnDimension(), 3);

        assertNotNull(l);
        assertNotNull(lt);

        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.5464466026435768, 0.8164965809277261, 0.0},
                {0.0, 0.4330127018922193, 1.7320508075688772}}, false);

        assertEquals(expectedL, l);

        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5464466026435768, 0.0},
                {0.5464466026435768, 0.8164965809277261, 0.4330127018922193},
                {0.0, 0.4330127018922193, 1.7320508075688772}}, false);

        assertEquals(expectedLT, lt);

        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();

        RealVector b = new Array2DRowRealVector(new double[][]{{1.0}, {2.0}, {3.0}});

        RealVector x = choleskyDecomposition.getSolver().solve(b);

        RealVector expectedX = new Array2DRowRealVector(new double[]{1.389540337359171, -0.27979742833121626, 0.4532438640135313});

        assertEquals(expectedX, x);

        RealVector y = new Array2DRowRealVector(new double[][]{{1.0, 0.0}, {-0.27979742833121626, 1.0}, {0.4532438640135313, 1.5469581590707408}});

        RealVector ySolved = choleskyDecomposition.getSolver().solve(y);

        RealVector expectedYSolved = new Array2DRowRealVector(new double[]{1.0, -0.4330127018922193, 0.7343935772549395});

        assertEquals(expectedYSolved, ySolved);

        RealMatrix svd = new SingularValueDecomposition(matrix);
        RealMatrix expectedInv = svd.getU().transpose().multiply(svd.getW().transpose().multiply(svd.getVT()));

        assertEquals(expectedInv, inverse);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testInvalidVectorSize() {
        double[][] data = {
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 3.0},
                {1.0, 2.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = org.apache.commons.math3.linear.NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] data = {
                {2.0, 1.0, 0.0},
                {1.0, -1.0, 1.0},
                {0.0, 1.0, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new CholeskyDecompositionImpl(matrix);
        } catch (NotPositiveDefiniteMatrixException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = org.apache.commons.math3.linear.NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] data = {
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 3.0},
                {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new CholeskyDecompositionImpl(matrix);
        } catch (NotSymmetricMatrixException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = {
                {2.0},
                {1.0, 2.0},
                {0.0, 1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data, false);

        try {
            new CholeskyDecompositionImpl(matrix);
        } catch (NonSquareMatrixException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

}