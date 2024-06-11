import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;
 private Well19937c randomGenerator;

 @Before
 public void setUp() {
 randomGenerator = new Well19937c();
 try {
 distribution = new HypergeometricDistribution(randomGenerator, 10, 5, 5);
 } catch (NotPositiveException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testConstruction() {
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(5, distribution.getSampleSize());
 }

 @Test
 public void testGetLowerDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 5;
 int sampleSize = 5;
 int[] domain = distribution.getHypergeometricDistribution().getDomain(populationSize, numberOfSuccesses, sampleSize);
 int lowerDomain = distribution.getLowerDomain(populationSize, numberOfSuccesses, sampleSize);
 assertEquals(domain[0], lowerDomain);
 }

 @Test
 public void testGetUpperDomain() {
 int populationSize = 10;
 int numberOfSuccesses = 5;
 int sampleSize = 5;
 int[] domain = distribution.getHypergeometricDistribution().getDomain(populationSize, numberOfSuccesses, sampleSize);
 int upperDomain = distribution.getUpperDomain(populationSize, numberOfSuccesses, sampleSize);
 assertEquals(domain[1], upperDomain);
 }

 @Test
 public void testCumulativeProbability() {
 final int[] domain = distribution.getDomain(this.distribution.getPopulationSize(), this.distribution.getNumberOfSuccesses(), this.distribution.getSampleSize());
 for (int i = domain[0]; i <= domain[1]; i++) {
 double expected = distribution.cumulativeProbability(i);
 double actual = SaddlePointExpansion.logBinomialProbability(i, this.distribution.getNumberOfSuccesses(), this.distribution.sampleSize / this.distribution.getPopulationSize(), (this.distribution.getPopulationSize() - this.distribution.sampleSize) / this.distribution.getPopulationSize());
 double difference = Math.abs(expected - actual);
 assertTrue("Cumulative probability does not match for x = " + i, difference <= 1e-10);
 }
 }

 @Test
 public void testProbability() {
 final int[] domain = distribution.getDomain(this.distribution.getPopulationSize(), this.distribution.getNumberOfSuccesses(), this.distribution.getSampleSize());
 for (int i = domain[0]; i <= domain[1]; i++) {
 double expected = distribution.probability(i);
 double actual = SaddlePointExpansion.logBinomialProbability(i, this.distribution.getNumberOfSuccesses(), this.distribution.sampleSize / this.distribution.getPopulationSize(), (this.distribution.getPopulationSize() - this.distribution.sampleSize) / this.distribution.getPopulationSize());
 double difference = Math.abs(expected - actual);
 assertTrue("Probability does not match for x = " + i, difference <= 1e-10);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 final int[] domain = distribution.getDomain(this.distribution.getPopulationSize(), this.distribution.getNumberOfSuccesses(), this.distribution.getSampleSize());
 for (int i = domain[0]; i <= domain[1]; i++) {
 double expected = 1 - distribution.cumulativeProbability(i);
 double actual = distribution.upperCumulativeProbability(i);
 double difference = Math.abs(expected - actual);
 assertTrue("Upper cumulative probability does not match for x = " + i, difference <= 1e-10);
 }
 }

 @Test
 public void testNumericalMean() {
 double numericalMean = distribution.getNumericalMean();
 double expected = this.distribution.getSampleSize() * this.distribution.getNumberOfSuccesses() / this.distribution.getPopulationSize();
 assertEquals("Numerical mean does not match", expected, numericalMean, 1e-10);
 }

 @Test
 public void testNumericalVariance() {
 double numericalVariance = distribution.getNumericalVariance();
 double expected = this.distribution.getSampleSize() * this.distribution.getNumberOfSuccesses() * (this.distribution.getPopulationSize() - this.distribution.getSampleSize()) * (this.distribution.getPopulationSize() - this.distribution.getNumberOfSuccesses()) / (this.distribution.getPopulationSize() * this.distribution.getPopulationSize() * (this.distribution.getPopulationSize() - 1));
 assertEquals("Numerical variance does not match", expected, numericalVariance, 1e-10);
 }
}