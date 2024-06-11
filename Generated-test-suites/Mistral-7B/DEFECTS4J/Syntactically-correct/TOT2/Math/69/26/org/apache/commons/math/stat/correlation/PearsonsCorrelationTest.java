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
    public void testDataConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 0.86602540378444}, {0.86602540378444, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertTrue(actual.equals(expected, 0.001));
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 0.86602540378444}, {0.86602540378444, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertTrue(actual.equals(expected, 0.001));
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{1.0, 0.86602540378444}, {0.86602540378444, 1.0}});
        RealMatrix actual = pc.getCorrelationMatrix();
        assertTrue(actual.equals(expected, 0.001));
        assertEquals(3, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataConstructorWithInvalidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {5.0, 5.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatrixConstructorWithInvalidData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {5.0, 5.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{0.447213602103916, 0.447213602103916},
                {0.447213602103916, 0.447213602103916}});
        RealMatrix actual = pc.getCorrelationStandardErrors();
        assertTrue(actual.equals(expected, 0.001));
        assertEquals(3, pc.nObs);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {{0.999999999999996, 0.999999999999996},
                {0.999999999999996, 0.999999999999996}});
        RealMatrix actual = pc.getCorrelationPValues();
        assertTrue(actual.equals(expected, 0.001));
        assertEquals(3, pc.nObs);
    }

}