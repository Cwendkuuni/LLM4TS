import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionImpl;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 HypergeometricDistributionImpl expected = new HypergeometricDistributionImpl(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 Assert.assertEquals(expected, distribution);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativePopulationSize() {
 int populationSize = -1;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroPopulationSize() {
 int populationSize = 0;
 int numberOfSuccesses = 3;
 int sampleSize = 5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
 int populationSize = 10;
 int numberOfSuccesses = -3;
 int sampleSize = 5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroNumberOfSuccesses() {
 int populationSize = 10;
 int numberOfSuccesses = 0;
 int sampleSize = 5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeSampleSize() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = -5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroSampleSize() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 0;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
 int populationSize = 10;
 int numberOfSuccesses = 11;
 int sampleSize = 5;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
 int populationSize = 10;
 int numberOfSuccesses = 3;
 int sampleSize = 11;
 HypergeometricDistribution distribution = null;
 try {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 Assert.assertNull(distribution);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 int[] expectedProbabilities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
 for (int i = 0; i < 10; i++) {
 double actualProbability = distribution.cumulativeProbability(i);
 Assert.assertEquals(expectedProbabilities[i], actualProbability, 1e-12);
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 double[] expectedProbabilities = {0.0539583792438624, 0.127878126277821, 0.152637324973738, 0.152637324973738, 0.130446195213118, 0.101553433359701, 0.070967492166853, 0.044849574814651, 0.023286850743794, 0.008016633423102};
 for (int i = 0; i < 10; i++) {
 double actualProbability = distribution.probability(i);
 Assert.assertEquals(expectedProbabilities[i], actualProbability, 1e-12);
 }
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 double[] expectedProbabilities = {0.946041561326136, 0.927567445982912, 0.909163741573052, 0.890695223694534, 0.871986380952551, 0.852647632704443, 0.832513929744506, 0.811379049656106, 0.789069462685528, 0.765544959334532};
 for (int i = 0; i < 10; i++) {
 double actualProbability = 1.0 - distribution.upperCumulativeProbability(i);
 Assert.assertEquals(expectedProbabilities[i], actualProbability, 1e-12);
 }
 }

 @Test
 public void testGetters() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 Assert.assertEquals(populationSize, distribution.getPopulationSize());
 Assert.assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
 Assert.assertEquals(sampleSize, distribution.getSampleSize());
 }

 @Test
 public void testGetNumericalMean() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 Assert.assertEquals(3.0 * 5.0 / 10.0, distribution.getNumericalMean(), 1e-12);
 }

 @Test
 public void testGetNumericalVariance() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 double expectedVariance = (5.0 * 3.0 * (10.0 - 5.0) * (10.0 - 3.0) / (10.0 * 10.0 * (10.0 - 1.0)));
 Assert.assertEquals(expectedVariance, distribution.getNumericalVariance(), 1e-12);
 }

 @Test
 public void testGetSupportLowerBound() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 Assert.assertEquals(sampleSize + numberOfSuccesses - populationSize, distribution.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 Assert.assertEquals(numberOfSuccesses, distribution.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 final int populationSize = 10;
 final int numberOfSuccesses = 3;
 final int sampleSize = 5;
 final HypergeometricDistribution distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 Assert.assertTrue(distribution.isSupportConnected());
 }

}