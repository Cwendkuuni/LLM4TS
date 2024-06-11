package org.apache.commons.math3.distribution;

import org.apache.commons.math3.LinearAlgebraTests;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealArray2DRowRelation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalDistributionTest {

 private MultivariateNormalDistribution dist;
 private double[][] covariances = {{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}};
 private double[] means = {0.0, 2.0, 3.0};
 private RealMatrix expectedCovarianceMatrix;

 @Before
 public void setUp() {
 try {
 dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
 expectedCovarianceMatrix = new Array2DRowRealMatrix(covariances);
 } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testDensity() {
 final double[] values = new double[]{0.0, 3.0, 4.0};
 final double expectedDensity = FastMath.pow(2 * FastMath.PI, -3.0) * FastMath.pow(expectedCovarianceMatrix.determinant(), -0.5) * getExponentTerm(values);
 assertEquals(expectedDensity, dist.density(values), 1e-12);
 }

 @Test
 public void testStandardDeviations() {
 final double[] stdDeviations = dist.getStandardDeviations();
 for (int i = 0; i < stdDeviations.length; i++) {
 assertEquals(Math.sqrt(expectedCovarianceMatrix.getData()[i][i]), stdDeviations[i], 1e-12);
 }
 }

 @Test
 public void testSample() {
 final double[] sample = dist.sample();
 for (int i = 0; i < sample.length; i++) {
 assertEquals(FastMath.round(sample[i] * 1000) / 1000.0, means[i], 1e-6);
 }
 }

 @Test
 public void testGetMeans() {
 final double[] expectedMeans = MathArrays.copyOf(means);
 assertArrayEquals(expectedMeans, dist.getMeans(), 1e-12);
 }

 @Test
 public void testGetCovariances() {
 final RealMatrix expectedCovariances = expectedCovarianceMatrix.copy();
 RealMatrix actualCovariances = dist.getCovariances();
 RealArray2RowRelation comparison = new RealArray2RowRelation(expectedCovariances, actualCovariances);
 assertTrue(comparison.isIdentity());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCovariancesDifferentDimensions() {
 final double[][] covariancesDifferentDimensions = {{1.0, 0.5}, {0.5}};
 try {
 dist = new MultivariateNormalDistribution(new Well19937c(), means, covariancesDifferentDimensions);
 } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
 // Test passed
 } finally {
 fail();
 }
 }

 @Test(expected = SingularMatrixException.class)
 public void testCovarianceSingular() {
 final double[][] covariancesSingular = {{1.0, 0.5}, {0.5, 0.0}};
 try {
 dist = new MultivariateNormalDistribution(new Well19937c(), means, covariancesSingular);
 } catch (SingularMatrixException e) {
 // Test passed
 } finally {
 fail();
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testCovarianceNonPositiveDefinite() {
 final double[][] covariancesNonPositiveDefinite = {{1.0, -0.5}, {-0.5, 2.0}};
 try {
 dist = new MultivariateNormalDistribution(new Well19937c(), means, covariancesNonPositiveDefinite);
 } catch (SingularMatrixException | DimensionMismatchException | NonPositiveDefiniteMatrixException e) {
 // Test passed
 } finally {
 fail();
 }
 }

 private double[] subtract(double[] a, double[] b) {
 final double[] result = new double[a.length];
 for (int i = 0; i < a.length; i++) {
 result[i] = a[i] - b[i];
 }
 return result;
 }

 private double getExponentTerm(double[] values) {
 final MultivariateNormalDistribution multivariateNormalDistribution = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
 final double[] centered = subtract(values, multivariateNormalDistribution.getMeans());
 final RealMatrix inverse = multivariateNormalDistribution.getCovarianceMatrixInverse();
 final double[] preMultiplied = inverse.operate(centered);
 double sum = 0;
 for (int i = 0; i < preMultiplied.length; i++) {
 sum += preMultiplied[i] * centered[i];
 }
 return FastMath.exp(-0.5 * sum);
 }
}