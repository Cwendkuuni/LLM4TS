import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;
 private RandomGenerator randomGenerator;

 @Before
 public void setUp() {
 randomGenerator = new Well19937c();
 try {
 distribution = new HypergeometricDistribution(randomGenerator, 10, 5, 15);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 e.printStackTrace();
 fail("Failed to initialize distribution");
 }
 }

 @Test
 public void testConstructorWithValidParameters() {
 assertEquals(10, distribution.getPopulationSize());
 assertEquals(5, distribution.getNumberOfSuccesses());
 assertEquals(15, distribution.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithInvalidPopulationSize() {
 try {
 distribution = new HypergeometricDistribution(randomGenerator, -5, 5, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected NotPositiveException
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithInvalidNumberOfSuccesses() {
 try {
 distribution = new HypergeometricDistribution(randomGenerator, 10, -5, 5);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // expected NotStrictlyPositiveException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidSampleSize() {
 try {
 distribution = new HypergeometricDistribution(randomGenerator, 10, 5, 20);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // expected NumberIsTooLargeException
 }
 }

 @Test
 public void testNumberOfSuccesses() {
 assertEquals(5, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testPopulationSize() {
 assertEquals(10, distribution.getPopulationSize());
 }

 @Test
 public void testSampleSize() {
 assertEquals(15, distribution.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 for (int i = 0; i <= distribution.getSupportUpperBound(); i++) {
 double cumulativeProbability = distribution.cumulativeProbability(i);
 double expectedCumulativeProbability = SaddlePointExpansion.logBinomialProbability(i, 5, distribution.getSampleSize() / 10.0, (10 - distribution.getSampleSize()) / 10.0);
 assertEquals(expectedCumulativeProbability, cumulativeProbability, 1e-10);
 }
 }

 @Test
 public void testProbability() {
 for (int i = 0; i <= distribution.getSupportUpperBound(); i++) {
 double probability = distribution.probability(i);
 double expectedProbability = SaddlePointExpansion.logBinomialProbability(i, 5, distribution.getSampleSize() / 10.0, (10 - distribution.getSampleSize()) / 10.0);
 assertEquals(expectedProbability, probability, 1e-10);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 for (int i = 0; i <= distribution.getSupportUpperBound(); i++) {
 double upperCumulativeProbability = distribution.upperCumulativeProbability(i);
 double expectedUpperCumulativeProbability = SaddlePointExpansion.logBinomialProbability(i + 1, 5, distribution.getSampleSize() / 10.0, (10 - distribution.getSampleSize()) / 10.0);
 assertEquals(expectedUpperCumulativeProbability, 1.0 - upperCumulativeProbability, 1e-10);
 }
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = distribution.getNumericalMean();
 assertEquals(5.0, numericalMean, 1e-10);
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = distribution.getNumericalVariance();
 double expectedNumericalVariance = 0.0392857142857143;
 assertEquals(expectedNumericalVariance, numericalVariance, 1e-10);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetStatistics() {
 distribution.getStatistics();
 // expected UnsupportedOperationException
 }

}