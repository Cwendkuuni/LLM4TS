import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultivariateNormalDistributionTest {

 @Test
 public void testMultivariateNormalDistribution() {
 // Test construction with valid input
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.5, 0.5}, {0.5, 2.0}};
 MultivariateNormalDistribution mvd = new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 // Test getters
 assertEquals(means.length, mvd.getDimension());
 assertArrayEquals(means, mvd.getMeans());
 RealMatrix covarianceMatrix = mvd.getCovariances();
 assertEquals(means.length, covarianceMatrix.getRowDimension());
 assertEquals(means.length, covarianceMatrix.getColumnDimension());
 // Test density
 double[] testValues = {1.5, 2.5};
 double density = mvd.density(testValues);
 double expectedDensity = 0.11278222239233115;
 assertEquals(expectedDensity, density, 1e-8);
 // Test sample
 double[] sample = mvd.sample();
 double[] expectedSample = {2.2293055584331246, 3.236127949267821};
 assertArrayEquals(expectedSample, sample, 1e-8);
 // Test sample covariance matrix
 RealMatrix sampleCovarianceMatrix = new Array2DRowRealMatrix(10, mvd.getDimension());
 for (int i = 0; i < 10; i++) {
 sampleCovarianceMatrix.setRow(i, mvd.sample());
 }
 RealMatrix expectedSampleCovarianceMatrix = covarianceMatrix.copy();
 assertEquals(expectedSampleCovarianceMatrix, sampleCovarianceMatrix.transpose().multiply(sampleCovarianceMatrix));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultivariateNormalDistributionInvalidMeansLength() {
 double[] means = {1.0};
 double[][] covariances = {{1.5, 0.5}, {0.5, 2.0}};
 new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultivariateNormalDistributionInvalidCovariancesRows() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.5, 0.5, 0.5}, {0.5, 2.0, 0.5}};
 new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultivariateNormalDistributionInvalidCovariancesColumns() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.5, 0.5}, {0.5, 2.0, 0.5}};
 new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 }

 @Test(expected = SingularMatrixException.class)
 public void testMultivariateNormalDistributionSingularCovarianceMatrix() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.5, 0.5}, {0.5, 0.0}};
 new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testMultivariateNormalDistributionNegativeEigenvalues() {
 double[] means = {1.0, 2.0};
 double[][] covariances = {{1.5, -0.5}, {-0.5, 2.0}};
 new MultivariateNormalDistribution(new RandomGenerator(), means, covariances);
 }
}