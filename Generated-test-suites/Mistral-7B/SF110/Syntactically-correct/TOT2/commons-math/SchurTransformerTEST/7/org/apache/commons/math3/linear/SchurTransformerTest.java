package org.example.junit;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}}));
        RealMatrix p1 = transformer.getP();
        RealMatrix p2 = transformer.getP();

        // Check that p1 and p2 are the same instance
        assertSame(p1, p2);

        // Call getP() multiple times and check that the returned instances are the same
        RealMatrix p3 = transformer.getP();
        assertSame(p1, p3);
    }

    @Test
    public void testGetT() {
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3}}));
        RealMatrix t1 = transformer.getT();
        RealMatrix t2 = transformer.getT();

        // Check that t1 and t2 are the same instance
        assertSame(t1, t2);

        // Call getT() multiple times and check that the returned instances are the same
        RealMatrix t3 = transformer.getT();
        assertSame(t1, t3);
    }

    @Test
    public void testNonSquareMatrixException() {
        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2}}));
            fail();
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testEmptyMatrixException() {
        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{}));
            fail();
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{-1, 2}, {2, -1}}));
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testSchurTransformer() {
        // test with identity matrix
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(2);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        RealMatrix pt = transformer.getPT();

        // check that P and PT are inverses of each other
        RealMatrix checkProduct = MatrixUtils.multiply(p, pt);
        assertTrue(checkProduct.isIdentityMatrix());

        // test with diagonal matrix
        matrix = MatrixUtils.createDiagonalMatrix(new double[]{1, 2});
        transformer = new SchurTransformer(matrix);
        p = transformer.getP();
        t = transformer.getT();
        pt = transformer.getPT();

        // check that P and PT are inverses of each other
        checkProduct = MatrixUtils.multiply(p, pt);
        assertTrue(checkProduct.isIdentityMatrix());

        // test with different sizes
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        transformer = new SchurTransformer(matrix);
        p = transformer.getP();
        t = transformer.getT();
        pt = transformer.getPT();

        // check that P and PT are inverses of each other
        checkProduct = MatrixUtils.multiply(p, pt);
        assertTrue(checkProduct.isIdentityMatrix());
    }
}