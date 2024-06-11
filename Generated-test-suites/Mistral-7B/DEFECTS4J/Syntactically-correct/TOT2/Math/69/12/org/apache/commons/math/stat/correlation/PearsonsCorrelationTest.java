import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Pearson's Correlation class
 */
public class PearsonsCorrelationTest {

    /**
     * Test the correlation method with two arrays of the same length
     */
    @Test
    public void testCorrelationWithArraysSameLength() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {1, 2, 3, 4, 5};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xArray, yArray);
        Assert.assertEquals(1.0, correlationCoefficient, 0.01);
    }

    /**
     * Test the correlation method with two arrays of different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithArraysDifferentLengths() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {1, 2, 3, 4};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    /**
     * Test the correlation method with arrays containing zero values
     */
    @Test
    public void testCorrelationWithArraysContainingZeros() {
        double[] xArray = {0, 1, 2, 0};
        double[] yArray = {1, 2, 3, 1};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xArray, yArray);
        Assert.assertEquals(1.0, correlationCoefficient, 0.01);
    }

    /**
     * Test the correlation method with arrays containing only NaN values
     */
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationWithArraysContainingNaN() {
        double[] xArray = {Double.NaN, Double.NaN, Double.NaN};
        double[] yArray = {Double.NaN, Double.NaN, Double.NaN};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    /**
     * Test the correlation method with arrays containing only negative values
     */
    @Test
    public void testCorrelationWithArraysContainingOnlyNegatives() {
        double[] xArray = {-1, -2, -3, -4, -5};
        double[] yArray = {-1, -2, -3, -4, -5};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xArray, yArray);
        Assert.assertEquals(-1.0, correlationCoefficient, 0.01);
    }

    /**
     * Test the correlation method with empty arrays
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithEmptyArrays() {
        double[] xArray = {};
        double[] yArray = {};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    /**
     * Test the getCorrelationMatrix method with a 2x2 matrix
     */
    @Test
    public void testGetCorrelationMatrixWith2x2Matrix() {
        double[][] data = {{1, 2}, {3, 4}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.01);
    }

    /**
     * Test the getCorrelationMatrix method with a 3x3 matrix
     */
    @Test
    public void testGetCorrelationMatrixWith3x3Matrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.571428571, 0.4545454545},
                {0.571428571, 1.0, 0.6222222222},
                {0.4545454545, 0.6222222222, 1.0}});
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.01);
    }

    /**
     * Test the getCorrelationStandardErrors method with a 3x3 matrix
     */
    @Test
    public void testGetCorrelationStandardErrorsWith3x3Matrix() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.4530434781, 0.3615044714, 0.3542973225},
                {0.3615044714, 0.4530434781, 0.4533632573},
                {0.3542973225, 0.4533632573, 0.4530434781}});
        Assert.assertArrayEquals(expectedCorrelationStandardErrors.getData(), correlationStandardErrors.getData(), 0.01);
    }

    /**
     * Test the getCorrelationPValues method with a 3x3 matrix
     */
    @Test
    public void testGetCorrelationPValuesWith3x3Matrix() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{0.0004217255, 0.0082215781, 0.0082215781},
                {0.0082215781, 0.0004217255, 0.0004494538},
                {0.0082215781, 0.0004494538, 0.0004217255}});
        Assert.assertArrayEquals(expectedCorrelationPValues.getData(), correlationPValues.getData(), 0.01);
    }
}