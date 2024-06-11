package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.eye;
import static org.apache.commons.math3.linear.MatrixUtils.randomRealMatrix;

/**
 * Test cases for {@link SchurTransformer}.
 */
public class SchurTransformerTest
{
    /**
     * Test transformation of a diagonal matrix.
     */
    @Test
    public void testDiagonalMatrix()
    {
        final double[][] input = { { 1.0, 0.0 }, { 0.0, 1.0 } };
        final SchurTransformer transformer = new SchurTransformer(createRealMatrix(input));
        final RealMatrix p = transformer.getP();
        final RealMatrix t = transformer.getT();
        Assert.assertEquals(input, p.transpose().multiply(t));
    }

    /**
     * Test transformation of a random matrix.
     */
    @Test
    public void testRandomMatrix()
    {
        final int size = 5;
        final double[][] input = randomRealMatrix(size, size).getData();
        final SchurTransformer transformer = new SchurTransformer(createRealMatrix(input));
        final RealMatrix p = transformer.getP();
        final RealMatrix t = transformer.getT();
        Assert.assertTrue(p.multiply(t).isIdentityMatrix(0.01));
    }

    /**
     * Test transformation of a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix()
    {
        final double[][] input = { { 1.0, 0.0 }, { 1.0, 1.0 } };
        new SchurTransformer(createRealMatrix(input));
    }
}