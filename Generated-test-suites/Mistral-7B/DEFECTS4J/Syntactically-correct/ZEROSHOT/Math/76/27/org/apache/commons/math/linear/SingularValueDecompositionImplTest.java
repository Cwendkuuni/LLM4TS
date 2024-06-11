import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.4495, 0.7186, -0.4495},
                                                                         {0.5736, 0.3420, 0.5736},
                                                                         {-0.5736, 0.3420, -0.5736}});
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.4495, 0.5736, -0.5736},
                                                                        {0.7186, 0.3420, 0.3420},
                                                                        {-0.4495, 0.5736, -0.5736}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.0, 0.0, 0.0},
                                                                       {0.0, 7.0426, 0.0},
                                                                       {0.0, 0.0, 3.1623}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.1211, 0.9273, 0.3827},
                                                                       {-0.9273, -0.1211, 0.3827},
                                                                       {0.3827, 0.3827, 0.8165}});
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.1211, -0.9273, 0.3827},
                                                                        {0.9273, -0.1211, 0.3827},
                                                                        {0.3827, 0.3827, 0.8165}});

        RealMatrix u = svd.getU();
        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix vt = svd.getVT();

        assertEquals(expectedU, u);
        assertEquals(expectedUt, ut);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        assertEquals(expectedVt, vt);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionInvalidMatrix() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                           {4.0, 5.0},
                                                                           {7.0, 8.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);
    }

    @Test
    public void testSingularValueDecompositionIdentity() throws Exception {
        RealMatrix inputMatrix = new IdentityRealMatrix(3);

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.6708, 0.3333, 0.3333},
                                                                         {0.3333, 0.6708, 0.3333},
                                                                         {-0.3333, 0.3333, 0.6708}});
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.6708, 0.3333, -0.3333},
                                                                        {0.3333, 0.6708, 0.3333},
                                                                        {0.3333, 0.3333, 0.6708}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                       {0.0, 1.0, 0.0},
                                                                       {0.0, 0.0, 1.0}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.7071, 0.7071, 0.7071},
                                                                       {-0.7071, 0.7071, 0.7071},
                                                                       {0.7071, -0.7071, 0.7071}});
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{0.7071, -0.7071, 0.7071},
                                                                        {0.7071, 0.7071, 0.7071},
                                                                        {-0.7071, 0.7071, 0.7071}});

        RealMatrix u = svd.getU();
        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix vt = svd.getVT();

        assertEquals(expectedU, u);
        assertEquals(expectedUt, ut);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        assertEquals(expectedVt, vt);
    }

    @Test
    public void testSingularValueDecompositionDiagonal() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0},
                                                                           {0.0, 3.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 2);

        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.7071, -0.7071},
                                                                        {0.7071, 0.7071}});
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.7071, 0.7071},
                                                                         {-0.7071, 0.7071}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{2.7321, 0.0},
                                                                       {0.0, 3.0}});
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.7071, 0.7071},
                                                                       {-0.7071, 0.7071}});
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{0.7071, -0.7071},
                                                                        {0.7071, 0.7071}});

        RealMatrix u = svd.getU();
        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix vt = svd.getVT();

        assertEquals(expectedU, u);
        assertEquals(expectedUt, ut);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        assertEquals(expectedVt, vt);
    }

    @Test
    public void testGetSingularValues() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        double[] expectedSingularValues = new double[]{15.0, 7.0426, 3.1623};

        assertArrayEquals(expectedSingularValues, svd.getSingularValues().clone(), 0.001);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        double expectedConditionNumber = 15.0 / 3.1623;

        assertEquals(expectedConditionNumber, svd.getConditionNumber(), 0.001);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        int expectedRank = 3;

        assertEquals(expectedRank, svd.getRank());
    }

    @Test
    public void testCovariance() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        double minSingularValue = 5.0;

        RealMatrix covarianceMatrix = svd.getCovariance(minSingularValue);

        double[][] expectedCovarianceMatrixData = new double[][]{{0.0546, 0.0332, 0.0183},
                                                                 {0.0332, 0.1082, 0.0625},
                                                                 {0.0183, 0.0625, 0.1475}};
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(expectedCovarianceMatrixData, false);

        RealMatrix actualCovarianceMatrix = svd.getCovariance(minSingularValue);

        assertEquals(expectedCovarianceMatrix, actualCovarianceMatrix);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                           {4.0, 5.0, 6.0},
                                                                           {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(inputMatrix, 3);

        double expectedNorm = 15.0;

        assertEquals(expectedNorm, svd.getNorm(), 0.001);
    }

}