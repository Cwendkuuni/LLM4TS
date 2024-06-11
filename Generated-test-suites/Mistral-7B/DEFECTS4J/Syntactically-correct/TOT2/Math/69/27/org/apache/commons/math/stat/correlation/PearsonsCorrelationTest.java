import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        Assert.assertNull(correlation.getCorrelationMatrix());
        Assert.assertEquals(0, correlation.getnObs());
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.819152, 0.819152},
                {0.819152, 1.0, 0.819152}, {0.819152, 0.819152, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
        Assert.assertEquals(3, correlation.getnObs());
    }

    @Test
    public void testRealMatrixConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.819152, 0.819152},
                {0.819152, 1.0, 0.819152}, {0.819152, 0.819152, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
        Assert.assertEquals(3, correlation.getnObs());
    }

    @Test
    public void testDataWithNaN() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, Double.NaN, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        Assert.assertNull(correlation.getCorrelationMatrix());
        Assert.assertEquals(0, correlation.getnObs());
    }

    @Test(expected = MathRuntimeException.class)
    public void testDataWithDifferentNumberOfColumns() {
        double[][] data1 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        double[][] data2 = {{1.0, 2.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation correlation1 = new PearsonsCorrelation(data1);
        PearsonsCorrelation correlation2 = new PearsonsCorrelation(data2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataWithLessThanTwoColumns() {
        double[][] data = {{1.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceConstructorWithNullCovarianceMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation(null, 3);
    }

    @Test
    public void testCovarianceConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix covarianceMatrix = new Covariance().covariance(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 3);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.819152, 0.819152},
                {0.819152, 1.0, 0.819152}, {0.819152, 0.819152, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
        Assert.assertEquals(3, correlation.getnObs());
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceConstructorWithNegativeNumberOfObservations() {
        RealMatrix covarianceMatrix = new Covariance().covariance(new double[][]{{1.0, 2.0}, {4.0, 5.0}, {7.0, 8.0}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, -1);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.819152, 0.819152},
                {0.819152, 1.0, 0.819152}, {0.819152, 0.819152, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix.toString(), actualCorrelationMatrix.toString());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.465803, 0.465803},
                {0.465803, 0.0, 0.465803}, {0.465803, 0.465803, 0.0}});
        RealMatrix actualCorrelationStandardErrors = correlation.getCorrelationStandardErrors();

        Assert.assertEquals(expectedCorrelationStandardErrors.toString(), actualCorrelationStandardErrors.toString());
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesWithLessThanThreeVariables() {
        double[][] data = {{1.0, 2.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        correlation.getCorrelationPValues();
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.329841, 0.999761},
                {0.329841, 0.0, 0.999761}, {0.999761, 0.999761, 0.0}});
        RealMatrix actualCorrelationPValues = correlation.getCorrelationPValues();

        Assert.assertEquals(expectedCorrelationPValues.toString(), actualCorrelationPValues.toString());
    }
}