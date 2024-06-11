package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.MathUtils;
import org.apache.commons.math3.stat.decomposition.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getCovariance(0.0);
        RealMatrix v = svd.getV();
        SingularValueDecomposition svdReference = new SingularValueDecomposition(matrix);
        RealMatrix uReference = svdReference.getMatrixU();
        RealMatrix sReference = svdReference.getSingularValuesMatrix();
        RealMatrix vReference = svdReference.getMatrixV();

        assertEquals(uReference, u);
        assertArrayEquals(sReference.getColumnVector(0).toArray(), s.getColumnVector(0).toArray());
        assertArrayEquals(sReference.getColumnVector(1).toArray(), s.getColumnVector(1).toArray());
        assertEquals(vReference, v);

        RealVector x = new ArrayRealVector(new double[]{1, 1});
        RealVector y = u.multiply(s.multiply(v.operate(x)));
        RealVector xRef = svdReference.solve(x);
        assertEquals(xRef, y);

        RealVector xInvalid = new ArrayRealVector(new double[]{3, 3});
        try {
            svd.solve(xInvalid);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix x = svd.getSolver().solve(b);
        assertEquals(x, svdReference.getSolver().solve(b));
    }

    @Test
    public void testSingularValueDecompositionIdentity() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getCovariance(0.0);
        RealMatrix v = svd.getV();
        SingularValueDecomposition svdReference = new SingularValueDecomposition(matrix);
        RealMatrix uReference = svdReference.getMatrixU();
        RealMatrix sReference = svdReference.getSingularValuesMatrix();
        RealMatrix vReference = svdReference.getMatrixV();

        assertEquals(uReference, u);
        assertArrayEquals(sReference.getColumnVector(0).toArray(), new double[]{1, 1}, 0.001);
        assertArrayEquals(sReference.getColumnVector(1).toArray(), new double[]{0, 0}, 0.001);
        assertEquals(vReference, v);

        RealVector x = new ArrayRealVector(new double[]{1, 1});
        RealVector y = u.multiply(s.multiply(v.operate(x)));
        RealVector xRef = svdReference.solve(x);
        assertEquals(xRef, y);

        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix x = svd.getSolver().solve(b);
        assertEquals(x, svdReference.getSolver().solve(b));
    }

    @Test
    public void testSingularValueDecompositionZero() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getCovariance(0.0);
        RealMatrix v = svd.getV();
        SingularValueDecomposition svdReference = new SingularValueDecomposition(matrix);
        RealMatrix uReference = svdReference.getMatrixU();
        RealMatrix sReference = svdReference.getSingularValuesMatrix();
        RealMatrix vReference = svdReference.getMatrixV();

        assertEquals(uReference, u);
        assertArrayEquals(sReference.getColumnVector(0).toArray(), new double[]{0, 0}, 0.001);
        assertEquals(vReference, v);

        RealVector x = new ArrayRealVector(new double[]{1, 1});
        RealVector y = u.multiply(s.multiply(v.operate(x)));
        RealVector xRef = svdReference.solve(x);
        assertEquals(xRef, new ArrayRealVector(new double[]{0}));

        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix x = svd.getSolver().solve(b);
        assertEquals(x, new Array2DRowRealMatrix(new double[][]{{0}, {0}}));
    }

    @Test
    public void testSingularValueDecompositionInvalidMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 2}, {1, 2}});
        try {
            new SingularValueDecompositionImpl(matrix);
            fail();
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularValueDecompositionInvalidMax() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 1}});
        try {
            new SingularValueDecompositionImpl(matrix, 2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRankZero() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 0}});
        new SingularValueDecompositionImpl(matrix).getRank();
    }

    @Test
    public void testGetRankOne() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}});
        new SingularValueDecompositionImpl(matrix).getRank();
    }

    @Test
    public void testGetRankFull() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        new SingularValueDecompositionImpl(matrix).getRank();
    }

}