import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        final double[] means = {0.0, 0.0};
        final double[][] covariances = {
                {1.0, 0.5},
                {0.5, 2.0}
        };
        RandomGenerator rng = new Well19937c();

        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);

        // Test mean and covariance
        double[] actualMeans = dist.getMeans();
        assertArrayEquals(means, actualMeans);
        RealMatrix actualCovariances = dist.getCovariances();
        Array2DRowRealMatrix expectedCovariances = new Array2DRowRealMatrix(covariances);
        assertTrue(actualCovariances.equals(expectedCovariances, 0.001));

        // Test density
        double[] testValues = {1.0, 1.5};
        double actualDensity = dist.density(testValues);
        double expectedDensity = 1 / (2 * Math.PI) * Math.exp(-1.0 / 2.0 - 0.75 / 2.0);
        assertEquals(expectedDensity, actualDensity, 0.001);

        // Test sampling
        double[] sampledValues = dist.sample();
        for (int i = 0; i < sampledValues.length; i++) {
            System.out.print(sampledValues[i] + " ");
        }
        System.out.println();

        double[] meansSample = {0.0, 0.0};
        for (int i = 0; i < sampledValues.length; i++) {
            meansSample[i] += sampledValues[i];
        }
        double actualMeanSample = 0.0;
        for (int i = 0; i < meansSample.length; i++) {
            actualMeanSample += meansSample[i];
        }
        actualMeanSample /= sampledValues.length;
        assertEquals(means[0], actualMeanSample, 0.1);

        double[] covarianceSample = new double[sampledValues.length];
        for (int i = 0; i < sampledValues.length; i++) {
            covarianceSample[i] = Math.pow((sampledValues[i] - means[i]) * (sampledValues[i] - means[i]), 2);
        }
        RealMatrix actualCovarianceSample = new Array2DRowRealMatrix(covarianceSample);
        assertTrue(actualCovariances.equals(actualCovarianceSample, 0.01));

        // Test exception
        try {
            new MultivariateNormalDistribution(rng, means, covariances.length > means.length ? new double[covariances.length][2] : new double[means.length][2]);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }

        try {
            new MultivariateNormalDistribution(rng, means, covariances);
            // Change covariances to singular matrix
            covariances[0][0] = 0.0;
            MultivariateNormalDistribution distSingular = new MultivariateNormalDistribution(rng, means, covariances);
            fail();
        } catch (SingularMatrixException e) {
            // expected
        }

        try {
            new MultivariateNormalDistribution(rng, means, covariances);
            // Change covariances to negative eigenvalues
            covariances[0][0] = -1.0;
            MultivariateNormalDistribution distNegative = new MultivariateNormalDistribution(rng, means, covariances);
            fail();
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}