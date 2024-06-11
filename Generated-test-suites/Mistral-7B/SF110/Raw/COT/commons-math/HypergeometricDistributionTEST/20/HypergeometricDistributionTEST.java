import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private static final int POPULATION_SIZE = 10;
 private static final int NUMBER_OF_SUCCESSSES = 5;
 private static final int SAMPLE_SIZE = 3;

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
   try {
     new HypergeometricDistribution(new Well19937c(), -1, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
     fail("Expected NotPositiveException");
   } catch (NotPositiveException e) {
     // Expected
   }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithZeroPopulationSize() {
   try {
     new HypergeometricDistribution(new Well19937c(), 0, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
     fail("Expected NotStrictlyPositiveException");
   } catch (NotStrictlyPositiveException e) {
     // Expected
   }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativeNumberOfSuccesses() {
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, -1, SAMPLE_SIZE);
     fail("Expected NotPositiveException");
   } catch (NotPositiveException e) {
     // Expected
   }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithZeroNumberOfSuccesses() {
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, 0, SAMPLE_SIZE);
     fail("Expected NotStrictlyPositiveException");
   } catch (NotStrictlyPositiveException e) {
     // Expected
   }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativeSampleSize() {
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, -1);
     fail("Expected NotPositiveException");
   } catch (NotPositiveException e) {
     // Expected
   }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, POPULATION_SIZE + 1, SAMPLE_SIZE);
     fail("Expected NumberIsTooLargeException");
   } catch (NumberIsTooLargeException e) {
     // Expected
   }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
   try {
     new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, POPULATION_SIZE + 1);
     fail("Expected NumberIsTooLargeException");
   } catch (NumberIsTooLargeException e) {
     // Expected
   }
 }

 @Test
 public void testCumulativeProbabilityWithinDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int[] domain = distribution.getDomain();
   assertEquals(distribution.cumulativeProbability(domain[0]), 0.0, 0.001);
   for (int i = domain[0]; i <= domain[1]; i++) {
     assertEquals(distribution.cumulativeProbability(i), distribution.probability(i), 0.001);
   }
   assertEquals(distribution.cumulativeProbability(domain[1]), 1.0, 0.001);
 }

 @Test
 public void testProbabilityWithinDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int[] domain = distribution.getDomain();
   for (int i = domain[0]; i <= domain[1]; i++) {
     assertEquals(distribution.probability(i), distribution.cumulativeProbability(i + 1) - distribution.cumulativeProbability(i), 0.001);
   }
 }

 @Test
 public void testCumulativeProbabilityOutsideDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int[] domain = distribution.getDomain();
   assertEquals(distribution.cumulativeProbability(domain[0] - 1), 0.0, 0.001);
   assertEquals(distribution.cumulativeProbability(domain[1] + 1), 1.0, 0.001);
 }

 @Test
 public void testUpperCumulativeProbabilityWithinDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int[] domain = distribution.getDomain();
   for (int i = domain[0]; i <= domain[1]; i++) {
     assertEquals(1.0 - distribution.cumulativeProbability(i), distribution.upperCumulativeProbability(i), 0.001);
   }
 }

 @Test
 public void testUpperCumulativeProbabilityOutsideDomain() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   int[] domain = distribution.getDomain();
   assertEquals(1.0, distribution.upperCumulativeProbability(domain[0] - 1), 0.001);
   assertEquals(0.0, distribution.upperCumulativeProbability(domain[1] + 1), 0.001);
 }

 @Test
 public void testNumericalMean() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   assertEquals(distribution.getNumericalMean(), (double)SAMPLE_SIZE * NUMBER_OF_SUCCESSSES / POPULATION_SIZE, 0.001);
 }

 @Test
 public void testNumericalVariance() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   assertEquals(distribution.getNumericalVariance(), (SAMPLE_SIZE * NUMBER_OF_SUCCESSSES * (POPULATION_SIZE - SAMPLE_SIZE) * (POPULATION_SIZE - NUMBER_OF_SUCCESSSES) / (POPULATION_SIZE * POPULATION_SIZE * (POPULATION_SIZE - 1.0))), 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   assertEquals(distribution.getSupportLowerBound(), FastMath.max(0, POPULATION_SIZE + NUMBER_OF_SUCCESSSES - SAMPLE_SIZE));
 }

 @Test
 public void testGetSupportUpperBound() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   assertEquals(distribution.getSupportUpperBound(), FastMath.min(NUMBER_OF_SUCCESSSES, SAMPLE_SIZE));
 }

 @Test
 public void testIsSupportConnected() {
   HypergeometricDistribution distribution = new HypergeometricDistribution(new Well19937c(), POPULATION_SIZE, NUMBER_OF_SUCCESSSES, SAMPLE_SIZE);
   assertTrue(distribution.isSupportConnected());
 }
}