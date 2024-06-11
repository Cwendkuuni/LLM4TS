import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 3, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected exception is NotPositiveException
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, -3, 5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected exception is NotPositiveException
 }
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorWithNegativeSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 3, -5);
 } catch (NotStrictlyPositiveException | NumberIsTooLargeException e) {
 // expected exception is NotPositiveException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 5);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // expected exception is NumberIsTooLargeException
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorWithSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 5, 6);
 } catch (NotPositiveException | NotStrictlyPositiveException e) {
 // expected exception is NumberIsTooLargeException
 }
 }

 @Test
 public void testHypergeometricDistributionGetters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 5);
 assertEquals(5, dist.getPopulationSize());
 assertEquals(3, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.0, dist.cumulativeProbability(0), 1e-12);
 assertEquals(0.1388888888888889, dist.cumulativeProbability(1), 1e-12);
 assertEquals(0.2857142857142857, dist.cumulativeProbability(2), 1e-12);
 assertEquals(0.4347826086956522, dist.cumulativeProbability(3), 1e-12);
 assertEquals(1.0, dist.cumulativeProbability(3), 1e-12);
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(0.0, dist.probability(0), 1e-12);
 assertEquals(0.0384615384615385, dist.probability(1), 1e-12);
 assertEquals(0.0769230769230769, dist.probability(2), 1e-12);
 assertEquals(0.1388888888888889, dist.probability(3), 1e-12);
 assertEquals(0.1388888888888889, dist.probability(4), 1e-12);
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(1.0, dist.upperCumulativeProbability(10), 1e-12);
 assertEquals(0.961111111111111, dist.upperCumulativeProbability(9), 1e-12);
 assertEquals(0.8709677419354838, dist.upperCumulativeProbability(8), 1e-12);
 assertEquals(0.7722222222222223, dist.upperCumulativeProbability(7), 1e-12);
 }

 @Test
 public void testHypergeometricDistributionSupportBounds() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(2, dist.getSupportLowerBound());
 assertEquals(3, dist.getSupportUpperBound());
 }

 @Test
 public void testHypergeometricDistributionIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertTrue(dist.isSupportConnected());
 }

 @Test
 public void testHypergeometricDistributionNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(3.0, dist.getNumericalMean(), 1e-12);
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 3, 5);
 assertEquals(2.94529863924651, dist.getNumericalVariance(), 1e-12);
 }
}