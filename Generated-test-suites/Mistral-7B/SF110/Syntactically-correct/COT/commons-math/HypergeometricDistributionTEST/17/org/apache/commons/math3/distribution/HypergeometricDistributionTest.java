import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.random.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 private HypergeometricDistribution distribution;

 @Test
 public void testHypergeometricDistributionConstruction() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(5, distribution.getPopulationSize());
   assertEquals(2, distribution.getNumberOfSuccesses());
   assertEquals(3, distribution.getSampleSize());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionNegativePopulationSize() {
  try {
   distribution = new HypergeometricDistribution(-1, 2, 3);
   fail("Should have thrown exception");
  } catch (NotStrictlyPositiveException e) {
   // Expected exception
  }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
  try {
   distribution = new HypergeometricDistribution(5, -1, 3);
   fail("Should have thrown exception");
  } catch (NotPositiveException e) {
   // Expected exception
  }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
  try {
   distribution = new HypergeometricDistribution(5, 6, 3);
   fail("Should have thrown exception");
  } catch (NumberIsTooLargeException e) {
   // Expected exception
  }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 6);
   fail("Should have thrown exception");
  } catch (NumberIsTooLargeException e) {
   // Expected exception
  }
 }

 @Test
 public void testCumulativeProbability() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   int[] domain = distribution.getDomain();
   for (int i = domain[0]; i <= domain[1]; i++) {
    assertEquals(distribution.cumulativeProbability(i), distribution.probability(i), 0.0001);
   }
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCumulativeProbabilityOutOfBounds() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   distribution.cumulativeProbability(-1);
   fail("Should have thrown exception");
  } catch (IndexOutOfBoundsException e) {
   // Expected exception
  }
 }

 @Test
 public void testProbability() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   int[] domain = distribution.getDomain();
   for (int i = domain[0]; i <= domain[1]; i++) {
    assertEquals(distribution.probability(i), distribution.cumulativeProbability(i + 1) - distribution.cumulativeProbability(i), 0.0001);
   }
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testProbabilityOutOfBounds() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   distribution.probability(-1);
   fail("Should have thrown exception");
  } catch (IndexOutOfBoundsException e) {
   // Expected exception
  }
 }

 @Test
 public void testGetDomain() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   int[] domain = distribution.getDomain();
   assertEquals(5, domain[0]);
   assertEquals(4, domain[1]);
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetLowerDomain() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(3, distribution.getLowerDomain(5, 2, 3));
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetUpperDomain() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(2, distribution.getUpperDomain(5, 2));
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetNumberOfSuccesses() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(2, distribution.getNumberOfSuccesses());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetPopulationSize() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(5, distribution.getPopulationSize());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetSampleSize() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(3, distribution.getSampleSize());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetSupportLowerBound() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(-1, distribution.getSupportLowerBound());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetSupportUpperBound() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(1, distribution.getSupportUpperBound());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testIsSupportConnected() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertTrue(distribution.isSupportConnected());
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetNumericalMean() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   assertEquals(1.6, distribution.getNumericalMean(), 0.01);
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testGetNumericalVariance() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   double numericalVariance = distribution.getNumericalVariance();
   assertEquals(0.16666666666666668, numericalVariance, 0.0000001);
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testCalculateNumericalVariance() {
  try {
   distribution = new HypergeometricDistribution(5, 2, 3);
   double numericalVariance = distribution.calculateNumericalVariance();
   assertEquals(0.16666666666666668, numericalVariance, 0.0000001);
  } catch (Exception e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }
}