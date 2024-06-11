package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test
    public void testEmptyArrayConstructor() {
        double[][] emptyArray = {};
        PearsonsCorrelation pc = new PearsonsCorrelation(emptyArray);
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArrayConstructorThrowsException() {
        double[][] emptyArray = {};
        new PearsonsCorrelation(emptyArray);
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.95, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.95, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixConstructorThrowsException() {
        double[][] data = {{1.0D, 2.0D}, {2.0D, 3.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        RealMatrix covarianceMatrix = new Covariance(data).getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.95, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testRealMatrixToCorrelationMatrix() {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(0, 2), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.95, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.6, correlationMatrix.getEntry(2, 0), 0.001);
        assertEquals(0.7, correlationMatrix.getEntry(2, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix standardErrors = pc.getCorrelationStandardErrors();
        assertEquals(3, standardErrors.getRowDimension());
        assertEquals(3, standardErrors.getColumnDimension());
        assertEquals(0.449489743, standardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.408248290, standardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.458257819, standardErrors.getEntry(0, 2), 0.001);
        assertEquals(0.408248290, standardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.425564242, standardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.425564242, standardErrors.getEntry(1, 2), 0.001);
        assertEquals(0.449489743, standardErrors.getEntry(2, 0), 0.001);
        assertEquals(0.425564242, standardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.449489743, standardErrors.getEntry(2, 2), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValuesThrowsException() throws MathException {
        double[][] data = {{1.0D, 2.0D}, {2.0D, 3.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        pc.getCorrelationPValues();
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0D, 2.0D, 3.0D}, {2.0D, 3.0D, 4.0D}, {3.0D, 4.0D, 5.0D}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix pValues = pc.getCorrelationPValues();
        assertEquals(3, pValues.getRowDimension());
        assertEquals(3, pValues.getColumnDimension());
        assertEquals(1.0, pValues.getEntry(0, 0), 0.001);
        assertEquals(1.0, pValues.getEntry(0, 1), 0.001);
        assertEquals(1.0, pValues.getEntry(0, 2), 0.001);
        assertEquals(1.0, pValues.getEntry(1, 0), 0.001);
        assertEquals(1.0, pValues.getEntry(1, 1), 0.001);
        assertEquals(1.0, pValues.getEntry(1, 2), 0.001);
        assertEquals(1.0, pValues.getEntry(2, 0), 0.001);
        assertEquals(1.0, pValues.getEntry(2, 1), 0.001);
        assertEquals(1.0, pValues.getEntry(2, 2), 0.001);
        // Off-diagonal elements should not be zero
        assertEquals(0.001173883, pValues.getEntry(0, 1), 0.001);
        assertEquals(0.001250195, pValues.getEntry(0, 2), 0.001);
        assertEquals(0.001250195, pValues.getEntry(1, 0), 0.001);
        assertEquals(0.001343429, pValues.getEntry(1, 2), 0.001);
        assertEquals(0.001343429, pValues.getEntry(2, 1), 0.001);
        assertEquals(0.001173883, pValues.getEntry(2, 0), 0.001);
    }

}