import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testCreateInstanceWithValidData() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(means, dist.getMeans());
        RealMatrix covariance = dist.getCovariances();
        assertEquals(covariances, covariance.getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testCreateInstanceWithInconsistentData() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.5}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testCreateInstanceWithSingularCovarianceMatrix() throws DimensionMismatchException,
            NonPositiveDefiniteMatrixException, SingularMatrixException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.0}, {0.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testCreateInstanceWithNegativeEigenvalues() throws SingularMatrixException,
            DimensionMismatchException, NonPositiveDefiniteMatrixException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test
    public void testGetDimension() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
    }

    @Test
    public void testGetMeans() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertArrayEquals(means, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        RealMatrix covariance = dist.getCovariances();
        assertEquals(covariances, covariance.getData());
    }

    @Test
    public void testDensityWithValidData() throws DimensionMismatchException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {1.5, 2.5};
        double density = dist.density(testValues);
        double expected = FastMath.pow(2 * FastMath.PI, -2) * FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) * dist.getExponentTerm(testValues);
        assertEquals(expected, density, 1e-14);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityWithInvalidData() throws DimensionMismatchException {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.0, 3.0};
        dist.density(testValues);
    }

    @Test
    public void testSampleWithValidData() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] sample = dist.sample();
        assertArrayEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertEquals(0.0, sample[i] - means[i], 1e-5);
        }
    }

    @Test
    public void testGetExponentTermWithValidData() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {0.1, 0.2};
        double exponentTerm = dist.getExponentTerm(testValues);
        double expected = 0.1764476;
        assertEquals(expected, exponentTerm, 1e-12);
    }

    @Test
    public void testGetExponentTermWithZeroCovarianceMatrix() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{0.0, 0.0}, {0.0, 0.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {0.1, 0.2};
        double exponentTerm = dist.getExponentTerm(testValues);
        double expected = FastMath.exp(-0.5 * FastMath.pow(0.1, 2) - 0.5 * FastMath.pow(0.2, 2));
        assertEquals(expected, exponentTerm, 1e-12);
    }

    @Test
    public void testGetExponentTermWithNegativeCovarianceMatrix() {
        final double[] means = {1.0, 2.0};
        final double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        final double[] testValues = {0.1, 0.2};
        double exponentTerm = dist.getExponentTerm(testValues);
        double expected = -1.0;
        assertEquals(expected, exponentTerm, 1e-12);
    }
}