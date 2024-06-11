package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setUp() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix u = svd.getU();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.342020143461349, 0.615352791932558, 0.724767397592549},
                {-0.857561473226443, 0.165165482794324, 0.47184641679094},
                {-0.475390821297328, -0.815966934382028, 0.413148456814326}}, false);
        Assert.assertEquals(expected, u);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        double[] singularValues = svd.getSingularValues();
        double[] expected = {8.79418121459713, 5.42123505721934, 0.0};
        Assert.assertArrayEquals(expected, singularValues, 0.001);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix v = svd.getV();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.564189583548357, 0.258819890521383, 0.812155820030655},
                {-0.815086839236641, -0.408283396878742, -0.451332384585987},
                {-0.395934334733333, 0.812155820030655, 0.451332384585987}}, false);
        Assert.assertEquals(expected, v);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix ut = svd.getUT();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.342020143461349, 0.615352791932558, 0.724767397592549},
                {-0.857561473226443, 0.165165482794324, 0.47184641679094},
                {-0.475390821297328, -0.815966934382028, 0.413148456814326}}, false);
        Assert.assertEquals(expected, ut);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix s = svd.getS();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{8.79418121459713, 0.0, 0.0},
                {0.0, 5.42123505721934, 0.0},
                {0.0, 0.0, 0.0}}, false);
        Assert.assertEquals(expected, s);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        double minSingularValue = 1.0;
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        double[][] data = covariance.getData();
        double[][] expected = new double[][]{{0.039337842734813, 0.008103531438212, 0.01316305731923},
                {0.008103531438212, 0.03882643115756, 0.01316305731923},
                {0.01316305731923, 0.01316305731923, 0.01316305731923}};
        Assert.assertArrayEquals(expected, data, 0.001);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        double norm = svd.getNorm();
        double expected = 8.79418121459713;
        Assert.assertEquals(expected, norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        double conditionNumber = svd.getConditionNumber();
        double expected = 8.79418121459713 / 0.0;
        Assert.assertEquals(expected, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        int rank = svd.getRank();
        int expected = 3;
        Assert.assertEquals(expected, rank);
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{3, 4}, {5, 6}});
        DecompositionSolver solver = svd.getSolver();
        RealVector x = solver.solve(new ArrayRealVector(new double[]{3, 4}));
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.815966934382028, -0.408283396878742},
                {-0.451332384585987, 0.815966934382028}}, false);
        Assert.assertEquals(expected, svd.getV().multiply(x.toMatrix()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWrongSize() throws InvalidMatrixException {
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{3, 4}, {5, 6}, {7, 8}});
        DecompositionSolver solver = svd.getSolver();
        solver.solve(new ArrayRealVector(new double[]{3, 4}));
    }
}