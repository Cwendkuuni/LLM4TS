import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.special.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws SingularValueDecompositionException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.845652839, 0.538460493},
                {-0.538460493, 0.845652839}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{
                {10.62228234, 0.0}, {0.0, 3.16227766}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.45784616, 0.89442719},
                {0.89442719, -0.45784616}});
        RealMatrix expectedUT = expectedU.transpose();
        RealMatrix expectedVT = expectedV.transpose();
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        assertEquals(expectedUT, svd.getUT());
        assertEquals(expectedVT, svd.getVT());

        double[] singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{Math.sqrt(11.0), Math.sqrt(3.0)}, singularValues, 0.0001);

        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{-0.05794956, -0.01938179},
                {-0.01938179, 0.05794956}});
        RealMatrix covariance = svd.getCovariance(1e-12);
        assertEquals(expectedCovariance, covariance);

        assertEquals(Math.sqrt(11.0), svd.getNorm(), 0.0001);
        assertEquals(Math.sqrt(11.0) / Math.sqrt(3.0), svd.getConditionNumber(), 0.0001);

        assertEquals(matrix.getRank(), svd.getRank());
        Solver solver = svd.getSolver();
        assertArrayEquals(new double[]{0.62315833, 0.37684167}, solver.solve(new double[]{3.0, 2.0}), 0.0001);
        RealVector vector = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector expectedVector = new ArrayRealVector(new double[]{0.75262258, -0.62315833});
        assertEquals(expectedVector, solver.solve(vector));

        double[] data = new double[2][2];
        data[0][0] = 1.0;
        data[0][1] = 1.0;
        data[1][0] = 2.0;
        data[1][1] = 2.0;
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data, false);
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(inputMatrix, 2);
        assertArrayEquals(new double[]{Math.sqrt(5.0), Math.sqrt(5.0)}, svd2.getSingularValues(), 0.0001);
    }

    @Test(expected = SingularValueDecompositionException.class)
    public void testSingularValueDecompositionException() throws SingularValueDecompositionException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, -1);
    }

}