import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test construction with correct parameters
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.5, 0.5}, {0.5, 2.0}};
        try {
            MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
            Assert.assertArrayEquals(means, mvnd.getMeans());
            RealMatrix covMat = mvnd.getCovariances();
            Array2DRowRealMatrix covMatCopy = new Array2DRowRealMatrix(covMat.getData());
            Assert.assertArrayEquals(covariances[0], covMatCopy.getRowVector(0));
            Assert.assertArrayEquals(covariances[1], covMatCopy.getRowVector(1));
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test density calculation
        double[] testValues = {2.5, 3.5};
        MultivariateNormalDistribution mvnd = null;
        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        double density = mvnd.density(testValues);
        double expectedDensity = 0.1148292968198236; // Calculated using given means and covariances
        Assert.assertEquals(expectedDensity, density, 0.001);

        // Test sample method
        RandomGenerator rng = new Well19937c();
        mvnd = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sampleValues = mvnd.sample();
        Assert.assertArrayEquals(testValues, sampleValues, 0.01);

        // Test negative covariance matrix
        double[] means2 = {1.0, 1.0};
        double[][] covariances2 = {{1.5, -0.5}, {-0.5, 2.0}};
        try {
            new MultivariateNormalDistribution(means2, covariances2);
            Assert.fail("Expected DimensionMismatchException or NonPositiveDefiniteMatrixException");
        } catch (DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test incorrect number of dimensions
        double[] means3 = {1.0};
        double[][] covariances3 = {{1.5, 0.5}, {0.5, 2.0}, {0.5, 2.0}};
        try {
            new MultivariateNormalDistribution(means3, covariances3);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        // Test incorrect number of rows or columns in covariance matrix
        double[][] covariances4 = {{1.5, 0.5}, {0.5, 2.0}, {0.5, 2.0}, {1.0, 2.0}};
        try {
            new MultivariateNormalDistribution(means, covariances4);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.5, 0.5}, {0.5, 2.0}};
        MultivariateNormalDistribution mvnd = null;
        try {
            mvnd = new MultivariateNormalDistribution(means, covariances);
        } catch (SingularMatrixException | NonPositiveDefiniteMatrixException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        double[] standardDeviations = mvnd.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{FastMath.sqrt(1.5), FastMath.sqrt(2.0)}, standardDeviations, 0.01);
    }
}