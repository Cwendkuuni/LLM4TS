package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

/**
 * Test class for PearsonsCorrelation.
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()}.
     */
    @Test
    public void testPearsonsCorrelationConstructorNullData() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.correlationMatrix);
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])}.
     */
    @Test
    public void testPearsonsCorrelationConstructorDoubleArray() {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.correlationMatrix.getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)}.
     */
    @Test
    public void testPearsonsCorrelationConstructorRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.correlationMatrix.getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(Covariance)}.
     */
    @Test
    public void testPearsonsCorrelationConstructorCovariance() {
        Covariance covariance = mock(Covariance.class);
        when(covariance.getCovarianceMatrix()).thenReturn(new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}}));
        when(covariance.getN()).thenReturn(3);

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.correlationMatrix.getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix, int)}.
     */
    @Test
    public void testPearsonsCorrelationConstructorRealMatrixInt() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertArrayEquals(expected.getData(), pearsonsCorrelation.correlationMatrix.getData(), 0.001);
        assertEquals(3, pearsonsCorrelation.nObs);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()}.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        assertEquals(expected, pearsonsCorrelation.correlationMatrix);
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()}.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.439311182748135, 0.311953242667243, 0.28785276149353},
                                                                     {0.311953242667243, 0.458192986536635, 0.37564292023449},
                                                                     {0.28785276149353, 0.37564292023449, 0.438762724487656}});
        assertEquals(expected, pearsonsCorrelation.getCorrelationStandardErrors());
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()}.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.307572968380577, 0.116917396437487, 0.04271321529642},
                                                                     {0.116917396437487, 0.307572968380577, 0.116917396437487},
                                                                     {0.04271321529642, 0.116917396437487, 0.307572968380577}});
        assertEquals(expected, pearsonsCorrelation.getCorrelationPValues());
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(RealMatrix)}.
     */
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        assertEquals(expected, PearsonsCorrelation.computeCorrelationMatrix(matrix));
    }

    /**
     * Test method for {@link PearsonsCorrelation#computeCorrelationMatrix(double[][])}.
     */
    @Test
    public void testComputeCorrelationMatrixDoubleArray() {
        double[][] data = new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8, 0.6}, {0.8, 1.0, 0.8}, {0.6, 0.8, 1.0}});
        assertEquals(expected, PearsonsCorrelation.computeCorrelationMatrix(data));
    }

    /**
     * Test method for {@link PearsonsCorrelation#correlation(double[], double[])}.
     */
    @Test
    public void testCorrelationDoubleArrayDoubleArray() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {1.5, 2.5, 3.5};
        double expected = 0.8;
        assertEquals(expected, PearsonsCorrelation.correlation(xArray, yArray), 0.001);
    }

    /**
     * Test method for {@link PearsonsCorrelation#covarianceToCorrelation(RealMatrix)}.
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expected, PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix));
    }
}