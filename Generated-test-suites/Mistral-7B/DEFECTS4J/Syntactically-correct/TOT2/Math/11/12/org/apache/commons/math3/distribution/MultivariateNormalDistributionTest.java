import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, mnd.getDimension());
        assertEquals(means, mnd.getMeans());
        RealMatrix covarianceMatrix = mnd.getCovariances();
        assertEquals(covariances, covarianceMatrix.getData());
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] stdDeviations = mnd.getStandardDeviations();
        assertEquals(means.length, stdDeviations.length);
        assertEquals(1.0, stdDeviations[0], 0.001);
        assertEquals(0.70710678118655, stdDeviations[1], 0.001);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = {1.5, 2.2};
        double expectedResult = Math.exp(-1.0) * Math.sqrt(2 * Math.PI * 2 * Math.PI) / (2 * Math.PI);
        expectedResult *= Math.exp(-0.5 * ((testValues[0] - means[0]) * ((testValues[0] - means[0]) / 1.0)
                + (testValues[1] - means[1]) * ((testValues[1] - means[1]) / 1.0)));
        double actualResult = mnd.density(testValues);
        assertEquals(expectedResult, actualResult, 0.001);
    }

    @Test
    public void testSample() throws DimensionMismatchException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        RandomGenerator rg = mnd.getRandomGenerator();
        double[] testSample = mnd.sample();
        for (int i = 0; i < testSample.length; i++) {
            double expectedMean = means[i];
            double expectedVariance = covariances[i][i];
            double expectedStandardDeviation = Math.sqrt(covariances[i][i]);
            double actualSampleValue = testSample[i];
            double actualMean = mnd.getMeans()[i];
            double actualVariance = covariances[i][i];
            double actualStandardDeviation = mnd.getStandardDeviations()[i];
            double tolerance = 0.001;
            assertTrue(expectedMean >= actualMean - tolerance && expectedMean <= actualMean + tolerance);
            assertTrue(expectedVariance >= actualVariance - tolerance && expectedVariance <= actualVariance + tolerance);
            assertTrue(expectedStandardDeviation >= actualStandardDeviation - tolerance && expectedStandardDeviation <= actualStandardDeviation + tolerance);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidMeansLength() throws DimensionMismatchException {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidCovariancesDimensions() throws DimensionMismatchException, SingularMatrixException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularCovarianceMatrix() throws SingularMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.0}, {0.0, 0.0}};
        new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeEigenvalueCovarianceMatrix() throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(means, covariances);
    }
}