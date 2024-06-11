import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test construction from means and covariance matrix
        double[] means = {0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertArrayEquals(means, dist.getMeans(), 0.0);
        RealMatrix covMatrix = dist.getCovariances();
        Array2DRowRealMatrix covMatrixCopy = new Array2DRowRealMatrix(covariances);
        Assert.assertArrayEquals(covMatrixCopy.getData(), covMatrix.getData(), 0.0);

        // Test density calculation
        double[] testValues = {1.0, 1.5};
        Assert.assertEquals(3.1415926536, dist.density(testValues), 0.001);

        // Test mean and standard deviation values
        double[] stdDev = dist.getStandardDeviations();
        Assert.assertEquals(1.0, stdDev[0], 0.0);
        Assert.assertEquals(1.0, stdDev[1], 0.0);

        // Test sample generation
        RealArray2DSample sample = dist.sample(1, true, false);
        RealVector sampleVector = sample.getColumnVector(0);
        Assert.assertEquals(2, sampleVector.getDimension());
        Assert.assertArrayEquals(testValues, sampleVector.toArray(), 0.01);

        // Test negative covariance matrix
        means = new double[]{0.0, 0.0};
        covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Should have thrown NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test singular covariance matrix
        means = new double[]{0.0, 0.0};
        covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Should have thrown SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        // Test incorrect dimensions
        means = new double[]{0.0};
        covariances = new double[][]{{1.0, 1.0}, {1.0, 1.0}};
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Should have thrown DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}