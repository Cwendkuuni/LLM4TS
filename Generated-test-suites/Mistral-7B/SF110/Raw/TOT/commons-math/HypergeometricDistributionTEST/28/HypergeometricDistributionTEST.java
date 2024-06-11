import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativePopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), -5, 2, 3);
 fail("Exception should be thrown");
 } catch (NotPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 0, 3);
 fail("Exception should be thrown");
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroSampleSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 2, 0);
 fail("Exception should be thrown");
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 6, 3);
 fail("Exception should be thrown");
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
 try {
 new HypergeometricDistribution(new Well19937c(), 5, 2, 6);
 fail("Exception should be thrown");
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.0, hd.cumulativeProbability(4), 0.001);
 assertEquals(0.5, hd.cumulativeProbability(5), 0.001);
 assertEquals(1.0, hd.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.166666667, hd.probability(3), 0.0001);
 assertEquals(0.230769231, hd.probability(4), 0.0001);
 assertEquals(0.166666667, hd.probability(5), 0.0001);
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(0.5, hd.upperCumulativeProbability(4), 0.001);
 assertEquals(0.833333333, hd.upperCumulativeProbability(7), 0.001);
 assertEquals(1.0, hd.upperCumulativeProbability(5), 0.001);
 }

 @Test
 public void testHypergeometricDistributionGetNumberOfSuccesses() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getNumberOfSuccesses());
 }

 @Test
 public void testHypergeometricDistributionGetPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(10, hd.getPopulationSize());
 }

 @Test
 public void testHypergeometricDistributionGetSampleSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5, hd.getSampleSize());
 }

 @Test
 public void testHypergeometricDistributionGetNumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(5.0, hd.getNumericalMean(), 0.001);
 }

 @Test
 public void testHypergeometricDistributionGetNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 assertEquals(3.125, hd.getNumericalVariance(), 0.001);
 }
}