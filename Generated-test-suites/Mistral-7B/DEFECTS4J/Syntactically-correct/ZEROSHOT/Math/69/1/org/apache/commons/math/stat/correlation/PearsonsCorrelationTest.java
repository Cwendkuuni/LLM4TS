import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.correlationMatrix);
        Assert.assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArray() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testValidDataArray() {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.8888888888888889, correlationMatrix.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 2), 0.001);
    }

    @Test
    public void testValidRealMatrix() {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.8888888888888889, correlationMatrix.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 2), 0.001);
    }

    @Test
    public void testInvalidRealMatrix() {
        double[][] data = { { 1, 2 }, { 3, 4 } };
        RealMatrix matrix = new BlockRealMatrix(data);
        try {
            new PearsonsCorrelation(matrix);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCovariance() {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        Covariance covariance = new Covariance(data);
        RealMatrix correlationMatrix = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN()).getCorrelationMatrix();
        Assert.assertNotNull(correlationMatrix);
        Assert.assertEquals(3, correlationMatrix.getRowDimension());
        Assert.assertEquals(3, correlationMatrix.getColumnDimension());
        Assert.assertEquals(1d, correlationMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.8888888888888889, correlationMatrix.getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 0), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.8, correlationMatrix.getEntry(1, 2), 0.001);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValues() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        Assert.assertNotNull(correlationPValues);
        Assert.assertEquals(3, correlationPValues.getRowDimension());
        Assert.assertEquals(3, correlationPValues.getColumnDimension());
        Assert.assertEquals(0, correlationPValues.getEntry(0, 0), 0.001);
        Assert.assertEquals(1.0 / 12.0, correlationPValues.getEntry(0, 1), 0.001);
        Assert.assertEquals(1.0 / 12.0, correlationPValues.getEntry(1, 0), 0.001);
        Assert.assertEquals(1.0 / 6.0, correlationPValues.getEntry(1, 1), 0.001);
        Assert.assertEquals(1.0 / 12.0, correlationPValues.getEntry(2, 0), 0.001);
        Assert.assertEquals(1.0 / 6.0, correlationPValues.getEntry(0, 2), 0.001);
        Assert.assertEquals(1.0 / 6.0, correlationPValues.getEntry(2, 1), 0.001);
        Assert.assertEquals(1.0 / 6.0, correlationPValues.getEntry(1, 2), 0.001);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = { { 1, 2, 3 }, { 2, 3, 4 }, { 3, 4, 5 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        Assert.assertNotNull(correlationStandardErrors);
        Assert.assertEquals(3, correlationStandardErrors.getRowDimension());
        Assert.assertEquals(3, correlationStandardErrors.getColumnDimension());
        Assert.assertEquals(0.5, correlationStandardErrors.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.3872983346214049, correlationStandardErrors.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.3872983346214049, correlationStandardErrors.getEntry(1, 0), 0.001);
        Assert.assertEquals(0.31622776601683795, correlationStandardErrors.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.3872983346214049, correlationStandardErrors.getEntry(2, 0), 0.001);
        Assert.assertEquals(0.3872983346214049, correlationStandardErrors.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.31622776601683795, correlationStandardErrors.getEntry(2, 1), 0.001);
        Assert.assertEquals(0.31622776601683795, correlationStandardErrors.getEntry(1, 2), 0.001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = { 1, 2, 3 };
        double[] yArray = { 2, 3, 4 };
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        Assert.assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationArraySizes() {
        double[] xArray = { 1 };
        double[] yArray = { 2, 3 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationInsufficientData() {
        double[] xArray = { 1 };
        double[] yArray = { 1 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}