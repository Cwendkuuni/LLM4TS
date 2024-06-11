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
 private HypergeometricDistribution distribution;

 @Parameterized.Parameters(name = "{0}, {1}, {2}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 { 10, 3, 4 },
 { 100, 50, 10 },
 { 1000, 500, 100 },
 { 10000, 5000, 1000 },
 { 100000, 50000, 10000 }
 });
 }

 public HypergeometricDistributionTest(int populationSize, int numberOfSuccesses, int sampleSize) {
 this.populationSize = populationSize;
 this.numberOfSuccesses = numberOfSuccesses;
 this.sampleSize = sampleSize;
 }

 @Test
 public void testConstruction() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
 }

 @Test(expected = NotPositiveException.class)
 public void testPopulationSizeNegative() {
 distribution = new HypergeometricDistribution(-1, numberOfSuccesses, sampleSize);
 }

 @Test(expected = NotPositiveException.class)
 public void testNumberOfSuccessesNegative() {
 distribution = new HypergeometricDistribution(populationSize, -1, sampleSize);
 }

 @Test(expected = NotPositiveException.class)
 public void testSampleSizeNegative() {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, -1);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testPopulationSizeZero() {
 distribution = new HypergeometricDistribution(0, numberOfSuccesses, sampleSize);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNumberOfSuccessesZero() {
 distribution = new HypergeometricDistribution(populationSize, 0, sampleSize);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesTooLarge() {
 distribution = new HypergeometricDistribution(populationSize, populationSize + 1, sampleSize);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeTooLarge() {
 distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, populationSize + 1);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 assertEquals(numberOfSuccesses, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 assertEquals(populationSize, distribution.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 assertEquals(sampleSize, distribution.getSampleSize());
 }

 @Test
 public void testGetDomain() {
 int[] domain = distribution.getDomain();
 int lowerBound = distribution.getSupportLowerBound();
 int upperBound = distribution.getSupportUpperBound();

 assertTrue(Arrays.equals(new int[] { lowerBound, upperBound }, domain));
 }

 @Test
 public void testCumulativeProbability() {
 for (int i = 0; i <= populationSize; i++) {
 double prob = distribution.cumulativeProbability(i);
 if (i < distribution.getLowerDomain()) {
 assertEquals(0.0, prob, 0.001);
 } else if (i > distribution.getUpperDomain()) {
 assertEquals(1.0, prob, 0.001);
 } else {
 assertEquals(this.innerCumulativeProbability(i), prob, 0.001);
 }
 }
 }

 private double innerCumulativeProbability(int x) {
 double cumulativeProbability = 0.0;
 for (int i = 0; i < x; i++) {
 cumulativeProbability += distribution.probability(i);
 }
 return cumulativeProbability;
 }

 @Test
 public void testProbability() {
 for (int i = distribution.getLowerDomain(); i <= populationSize; i++) {
 double prob = distribution.probability(i);
 assertEquals(this.probability(i), prob, 0.001);
 }
 }

 private double probability(int x) {
 double nCrX = Math.comb(numberOfSuccesses, x) / Math.pow(populationSize, x) / Math.pow(populationSize - numberOfSuccesses, populationSize - x);
 double p = (double) sampleSize / populationSize;
 double q = (double) (populationSize - sampleSize) / populationSize;
 return nCrX * Math.pow(p, x) * Math.pow(q, populationSize - x);
 }

 @Test
 public void testUpperCumulativeProbability() {
 for (int i = 0; i <= populationSize; i++) {
 double prob = distribution.upperCumulativeProbability(i);
 if (i < distribution.getLowerDomain()) {
 assertEquals(1.0, prob, 0.001);
 } else if (i > distribution.getUpperDomain()) {
 assertEquals(0.0, prob, 0.001);
 } else {
 assertEquals(1.0 - this.innerCumulativeProbability(i), prob, 0.001);
 }
 }
 }

 @Test
 public void testNumericalMean() {
 double numericalMean = distribution.getNumericalMean();
 double expectedMean = (double) sampleSize * numberOfSuccesses / populationSize;
 assertEquals(expectedMean, numericalMean, 0.001);
 }

 @Test
 public void testNumericalVariance() {
 double numericalVariance = distribution.getNumericalVariance();
 double expectedVariance = this.calculateNumericalVariance();
 assertEquals(expectedVariance, numericalVariance, 0.001);
 }

 private double calculateNumericalVariance() {
 double N = (double) populationSize;
 double m = (double) numberOfSuccesses;
 double n = (double) sampleSize;
 return n * m * (N - n) * (N - m) / (N * N * (N - 1.0));
 }
}