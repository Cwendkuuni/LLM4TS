import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        // Test construction with mean vector and covariance matrix.
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);
        Assert.assertArrayEquals(means, mvnd.getMeans(), 1e-10);
        RealMatrix covarianceMatrix = mvnd.getCovariances();
        Assert.assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        Assert.assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                Assert.assertEquals(covariances[i][j], covarianceMatrix.getEntry(i, j), 1e-10);
            }
        }

        // Test construction with random number generator, mean vector and covariance matrix.
        RealMatrix covarianceMatrix2 = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0}, {1.0, 5.0}});
        MultivariateNormalDistribution mvnd2 = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        Assert.assertArrayEquals(means, mvnd2.getMeans(), 1e-10);
        RealMatrix covarianceMatrix3 = mvnd2.getCovariances();
        Assert.assertEquals(covariances[0].length, covarianceMatrix3.getColumnDimension());
        Assert.assertEquals(covariances.length, covarianceMatrix3.getRowDimension());
        for (int i = 0; i < covariances.length; i++) {
            for (int j = 0; j < covariances[i].length; j++) {
                Assert.assertEquals(covariances[i][j], covarianceMatrix3.getEntry(i, j), 1e-10);
            }
        }
        Assert.assertEquals(covarianceMatrix2, covarianceMatrix3);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] testValues = {1.5, 2.5};
        double testDensity = mvnd.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(1.0, -1.0) * FastMath.exp(-0.5 * 0.5);
        Assert.assertEquals(expectedDensity, testDensity, 1e-10);

        testValues = new double[]{-1.5, 2.5};
        testDensity = mvnd.density(testValues);
        expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(1.0, -1.0) * FastMath.exp(-0.5 * 3.5);
        Assert.assertEquals(expectedDensity, testDensity, 1e-10);

        testValues = new double[]{1.5, -2.5};
        testDensity = mvnd.density(testValues);
        expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) * FastMath.pow(3.0, -1.0) * FastMath.exp(-0.5 * (Math.pow(1.5 - 1.0, 2.0) + Math.pow(2.5 - 2.0, 2.0)));
        Assert.assertEquals(expectedDensity, testDensity, 1e-10);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] sample = mvnd.sample();
        double[] meansCopy = MathArrays.copyOf(means);
        RealMatrix covarianceMatrix = mvnd.getCovariances();

        for (int i = 0; i < sample.length; i++) {
            Assert.assertTrue(Precision.isClose(meansCopy[i], sample[i], 1e-6));
        }

        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample.length; j++) {
                Assert.assertTrue(Precision.isClose(covarianceMatrix.getEntry(i, j), 0.0, 1e-6));
            }
        }

        for (int i = 0; i < sample.length; i++) {
            for (int j = i; j < sample.length; j++) {
                Assert.assertEquals(covarianceMatrix.getEntry(i, j), covarianceMatrix.getEntry(j, i), 1e-6);
            }
        }

        for (int i = 0; i < sample.length; i++) {
            for (int j = 0; j < sample.length; j++) {
                double sum = 0;
                for (int k = 0; k < sample.length; k++) {
                    sum += (sample[i] - meansCopy[i]) * (sample[j] - meansCopy[j]) * covarianceMatrix.getEntry(k, k);
                }
                Assert.assertEquals(sum, 1.0, 1e-6);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionSampleWithWrongDimension() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{2.0, 1.0}, {1.0, 3.0}};
        MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(means, covariances);

        double[] sample = mvnd.sample();
        double[] meansCopy = MathArrays.copyOf(means);
        RealMatrix covarianceMatrix = mvnd.getCovariances();

        sample = new double[]{sample[0], sample[1], 3.0};
    }
}