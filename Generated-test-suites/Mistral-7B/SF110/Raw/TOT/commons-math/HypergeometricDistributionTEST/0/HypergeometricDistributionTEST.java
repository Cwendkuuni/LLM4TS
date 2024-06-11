import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution hd;
 // Test valid constructor with positive arguments
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 // Test invalid constructor with non-positive arguments
 try {
 hd = new HypergeometricDistribution(new Well19937c(), -5, 5, 5);
 } catch (NotPositiveException e) {
 // Expected
 }
 // Test invalid constructor with number of successes larger than population size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 11, 5);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 // Test invalid constructor with sample size larger than population size
 try {
 hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int[] domain = hd.getDomain();
 for (int x = domain[0]; x <= domain[1]; x++) {
 double p = hd.cumulativeProbability(x);
 System.out.println("Cumulative probability for x = " + x + " is " + p);
 assertEquals(p >= 0 && p <= 1, true);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 for (int x = 0; x <= hd.getSupportUpperBound(); x++) {
 double p = hd.probability(x);
 System.out.println("Probability for x = " + x + " is " + p);
 double sum = 0;
 for (int i = 0; i <= hd.getSupportUpperBound(); i++) {
 sum += p;
 p = hd.probability(i);
 assertEquals(sum + p == hd.cumulativeProbability(i + 1), true);
 }
 }
 }

 @Test
 public void testGetSupportLowerBoundAndUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int lowerBound = hd.getSupportLowerBound();
 int upperBound = hd.getSupportUpperBound();
 System.out.println("Support lower bound is " + lowerBound);
 System.out.println("Support upper bound is " + upperBound);
 assertEquals(lowerBound < 0, false);
 assertEquals(upperBound > hd.getPopulationSize(), false);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertTrue(hd.isSupportConnected());
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 double numericalVariance = hd.getNumericalVariance();
 System.out.println("Numerical variance is " + numericalVariance);
 assertTrue(numericalVariance > 0);
 }

}