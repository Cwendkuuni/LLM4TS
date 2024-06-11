import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.apache.commons.math3.util.Precision.abs;

@RunWith(JUnit4.class)
public class HypergeometricDistributionTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(-1, 1, 1);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(1, -1, 1);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, -1);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithZeroPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(0, 1, 1);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithZeroNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(1, 0, 1);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithZeroSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(1, 1, 0);
 }

 @Test
 public void testHypergeometricDistributionConstructor() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertEquals(populationSize, dist.getPopulationSize());
 assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
 assertEquals(sampleSize, dist.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double cp = dist.cumulativeProbability(x);
 double np = dist.probability(x);
 assertEquals(cp, np, 1e-12);
 }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = dist.probability(x);
 double npSum = 0;
 for (int i = domain[0]; i <= x; i++) {
 npSum += p;
 p = dist.probability(i);
 }
 assertEquals(npSum, 1.0, 1e-12);
 }
 }

 @Test
 public void testHypergeometricDistributionGetDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain();
 assertEquals(2, domain.length);
 assertTrue(domain[0] >= 0);
 assertTrue(domain[1] <= populationSize);
 }

 @Test
 public void testHypergeometricDistributionGetNumberOfSuccesses() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
 }

 @Test
 public void testHypergeometricDistributionGetPopulationSize() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertEquals(populationSize, dist.getPopulationSize());
 }

 @Test
 public void testHypergeometricDistributionGetSampleSize() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertEquals(sampleSize, dist.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionNumericalMean() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertEquals(dist.getNumericalMean(), (double)sampleSize * numberOfSuccesses / populationSize, 1e-12);
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 double numericalVariance = dist.getNumericalVariance();
 double analyticalVariance = ((double)sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1.0));
 assertEquals(analyticalVariance, numericalVariance, 1e-12);
 }

 @Test
 public void testHypergeometricDistributionSupportBoundaries() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int supportLowerBound = dist.getSupportLowerBound();
 int supportUpperBound = dist.getSupportUpperBound();
 int expectedSupportLowerBound = populationSize + numberOfSuccesses - sampleSize;
 int expectedSupportUpperBound = numberOfSuccesses;
 assertEquals(expectedSupportLowerBound, supportLowerBound);
 assertEquals(expectedSupportUpperBound, supportUpperBound);
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbabilityWithInvalidArguments() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain();
 for (int x = domain[0] - 1; x < domain[0]; x++) {
 double cp = dist.cumulativeProbability(x);
 assertEquals(Double.NaN, cp);
 }
 for (int x = domain[1] + 1; x >= domain[1]; x--) {
 double cp = dist.cumulativeProbability(x);
 assertEquals(Double.NaN, cp);
 }
 }

 @Test
 public void testHypergeometricDistributionProbabilityWithInvalidArguments() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 int[] domain = dist.getDomain();
 for (int x = domain[0] - 1; x < domain[0]; x++) {
 double p = dist.probability(x);
 assertEquals(0.0, p, 1e-12);
 }
 for (int x = domain[1] + 1; x >= domain[1]; x--) {
 double p = dist.probability(x);
 assertEquals(0.0, p, 1e-12);
 }
 }

 @Test
 public void testCalculateNumericalVariance() {
 int populationSize = 10;
 int numberOfSuccesses = 2;
 int sampleSize = 5;
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 double numericalVariance = dist.calculateNumericalVariance();
 double analyticalVariance = ((double)sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1.0));
 assertEquals(analyticalVariance, numericalVariance, 1e-12);
 }
}