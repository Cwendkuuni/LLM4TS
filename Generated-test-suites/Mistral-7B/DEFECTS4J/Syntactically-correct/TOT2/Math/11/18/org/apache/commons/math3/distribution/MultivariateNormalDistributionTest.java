import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetMeans() {
        final double[] means = {1.0, 2.0};
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final double[] actual = distribution.getMeans();
        Assert.assertArrayEquals(means, actual);
    }

    @Test
    public void testGetCovariances() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        final RealMatrix expected = new Array2DRowRealMatrix(covariances);
        final RealMatrix actual = distribution.getCovariances();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDensity() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] testValues = {1.5, 2.5};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2 / 2) * FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(testValues);
        double actualDensity = distribution.density(testValues);
        Assert.assertEquals(expectedDensity, actualDensity, 1e-8);
    }

    @Test
    public void testSample() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] sample = distribution.sample();
        Assert.assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Double.isFinite(sample[i]));
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleWithInvalidDimension() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        distribution.sample(3);
    }

    private static double getExponentTerm(final double[] values) {
        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - getMeans()[i];
        }
        final double[] preMultiplied = covarianceMatrixInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }

    private static double[] getMeans() {
        final double[] means = {1.0, 2.0};
        return means;
    }
}