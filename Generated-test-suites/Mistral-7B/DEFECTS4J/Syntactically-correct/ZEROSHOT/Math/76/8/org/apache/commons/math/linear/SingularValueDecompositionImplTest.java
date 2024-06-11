import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = decomposition.getU();
        RealMatrix s = decomposition.getS();
        RealMatrix vt = decomposition.getVT();

        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.54488856, 0.73438555, -0.43301271},
                                                                         {-0.81428856, 0.45387298, 0.41510822},
                                                                         {-0.40824829, -0.49995644, -0.73438555}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{15.0, 7.93535206, 0.0});
        RealMatrix expectedVt = MatrixUtils.createRealMatrix(new double[][]{{0.40824829, -0.81428856, 0.54488856},
                                                                            {0.49995644, 0.45387298, 0.73438555},
                                                                            {-0.73438555, 0.41510822, 0.43301271}});

        Assert.assertEquals("U is not correct", expectedU, u);
        Assert.assertEquals("S is not correct", expectedS, s);
        Assert.assertEquals("VT is not correct", expectedVt, vt);

        double[] expectedSingularValues = {15.0, 7.93535206, 0.0};
        Assert.assertArrayEquals("Singular values are not correct", expectedSingularValues, decomposition.getSingularValues());
    }

    @Test(expected = InvalidMatrixException.class)
    public void testInvalidMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {4.0, 5.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                        {4.0, 5.0, 6.0},
                                                                        {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        Assert.assertEquals(3, decomposition.getRank());

        RealMatrix singularValueDecomposition = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                            {0.0, 1.0, 0.0},
                                                                                            {0.0, 0.0, 1.0}});
        decomposition = new SingularValueDecompositionImpl(singularValueDecomposition);
        Assert.assertEquals(3, decomposition.getRank());

        RealMatrix singularValueDecomposition2 = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                            {0.0, 0.0, 0.0},
                                                                                            {0.0, 0.0, 0.0}});
        decomposition = new SingularValueDecompositionImpl(singularValueDecomposition2);
        Assert.assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0},
                                                                        {2.0, 3.0, 4.0, 5.0},
                                                                        {3.0, 4.0, 5.0, 6.0},
                                                                        {4.0, 5.0, 6.0, 7.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = decomposition.getCovariance(1.0);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{6.0, 5.0},
                                                                                    {5.0, 6.0}});
        Assert.assertEquals("Covariance matrix is not correct", expectedCovariance, covariance);
    }

    @Test
    public void testNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                        {3.0, 4.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        Assert.assertEquals(Math.sqrt(15.0), decomposition.getNorm(), 1e-9);
    }

    @Test
    public void testConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                        {3.0, 4.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        Assert.assertEquals(Math.sqrt(15.0) / Math.sqrt(3.0), decomposition.getConditionNumber(), 1e-9);
    }

    @Test
    public void testSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                        {3.0, 4.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();

        double[] b = {1.0, 2.0};
        double[] x = solver.solve(b);
        Assert.assertArrayEquals("Solution is not correct", new double[]{1.16708607, -0.33333335}, x);

        RealVector rvB = MatrixUtils.createRealVector(b);
        RealVector rvX = solver.solve(rvB);
        Assert.assertEquals("Solution is not correct", 1.16708607, rvX.getEntry(0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.33333335, rvX.getEntry(1), 1e-9);

        RealMatrix bMatrix = MatrixUtils.createRealMatrix(b, 2, 1);
        RealMatrix xMatrix = solver.solve(bMatrix);
        Assert.assertEquals("Solution is not correct", 1.16708607, xMatrix.getEntry(0, 0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.33333335, xMatrix.getEntry(1, 0), 1e-9);

        Assert.assertTrue(solver.isNonSingular());

        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                           {3.0, 0.0}});
        decomposition = new SingularValueDecompositionImpl(matrix);
        solver = decomposition.getSolver();

        b = new double[]{1.0, 2.0};
        x = solver.solve(b);
        Assert.assertArrayEquals("Solution is not correct", new double[]{1.15470054, -0.76159766}, x);

        rvB = MatrixUtils.createRealVector(b);
        rvX = solver.solve(rvB);
        Assert.assertEquals("Solution is not correct", 1.15470054, rvX.getEntry(0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.76159766, rvX.getEntry(1), 1e-9);

        bMatrix = MatrixUtils.createRealMatrix(b, 2, 1);
        xMatrix = solver.solve(bMatrix);
        Assert.assertEquals("Solution is not correct", 1.15470054, xMatrix.getEntry(0, 0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.76159766, xMatrix.getEntry(1, 0), 1e-9);

        Assert.assertFalse(solver.isNonSingular());

        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                           {4.0, 5.0, 6.0},
                                                           {7.0, 8.0, 9.0}});
        decomposition = new SingularValueDecompositionImpl(matrix);
        solver = decomposition.getSolver();

        b = new double[]{1.0, 2.0, 3.0};
        x = solver.solve(b);
        Assert.assertArrayEquals("Solution is not correct", new double[]{0.62842051, -0.23728529, -0.56116311}, x);

        rvB = MatrixUtils.createRealVector(b);
        rvX = solver.solve(rvB);
        Assert.assertEquals("Solution is not correct", 0.62842051, rvX.getEntry(0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.23728529, rvX.getEntry(1), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.56116311, rvX.getEntry(2), 1e-9);

        bMatrix = MatrixUtils.createRealMatrix(b, 3, 1);
        xMatrix = solver.solve(bMatrix);
        Assert.assertEquals("Solution is not correct", 0.62842051, xMatrix.getEntry(0, 0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.23728529, xMatrix.getEntry(1, 0), 1e-9);
        Assert.assertEquals("Solution is not correct", -0.56116311, xMatrix.getEntry(2, 0), 1e-9);

        Assert.assertTrue(solver.isNonSingular());
    }

}