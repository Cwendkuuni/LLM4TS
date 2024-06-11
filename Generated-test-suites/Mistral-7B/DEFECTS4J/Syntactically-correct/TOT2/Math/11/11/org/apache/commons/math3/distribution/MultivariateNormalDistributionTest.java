import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private static final double EPSILON = 1e-12;

 @Test
 public void testMultivariateNormalDistributionConstructor() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{}, new double[][]{});
 assertTrue(dist instanceof MultivariateNormalDistribution);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultivariateNormalDistributionConstructorInvalidMeansDimension() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{1.0}, new double[][]{new double[]{1.0, 2.0}, new double[]{3.0}});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultivariateNormalDistributionConstructorInvalidCovariancesDimension() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{1.0}, new double[][]{new double[]{1.0}, new double[]{1.0, 2.0}});
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testMultivariateNormalDistributionConstructorNegativeCovariances() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{1.0}, new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
 }

 @Test
 public void testGetMeans() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 RealVector means = dist.getMeans();
 assertArrayEquals(new double[]{1.0, 2.0}, means.toArray(), EPSILON);
 }

 @Test
 public void testGetCovariances() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 RealMatrix covariances = dist.getCovariances();
 RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 assertMatrixEquals(expectedCovariances, covariances);
 }

 @Test
 public void testDensity() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 double[] testValues = new double[]{1.0, 1.0};
 double result = dist.density(testValues);
 double expectedDensity = 0.39894228040143272;
 assertEquals(expectedDensity, result, EPSILON);
 }

 @Test
 public void testSample() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 double[] samples = dist.sample();
 assertArrayEquals(new double[]{0.0, 0.0}, samples, EPSILON);
 }

 @Test
 public void testSampleDifferentValues() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 double[] samples = dist.sample();
 assertArrayNotEquals(new double[]{0.0, 0.0}, samples, EPSILON);
 }

 @Test
 public void testSampleSameValueMultipleTimes() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 double[] sampleValue = dist.sample();
 double[] samples = new double[10][];
 for (int i = 0; i < 10; i++) {
 samples[i] = sampleValue.clone();
 }
 for (int i = 0; i < samples.length; i++) {
 assertArrayEquals(sampleValue, samples[i], EPSILON);
 }
 }

 @Test
 public void testGetStandardDeviations() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
 {1.0, 0.0},
 {0.0, 1.0}
 });
 double[] stdDeviations = dist.getStandardDeviations();
 assertArrayEquals(new double[]{1.0, 1.0}, stdDeviations, EPSILON);
 }
}