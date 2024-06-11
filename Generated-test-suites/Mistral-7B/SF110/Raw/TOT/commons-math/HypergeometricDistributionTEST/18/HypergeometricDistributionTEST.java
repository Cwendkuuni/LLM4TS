import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 1, 3);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // Expected NotPositiveException
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionWithNonPositiveNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -2, 3);
 } catch (NotPositiveException | NumberIsTooLargeException e) {
 // Expected NotStrictlyPositiveException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 3);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // Expected NumberIsTooLargeException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, 6);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // Expected NumberIsTooLargeException
 }
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.0, dist.cumulativeProbability(0), 0.001);
 assertEquals(0.375, dist.cumulativeProbability(2), 0.001);
 assertEquals(0.8125, dist.cumulativeProbability(4), 0.001);
 assertEquals(1.0, dist.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.1875, dist.probability(2), 0.001);
 assertEquals(0.2344, dist.probability(3), 0.001);
 assertEquals(0.1875, dist.probability(4), 0.001);
 assertEquals(0.1875, dist.probability(5), 0.001);
 }

 @Test
 public void testHypergeometricDistributionGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getNumberOfSuccesses());
 }

 @Test
 public void testHypergeometricDistributionGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testHypergeometricDistributionGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(2, dist.getSupportLowerBound());
 }

 @Test
 public void testHypergeometricDistributionGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3, dist.getSupportUpperBound());
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(dist.isSupportConnected());
 }
}