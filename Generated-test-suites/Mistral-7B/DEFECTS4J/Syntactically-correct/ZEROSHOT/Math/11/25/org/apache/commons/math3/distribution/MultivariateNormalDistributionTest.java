import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test with given mean vector and covariance matrix
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 2.0}};

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariances);

        Assert.assertArrayEquals(means, distribution.getMeans(), 0.001);
        RealMatrix covarianceMatrix = distribution.getCovariances();
        RealMatrix expectedCovarianceMatrix = RealMatrixUtils.createSymmetric(2, 2, 0.5);
        Assert.assertEquals(expectedCovarianceMatrix, covarianceMatrix);

        // Test getting standard deviations
        double[] standardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, Math.sqrt(2.0)}, standardDeviations, 0.001);

        // Test getting density
        double[] values = new double[]{2.5, 3.5};
        double density = distribution.density(values);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(Math.E, -5.5) * Math.exp(-1.5) * Math.exp(-1.5 * Math.pow(1.5, 2));
        Assert.assertEquals(expectedDensity, density, 0.001);

        // Test sampling
        double[] sample = distribution.sample();
        double[] expectedSample = new double[]{1.523486721293286, 3.137881443482893};
        Assert.assertArrayEquals(expectedSample, sample, 0.01);

        // Test with custom random number generator
        RandomGenerator rng = new Well19937c();
        MultivariateNormalDistribution distributionWithCustomRng = new MultivariateNormalDistribution(rng, means, covariances);
        double[] sampleWithCustomRng = distributionWithCustomRng.sample();
        Assert.assertArrayEquals(expectedSample, sampleWithCustomRng, 0.01);

        // Test with incorrect mean vector and covariance matrix size
        try {
            double[] incorrectMeans = {1.0, 2.0, 3.0};
            double[][] incorrectCovariances = {{1.0, 0.5}, {0.5, 2.0}, {0.5, 2.0}};
            new MultivariateNormalDistribution(incorrectMeans, incorrectCovariances);
            Assert.fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }

        // Test with singular covariance matrix
        double[][] singularCovariances = {{1.0, 0.5}, {0.5, 0.0}};
        try {
            new MultivariateNormalDistribution(means, singularCovariances);
            Assert.fail();
        } catch (SingularMatrixException e) {
            // Expected
        }

        // Test with non-positive definite covariance matrix
        double[][] nonPositiveDefiniteCovariances = {{1.0, 0.5}, {0.5, -1.0}};
        try {
            new MultivariateNormalDistribution(means, nonPositiveDefiniteCovariances);
            Assert.fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }
}