package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private double[][] data;
    private double[][] dataWithDuplicateColumns;
    private double[][] dataWithInvalidColumns;
    private double[][] dataWithZeroRows;
    private double[][] dataWithZeroColumns;
    private PearsonsCorrelation pc;

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
        pc = new PearsonsCorrelation(data);

        dataWithDuplicateColumns = new double[][]{{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 3.2, 5.2}};
        dataWithInvalidColumns = new double[][]{{1.0, "invalid"}, {2.1, 3.1}, {3.2, 4.2}};
        dataWithZeroRows = new double[][]{{1.0, 2.0}, {}, {3.2, 4.2}};
        dataWithZeroColumns = new double[][]{{1.0, 2.0}, {2.1}, {3.2, 4.2}};
    }

    @Test
    public void testCorrelationMatrixWithValidData() {
        RealMatrix expected = new Array2DRowRealMatrix(new RealArray2D(3, 3,
                3.0, 0.81649658, 0.81649658, 0.81649658,
                0.81649658, 1.0, 0.81649658,
                0.81649658, 0.81649658, 1.0));

        RealMatrix result = pc.getCorrelationMatrix();

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationMatrixWithInvalidData() {
        new PearsonsCorrelation(dataWithInvalidColumns);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationMatrixWithZeroRows() {
        new PearsonsCorrelation(dataWithZeroRows);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationMatrixWithZeroColumns() {
        new PearsonsCorrelation(dataWithZeroColumns);
    }

    @Test
    public void testCorrelationMatrixWithDuplicateColumns() {
        PearsonsCorrelation pcDuplicate = null;
        try {
            pcDuplicate = new PearsonsCorrelation(dataWithDuplicateColumns);
        } catch (IllegalArgumentException e) {
            // expected
        }

        Assert.assertNull(pcDuplicate);
    }

    @Test
    public void testCorrelation_ValidData() {
        double[] xArray = {1.0, 2.1, 3.2};
        double[] yArray = {2.0, 3.1, 4.2};
        double correlation = pc.correlation(xArray, yArray);

        SimpleRegression regression = new SimpleRegression();
        regression.addData(xArray, yArray);
        double correlationFromRegression = regression.getR();

        Assert.assertEquals(correlationFromRegression, correlation, 0.001);
    }
}