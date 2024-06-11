import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        RealMatrix us = u.transpose();
        RealMatrix ut = u.transpose();

        RealMatrix expectedUs = new Array2DRowRealMatrix(new double[][]{{0.54184209, -0.81432623, 0.13129595},
                                                                        {-0.81432623, 0.54184209, -0.13129595},
                                                                        {0.13129595, -0.13129595, 0.9486837}});
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.54184209, -0.81432623, 0.13129595},
                                                                        {-0.81432623, 0.54184209, -0.13129595},
                                                                        {-0.13129595, -0.13129595, 0.9486837}});

        Assert.assertEquals(expectedUs, us);
        Assert.assertEquals(expectedUt, ut);

        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.73142592, 0.0, 0.0},
                                                                         {0.0, 8.31453236, 0.0},
                                                                         {0.0, 0.0, 1.11022302}});
        Assert.assertEquals(expectedS, s);

        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.05574877, 0.81649658, 0.57358197},
                                                                        {-0.81649658, -0.57358197, 0.05574877},
                                                                        {0.57358197, 0.05574877, -0.81649658}});
        Assert.assertEquals(expectedV, v);

        // Test getSolver method
        Solver solver = svd.getSolver();
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}});
        RealVector solution = solver.solve(b);
        Assert.assertEquals(new double[]{0.56418055, 0.73439075}, solution.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(3.104374867736707E8, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        Assert.assertEquals(3, rank);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{1.25992105, 0.63245552},
                                                                                {0.63245552, 1.25992105}});
        Assert.assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        Assert.assertEquals(11.817133627401486, norm, 0.001);
    }

}