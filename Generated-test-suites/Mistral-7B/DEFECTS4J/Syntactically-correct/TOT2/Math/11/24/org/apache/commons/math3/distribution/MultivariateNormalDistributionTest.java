import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        // Test constructor with valid arguments
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, distribution.getMeans());
        Assert.assertEquals(2, distribution.getDimension());
        RealMatrix covarianceMatrix = distribution.getCovariances();
        Assert.assertEquals(1.0, covarianceMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.5, covarianceMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.5, covarianceMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(1.0, covarianceMatrix.getEntry(1, 1), 0.001);

        // Test density method
        double[] testValues = new double[]{1.0, 1.5};
        double density = distribution.density(testValues);
        // Calculate density mathematically
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(1.0, -1.0) * FastMath.exp(-0.5 * (Math.pow(testValues[0] - means[0], 2) + Math.pow(testValues[1] - means[1], 2)));
        Assert.assertEquals(expectedDensity, density, 0.01);

        // Test sample method
        double[] sample = distribution.sample();
        Assert.assertArrayEquals(means, sample, 0.01);
        for (int i = 0; i < distribution.getDimension(); i++) {
            double standardDeviation = Math.sqrt(covariances[i][i]);
            Assert.assertTrue(Math.abs(sample[i] - means[i]) < standardDeviation);
        }

        // Test getStandardDeviations method
        double[] standardDeviations = distribution.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, 0.70710678118654755}, standardDeviations, 0.01);

        // Test exception handling
        try {
            MultivariateNormalDistribution wrongDistribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0}, new double[][]{{1.0, 0.5}, {0.5, -1.0}});
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        try {
            MultivariateNormalDistribution wrongDistribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0}, new double[][]{{1.0, 0.5}, {0.5, 3.0}});
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        try {
            MultivariateNormalDistribution wrongDistribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0}, new double[][]{
                    {1.0, 0.5},
                    {3.0, 1.0}
            });
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            MultivariateNormalDistribution wrongDistribution = new MultivariateNormalDistribution(new Well19937c(), new double[]{0.0, 0.0}, new double[][]{
                    {1.0, 0.5},
                    {3.0, 2.0}
            });
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}