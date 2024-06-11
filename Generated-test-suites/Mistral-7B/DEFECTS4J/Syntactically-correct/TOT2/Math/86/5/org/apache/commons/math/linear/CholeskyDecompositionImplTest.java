package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(5);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        DecompositionSolver solver = decomp.getSolver();

        RealVector b = new RealVectorImpl(new double[]{1, 1, 1, 1, 1}, false);
        RealVector x = solver.solve(b);
        RealVector expected = new RealVectorImpl(new double[]{1, 1, 1, 1, 1}, false);
        assertArrayEquals("Solution should be equal to identity", expected.toArray(), x.toArray(), 1e-14);

        RealMatrix lExpected = MatrixUtils.createRealIdentityMatrix(5);
        assertArrayEquals("L should be equal to identity", lExpected.getData(), l.getData(), 1e-14);
        assertArrayEquals("LT should be equal to transposed L", lExpected.transpose().getData(), lt.getData(), 1e-14);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testLargeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0},
            {2.0, 4.0, 6.0, 8.0, 10.0},
            {3.0, 6.0, 9.0, 12.0, 15.0},
            {4.0, 8.0, 12.0, 16.0, 20.0},
            {5.0, 10.0, 15.0, 20.0, 25.0}
        });

        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();
        DecompositionSolver solver = decomp.getSolver();

        RealVector b = new RealVectorImpl(new double[]{1, 2, 3, 4, 5}, false);
        RealVector x = solver.solve(b);
        RealVector expected = new RealVectorImpl(new double[]{4.5371693333525806, 2.6672818316353857, 0.6018254777883373, -1.1127649711529305, -2.168230236852183}, false);
        assertArrayEquals("Solution should match expected", expected.toArray(), x.toArray(), 1e-12);

        RealMatrix lExpected = new Array2DRowRealMatrix(new double[][]{
            {4.5371693333525806, 0.0, 0.0, 0.0, 0.0},
            {1.8142707700612914, 2.6672818316353857, 0.0, 0.0, 0.0},
            {0.4371844421233598, 0.9413297215335885, 2.6672818316353857, 0.0, 0.0},
            {-0.4480193738999561, -0.4123187438441987, 0.4813893969233183, 3.6672818316353857, 0.0},
            {-1.152112161283523, -1.3735324416399753, -0.9841314523295988, -1.3735324416399753, 4.5371693333525806}
        });
        assertArrayEquals("L should match expected", lExpected.getData(), l.getData(), 1e-12);

        RealMatrix ltExpected = new Array2DRowRealMatrix(new double[][]{
            {4.5371693333525806, 1.8142707700612914, 0.4371844421233598, -0.4480193738999561, -1.152112161283523},
            {0.0, 2.6672818316353857, 0.9413297215335885, 0.4123187438441987, 1.3735324416399753},
            {0.0, 0.0, 2.6672818316353857, 0.9413297215335885, 1.3735324416399753},
            {0.0, 0.0, 0.0, 3.6672818316353857, 1.3735324416399753},
            {0.0, 0.0, 0.0, 0.0, 4.5371693333525806}
        });
        assertArrayEquals("LT should match expected", ltExpected.getData(), lt.getData(), 1e-12);
    }

}