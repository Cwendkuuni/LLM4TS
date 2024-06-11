import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() throws Exception {
        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        };

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        Assert.assertArrayEquals(means, mvnd.getMeans(), 0.0);
        Assert.assertEquals(3, mvnd.getDimension());
        Assert.assertNotNull(mvnd.getCovariances());

        RealMatrix covarianceMatrix = mvnd.getCovariances();
        RealMatrix expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedCovarianceMatrix, covarianceMatrix);

        double[] standardDeviations = mvnd.getStandardDeviations();
        double[] expectedStandardDeviations = {1.0, 1.0, 1.0};
        Assert.assertArrayEquals(expectedStandardDeviations, standardDeviations, 0.0);

        double[] sample = mvnd.sample();
        Assert.assertArrayEquals(means, sample, 0.01);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDimensionMismatchException() throws Exception {
        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
                {1.0, 0.5},
                {0.5, 1.0},
                {0.5, 0.5}
        };

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionSingularMatrixException() throws Exception {
        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
                {0.0, 0.0},
                {0.0, 0.0},
                {0.0, 0.0}
        };

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionNonPositiveDefiniteMatrixException() throws Exception {
        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
                {-1.0, 0.5},
                {0.5, -1.0},
                {0.5, 0.5}
        };

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() throws Exception {
        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
                {1.0, 0.5, 0.5},
                {0.5, 1.0, 0.5},
                {0.5, 0.5, 1.0}
        };

        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] sample1 = {1.0, 1.0, 1.0};
        double density1 = mvnd.density(sample1);

        double[] sample2 = {-1.0, -1.0, -1.0};
        double density2 = mvnd.density(sample2);

        Assert.assertNotEquals(density1, density2);
        Assert.assertTrue(density1 > 0.0);
    }
}