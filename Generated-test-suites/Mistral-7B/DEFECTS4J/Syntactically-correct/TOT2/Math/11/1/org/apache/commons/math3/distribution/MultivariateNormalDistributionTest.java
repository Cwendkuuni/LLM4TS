import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionConstruction() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(means, dist.getMeans());
        RealMatrix covMatrix = dist.getCovariances();
        Array2DRowRealMatrix covMatrixExpected = new Array2DRowRealMatrix(covariances);
        assertEquals(covMatrixExpected, covMatrix);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalDistributionDimMismatchException() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0, 2.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testMultivariateNormalDistributionSingularMatrixException() {
        double[] means = {1.0};
        double[][] covariances = {{0.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testMultivariateNormalDistributionNonPositiveDefiniteMatrixException() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testMultivariateNormalDistributionDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.5, 2.5};
        double expectedDensity = 0.11274183764323264;
        assertEquals(expectedDensity, dist.density(testValues), 0.01);
    }

    @Test
    public void testMultivariateNormalDistributionSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sample = dist.sample();
        double[] expectedSample = {1.43183685, 1.79433488};
        assertArrayEquals(expectedSample, sample);
    }

    @Test
    public void testMultivariateNormalDistributionGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] standardDeviations = dist.getStandardDeviations();
        double expectedStandardDeviation1 = 1.0;
        double expectedStandardDeviation2 = 0.70710678;
        assertArrayEquals(new double[]{expectedStandardDeviation1, expectedStandardDeviation2}, standardDeviations);
    }
}