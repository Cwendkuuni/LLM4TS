import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private final static double EPSILON = 1e-10;

 @Test
 public void testMultivariateNormalDistributionCreation() {
 double[] means = new double[]{0.0, 0.0};
 double[][] covariances = new double[][]{{1.0, 0.5}, {0.5, 1.0}};
 try {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 assertEquals(means.length, dist.getDimension());
 assertEquals(means, dist.getMeans());
 RealMatrix covarianceMatrix = dist.getCovariances();
 assertArrayEquals(covariances, covarianceMatrix.getData(), EPSILON);
 } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
 fail("Exception should not be thrown.");
 }
 }

 @Test
 public void testGetStandardDeviations() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 double[] std = dist.getStandardDeviations();
 assertEquals(2.0, std.length, 0.0);
 assertEquals(1.0, std[0], EPSILON);
 assertEquals(1.0, std[1], EPSILON);
 }

 @Test
 public void testDensity() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 double[] values = new double[]{1.0, 1.0};
 double density = dist.density(values);
 assertEquals(0.27878116, density, EPSILON);
 }

 @Test
 public void testSample() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 double[] sample = dist.sample();
 assertTrue(sample.length > 0);
 for (int i = 0; i < sample.length; i++) {
 assertTrue(sample[i] > -1000.0 && sample[i] < 1000.0);
 }
 }

 @Test
 public void testSampleMatrix() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 int rows = 10;
 int cols = 5;
 RealMatrix sampleMatrix = new Array2DRowRealMatrix(rows, cols);
 for (int i = 0; i < rows; i++) {
 double[] sample = dist.sample();
 sampleMatrix.setRowVector(i, sample);
 }
 RealVector expectedMean = dist.getMeans();
 RealVector actualMean = sampleMatrix.getColumnVector(0);
 assertEquals(expectedMean.getDimension(), actualMean.getDimension());
 for (int i = 0; i < expectedMean.getDimension(); i++) {
 assertEquals(expectedMean.getEntry(i), actualMean.getEntry(i), EPSILON);
 }
 }

 @Test
 public void testSampleMatrixWithLargeDimension() {
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new double[]{0.0}, new double[][]{{1.0}});
 int rows = 1000;
 int cols = 100;
 RealMatrix sampleMatrix = new Array2DRowRealMatrix(rows, cols);
 for (int i = 0; i < rows; i++) {
 double[] sample = dist.sample();
 sampleMatrix.setRowVector(i, sample);
 }
 double mean = dist.getMeans()[0];
 RealVector actualMean = sampleMatrix.columnMultiplicative(new Array2DRowRealMatrix(1, cols).mapMultiply(new RealVector(new double[]{mean})));
 RealVector expectedMean = dist.getMeans();
 assertEquals(expectedMean.getDimension(), actualMean.getDimension());
 for (int i = 0; i < expectedMean.getDimension(); i++) {
 assertEquals(expectedMean.getEntry(i), actualMean.getEntry(i), EPSILON);
 }
 }

 @Test
 public void testSampleWithNegativeCovariance() {
 try {
 double[] means = new double[]{0.0, 0.0};
 double[][] covariances = new double[][]{{-1.0, 0.5}, {0.5, -1.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 fail("Exception should be thrown.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testSampleWithSingularCovariance() {
 try {
 double[] means = new double[]{0.0, 0.0};
 double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 0.0}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 fail("Exception should be thrown.");
 } catch (SingularMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testSampleWithInconsistentDimension() {
 try {
 double[] means = new double[]{0.0};
 double[][] covariances = new double[][]{{1.0, 0.5}};
 MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
 fail("Exception should be thrown.");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }
}