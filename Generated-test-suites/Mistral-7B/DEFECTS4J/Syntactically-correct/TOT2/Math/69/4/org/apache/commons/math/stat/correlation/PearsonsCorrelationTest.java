import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    /**
     * Test constructor with empty matrix
     */
    @Test
    public void testEmptyMatrix() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        assertNull(pearson.getCorrelationMatrix());
        assertEquals(0, pearson.getNObs());
    }

    /**
     * Test constructor with rectangular array
     */
    @Test
    public void testRectangularArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearson = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.7416, 0.7353, 0.7288},
                {0.7353, 0.7416, 0.7353},
                {0.7288, 0.7353, 0.7416}});
        RealMatrix result = pearson.getCorrelationMatrix();
        assertEquals(expected, result);
        assertEquals(3, pearson.getNObs());
    }

    /**
     * Test constructor with RealMatrix
     */
    @Test
    public void testRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        PearsonsCorrelation pearson = new PearsonsCorrelation(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8165},
                {0.8165, 1.0}});
        RealMatrix result = pearson.getCorrelationMatrix();
        assertEquals(expected, result);
        assertEquals(2, pearson.getNObs());
    }

    /**
     * Test constructor with Covariance
     */
    @Test
    public void testCovariance() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8165},
                {0.8165, 1.0}});
        RealMatrix result = pearson.getCorrelationMatrix();
        assertEquals(expected, result);
        assertEquals(2, pearson.getNObs());
    }

    /**
     * Test constructor with Covariance and number of observations
     */
    @Test
    public void testCovarianceAndNumberOfObservations() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearson = new PearsonsCorrelation(covariance.getCovarianceMatrix(), covariance.getN());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8165},
                {0.8165, 1.0}});
        RealMatrix result = pearson.getCorrelationMatrix();
        assertEquals(expected, result);
        assertEquals(2, pearson.getNObs());
    }

    /**
     * Test getCorrelationMatrix() method with empty instance
     */
    @Test(expected = MathRuntimeException.class)
    public void testGetCorrelationMatrixEmptyInstance() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        pearson.getCorrelationMatrix();
    }

    /**
     * Test getCorrelationStandardErrors() method with empty instance
     */
    @Test(expected = MathRuntimeException.class)
    public void testGetCorrelationStandardErrorsEmptyInstance() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        pearson.getCorrelationStandardErrors();
    }

    /**
     * Test getCorrelationPValues() method with empty instance
     */
    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesEmptyInstance() {
        PearsonsCorrelation pearson = new PearsonsCorrelation();
        pearson.getCorrelationPValues();
    }

    /**
     * Test correlation method with arrays of different lengths
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationArraysDifferentLengths() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {1.0, 2.0, 3.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test correlation method with arrays of same length but insufficient data
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationArraysInsufficientData() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {1.0};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    /**
     * Test correlation method with arrays of same length and sufficient data
     */
    @Test
    public void testCorrelationArraysSufficientData() {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0, 3.0};
        double expectedCorrelation = 0.8165;
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(expectedCorrelation, correlation, 0.001);
    }

    /**
     * Test covarianceToCorrelation method with empty covariance matrix
     */
    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationEmptyCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[0][0]);
        new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
    }

    /**
     * Test covarianceToCorrelation method with covariance matrix with two variables
     */
    @Test
    public void testCovarianceToCorrelationTwoVariables() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{0.5, 1.0}, {1.0, 1.5}});
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8225},
                {0.8225, 1.0}});
        RealMatrix result = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        assertEquals(expected, result);
    }

}