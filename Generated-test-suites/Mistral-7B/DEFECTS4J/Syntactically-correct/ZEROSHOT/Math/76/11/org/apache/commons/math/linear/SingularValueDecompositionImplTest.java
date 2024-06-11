import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = singularValueDecomposition.getU();
        RealMatrix s = singularValueDecomposition.getS();
        RealMatrix v = singularValueDecomposition.getV();
        RealMatrix ut = singularValueDecomposition.getUT();
        RealMatrix st = singularValueDecomposition.getS().transpose();
        RealMatrix vt = singularValueDecomposition.getVT();

        RealMatrix productUsStVt = u.multiply(s).multiply(st).multiply(vt);
        RealMatrix productMatrix = matrix.multiply(matrix.transpose());

        assertEquals(productMatrix, productUsStVt, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionInvalidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2},
            {4, 5},
            {7, 8}
        });
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = singularValueDecomposition.getSingularValues();

        assertArrayEquals(new double[]{15.19661313227331, 7.87487689182722, 0.0}, singularValues, 0.00001);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        int rank = singularValueDecomposition.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix covariance = singularValueDecomposition.getCovariance(1);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0/Math.sqrt(15.19661313227331), 1.0/Math.sqrt(15.19661313227331), 1.0/Math.sqrt(15.19661313227331)},
            {1.0/Math.sqrt(15.19661313227331), 1.0/Math.sqrt(15.19661313227331), 1.0/Math.sqrt(15.19661313227331)}
        });

        assertEquals(expected, covariance);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        double norm = singularValueDecomposition.getNorm();
        assertEquals(Math.sqrt(15.19661313227331), norm, 0.0001);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        double conditionNumber = singularValueDecomposition.getConditionNumber();
        assertEquals(Math.sqrt(15.19661313227331) / Math.sqrt(Math.sqrt(0.000000000001)*Math.sqrt(Math.sqrt(0.000000000001))), conditionNumber, 0.0001);
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        Solver solver = singularValueDecomposition.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector x = solver.solve(b);

        assertEquals(new ArrayRealVector(new double[]{1.0/3.0, 1.0/3.0, 1.0/3.0}), x);
    }

    @Test
    public void testIsNonSingular() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        SingularValueDecomposition singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        boolean nonSingular = singularValueDecomposition.getSolver().isNonSingular();
        assertTrue(nonSingular);
    }
}