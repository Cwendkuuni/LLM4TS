import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HypergeometricDistributionTest {

 private int populationSize;
 private int numberOfSuccesses;
 private int sampleSize;
 private HypergeometricDistribution dist;

 @Parameterized.Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 { 10, 4, 3 },
 { 20, 15, 5 },
 { 50, 25, 10 },
 { 100, 50, 15 }
 });
 }

 @Parameterized.Constructor
 public HypergeometricDistributionTest(int populationSize, int numberOfSuccesses, int sampleSize) {
 this.populationSize = populationSize;
 this.numberOfSuccesses = numberOfSuccesses;
 this.sampleSize = sampleSize;
 try {
 dist = new HypergeometricDistribution(new Well19937c(), populationSize, numberOfSuccesses, sampleSize);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 fail("Failed to instantiate HypergeometricDistribution: " + e.getMessage());
 }
 }

 @Test
 public void testGetPopulationSize() {
 assertEquals(populationSize, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 assertEquals(sampleSize, dist.getSampleSize());
 }

 @Test
 public void testGetNumberOfSuccesses() {
 assertEquals(numberOfSuccesses, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetLowerDomain() {
 int[] domain = dist.getDomain();
 assertEquals(numberOfSuccesses + sampleSize - populationSize, domain[0]);
 }

 @Test
 public void testGetUpperDomain() {
 int[] domain = dist.getDomain();
 assertEquals(Math.min(numberOfSuccesses, sampleSize), domain[1]);
 }

 @Test
 public void testGetSupportLowerBound() {
 int supportLowerBound = dist.getSupportLowerBound();
 assertEquals(populationSize + numberOfSuccesses - sampleSize, supportLowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 int supportUpperBound = dist.getSupportUpperBound();
 assertEquals(Math.min(numberOfSuccesses, sampleSize), supportUpperBound);
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testCumulativeProbability() {
 for (int i = 0; i <= sampleSize; i++) {
 double probability = dist.cumulativeProbability(i);
 double expected = SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses, sampleSize / (double) populationSize, (populationSize - sampleSize) / (double) populationSize);
 double error = Math.abs(probability - expected);
 System.out.printf("%d: %f (expected: %f, error: %f)\n", i, probability, expected, error);
 assertTrue(error < 1e-6);
 }
 }

 @Test
 public void testProbability() {
 for (int i = 0; i <= sampleSize; i++) {
 double probability = dist.probability(i);
 double expected = SaddlePointExpansion.logBinomialProbability(i, numberOfSuccesses, sampleSize / (double) populationSize, (populationSize - sampleSize) / (double) populationSize);
 double error = Math.abs(probability - expected);
 System.out.printf("%d: %f (expected: %f, error: %f)\n", i, probability, expected, error);
 assertEquals(expected, probability, 1e-6);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 for (int i = 1; i <= sampleSize; i++) {
 double probability = dist.upperCumulativeProbability(i);
 double expected = 1.0 - dist.cumulativeProbability(i - 1);
 double error = Math.abs(probability - expected);
 System.out.printf("%d: %f (expected: %f, error: %f)\n", i, probability, expected, error);
 assertTrue(error < 1e-6);
 }
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = dist.getNumericalMean();
 double expected = (double) numberOfSuccesses * sampleSize / populationSize;
 assertEquals(expected, numericalMean, 1e-6);
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = dist.getNumericalVariance();
 double expected = (sampleSize * numberOfSuccesses * (populationSize - sampleSize) * (populationSize - numberOfSuccesses)) / (populationSize * populationSize * (populationSize - 1.0));
 assertEquals(expected, numericalVariance, 1e-6);
 }
}