import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.SingularValueDecompositionSolver;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.linear.EigenDecomposition.RealEigenvalue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SingularValueDecompositionImpl singularValueDecomposition = null;
        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            e.printStackTrace();
        }
        RealMatrix u = singularValueDecomposition.getU();
        RealMatrix s = singularValueDecomposition.getS();
        RealMatrix v = singularValueDecomposition.getV();
        assertNotNull(u);
        assertNotNull(s);
        assertNotNull(v);
        assertEquals(3, u.getRowDimension());
        assertEquals(3, u.getColumnDimension());
        assertEquals(3, s.getRowDimension());
        assertEquals(3, s.getColumnDimension());
        assertEquals(3, v.getRowDimension());
        assertEquals(3, v.getColumnDimension());
    }

    @Test
    public void testSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SingularValueDecompositionImpl singularValueDecomposition = null;
        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            e.printStackTrace();
        }
        double[] singularValues = singularValueDecomposition.getSingularValues();
        assertEquals(3, singularValues.length);
        assertEquals(5.984332613596825, singularValues[0], 0.00001);
        assertEquals(1.414213741285434, singularValues[1], 0.00001);
        assertEquals(0.0, singularValues[2], 0.00001);
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SingularValueDecompositionImpl singularValueDecomposition = null;
        try {
            singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            e.printStackTrace();
        }
        SingularValueDecompositionSolver solver = singularValueDecomposition.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1, 2, 3});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[]{1.161734444134226, 1.331437845485959, 0.7796066008661966});
        assertEquals(expected, x, 0.00001);
        RealMatrix B = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}});
        RealMatrix X = solver.solve(B);
        RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{-0.025208469412096, 0.631794154795845, -0.7987184837474025}, {0.623201350635831, 0.6186648571713356, -0.195736512105385}, {0.737994728610233, -0.622092413098554, 0.358764875260366}});
        assertEquals(expectedX, X);
    }
}