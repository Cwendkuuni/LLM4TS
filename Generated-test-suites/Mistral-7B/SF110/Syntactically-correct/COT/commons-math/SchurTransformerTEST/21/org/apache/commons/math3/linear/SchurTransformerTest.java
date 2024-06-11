package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.PrecisionUtils.DoubleArray;
import org.junit.Before;
import org.junit.Test;

public class SchurTransformerTest
{
    private RealMatrix inputMatrix;
    private SchurTransformer schurTransformer;

    @Before
    public void setUp()
    {
        double[][] inputData = { { 1.0, 2.0, 3.0 }, { 2.0, 1.0, 1.0 }, { 3.0, 1.0, 1.0 } };
        this.inputMatrix = new Array2DRowRealMatrix(inputData);
    }

    @Test
    public void testSchurTransformerConstructor()
    {
        try {
            schurTransformer = new SchurTransformer(inputMatrix);
            assertNotNull(schurTransformer.getP());
            assertNotNull(schurTransformer.getPT());
            assertNotNull(schurTransformer.getT());
        }
        catch (NonSquareMatrixException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test
    public void testGetP()
    {
        schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = schurTransformer.getP();
        assertEquals(inputMatrix.getRowDimension(), pMatrix.getRowDimension());
        assertEquals(inputMatrix.getColumnDimension(), pMatrix.getColumnDimension());
        assertEquals(inputMatrix.getEntry(0, 0), pMatrix.getEntry(0, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(0, 1), pMatrix.getEntry(0, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(0, 2), pMatrix.getEntry(0, 2), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 0), pMatrix.getEntry(1, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 1), pMatrix.getEntry(1, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 2), pMatrix.getEntry(1, 2), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 0), pMatrix.getEntry(2, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 1), pMatrix.getEntry(2, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 2), pMatrix.getEntry(2, 2), 1e-14);
    }

    @Test
    public void testGetPT()
    {
        schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix ptMatrix = schurTransformer.getPT();
        assertEquals(inputMatrix.getRowDimension(), ptMatrix.getRowDimension());
        assertEquals(inputMatrix.getColumnDimension(), ptMatrix.getColumnDimension());
        assertEquals(inputMatrix.getEntry(0, 0), ptMatrix.getEntry(0, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(0, 1), ptMatrix.getEntry(1, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(0, 2), ptMatrix.getEntry(1, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 0), ptMatrix.getEntry(1, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 1), ptMatrix.getEntry(0, 0), 1e-14);
        assertEquals(inputMatrix.getEntry(1, 2), ptMatrix.getEntry(1, 2), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 0), ptMatrix.getEntry(2, 1), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 1), ptMatrix.getEntry(2, 2), 1e-14);
        assertEquals(inputMatrix.getEntry(2, 2), ptMatrix.getEntry(0, 1), 1e-14);
    }

    @Test
    public void testGetT()
    {
        schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix tMatrix = schurTransformer.getT();
        assertEquals(inputMatrix.getRowDimension(), tMatrix.getRowDimension());
        assertEquals(inputMatrix.getColumnDimension(), tMatrix.getColumnDimension());
        DoubleArray tEntries = new DoubleArray(3 * 3);
        inputMatrix.getData(tEntries);
        tMatrix.getData(tEntries, 0, 0);
        assertArrayEquals(tEntries.toArray(), tMatrix.getData(0, 0).toArray(), 1e-14);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPAndTAfterTransformWithoutTransform()
    {
        schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();
    }

    @Test
    public void testTransform()
    {
        schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();

        // Check that transform method has been called
        assertNotNull(schurTransformer.getCachedP());
        assertNotNull(schurTransformer.getCachedT());
        assertNotNull(schurTransformer.getCachedPt());

        // Check that P and T matrices are equal to those obtained from the input matrix
        assertEquals(inputMatrix, schurTransformer.getCachedP().transpose().multiply(schurTransformer.getCachedT()));

        // Check that T matrix is upper triangular
        for (int i = 0; i < tMatrix.getRowDimension(); ++i) {
            for (int j = i + 1; j < tMatrix.getColumnDimension(); ++j) {
                assertEquals(0.0, tMatrix.getEntry(i, j), 1e-14);
            }
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWhenConvergenceFailed()
    {
        double[][] inputData = { { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputData);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
    }
}