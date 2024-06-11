import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.stat.Stats;
import org.junit.Test;
import org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
   try {
     distribution = new HypergeometricDistribution(0, 1, 1);
   } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // expected exception
   }
   fail("Should have thrown NotPositiveException");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNonStrictlyPositiveNumberOfSuccesses() {
   try {
     distribution = new HypergeometricDistribution(10, 0, 1);
   } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // expected exception
   }
   fail("Should have thrown NotStrictlyPositiveException");
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNonPositiveSampleSize() {
   try {
     distribution = new HypergeometricDistribution(10, 5, -1);
   } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // expected exception
   }
   fail("Should have thrown NotPositiveException");
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
   try {
     distribution = new HypergeometricDistribution(10, 11, 1);
   } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // expected exception
   }
   fail("Should have thrown NumberIsTooLargeException");
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
   try {
     distribution = new HypergeometricDistribution(10, 5, 11);
   } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
     // expected exception
   }
   fail("Should have thrown NumberIsTooLargeException");
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   int[] domain = distribution.getDomain();
   for (int i = domain[0]; i <= domain[1]; i++) {
     double expected = (double) i / 10.0;
     double actual = distribution.cumulativeProbability(i);
     assertEquals(expected, actual, 0.0001);
   }
 }

 @Test
 public void testHypergeometricDistributionProbability() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   for (int i = 0; i <= 10; i++) {
     double actual = distribution.probability(i);
     double expected = Stats.binomialProbability(i, 10, 5.0);
     assertEquals(expected, actual, 0.0001);
   }
 }

 @Test
 public void testHypergeometricDistributionGetNumberOfSuccesses() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   assertEquals(5, distribution.getNumberOfSuccesses());
 }

 @Test
 public void testHypergeometricDistributionGetPopulationSize() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   assertEquals(10, distribution.getPopulationSize());
 }

 @Test
 public void testHypergeometricDistributionGetSampleSize() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   assertEquals(1, distribution.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionGetSupportLowerBound() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   int expected = 0;
   int actual = distribution.getSupportLowerBound();
   assertEquals(expected, actual);
 }

 @Test
 public void testHypergeometricDistributionGetSupportUpperBound() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   int expected = 5;
   int actual = distribution.getSupportUpperBound();
   assertEquals(expected, actual);
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   assertTrue(distribution.isSupportConnected());
 }

 @Test
 public void testHypergeometricDistributionGetNumericalMean() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   double expected = 5.0;
   double actual = distribution.getNumericalMean();
   assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testHypergeometricDistributionGetNumericalVariance() {
   distribution = new HypergeometricDistribution(10, 5, 1);
   double expected = 5.0 * (10.0 - 5.0) * (10.0 - 5.0) / (10.0 * 10.0 * (10.0 - 1.0));
   double actual = distribution.getNumericalVariance();
   assertEquals(expected, actual, 0.0001);
 }
}