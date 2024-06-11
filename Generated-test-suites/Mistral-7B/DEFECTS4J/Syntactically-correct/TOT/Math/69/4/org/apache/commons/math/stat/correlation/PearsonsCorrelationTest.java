package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.StatisticsFacts;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            correlation.computeCorrelationMatrix(emptyMatrix);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix identity = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(identity);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        Assert.assertEquals("Correlation matrix not equal to identity", expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix correlationMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.741, 0.816},
                {0.741, 1.0, 0.923},
                {0.816, 0.923, 1.0}
        });
        Assert.assertEquals("Correlation matrix not equal to expected", expectedCorrelationMatrix, correlation.getCorrelationMatrix());
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix correlationMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{
                {0.5, 0.432, 0.432},
                {0.432, 0.432, 0.339},
                {0.432, 0.339, 0.282}
        });
        Assert.assertEquals("Correlation standard errors not equal to expected", expectedCorrelationStandardErrors, correlation.getCorrelationStandardErrors());
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix correlationMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{
                {0.999, 0.734, 0.999},
                {0.734, 0.006, 0.001},
                {0.999, 0.001, 0.001}
        });
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        Assert.assertEquals("Correlation p-values not equal to expected", expectedCorrelationPValues, correlationPValues, StatisticsFacts.EPSILON);
    }

    @Test
    public void testNullInput() {
        RealMatrix nullMatrix = null;
        try {
            new PearsonsCorrelation(nullMatrix);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testScalarInput() {
        double[][] data = {{1.0, 1.0}, {1.0, 1.0}};
        RealMatrix correlationMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(correlationMatrix);
        double correlationCoefficient = correlation.correlation(new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        SimpleRegression regression = new SimpleRegression();
        regression.addData(1.0, 1.0);
        regression.addData(1.0, 1.0);
        double expectedCorrelationCoefficient = regression.getR();
        Assert.assertEquals("Correlation coefficient not equal to expected", expectedCorrelationCoefficient, correlationCoefficient, 1e-6);
    }

    @Test
    public void testEmptyInput() {
        double[] xArray = {};
        double[] yArray = {};
        try {
            new PearsonsCorrelation().correlation(xArray, yArray);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNonRectangularInput() {
        double[][] data = {{1.0, 2.0}, {1.0, 2.0, 3.0}};
        try {
            new PearsonsCorrelation().computeCorrelationMatrix(new BlockRealMatrix(data));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNonNumericInput() {
        double[][] data = {{1.0, Complex.ONE}, {2.0, "not a number"}};
        try {
            new PearsonsCorrelation().computeCorrelationMatrix(new BlockRealMatrix(data));
            Assert.fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // expected
        }
    }
}