import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        // Test empty constructor
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonsCorrelation.correlationMatrix);
        Assert.assertEquals(0, pearsonsCorrelation.nObs);

        // Test constructor with double array
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonsCorrelation1 = new PearsonsCorrelation(data);
        RealMatrix correlationMatrixExpected = new BlockRealMatrix(new double[][]{{1.0, 0.819152, 0.819152}, {0.819152, 1.0, 0.819152}, {0.819152, 0.819152, 1.0}});
        Assert.assertArrayEquals(correlationMatrixExpected.getData(), pearsonsCorrelation1.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonsCorrelation1.nObs);

        // Test constructor with RealMatrix
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pearsonsCorrelation2 = new PearsonsCorrelation(matrix);
        Assert.assertArrayEquals(correlationMatrixExpected.getData(), pearsonsCorrelation2.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonsCorrelation2.nObs);

        // Test constructor with Covariance
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation3 = new PearsonsCorrelation(covariance);
        Assert.assertArrayEquals(correlationMatrixExpected.getData(), pearsonsCorrelation3.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonsCorrelation3.nObs);

        // Test getCorrelationMatrix()
        RealMatrix correlationMatrix = pearsonsCorrelation1.getCorrelationMatrix();
        Assert.assertArrayEquals(correlationMatrixExpected.getData(), correlationMatrix.getData(), 0.001);

        // Test getCorrelationStandardErrors()
        RealMatrix correlationStandardErrors = pearsonsCorrelation1.getCorrelationStandardErrors();
        RealMatrix correlationStandardErrorsExpected = new BlockRealMatrix(new double[][]{{0.547650, 0.547650, 0.547650}, {0.547650, 0.547650, 0.547650}, {0.547650, 0.547650, 0.547650}});
        Assert.assertArrayEquals(correlationStandardErrorsExpected.getData(), correlationStandardErrors.getData(), 0.001);

        // Test getCorrelationPValues()
        try {
            RealMatrix correlationPValues = pearsonsCorrelation1.getCorrelationPValues();
            RealMatrix correlationPValuesExpected = new BlockRealMatrix(new double[][]{{0.997346, 0.997346, 0.997346}, {0.997346, 0.997346, 0.997346}, {0.997346, 0.997346, 0.997346}});
            Assert.assertArrayEquals(correlationPValuesExpected.getData(), correlationPValues.getData(), 0.001);
        } catch (MathException e) {
            Assert.fail("getCorrelationPValues() should not throw an exception.");
        }

        // Test correlation() method
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.819152, correlation, 0.001);
    }
}