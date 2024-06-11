import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[0][0]);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(emptyMatrix);
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        double[][] identityMatrix = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new BlockRealMatrix(identityMatrix);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        }, correlationMatrix.getData());
    }

    @Test
    public void testNegativeCovarianceMatrix() {
        double[][] negativeCovarianceMatrix = {{1.0, 2.0}, {2.0, -1.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(negativeCovarianceMatrix);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(new double[][]{
                {0.57142857, -0.57142857},
                {-0.57142857, 0.57142857}
        }, correlationMatrix.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientData() {
        double[][] insufficientData = {{1.0}};
        RealMatrix matrix = new BlockRealMatrix(insufficientData);
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentRowLengths() {
        double[][] differentRowLengths = {{1.0, 2.0}, {3.0}};
        RealMatrix matrix = new BlockRealMatrix(differentRowLengths);
        new PearsonsCorrelation(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareMatrix() {
        double[][] nonSquareMatrix = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        RealMatrix matrix = new BlockRealMatrix(nonSquareMatrix);
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.74165979, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArrayDimensions() {
        double[] xArray = {1.0};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }
}