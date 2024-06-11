import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributiontest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructor() {
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        Assert.assertNotNull(mnd);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, new double[2][2]);
        RealVector meansVector = mnd.getMeans();
        Assert.assertArrayEquals(means, meansVector.toArray(), 0.0);
    }

    @Test
    public void testgetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covariancesMatrix = mnd.getCovariances();
        RealMatrix expectedCovariancesMatrix = new Array2DRowRealMatrix(covariances);
        Assert.assertEquals(expectedCovariancesMatrix, covariancesMatrix);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        double[] testValues = {1.5, 2.5};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double density = mnd.density(testValues);
        double expectedDensity = Math.exp(-1.5) * Math.pow(Math.PI, 2) / (Math.sqrt(2 * Math.PI) * Math.sqrt(2)) * Math.exp(-0.5 * ((1.5 - 1.0) * 1.5 + (2.5 - 2.0) * 2.5));
        Assert.assertEquals(expectedDensity, density, 1e-8);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] sample = mnd.sample();
        double[] expectedSample = new double[]{1.531144754669413, 2.321682083137195};
        Assert.assertArrayEquals(expectedSample, sample, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSampleException() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] sample = mnd.sample();
        double[] sample2 = mnd.sample();
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = mnd.getStandardDeviations();
        Assert.assertArrayEquals(new double[]{1.0, 0.7071067811865476}, standardDeviations, 0.0);
    }

    @Test
    public void testGetDimension() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        int dimension = mnd.getDimension();
        Assert.assertEquals(2, dimension);
    }
}