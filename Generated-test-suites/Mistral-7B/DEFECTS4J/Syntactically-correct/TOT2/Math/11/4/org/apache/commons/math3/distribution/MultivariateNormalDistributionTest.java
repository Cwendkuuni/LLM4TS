import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        // Test valid construction
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        Assert.assertNotNull(dist);
        Assert.assertEquals(means.length, dist.getDimension());
        Assert.assertEquals(means, dist.getMeans());
        // Test invalid construction with inconsistent arrays
        means = new double[2];
        covariances = new double[2][3];
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("An exception should have been thrown.");
        } catch (DimensionMismatchException e) {
            // expected
        }
        // Test invalid construction with singular covariance matrix
        covariances = new double[3][3];
        SingularValueDecomposition svd = new SingularValueDecomposition(new Array2DRowRealMatrix(covariances));
        covariances = svd.getSingularValues().subtract(new Array2DRowRealMatrix(new double[covariances.length][3], true)).getData();
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("An exception should have been thrown.");
        } catch (SingularMatrixException e) {
            // expected
        }
        // Test invalid construction with negative eigenvalues
        covariances = new double[3][3];
        EigenDecomposition eig = new EigenDecomposition(new Array2DRowRealMatrix(covariances));
        covariances = eig.getSolver().getInverse().getData();
        covariances[0][0]--;
        try {
            new MultivariateNormalDistribution(means, covariances);
            Assert.fail("An exception should have been thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testMultivariateNormalDistributionGetters() {
        // Test getMeans()
        double[] means = {1.0, 2.0, 3.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[3][3]);
        Assert.assertArrayEquals(means, dist.getMeans());
        // Test getCovariances()
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covMatrix = dist.getCovariances();
        Assert.assertEquals(covMatrix, new Array2DRowRealMatrix(covariances));
        // Test getStandardDeviations()
        double[] stdDevs = dist.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{Math.sqrt(covariances[0][0]), Math.sqrt(covariances[1][1]), Math.sqrt(covariances[2][2])}, stdDevs);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        // Test valid density calculation
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {0.0, 0.5, 1.0};
        double expectedDensity = 1.0 / (Math.sqrt(2 * Math.PI) * Math.sqrt(3.0)) * Math.exp(-((testValues[0] - means[0]) * (testValues[0] - means[0]) / 3.0 +
                (testValues[1] - means[1]) * (testValues[1] - means[1]) / 2.0 +
                (testValues[2] - means[2]) * (testValues[2] - means[2]) / 3.0)) / 3.0;
        double calculatedDensity = dist.density(testValues);
        Assert.assertEquals(expectedDensity, calculatedDensity, 1e-8);
        // Test invalid density calculation with inconsistent input
        testValues = new double[4];
        try {
            dist.density(testValues);
            Assert.fail("An exception should have been thrown.");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        // Test valid sample generation
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rng = new Well19937c();
        double[] testValues = dist.sample();
        for (int i = 0; i < testValues.length; i++) {
            Assert.assertTrue(Double.isFinite(testValues[i]));
        }
        // Test sample generation with zero mean and identity covariance matrix
        means = new double[3];
        covariances = new double[3][3];
        for (int i = 0; i < covariances.length; i++) {
            covariances[i][i] = 1.0;
        }
        dist = new MultivariateNormalDistribution(means, covariances);
        testValues = dist.sample();
        for (int i = 0; i < testValues.length; i++) {
            Assert.assertEquals(means[i], testValues[i], 1e-8);
        }
    }
}