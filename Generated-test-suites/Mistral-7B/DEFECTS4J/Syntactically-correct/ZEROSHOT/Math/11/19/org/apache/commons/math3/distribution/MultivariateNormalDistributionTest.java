import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultivariateNormalDistributionTest {

 @Test
 public void testConstructors() {
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 try {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, dist.getDimension());
 assertArrayEquals(means, dist.getMeans());
 assertEquals(covariances, dist.getCovariances().getData());
 } catch (SingularMatrixException e) {
 // expected
 } catch (DimensionMismatchException e) {
 // expected
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testSample() {
 final RandomGenerator rng = new Well19937c();
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 try {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covariances);
 final double[] sample = dist.sample();
 assertArrayEquals(means.length, sample.length);
 for (int i = 0; i < sample.length; i++) {
 assertEquals(Double.class, sample[i].getClass());
 }
 } catch (SingularMatrixException e) {
 // expected
 } catch (DimensionMismatchException e) {
 // expected
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testDensity() {
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 final double[] sample = {1.5, 2.5};
 assertEquals(0.0, dist.density(sample), 1e-8);
 final double[] sample2 = {1.0, 2.0};
 assertEquals(1.0 / (Math.sqrt(2 * Math.PI) * Math.sqrt(Math.PI)), dist.density(sample2), 1e-8);
 }

 @Test
 public void testGetStandardDeviations() {
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 final double[] std = dist.getStandardDeviations();
 assertEquals(2, std.length);
 assertEquals(1.0, std[0], 1e-8);
 assertEquals(1.0, std[1], 1e-8);
 }

 @Test
 public void testGetCovariances() {
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 final RealMatrix covMatrix = dist.getCovariances();
 final double[][] covMatrixData = covMatrix.getData();
 assertEquals(2, covMatrixData.length);
 assertEquals(2, covMatrixData[0].length);
 assertEquals(2, covMatrixData[1].length);
 assertEquals(1.0, covMatrixData[0][0], 1e-8);
 assertEquals(0.5, covMatrixData[0][1], 1e-8);
 assertEquals(0.5, covMatrixData[1][0], 1e-8);
 assertEquals(1.0, covMatrixData[1][1], 1e-8);
 }

 @Test
 public void testGetMeans() {
 final double[] means = new double[] {1.0, 2.0};
 final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, new double[0][0]);
 final double[] distMeans = dist.getMeans();
 assertArrayEquals(means, distMeans);
 }

 @Test
 public void testGetDimension() {
 final double[] means = new double[] {1.0, 2.0};
 final double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
 final MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 assertEquals(2, dist.getDimension());
 }
}